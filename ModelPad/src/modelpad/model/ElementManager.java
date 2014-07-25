package modelpad.model;

public class ElementManager {

	private Solution mSolution = new Solution();

	public boolean hasClass(EClass clazz) {
		return mSolution.hasEClass(clazz);
	}

	public void addClass(EClass clazz) {
		if (hasClass(clazz)) {
			throw new IllegalStateException("Class already in use");
		}
		mSolution.addEClass(clazz);
	}

	public void removeClass(EClass clazz) {
		if (!hasClass(clazz)) {
			throw new IllegalStateException("Class not in use");
		}
		mSolution.removeEClass(clazz);
		clazz.recycle();
	}

	/**
	 * Always remove attr from its origin, the receiver may ignore the attr if it has duplicates.
	 * 
	 * @param material
	 * @param receiver
	 * @return
	 */
	public void addAttrToClass(EAttribute attr, EClass receiver) {

		if (!attr.isOwnedBy(receiver)) {
			attr.removeFromOwner();
			receiver.addAttr(attr);
			attr.setOwner(receiver);
		}
	}

	public void removeAttr(EAttribute attr) {
		attr.removeFromOwner();
		attr.recycle();
	}

	public boolean allowConnect(Element left, Element right) {
		return allowMakeRef(left, right) || allowMakeRef(right, left) || allowInherit(left, right)
				|| allowInherit(right, left);
	}

	public boolean allowMakeRef(Element from, Element to) {
		return (from instanceof EClass) && (to instanceof EClass);
	}

	public boolean allowInherit(Element superType, Element subType) {
		// TODO check super types
		return (subType instanceof EClass) && (superType instanceof EClass);
	}

	public EReference makeRefBetween(EClass from, EClass to) {
		EReference ref = new EReference(from, to);
		from.addRef(ref);
		return ref;
	}

	public void pairRefs(EReference ref1, EReference ref2) {
		ref1.removeOpposite();
		ref2.removeOpposite();
		ref1.setOpposite(ref2);
		ref2.setOpposite(ref1);
	}

	public boolean isReferenced(EClass left, EClass right) {
		for (EReference ref : left.getReferences()) {
			if (ref.getTarget().equals(right)) {
				return true;
			}
		}
		for (EReference ref : right.getReferences()) {
			if (ref.getTarget().equals(left)) {
				return true;
			}
		}
		return false;
	}

	public EReferenceInfo swapReferenceInfo(EReference target, EReferenceInfo newInfo) {
		EReferenceInfo oldInfo = target.getInfo();
		target.setInfo(newInfo);
		return oldInfo;
	}
}
