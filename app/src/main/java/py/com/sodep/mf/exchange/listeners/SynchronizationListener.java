package py.com.sodep.mf.exchange.listeners;

import py.com.sodep.mf.exchange.net.MetadataAndLookupTableSynchronizer;
import py.com.sodep.mf.exchange.objects.metadata.SynchronizationError;

public interface SynchronizationListener {

	public void syncOfMetadataStarted();

	public void syncOfMetadataFinished();

	public void syncOfLookupDataStarted();

	public void syncOfLookupDataFinished();

	public void synchronizationFailed(SynchronizationError error);

	/**
	 * Hopefully this will never happen. The {@link MetadataAndLookupTableSynchronizer} is
	 * catching all throwable errors and reporting it here, to let the android
	 * app handle it.
	 * 
	 * @param e
	 */
	public void unexpectedError(Throwable e);

}
