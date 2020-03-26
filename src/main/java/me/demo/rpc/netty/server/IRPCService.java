package me.demo.rpc.netty.server;


import me.demo.rpc.netty.protocol.RPCRequest;

public interface IRPCService {
	/**
	 * 服务初始化，只会在创建时调用一次
	 */
	void init();

	/**
	 * 执行一个远程调用之前要做的预处理
	 */
	void invokeBefore(RPCRequest request);

	/**
	 * 执行一个远程调用之后要做的后处理
	 */
	void invokeAfter(RPCRequest request, Object returnValue, Throwable throwable);

	/**
	 * 服务销毁，在服务被移除时调用 一次
	 */
	void destroy();
}
