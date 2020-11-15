package py.com.sodep.mobileforms.dataservices;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import py.com.sodep.mf.exchange.objects.metadata.Form;
import py.com.sodep.mobileforms.dataservices.documents.Document;
import py.com.sodep.mobileforms.dataservices.documents.DocumentMetadata;

/**
 * A datasource implementation should allow to lookup data and save data
 * inputted by the user through Forms
 * 
 * Independently of how data is persisted (internal data storage, database or
 * other) this is the interface defines the contract for persisting data.
 * 
 * @author Miguel
 * 
 */
public interface DocumentsDataSource {

	/**
	 * A list of entries that belong to the form that have not yet been sent to
	 * the server.
	 * 
	 * The returned data is expected to have an id field, "_id". The data is
	 * ordered by this field.
	 * 
	 * "_id" is an increasing number.
	 * 
	 * @param form
	 * @return
	 */
	List<Map<String, String>> listUnsyncedDocuments(Form form);

    void resetUploadAttempts(long id);

    /**
	 * Delete all documents that were marked as synced/sent and not rejected.
	 * 
	 * Rejected entries are left for log/auditing purposes
	 */
	void deleteSynced();

	Map<String, String> getDocumentAsMap(long id);
	
	Document getDocument(long id);

	Form getFormOfDocument(long id);

	LinkedHashMap<Date, List<DocumentMetadata>> listHistory(long appId);

	/**
	 * Data from the form f that is less or equal to maxId will be marked as
	 * synced or sent.
	 * 
	 * Every entry has an id associated, which is an increasing number.
	 * 
	 * @param f
	 * @param maxId
	 */
	void markDocumentsAsSynced(long id);

	void markDocumentAsInProgress(long id);

	void markDocumentAsRejected(long id);

	void markDocumentAsFailed(long id);

	void deleteAll(int [] status);
	
	void delete(Long id);

	void resetAllInProgress();

	void resetAllFailed(int max);

	List<Long> listUnsyncedDocuments(Long appId);

	void markDocumentAsFailed(long id, int responseCode);
	
	void markDocumentAsFailed(long id, int responseCode, String failMessage);
	
	void setMD5(long id, String md5);

	String getMD5(long id);

	List<Long> listUnsyncedDocuments();

	void markDocumentAsRejected(long id, int responseCode, String message);
	
	Long save(Form form, Long documentId, final Map<String, String> document);

	Long saveDraft(Form form, Long documentId, Map<String, String> document);
	
}
