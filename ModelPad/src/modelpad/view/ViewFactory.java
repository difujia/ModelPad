package modelpad.view;

import modelpad.activity.R;
import android.content.Context;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class ViewFactory {

	public static TextView createLabel(Context ctx) {
		ElementView label = new ElementView(ctx);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// label.setMinEms(8);
		label.setText("       ");
		label.setTextAppearance(ctx, android.R.style.TextAppearance_Holo_Medium);
		label.setBackgroundResource(R.drawable.bg_label);
		label.setLayoutParams(lp);
		label.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		return label;
	}
}
