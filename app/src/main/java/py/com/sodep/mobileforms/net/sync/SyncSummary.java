package py.com.sodep.mobileforms.net.sync;

import java.util.ArrayList;
import java.util.List;

import py.com.sodep.mobileforms.net.sync.DocumentSyncResult.ERROR;

public class SyncSummary {

	private List<DocumentSyncResult> results = new ArrayList<DocumentSyncResult>();;

	private int successCount = -1;

	private int rejectCount = -1;

	private int failCount = -1;

	private Boolean timeout;
	
	private Boolean authException;
	
	private Boolean serverException;
	
	private Boolean unexpectedException;
	
	private Boolean noHandle;

	public int getSuccessCount() {
		if (successCount == -1) {
			successCount = 0;
			for (DocumentSyncResult result : results) {
				if (result.isSuccess()) {
					successCount++;
				}
			}
		}
		return successCount;
	}

	public boolean wasThereATimeout() {
		if (timeout == null) {
			timeout = false;
			for (DocumentSyncResult result : results) {
				if(result.getError() == ERROR.TIMEOUT){
					timeout = true;
					break;
				}
			}
		}
		return timeout;
	}
	
	public boolean wasThereAnAuthException(){
		if (authException == null) {
			authException = false;
			for (DocumentSyncResult result : results) {
				if(result.getError() == ERROR.INVALID_AUTH){
					authException = true;
					break;
				}
			}
		}
		return authException;
	}
	
	public boolean wasThereANoHandle(){
		if (noHandle == null) {
			noHandle = false;
			for (DocumentSyncResult result : results) {
				if(result.getError() == ERROR.NO_HANDLE){
					noHandle = true;
					break;
				}
			}
		}
		return noHandle;
	}
	
	public DocumentSyncResult getFirstServerException(){
		for (DocumentSyncResult result : results) {
			if(result.getError() == ERROR.SERVER_ERROR){
				return result;
			}
		}
		return null;
	}
	
	public boolean wasThereAServerException(){
		if(serverException == null){
			serverException = false;
			for (DocumentSyncResult result : results) {
				if(result.getError() == ERROR.SERVER_ERROR){
					serverException = true;
					break;
				}
			}
		}
		return serverException;
	}
	
	public boolean wasThereAnUnexpectedException() {
		if(unexpectedException == null){
			unexpectedException = false;
			for (DocumentSyncResult result : results) {
				if(result.getError() == ERROR.EXCEPTION){
					unexpectedException = true;
					break;
				}
			}
		}
		return unexpectedException;
	}
	
	public DocumentSyncResult getFirstUnexpectedException(){
		for (DocumentSyncResult result : results) {
			if(result.getError() == ERROR.EXCEPTION){
				return result;
			}
		}
		return null;
	}

	public int getFailCount() {
		if (failCount == -1) {
			failCount = 0;
			for (DocumentSyncResult result : results) {
				if (!result.isSuccess()) {
					failCount++;
				}
			}
		}
		return failCount;
	}

	public int getRejectCount() {
		if (rejectCount == -1) {
			rejectCount = 0;
			for (DocumentSyncResult result : results) {
				if (!result.isSuccess() && result.getError() == ERROR.REJECTED) {
					rejectCount++;
				}
			}
		}
		return rejectCount;
	}

	public void addResult(DocumentSyncResult result) {
		clearCache();
		results.add(result);
	}

	private void clearCache() {
		failCount = -1;
		successCount = -1;
		rejectCount = -1;
		timeout = null;
		authException = null;
		serverException = null;
		unexpectedException = null;
		noHandle = null;
	}

	public void addResults(List<DocumentSyncResult> results) {
		clearCache();
		this.results.addAll(results);
	}

	public List<DocumentSyncResult> getResults() {
		return results;
	}

	public void setResults(List<DocumentSyncResult> results) {
		this.results = results;
	}

	

}
