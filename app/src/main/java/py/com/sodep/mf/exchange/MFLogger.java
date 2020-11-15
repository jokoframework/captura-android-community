package py.com.sodep.mf.exchange;

/**
 * This is an abstract logger that can be later implemented with platform
 * specific (e.g. android)
 * 
 * @author danicricco
 * 
 */
public interface MFLogger {

	public void init(Class<?> c);

	public void trace(String string);

	public void debug(String msg);

	public void info(String msg);

	public void error(String msg);

	public void debug(String msg, Throwable e);

	public void info(String msg, Throwable e);

	public void error(String msg, Throwable e);

}
