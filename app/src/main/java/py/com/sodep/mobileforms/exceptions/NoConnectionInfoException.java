package py.com.sodep.mobileforms.exceptions;

public class NoConnectionInfoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoConnectionInfoException() {
		super();
	}

	public NoConnectionInfoException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NoConnectionInfoException(String detailMessage) {
		super(detailMessage);
	}

	public NoConnectionInfoException(Throwable throwable) {
		super(throwable);
	}

}
