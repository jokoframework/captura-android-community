package py.com.sodep.mobileforms.exceptions;


public class LengthException extends ValidationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LengthException() {
		super();
	}

	public LengthException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public LengthException(String detailMessage) {
		super(detailMessage);
	}

	public LengthException(Throwable throwable) {
		super(throwable);
	}

}
