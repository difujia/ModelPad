package modelpad.view;

import modelpad.metamodel.SimpleObserver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class LinkTouchDelegateView extends View {

	private static final String TAG = "TouchArea";

	private SimpleObserver mObserver = new SimpleObserver() {
		public void onInvalidated() {
			if (semiInvalidated) {
				Log.d(TAG, "remove touch area");
				ViewGroup parent = (ViewGroup) getParent();
				parent.removeView(LinkTouchDelegateView.this);
			} else {
				semiInvalidated = true;
			}
		};
	};

	private boolean semiInvalidated = false;

	public LinkTouchDelegateView(Context context) {
		super(context);
	}

	public SimpleObserver getObserver() {
		return mObserver;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(48, 48);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.LTGRAY);
	}
}
