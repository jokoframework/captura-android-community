package py.com.sodep.mobileforms.exceptions;


public class RequiredValueException extends ValidationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequiredValueException() {
		super();
	}

	public RequiredValueException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public RequiredValueException(String detailMessage) {
		super(detailMessage);
	}

	public RequiredValueException(Throwable throwable) {
		super(throwable);
	}
	
	
	
	

}
