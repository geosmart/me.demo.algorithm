package me.demo.common.exceptions;

/**
 * 不正确的配置项
 * 


 * 
 */
public class IncorrectConfigurationError extends CoreException {
	private static final long serialVersionUID = 1L;

	public IncorrectConfigurationError() {
		super();
	}

	public IncorrectConfigurationError(String msg, Object data) {
		super(msg, data);
	}

	public IncorrectConfigurationError(String msg, Throwable cause, Object data) {
		super(msg, cause, data);
	}

	public IncorrectConfigurationError(Throwable cause, Object data) {
		super(cause, data);
	}

	public IncorrectConfigurationError(Throwable cause) {
		super(cause);
	}

	public IncorrectConfigurationError(String message, Throwable t) {
		super(message, t);
	}

	public IncorrectConfigurationError(String message) {
		super(message);
	}

}
