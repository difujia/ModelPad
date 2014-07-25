package modelpad.model;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public abstract class Element implements Recycleable {

	private String name;
	private ElementRecycler mRecycler;

	protected Element(String name) {
		this.name = name;
	}

	protected ToStringHelper toStringHelper() {
		return Objects.toStringHelper(this).add("name", getName());
	}

	public String getName() {
		return name;
	}

	@Override
	public void recycle() {
		if (mRecycler != null) {
			mRecycler.recycle(this);
		}
	}

	public void setRecycler(ElementRecycler recycler) {
		mRecycler = recycler;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Element that = (Element) obj;
		return Objects.equal(getName(), that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getName(), getClass());
	}

	@Override
	public String toString() {
		return toStringHelper().toString();
	}

}
