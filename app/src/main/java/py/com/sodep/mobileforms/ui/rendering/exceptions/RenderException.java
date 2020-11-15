package py.com.sodep.mobileforms.ui.rendering.exceptions;

public class RenderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RenderException() {
		super();
	}

	public RenderException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public RenderException(String detailMessage) {
		super(detailMessage);
	}

	public RenderException(Throwable throwable) {
		super(throwable);
	}

}
