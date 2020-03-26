package me.demo.rpc.netty.pools;

import java.util.concurrent.CountDownLatch;

/**
 * 异步连接包
 */
public interface IAsyncConnectionPack<K> {
    AsyncConnection<K> getConnection();

    void setConnection(AsyncConnection<K> con);

    K getPackId();

    IAsyncConnectionPack<K> getRecvPack();

    /**
     * 返回当前是否是请求包
     */
    boolean isRequestPack();

    /**
     * 返回当前是否是心跳包
     */
    boolean isIdlePack();

    void setRecvPack(IAsyncConnectionPack<K> pack);

    CountDownLatch getCountDownLatch();

    void setCountDownLatch(CountDownLatch latch);

    long getCreationTime();

    long getCompletionTime();

    void setCompletionTime(long time);

}
