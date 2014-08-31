package modelpad.metamodel;

public abstract class ViewModelBase {

	protected ElementBase model;
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
		this.model = model;
		this.model.registerObserver(mObserver);
	}

	public void releaseModel() {
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
