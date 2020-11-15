package py.com.sodep.mf.exchange.exceptions;

public class SodepException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SodepException() {
	}

	public SodepException(String detailMessage) {
		super(detailMessage);
	}

	public SodepException(Throwable throwable) {
		super(throwable);
	}

	public SodepException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
