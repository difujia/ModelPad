package modelpad.activity.editor;

import java.util.Collections;
import java.util.Set;

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

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

public class ClickToRemoveListener implements OnClickListener {

	private static final String TAG = "ElementClick";

	private Context mContext;
	private Anchor mAnchor;
	private Set<ElementBase> mElements;
	private Optional<StateResponder> mResponder;
	private Optional<CompletionHandler> mHandler;

	private ClickToRemoveListener(Builder builder) {
		mContext = builder.mContext;
		mAnchor = builder.mAnchor;
		mResponder = builder.mResponder;
		mElements = builder.mElements;
		mHandler = builder.mHandler;
	}

	public static class Builder {
		private Context mContext;
		private Anchor mAnchor;
		private Set<ElementBase> mElements = Sets.newHashSet();;
		private Optional<StateResponder> mResponder = Optional.absent();
		private Optional<CompletionHandler> mHandler = Optional.absent();

		public Builder(Context mContext, Anchor mAnchor) {
			this.mContext = mContext;
			this.mAnchor = mAnchor;
		}

		public Builder with(StateResponder responder) {
			this.mResponder = Optional.of(responder);
			return this;
		}

		public Builder with(CompletionHandler handler) {
			this.mHandler = Optional.of(handler);
			return this;
		}

		public Builder add(ElementBase... elements) {
			Collections.addAll(mElements, elements);
			return this;
		}

		public OnClickListener build() {
			return new ClickToRemoveListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		final PopupWindow popup = new PopupWindow(mContext);
		if (mResponder.isPresent()) {
			mResponder.get().beTarget();
			popup.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					mResponder.get().beNormal();
				}
			});
		}

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
				if (mHandler.isPresent()) mHandler.get().complete(true);
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
