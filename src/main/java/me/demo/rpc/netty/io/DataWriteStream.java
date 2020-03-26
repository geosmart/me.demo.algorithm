package me.demo.rpc.netty.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 数据写入流，实现DataWrite接口
 * 



 * 
 * 
 */
public class DataWriteStream extends AbstractDataWrite {
	private volatile int wroteBytes = 0;
	OutputStream outputStream;

	/**
	 * 构造DataWriteStream
	 * 
	 * @param outputStream
	 *            输出流
	 * @param timeout
	 *            写数据超时时间，以毫秒为单位
	 * 
	 */
	public DataWriteStream(OutputStream outputStream, int timeout) {
		super(timeout);
		this.outputStream = outputStream;
	}

	/**
	 * 获取输出流
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	protected int write(byte[] b, int offset, int len, int timeout) throws IOException {
		if (len == 0)
			return 0;
		if (b == null)
			throw new NullPointerException();
		if (offset < 0 || (offset + len) > b.length)
			throw new IndexOutOfBoundsException();
		if (outputStream == null)
			throw new IOException("尚未指定具体的输出流对象，不允许写数据!");
		outputStream.write(b, offset, len);
		outputStream.flush();
		wroteBytes += len;
		return len;
	}

	@Override
	public int wroteBytes() {
		return wroteBytes;
	}

}
