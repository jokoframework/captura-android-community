package py.com.sodep.mobileforms.ui.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View.OnClickListener;

public class ActionPreference extends DialogPreference {

	private OnClickListener l;

	public ActionPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ActionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setPositiveButtonOnClickListner(OnClickListener l) {
		this.l = l;

	}

	@Override
	protected void showDialog(Bundle state) {
		super.showDialog(state);
		((AlertDialog) getDialog()).getButton(android.content.DialogInterface.BUTTON_POSITIVE).setOnClickListener(l);
	}

}