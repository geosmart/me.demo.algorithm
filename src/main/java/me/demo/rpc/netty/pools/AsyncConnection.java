package me.demo.rpc.netty.pools;


import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.demo.common.exceptions.ConnectException;

class AsyncReadThread implements Runnable {
    AsyncConnection<?> con;

    public AsyncReadThread(AsyncConnection<?> con) {
        super();
        this.con = con;
    }

    @Override
    public void run() {
        con.processReadThread();
    }

}

class AsyncWriteThread implements Runnable {
    AsyncConnection<?> con;

    public AsyncWriteThread(AsyncConnection<?> con) {
        super();
        this.con = con;
    }

    @Override
    public void run() {
        con.processWriteThread();
    }

}

/**
 * 异步连接
 *

 */
public abstract class AsyncConnection<K> implements IAsyncConnection {
    protected boolean started = false;
    protected volatile boolean destorying = false;
    CountDownLatch connectDownLatch = null;
    protected AsyncConnectionPool<K, ? extends IAsyncConnection> pool;
    //    final static Logger logger = Logger.getLogger(AsyncConnection.class, "biss", "pools");
    protected Thread readThread, writeThread;

    public AsyncConnection(AsyncConnectionPool<K, ? extends IAsyncConnection> pool) {
        super();
        this.pool = pool;
    }

    protected void setThreadPriority() {
        writeThread.setPriority(Thread.MAX_PRIORITY);
        readThread.setPriority(Thread.MAX_PRIORITY);
    }

    public synchronized AsyncConnection<K> start() {
        if (started)
            return this;
        started = true;
        writeThread = new Thread(new AsyncWriteThread(this));
        writeThread.start();
        readThread = new Thread(new AsyncReadThread(this));
        readThread.start();
        setThreadPriority();
        return this;
    }

    public synchronized void stop() {
        if (started) {
            started = false;
            writeThread.interrupt();
            readThread.interrupt();
            try {
                destroy();
            } catch (Throwable e) {
            }
        }
    }

    @Override
    public String toString() {
        return pool != null ? pool.getConnectionUrl() + "" : "";
    }

    /**
     * 处理写数据，从发送队列中读取数据发送
     */
    protected void processWriteThread() {
        try {
            create();
        } catch (ConnectException e) {
            if (e.getCause() instanceof InterruptedException)
                return;
        } catch (Throwable e) {
        }
        boolean firstDebugConnectError = true;
        IAsyncConnectionPack<K> savedPack = null;
        while (!Thread.currentThread().isInterrupted()) {
            IAsyncConnectionPack<K> pack = null;
            try {
                //先确保连接建立
                if (isOpened()) {
                    if (savedPack == null) {
                        int timeout = isOpened() ? pool.idleInterval : pool.connectTimeout;
                        pack = pool.sendQueue.poll(timeout, TimeUnit.MILLISECONDS);
                    } else
                        pack = savedPack;
                    if (pack == null) {
                        if (!isOpened()) {
                            create();
                            firstDebugConnectError = true;
                        } else
                            pack = createIdlePack();
                    } else {
                        create();
                        firstDebugConnectError = true;
                    }
                    if (pack != null) {
                        savedPack = null;
                        pack.setCompletionTime(System.currentTimeMillis());
                        send(pack);
                    }
                } else {
                    create();
                    firstDebugConnectError = true;
                }
            } catch (ConnectException e) {
                if (pack != null && !pack.isIdlePack())
                    savedPack = pack;
                if (firstDebugConnectError) {
                    firstDebugConnectError = false;
//                    logger.error("connected error:", e);
                }
            } catch (InterruptedException e) {
                break;
            } catch (Throwable e) {
//                logger.error("send error(" + pack + ")", e);
            }
        }
//        logger.debug("write thread exited.");
    }

    /**
     * 创建心跳包
     *
     * @return
     * @throws IOException
     */
    protected abstract IAsyncConnectionPack<K> createIdlePack() throws IOException;

    /**
     * 检查当前连接是否打开
     *
     * @return
     */
    protected abstract boolean isOpened();

    public boolean isClosed() {
        return !isOpened();
    }

    /**
     * 处理读数据线程，接收服务端的数据，并放到接收包MAP中
     */
    protected void processReadThread() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                IAsyncConnectionPack<K> pack = recv();
                if (pack != null) {
                    if (!pack.isIdlePack()) {
//                        pool.getSendReceiveLogger().debug(pack + " has been received.");
                    }
                    pack.setConnection(this);
                    pack.setCompletionTime(System.currentTimeMillis());
                    pool.onRecevie(pack);
                }
            } catch (InterruptedException e) {
                break;
            } catch (Throwable e) {
            }
        }
//        logger.debug("read thread exited.");
    }

    protected synchronized void create() throws ConnectException {
        if (isOpened())
            return;
        try {
            connectDownLatch = new CountDownLatch(1);
            try {
                doCreateBegin();
                doCreate();
            } finally {
                connectDownLatch.countDown();
                connectDownLatch = null;
            }
            doCreateEnd();
//            logger.info("connection created[" + this + "]");
        } catch (ConnectException e) {
            throw e;
        } catch (Throwable e) {
            throw new ConnectException(e);
        }
    }

    /**
     * 处理连接之前要做的前期处理
     */
    protected void doCreateBegin() throws IOException, InterruptedException {
        pool.doOpenConnectionBegin(this);
    }

    /**
     * 处理连接成功之后要做的后续处理
     */
    protected void doCreateEnd() throws IOException, InterruptedException {
        pool.doOpenConnectionEnd(this);
    }

    protected synchronized void destroy() throws IOException, InterruptedException {
        if (!isOpened())
            return;
        destorying = true;
        try {
            doDestory();
        } catch (Throwable e) {
        } finally {
            destorying = false;
        }
    }

    /**
     * 建立连接
     *
     * @throws IOException
     * @throws InterruptedException
     */
    protected abstract void doCreate() throws IOException, InterruptedException;

    /**
     * 关闭连接
     *
     * @throws IOException
     * @throws InterruptedException
     */
    protected abstract void doDestory() throws IOException, InterruptedException;

    /**
     * 发送一个数据包，不要直接使用此函数，应该用send
     *
     * @param pack
     */
    protected abstract void doSend(IAsyncConnectionPack<K> pack) throws IOException, InterruptedException;

    /**
     * 接收一个数据包，不要直接使用此函数，应该用recv
     *
     * @return
     * @throws IOException
     */
    protected abstract IAsyncConnectionPack<K> doRecv() throws IOException, InterruptedException;

    protected synchronized void send(IAsyncConnectionPack<K> pack) throws IOException, InterruptedException {
        if (!isOpened()) {
            if (!pack.isIdlePack())
//                pool.getSendReceiveLogger()
//                        .error(this.toString() + " => " + pack + " send failed: connection is not opened");
                throw new IOException("connection is not opened");
        }
        pack.setConnection(this);
        try {
            doSend(pack);
            if (!pack.isIdlePack()) {

            }
//                pool.getSendReceiveLogger().debug(this.toString() + " => " + pack + " has been sent.");
        } catch (IOException e) {
            if (!pack.isIdlePack())
//                pool.getSendReceiveLogger().error(this.toString() + " => " + pack + " send failed: ", e);
                notifyErrorSendPack(pack, e);
            throw e;
        } catch (InterruptedException e) {
            if (!pack.isIdlePack())
//                pool.getSendReceiveLogger().error(this.toString() + " => " + pack + " send failed: ", e);
                notifyErrorSendPack(pack, e);
            throw e;
        } catch (Throwable e) {
            if (!pack.isIdlePack())
//                pool.getSendReceiveLogger().error(this.toString() + " => " + pack + " send failed: ", e);
                notifyErrorSendPack(pack, e);
            throw new IOException(e);
        }
    }

    protected void notifyErrorSendPack(IAsyncConnectionPack<K> reqPack, Throwable e) {
        if (reqPack != null && !reqPack.isIdlePack()) {
            IAsyncConnectionPack<K> p = pool.recvPackMap.get(reqPack.getPackId());
            if (p != null && p == reqPack) {
                IAsyncConnectionPack<K> rp = new AsyncConnectionSendErrorPack<K>(reqPack, e);
                p.setRecvPack(rp);
                if (p.getCountDownLatch() != null)
                    p.getCountDownLatch().countDown();
            }
        }
    }

    protected IAsyncConnectionPack<K> recv() throws IOException, InterruptedException, TimeoutException {
        if (!isOpened()) {
            if (connectDownLatch != null)
                connectDownLatch.await(pool.idleInterval, TimeUnit.MILLISECONDS);
            if (!isOpened())
                throw new TimeoutException("operation timeout");
        }
        return doRecv();
    }

    public boolean isStarted() {
        return started;
    }

    public AsyncConnectionPool<K, ? extends IAsyncConnection> getPool() {
        return pool;
    }

    @Override
    public void open() throws ConnectException {
        create();
    }

    /**
     * 异步连接，不做任何事
     */
    @Override
    public void close() {
    }

}
