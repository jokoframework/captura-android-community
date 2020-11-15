package py.com.sodep.mobileforms.log;

import android.util.Log;
import py.com.sodep.mf.exchange.MFLogger;

public class AndroidLogger implements MFLogger {

	private String LOG_TAG = "MF_Logger";

	@Override
	public void debug(String msg) {
		Log.d(LOG_TAG, msg);

	}

	@Override
	public void debug(String msg, Throwable tr) {
		Log.d(LOG_TAG, msg, tr);
	}

	@Override
	public void error(String msg) {
		Log.e(LOG_TAG, msg);
	}

	@Override
	public void error(String msg, Throwable tr) {
		Log.e(LOG_TAG, msg, tr);
	}

	@Override
	public void info(String msg) {
		Log.i(LOG_TAG, msg);
	}

	@Override
	public void info(String msg, Throwable tr) {
		Log.i(LOG_TAG, msg, tr);
	}

	@Override
	public void init(Class<?> clazz) {
		this.LOG_TAG = clazz.getSimpleName();
	}

	@Override
	public void trace(String msg) {
		Log.v(LOG_TAG, msg);
	}

}
