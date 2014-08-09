package modelpad.view;

import java.util.HashSet;
import java.util.Set;

import modelpad.activity.R;
import modelpad.metamodel.ViewModelBase;
import modelpad.metamodel.SimpleObserver;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class ElementView extends TextView implements StateResponder {

	private static final String TAG = "ElementView";

	private ViewModelBase mElementVM;
	private SimpleObserver mObserver = new SimpleObserver() {
		public void onChanged() {
			update();
		};

		public void onInvalidated() {
			Log.d(TAG, "remove view: " + mElementVM.getStringDisplay());
			ViewGroup parent = (ViewGroup) getParent();
			parent.removeView(ElementView.this);
		};
	};

	private Set<ViewGeoChangeListener> listeners = new HashSet<>();

	public ElementView(Context context) {
		super(context);
		init();
	}

	public ElementView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setBackgroundResource(R.drawable.bg_element_normal);
		updateAlpha();
	}

	private void update() {
		setText(mElementVM.getStringDisplay());
		updateAlpha();
		notifyViewChange();
	}

	private void updateAlpha() {
		if (getText().length() == 0) {
			setBackgroundResource(R.drawable.bg_element_blank);
			setAlpha(0.1f);
			setMinWidth(80);
			setMinHeight(36);
		} else {
			setBackgroundResource(R.drawable.bg_element_normal);
			setAlpha(1);
			setMinWidth(0);
		}
	}

	public void setViewModel(ViewModelBase viewModel) {
		ViewModelBase oldViewModel = mElementVM;
		if (oldViewModel != null) {
			oldViewModel.unregisterObserver(mObserver);
		}
		mElementVM = viewModel;
		mElementVM.registerObserver(mObserver);
		update();
	}

	@Override
	public void beActive() {
		setBackgroundResource(R.drawable.bg_element_active);
		setAlpha(1);
	}

	@Override
	public void beTarget() {
		setBackgroundResource(R.drawable.bg_element_target);
		setAlpha(1);
	}

	@Override
	public void beNormal() {
		setBackgroundResource(R.drawable.bg_element_normal);
		updateAlpha();
	}

	public void registerNodeListener(ViewGeoChangeListener listener) {
		listeners.add(listener);
	}

	public void removeNodeListener(ViewGeoChangeListener listener) {
		listeners.remove(listener);
	}

	public void notifyViewChange() {
		for (ViewGeoChangeListener l : listeners) {
			l.viewGeoChanged(this);
		}
	}
}
