package modelpad.model;

import java.util.HashSet;
import java.util.Set;

public class Solution {

	private Set<EClass> allClasses = new HashSet<>();

	void addEClass(EClass clazz) {
		allClasses.add(clazz);
	}

	void removeEClass(EClass clazz) {
		allClasses.remove(clazz);
	}

	boolean hasEClass(EClass clazz) {
		return allClasses.contains(clazz);
	}
}
