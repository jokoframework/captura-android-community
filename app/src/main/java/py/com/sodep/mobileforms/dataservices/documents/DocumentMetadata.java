package py.com.sodep.mobileforms.dataservices.documents;

import java.io.Serializable;
import java.util.Date;

import py.com.sodep.mf.exchange.objects.metadata.Form;

public class DocumentMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int STATUS_NOT_SYNCED = 0;

	public static final int STATUS_SYNCED = 1;

	public static final int STATUS_REJECTED = 2;

	public static final int STATUS_FAILED = 3;

	public static final int STATUS_IN_PROGRESS = 4;
	
	public static final int STATUS_DRAFT = 5;

	// public static final int STATUS_QUEUED;

	private Long id;

	private Form form;

	private Date savedAt;

	private Date syncedAt;

	private Date lastFailureAt;

	private int responseCode;

	private int status;

	private int uploadAttempts;

	private String failMessage;

	private int percentage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Date getSavedAt() {
		return savedAt;
	}

	public void setSavedAt(Date savedAt) {
		this.savedAt = savedAt;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public Date getSyncedAt() {
		return syncedAt;
	}

	public void setSyncedAt(Date syncedAt) {
		this.syncedAt = syncedAt;
	}

	public Date getLastFailureAt() {
		return lastFailureAt;
	}

	public void setLastFailureAt(Date lastFailureAt) {
		this.lastFailureAt = lastFailureAt;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getUploadAttempts() {
		return uploadAttempts;
	}

	public void setUploadAttempts(int uploadAttempts) {
		this.uploadAttempts = uploadAttempts;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

}
