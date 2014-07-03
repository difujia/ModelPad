package modelpad.model;

public class EAttribute extends Element {

	protected EAttribute(String name) {
		super(name);
	}

	private EClass owner;

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
	public void recycle() {
		removeFromOwner();
	}

}
