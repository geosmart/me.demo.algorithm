package me.demo.rpc.netty.pools.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import me.demo.rpc.netty.pools.AsyncConnection;
import me.demo.rpc.netty.pools.AsyncConnectionPool;
import me.demo.common.exceptions.ConnectException;
import me.demo.rpc.netty.io.DataReadStream;
import me.demo.rpc.netty.io.DataWriteStream;

public abstract class AsyncTcpConnection<K> extends AsyncConnection<K> {
	InetSocketAddress address;
	Socket socket;
	protected DataReadStream readStream;
	protected DataWriteStream writeStream;

	public AsyncTcpConnection(InetSocketAddress address, AsyncConnectionPool<K, ? extends AsyncConnection<K>> pool) {
		super(pool);
		this.address = address;
	}

	public AsyncTcpConnection(String host, int port, AsyncConnectionPool<K, ? extends AsyncConnection<K>> pool) {
		super(pool);
		this.address = new InetSocketAddress(host, port);
	}


	@Override
	protected boolean isOpened() {
		return socket != null && !socket.isClosed();
	}

	@Override
	protected void doCreate() throws IOException, InterruptedException {
		Socket socket = new Socket();
		try {
			socket.connect(address, pool.getConnectTimeout());
			socket.setSoTimeout(pool.getIdleInterval());
			socket.setTcpNoDelay(true);
			DataReadStream readStream = new DataReadStream(socket.getInputStream(), pool.getIdleInterval());
			DataWriteStream writeStream = new DataWriteStream(socket.getOutputStream(), pool.getIdleInterval());
			this.socket = socket;
			this.readStream = readStream;
			this.writeStream = writeStream;
		} catch (IOException e) {
			throw new ConnectException("connect to " + address + " failed:", e);
		}
	}

	@Override
	protected void doDestory() throws IOException, InterruptedException {
		readStream = null;
		writeStream = null;
		try {
			if (socket != null)
				socket.close();
		} catch (Throwable e) {
		}
		socket = null;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return socket + "";
	}

}
