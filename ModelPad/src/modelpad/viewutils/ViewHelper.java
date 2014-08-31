package modelpad.viewutils;

import modelpad.activity.R;
import modelpad.view.ClassView;
import modelpad.view.ElementView;
import modelpad.view.SectionView;
import modelpad.view.TouchView;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;

public class ViewHelper {

	public static ClassView createClassView(Context ctx) {
		ClassView view = new ClassView(ctx);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		view.setBackgroundResource(R.drawable.bg_class_normal);
		view.setDividerDrawable(ctx.getResources().getDrawable(R.drawable.divider_horizontal));
		view.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
		return view;
	}

	public static ElementView createListSectionView(Context ctx) {
		ElementView view = new ElementView(ctx);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		view.setTextSize(12);
		view.setGravity(Gravity.CENTER);
		return view;
	}

	public static ElementView createListItemView(Context ctx) {
		ElementView view = new ElementView(ctx);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		view.setTextSize(16);
		view.setGravity(Gravity.CENTER);
		return view;
	}

	public static ViewGroup createClassSection(Context ctx) {
		SectionView section = new SectionView(ctx);
		return section;
	}

	public static ElementView createClassSectionItem(Context ctx) {
		ElementView view = new ElementView(ctx);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
		view.setTextSize(16);
		return view;
	}

	public static ElementView createRefLabelView(Context ctx) {
		ElementView view = new ElementView(ctx);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		view.setTextSize(16);
		view.setVisibility(View.INVISIBLE);
		view.setMinHeight(36);
		return view;
	}

	public static TouchView createTouchArea(Context ctx) {
		TouchView view = new TouchView(ctx);
		view.setVisibility(View.INVISIBLE);
		return view;
	}
}
