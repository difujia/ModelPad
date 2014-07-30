package modelpad.view;

import modelpad.view.LinkView.Orientation;
import android.graphics.PointF;
import android.view.View;

public class LinkBinder implements ViewGeoChangeListener {

	private final String LOG = "LinkBinder";

	protected View mAnchorA;
	protected View manchorB;
	protected View mLabelA;
	protected View mLabelB;
	protected View mTouchArea;
	protected DynamicLink mLink;

	public LinkBinder(View anchorA, View labelA, View anchorB, View labelB, DynamicLink link, View touchArea) {
		mAnchorA = anchorA;
		manchorB = anchorB;
		mLabelA = labelA;
		mLabelB = labelB;
		mLink = link;
		mTouchArea = touchArea;
		// checkParent();
		measure();
	}

	public LinkBinder(View anchorA, View anchorB, DynamicLink link, View touchArea) {
		this(anchorA, null, anchorB, null, link, touchArea);
	}

	public LinkBinder(View anchorA, View anchorB, DynamicLink link) {
		this(anchorA, null, anchorB, null, link, null);
	}

	public void measure() {
		float centerAX = mAnchorA.getX() + mAnchorA.getWidth() / 2;
		float centerAY = mAnchorA.getY() + mAnchorA.getHeight() / 2;
		float centerBX = manchorB.getX() + manchorB.getWidth() / 2;
		float centerBY = manchorB.getY() + manchorB.getHeight() / 2;
		// A -> B
		PointF vA2B = new PointF(centerBX - centerAX, centerBY - centerAY);
		// B -> A
		PointF vB2A = new PointF(centerAX - centerBX, centerAY - centerBY);

		// position labelA
		if (mLabelA != null) {
			PointF centerLabelA = computeLabelCenter(mLabelA, mAnchorA, vA2B);
			mLabelA.setX(centerLabelA.x - mLabelA.getMeasuredWidth() / 2);
			mLabelA.setY(centerLabelA.y - mLabelA.getMeasuredHeight() / 2);
		}

		// position labelB
		if (mLabelB != null) {
			PointF centerLabelB = computeLabelCenter(mLabelB, manchorB, vB2A);
			mLabelB.setX(centerLabelB.x - mLabelB.getMeasuredWidth() / 2);
			mLabelB.setY(centerLabelB.y - mLabelB.getMeasuredHeight() / 2);
		}

		// position touch area
		if (mTouchArea != null) {
			mTouchArea.post(new Runnable() {
				
				@Override
				public void run() {
					PointF touchCenter = computeTouchAreaCenter();
					mTouchArea.setX(touchCenter.x - mTouchArea.getWidth() / 2);
					mTouchArea.setY(touchCenter.y - mTouchArea.getHeight() / 2);
					mTouchArea.setVisibility(View.VISIBLE);
				}
			});
		}

		// link
		float posX = centerAX < centerBX ? centerAX : centerBX;
		float posY = centerAY < centerBY ? centerAY : centerBY;
		int corners = 0;
		Orientation o = Orientation.Horizontal;
		if (centerAY < centerBY) {
			mLink.update(posX, posY, vA2B, corners, o);
		} else {
			mLink.update(posX, posY, vB2A, corners, o);
		}
	}

	private PointF computeLabelCenter(View label, View anchor, PointF vec) {
		float length = (float) Math.sqrt(vec.x * vec.x + vec.y * vec.y);
		PointF unitV = new PointF(vec.x / length, vec.y / length);
		int centerGapX = (label.getMeasuredWidth() + anchor.getWidth()) / 2;
		int centerGapY = (label.getMeasuredHeight() + anchor.getHeight()) / 2;
		float goodDistance = (float) Math.sqrt(centerGapX * centerGapX + centerGapY * centerGapY);
		PointF centerAnchor2Label = new PointF(unitV.x * goodDistance, unitV.y * goodDistance);
		PointF centerAnchor = new PointF(anchor.getX() + anchor.getWidth() / 2, anchor.getY() + anchor.getHeight() / 2);
		PointF result = new PointF(centerAnchor.x + centerAnchor2Label.x, centerAnchor.y + centerAnchor2Label.y);
		return result;
	}

	private PointF computeTouchAreaCenter() {
		float centerAX = mAnchorA.getX() + mAnchorA.getWidth() / 2;
		float centerAY = mAnchorA.getY() + mAnchorA.getHeight() / 2;
		float centerBX = manchorB.getX() + manchorB.getWidth() / 2;
		float centerBY = manchorB.getY() + manchorB.getHeight() / 2;
		PointF result = new PointF();
		result.x = (centerAX + centerBX) / 2;
		result.y = (centerAY + centerBY) / 2;
		return result;
	}

	@Override
	public void viewGeoChanged(View v) {
		measure();
	}
}
