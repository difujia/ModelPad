package modelpad.activity.editor;

import modelpad.datamodel.AbstractElement;
import modelpad.datamodel.AbstractViewModel;
import modelpad.datamodel.EAttribute;
import modelpad.datamodel.EClass;
import modelpad.datamodel.ModelFactory;
import modelpad.datamodel.SolutionManager;
import modelpad.view.ElementView;
import modelpad.view.SectionView;
import modelpad.viewutils.ElementAnchor;
import modelpad.viewutils.ViewHelper;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

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
		AbstractElement comingElement = data.getElement();
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
				final AbstractViewModel viewModel = ModelFactory.createViewModel(attr);
				attrView = ViewHelper.createClassSectionItem(mContext);
				attrView.setViewModel(viewModel);
				attrView.setOnLongClickListener(LongClickToDragListener.builder(attr).with(new CompletionHandler() {

					@Override
					public void complete(boolean consumed) {
						if (consumed) {
							viewModel.releaseModel();
							thisSection.removeView(attrView);
						}
					}
				}).build());

				ElementAnchor anchor = new ElementAnchor(attrView);
				attrView.setOnClickListener(new ClickToRemoveListener.Builder(mContext, anchor).with(attrView)
						.add(attr).build());

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