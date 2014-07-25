package modelpad.view;

import java.util.HashSet;
import java.util.Set;

import modelpad.activity.R;
import modelpad.model.ElementViewModel;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.widget.TextView;

public class ElementView extends TextView implements DropZone {

	private static final String TAG = "ElementView";

	private ElementViewModel mElementVM;
	private DataSetObserver mObserver = new DataSetObserver() {
		public void onChanged() {
			update();
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
			setAlpha(0.1f);
			setMinWidth(80);
			setMinHeight(36);
		} else {
			setAlpha(1);
			setMinWidth(0);
		}
	}

	public void setViewModel(ElementViewModel viewModel) {
		ElementViewModel oldViewModel = mElementVM;
		if (oldViewModel != null) {
			oldViewModel.unregisterObserver(mObserver);
		}
		mElementVM = viewModel;
		mElementVM.registerObserver(mObserver);
		update();
	}

	@Override
	public void onNotify() {
		setAlpha(1);
		setBackgroundResource(R.drawable.bg_element_highlight);
	}

	@Override
	public void onHover() {
		setAlpha(1);
		setBackgroundResource(R.drawable.bg_element_hover);
	}

	@Override
	public void onFinish() {
		updateAlpha();
		setBackgroundResource(R.drawable.bg_element_normal);
	}

	public void registerNodeListener(ViewGeoChangeListener listener) {
		listeners.add(listener);
	}

	public void removeNodeListener(ViewGeoChangeListener listener) {
		listeners.remove(listener);
	}

	public void notifyViewChange() {
		for (ViewGeoChangeListener l : listeners) {
			l.nodeGeoChanged(this);
		}
	}
}
