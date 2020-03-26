package me.demo.common.exceptions;

import java.io.IOException;

/**
 * 连接异常。用于连接失败或异常断开时抛出
 * 



 * 
 */
public class ConnectException extends IOException {
	private static final long serialVersionUID = 1L;

	public ConnectException() {
		super();
	}

	public ConnectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ConnectException(String arg0) {
		super(arg0);
	}

	public ConnectException(Throwable arg0) {
		super(arg0);
	}

}
