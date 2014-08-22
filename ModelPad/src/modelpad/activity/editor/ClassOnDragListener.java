package modelpad.activity.editor;

import modelpad.metamodel.EClass;
import modelpad.metamodel.EReference;
import modelpad.metamodel.ElementBase;
import modelpad.metamodel.ReferenceViewModel;
import modelpad.metamodel.SolutionManager;
import modelpad.view.ClassView;
import modelpad.view.CompositeStateResponder;
import modelpad.view.ElementView;
import modelpad.view.LinkView;
import modelpad.view.TouchView;
import modelpad.viewutils.LinkAnchor;
import modelpad.viewutils.LinkBinder;
import modelpad.viewutils.ViewFactory;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

public class ClassOnDragListener implements OnDragListener {

	private ViewGroup mCanvas;
	private Context mContext;
	private EClass mClass;
	private SolutionManager mManager;

	public ClassOnDragListener(Context ctx, ViewGroup canvas, EClass c, SolutionManager sm) {
		mContext = ctx;
		mCanvas = canvas;
		mClass = c;
		mManager = sm;
	}

	@Override
	public boolean onDrag(View v, DragEvent ev) {
		DragData data = (DragData) ev.getLocalState();
		ElementBase comingElement = data.getElement();

		if (!(comingElement instanceof EClass)) {
			return false;
		}

		EClass thatClass = (EClass) data.getElement();
		if (!mManager.hasClass(thatClass)) {
			return false;
		}

		ClassView self = (ClassView) v;

		switch (ev.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				self.beActive();
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				// TODO connect two views with dash line
				self.beTarget();
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				// TODO remove dash line
				self.beActive();
				break;
			case DragEvent.ACTION_DROP:
				ClassView that = (ClassView) data.getSourceView().getParent();
				if (self != that) {
					if (mManager.isReferenced(mClass, thatClass)) {
						// TODO two classes already cross-referenced
					} else {
						EReference selfToThat = mManager.makeRefBetween(mClass, thatClass);
						EReference thatToSelf = mManager.makeRefBetween(thatClass, mClass);
						mManager.pairRefs(selfToThat, thatToSelf);

						ElementView labelForSelf = ViewFactory.createRefLabelView(mContext);
						labelForSelf.setOnDragListener(new RefLabelOnDragListener(mContext, selfToThat, mManager));
						labelForSelf.setViewModel(new ReferenceViewModel(selfToThat));

						ElementView labelForThat = ViewFactory.createRefLabelView(mContext);
						labelForThat.setOnDragListener(new RefLabelOnDragListener(mContext, thatToSelf, mManager));
						labelForThat.setViewModel(new ReferenceViewModel(thatToSelf));

						mCanvas.addView(labelForSelf, 0);
						mCanvas.addView(labelForThat, 0);

						// link
						LinkView linkView = new LinkView(mContext);
						CompositeStateResponder responders = new CompositeStateResponder(labelForSelf, labelForThat,
								linkView);

						// click delegate view
						TouchView touchArea = ViewFactory.createTouchArea(mContext);
						LinkAnchor anchor = new LinkAnchor(touchArea);
						touchArea.setOnClickListener(new ClickToRemoveListener.Builder(mContext, anchor)
								.with(responders).add(selfToThat, thatToSelf).build());
						mCanvas.addView(touchArea, 0);
						mCanvas.addView(linkView, 0);

						selfToThat.registerObserver(linkView.getObserver(), touchArea.getObserver());
						thatToSelf.registerObserver(linkView.getObserver(), touchArea.getObserver());

						LinkBinder binder = LinkBinder
								.from(self, labelForThat, that, labelForSelf, linkView, touchArea);
						self.registerNodeListener(binder);
						labelForSelf.registerNodeListener(binder);
						that.registerNodeListener(binder);
						labelForThat.registerNodeListener(binder);
					}
				} else {
					// TODO implement create ref to self

				}
				data.complete(true);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				// TODO remove dash line
				self.beNormal();
				break;
		}
		return true;
	}
}