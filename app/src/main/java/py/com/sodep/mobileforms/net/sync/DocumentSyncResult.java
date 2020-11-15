package py.com.sodep.mobileforms.net.sync;

public class DocumentSyncResult {

	public enum ERROR {
		NONE, 
		REJECTED, 
		SERVER_UNKOWN_RESPONSE, 
		SERVER_ERROR, 
		EXCEPTION, 
		INVALID_AUTH, 
		TIMEOUT,
		NO_HANDLE,
		FAIL
	}

	private long documentId;

	private boolean success;

	private ERROR error = ERROR.NONE;

	private Exception thrownException;

	private int statusCode;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ERROR getError() {
		return error;
	}

	public void setError(ERROR error) {
		this.error = error;
	}

	public Exception getThrownException() {
		return thrownException;
	}

	public void setThrownException(Exception thrownException) {
		this.thrownException = thrownException;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

}
