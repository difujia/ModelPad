package modelpad.view;

import java.util.HashSet;
import java.util.Set;

import modelpad.activity.R;
import modelpad.model.ClassViewModel;
import android.content.Context;
import android.database.DataSetObserver;
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

public class ClassView extends LinearLayout implements DropZone {

	private ElementView mTitleView;
	private ViewGroup mAttrSection;

	private Set<ViewGeoChangeListener> listeners = new HashSet<>();
	private ClassViewModel mClassVM;
	private DataSetObserver mObserver = new DataSetObserver() {
		public void onChanged() {
			//TODO
		};
	};

	private int mPointerId = -1;
	private float mStartX = -1;
	private float mStartY = -1;
	private RectF mDragRect = new RectF();
	private float mDragRectSideLength = 25;

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
		setOrientation(VERTICAL);
		setBackgroundResource(R.drawable.bg_class_normal);
		// create title view
		mTitleView = new ElementView(getContext());
		mTitleView.setBackgroundResource(R.drawable.bg_class_title);
		mTitleView.setTypeface(mTitleView.getTypeface(), Typeface.BOLD);
		mTitleView.setTextSize(16);
		mTitleView.setGravity(Gravity.CENTER);
		addView(mTitleView);

		// create section view
		mAttrSection = ViewFactory.createSection(getContext());
		TextView sectionLabel = new TextView(getContext());
		sectionLabel.setTypeface(sectionLabel.getTypeface(), Typeface.ITALIC);
		sectionLabel.setText("attributes");
		mAttrSection.addView(sectionLabel);
		addView(mAttrSection);

		// create corner paint
		cornerPaint = new Paint();
		cornerPaint.setStyle(Style.FILL);
		cornerPaint.setColor(Color.BLUE);
	}
	
	private void update() {
		// TODO use ViewModel to auto sych the view
	}

	public void setViewModel(ClassViewModel viewModel) {
		ClassViewModel oldViewModel = mClassVM;
		if (oldViewModel != null) {
			oldViewModel.unregisterObserver(mObserver);
		}
		mClassVM = viewModel;
		mClassVM.registerObserver(mObserver);
		mTitleView.setViewModel(mClassVM);
	}

	public void setOnDragListenerForAttrSection(OnDragListener listener) {
		mAttrSection.setOnDragListener(listener);
	}

	public void setOnLongClickListenerForTitleView(OnLongClickListener listener) {
		mTitleView.setOnLongClickListener(listener);
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
		// float left = getWidth() - mDragRectSideLength;
		float left = 0;
		float top = getHeight() - mDragRectSideLength;
		float right = getWidth();
		float bottom = getHeight();
		mDragRect.set(left, top, right, bottom);
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
		return false;
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
			l.nodeGeoChanged(this);
		}
	}

	@Override
	public void onNotify() {
		setBackgroundResource(R.drawable.bg_class_highlight);
	}

	@Override
	public void onHover() {
		setBackgroundResource(R.drawable.bg_class_hover);
	}

	@Override
	public void onFinish() {
		setBackgroundResource(R.drawable.bg_class_normal);
	}
}
