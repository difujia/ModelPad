package modelpad.metamodel;

import java.io.Serializable;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public abstract class ElementBase implements Serializable {

	private static final long serialVersionUID = -6464326406776920556L;

	private ElementRecycler mRecycler;
	private SimpleObservable mObservable = new SimpleObservable();

	public abstract String getName();

	public abstract boolean match(ElementBase other);

	public abstract boolean lookslike(ElementBase other);

	public void setRecycler(ElementRecycler recycler) {
		mRecycler = recycler;
	}

	public void dispose() {
		mObservable.notifyInvalidated();
		unregisterAllObservers();
		if (mRecycler != null) {
			mRecycler.recycle(this);
		}
	}

	public void registerObserver(SimpleObserver... observers) {
		for (SimpleObserver observer : observers) {
			mObservable.registerObserver(observer);
		}
	}

	public void unregisterObserver(SimpleObserver... observers) {
		for (SimpleObserver observer : observers) {
			mObservable.unregisterObserver(observer);
		}
	}

	public void unregisterAllObservers() {
		mObservable.unregisterAll();
	}

	public void notifyDataChanged() {
		mObservable.notifyChanged();
	}

	/*
	 * @Override
	 * public boolean equals(Object obj) {
	 * if (obj == null) {
	 * return false;
	 * }
	 * if (obj == this) {
	 * return true;
	 * }
	 * if (obj.getClass() != getClass()) {
	 * return false;
	 * }
	 * Element that = (Element) obj;
	 * return Objects.equal(getName(), that.getName());
	 * }
	 * @Override
	 * public int hashCode() {
	 * return Objects.hashCode(getName(), getClass());
	 * }
	 */

	protected ToStringHelper toStringHelper() {
		return Objects.toStringHelper(this).add("name", getName());
	}

	@Override
	public String toString() {
		return toStringHelper().toString();
	}

	/**
	 * Used by xstream to initialize fields that needs a default value.
	 * 
	 * @return
	 */
	private Object readResolve() {
		mObservable = new SimpleObservable();
		return this;
	}
}
