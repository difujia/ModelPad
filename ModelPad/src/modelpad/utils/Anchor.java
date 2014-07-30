package modelpad.utils;

import android.view.View;

public interface Anchor {

	View getAnchorView();

	void getTopHookOnScreen(int[] location);
}
