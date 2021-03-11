package org.jfinger.cloud.exception;

public class JFingerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JFingerException(String message){
		super(message);
	}

	public JFingerException(Throwable cause)
	{
		super(cause);
	}

	public JFingerException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
