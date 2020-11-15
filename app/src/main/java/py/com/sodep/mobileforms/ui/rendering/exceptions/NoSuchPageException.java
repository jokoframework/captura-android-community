package py.com.sodep.mobileforms.ui.rendering.exceptions;

public class NoSuchPageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchPageException() {
		super();
	}

	public NoSuchPageException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NoSuchPageException(String detailMessage) {
		super(detailMessage);
	}

	public NoSuchPageException(Throwable throwable) {
		super(throwable);
	}

}
