package me.demo.rpc.netty.client;


import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import me.demo.rpc.netty.pools.AsyncNotifyListener;
import me.demo.rpc.netty.pools.IAsyncConnectionPack;
import me.demo.rpc.netty.pools.tcp.AsyncTcpConnectionPool;

public class RPCTcpConnectionPool extends AsyncTcpConnectionPool<Integer, RPCTcpConnection> {
//    final static Logger recvSendlogger = Logger.getLogger("rpc.sendreceive", "biss", "rpc");
    ConcurrentHashMap<String, Object> proxyMap = new ConcurrentHashMap<String, Object>();

    public RPCTcpConnectionPool() {
    }

    public RPCTcpConnectionPool(AsyncNotifyListener<Integer> listener, int idleInterval, int connectTimeout,
                                int dataTimeout) {
        super(null, listener, idleInterval, connectTimeout, dataTimeout);
        
    }

    public RPCTcpConnectionPool(BlockingQueue<IAsyncConnectionPack<Integer>> sendQueue,
                                ConcurrentHashMap<Integer, IAsyncConnectionPack<Integer>> recvPackMap,
                                AsyncNotifyListener<Integer> listener, int idleInterval, int connectTimeout, int dataTimeout) {
        super(null, sendQueue, recvPackMap, listener, idleInterval, connectTimeout, dataTimeout);
        
    }

    public RPCTcpConnectionPool(int idleInterval, int connectTimeout, int dataTimeout) {
        super(null, idleInterval, connectTimeout, dataTimeout);
        
    }

    public RPCTcpConnectionPool(String name, int idleInterval, int connectTimeout, int dataTimeout) {
        super(name, idleInterval, connectTimeout, dataTimeout);
        
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T getRemoteService(String serviceName, Class<T> serviceCls) {
        T proxy = (T) proxyMap.get(serviceName);
        if (proxy == null) {
            LocalServiceStub wraper = new LocalServiceStub(this, serviceName);
            proxy = (T) wraper.getProxy(serviceCls);
            proxyMap.put(serviceName, proxy);
        } else {
            //logger.debug("from cache");
        }
        return proxy;
    }

    public void removeConnections(InetSocketAddress address) {
        for (int i = 0; i < connections.size(); i++) {
            RPCTcpConnection con = (RPCTcpConnection) connections.get(i);
            if (con.getAddress().equals(address)) {
                remove(con);
                i--;
            }
        }
    }


    public void addConnections(InetSocketAddress address, int totalServers) {
        for (int i = 0; i < connections.size(); i++) {
            RPCTcpConnection con = (RPCTcpConnection) connections.get(i);
            if (con.getAddress().equals(address)) {
                remove(con);
            }
        }
        for (int i = 0; i < totalServers; i++)
            add(new RPCTcpConnection(address, this));
    }
}