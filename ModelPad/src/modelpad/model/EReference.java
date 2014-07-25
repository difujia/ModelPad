package modelpad.model;

import com.google.common.base.Objects.ToStringHelper;

public class EReference extends Element {

	private EClass mSource;
	private EClass mTarget;
	private EReference mOpposite;
	private EReferenceInfo mInfo = ModelFactory.getPlaceHolder();

	protected EReference(EClass source, EClass target) {
		super("");
		mSource = source;
		mTarget = target;
	}

	@Override
	public String getName() {
		return mInfo.getName();
	}

	public EReferenceInfo getInfo() {
		return mInfo;
	}

	void setInfo(EReferenceInfo info) {
		mInfo = info;
	}

	boolean isContainment() {
		return mInfo.isContainment();
	}

	void setContainment(boolean containment) {
		mInfo.setContainment(containment);
	}

	int getLowerBound() {
		return mInfo.getLowerBound();
	}

	void setLowerBound(int lowerBound) {
		mInfo.setLowerBound(lowerBound);
	}

	int getUpperBound() {
		return mInfo.getUpperBound();
	}

	void setUpperBound(int upperBound) {
		mInfo.setUpperBound(upperBound);
	}

	EClass getTarget() {
		return mTarget;
	}

	EReference getOpposite() {
		return mOpposite;
	}

	void setOpposite(EReference opposite) {
		mOpposite = opposite;
	}

	void removeOpposite() {
		if (mOpposite != null) {
			mOpposite.setOpposite(null);
			mOpposite = null;
		}
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()					//
				.add("source", mSource.getName())		//
				.add("target", mTarget.getName())		//
				.add("opposite", mOpposite.getName())	//
				.add("containment", isContainment())	//
				.add("lower", getLowerBound())			//
				.add("upper", getUpperBound());
	}

	@Override
	public void recycle() {
		mInfo.recycle();
		mSource.removeRef(this);
		mSource = null;
		mTarget = null;
		if (mOpposite != null) {
			mOpposite.removeOpposite();
			mOpposite = null;
		}
		super.recycle();
	}

}
