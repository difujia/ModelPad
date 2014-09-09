package modelpad.datamodel;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects.ToStringHelper;

public class EReferenceInfo extends AbstractElement {

	private static final long serialVersionUID = -1241253000622710767L;

	private String name;
	private boolean containment;
	private String lowerbound;
	private String upperbound;

	private EReference owner;

	/**
	 * DO NOT USE. This is for xml serialization
	 */
	protected EReferenceInfo() {}

	protected EReferenceInfo(String name, String lowerbound, String upperbound) {
		this.name = name;
		this.lowerbound = lowerbound;
		this.upperbound = upperbound;
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
	protected boolean lookslike(AbstractElement other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EReferenceInfo that = (EReferenceInfo) other;
		return name.equals(that.name) && containment == that.containment && lowerbound.equals(that.lowerbound)
				&& upperbound.equals(that.upperbound);
	}

	@Override
	protected boolean match(AbstractElement other) {
		if (lookslike(other)) {
			EReferenceInfo that = (EReferenceInfo) other;
			return checkNotNull(owner).match(checkNotNull(that.owner));
		} else {
			return false;
		}
	}

	public boolean isContainment() {
		return containment;
	}

	public String getLowerbound() {
		return lowerbound;
	}

	public String getUpperbound() {
		return upperbound;
	}

	protected void setOwner(EReference owner) {
		this.owner = owner;
	}

	protected void removeFromOwner() {
		if (owner != null) {
			owner.clearInfo(this);
			owner = null;
		}
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("containment", isContainment())	//
				.add("lowerBound", getLowerbound())							//
				.add("upperBound", getUpperbound());
	}
}
