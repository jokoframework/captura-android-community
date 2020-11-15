package py.com.sodep.mobileforms.dataservices;

import java.util.List;

import py.com.sodep.mf.exchange.objects.metadata.Form;

/**
 * An implementation of FormsDAO (Data Access Object) provides the means for
 * retrieving, listing and saving instances of Form and/or data related to them
 * 
 * @author Miguel
 * 
 */
public interface FormsDAO {

	Long getMaxDefinedVersion(Long formId);

	Form getForm(Long formId, Long version);

	void saveForm(Form form);

	void updateForm(Form form);

	void deleteForm(Long formId, Long version);

	String loadDefinition(Form f);

	List<Form> listForms(Long projectId);

	List<Form> listAllForms(long appId);

	void deleteForm(Long formId);

	List<Form> listAllForms();

}
