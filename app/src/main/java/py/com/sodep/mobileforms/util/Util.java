package py.com.sodep.mobileforms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;
import android.view.View;

public class Util {
	
	private static final String LOG_TAG = Util.class.getSimpleName();

	public static void changeLanguage(Activity activity, String language) {
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		activity.getBaseContext().getResources()
				.updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
	}

	private static byte[] buffer = new byte[1024];

	private static synchronized byte[] createChecksum(File file) throws NoSuchAlgorithmException, IOException  {
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);

			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);

			return complete.digest();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	public static String getMD5Checksum(File file) throws IOException {
		try {
			byte[] b = createChecksum(file);

			String result = "";
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, e.getMessage(), e);

		}
		return null;
	}

	public static View.OnClickListener onURLClickListener(final Context context, final String url) {
		return new View.OnClickListener() {
			final String HTTP_URL = "http://";
			final String HTTPS_URL = "https://";

			@Override
			public void onClick(View v) {
				if (url != null && !url.isEmpty()) {
					Uri webpage = Uri.parse(url);

					if (!url.startsWith(HTTP_URL) && !url.startsWith(HTTPS_URL)) {
						webpage = Uri.parse(HTTPS_URL + url);
					}

					Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
					if (intent.resolveActivity(context.getPackageManager()) != null) {
						context.startActivity(intent);
					}
				}
			}
		};
	}
}
