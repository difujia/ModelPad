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
	protected EReference() {}

	protected EReference(EClass source, EClass target) {
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
	protected boolean lookslike(ElementBase other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		EReference that = (EReference) other;
		return info.lookslike(that.info);
	}

	@Override
	protected boolean match(ElementBase other) {
		if (lookslike(other)) {
			EReference that = (EReference) other;
			return source.match(that.source) && target.match(that.target);
		} else {
			return false;
		}
	}

	protected void setInfo(EReferenceInfo info) {
		EReferenceInfo oldInfo = this.info;
		oldInfo.dispose();
		this.info = info;
		this.info.setOwner(this);
		notifyDataChanged();
	}

	protected EReferenceInfo getInfo() {
		return info;
	}

	protected void clearInfo() {
		info = ModelFactory.createInfoPlaceHolder();
		info.setOwner(this);
		notifyDataChanged();
	}

	protected boolean isContainment() {
		return info.isContainment();
	}

	protected void setContainment(boolean containment) {
		info.setContainment(containment);
	}

	protected String getLowerBound() {
		return info.getLowerBound();
	}

	protected void setLowerBound(String lowerBound) {
		info.setLowerBound(lowerBound);
	}

	protected String getUpperBound() {
		return info.getUpperBound();
	}

	protected void setUpperBound(String upperBound) {
		info.setUpperBound(upperBound);
	}

	protected EClass getTarget() {
		return target;
	}

	protected EReference getOpposite() {
		return opposite;
	}

	protected void setOpposite(EReference opposite) {
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
