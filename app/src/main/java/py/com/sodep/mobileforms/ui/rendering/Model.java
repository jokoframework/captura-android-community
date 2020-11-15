package py.com.sodep.mobileforms.ui.rendering;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import py.com.sodep.mf.exchange.objects.metadata.Form;
import py.com.sodep.mf.form.model.MFForm;
import py.com.sodep.mf.form.model.MFPage;
import py.com.sodep.mf.form.model.element.MFElement;
import py.com.sodep.mf.form.model.flow.MFConditionalTarget;
import py.com.sodep.mf.form.model.flow.MFFlow;
import py.com.sodep.mf.form.model.prototype.MFInput;
import py.com.sodep.mf.form.model.prototype.MFInput.Type;
import py.com.sodep.mf.form.model.prototype.MFPrototype;
import py.com.sodep.mobileforms.dataservices.DocumentsDataSource;
import py.com.sodep.mobileforms.dataservices.documents.Document;
import py.com.sodep.mobileforms.dataservices.sql.SQLDocumentsDataSource;
import py.com.sodep.mobileforms.ui.rendering.exceptions.NoSuchPageException;
import py.com.sodep.mobileforms.ui.rendering.exceptions.ParseException;
import py.com.sodep.mobileforms.ui.rendering.objects.LabelAndValue;
import py.com.sodep.mobileforms.ui.rendering.objects.LookupData;

public class Model implements Serializable {

	private transient DocumentsDataSource _dataSource;

	private transient LookupTableUtils _lookupTableUtils;

	private transient ObjectMapper _mapper;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Calendar activeDate = Calendar.getInstance();

	Calendar activeTime = Calendar.getInstance();

	Document document;

	private Stack<MFPage> pageStack = new Stack<MFPage>();

	private Stack<MFPage> pageBackStack = new Stack<MFPage>();

	private Form form;

	private MFForm mfform;

	private MFPage currentPage;

	// For saving a photo
	File currentPhotoFile;

	Long currentPhotoElementId;

	Model(Form form) throws ParseException {
		this.form = form;
		this.document = new Document();
		init(form);
	}

	private ObjectMapper mapper() {
		if (_mapper == null) {
			_mapper = new ObjectMapper();
		}
		return _mapper;
	}

	private LookupTableUtils lookupTableUtils() {
		if (_lookupTableUtils == null) {
			_lookupTableUtils = new LookupTableUtils();
		}
		return _lookupTableUtils;
	}

	private DocumentsDataSource dataSource() {
		if (_dataSource == null) {
			_dataSource = new SQLDocumentsDataSource();
		}
		return _dataSource;
	}

	Model(Document document) throws ParseException {
		this.form = document.getForm();
		this.document = document;
		init(form);
	}

	private void init(Form form) throws ParseException {
		String definition = form.getDefinition();
		if (definition == null) {
			throw new ParseException("Form's definition is null");
		}

		try {
			mfform = mapper().readValue(definition, MFForm.class);
		} catch (JsonParseException e) {
			throw new ParseException(e);
		} catch (JsonMappingException e) {
			throw new ParseException(e);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public MFPage changeCurrentPageByPosition(int position) {
		MFPage page = null;
		for (MFPage p : mfform.getPages()) {
			if (p.getPosition() == position) {
				page = p;
				break;
			}
		}
		return changeCurrentPage(page);
	}

	/*public MFPage changeCurrentPageById(long id) {
		MFPage page = mfform.getPageById(id);
		if (page == null) {
			throw new NoSuchPageException("No page with id " + id);
		}
		return changeCurrentPage(page);
	}*/

	public MFPage changeCurrentPage(MFPage page) {
		if (page == null) {
			throw new IllegalArgumentException("Page cannot be null");
		}
		currentPage = page;
		pageStack.add(currentPage);
		if (!pageBackStack.isEmpty()) {
			pageBackStack.pop();
			// TODO check if the backstack is consistent?
		}
		return currentPage;
	}

	public Form getForm() {
		return form;
	}

	public MFForm getMfform() {
		return mfform;
	}

	public MFPage getCurrentPage() {
		return currentPage;
	}

	public void newDocument() {
		pageStack.clear();
		pageBackStack.clear();
		document.clear();
		currentPage = null;
	}

	public boolean hasBeenVisited(MFPage page) {
		List<MFElement> elements = currentPage.getElements();
		for (MFElement e : elements) {
			if (document.containsKey(e.getInstanceId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a MFPage based on the instanceId
	 * 
	 * Return null if no page is found with the given instanceId
	 * 
	 * @param instanceId
	 * @return
	 */
	public MFPage pageFromInstanceId(String instanceId) {
		if (mfform.getPages() != null) {
			for (MFPage p : mfform.getPages()) {
				if (p.getInstanceId().equals(instanceId)) {
					return p;
				}
			}
		}

		return null;
	}

	public void addPageToStack(MFPage page) {
		pageStack.add(page);
	}

	public void cleanBackStackAndRelatedData() {
		while (!pageBackStack.isEmpty()) {
			MFPage mffpage = pageBackStack.pop();
			List<MFElement> elements = mffpage.getElements();
			for (MFElement e : elements) {
				document.remove(e.getInstanceId());
			}
		}
	}

	public void save() {
		dataSource().save(form, document.getId(), document);
	}

	void saveDraft() {
		Long saveDraft = dataSource().saveDraft(form, document.getId(), document);
		document.setId(saveDraft);
	}

	public Stack<MFPage> getPageBackStack() {
		return pageBackStack;
	}

	public String computeTarget() {
		MFFlow flow = currentPage.getFlow();
		String target = null;
		List<MFConditionalTarget> targets = flow.getTargets();
		if (targets != null) {
			for (MFConditionalTarget n : targets) {
				String possibleTarget = n.getTarget(); // a page
				String element = n.getElementId(); // the instanceId to test
				String desiredValue = n.getValue();
				String actualValue = document.get(element);
				MFConditionalTarget.Operator condition = n.getOperator();
				Map<String, MFElement> elementsMappedByName = getMfform().elementsMappedByName();
				MFElement mfElement = elementsMappedByName.get(element);
				if (condition == MFConditionalTarget.Operator.EQUALS) {
					if (actualValue.equals(desiredValue)) {
						target = possibleTarget;
						break;
					}
				} else if (condition == MFConditionalTarget.Operator.DISTINCT) {
					if (!actualValue.equals(desiredValue)) {
						target = possibleTarget;
						break;
					}
				} else if (condition == MFConditionalTarget.Operator.CONTAINS) {
					if (actualValue.contains(desiredValue)) {
						target = possibleTarget;
						break;
					}
				} else if (condition == MFConditionalTarget.Operator.GT || condition == MFConditionalTarget.Operator.LT) {
					MFPrototype proto = mfElement.getProto();
					if (proto instanceof MFInput) {
						MFInput mfInput = (MFInput) mfElement.getProto();
						Type inputType = mfInput.getSubtype();
						try {
							double desiredValueNumber;
							double actualValueNumber;

							if (inputType == Type.DECIMAL) {
								desiredValueNumber = Double.parseDouble(desiredValue);
								actualValueNumber = Double.parseDouble(actualValue);
							} else if (inputType == Type.INTEGER) {
								desiredValueNumber = Integer.parseInt(desiredValue);
								actualValueNumber = Integer.parseInt(actualValue);
							} else {
								// TODO log an error
								continue;
							}

							if (condition == MFConditionalTarget.Operator.GT && actualValueNumber > desiredValueNumber) {
								target = possibleTarget;
								break;
							} else if (condition == MFConditionalTarget.Operator.LT
									&& actualValueNumber < desiredValueNumber) {
								target = possibleTarget;
								break;
							}
						} catch (NumberFormatException e) {
							// TODO log an error
						}
					}
				}
			}
		}
		return target == null ? flow.getDefaultTarget() : target;
	}

	public List<LabelAndValue> listSelectOptions(MFElement element) {
		return lookupTableUtils().listSelectOptions(element, document);
	}

	public List<LookupData> listPossibleValues(MFElement element) {
		return lookupTableUtils().listPossibleValues(element, document);
	}

	public MFPage back() {
		if (pageStack.size() > 1) {
			MFPage page = pageStack.pop();
			pageBackStack.push(page);
			currentPage = pageStack.peek();
			return currentPage;
		}
		return null;
	}

	public boolean areEqual(Map<String, String> map2) {
		if (document.size() != map2.size()) {
			return false;
		}
		Set<String> keySet1 = document.keySet();
		Set<String> keySet2 = map2.keySet();
		for (String s : keySet1) {
			if (!keySet2.contains(s)) {
				return false;
			}
			String val1 = document.get(s);
			String val2 = map2.get(s);
			if (val1 != val2 && val1 != null && !val1.equals(val2)) {
				return false;
			}
		}
		return true;
	}

	public Document getDocument() {
		return document;
	}

	public Calendar getActiveDate() {
		return activeDate;
	}

	/*public void setActiveDate(Calendar activeDate) {
		this.activeDate = activeDate;
	}*/

	public Calendar getActiveTime() {
		return activeTime;
	}

	/*public void setActiveTime(Calendar activeTime) {
		this.activeTime = activeTime;
	}*/

	/*public File getCurrentPhotoFile() {
		return currentPhotoFile;
	}*/

	public Long getCurrentPhotoElementId() {
		return currentPhotoElementId;
	}

	public String getCurrentPhotoFilePath() {
		if (currentPhotoFile != null) {
			return currentPhotoFile.getAbsolutePath();
		}
		return null;
	}
}
