package modelpad.model;

import com.google.common.base.Objects.ToStringHelper;

public class EAttribute extends Element {

	private EClass mOwner;
	private String mType;

	EAttribute(String name, String type) {
		super(name);
		mType = type;
	}

	@Override
	public String getName() {
		return super.getName();
	}

	String getType() {
		return mType;
	}

	void setOwner(EClass owner) {
		this.mOwner = owner;
	}

	void removeFromOwner() {
		if (mOwner != null) {
			boolean modified = mOwner.removeAttr(this);
			if (!modified) {
				throw new IllegalStateException("Owner doesn't have this attribute");
			}
			mOwner = null;
		}
	}

	boolean isOwnedBy(EClass clazz) {
		return mOwner == clazz;
	}

	@Override
	public void dispose() {
		removeFromOwner();
		super.dispose();
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("type", mType);
	}

}
