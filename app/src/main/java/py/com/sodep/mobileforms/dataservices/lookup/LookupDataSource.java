package py.com.sodep.mobileforms.dataservices.lookup;

import java.util.List;

import py.com.sodep.mf.exchange.MFDataSetDefinition;
import py.com.sodep.mf.exchange.TXInfo;
import py.com.sodep.mf.exchange.objects.lookup.MFDMLTransport;
import py.com.sodep.mf.exchange.objects.metadata.Form;
import py.com.sodep.mf.form.model.element.filter.MFFilter;
import py.com.sodep.mobileforms.ui.rendering.objects.LookupData;
import android.database.Cursor;

public interface LookupDataSource {
	
	void define(Long tablelookupId,  MFDataSetDefinition dataSet);
	
	void startTx(String tx);
	
	void applyDML(MFDMLTransport dml);
	
	void endTx(String tx);

	Cursor query(Long id);
	
	Cursor query(Long id, List<MFFilter> conditions);
	
	boolean hasLookupTableDefinition(Long id);
	
	TXInfo getTXInfo(Long lookupId);
	
	void deleteLookupData();

	void markAsSynced(Long id);

	boolean isSynced(Long id);

	boolean isAllDataAvailable(Form form);

	List<LookupData[]> list(Long id, List<String> fields, List<MFFilter> filters);
	
}
