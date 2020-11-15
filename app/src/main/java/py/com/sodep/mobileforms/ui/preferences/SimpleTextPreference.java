package py.com.sodep.mobileforms.ui.preferences;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.Toast;

public class SimpleTextPreference extends Preference {

	protected Context ctx = null;

	public SimpleTextPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.ctx = context;
		init();
	}

	public SimpleTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
		init();
	}

	public SimpleTextPreference(Context context) {
		super(context);
		this.ctx = context;
		init();
	}
	
	protected void init() {
		String key = getKey();
		String value = PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, null);
		setSummary(value);
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	protected void onClick() {
		Toast.makeText(ctx, "Not editable", Toast.LENGTH_LONG).show();
	}

}
