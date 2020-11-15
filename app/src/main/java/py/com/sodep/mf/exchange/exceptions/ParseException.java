package py.com.sodep.mf.exchange.exceptions;

public class ParseException extends SodepException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParseException() {
	}

	public ParseException(String detailMessage) {
		super(detailMessage);
	}

	public ParseException(Throwable throwable) {
		super(throwable);
	}

	public ParseException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
