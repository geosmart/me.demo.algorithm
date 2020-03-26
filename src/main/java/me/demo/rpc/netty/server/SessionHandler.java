package me.demo.rpc.netty.server;


import java.util.concurrent.ExecutorService;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import me.demo.rpc.netty.protocol.RPCRequest;
import me.demo.rpc.netty.protocol.RPCResponse;

/**
 * 客户端会话处理句柄
 * 

 *
 */
public class SessionHandler extends ChannelDuplexHandler {
//	static Logger logger = Logger.getLogger(SessionHandler.class, "biss", "rpc");
//	static Logger serverLogger = Logger.getLogger("rpc.service.sendreceive", "biss", "rpc");
	ChannelHandlerContext ctx;
	ExecutorService threadPool;

	public SessionHandler(ExecutorService threadPool) {
		super();
		this.threadPool = threadPool;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
//		logger.error("exception caught:", cause);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		this.ctx = ctx;
		// logger.debug(ctx + " added.");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		// logger.debug(ctx + " removed.");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						RPCRequest q = (RPCRequest) msg;
						if (q.isIdlePack()) {
							// idle msg
							ctx.writeAndFlush(new RPCResponse(q.getPackId(), true, null, null));
						} else {
//							serverLogger.info("receive msg[" + msg + "] .");
							RPCResponse response = ServicePool.defaultInstance.invokeServiceMethod(q);
//							serverLogger.info("response msg[" + msg + "] .");
							ctx.writeAndFlush(response);
						}
					} catch (Throwable e) {
//						logger.debug("invoke error:", e);
					}
				}
			});
		} finally {
			ReferenceCountUtil.release(0);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
//				logger.warn("not received client[" + ctx.handler() + "] idle response, removed.");
				ctx.close();
			} else if (e.state() == IdleState.WRITER_IDLE) {
				// ctx.writeAndFlush(new PingMessage());
			}
		}
	}

}
