package modelpad.model;

import android.view.View;

public class DragDataBuilder {

	private DragData data;

	public DragDataBuilder() {
		reset();
	}

	public DragData build() {
		return data;
	}

	public DragDataBuilder setElement(NamedElement e) {
		data.setElement(e);
		return this;
	}

	public DragDataBuilder setSourceView(View v) {
		data.setSourceView(v);
		return this;
	}

	public DragData buildAndClean() {
		DragData tmp = data;
		reset();
		return tmp;
	}

	private void reset() {
		data = new DragData();
	}
}
