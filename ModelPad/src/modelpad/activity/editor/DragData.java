package modelpad.activity.editor;

import modelpad.metamodel.ElementBase;
import android.view.View;

public class DragData {

	public interface CompletionHandler {
		void complete(boolean consumed);
	}

	public static final CompletionHandler DummyHandler = new CompletionHandler() {
		@Override
		public void complete(boolean consumed) {
			// dummy does nothing
		}
	};

	private ElementBase mElement;
	private View mSourceView;
	private CompletionHandler mCompletionHandler;

	public DragData(ElementBase e) {
		mElement = e;
	}

	public ElementBase getElement() {
		return mElement;
	}

	public View getSourceView() {
		return mSourceView;
	}

	public DragData with(CompletionHandler handler) {
		mCompletionHandler = handler;
		return this;
	}

	public DragData with(View sourceView) {
		mSourceView = sourceView;
		return this;
	}

	public void complete(boolean consumed) {
		mCompletionHandler.complete(consumed);
	}
}
