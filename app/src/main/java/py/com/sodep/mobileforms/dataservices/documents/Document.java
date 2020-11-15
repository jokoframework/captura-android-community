package py.com.sodep.mobileforms.dataservices.documents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Document extends DocumentMetadata implements Map<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HashMap<String, String> data = new HashMap<String, String>();

	public String get(Object key) {
		return data.get(key);
	}

	public String put(String key, String value) {
		return data.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends String> map) {
		data.putAll(map);
	}

	public void clear() {
		data.clear();
	}

	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	public String remove(Object key) {
		return data.remove(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return data.containsKey(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return data.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public Collection<String> values() {
		return data.values();
	}
}
