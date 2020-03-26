package me.demo.rpc.netty.pools;

import java.util.concurrent.CountDownLatch;

public class AsyncConnectionSendErrorPack<K> implements IAsyncConnectionPack<K> {
	IAsyncConnectionPack<K> reqPack;
	Throwable error;
	long creationTime = System.currentTimeMillis();

	public AsyncConnectionSendErrorPack(IAsyncConnectionPack<K> reqPack, Throwable e) {
		this.reqPack = reqPack;
		this.error = e;
	}

	@Override
	public AsyncConnection<K> getConnection() {
		return reqPack.getConnection();
	}

	@Override
	public void setConnection(AsyncConnection<K> con) {
		throw new UnsupportedOperationException();
	}

	@Override
	public K getPackId() {
		return reqPack.getPackId();
	}

	@Override
	public IAsyncConnectionPack<K> getRecvPack() {
		return null;
	}

	@Override
	public boolean isRequestPack() {
		return false;
	}

	@Override
	public boolean isIdlePack() {
		return false;
	}

	@Override
	public void setRecvPack(IAsyncConnectionPack<K> pack) {
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	public Throwable getError() {
		return error;
	}

	@Override
	public CountDownLatch getCountDownLatch() {
		return null;
	}

	@Override
	public void setCountDownLatch(CountDownLatch latch) {
	}

	@Override
	public long getCompletionTime() {
		return creationTime;
	}

	@Override
	public void setCompletionTime(long time) {
		creationTime = time;
	}

}
