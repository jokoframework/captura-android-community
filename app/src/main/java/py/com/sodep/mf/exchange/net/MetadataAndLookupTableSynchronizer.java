package py.com.sodep.mf.exchange.net;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import py.com.sodep.mf.exchange.LoggerFactory;
import py.com.sodep.mf.exchange.MFDataSetDefinition;
import py.com.sodep.mf.exchange.MFLogger;
import py.com.sodep.mf.exchange.TXInfo;
import py.com.sodep.mf.exchange.exceptions.AuthenticationException;
import py.com.sodep.mf.exchange.exceptions.HttpResponseException;
import py.com.sodep.mf.exchange.listeners.LookupDataListener;
import py.com.sodep.mf.exchange.listeners.MetadataListener;
import py.com.sodep.mf.exchange.listeners.SynchronizationListener;
import py.com.sodep.mf.exchange.listeners.SynchronizationStatusProvider;
import py.com.sodep.mf.exchange.objects.lookup.MFDMLTransport;
import py.com.sodep.mf.exchange.objects.lookup.MFDMLTransportMultiple;
import py.com.sodep.mf.exchange.objects.metadata.Application;
import py.com.sodep.mf.exchange.objects.metadata.Form;
import py.com.sodep.mf.exchange.objects.metadata.Project;
import py.com.sodep.mf.exchange.objects.metadata.SynchronizationError;

/**
 * This class encapsulate a thread that will pull exchange objects from the
 * server. There are two types of exchange objecs
 * <ul>
 * <li>metadata: {@link Application}, {@link Project}, {@link Form}</li>
 * <li>lookuptable: This is a table of data</li>
 * </ul>
 * It is expected that the size of Metadata won't be too much. Therefore,
 * we have deciced to download every time the full content. When new metadata is
 * available this class will call the methods of {@link MetadataListener}. On
 * the other hand, data for a lookup table might be big, so data will be
 * download as chunks from the server. Whenever a new chunk is available....
 * 
 * 
 * @author danicricco
 * 
 */
public class MetadataAndLookupTableSynchronizer {

	private static final MFLogger logger = LoggerFactory.getLogger(MetadataAndLookupTableSynchronizer.class);

	private final MetadataListener listener;
	private final LookupDataListener dataListener;
	private final ServerConnection serverConnection;
	private final SynchronizationListener synchronizationListener;
	private final SynchronizationStatusProvider datomoCache;

	public MetadataAndLookupTableSynchronizer(ServerConnection serverConnection, MetadataListener listener,
			LookupDataListener dataListener, SynchronizationListener connectionListener,
			SynchronizationStatusProvider datomoCache) {
		this.listener = listener;
		this.dataListener = dataListener;
		this.serverConnection = serverConnection;
		this.synchronizationListener = connectionListener;
		this.datomoCache = datomoCache;
	}

	private void reportSynchronizationError(SynchronizationError error) {
		if (this.synchronizationListener != null) {
			this.synchronizationListener.synchronizationFailed(error);
		} else {
			logger.error("ConnectionListener was not assigned to the DatamoSynchronizer");
		}
	}

	public ServerResponse syncMetadataAndLookupTables() {
		ServerResponse serverResponse = null;
		try {
            this.synchronizationListener.syncOfMetadataStarted();
			serverResponse = serverConnection.testConnectionAndGetSettings();
			if (serverResponse.isReachable()) {
				logger.debug("Synchronization in progress");
				doSync();
				logger.debug("Synchronization ended");
			} else {
				reportSynchronizationError(new SynchronizationError(SynchronizationError.ErrorType.SERVER_UNREACHABLE));
				logger.debug("Server Unreachable");
			}

		} catch (Throwable e) {
			// hopefully this should never happen. I'm catching it because this
			// is a secondary thread, and we need to report if this situation
			// arise
			this.synchronizationListener.unexpectedError(e);
		}
		return serverResponse;
	}

	private void doSync() {
		try {

			Set<Long> requiredLookupTables = doSyncMetadata();
			this.synchronizationListener.syncOfMetadataFinished();
			this.synchronizationListener.syncOfLookupDataStarted();
			doSyncLookups(requiredLookupTables);
			this.synchronizationListener.syncOfLookupDataFinished();
		} catch (JsonParseException e) {
			logger.error("Unable to parse json data", e);
			reportSynchronizationError(new SynchronizationError(SynchronizationError.ErrorType.PARSE_ERROR, e));
		} catch (JsonMappingException e) {
			logger.error("Unable to parse json data", e);
			reportSynchronizationError(new SynchronizationError(SynchronizationError.ErrorType.PARSE_ERROR, e));
		} catch (AuthenticationException e) {
			logger.debug("Authentication error");
			reportSynchronizationError(new SynchronizationError(SynchronizationError.ErrorType.AUTHENTICATION_ERROR, e));
		} catch (SocketTimeoutException e) {
			reportSynchronizationError(new SynchronizationError(SynchronizationError.ErrorType.CONNECTION_TIMEOUT, e));
		} catch (HttpResponseException e) {
			logger.debug("Server Response Exception", e);
			reportSynchronizationError(new SynchronizationError(SynchronizationError.ErrorType.RESPONSE_CODE_NOT_200, e));
		} catch (IOException e) {
			logger.debug("Connection error", e);
			reportSynchronizationError(new SynchronizationError(SynchronizationError.ErrorType.CONNECTION_FAILED, e));
		}
	}

	private void doSyncLookups(Set<Long> requiredLockups) throws AuthenticationException, IOException,
			HttpResponseException {
		Iterator<Long> it = requiredLockups.iterator();
		while (it.hasNext()) {
			Long lookupTableId = it.next();
			boolean hasDefinition = this.datomoCache.hasLookupTableDefinition(lookupTableId);
			if (!hasDefinition) {
				logger.debug("Need to download lookupTable definition of #" + lookupTableId);
				// need to download the lookuptable definition
				MFDataSetDefinition def = serverConnection.getLookupTableDefinition(lookupTableId);
				this.dataListener.define(lookupTableId, def);
			}
			synchronizeLookupTable(lookupTableId);
		}
	}

	/**
	 * This method will check if a lookupTable is synchronized and download
	 * missing data if necessary.
	 * 
	 * @param lookupTableId
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws AuthenticationException
	 * @throws HttpResponseException
	 */
	private void synchronizeLookupTable(Long lookupTableId) throws JsonParseException, JsonMappingException,
			IOException, AuthenticationException, HttpResponseException {

		String lastDownloadedTx = null;
		long computedTime = System.currentTimeMillis();
		boolean synced = false;
		do {
			TXInfo lastStoredTX = datomoCache.getLastTransaction(lookupTableId);
			if (lastStoredTX == null) {
				logger.debug("The Cache doesn't have any register of the lookup table #" + lookupTableId
						+ " Requesting a full download");
				lastStoredTX = new TXInfo(lookupTableId);
			} else {
				logger.debug("Resuming download of #" + lookupTableId + " from " + lastStoredTX.getTx() + " - "
						+ lastStoredTX.getEndRow());
			}
			// dmlTransport = httpConnectionManager.getData(lookupTableId,
			// lastStoredTX);
			MFDMLTransportMultiple multipleDMLs = serverConnection.getDataFast(lookupTableId, lastStoredTX);
			List<MFDMLTransport> transports = multipleDMLs.getListOfTransports();

			for (MFDMLTransport dmlTransport : transports) {
				TXInfo currentTxInfo = dmlTransport.getTxInfo();
				currentTxInfo.setLookupTable(lookupTableId);
				if (lastDownloadedTx == null || !lastDownloadedTx.equals(currentTxInfo.getTx())) {
					long currentTime = System.currentTimeMillis();
					this.dataListener.startTx(currentTxInfo.getTx());
					currentTime = System.currentTimeMillis() - currentTime;
					logger.trace("StartTX took " + currentTime + " ms.");
				}

				// This won't execute the DML it will just append it to the
				// db
				long currentTime = System.currentTimeMillis();
				this.dataListener.applyDML(dmlTransport);
				currentTime = System.currentTimeMillis() - currentTime;
				logger.trace("Apply DML took " + currentTime + " ms.");

				synced = dmlTransport.isSynch();

				if (dmlTransport.isFinal()) {
					// This will actually execute the query
					currentTime = System.currentTimeMillis() - currentTime;
					this.dataListener.endTx(currentTxInfo.getTx());
					currentTime = System.currentTimeMillis() - currentTime;
					logger.trace("endTX " + currentTime + " ms.");
				}

			}

		} while (!synced);

		if (synced) {
			computedTime = System.currentTimeMillis() - computedTime;
			logger.debug("Sync lookupTable #" + lookupTableId + " in " + computedTime + " ms.");
			// Is it OK to call to nowIsSynch here?
			this.dataListener.nowIsSynced(lookupTableId);
		} else {
			logger.info("Stop the synchronization of #" + lookupTableId);
		}
	}

	private Set<Long> doSyncMetadata() throws AuthenticationException, IOException, HttpResponseException {
		// Download application and projets
		List<Application> applications = serverConnection.getApplications();

		this.listener.fullUpdateApplicationsAndProjects(applications);
		// Download forms
		List<Form> forms = serverConnection.getForms();
		this.listener.fullUpdate(forms);
		TreeSet<Long> requiredLookupTables = new TreeSet<Long>();
		for (Form f : forms) {
			Long lastStoredVersion = datomoCache.getStoredVersion(f);
			if (f.getRequiredLookupTables() != null) {
				requiredLookupTables.addAll(f.getRequiredLookupTables());
			}
			if (lastStoredVersion == null || lastStoredVersion < f.getVersion()) {
				// need to update the form
				Form form = this.serverConnection.getFormDefinition(f.getId(), f.getVersion());
				this.listener.changeFormDefinition(form);
			}
		}
		return requiredLookupTables;

	}
}
