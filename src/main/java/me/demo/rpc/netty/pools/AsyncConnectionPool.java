package me.demo.rpc.netty.pools;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import me.demo.common.exceptions.ConnectException;
import me.demo.common.exceptions.TimeoutException;

/**
 * 异步连接池
 *

 */
public class AsyncConnectionPool<K, C extends AsyncConnection<K>> implements IAsyncConnectionPool<C> {
    protected volatile String name;
    protected CopyOnWriteArrayList<C> connections = new CopyOnWriteArrayList<C>();
    /**
     * 发送数据包
     */
    protected BlockingQueue<IAsyncConnectionPack<K>> sendQueue;
    /**
     * 接收包MAP
     */
    protected ConcurrentHashMap<K, IAsyncConnectionPack<K>> recvPackMap;
    /**
     * 通知侦听器
     */
    protected AsyncNotifyListener<K> listener = null;
    /**
     * 心跳间隔，毫秒
     */
    protected volatile int idleInterval = 30000;
    /**
     * 连接超时
     */
    protected int connectTimeout = 10000;
    protected volatile int dataTimeout = 30000;

    public AsyncConnectionPool() {
    }

    public AsyncConnectionPool(String name, int idleInterval, int connectTimeout, int dataTimeout) {
        this(name, new LinkedBlockingQueue<IAsyncConnectionPack<K>>(),
                new ConcurrentHashMap<K, IAsyncConnectionPack<K>>(), null, idleInterval, connectTimeout, dataTimeout);
    }

    public AsyncConnectionPool(String name, AsyncNotifyListener<K> listener, int idleInterval, int connectTimeout,
                               int dataTimeout) {
        this(name, new LinkedBlockingQueue<IAsyncConnectionPack<K>>(),
                new ConcurrentHashMap<K, IAsyncConnectionPack<K>>(), listener, idleInterval, connectTimeout,
                dataTimeout);
    }

    public AsyncConnectionPool(String name, BlockingQueue<IAsyncConnectionPack<K>> sendQueue,
                               ConcurrentHashMap<K, IAsyncConnectionPack<K>> recvPackMap, AsyncNotifyListener<K> listener,
                               int idleInterval, int connectTimeout, int dataTimeout) {
        super();
        this.name = name;
        this.sendQueue = sendQueue;
        this.recvPackMap = recvPackMap;
        this.listener = listener;
        this.idleInterval = idleInterval;
        this.connectTimeout = connectTimeout;
        this.dataTimeout = dataTimeout;
    }


    /**
     * 构建一个连接池，参数从配置文件中读取
     *
     * @param config json配置结点
     */
    public AsyncConnectionPool(String name, JSONObject config, BlockingQueue<IAsyncConnectionPack<K>> sendQueue,
                               ConcurrentHashMap<K, IAsyncConnectionPack<K>> recvPackMap, AsyncNotifyListener<K> listener) throws JSONException {
        this.name = name;
        if (config.containsKey("connectTimeout"))
            connectTimeout = Integer.valueOf(config.getString("connectTimeout")) * 1000;
        if (config.containsKey("keepAliveInterval")) {
            idleInterval = Integer.valueOf(config.getString("keepAliveInterval"));
            dataTimeout = idleInterval;
        }
        if (config.containsKey("dataTimeout")) {
            dataTimeout = Integer.valueOf(config.getString("dataTimeout")) * 1000;
        }
        this.sendQueue = sendQueue;
        this.recvPackMap = recvPackMap;
        this.listener = listener;
    }

    public void add(C con) {
        connections.add(con);
        con.start();
    }

    public void remove(C con) {
        connections.remove(con);
        con.stop();
    }

    public BlockingQueue<IAsyncConnectionPack<K>> getSendQueue() {
        return sendQueue;
    }

    public void setSendQueue(BlockingQueue<IAsyncConnectionPack<K>> sendQueue) {
        this.sendQueue = sendQueue;
    }

    public ConcurrentHashMap<K, IAsyncConnectionPack<K>> getRecvPackMap() {
        return recvPackMap;
    }

    public void setRecvPackMap(ConcurrentHashMap<K, IAsyncConnectionPack<K>> recvPackMap) {
        this.recvPackMap = recvPackMap;
    }

    public AsyncNotifyListener<K> getListener() {
        return listener;
    }

    public void setListener(AsyncNotifyListener<K> listener) {
        this.listener = listener;
    }

    public int getIdleInterval() {
        return idleInterval;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    protected IAsyncConnectionPack<K> onRecevie(IAsyncConnectionPack<K> pack) {
        if (pack != null) {
            if (!pack.isRequestPack()) {
                IAsyncConnectionPack<K> p = recvPackMap.get(pack.getPackId());
                if (p != null) {
                    p.setRecvPack(pack);
                    if (p.getCountDownLatch() != null)
                        p.getCountDownLatch().countDown();
                    return pack;
                }
            }
            if (listener != null)
                listener.onReceive(pack);
        }
        return pack;
    }

    /**
     * 获取响应包
     *
     * @param pack
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected IAsyncConnectionPack<K> getResponsePack(IAsyncConnectionPack<K> pack)
            throws IOException, InterruptedException {
        try {
            pack.getCountDownLatch().await(getDataTimeout(), TimeUnit.MILLISECONDS);
        } finally {
            recvPackMap.remove(pack.getPackId());
        }
        if (pack.getRecvPack() == null)
            throw new TimeoutException("operation timeout[packId=" + pack.getPackId() + "]");
        else {
            if (pack.getRecvPack() instanceof AsyncConnectionSendErrorPack) {// 发送数据失败
                AsyncConnectionSendErrorPack<K> rp = (AsyncConnectionSendErrorPack<K>) pack.getRecvPack();
                if (rp.getError() instanceof IOException)
                    throw (IOException) rp.getError();
                else if (rp.getError() instanceof InterruptedException)
                    throw (InterruptedException) rp.getError();
                else
                    throw new IOException(rp.getError());
            } else
                return pack.getRecvPack();
        }
    }

    public void send(IAsyncConnectionPack<K> pack) {
//        if (logger != null && !pack.isIdlePack())
//            logger.debug(getConnectionUrl() + " => " + pack + " add queue.");
        if (pack.getCountDownLatch() == null)
            pack.setCountDownLatch(new CountDownLatch(1));
        sendQueue.add(pack);
    }

    protected void doOpenConnectionBegin(AsyncConnection<K> connection) {
        if (listener != null)
            listener.onOpenConnectionBegin(connection);
    }

    protected void doOpenConnectionEnd(AsyncConnection<K> connection) {
        if (listener != null)
            listener.onOpenConnectionEnd(connection);
    }

    /**
     * 向指定连接，执行一个命令
     *
     * @param pack
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public IAsyncConnectionPack<K> execute(AsyncConnection<K> con, IAsyncConnectionPack<K> pack)
            throws IOException, InterruptedException {
        recvPackMap.put(pack.getPackId(), pack);
//        if (logger != null && !pack.isIdlePack())
//            logger.debug(getConnectionUrl() + " => " + pack + " has been sent.");
        if (pack.getCountDownLatch() == null)
            pack.setCountDownLatch(new CountDownLatch(1));
        con.send(pack);
        return getResponsePack(pack);
    }

    /**
     * 执行一个命令
     *
     * @param pack
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public IAsyncConnectionPack<K> execute(IAsyncConnectionPack<K> pack) throws IOException, InterruptedException {
//        if (logger != null && !pack.isIdlePack())
//            logger.debug(getConnectionUrl() + " => " + pack + " add queue.");
        if (pack.getCountDownLatch() == null)
            pack.setCountDownLatch(new CountDownLatch(1));
        recvPackMap.put(pack.getPackId(), pack);
        sendQueue.add(pack);
        return getResponsePack(pack);
    }

    /**
     * 执行一批命令
     *
     * @param packs
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public void execute(Collection<IAsyncConnectionPack<K>> packs) throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(packs.size());
        try {
            for (IAsyncConnectionPack<K> pack : packs) {
                pack.setCountDownLatch(latch);
                getRecvPackMap().put(pack.getPackId(), pack);
                send(pack);
            }
            latch.await(getDataTimeout(), TimeUnit.MILLISECONDS);
        } finally {
            for (IAsyncConnectionPack<K> pack : packs)
                getRecvPackMap().remove(pack.getPackId());
        }
    }

    public int getDataTimeout() {
        return dataTimeout;
    }

    @Override
    public void clean() {
    }

    @Override
    public Object getConnectionUrl() {
        return null;
    }

    AtomicInteger currenIndex = new AtomicInteger(0);

    @Override
    public C getConnection(Object caller) throws InterruptedException, ConnectException {
        if (connections.isEmpty())
            return null;
        int index = currenIndex.getAndAdd(1);
        if (index >= connections.size()) {
            currenIndex.set(0);
            index = 0;
        }
        return connections.get(index);
    }

    public String getName() {
        return name;
    }

    public int getConnectionSize() {
        return connections.size();
    }
}
