package me.demo.rpc.netty.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import me.demo.common.helper.StringHelper;
import me.demo.common.helper.SystemHelper;
import me.demo.rpc.netty.protocol.RequestDecoder;
import me.demo.rpc.netty.protocol.ResponseEncoder;
import me.demo.rpc.netty.sdc.SDCHelper;

public class RPCServer {
    static Logger logger = Logger.getLogger(RPCServer.class.getName());
    /**
     * 服务端监听的端口地址
     */
    static InetSocketAddress addrs[] = new InetSocketAddress[]{new InetSocketAddress("0.0.0.0", 6878)};
    static volatile int idleInterval = 60000;
    static volatile int expire_time = 120;
    static ExecutorService threadPool = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    private static String rpcServiceName;

    public static void main(String[] args) throws IOException, JSONException {
        try {
            loadConfigFromSdc(args[0]);
            startServer();
        } catch (Throwable e) {
//            logger.fatal("start rpc server failure:", e);
            System.exit(-1);
        }
    }

    /**
     * 从共享数据中心获取配置：json格式：{idle:30,bind_addr:'0.0.0.0',bind_port:6878,lib_home:'',service_home:'',
     * expire_time:60}
     *
     * @param name
     * @throws IOException
     * @throws JSONException
     */
    private static void loadConfigFromSdc(String name) throws IOException, JSONException, ClassNotFoundException {
        rpcServiceName = name;
        String value = SDCHelper.get("/rpcservice/" + name);
        JSONObject json = JSON.parseObject(value);

        if (json.containsKey("idle"))
            idleInterval = json.getInteger("idle") * 1000;
        if (json.containsKey("expire_time"))
            expire_time = json.getInteger("expire_time");
        Integer[] ps = StringHelper.splitToIntArray(json.getString("bind_port"), ",");
        if (ps.length == 1) {
            addrs = new InetSocketAddress[1];
            addrs[0] = new InetSocketAddress(json.getString("bind_addr"), ps[0]);
        } else {
            addrs = new InetSocketAddress[ps[1] - ps[0] + 1];
            for (int i = ps[0]; i <= ps[1]; i++)
                addrs[i - ps[0]] = new InetSocketAddress(json.getString("bind_addr"), i);
        }
        ServicePool.defaultInstance.load(json.getString("service_home"), json.getString("lib_home"));
    }

    private static void startServer() throws SocketException, InterruptedException {
        for (int i = 0; i < addrs.length; i++) {
            final InetSocketAddress addr = addrs[i];
            final String addrstr = getHostAddress(addr);
            final AtomicBoolean error = new AtomicBoolean(false);
            final CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup);
                    b.channel(NioServerSocketChannel.class);
                    b.childHandler(new ServerInitializer(threadPool, idleInterval));
                    b.option(ChannelOption.SO_REUSEADDR, true);
                    b.option(ChannelOption.SO_KEEPALIVE, true);
                    b.option(ChannelOption.TCP_NODELAY, true);
                    ChannelFuture f = b.bind(addr.getAddress(), addr.getPort()).sync();
                    latch.countDown();

                    logger.info("push server started, listen: " + addr.getAddress() + ":" + addr.getPort());
                    register(addrstr, addr.getPort(), false);
                    f.channel().closeFuture().sync();
                } catch (Throwable e) {
                    error.set(true);
                    latch.countDown();
                } finally {
                    if (!error.get())
                        unregister(addrstr, addr.getPort());
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }).start();
            latch.await();
            if (!error.get()) {
                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(idleInterval);
                        } catch (InterruptedException e) {
                            break;
                        }
                        try {
                            register(addrstr, addr.getPort(), true);
                        } catch (Throwable e) {
                            System.out.println("update rpc server info failure:" + e);
                        }
                    }
                }).start();
                break;
            } else if (i == (addrs.length - 1))
                System.out.println("启动失败，所有端口都被占用。");
        }
    }

    private static String getHostAddress(InetSocketAddress addr) throws SocketException {
        String addrstr = addr.getAddress().getHostAddress();
        if (addrstr.equals("0.0.0.0"))
            addrstr = SystemHelper.getLocalHostAddress();
        return addrstr;
    }

    private static void register(String addr, int port, boolean update) throws IOException {
        String key = String.format("/rpcservice/%s/%s:%d", rpcServiceName, addr, port);
        if (update) {
            try {
                SDCHelper.putExpire(key, expire_time, false);
            } catch (Throwable e) {
                update = false;
            }
        }
        if (!update) {
            //更新客户端配置信息，格式：{'idle':30,'connections_per_server':1,'connection_timeout':10}
            int idle = idleInterval / 2;
            if (idle < 3000) idle = 3000;
            String clientConfig = String.format("{\"idle\":%d,\"connections_per_server\":1,\"connection_timeout\":10}", idle);
            SDCHelper.put(key, clientConfig, expire_time);
        }
    }

    private static void unregister(String addr, int port) {
        try {
            String key = String.format("/rpcservice/%s/%s:%d", rpcServiceName, addr, port);
            SDCHelper.delete(key);
        } catch (Throwable e) {
//            logger.warn(String.format("unregister %s:%d failure:", addr, port), e);
        }
    }
}

class ServerInitializer extends ChannelInitializer<SocketChannel> {
    ExecutorService threadPool;
    int idleInterval;

    public ServerInitializer(ExecutorService threadPool, int idleInterval) {
        super();
        this.threadPool = threadPool;
        this.idleInterval = idleInterval;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
        pipeline.addLast("decoder", new RequestDecoder());
        pipeline.addLast("encoder", new ResponseEncoder());
        pipeline.addLast("idleStateHandler", new IdleStateHandler(idleInterval, 0, 0));
        pipeline.addLast("sessionhandler", new SessionHandler(threadPool));
    }
}
