package modelpad.activity.editor;

import modelpad.activity.editor.DragData.CompletionHandler;
import modelpad.metamodel.ElementBase;
import modelpad.view.StateResponder;
import modelpad.viewutils.Anchor;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class ElementClickToRemoveListener implements OnClickListener {

	private static final String TAG = "ElementClick";
	private Context mContext;
	private Anchor mAnchor;
	private StateResponder mResponder;
	private ElementBase[] mElements;
	private CompletionHandler mHandler = DragData.DummyHandler;

	public ElementClickToRemoveListener(Context context, Anchor anchor, StateResponder responder,
			ElementBase... elements) {
		mContext = context;
		mAnchor = anchor;
		mResponder = responder;
		mElements = elements;
	}

	public ElementClickToRemoveListener with(CompletionHandler handler) {
		mHandler = handler;
		return this;
	}

	@Override
	public void onClick(View v) {
		mResponder.beTarget();
		final PopupWindow popup = new PopupWindow(mContext);
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mResponder.beNormal();
			}
		});

		int[] location = new int[2];
		mAnchor.getTopHookOnScreen(location);

		int x = location[0];
		int y = location[1];

		View contentView = createContentView();
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ElementBase element : mElements) {
					element.dispose();
				}
				popup.dismiss();
				mHandler.complete(true);
			}
		});

		popup.setContentView(contentView);
		popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popup.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), ""));
		popup.setOutsideTouchable(true);

		contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int popupX = x - contentView.getMeasuredWidth() / 2;
		int popupY = y - contentView.getMeasuredHeight();
		popup.showAtLocation(mAnchor.getAnchorView(), Gravity.NO_GRAVITY, popupX, popupY);
	}

	private View createContentView() {
		Button button = new Button(mContext);
		button.setText("remove");
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setTextColor(Color.WHITE);
		button.setBackgroundColor(Color.BLACK);

		return button;
	}
}
