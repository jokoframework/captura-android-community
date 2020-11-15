package py.com.sodep.mf.exchange.exceptions;

public class AuthenticationException extends HttpResponseException {

	private FailCause failCause;

	public enum FailCause {
		NO_COOKIE, INVALID_USER, INVALID_SERVER_RESPONSE
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AuthenticationException(String detailMessage) {
		super(detailMessage);
	}

	public AuthenticationException(Throwable throwable) {
		super(throwable);
	}

	public FailCause getFailCause() {
		return failCause;
	}

	public void setFailCause(FailCause failCause) {
		this.failCause = failCause;
	}

}
