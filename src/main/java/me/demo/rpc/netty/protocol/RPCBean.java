package me.demo.rpc.netty.protocol;


import me.demo.rpc.netty.server.IRPCService;

/**
 * 执行数据库操作的Bean
 * 



 * 
 */
public class RPCBean implements IRPCService {
//	final static Logger logger = Logger.getLogger(RPCBean.class);

	public RPCBean() {
		super();
	}

	long startTime;

	@Override
	public void invokeBefore(RPCRequest request) {
		startTime = System.currentTimeMillis();
	}

	@Override
	public void invokeAfter(RPCRequest request, Object retValue, Throwable e) {
//		if (logger.isDebugEnabled())
//			logger.debug(request.getMethodName() + " called. [runinterval="
//					+ DateTime.milliSecondsBetween(startTime, System.currentTimeMillis()) + "]");
	}

	@Override
	public void init() {
	}

	@Override
	public void destroy() {
	}

}
