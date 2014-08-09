package modelpad.viewutils;

import android.view.View;

public class LinkAnchor implements Anchor {

	private View mAnchorView;

	public LinkAnchor(View anchorView) {
		mAnchorView = anchorView;
	}

	@Override
	public View getAnchorView() {
		return mAnchorView;
	}

	@Override
	public void getTopHookOnScreen(int[] location) {
		int[] pos = new int[2];
		mAnchorView.getLocationOnScreen(pos);
		int x = pos[0] + mAnchorView.getWidth() / 2;
		int y = pos[1] + mAnchorView.getHeight() / 2;
		location[0] = x;
		location[1] = y;
	}

}
