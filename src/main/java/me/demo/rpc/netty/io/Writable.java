package me.demo.rpc.netty.io;

import java.io.IOException;
import java.io.Serializable;

/**
 * 具备可写能力的接口
 * 



 * 
 */
public interface Writable extends Serializable {
	/**
	 * 将接口数据写入流中
	 * 
	 * @param stream
	 *            写入流
	 */
	void writeToStream(DataWrite stream) throws IOException;
}
