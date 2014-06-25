package modelpad.model;

import java.util.HashSet;
import java.util.Set;

public class ElementManager {

	public interface ModelChangeListener {
		void classCreated(EClass newClazz);

		void classNameChanged(EClass clazz);

		void classHasNewAttr(EClass clazz, EAttribute attr);

		void classLostAttr(EClass clazz, EAttribute attr);
	}

	private Set<EClass> allClasses = new HashSet<>();
	private Set<EText> allText = new HashSet<>();

	String[] textNames = new String[] { "apple", "orange", "banana", "lemon", "melon", "strawberry" };
	String[] attrNames = new String[] { "myApple", "myOrange", "myBanana", "myLemon", "myMelon",
			"myStrawberry" };
	String[] classNames = new String[] { "Apple", "Orange", "Banana", "Lemon", "Melon", "Strawberry" };

	public ElementManager() {
		for (int i = 0; i < 6; i++) {
			NameSet nameSet = new NameSet();
			nameSet.putNameAndKey(textNames[i], "text");
			nameSet.putNameAndKey(attrNames[i], "attribute");
			nameSet.putNameAndKey(classNames[i], "class");
			EText text = new EText(nameSet);
			allText.add(text);
		}
	}

	public EText[] getAllText() {
		return allText.toArray(new EText[allText.size()]);
	}

	public boolean allowCopyName(NamedElement from, EClass to) {
		return !(from instanceof EClass);
	}

	public void copyName(NamedElement from, EClass to) {
		if (allowCopyName(from, to) == false) {
			throw new IllegalArgumentException();
		}
		to.copyNameFrom(from);
		from.destroy();
	}

	public boolean allowCreateClass(NamedElement material) {
		return !(material instanceof EClass) && !allClasses.contains(material);
	}

	public EClass createClass(NamedElement material) {
		if (allowCreateClass(material) == false) {
			throw new IllegalArgumentException();
		}
		EClass clazz = new EClass(material);
		material.destroy();
		allClasses.add(clazz);
		return clazz;
	}

	public boolean allowAddAttrToClass(NamedElement material, EClass receiver) {
		return !(material instanceof EClass);
	}

	/**
	 * Always remove attr from its origin, the receiver may ignore the attr if it has duplicates.
	 * 
	 * @param material
	 * @param receiver
	 * @return
	 */
	public EAttribute addAttrToClass(NamedElement material, EClass receiver) {
		if (allowAddAttrToClass(material, receiver) == false) {
			throw new IllegalArgumentException();
		}

		EAttribute attr;
		if (material instanceof EAttribute) {
			attr = (EAttribute) material;
			if (!attr.isOwnedBy(receiver)) {
				attr.removeFromOwner();
			}
		} else {
			attr = new EAttribute(material);
			material.destroy();
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

	public boolean allowConnect(NamedElement left, NamedElement right) {
		return allowMakeRef(left, right) || allowMakeRef(right, left) || allowInherit(left, right)
				|| allowInherit(right, left);
	}

	public boolean allowMakeRef(NamedElement from, NamedElement to) {
		return (from instanceof EClass) && (to instanceof EClass);
	}

	public boolean allowInherit(NamedElement superType, NamedElement subType) {
		// TODO check super types
		return (subType instanceof EClass) && (superType instanceof EClass);
	}
}
