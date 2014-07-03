package modelpad.view;

import modelpad.view.LinkView.Orientation;
import android.graphics.PointF;

interface DynamicLink {

	void update(float posX, float posY, PointF vec, int numOfCorners, Orientation o);
}
