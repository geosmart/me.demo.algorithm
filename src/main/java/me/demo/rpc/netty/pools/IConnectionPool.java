package me.demo.rpc.netty.pools;


import me.demo.common.exceptions.ConnectException;

public interface IConnectionPool<C extends IConnection> {
	/**
	 * 清空连接池
	 */
	void clean();

	/** 获取连接池名称 */
	String getName();

	/**
	 * 获取一个连接
	 * 
	 * @param caller
	 *            连接调用者
	 * @return 如果获取成功，则返回获取的连接对象，否则，返回null
	 * @throws InterruptedException
	 */
	public C getConnection(Object caller) throws InterruptedException, ConnectException;

	Object getConnectionUrl();
}
