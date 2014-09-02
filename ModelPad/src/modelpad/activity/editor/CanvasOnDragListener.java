package modelpad.activity.editor;

import modelpad.datamodel.AbstractElement;
import modelpad.datamodel.ClassViewModel;
import modelpad.datamodel.EClass;
import modelpad.datamodel.SolutionManager;
import modelpad.view.ClassView;
import modelpad.viewutils.ElementAnchor;
import modelpad.viewutils.ViewHelper;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.FrameLayout;

class CanvasOnDragListener implements OnDragListener {

	private Context mContext;
	private FrameLayout mCanvas;
	private SolutionManager mManager;

	public CanvasOnDragListener(Context ctx, FrameLayout canvas, SolutionManager sm) {
		mContext = ctx;
		mCanvas = canvas;
		mManager = sm;
	}

	@Override
	public boolean onDrag(View v, DragEvent ev) {
		DragData data = (DragData) ev.getLocalState();
		AbstractElement comingElement = data.getElement();
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
				final ClassView classView = ViewHelper.createClassView(mContext);
				classView.setViewModel(new ClassViewModel(clazz));
				// for start dragging this class title
				classView.setOnLongClickListenerForTitleView(LongClickToDragListener.builder(clazz).build());
				// for move the class view
				// for create reference
				classView.setOnDragListener(new ClassOnDragListener(mContext, mCanvas, clazz, mManager));
				// for add attribute
				classView.setOnDragListenerForAttrSection(new AttrSectionOnDragListener(mContext, clazz, mManager));

				ElementAnchor anchor = new ElementAnchor(classView);
				classView.setOnClickListenerForTitleView(new ClickToRemoveListener.Builder(mContext, anchor)
						.with(classView).add(clazz).build());

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