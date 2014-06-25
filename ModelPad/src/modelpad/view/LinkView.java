package modelpad.view;

import modelpad.activity.BuildConfig;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LinkView extends View implements DynamicLink {
	private final String LOG = "LinkView";

	private PointF vector = new PointF();;
	private PointF origin = new PointF();
	private Path path = new Path();
	private Paint paint = new Paint();

	public LinkView(Context context) {
		this(context, null);
	}

	public LinkView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(0);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = (int) (Math.abs(vector.x) > 0 ? Math.abs(vector.x) : 1);
		int height = (int) (Math.abs(vector.y) > 0 ? Math.abs(vector.y) : 1);
		setMeasuredDimension(width, height);
		// measure vector
		origin.set(0, 0);
		float dispX = 0;
		float dispY = 0;
		if (vector.x < 0) {
			dispX = Math.abs(vector.x);
		}
		if (vector.y < 0) {
			dispY = Math.abs(vector.y);
		}

		vector.offset(dispX, dispY);
		origin.offset(dispX, dispY);
		if (BuildConfig.DEBUG) {
			Log.d(LOG, "/******** onMeasure ********/");
			Log.d(LOG, String.format("posX: %f, posY: %f", getX(), getY()));
			Log.d(LOG, String.format("width: %d, height: %d", getWidth(), getHeight()));
			Log.d(LOG, String.format("vectorX: %f, vectorY: %f", vector.x, vector.y));
			Log.d(LOG, String.format("originX: %f, originY: %f", origin.x, origin.y));
			Log.d(LOG, "/******** onMeasure end ********/");
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		path.rewind();
		path.moveTo(origin.x, origin.y);
		path.lineTo(vector.x, vector.y);
		canvas.drawPath(path, paint);
	}

	@Override
	public void update(float posX, float posY, PointF vec, int numOfCorners) {
		// TODO Auto-generated method stub
		setX(posX);
		setY(posY);
		this.vector.set(vec);
		invalidate();
		requestLayout();
	}
}
