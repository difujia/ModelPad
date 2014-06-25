package modelpad.model;

import java.util.HashMap;
import java.util.Map;

import android.view.View;

public class DragData {

	private static final String SOURCE_VIEW = "SourceView";
	private static final String ELEMENT = "Element";

	private Map<String, Object> dataHolder = new HashMap<>();

	public View getSourceView() {
		return (View) dataHolder.get(SOURCE_VIEW);
	}

	public NamedElement getElement() {
		return (NamedElement) dataHolder.get(ELEMENT);
	}

	void setSourceView(View v) {
		dataHolder.put(SOURCE_VIEW, v);
	}

	void setElement(NamedElement e) {
		dataHolder.put(ELEMENT, e);
	}

}
