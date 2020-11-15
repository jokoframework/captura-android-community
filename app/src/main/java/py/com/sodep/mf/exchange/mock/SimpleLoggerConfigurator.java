package py.com.sodep.mf.exchange.mock;

public class SimpleLoggerConfigurator {

	private boolean trace = false;
	private boolean debug = true;
	private boolean info = true;
	private boolean error = true;
	private static SimpleLoggerConfigurator instance;

	private SimpleLoggerConfigurator() {

	}

	public static SimpleLoggerConfigurator getInstance() {
		synchronized (SimpleLoggerConfigurator.class) {
			if (instance == null) {
				instance = new SimpleLoggerConfigurator();
			}
			return instance;
		}
	}

	public static boolean isTrace() {
		return getInstance().trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}

	public static boolean isDebug() {
		return getInstance().debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public static boolean isInfo() {
		return getInstance().info;
	}

	public void setInfo(boolean info) {
		this.info = info;
	}

	public static boolean isError() {
		return getInstance().error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
