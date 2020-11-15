package py.com.sodep.mf.exchange.listeners;

import py.com.sodep.mf.exchange.MFDataSetDefinition;
import py.com.sodep.mf.exchange.net.MetadataAndLookupTableSynchronizer;
import py.com.sodep.mf.exchange.objects.lookup.MFDMLTransport;

/**
 * This interface has the callbacks that the {@link MetadataAndLookupTableSynchronizer} will
 * call to inform the status of the synchronization of a the lookupTables
 * 
 * @author danicricco
 * 
 */
public interface LookupDataListener {

	/**
	 * Call when it is necessary to update the definition of a lookupTable
	 * 
	 * @param lookupTableId
	 * @param def
	 */
	public void define(Long lookupTableId, MFDataSetDefinition def);

	/**
	 * Start a given transaction. A transaction can only contain data for a
	 * single lookup table
	 * 
	 * @param tx
	 */
	public void startTx(String tx);

	/**
	 * Data for a given transaction. The {@link MFDMLTransport} has a field that
	 * contains information about the transaction
	 * {@link MFDMLTransport#getTxInfo()} , the client should keep track of this
	 * information in order to support resume download. The interface
	 * {@link SynchronizationStatusProvider} should return this value in the method
	 * {@link SynchronizationStatusProvider#getLastTransaction(Long)}.
	 * 
	 * @param row
	 */
	public void applyDML(MFDMLTransport row);

	/**
	 * End a given transaction. The client implementing this interface can
	 * assume that the data it has is valid and can make it available for the
	 * user
	 * 
	 * @param tx
	 */
	public void endTx(String tx);

	/**
	 * Inform that a lookup table is fully synchronized. The client can use this
	 * method to commit temporal data (if any)
	 * 
	 * @param lookupTableId
	 */
	public void nowIsSynced(Long lookupTableId);
}
