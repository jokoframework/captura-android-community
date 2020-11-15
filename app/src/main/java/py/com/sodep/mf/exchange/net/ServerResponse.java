package py.com.sodep.mf.exchange.net;

import java.util.Map;

public class ServerResponse {

	private boolean reachable;

	private Map<String, String> settings;

	private String cause;

	public boolean isReachable() {
		return reachable;
	}

	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

}
