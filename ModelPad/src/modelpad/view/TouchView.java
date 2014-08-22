package modelpad.view;

import modelpad.activity.R;
import modelpad.metamodel.SimpleObserver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class TouchView extends View {

	private static final String TAG = "TouchArea";

	private Paint mPaint;

	private SimpleObserver mObserver = new SimpleObserver() {
		public void onInvalidated() {
			if (semiInvalidated) {
				Log.d(TAG, "remove touch area");
				ViewGroup parent = (ViewGroup) getParent();
				parent.removeView(TouchView.this);
			} else {
				semiInvalidated = true;
			}
		};
	};

	private boolean semiInvalidated = false;

	public TouchView(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.touchable));
		mPaint.setStyle(Style.FILL);
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
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
	}
}
