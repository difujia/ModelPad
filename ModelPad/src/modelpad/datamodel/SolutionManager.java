package modelpad.datamodel;

import static com.google.common.base.Preconditions.checkArgument;

public class SolutionManager {

	private Solution solution = new Solution();

	public Solution getSolution() {
		return solution;
	}

	public boolean hasClass(EClass clazz) {
		return solution.hasEClass(clazz);
	}

	public void addClass(final EClass clazz) {
		checkArgument(!hasClass(clazz), "Class already in solution.");
		solution.addEClass(clazz);
		clazz.registerObserver(new SimpleObserver() {
			@Override
			public void onInvalidated() {
				solution.removeEClass(clazz);
			}
		});
	}

	public void addAttrToClass(EAttribute attr, EClass receiver) {
		receiver.addAttr(attr);
	}

	public EReference makeRefBetween(EClass from, EClass to) {
		EReference ref = new EReference(from, to);
		from.addRef(ref);
		return ref;
	}

	public void pairRefs(EReference ref1, EReference ref2) {
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

	public void changeReferenceInfo(EReference target, EReferenceInfo newInfo) {
		target.setInfo(newInfo);
	}
}
