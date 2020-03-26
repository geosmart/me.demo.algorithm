package me.demo.rpc.netty.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import me.demo.common.exceptions.TimeoutException;
import me.demo.rpc.netty.pools.IAsyncConnectionPack;
import me.demo.rpc.netty.pools.tcp.AsyncTcpConnection;
import me.demo.rpc.netty.io.DataReadStream;
import me.demo.rpc.netty.io.DataWriteStream;
import me.demo.rpc.netty.protocol.RPCRequest;
import me.demo.rpc.netty.protocol.RPCResponse;

public class RPCTcpConnection extends AsyncTcpConnection<Integer> {
//	static Logger logger = Logger.getLogger(RPCTcpConnection.class, "biss", "rpc");

	public RPCTcpConnection(InetSocketAddress address, RPCTcpConnectionPool pool) {
		super(address, pool);
	}

	public RPCTcpConnection(String host, int port, RPCTcpConnectionPool pool) {
		super(host, port, pool);
	}

	@Override
	protected IAsyncConnectionPack<Integer> createIdlePack() throws IOException {
		return new RPCRequest(null, null, null);
	}

	@Override
	protected void doSend(IAsyncConnectionPack<Integer> pack) throws IOException, InterruptedException {
		final DataWriteStream stream = writeStream;
		if (stream != null) {
			RPCRequest request = (RPCRequest) pack;
			request.writeToStream(stream);
			// logger.debug("connection[" + getAddress() + "] send message:" +
			// request);
		} else
			throw new IOException("connection[" + getAddress() + "] is not opened");
	}

	@Override
	protected IAsyncConnectionPack<Integer> doRecv() throws IOException, InterruptedException {
		try {
			final DataReadStream stream = readStream;
			if (stream != null) {
				RPCResponse r = new RPCResponse();
				r.readFromStream(stream);
				// logger.debug("connection[" + getAddress() +
				// "] received message:" + r);
				return r;
			} else
				throw new IOException("connection[" + getAddress() + "] is not opened");
		} catch (SocketTimeoutException e) {
			throw new TimeoutException(e);
		}
	}

	@Override
	protected synchronized void destroy() throws IOException, InterruptedException {
//		logger.error("connection[" + getAddress() + "] closed");
		super.destroy();
	}

	@Override
	protected IAsyncConnectionPack<Integer> recv() throws IOException, InterruptedException {
		try {
			RPCResponse p = (RPCResponse) super.recv();
			if (p.isRequestPack()) {
				// processRequest(p);
			}
			return p;
		} catch (TimeoutException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (IOException e) {
			if (isOpened() && !destorying) {
//				logger.error("connection[" + getAddress() + "] fire exception:", e);
				destroy();
			}
			throw new IOException(e);
		} catch (Throwable e) {
			if (isOpened() && !destorying) {
//				logger.error("connection[" + getAddress() + "] fire exception:", e);
				destroy();
			}
			throw new IOException(e);
		}
	}

}
