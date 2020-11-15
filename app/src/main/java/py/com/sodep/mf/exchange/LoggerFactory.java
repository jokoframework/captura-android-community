package py.com.sodep.mf.exchange;

/**
 * This is a thread safe class that can produce {@link MFLogger}. The method
 * {@link #initialize(String)} should be called with a valid implementation of
 * {@link MFLogger}, the logger adapter. When the method
 * {@link #getLogger(Class)} is called a new instance of the logger adapter will
 * be returned. The goal of this class is to encapsulate the logging mechanism
 * of different platforms. Each platform should creates its own
 * {@link MFLogger} and initialize this loggerFactory
 * 
 * @author danicricco
 * 
 */
public class LoggerFactory {

	private static LoggerFactory factory;

	private Class<?> loggerClass;

	private LoggerFactory(String loggerClassName) {
		try {
			init(Class.forName(loggerClassName));

		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Logger not properly initialized. Couldn't find the class "
					+ loggerClassName, e);
		}
	}

	private void init(Class<?> loggerClass) {
		Object o;
		try {
			o = loggerClass.newInstance();
			if (o instanceof MFLogger) {
				this.loggerClass = loggerClass;
			}
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private LoggerFactory(Class<?> loggerClass) {
		init(loggerClass);

	}

	public static void initialize(String loggerClassName) {
		synchronized (LoggerFactory.class) {
			factory = new LoggerFactory(loggerClassName);
		}

	}

	public static void initialize(Class<?> loggerClass) {
		synchronized (LoggerFactory.class) {
			factory = new LoggerFactory(loggerClass);
		}

	}

	private static LoggerFactory getLoggerFactory() {
		synchronized (LoggerFactory.class) {
			return factory;
		}
	}

	private MFLogger produce(Class<?> t) {
		try {
			MFLogger logger = (MFLogger) loggerClass.newInstance();
			logger.init(t);
			return logger;
		} catch (InstantiationException e) {
			throw new IllegalStateException("Couldn't instantiate logger " + loggerClass.getCanonicalName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Couldn't instantiate logger " + loggerClass.getCanonicalName(), e);
		}
	}

	public static MFLogger getLogger(Class<?> t) {
		LoggerFactory factory = getLoggerFactory();
		if (factory == null) {
			throw new IllegalStateException("LoggerFactory has not been initialized. See LoggerFactory.initialize");
		}
		return factory.produce(t);

	}
}
