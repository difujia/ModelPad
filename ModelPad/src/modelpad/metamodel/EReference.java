package modelpad.metamodel;

import com.google.common.base.Objects.ToStringHelper;

public class EReference extends ElementBase {

	private static final long serialVersionUID = 5707299262724241156L;

	private EClass source;
	private EClass target;
	private EReference opposite;
	private EReferenceInfo info;

	/**
	 * DO NOT USE. This is for xml serialization
	 */
	EReference() {}

	EReference(EClass source, EClass target) {
		this.source = source;
		this.target = target;
		this.info = ModelFactory.createInfoPlaceHolder();
		this.info.setOwner(this);
	}

	@Override
	public String getName() {
		return info.getName();
	}

	@Override
	public void dispose() {
		super.dispose();
		info.dispose();
		source.removeRef(this);
		source = null;
		target = null;
		if (opposite != null) {
			opposite.removeOpposite();
			opposite = null;
		}
	}

	@Override
	public boolean lookslike(ElementBase other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EReference that = (EReference) other;
		return info.lookslike(that.info);
	}

	@Override
	public boolean match(ElementBase other) {
		if (lookslike(other)) {
			EReference that = (EReference) other;
			return source.match(that.source) && target.match(that.target);
		} else {
			return false;
		}
	}

	void setInfo(EReferenceInfo info) {
		EReferenceInfo oldInfo = this.info;
		oldInfo.dispose();
		this.info = info;
		this.info.setOwner(this);
		notifyDataChanged();
	}

	EReferenceInfo getInfo() {
		return info;
	}

	void clearInfo() {
		info = ModelFactory.createInfoPlaceHolder();
		info.setOwner(this);
		notifyDataChanged();
	}

	boolean isContainment() {
		return info.isContainment();
	}

	void setContainment(boolean containment) {
		info.setContainment(containment);
	}

	String getLowerBound() {
		return info.getLowerBound();
	}

	void setLowerBound(String lowerBound) {
		info.setLowerBound(lowerBound);
	}

	String getUpperBound() {
		return info.getUpperBound();
	}

	void setUpperBound(String upperBound) {
		info.setUpperBound(upperBound);
	}

	EClass getTarget() {
		return target;
	}

	EReference getOpposite() {
		return opposite;
	}

	void setOpposite(EReference opposite) {
		removeOpposite();
		this.opposite = opposite;
	}

	private void removeOpposite() {
		opposite = null;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()					//
				.add("source", source.getName())		//
				.add("target", target.getName())		//
				.add("opposite", opposite.getName())	//
				.add("containment", isContainment())	//
				.add("lowerbound", getLowerBound())			//
				.add("upperbound", getUpperBound());
	}

}
