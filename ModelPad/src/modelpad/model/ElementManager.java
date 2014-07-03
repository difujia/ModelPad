package modelpad.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElementManager {

	public interface ModelChangeListener {
		void classCreated(EClass newClazz);

		void classNameChanged(EClass clazz);

		void classHasNewAttr(EClass clazz, EAttribute attr);

		void classLostAttr(EClass clazz, EAttribute attr);
	}

	private Level level;
	private List<EClass> allClasses = new ArrayList<>();
	private List<EClass> allConsumedClasses = new ArrayList<>();
	private List<EAttribute> allAttrs = new ArrayList<>();
	private List<EAttribute> allConsumedAttrs = new ArrayList<>();

	public ElementManager(Level level) {
		this.level = level;
		String[] classNames = level.getAllClassNames();
		for (String className : classNames) {
			EClass clazz = new EClass(className);
			allClasses.add(clazz);
		}
		String[] attrNames = level.getAllAttrNames();
		for (String attrName : attrNames) {
			EAttribute attr = new EAttribute(attrName);
			allAttrs.add(attr);
		}
	}

	// public boolean allowCopyName(Element from, EClass to) {
	// return !(from instanceof EClass);
	// }
	//
	// public void copyName(Element from, EClass to) {
	// if (allowCopyName(from, to) == false) {
	// throw new IllegalArgumentException();
	// }
	// to.copyNameFrom(from);
	// from.recycle();
	// }

	public EClass[] getAllClasses() {
		return allClasses.toArray(new EClass[allClasses.size()]);
	}

	public EClass[] getAllConsumedClasses() {
		return allConsumedClasses.toArray(new EClass[allConsumedClasses.size()]);
	}

	public EAttribute[] getAllAttrs() {
		return allAttrs.toArray(new EAttribute[allAttrs.size()]);
	}

	public EAttribute[] getAllConsumedAttrs() {
		return allConsumedAttrs.toArray(new EAttribute[allConsumedAttrs.size()]);
	}

	public boolean canConsumeClass(EClass clazz) {
		return !allConsumedClasses.contains(clazz);
	}

	public EClass consumeClass(EClass clazz) {
		if (canConsumeClass(clazz) == false) {
			throw new IllegalArgumentException();
		}
		allConsumedClasses.add(clazz);
		return clazz;
	}

//	public boolean allowAddAttrToClass(Element material, EClass receiver) {
//		return !(material instanceof EClass);
//	}

	/**
	 * Always remove attr from its origin, the receiver may ignore the attr if it has duplicates.
	 * 
	 * @param material
	 * @param receiver
	 * @return
	 */
	public EAttribute addAttrToClass(EAttribute attr, EClass receiver) {
//		if (allowAddAttrToClass(material, receiver) == false) {
//			throw new IllegalArgumentException();
//		}

			if (!attr.isOwnedBy(receiver)) {
				attr.removeFromOwner();
				receiver.addAttr(attr)
			}


		boolean hadAlready = receiver.hasAttr(attr);
		if (hadAlready) {
			return null;
		} else {
			receiver.addAttr(attr);
			attr.setOwner(receiver);
			return attr;
		}
	}

	public void removeAttr(EAttribute attr) {

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
}
