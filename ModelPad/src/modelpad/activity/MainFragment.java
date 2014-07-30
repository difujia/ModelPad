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
import modelpad.model.Level;
import modelpad.model.ModelFactory;
import modelpad.model.ReferenceViewModel;
import modelpad.model.SolutionManager;
import modelpad.utils.ElementAnchor;
import modelpad.utils.LinkAnchor;
import modelpad.view.ClassView;
import modelpad.view.CompositeStateResponder;
import modelpad.view.ElementView;
import modelpad.view.LinkBinder;
import modelpad.view.LinkTouchDelegateView;
import modelpad.view.LinkView;
import modelpad.view.SectionView;
import modelpad.view.ViewFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hb.views.PinnedSectionListView;

public class MainFragment extends Fragment {

	static final String LOG = "MainFragment";

	private FrameLayout mCanvas;

	private final ClassOnDragListener mClassOnDragListener = new ClassOnDragListener();
	private final AttrSectionOnDragListener mAttrSectionOnDragListener = new AttrSectionOnDragListener();

	final SolutionManager manager = new SolutionManager();
	final Level mLevel = new Level();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_main, container, false);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		ElementSectionListAdapter adapter = new ElementSectionListAdapter(getActivity());
		adapter.addSectionObject(ModelFactory.getHeaderClass());
		adapter.addSectionObject(ModelFactory.getHeaderAttr());
		adapter.addSectionObject(ModelFactory.getHeaderRef());

		adapter.addAll(mLevel.getAllClasses());
		adapter.addAll(mLevel.getAllAttrs());
		adapter.addAll(mLevel.getAllRefs());

		PinnedSectionListView listView = (PinnedSectionListView) getView().findViewById(R.id.panel_element_list);
		listView.setAdapter(adapter);

		mCanvas = (FrameLayout) getView().findViewById(R.id.canvas);
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
					final ClassView classView = ViewFactory.createClassView(getActivity());
					classView.setViewModel(new ClassViewModel(clazz));
					// for start dragging this class title
					classView.setOnLongClickListenerForTitleView(new ElementLongClickToDragListener());
					// for move the class view
					// for create reference
					classView.setOnDragListener(mClassOnDragListener);
					// for add attribute
					classView.setOnDragListenerForAttrSection(mAttrSectionOnDragListener);

					ElementAnchor anchor = new ElementAnchor(classView);
					classView.setOnClickListenerForTitleView(new ElementClickToRemoveListener(getActivity(), anchor,
							classView, clazz));

					classView.setTag(clazz);
					classView.setVisibility(View.INVISIBLE);
					ViewGroup group = (ViewGroup) v;
					group.addView(classView);
					// classView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					final float dropX = ev.getX();
					final float dropY = ev.getY();
					classView.post(new Runnable() {

						@Override
						public void run() {
							float x = (dropX - classView.getWidth() / 2);
							float y = (dropY - classView.getHeight() / 2);
							classView.moveTo(x, y);
							classView.setVisibility(View.VISIBLE);
						}
					});
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
					self.beActive();
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					// TODO connect two views with dash line
					self.beTarget();
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					// TODO remove dash line
					self.beActive();
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

							ElementView labelForSelf = ViewFactory.createRefLabelView(getActivity());
							labelForSelf.setOnDragListener(new RefLabelOnDragListener(selfToThat));
							labelForSelf.setViewModel(new ReferenceViewModel(selfToThat));

							ElementView labelForThat = ViewFactory.createRefLabelView(getActivity());
							labelForThat.setOnDragListener(new RefLabelOnDragListener(thatToSelf));
							labelForThat.setViewModel(new ReferenceViewModel(thatToSelf));

							mCanvas.addView(labelForSelf, 0);
							mCanvas.addView(labelForThat, 0);

							// link
							LinkView linkView = new LinkView(getActivity());
							CompositeStateResponder responders = new CompositeStateResponder(labelForSelf,
									labelForThat, linkView);

							// click delegate view
							LinkTouchDelegateView touchArea = new LinkTouchDelegateView(getActivity());
							LinkAnchor anchor = new LinkAnchor(touchArea);
							touchArea.setOnClickListener(new ElementClickToRemoveListener(getActivity(), anchor,
									responders, selfToThat, thatToSelf));
							mCanvas.addView(touchArea, 0);
							mCanvas.addView(linkView, 0);

							selfToThat.registerObserver(linkView.getObserver(), touchArea.getObserver());
							thatToSelf.registerObserver(linkView.getObserver(), touchArea.getObserver());

							LinkBinder binder = new LinkBinder(self, labelForSelf, that, labelForThat, linkView,
									touchArea);
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
					self.beNormal();
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
					self.beActive();
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					self.beTarget();
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					self.beActive();
					break;
				case DragEvent.ACTION_DROP:
					ElementView sourceView = (ElementView) data.getSourceView();
					if (sourceView == v) {
						// drop in the same section, do nothing
						break;
					}
					manager.changeReferenceInfo(mRef, newInfo);
					self.setTag(newInfo);

					ElementAnchor anchor = new ElementAnchor(self);
					self.setOnClickListener(new ElementClickToRemoveListener(getActivity(), anchor, self, newInfo));

					self.setOnLongClickListener(new ElementLongClickToDragListener().with(new CompletionHandler() {
						@Override
						public void complete(boolean consumed) {
							if (consumed) {
								EReferenceInfo placeHolder = ModelFactory.createPlaceHolder();
								manager.changeReferenceInfo(mRef, placeHolder);
								self.setLongClickable(false);
								self.setClickable(false);
							}
						}
					}));
					data.complete(true);
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					self.beNormal();
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
					thisSection.beActive();
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					thisSection.beTarget();
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					thisSection.beActive();
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
					attrView.setOnLongClickListener(new ElementLongClickToDragListener().with(new CompletionHandler() {

						@Override
						public void complete(boolean consumed) {
							if (consumed) {
								thisSection.removeView(attrView);
							}
						}
					}));

					ElementAnchor anchor = new ElementAnchor(attrView);
					attrView.setOnClickListener(new ElementClickToRemoveListener(getActivity(), anchor, attrView, attr));

					thisSection.addView(attrView);
					data.complete(true);
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					thisSection.beNormal();
					break;
			}
			return true;
		}
	}

}
