package py.com.sodep.mf.exchange.mock;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import py.com.sodep.mf.exchange.LoggerFactory;
import py.com.sodep.mf.exchange.MFLogger;

/**
 * This logger shouldn't be used in production, because it writes the output
 * directly to the {@link System#out} stream. It purpose is to have a simple
 * implementation that can be used in testing
 * 
 * @author danicricco
 * 
 */
public class SimpleLogger implements MFLogger {

	private String className;

	@Override
	public void init(Class<?> c) {
		this.className = c.getSimpleName();
	}

	private void print(PrintStream p, String msg, Throwable e) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss:SSS");
		String instance = format.format(new Date());
		p.println(instance + " [" + className + "]: " + msg);
		if (e != null) {
			e.printStackTrace(p);
		}
	}

	@Override
	public void trace(String msg) {
		if (SimpleLoggerConfigurator.isTrace()) {
			print(System.out, msg, null);
		}

	}

	@Override
	public void debug(String msg) {
		if (SimpleLoggerConfigurator.isDebug()) {
			print(System.out, msg, null);
		}

	}

	@Override
	public void info(String msg) {
		if (SimpleLoggerConfigurator.isInfo()) {
			print(System.out, msg, null);
		}

	}

	@Override
	public void error(String msg) {
		if (SimpleLoggerConfigurator.isError()) {
			print(System.out, msg, null);
		}

	}

	@Override
	public void debug(String msg, Throwable e) {

		if (SimpleLoggerConfigurator.isDebug()) {
			print(System.out, msg, e);
		}
	}

	@Override
	public void info(String msg, Throwable e) {
		if (SimpleLoggerConfigurator.isInfo()) {
			print(System.out, msg, e);
		}

	}

	@Override
	public void error(String msg, Throwable e) {
		if (SimpleLoggerConfigurator.isError()) {
			print(System.out, msg, e);
		}
	}

	public static void main(String[] args) {
		// instruct the loggerfactory to use the SimpleLogger
		LoggerFactory.initialize(SimpleLogger.class);
		// instantiate the loggerConfigurator and customize the log level
		SimpleLoggerConfigurator instance = SimpleLoggerConfigurator.getInstance();
		instance.setTrace(true);
		// message
		MFLogger t = LoggerFactory.getLogger(String.class);
		t.debug("A message");
	}

}
