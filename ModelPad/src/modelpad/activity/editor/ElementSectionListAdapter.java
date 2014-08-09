package modelpad.activity.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import modelpad.activity.editor.DragData.CompletionHandler;
import modelpad.metamodel.ElementBase;
import modelpad.metamodel.ElementRecycler;
import modelpad.metamodel.ViewModelBase;
import modelpad.metamodel.ModelFactory;
import modelpad.view.ElementView;
import modelpad.view.ViewFactory;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

public class ElementSectionListAdapter extends ArrayAdapter<ElementBase> implements PinnedSectionListAdapter,
		ElementRecycler {

	private static final String LOG = "ElementAdapter";
	private static final int ITEM = 0;
	private static final int SECTION = 1;

	private List<ElementBase> sectionObjects = new ArrayList<>();

	public ElementSectionListAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		ElementBase item = getItem(position);
		return sectionObjects.contains(item) ? SECTION : ITEM;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ElementView view;
		if (convertView == null) {
			view = ViewFactory.createListItemView(getContext());
		} else {
			view = (ElementView) convertView;
		}

		final ElementBase item = getItem(position);

		// prevent unwanted registration because of scrolling
		item.unregisterAllObservers();

		final ViewModelBase viewModel = ModelFactory.createViewModel(item);
		view.setViewModel(viewModel);

		if (sectionObjects.contains(item)) {
			view.setBackgroundColor(Color.YELLOW);
			// section row should not be clickable
			view.setLongClickable(false);
		} else {
			view.setBackgroundColor(Color.WHITE);
			view.setOnLongClickListener(new ElementLongClickToDragListener(item).with(new CompletionHandler() {
				@Override
				public void complete(boolean consumed) {
					if (consumed) {
						viewModel.releaseModel();
						remove(item);
					}
				}
			}));
		}
		return view;
	}

	public void addSectionObject(ElementBase sectionObj) {
		sectionObjects.add(sectionObj);
		super.add(sectionObj);
	}

	@Override
	public void add(ElementBase item) {
		for (ElementBase sectionObj : sectionObjects) {
			if (isItemOfSection(item, sectionObj)) {
				int sectionPosition = getPosition(sectionObj);
				super.insert(item, sectionPosition + 1);
				item.setRecycler(this);
			}
		}
	}

	@Override
	public void addAll(Collection<? extends ElementBase> collection) {
		for (ElementBase obj : collection) {
			add(obj);
		}
	}

	@Override
	public void addAll(ElementBase... items) {
		for (ElementBase obj : items) {
			add(obj);
		}
	}

	@Override
	public void remove(ElementBase item) {
		if (sectionObjects.contains(item)) {
			throw new IllegalArgumentException("Cannot remove section objects.");
		}
		super.remove(item);
	}

	@Override
	public void clear() {
		sectionObjects.clear();
		super.clear();
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == SECTION;
	}

	private boolean isItemOfSection(ElementBase item, ElementBase sectionItem) {
		return item.getClass() == sectionItem.getClass();
	}

	@Override
	public void recycle(ElementBase item) {
		if (getPosition(item) == -1) {
			Log.d(LOG, "recycle " + item.getClass().getSimpleName() + ": " + item.getName());
			add(item);
		}
	}
}
