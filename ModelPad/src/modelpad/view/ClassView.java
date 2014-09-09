package modelpad.view;

import java.util.HashSet;
import java.util.Set;

import modelpad.activity.R;
import modelpad.datamodel.ClassViewModel;
import modelpad.datamodel.SimpleObserver;
import modelpad.viewutils.ViewHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClassView extends LinearLayout implements VisualResponder {

	private static final String TAG = "ClassView";
	private LinearLayout mInnerLayout;
	private ElementView mTitleView;
	private ViewGroup mAttrSection;
	private Bitmap mCornerImage;

	private Set<ViewGeoChangeListener> listeners = new HashSet<>();
	private ClassViewModel mClassVM;
	private SimpleObserver mObserver = new SimpleObserver() {

		public void onChanged() {
			update();
		}

		public void onInvalidated() {
			ViewGroup parent = (ViewGroup) getParent();
			parent.removeView(ClassView.this);
		}
	};

	private int mPointerId = -1;
	private float mStartX = -1;
	private float mStartY = -1;
	private RectF mDragRect = new RectF();
	private static final int DRAG_RECT_SIDE = 36;
	private static final int CORNER_OFFSET = 2;

	private Paint cornerPaint;

	public ClassView(Context ctx) {
		super(ctx);
		init();
	}

	public ClassView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init();
	}

	public ClassView(Context ctx, AttributeSet attrs, int defstyle) {
		super(ctx, attrs, defstyle);
		init();
	}

	private void init() {
		setWillNotDraw(false);
		setOrientation(VERTICAL);
		mInnerLayout = new LinearLayout(getContext());
		mInnerLayout.setOrientation(VERTICAL);
		mInnerLayout.setMinimumWidth(120);
		addView(mInnerLayout);
		beNormal();
		setPadding(0, 0, 0, DRAG_RECT_SIDE);

		// create title view
		mTitleView = new ElementView(getContext());
		mTitleView.setTypeface(mTitleView.getTypeface(), Typeface.BOLD);
		mTitleView.setTextSize(16);
		mTitleView.setGravity(Gravity.CENTER);
		mInnerLayout.addView(mTitleView);

		// create section view
		mAttrSection = ViewHelper.createClassSection(getContext());
		TextView sectionLabel = new TextView(getContext());
		sectionLabel.setTypeface(sectionLabel.getTypeface(), Typeface.ITALIC);
		sectionLabel.setText("attributes");
		mAttrSection.addView(sectionLabel);
		mInnerLayout.addView(mAttrSection);

		// create corner paint
		cornerPaint = new Paint();
		cornerPaint.setStyle(Style.STROKE);
		cornerPaint.setColor(Color.BLACK);
		cornerPaint.setStrokeWidth(2);

		// load image
		mCornerImage = BitmapFactory.decodeResource(getResources(), R.drawable.cross);
	}

	private void update() {
		// TODO use ViewModel to auto sych the view (maybe?)
	}

	public void setViewModel(ClassViewModel viewModel) {
		ClassViewModel oldViewModel = mClassVM;
		if (oldViewModel != null) {
			oldViewModel.unregisterObserver(mObserver);
		}
		mClassVM = viewModel;
		mClassVM.registerObserver(mObserver);
		mTitleView.setViewModel(viewModel);
		update();
	}

	public void setOnDragListenerForAttrSection(OnDragListener listener) {
		mAttrSection.setOnDragListener(listener);
	}

	public void setOnLongClickListenerForTitleView(OnLongClickListener listener) {
		mTitleView.setOnLongClickListener(listener);
	}

	public void setOnClickListenerForTitleView(OnClickListener listener) {
		mTitleView.setOnClickListener(listener);
	}

	public void moveTo(float x, float y) {
		setX(x);
		setY(y);
		notifyViewChange();
	}

	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		mTitleView.setTag(tag);
		mAttrSection.setTag(tag);
	}

	@Override
	public String toString() {
		return mTitleView.getText().toString();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float left = getWidth() - DRAG_RECT_SIDE - CORNER_OFFSET;
		float top = getHeight() - DRAG_RECT_SIDE;
		float right = getWidth() - CORNER_OFFSET;
		float bottom = getHeight() - CORNER_OFFSET;
		mDragRect.set(left, top, right, bottom);
		canvas.drawBitmap(mCornerImage, null, mDragRect, null);
		canvas.drawRect(mDragRect, cornerPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		bringToFront();
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
					float newX = getX() + dx;
					float newY = getY() + dy;
					moveTo(newX, newY);
					return true;
				}
				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		notifyViewChange();
	}

	private void reset() {
		mPointerId = -1;
		mStartX = -1;
		mStartY = -1;
	}

	public void registerNodeListener(ViewGeoChangeListener listener) {
		listeners.add(listener);
	}

	public void removeNodeListener(ViewGeoChangeListener listener) {
		listeners.remove(listener);
	}

	public void notifyViewChange() {
		for (ViewGeoChangeListener l : listeners) {
			l.viewGeoChanged(this);
		}
	}

	@Override
	public void beActive() {
		mInnerLayout.setBackgroundResource(R.drawable.bg_class_active);
	}

	@Override
	public void beTarget() {
		mInnerLayout.setBackgroundResource(R.drawable.bg_class_target);
	}

	@Override
	public void beNormal() {
		mInnerLayout.setBackgroundResource(R.drawable.bg_class_normal);
	}
}
