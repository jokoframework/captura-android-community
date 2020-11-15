package py.com.sodep.mobileforms.exceptions;

import py.com.sodep.mf.exchange.exceptions.SodepException;

public class ValidationException extends SodepException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ValidationException(String detailMessage) {
		super(detailMessage);
	}

	public ValidationException(Throwable throwable) {
		super(throwable);
	}

}
