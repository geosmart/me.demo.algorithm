package me.demo.rpc.netty.pools;

import java.util.concurrent.CountDownLatch;

abstract public class AbstractAsyncConnectionPack<K> implements IAsyncConnectionPack<K> {
	AsyncConnection<K> connection;
	long creationTime = System.currentTimeMillis();
	long completionTime = 0;
	IAsyncConnectionPack<K> recvPack = null;

	public AbstractAsyncConnectionPack() {
	}

	public AbstractAsyncConnectionPack(AsyncConnection<K> connection) {
		super();
		this.connection = connection;
	}

	public AsyncConnection<K> getConnection() {
		return connection;
	}

	public void setConnection(AsyncConnection<K> connection) {
		this.connection = connection;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public long getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(long completionTime) {
		this.completionTime = completionTime;
	}

	CountDownLatch countDownLatch = null;

	@Override
	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	@Override
	public void setCountDownLatch(CountDownLatch latch) {
		this.countDownLatch = latch;
	}

	public IAsyncConnectionPack<K> getRecvPack() {
		return recvPack;
	}

	public void setRecvPack(IAsyncConnectionPack<K> recvPack) {
		this.recvPack = recvPack;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
}
