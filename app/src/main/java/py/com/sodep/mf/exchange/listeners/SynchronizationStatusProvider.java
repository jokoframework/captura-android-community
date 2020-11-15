package py.com.sodep.mf.exchange.listeners;

import py.com.sodep.mf.exchange.TXInfo;
import py.com.sodep.mf.exchange.net.MetadataAndLookupTableSynchronizer;
import py.com.sodep.mf.exchange.objects.metadata.Form;

/**
 * This interface provides a platform independent access to the storage. The
 * {@link MetadataAndLookupTableSynchronizer} will call methods of this class to :
 * 
 * <ul>
 * <li>detect the last version stored of a form:</li>
 * </ul>
 * 
 * @author danicricco
 * 
 */
public interface SynchronizationStatusProvider {

	/**
	 * 
	 * @param f
	 * @return The version stored of the form or null if it doesn't have any
	 *         version for the given form
	 */
	public Long getStoredVersion(Form f);

	public boolean hasLookupTableDefinition(Long lookupTableId);

	/**
	 * This method mark the last point of synchronization received for a given
	 * lookup table. The {@link MetadataAndLookupTableSynchronizer} will use this information to
	 * resume the download of a lookup table
	 * 
	 * @param lookupTable
	 * @return
	 */
	public TXInfo getLastTransaction(Long lookupTable);
}
