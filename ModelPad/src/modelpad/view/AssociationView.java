package modelpad.view;

import modelpad.activity.R;
import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;

public class AssociationView extends ViewGroup implements DynamicLink {

	private ElementView labelA;
	private ElementView labelB;
	private LinkView link;
	private View anchorA;
	private View anchorB;

	public AssociationView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		View.inflate(context, R.layout.link_components, this);
		labelA = (ElementView) findViewById(R.id.link_lableA);
		labelB = (ElementView) findViewById(R.id.link_lableB);
		link = (LinkView) findViewById(R.id.link);
	}

	public ElementView getLabelA() {
		return labelA;
	}

	public ElementView getLabelB() {
		return labelB;
	}

	@Override
	public void update(float posX, float posY, PointF vector, int numOfCorners) {
		setX(posX);
		setY(posY);
		requestLayout();
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int left = anchorA.getLeft() < anchorB.getLeft() ? anchorA.getLeft() : anchorB.getLeft();
		int right = anchorA.getRight() > anchorB.getRight() ? anchorA.getRight() : anchorB.getRight();
		int top = anchorA.getTop() < anchorB.getTop() ? anchorA.getTop() : anchorB.getTop();
		int bottom = anchorA.getBottom() > anchorB.getBottom() ? anchorA.getBottom() : anchorB.getBottom();
		int width = right - left;
		int height = bottom - top;
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}

}
