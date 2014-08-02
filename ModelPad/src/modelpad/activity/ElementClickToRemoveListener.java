package modelpad.activity;

import modelpad.model.DragData.CompletionHandler;
import modelpad.model.DragData;
import modelpad.model.Element;
import modelpad.utils.Anchor;
import modelpad.view.StateResponder;
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
	private PopupWindow mPopup;
	private Element[] mElements;
	private CompletionHandler mHandler = DragData.DummyHandler;

	public ElementClickToRemoveListener(Context context, Anchor anchor, StateResponder responder, Element... elements) {
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
		mPopup = new PopupWindow(mContext);
		mPopup.setOnDismissListener(new OnDismissListener() {

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
		mPopup.setContentView(contentView);
		mPopup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopup.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), ""));
		mPopup.setOutsideTouchable(true);

		contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int popupX = x - contentView.getMeasuredWidth() / 2;
		int popupY = y - contentView.getMeasuredHeight();
		mPopup.showAtLocation(mAnchor.getAnchorView(), Gravity.NO_GRAVITY, popupX, popupY);
	}

	private View createContentView() {
		Button button = new Button(mContext);
		button.setText("remove");
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setTextColor(Color.WHITE);
		button.setBackgroundColor(Color.BLACK);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Element element : mElements) {
					element.dispose();
				}
				mPopup.dismiss();
				mHandler.complete(true);
			}
		});
		return button;
	}
}
