package modelpad.view;

import modelpad.activity.BuildConfig;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LinkView extends View implements DynamicLink {

	public enum Orientation {
		Unspecified, Horizontal, Vertical
	}

	private final String LOG = "LinkView";

	private PointF vector = new PointF();
	private PointF from = new PointF();
	private PointF to = new PointF();
	private Path path = new Path();
	private Paint paint = new Paint();
	private int numOfCorners = 0;
	private Orientation orientation = Orientation.Unspecified;
	private float strokeWidth = 2;

	public LinkView(Context context) {
		this(context, null);
	}

	public LinkView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(strokeWidth);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = (int) (Math.abs(vector.x) > strokeWidth ? Math.abs(vector.x) : strokeWidth);
		int height = (int) (Math.abs(vector.y) > strokeWidth ? Math.abs(vector.y) : strokeWidth);
		setMeasuredDimension(width, height);
		// measure vector
		from.set(0, 0);
		to.set(vector);
		float dispX = 0;
		float dispY = 0;
		if (to.x < 0) {
			dispX = Math.abs(to.x);
		}
		if (to.y < 0) {
			dispY = Math.abs(to.y);
		}
		from.offset(dispX, dispY);
		to.offset(dispX, dispY);

		if (BuildConfig.DEBUG == false) {
			Log.d(LOG, "/******** onMeasure ********/");
			Log.d(LOG, String.format("posX: %f, posY: %f", getX(), getY()));
			Log.d(LOG, String.format("originX: %f, originY: %f", from.x, from.y));
			Log.d(LOG, String.format("vectorX: %f, vectorY: %f", vector.x, vector.y));
			Log.d(LOG, String.format("width: %d, height: %d", getMeasuredWidth(), getMeasuredHeight()));
			Log.d(LOG, "/******** onMeasure end ********/");
		}
//		Log.d(LOG, "measured");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		path.reset();
		path.moveTo(from.x, from.y);
		if (numOfCorners == 0) {
			makeStraightPath();
		} else if (numOfCorners == 1) {
			makeOneCornerPath();
		} else if (numOfCorners == 2) {
			makeTwoCornerPath();
		} else {
			throw new IllegalStateException("numOfCorner must be 0, 1 or 2");
		}

		canvas.drawPath(path, paint);
//		Log.d(LOG, "drawn");
	}

	private void makeStraightPath() {
		path.lineTo(to.x, to.y);
	}

	private void makeOneCornerPath() {
		if (orientation == Orientation.Horizontal) {
			path.rLineTo(to.x - from.x, 0);
			path.rLineTo(0, to.y - from.y);
		} else if (orientation == Orientation.Vertical) {
			path.rLineTo(0, to.y - from.y);
			path.rLineTo(to.x - from.x, 0);
		} else {
			makeStraightPath();
		}
	}

	private void makeTwoCornerPath() {
		if (orientation == Orientation.Horizontal) {
			path.rLineTo((to.x - from.x) / 2, 0);
			path.rLineTo(0, to.y - from.y);
			path.rLineTo((to.x - from.x) / 2, 0);
		} else if (orientation == Orientation.Vertical) {
			path.rLineTo(0, (to.y - from.y) / 2);
			path.rLineTo(to.x - from.x, 0);
			path.rLineTo(0, (to.y - from.y) / 2);
		} else {
			makeStraightPath();
		}
	}

	@Override
	public void update(float posX, float posY, PointF vec, int numOfCorners, Orientation o) {
//		Log.d(LOG, "update: (" + posX + "," + posY + ") v: (" + vec.x + "," + vec.y + ")");
		setX(posX);
		setY(posY);
		this.vector.set(vec);
		this.numOfCorners = numOfCorners;
		this.orientation = o;
		requestLayout();
		invalidate();
	}
}
