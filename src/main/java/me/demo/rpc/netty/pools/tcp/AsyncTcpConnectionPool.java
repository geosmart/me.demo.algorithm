package me.demo.rpc.netty.pools.tcp;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import me.demo.common.helper.StringHelper;
import me.demo.rpc.netty.pools.AsyncConnection;
import me.demo.rpc.netty.pools.AsyncConnectionPool;
import me.demo.rpc.netty.pools.AsyncNotifyListener;
import me.demo.rpc.netty.pools.IAsyncConnectionPack;
import me.demo.common.exceptions.IncorrectConfigurationError;

/**
 * tcp连接池
 *
 * @param <C>



 */
abstract public class AsyncTcpConnectionPool<K, C extends AsyncConnection<K>> extends AsyncConnectionPool<K, C> {
    protected InetSocketAddress[] addressList;
    protected int soTimeout;
    protected boolean noDelay;

    public AsyncTcpConnectionPool() {
        super();
    }

    public AsyncTcpConnectionPool(String name, AsyncNotifyListener<K> listener, int idleInterval, int connectTimeout,
                                  int dataTimeout) {
        super(name, listener, idleInterval, connectTimeout, dataTimeout);
    }

    public AsyncTcpConnectionPool(String name, BlockingQueue<IAsyncConnectionPack<K>> sendQueue,
                                  ConcurrentHashMap<K, IAsyncConnectionPack<K>> recvPackMap, AsyncNotifyListener<K> listener,
                                  int idleInterval, int connectTimeout, int dataTimeout) {
        super(name, sendQueue, recvPackMap, listener, idleInterval, connectTimeout, dataTimeout);
    }

    public AsyncTcpConnectionPool(String name, int idleInterval, int connectTimeout, int dataTimeout) {
        super(name, idleInterval, connectTimeout, dataTimeout);
    }

    public AsyncTcpConnectionPool(String name, JSONObject config, BlockingQueue<IAsyncConnectionPack<K>> sendQueue,
                                  ConcurrentHashMap<K, IAsyncConnectionPack<K>> recvPackMap, AsyncNotifyListener<K> listener) throws JSONException {
        super(name, config, sendQueue, recvPackMap, listener);
        if (config.containsKey("dataTimeout"))
            dataTimeout = Integer.valueOf(config.getString("dataTimeout")) * 1000;
        if (config.containsKey("soTimeout"))
            soTimeout = Integer.valueOf(config.getString("soTimeout")) * 1000;
        // 读取连接字串，可以设置一主多从
        String urls = config.getString("connectionUrls");
        String[] s = StringHelper.splitToStringArray(urls, ",");
        addressList = new InetSocketAddress[s.length];
        int i = 0;
        for (String str : s) {
            String[] ss = StringHelper.splitToStringArray(str, ":");
            if (ss.length >= 2)
                addressList[i++] = new InetSocketAddress(ss[0], Integer.valueOf(ss[1]));
        }
        for (InetSocketAddress a : addressList) {
            if (a == null)
                throw new IncorrectConfigurationError(
                        "config 'connectionUrls' error: " + urls + ", className:" + this.getClass().getName());
        }
    }

    @Override
    public Object getConnectionUrl() {
        if (addressList == null || addressList.length < 1)
            return "";
        String ret = addressList[0].getAddress().getHostAddress() + ":" + addressList[0].getPort();
        return ret;
    }

    public InetSocketAddress getAddress() {
        return addressList[0];
    }

    public void setAddress(InetSocketAddress address) {
        if (this.addressList == null)
            this.addressList = new InetSocketAddress[1];
        this.addressList[0] = address;
    }

    public InetSocketAddress[] getAddressList() {
        return addressList;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public boolean isNoDelay() {
        return noDelay;
    }

    public void setNoDelay(boolean noDelay) {
        this.noDelay = noDelay;
    }

}
