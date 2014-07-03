package modelpad.model;

import java.util.HashSet;
import java.util.Set;

public class EClass extends Element {

	protected EClass(String name) {
		super(name);
	}

	private Set<EClass> superTypes = new HashSet<>();
	private Set<EAttribute> attributes = new HashSet<>();
	private Set<EReference> references = new HashSet<>();

//	void copyNameFrom(Element source) {
//		this.names = source.names;
//	}

	boolean addSuperType(EClass superType) {
		if (superType == this) {
			throw new IllegalArgumentException("Super type cannot be self.");
		}
		return superTypes.add(superType);
	}

	boolean removeSuperType(EClass superType) {
		return superTypes.remove(superType);
	}

	boolean addAttr(EAttribute attr) {
		return attributes.add(attr);
	}

	boolean removeAttr(EAttribute attr) {
		return attributes.remove(attr);
	}

	boolean hasAttr(EAttribute attr) {
		return attributes.contains(attr);
	}

	boolean addRef(EReference ref) {
		return references.add(ref);
	}

	boolean removeRef(EReference ref) {
		return references.remove(ref);
	}

	@Override
	public void recycle() {
		super.recycle();
		superTypes.clear();
		superTypes = null;
		attributes.clear();
		attributes = null;
		references.clear();
		references = null;
	}

}
