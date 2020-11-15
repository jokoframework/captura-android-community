package py.com.sodep.mf.exchange.listeners;

import java.util.List;

import py.com.sodep.mf.exchange.net.MetadataAndLookupTableSynchronizer;
import py.com.sodep.mf.exchange.objects.metadata.Application;
import py.com.sodep.mf.exchange.objects.metadata.Form;

/***
 * This interface has method that will be invoked by the
 * {@link MetadataAndLookupTableSynchronizer} when new data has arrived. The methods that start
 * with the prefix "fullUpdate" should be idempotent. Thus, the whole list will
 * always be received
 * 
 * @author danicricco
 * 
 */
public interface MetadataListener {

	/**
	 * A list of applications and the projects within the applications. Projects
	 * will have an empty list of forms, no matter if they have or not
	 * associated forms
	 * 
	 * @param applications
	 */
	public void fullUpdateApplicationsAndProjects(List<Application> applications);

	/**
	 * A list of forms where the user has access rights. The field xml will be
	 * empty . The field version won't be null but it shouldn't be used to
	 * update the version, since the xml is null
	 * 
	 * @param forms
	 */
	public void fullUpdate(List<Form> forms);

	/**
	 * The definition of the form has changed from the last time. All fields of
	 * form will be set
	 * 
	 * @param form
	 */
	public void changeFormDefinition(Form form);

}
