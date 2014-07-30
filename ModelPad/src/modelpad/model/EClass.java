package modelpad.model;

import java.util.HashSet;

import com.google.common.base.Objects.ToStringHelper;

public class EClass extends Element {

	protected EClass(String name) {
		super(name);
	}

	private HashSet<EClass> superTypes = new HashSet<>();
	private HashSet<EAttribute> attributes = new HashSet<>();
	private HashSet<EReference> references = new HashSet<>();

	EClass[] getSuperTypes() {
		return superTypes.toArray(new EClass[superTypes.size()]);
	}

	EAttribute[] getAttributes() {
		return attributes.toArray(new EAttribute[attributes.size()]);
	}

	EReference[] getReferences() {
		return references.toArray(new EReference[references.size()]);
	}

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
		if (hasAttr(attr))
			return false;
		attr.removeFromOwner();
		attr.setOwner(this);
		return attributes.add(attr);
	}

	boolean removeAttr(EAttribute attr) {
		if (!hasAttr(attr))
			return false;
		attr.setOwner(null);
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
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("superTypes", superTypes).add("attrs", attributes).add("refs", references);
	}

	@Override
	public void dispose() {
		for (EAttribute attr : attributes.toArray(new EAttribute[attributes.size()])) {
			attr.dispose();
		}

		for (EReference ref : references.toArray(new EReference[references.size()])) {
			if (ref.getOpposite() != null) {
				ref.getOpposite().dispose();
			}
			ref.dispose();
		}
		superTypes.clear();
		attributes.clear();
		references.clear();
		super.dispose();
	}

}
