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
	EReferenceInfo() {}

	EReferenceInfo(String name, String lowerbound, String upperbound) {
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
	public boolean lookslike(ElementBase other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EReferenceInfo that = (EReferenceInfo) other;
		return name.equals(that.name) && containment == that.containment && lowerbound.equals(that.lowerbound)
				&& upperbound.equals(that.upperbound);
	}

	@Override
	public boolean match(ElementBase other) {
		if (lookslike(other)) {
			EReferenceInfo that = (EReferenceInfo) other;
			return owner.match(that.owner);
		} else {
			return false;
		}
	}

	boolean isContainment() {
		return containment;
	}

	void setContainment(boolean containment) {
		this.containment = containment;
	}

	String getLowerBound() {
		return lowerbound;
	}

	void setLowerBound(String lowerbound) {
		this.lowerbound = lowerbound;
	}

	String getUpperBound() {
		return upperbound;
	}

	void setUpperBound(String upperBound) {
		upperbound = upperBound;
	}

	void setOwner(EReference owner) {
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
