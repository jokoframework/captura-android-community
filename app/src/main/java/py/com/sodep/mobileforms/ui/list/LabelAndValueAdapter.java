package py.com.sodep.mobileforms.ui.list;

import java.util.List;

import py.com.sodep.mobileforms.ui.rendering.objects.LabelAndValue;
import py.com.sodep.mobileforms.ui.rendering.objects.LookupData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Used as an adapter in the Dropdown process items
 * 
 * @author jmpr
 * 
 */
public class LabelAndValueAdapter extends ArrayAdapter<LabelAndValue> {

	private SpinnerLabelFormatter labelFormatter;

	public LabelAndValueAdapter(Context context, int resource, int textViewResourceId, LabelAndValue[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public LabelAndValueAdapter(Context context, int resource, int textViewResourceId, List<LabelAndValue> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public LabelAndValueAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public LabelAndValueAdapter(Context context, int textViewResourceId, LabelAndValue[] objects) {
		super(context, textViewResourceId, objects);
	}

	public LabelAndValueAdapter(Context context, int textViewResourceId, List<LabelAndValue> objects) {
		super(context, textViewResourceId, objects);
	}

	public LabelAndValueAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		return setTextAndGet(position, view);
	}

	public SpinnerLabelFormatter getLabelFormatter() {
		return labelFormatter;
	}

	public void setLabelFormatter(SpinnerLabelFormatter labelFormatter) {
		this.labelFormatter = labelFormatter;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = super.getDropDownView(position, convertView, parent);
		return setTextAndGet(position, view);
	}

	private View setTextAndGet(int position, View view) {
		TextView textView = (TextView) view;
		LabelAndValue labelAndValue = this.getItem(position);
		LookupData label = labelAndValue.getLabel();
		if (labelFormatter == null) {
			textView.setText(label.getData().toString());
		} else {
			String text = labelFormatter.toText(label);
			textView.setText(text);
		}
		return view;
	}

}
