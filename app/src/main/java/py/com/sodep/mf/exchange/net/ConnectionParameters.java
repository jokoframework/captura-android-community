package py.com.sodep.mf.exchange.net;

public class ConnectionParameters {

	/**
	 * This values shouldn't be null
	 */
	private final String baseURL;
	private final String user;
	private final String password;

	/**
	 * ApplicationId may be null
	 */
	private Long applicationId;

	public ConnectionParameters(String baseURL, String user, String password) {
		this(baseURL, user, password, null);
	}

	public ConnectionParameters(String baseURL, String user, String password, Long applicationId) {
		this.baseURL = baseURL;
		this.user = user;
		this.password = password;
		this.applicationId = applicationId;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

}
