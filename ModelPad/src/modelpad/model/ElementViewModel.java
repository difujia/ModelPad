package modelpad.model;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

public abstract class ElementViewModel {

	private static final String TAG = "ViewModel";

	private Element mModel;
	private DataSetObservable mObservable = new DataSetObservable();
	private DataSetObserver mObserver = new DataSetObserver() {

		public void onChanged() {
			// Log.d(TAG, "model changed");
			mObservable.notifyChanged();
		};

		public void onInvalidated() {
			// Log.d(TAG, "dispose view model");
			mObservable.notifyInvalidated();
			unregisterAllObservers();
		};
	};

	public ElementViewModel(Element model) {
		mModel = model;
		mModel.registerObserver(mObserver);
	}

	public void releaseModel() {
		mModel.unregisterObserver(mObserver);
		mModel = null;
	}

	public void registerObserver(DataSetObserver observer) {
		mObservable.registerObserver(observer);
	}

	public void unregisterObserver(DataSetObserver observer) {
		mObservable.unregisterObserver(observer);
	}

	public void unregisterAllObservers() {
		mObservable.unregisterAll();
	}

	public void notifyDataChanged() {
		mObservable.notifyChanged();
	}

	public abstract String getStringDisplay();
}
