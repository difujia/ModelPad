package modelpad.view;

import modelpad.view.ClassNodeView.NodeListener;
import modelpad.view.LinkView.Orientation;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewParent;

public class LinkBinder implements NodeListener {

	private final String LOG = "LinkBinder";

	protected View anchorA;
	protected View anchorB;
	protected View labelA;
	protected View labelB;
	protected DynamicLink link;

	public LinkBinder(View anchorA, View labelA, View anchorB, View labelB, DynamicLink link) {
		this.anchorA = anchorA;
		this.anchorB = anchorB;
		this.labelA = labelA;
		this.labelB = labelB;
		this.link = link;
		checkParent();
		measure();
	}

	private void checkParent() {
		ViewParent parent = anchorA.getParent();
		if (parent != anchorB.getParent() || parent != labelA.getParent() || parent != labelB.getParent()) {
			throw new IllegalStateException("Anchors and Label must have the same parent view!");
		}
	}

	protected void measure() {
		float centerAX = anchorA.getX() + anchorA.getWidth() / 2;
		float centerAY = anchorA.getY() + anchorA.getHeight() / 2;
		float centerBX = anchorB.getX() + anchorB.getWidth() / 2;
		float centerBY = anchorB.getY() + anchorB.getHeight() / 2;

		// A -> B
		PointF vA2B = new PointF(centerBX - centerAX, centerBY - centerAY);
		PointF centerLabelA = computeLabelCenter(labelA, anchorA, vA2B);
		labelA.setX(centerLabelA.x - labelA.getMeasuredWidth() / 2);
		labelA.setY(centerLabelA.y - labelA.getMeasuredHeight() / 2);

		// B -> A
		PointF vB2A = new PointF(centerAX - centerBX, centerAY - centerBY);
		PointF centerLabelB = computeLabelCenter(labelB, anchorB, vB2A);
		labelB.setX(centerLabelB.x - labelB.getMeasuredWidth() / 2);
		labelB.setY(centerLabelB.y - labelB.getMeasuredHeight() / 2);

		// link
		float posX = centerAX < centerBX ? centerAX : centerBX;
		float posY = centerAY < centerBY ? centerAY : centerBY;
		int corners = 0;
		Orientation o = Orientation.Horizontal;
		if (centerAY < centerBY) {
			link.update(posX, posY, vA2B, corners, o);
		} else {
			link.update(posX, posY, vB2A, corners, o);
		}
	}

	private PointF computeLabelCenter(View label, View anchor, PointF v) {
		float length = (float) Math.sqrt(v.x * v.x + v.y * v.y);
		PointF unitV = new PointF(v.x / length, v.y / length);
		int centerGapX = (label.getMeasuredWidth() + anchor.getWidth()) / 2;
		int centerGapY = (label.getMeasuredHeight() + anchor.getHeight()) / 2;
		float goodDistance = (float) Math.sqrt(centerGapX * centerGapX + centerGapY * centerGapY);
		PointF centerAnchor2Label = new PointF(unitV.x * goodDistance, unitV.y * goodDistance);
		PointF centerAnchor = new PointF(anchor.getX() + anchor.getWidth() / 2, anchor.getY() + anchor.getHeight() / 2);
		PointF result = new PointF(centerAnchor.x + centerAnchor2Label.x, centerAnchor.y + centerAnchor2Label.y);
		return result;
	}

	@Override
	public void nodeMoved(View node) {
		if (node == anchorA || node == anchorB) {
			measure();
		} else {
			throw new IllegalStateException("Binder is listening to uninterested Node");
		}

	}

	@Override
	public void nodeDestroyed(View node) {
		// TODO Auto-generated method stub

	}
}
