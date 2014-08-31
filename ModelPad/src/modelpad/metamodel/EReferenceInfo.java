package modelpad.metamodel;

import com.google.common.base.Objects.ToStringHelper;

public class EReferenceInfo extends ElementBase {

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
		if (owner != null) {
			owner.clearInfo();
			owner = null;
		}
		super.dispose();
	}

	@Override
	protected boolean lookslike(ElementBase other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EReferenceInfo that = (EReferenceInfo) other;
		return name.equals(that.name) && containment == that.containment && lowerbound.equals(that.lowerbound)
				&& upperbound.equals(that.upperbound);
	}

	@Override
	protected boolean match(ElementBase other) {
		if (lookslike(other)) {
			EReferenceInfo that = (EReferenceInfo) other;
			return owner.match(that.owner);
		} else {
			return false;
		}
	}

	protected boolean isContainment() {
		return containment;
	}

	protected void setContainment(boolean containment) {
		this.containment = containment;
	}

	protected String getLowerBound() {
		return lowerbound;
	}

	protected void setLowerBound(String lowerbound) {
		this.lowerbound = lowerbound;
	}

	protected String getUpperBound() {
		return upperbound;
	}

	protected void setUpperBound(String upperBound) {
		upperbound = upperBound;
	}

	protected void setOwner(EReference owner) {
		EReference oldOwner = this.owner;
		if (oldOwner != null) {
			oldOwner.clearInfo();
		}
		this.owner = owner;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("containment", isContainment())	//
				.add("lowerBound", getLowerBound())							//
				.add("upperBound", getUpperBound());
	}
}
