package me.demo.rpc.netty.pools;

import me.demo.common.exceptions.ConnectException;

/**
 * 连接对象，所有不同的连接对象基数，描述一个连接的基本行为
 * 



 * 
 */
public interface IConnection {

	/**
	 * 打开一个连接
	 * 
	 * @throws ConnectException
	 *             打开连接失败时，抛出此错误
	 */
	void open() throws ConnectException, ConnectException;

	/**
	 * 关闭或归还一个连接 <br>
	 * <font color=red>不允许抛出任何错误</font>
	 */
	void close();

	/**
	 * 测试当前连接是否打开
	 * 
	 * @return true - 连接已经关闭<br>
	 *         false - 连接已经打开
	 */
	boolean isClosed();

	/**
	 * 获取连接所属的连接池对象
	 */
	public IConnectionPool<?> getPool();

}
