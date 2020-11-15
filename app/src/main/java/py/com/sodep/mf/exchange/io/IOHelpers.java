package py.com.sodep.mf.exchange.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOHelpers {

	public static boolean closeStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return false;
	}

	public static boolean closeStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
			}
		}
		return false;
	}

}
