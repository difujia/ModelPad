package modelpad.activity.editor;

import modelpad.activity.editor.DragData.CompletionHandler;
import modelpad.metamodel.ElementBase;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;

class ElementLongClickToDragListener implements OnLongClickListener {

	private ElementBase mElement;
	private CompletionHandler mHandler = DragData.DummyHandler;

	public ElementLongClickToDragListener(ElementBase e) {
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