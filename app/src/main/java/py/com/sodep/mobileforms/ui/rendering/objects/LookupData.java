package py.com.sodep.mobileforms.ui.rendering.objects;

import py.com.sodep.mf.exchange.MFField;
import py.com.sodep.mf.exchange.MFField.FIELD_TYPE;

public class LookupData {

	private Object data;

	private MFField.FIELD_TYPE type;

	public LookupData(Object data, FIELD_TYPE type) {
		this.data = data;
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public MFField.FIELD_TYPE getType() {
		return type;
	}

	public void setType(MFField.FIELD_TYPE type) {
		this.type = type;
	}

}
