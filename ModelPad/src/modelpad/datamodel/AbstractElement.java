package modelpad.datamodel;

import java.io.Serializable;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public abstract class AbstractElement implements Serializable {

	private static final long serialVersionUID = -6464326406776920556L;

	private ElementRecycler recycler;
	private SimpleObservable observable = new SimpleObservable();

	/**
	 * Not to be initialised outside of this package.
	 */
	protected AbstractElement() {}

	protected abstract boolean match(AbstractElement other);

	protected abstract boolean lookslike(AbstractElement other);

	public abstract String getName();

	public void setRecycler(ElementRecycler recycler) {
		this.recycler = recycler;
	}

	public void dispose() {
		observable.notifyInvalidated();
		unregisterAllObservers();
		if (recycler != null) {
			recycler.recycle(this);
		}
	}

	public void registerObserver(SimpleObserver... observers) {
		for (SimpleObserver observer : observers) {
			observable.registerObserver(observer);
		}
	}

	public void unregisterObserver(SimpleObserver... observers) {
		for (SimpleObserver observer : observers) {
			observable.unregisterObserver(observer);
		}
	}

	public void unregisterAllObservers() {
		observable.unregisterAll();
	}

	public void notifyDataChanged() {
		observable.notifyChanged();
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
		observable = new SimpleObservable();
		return this;
	}
}
