package modelpad.viewutils;

import modelpad.view.ViewGeoChangeListener;
import modelpad.viewutils.LinkBinder.DynamicLink.Orientation;
import android.graphics.PointF;
import android.view.View;

import com.google.common.base.Optional;

public class LinkBinder implements ViewGeoChangeListener {

	public interface DynamicLink {
		enum Orientation {
			Unspecified, Horizontal, Vertical
		}

		void update(float posX, float posY, PointF vec, int numOfCorners, Orientation o);
	}

	private final String LOG = "LinkBinder";

	protected View mAnchorA;
	protected View manchorB;
	protected Optional<View> mLabelA;
	protected Optional<View> mLabelB;
	protected Optional<View> mTouchArea;
	protected DynamicLink mLink;

	private LinkBinder(View anchorA, Optional<View> labelA, View anchorB, Optional<View> labelB, DynamicLink link,
			Optional<View> touchArea) {
		mAnchorA = anchorA;
		manchorB = anchorB;
		mLabelA = labelA;
		mLabelB = labelB;
		mLink = link;
		mTouchArea = touchArea;
		// checkParent();
		measure();
	}

	public static LinkBinder from(View anchorA, View labelA, View anchorB, View labelB, DynamicLink link, View touchArea) {
		return new LinkBinder(anchorA, Optional.of(labelA), anchorB, Optional.of(labelB), link, Optional.of(touchArea));
	}

	public static LinkBinder from(View anchorA, View anchorB, DynamicLink link, View touchArea) {
		return new LinkBinder(anchorA, Optional.<View> absent(), anchorB, Optional.<View> absent(), link,
				Optional.of(touchArea));
	}

	public static LinkBinder from(View anchorA, View anchorB, DynamicLink link) {
		return new LinkBinder(anchorA, Optional.<View> absent(), anchorB, Optional.<View> absent(), link,
				Optional.<View> absent());
	}

	public void measure() {
		float centerAX = mAnchorA.getX() + mAnchorA.getWidth() / 2;
		final float centerAY = mAnchorA.getY() + mAnchorA.getHeight() / 2;
		float centerBX = manchorB.getX() + manchorB.getWidth() / 2;
		final float centerBY = manchorB.getY() + manchorB.getHeight() / 2;
		// A -> B
		final PointF vA2B = new PointF(centerBX - centerAX, centerBY - centerAY);
		// B -> A
		final PointF vB2A = new PointF(centerAX - centerBX, centerAY - centerBY);

		// position labelA
		if (mLabelA.isPresent()) {
			positionLabel(mLabelA.get(), mAnchorA, vA2B);
		}

		// position labelB
		if (mLabelB.isPresent()) {
			positionLabel(mLabelB.get(), manchorB, vB2A);
		}

		// position touch area
		if (mTouchArea.isPresent()) {
			positionTouchArea(mTouchArea.get());
		}

		// link
		final float posX = centerAX < centerBX ? centerAX : centerBX;
		final float posY = centerAY < centerBY ? centerAY : centerBY;
		final int corners = 0;
		final Orientation o = Orientation.Horizontal;
		mAnchorA.post(new Runnable() {
			@Override
			public void run() {
				if (centerAY < centerBY) {
					mLink.update(posX, posY, vA2B, corners, o);
				} else {
					mLink.update(posX, posY, vB2A, corners, o);
				}
			}
		});
	}

	private void positionLabel(final View label, final View anchor, final PointF vec) {
		label.post(new Runnable() {
			@Override
			public void run() {
				PointF centerLabelA = computeLabelCenter(label, anchor, vec);
				label.setX(centerLabelA.x - label.getWidth() / 2);
				label.setY(centerLabelA.y - label.getHeight() / 2);
				label.setVisibility(View.VISIBLE);
			}
		});
	}

	private void positionTouchArea(final View touchArea) {
		touchArea.post(new Runnable() {
			@Override
			public void run() {
				PointF touchCenter = computeTouchAreaCenter();
				touchArea.setX(touchCenter.x - touchArea.getWidth() / 2);
				touchArea.setY(touchCenter.y - touchArea.getHeight() / 2);
				touchArea.setVisibility(View.VISIBLE);
			}
		});
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
