package me.demo.common.exceptions;

public class UnexpectedRPCException extends RPCException {
	private static final long serialVersionUID = 1L;

	public UnexpectedRPCException() {
	}

	public UnexpectedRPCException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedRPCException(String message) {
		super(message);
	}

	public UnexpectedRPCException(Throwable cause) {
		super(cause);
	}

}
