package modelpad.activity;

import modelpad.view.ClassView;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author difujia
 *	Not in use
 */
public class ClassOnTouchListener implements OnTouchListener {

	private int mPointerId = -1;
	private float mStartX = -1;
	private float mStartY = -1;
	private RectF mDragRect;
	private float mDragRectSideLength = 20;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mDragRect == null) {
			float left = v.getWidth() - mDragRectSideLength;
			float top = v.getHeight() - mDragRectSideLength;
			float right = v.getWidth();
			float bottom = v.getHeight();
			mDragRect = new RectF(left, top, right, bottom);
		}
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mPointerId = event.getPointerId(event.getActionIndex());
				mStartX = event.getX(mPointerId);
				mStartY = event.getY(mPointerId);
				if (!mDragRect.contains(mStartX, mStartY)) {
					// not interested in touching area outside DragRect
					reset();
					break;
				}
				return true;
			case MotionEvent.ACTION_UP:
				reset();
				return true;
			case MotionEvent.ACTION_MOVE:
				int activePointerId = event.getPointerId(event.getActionIndex());
				if (activePointerId == mPointerId) {
					float moveX = event.getX(mPointerId);
					float moveY = event.getY(mPointerId);
					float dx = moveX - mStartX;
					float dy = moveY - mStartY;
					float newX = v.getX() + dx;
					float newY = v.getY() + dy;
					ClassView node = (ClassView) v;
					node.moveTo(newX, newY);
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}

	private void reset() {
		mPointerId = -1;
		mStartX = -1;
		mStartY = -1;
	}
}
