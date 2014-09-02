package modelpad.activity.editor;

import modelpad.datamodel.AbstractElement;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;

import com.google.common.base.Optional;

class LongClickToDragListener implements OnLongClickListener {

	private AbstractElement mElement;
	private Optional<CompletionHandler> mHandler;

	private LongClickToDragListener(Builder builder) {
		mElement = builder.mElement;
		mHandler = builder.mHandler;
	}

	public static Builder builder(AbstractElement element) {
		return new Builder(element);
	}

	public static class Builder {
		private AbstractElement mElement;
		private Optional<CompletionHandler> mHandler = Optional.absent();

		public Builder(AbstractElement element) {
			mElement = element;
		}

		public Builder with(CompletionHandler handler) {
			mHandler = Optional.of(handler);
			return this;
		}

		public OnLongClickListener build() {
			return new LongClickToDragListener(this);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
		DragData.Builder builder = new DragData.Builder(mElement, v).with(mHandler);
		v.startDrag(null, shadowBuilder, builder.build(), 0);
		return true;
	}
}