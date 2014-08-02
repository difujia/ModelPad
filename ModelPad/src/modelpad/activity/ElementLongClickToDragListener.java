package modelpad.activity;

import modelpad.model.DragData;
import modelpad.model.DragData.CompletionHandler;
import modelpad.model.Element;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;

class ElementLongClickToDragListener implements OnLongClickListener {

	private Element mElement;
	private CompletionHandler mHandler = DragData.DummyHandler;

	public ElementLongClickToDragListener(Element e) {
		mElement = e;
	}

	ElementLongClickToDragListener with(CompletionHandler handler) {
		mHandler = handler;
		return this;
	}

	@Override
	public boolean onLongClick(View v) {
		DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
		DragData data = new DragData(mElement).with(v).with(mHandler);
		v.startDrag(null, shadowBuilder, data, 0);
		return true;
	}
}