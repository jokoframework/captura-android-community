package py.com.sodep.mf.exchange.exceptions;

public class DownloadException extends SodepException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DownloadException() {
	}

	public DownloadException(String detailMessage) {
		super(detailMessage);
	}

	public DownloadException(Throwable throwable) {
		super(throwable);
	}

	public DownloadException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
