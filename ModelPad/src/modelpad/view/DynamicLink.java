package modelpad.view;

import android.graphics.PointF;

interface DynamicLink {
	void update(float posX, float posY, PointF vector, int numOfCorners);
}
