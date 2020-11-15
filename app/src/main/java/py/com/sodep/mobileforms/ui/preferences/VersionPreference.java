package py.com.sodep.mobileforms.ui.preferences;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AttributeSet;

public class VersionPreference extends SimpleTextPreference {

	public VersionPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public VersionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VersionPreference(Context context) {
		super(context);
	}

	protected void init() {
		PackageInfo packageInfo;
		try {
			packageInfo = ctx.getPackageManager().getPackageInfo("py.com.sodep.captura.forms", 0);
			setSummary(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			setSummary("unknown");
		}
		
	}

}
