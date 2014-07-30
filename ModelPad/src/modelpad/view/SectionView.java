package modelpad.view;

import modelpad.activity.R;
import android.content.Context;
import android.widget.LinearLayout;

public class SectionView extends LinearLayout implements StateResponder {

	public SectionView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setOrientation(VERTICAL);
		setBackgroundResource(R.drawable.bg_section_normal);
	}

	@Override
	public void beActive() {
		setBackgroundResource(R.drawable.bg_section_active);
	}

	@Override
	public void beTarget() {
		setBackgroundResource(R.drawable.bg_section_target);
	}

	@Override
	public void beNormal() {
		setBackgroundResource(R.drawable.bg_section_normal);
	}

}
