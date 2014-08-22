package modelpad.activity.editor;

import static com.google.common.base.Preconditions.checkNotNull;
import modelpad.metamodel.ElementBase;
import android.view.View;

import com.google.common.base.Optional;

public class DragData {

	private ElementBase mElement;
	private View mSourceView;
	private Optional<CompletionHandler> mHandler;

	private DragData(Builder builder) {
		mElement = builder.mElement;
		mSourceView = builder.mSourceView;
		mHandler = builder.mCompletionHandler;
	}

	public ElementBase getElement() {
		return mElement;
	}

	public View getSourceView() {
		return mSourceView;
	}

	public void complete(boolean consumed) {
		if (mHandler.isPresent()) mHandler.get().complete(consumed);
	}

	public static class Builder {

		private ElementBase mElement;
		private View mSourceView;
		private Optional<CompletionHandler> mCompletionHandler;

		public Builder(ElementBase element, View sourceView) {
			mElement = element;
			mSourceView = sourceView;
		}

		public Builder with(CompletionHandler handler) {
			mCompletionHandler = Optional.of(handler);
			return this;
		}

		public Builder with(Optional<CompletionHandler> handler) {
			mCompletionHandler = checkNotNull(handler);
			return this;
		}

		public DragData build() {
			return new DragData(this);
		}
	}
}
