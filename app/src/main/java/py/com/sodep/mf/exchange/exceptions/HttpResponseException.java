package py.com.sodep.mf.exchange.exceptions;

import py.com.sodep.mf.exchange.objects.error.ErrorResponse;

public class HttpResponseException extends SodepException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int responseCode;

	private String URL;

	private ErrorResponse errorResponse;

	public HttpResponseException() {
		super();
	}

	public HttpResponseException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public HttpResponseException(String detailMessage) {
		super(detailMessage);
	}

	public HttpResponseException(Throwable throwable) {
		super(throwable);
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

}
