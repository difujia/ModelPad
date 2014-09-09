package modelpad.datamodel;

import static com.google.common.base.Preconditions.checkState;

public abstract class AbstractViewModel {

	protected AbstractElement model;
	private SimpleObservable mObservable = new SimpleObservable();
	private SimpleObserver mObserver = new SimpleObserver() {

		public void onChanged() {
			mObservable.notifyChanged();
		};

		public void onInvalidated() {
			mObservable.notifyInvalidated();
			unregisterAllObservers();
		};
	};

	public AbstractViewModel(AbstractElement model) {
		this.model = model;
		this.model.registerObserver(mObserver);
	}

	public void releaseModel() {
		checkState(model != null, "model has already been released!");
		model.unregisterObserver(mObserver);
		model = null;
	}

	public void registerObserver(SimpleObserver observer) {
		mObservable.registerObserver(observer);
	}

	public void unregisterObserver(SimpleObserver observer) {
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
