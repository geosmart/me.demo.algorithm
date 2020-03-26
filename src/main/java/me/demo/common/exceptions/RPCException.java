package me.demo.common.exceptions;

import java.io.IOException;

public class RPCException extends IOException {
	private static final long serialVersionUID = 1L;

	public RPCException() {
		super();
	}

	public RPCException(String message, Throwable cause) {
		super(message, cause);
	}

	public RPCException(String message) {
		super(message);
	}

	public RPCException(Throwable cause) {
		super(cause);
	}

}
