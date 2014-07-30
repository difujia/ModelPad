package modelpad.model;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public abstract class Element {

	private static final String TAG = "Element";
	private String name;
	private ElementRecycler mRecycler;
	private DataSetObservable mObservable = new DataSetObservable();

	protected Element(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setRecycler(ElementRecycler recycler) {
		mRecycler = recycler;
	}

	public void dispose() {
		Log.d(TAG, "dispose " + getClass().getSimpleName() + ": " + getName());
		mObservable.notifyInvalidated();
		unregisterAllObservers();
		if (mRecycler != null) {
			mRecycler.recycle(this);
		}
	}

	public void registerObserver(DataSetObserver... observers) {
		for (DataSetObserver observer : observers) {
			mObservable.registerObserver(observer);
		}
	}

	public void unregisterObserver(DataSetObserver... observers) {
		for (DataSetObserver observer : observers) {
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
*/
	protected ToStringHelper toStringHelper() {
		return Objects.toStringHelper(this).add("name", getName());
	}

	@Override
	public String toString() {
		return toStringHelper().toString();
	}

}
