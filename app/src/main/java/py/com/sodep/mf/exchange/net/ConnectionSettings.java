package py.com.sodep.mf.exchange.net;

public class ConnectionSettings {

	/** Public static fields **/
	public static int READ_TIMEOUT = 2 * 60 * 1000;// 120 seconds

	public static int CONNECT_TIMEOUT = 10 * 1000; // 10

	/**
	 * If this is set to true, the system won't check for certificate coming
	 * from the server. This is a flag that we need during testing. Probably, in
	 * production this won't be available for the end user
	 */
	public static boolean TRUST_ALL_HOSTS = true;

	public static int PING_READ_TIMEOUT = 15 * 1000;

	public static int PING_CONNECT_TIMEOUT = 15 * 1000;

}
