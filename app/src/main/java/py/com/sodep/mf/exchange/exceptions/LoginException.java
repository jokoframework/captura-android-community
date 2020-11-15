package py.com.sodep.mf.exchange.exceptions;

public class LoginException extends SodepException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public LoginException(Throwable throwable) {
		super(throwable);
	}

    public LoginException(String detailMessage) {
        super(detailMessage);
    }
}
