package modelpad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class ElementView extends TextView implements View {

	public ElementView(Context context) {
		super(context);
	}

	public ElementView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void update(float posX, float posY) {
		// TODO Auto-generated method stub

	}
}
