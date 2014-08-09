package modelpad.metamodel;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Solution implements Serializable {

	private static final long serialVersionUID = 1904045626094957288L;

	private Set<EClass> structure = new HashSet<>();

	void addEClass(EClass clazz) {
		structure.add(clazz);
	}

	void removeEClass(EClass clazz) {
		structure.remove(clazz);
	}

	boolean hasEClass(EClass clazz) {
		return structure.contains(clazz);
	}

	Set<EClass> getStructuredClasses() {
		return Collections.unmodifiableSet(structure);
	}
}
