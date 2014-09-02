package modelpad.activity.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import modelpad.activity.R;
import modelpad.datamodel.AbstractElement;
import modelpad.datamodel.AbstractViewModel;
import modelpad.datamodel.ElementRecycler;
import modelpad.datamodel.ModelFactory;
import modelpad.view.ElementView;
import modelpad.viewutils.ViewHelper;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

public class ElementSectionListAdapter extends ArrayAdapter<AbstractElement> implements PinnedSectionListAdapter,
		ElementRecycler {

	private static final String LOG = "ElementAdapter";
	private static final int ITEM = 0;
	private static final int SECTION = 1;

	private List<AbstractElement> sectionObjects = new ArrayList<>();

	public ElementSectionListAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		AbstractElement item = getItem(position);
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

		final AbstractElement item = getItem(position);

		// prevent unwanted registration because of scrolling
		item.unregisterAllObservers();

		final AbstractViewModel viewModel = ModelFactory.createViewModel(item);
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

	public void addSectionObject(AbstractElement sectionObj) {
		sectionObjects.add(sectionObj);
		super.add(sectionObj);
	}

	@Override
	public void add(AbstractElement item) {
		for (AbstractElement sectionObj : sectionObjects) {
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
	public void addAll(Collection<? extends AbstractElement> collection) {
		for (AbstractElement obj : collection) {
			add(obj);
		}
	}

	@Override
	public void addAll(AbstractElement... items) {
		for (AbstractElement obj : items) {
			add(obj);
		}
	}

	@Override
	public void remove(AbstractElement item) {
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

	private boolean isItemOfSection(AbstractElement item, AbstractElement sectionItem) {
		return item.getClass() == sectionItem.getClass();
	}

	@Override
	public void recycle(AbstractElement item) {
		if (getPosition(item) == -1) {
			add(item);
		}
	}
}