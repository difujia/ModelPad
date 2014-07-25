package modelpad.activity;

import modelpad.model.AttributeViewModel;
import modelpad.model.ClassViewModel;
import modelpad.model.DragData;
import modelpad.model.DragData.CompletionHandler;
import modelpad.model.EAttribute;
import modelpad.model.EClass;
import modelpad.model.EReference;
import modelpad.model.EReferenceInfo;
import modelpad.model.Element;
import modelpad.model.ModelFactory;
import modelpad.model.ElementManager;
import modelpad.model.Level;
import modelpad.model.ReferenceInfoViewModel;
import modelpad.view.ClassView;
import modelpad.view.ElementView;
import modelpad.view.LinkBinder;
import modelpad.view.LinkView;
import modelpad.view.SectionView;
import modelpad.view.ViewFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hb.views.PinnedSectionListView;

public class MainFragment extends Fragment {

	static final String LOG = "MainFragment";

	private FrameLayout mCanvas;
	private PinnedSectionListView mElementList;
	private ElementSectionListAdapter mElementListAdapter;

	private final ClassOnDragListener mClassOnDragListener = new ClassOnDragListener();
	private final AttrSectionOnDragListener mAttrSectionOnDragListener = new AttrSectionOnDragListener();

	final ElementManager manager = new ElementManager();
	final Level mLevel = new Level();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_main, container, false);
		// panel = (LinearLayout) root.findViewById(R.id.panel);
		mCanvas = (FrameLayout) root.findViewById(R.id.canvas);
		mElementList = (PinnedSectionListView) root.findViewById(R.id.panel_element_list);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		mElementListAdapter = new ElementSectionListAdapter(getActivity());
		mElementListAdapter.addSectionObject(ModelFactory.getHeaderClass());
		mElementListAdapter.addSectionObject(ModelFactory.getHeaderAttr());
		mElementListAdapter.addSectionObject(ModelFactory.getHeaderRef());

		mElementListAdapter.addAll(mLevel.getAllClasses());
		mElementListAdapter.addAll(mLevel.getAllAttrs());
		mElementListAdapter.addAll(mLevel.getAllRefs());

		mElementList.setAdapter(mElementListAdapter);

		mCanvas.setOnDragListener(new CanvasOnDragListener());
		super.onActivityCreated(savedInstanceState);
	}

	class CanvasOnDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent ev) {
			DragData data = (DragData) ev.getLocalState();
			Element comingElement = data.getElement();
			if (!(comingElement instanceof EClass)) {
				return false;
			}
			EClass clazz = (EClass) comingElement;
			if (manager.hasClass(clazz)) {
				return false;
			}

			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					break;
				case DragEvent.ACTION_DROP:
					// modify the model
					manager.addClass(clazz);
					// create new class element view
					ClassView view = ViewFactory.createClassView(getActivity());
					view.setViewModel(new ClassViewModel(clazz));
					// for start dragging this class title
					view.setOnLongClickListenerForTitleView(new ElementViewOnLongClickListener());
					// for move the class view
					// node.setOnTouchListener(new ClassOnTouchListener());
					// for create reference
					view.setOnDragListener(mClassOnDragListener);
					// for add attribute
					view.setOnDragListenerForAttrSection(mAttrSectionOnDragListener);

					view.setTag(clazz);
					ViewGroup group = (ViewGroup) v;
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					group.addView(view);

					float x = (ev.getX() - view.getMeasuredWidth() / 2);
					float y = (ev.getY() - view.getMeasuredHeight() / 2);
					view.moveTo(x, y);
					data.complete(true);
					break;
			}
			return true;
		}
	}

	/**
	 * @author difujia
	 *         Handles creating reference between two classes.
	 */
	class ClassOnDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent ev) {
			DragData data = (DragData) ev.getLocalState();
			Element comingElement = data.getElement();
			if (!(comingElement instanceof EClass)) {
				return false;
			}
			EClass thatClass = (EClass) data.getElement();
			if (!manager.hasClass(thatClass)) {
				return false;
			}
			EClass thisClass = (EClass) v.getTag();
			ClassView self = (ClassView) v;

			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					self.onNotify();
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					// TODO connect two views with dash line
					self.onHover();
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					// TODO remove dash line
					self.onNotify();
					break;
				case DragEvent.ACTION_DROP:
					ClassView that = (ClassView) data.getSourceView().getParent();
					if (self != that) {
						if (manager.isReferenced(thisClass, thatClass)) {
							// TODO two classes already cross-referenced
						} else {
							EReference selfToThat = manager.makeRefBetween(thisClass, thatClass);
							EReference thatToSelf = manager.makeRefBetween(thatClass, thisClass);
							manager.pairRefs(selfToThat, thatToSelf);

							LinkView link = new LinkView(getActivity());

							ElementView labelForSelf = ViewFactory.createRefLabelView(getActivity());
							labelForSelf.setTag(selfToThat.getInfo());
							labelForSelf.setOnDragListener(new RefLabelOnDragListener(selfToThat));

							ElementView labelForThat = ViewFactory.createRefLabelView(getActivity());
							labelForThat.setTag(thatToSelf.getInfo());
							labelForThat.setOnDragListener(new RefLabelOnDragListener(thatToSelf));

							mCanvas.addView(labelForSelf, 0);
							mCanvas.addView(labelForThat, 0);

							mCanvas.addView(link, 0);
							LinkBinder binder = new LinkBinder(self, labelForSelf, that, labelForThat, link);
							self.registerNodeListener(binder);
							labelForSelf.registerNodeListener(binder);
							that.registerNodeListener(binder);
							labelForThat.registerNodeListener(binder);
						}
					} else {
						// TODO implement create ref to self

					}
					data.complete(true);
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					// TODO remove dash line
					self.onFinish();
					break;
			}
			return true;
		}

	}

	class RefLabelOnDragListener implements OnDragListener {

		EReference mRef;

		public RefLabelOnDragListener(EReference ref) {
			mRef = ref;
		}

		@Override
		public boolean onDrag(View v, DragEvent ev) {
			DragData data = (DragData) ev.getLocalState();
			Element comingElement = data.getElement();
			if (!(comingElement instanceof EReferenceInfo)) {
				return false;
			}
			EReferenceInfo newInfo = (EReferenceInfo) comingElement;
			final ElementView self = (ElementView) v;
			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					self.onNotify();
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					self.onHover();
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					self.onNotify();
					break;
				case DragEvent.ACTION_DROP:
					EReferenceInfo oldInfo = manager.swapReferenceInfo(mRef, newInfo);
					oldInfo.recycle();
					self.setViewModel(new ReferenceInfoViewModel(newInfo));
					self.setTag(newInfo);
					self.setOnLongClickListener(new ElementViewOnLongClickListener().with(new CompletionHandler() {
						@Override
						public void complete(boolean consumed) {
							if (consumed) {
								EReferenceInfo placeHolder = ModelFactory.getPlaceHolder();
								manager.swapReferenceInfo(mRef, placeHolder);
								self.setViewModel(new ReferenceInfoViewModel(placeHolder));
								self.setTag(placeHolder);
								self.setLongClickable(false);
							}
						}
					}));
					data.complete(true);
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					self.onFinish();
					break;
			}
			return true;
		}

	}

	class AttrSectionOnDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent ev) {
			DragData data = (DragData) ev.getLocalState();
			Element comingElement = data.getElement();
			if (!(comingElement instanceof EAttribute)) {
				return false;
			}
			EAttribute attr = (EAttribute) comingElement;
			EClass thisClass = (EClass) v.getTag();
			final SectionView thisSection = (SectionView) v;

			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					thisSection.onNotify();
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					thisSection.onHover();
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					thisSection.onNotify();
					break;
				case DragEvent.ACTION_DROP:
					ElementView sourceView = (ElementView) data.getSourceView();
					ViewGroup sourceParent = (ViewGroup) sourceView.getParent();
					if (sourceParent == v) {
						// drop in the same section, do nothing
						break;
					}

					manager.addAttrToClass(attr, thisClass);
					final ElementView attrView;
					attrView = ViewFactory.createSectionItemView(getActivity());
					attrView.setViewModel(new AttributeViewModel(attr));
					attrView.setTag(attr);
					attrView.setOnLongClickListener(new ElementViewOnLongClickListener().with(new CompletionHandler() {

						@Override
						public void complete(boolean consumed) {
							if (consumed) {
								thisSection.removeView(attrView);
							}
						}
					}));

					thisSection.addView(attrView);
					data.complete(true);
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					thisSection.onFinish();
					break;
			}
			return true;
		}
	}

}
