package modelpad.activity.editor;

import modelpad.datamodel.AbstractElement;
import modelpad.datamodel.EReference;
import modelpad.datamodel.EReferenceInfo;
import modelpad.datamodel.SolutionManager;
import modelpad.view.ElementView;
import modelpad.viewutils.ElementAnchor;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

public class RefLabelOnDragListener implements OnDragListener {

	private Context mContext;
	private EReference mRef;
	private SolutionManager mManager;

	public RefLabelOnDragListener(Context ctx, EReference ref, SolutionManager sm) {
		mContext = ctx;
		mRef = ref;
		mManager = sm;
	}

	@Override
	public boolean onDrag(View v, DragEvent ev) {
		DragData data = (DragData) ev.getLocalState();
		AbstractElement comingElement = data.getElement();
		if (!(comingElement instanceof EReferenceInfo)) {
			return false;
		}
		EReferenceInfo newInfo = (EReferenceInfo) comingElement;
		final ElementView self = (ElementView) v;
		switch (ev.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				self.beActive();
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				self.beTarget();
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				self.beActive();
				break;
			case DragEvent.ACTION_DROP:
				ElementView sourceView = (ElementView) data.getSourceView();
				if (sourceView == v) {
					// drop in the same section, do nothing
					break;
				}
				mManager.changeReferenceInfo(mRef, newInfo);
				CompletionHandler handler = new CompletionHandler() {

					@Override
					public void complete(boolean consumed) {
						if (consumed) {
							self.setLongClickable(false);
							self.setClickable(false);
						}
					}
				};
				ElementAnchor anchor = new ElementAnchor(self);
				self.setOnClickListener(new ClickToRemoveListener.Builder(mContext, anchor).with(self).add(newInfo)
						.with(handler).build());

				self.setOnLongClickListener(LongClickToDragListener.builder(newInfo).with(handler).build());
				data.complete(true);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				self.beNormal();
				break;
		}
		return true;
	}

}