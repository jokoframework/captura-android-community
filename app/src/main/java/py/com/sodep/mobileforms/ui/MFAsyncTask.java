package py.com.sodep.mobileforms.ui;

import android.os.AsyncTask;



public abstract class MFAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	protected Exception uncaughtException;

	@Override
	protected Result doInBackground(Params... params) {
		try {
			return doWork(params);
		} catch (Exception e) {

			uncaughtException = e;
		}
		return null;
	}

	protected abstract Result doWork(Params... params);

}
