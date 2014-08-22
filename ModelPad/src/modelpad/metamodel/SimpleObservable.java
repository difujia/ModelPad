package modelpad.metamodel;

import java.io.Serializable;
import java.util.ArrayList;

public class SimpleObservable implements Serializable {

	private static final long serialVersionUID = 6043606514391369532L;

	private ArrayList<SimpleObserver> mObservers = new ArrayList<>();

	public void registerObserver(SimpleObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("The observer is null.");
		}
		synchronized (mObservers) {
			if (mObservers.contains(observer)) {
				return;
			}
			mObservers.add(observer);
		}
	}

	public void unregisterObserver(SimpleObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("The observer is null.");
		}
		synchronized (mObservers) {
			int index = mObservers.indexOf(observer);
			if (index == -1) {
				throw new IllegalStateException("Observer " + observer + " was not registered.");
			}
			mObservers.remove(index);
		}
	}

	public void unregisterAll() {
		synchronized (mObservers) {
			mObservers.clear();
		}
	}

	public void notifyChanged() {
		synchronized (mObservers) {
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onChanged();
			}
		}
	}

	public void notifyInvalidated() {
		synchronized (mObservers) {
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onInvalidated();;
			}
		}
	}
}
