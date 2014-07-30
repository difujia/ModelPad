package modelpad.model;

import android.util.Log;

import com.google.common.base.Objects.ToStringHelper;

public class EReference extends Element {

	private EClass mSource;
	private EClass mTarget;
	private EReference mOpposite;
	private EReferenceInfo mInfo;

	protected EReference(EClass source, EClass target) {
		super("");
		mSource = source;
		mTarget = target;
		mInfo = ModelFactory.createPlaceHolder();
		mInfo.setOwner(this);
	}

	@Override
	public String getName() {
		return mInfo.getName();
	}

	void setInfo(EReferenceInfo info) {
		EReferenceInfo oldInfo = mInfo;
		oldInfo.dispose();
		mInfo = info;
		mInfo.setOwner(this);
		notifyDataChanged();
	}

	void clearInfo() {
		mInfo = ModelFactory.createPlaceHolder();
		mInfo.setOwner(this);
		notifyDataChanged();
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
		removeOpposite();
		mOpposite = opposite;
	}

	private void removeOpposite() {
		mOpposite = null;
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
	public void dispose() {
		super.dispose();
		mInfo.dispose();
		mSource.removeRef(this);			
		mSource = null;
		mTarget = null;
		if (mOpposite != null) {
			mOpposite.removeOpposite();
			mOpposite = null;
		}
	}

}
