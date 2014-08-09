package modelpad.activity.editor;

import modelpad.activity.editor.DragData.CompletionHandler;
import modelpad.metamodel.EReference;
import modelpad.metamodel.EReferenceInfo;
import modelpad.metamodel.ElementBase;
import modelpad.metamodel.SolutionManager;
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
		ElementBase comingElement = data.getElement();
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
				self.setOnClickListener(new ElementClickToRemoveListener(mContext, anchor, self, newInfo).with(handler));

				self.setOnLongClickListener(new ElementLongClickToDragListener(newInfo).with(handler));
				data.complete(true);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				self.beNormal();
				break;
		}
		return true;
	}

}