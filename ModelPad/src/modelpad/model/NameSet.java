package modelpad.model;

import java.util.HashMap;

class NameSet {

	private HashMap<String, String> names;

	NameSet() {
		names = new HashMap<>();
	}

	String getNameByKey(String key) {
		return names.get(key);
	}

	void putNameAndKey(String name, String key) {
		names.put(key, name);
	}
}
