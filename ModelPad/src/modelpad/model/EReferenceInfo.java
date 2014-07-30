package modelpad.model;

import com.google.common.base.Objects.ToStringHelper;

public class EReferenceInfo extends Element {

	private boolean mContainment;
	private int mLowerBound;
	private int mUpperBound;

	private EReference mOwner;

	protected EReferenceInfo(String name) {
		super(name);
	}

	boolean isContainment() {
		return mContainment;
	}

	void setContainment(boolean Containment) {
		mContainment = Containment;
	}

	int getLowerBound() {
		return mLowerBound;
	}

	void setLowerBound(int lowerBound) {
		mLowerBound = lowerBound;
	}

	int getUpperBound() {
		return mUpperBound;
	}

	void setUpperBound(int upperBound) {
		mUpperBound = upperBound;
	}

	void setOwner(EReference owner) {
		EReference oldOwner = mOwner;
		if (oldOwner != null) {
			oldOwner.clearInfo();
		}
		mOwner = owner;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("containment", isContainment())	//
				.add("lowerBound", getLowerBound())							//
				.add("upperBound", getUpperBound());
	}

	@Override
	public void dispose() {
		mOwner.clearInfo();
		mOwner = null;
		super.dispose();
	}
}
