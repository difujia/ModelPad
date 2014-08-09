package modelpad.metamodel;

import com.google.common.base.Objects.ToStringHelper;

public class EAttribute extends ElementBase {

	private static final long serialVersionUID = 2704337349056904489L;

	private String name;
	private EClass owner;
	private String type;

	/**
	 * DO NOT USE. This is for xml serialization
	 */
	EAttribute() {}

	EAttribute(String name, String type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		removeFromOwner();
		super.dispose();
	}

	@Override
	public boolean lookslike(ElementBase other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EAttribute that = (EAttribute) other;
		return name.equals(that.name) && type.equals(that.type);
	}

	@Override
	public boolean match(ElementBase other) {
		if (lookslike(other)) {
			EAttribute that = (EAttribute) other;
			return owner.match(that.owner);
		} else {
			return false;
		}
	}

	String getType() {
		return type;
	}

	void setOwner(EClass owner) {
		this.owner = owner;
	}

	void removeFromOwner() {
		if (owner != null) {
			boolean modified = owner.removeAttr(this);
			if (!modified) {
				throw new IllegalStateException("Owner doesn't have this attribute");
			}
			owner = null;
		}
	}

	boolean isOwnedBy(EClass clazz) {
		return owner == clazz;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("type", type);
	}

}
