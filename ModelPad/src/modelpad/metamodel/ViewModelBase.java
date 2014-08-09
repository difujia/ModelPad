package modelpad.metamodel;

public abstract class ViewModelBase {

	protected ElementBase mModel;
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

	public ViewModelBase(ElementBase model) {
		mModel = model;
		mModel.registerObserver(mObserver);
	}

	public void releaseModel() {
		mModel.unregisterObserver(mObserver);
		mModel = null;
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
