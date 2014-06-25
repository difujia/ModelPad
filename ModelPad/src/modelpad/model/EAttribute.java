package modelpad.model;

public class EAttribute extends NamedElement {

	private EClass owner;

	EAttribute(NamedElement source) {
		super(source.names, "attribute");
		source.destroy();
	}

	void setOwner(EClass owner) {
		this.owner = owner;
	}

	void removeFromOwner() {
		if (owner == null) {
			return;
		}
		owner.removeAttr(this);
		owner = null;
	}

	boolean isOwnedBy(EClass clazz) {
		return owner == clazz;
	}

	@Override
	public void destroy() {
		super.destroy();
		removeFromOwner();
	}

}
