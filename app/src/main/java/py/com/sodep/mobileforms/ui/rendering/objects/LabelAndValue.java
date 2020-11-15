package py.com.sodep.mobileforms.ui.rendering.objects;


/**
 * LabelAndValue is an entry on a Select or dropdown
 * 
 * @author Miguel
 * 
 */
public class LabelAndValue {

	private LookupData label;

	private LookupData value;

	public LabelAndValue(LookupData label, LookupData value) {
		this.label = label;
		this.value = value;
	}

	public LookupData getLabel() {
		return label;
	}

	public void setLabel(LookupData label) {
		this.label = label;
	}

	public LookupData getValue() {
		return value;
	}

	public void setValue(LookupData value) {
		this.value = value;
	}

}
