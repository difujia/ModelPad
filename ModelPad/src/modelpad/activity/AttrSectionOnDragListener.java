package modelpad.activity;

import modelpad.model.AttributeViewModel;
import modelpad.model.DragData;
import modelpad.model.EAttribute;
import modelpad.model.EClass;
import modelpad.model.Element;
import modelpad.model.SolutionManager;
import modelpad.model.DragData.CompletionHandler;
import modelpad.utils.ElementAnchor;
import modelpad.view.ElementView;
import modelpad.view.SectionView;
import modelpad.view.ViewFactory;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;

public class AttrSectionOnDragListener implements OnDragListener {

	private Context mContext;
	private EClass mClass;
	private SolutionManager mManager;

	public AttrSectionOnDragListener(Context ctx, EClass c, SolutionManager sm) {
		mContext = ctx;
		mClass = c;
		mManager = sm;
	}

	@Override
	public boolean onDrag(View v, DragEvent ev) {
		DragData data = (DragData) ev.getLocalState();
		Element comingElement = data.getElement();
		if (!(comingElement instanceof EAttribute)) {
			return false;
		}
		EAttribute attr = (EAttribute) comingElement;
		final SectionView thisSection = (SectionView) v;

		switch (ev.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				thisSection.beActive();
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				thisSection.beTarget();
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				thisSection.beActive();
				break;
			case DragEvent.ACTION_DROP:
				ElementView sourceView = (ElementView) data.getSourceView();
				ViewGroup sourceParent = (ViewGroup) sourceView.getParent();
				if (sourceParent == v) {
					// drop in the same section, do nothing
					break;
				}

				mManager.addAttrToClass(attr, mClass);
				final ElementView attrView;
				attrView = ViewFactory.createSectionItemView(mContext);
				attrView.setViewModel(new AttributeViewModel(attr));
				attrView.setOnLongClickListener(new ElementLongClickToDragListener(attr)
						.with(new CompletionHandler() {

							@Override
							public void complete(boolean consumed) {
								if (consumed) {
									thisSection.removeView(attrView);
								}
							}
						}));

				ElementAnchor anchor = new ElementAnchor(attrView);
				attrView.setOnClickListener(new ElementClickToRemoveListener(mContext, anchor, attrView, attr));

				thisSection.addView(attrView);
				data.complete(true);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				thisSection.beNormal();
				break;
		}
		return true;
	}
}