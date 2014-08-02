package modelpad.activity;

import modelpad.model.ClassViewModel;
import modelpad.model.DragData;
import modelpad.model.EClass;
import modelpad.model.Element;
import modelpad.model.SolutionManager;
import modelpad.utils.ElementAnchor;
import modelpad.view.ClassView;
import modelpad.view.ViewFactory;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

class CanvasOnDragListener implements OnDragListener {

	private Context mContext;
	private ViewGroup mCanvas;
	private SolutionManager mManager;

	public CanvasOnDragListener(Context ctx, ViewGroup canvas, SolutionManager sm) {
		mContext = ctx;
		mCanvas = canvas;
		mManager = sm;
	}

	@Override
	public boolean onDrag(View v, DragEvent ev) {
		DragData data = (DragData) ev.getLocalState();
		Element comingElement = data.getElement();
		if (!(comingElement instanceof EClass)) {
			return false;
		}
		EClass clazz = (EClass) comingElement;
		if (mManager.hasClass(clazz)) {
			return false;
		}

		switch (ev.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				// modify the model
				mManager.addClass(clazz);
				// create new class element view
				final ClassView classView = ViewFactory.createClassView(mContext);
				classView.setViewModel(new ClassViewModel(clazz));
				// for start dragging this class title
				classView.setOnLongClickListenerForTitleView(new ElementLongClickToDragListener(clazz));
				// for move the class view
				// for create reference
				classView.setOnDragListener(new ClassOnDragListener(mContext, mCanvas, clazz, mManager));
				// for add attribute
				classView.setOnDragListenerForAttrSection(new AttrSectionOnDragListener(mContext, clazz, mManager));

				ElementAnchor anchor = new ElementAnchor(classView);
				classView.setOnClickListenerForTitleView(new ElementClickToRemoveListener(mContext, anchor, classView,
						clazz));

				classView.setVisibility(View.INVISIBLE);
				mCanvas.addView(classView);
				// classView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				final float dropX = ev.getX();
				final float dropY = ev.getY();
				classView.post(new Runnable() {

					@Override
					public void run() {
						float x = (dropX - classView.getWidth() / 2);
						float y = (dropY - classView.getHeight() / 2);
						classView.moveTo(x, y);
						classView.setVisibility(View.VISIBLE);
					}
				});
				data.complete(true);
				break;
		}
		return true;
	}
}