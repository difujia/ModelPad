package modelpad.activity.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import modelpad.activity.R;
import modelpad.metamodel.ElementBase;
import modelpad.metamodel.ElementRecycler;
import modelpad.metamodel.ModelFactory;
import modelpad.metamodel.ViewModelBase;
import modelpad.view.ElementView;
import modelpad.viewutils.ViewHelper;
import android.content.Context;
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

//	private static int counter = 0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ElementView view;
		if (convertView == null) {
			if (getItemViewType(position) == SECTION) {
				view = ViewHelper.createListSectionView(getContext());
			} else {
				view = ViewHelper.createListItemView(getContext());
			}
		} else {
			view = (ElementView) convertView;
		}

		final ElementBase item = getItem(position);

		// prevent unwanted registration because of scrolling
		item.unregisterAllObservers();

		final ViewModelBase viewModel = ModelFactory.createViewModel(item);
		view.setViewModel(viewModel);
//		final int cc = counter++;
//		Log.d(LOG, "cc: " + cc + ", " + viewModel.getStringDisplay());
		if (getItemViewType(position) == SECTION) {
			view.setBackgroundResource(R.drawable.bg_list_section);
		} else {
			view.setOnLongClickListener(LongClickToDragListener.builder(item).with(new CompletionHandler() {
				@Override
				public void complete(boolean consumed) {
					if (consumed) {
//						Log.e(LOG, "counter: " + cc);
						try {
							viewModel.releaseModel();							
						} catch (Exception e) {
							Log.w(LOG, "not registered!");
						}
						remove(item);
					}
				}
			}).build());
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
				return;
			}
		}
		throw new IllegalArgumentException("Added item doesn't belong to any section, add section item first.");
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
			add(item);
		}
	}
}