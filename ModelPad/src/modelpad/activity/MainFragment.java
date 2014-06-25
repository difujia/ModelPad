package modelpad.activity;

import modelpad.model.DragData;
import modelpad.model.DragDataBuilder;
import modelpad.model.EAttribute;
import modelpad.model.EClass;
import modelpad.model.EText;
import modelpad.model.ElementManager;
import modelpad.model.NamedElement;
import modelpad.view.ClassNodeView;
import modelpad.view.ElementView;
import modelpad.view.LinkBinder;
import modelpad.view.LinkView;
import modelpad.view.ViewFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainFragment extends Fragment {

	static final String LOG = "MainFragment";

	FrameLayout canvas;
	LinearLayout panel;
	// TODO refactor following to private static final
	final TextElementOnLongClickListener mTextLongClickListener = new TextElementOnLongClickListener();
	final ElementViewOnLongClickListener mElementLongClickListener = new ElementViewOnLongClickListener();
	final ClassOnDragListener mClassOnDragListener = new ClassOnDragListener();
	final ClassTitleOnDragListener mClassTitleOnDragListener = new ClassTitleOnDragListener();
	final SectionOnDragListener mAttrSectionOnDragListener = new SectionOnDragListener();

	final ElementManager manager = new ElementManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_main, container, false);
		panel = (LinearLayout) root.findViewById(R.id.panel);
		canvas = (FrameLayout) root.findViewById(R.id.canvas);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		EText[] all = manager.getAllText();
		for (EText text : all) {
			Button b = new Button(getActivity());
			b.setText(text.getName());
			b.setTag(text);
			b.setOnLongClickListener(mTextLongClickListener);
			panel.addView(b);
		}
		canvas.setOnDragListener(new CanvasOnDragListener());
		super.onActivityCreated(savedInstanceState);
	}

	class CanvasOnDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent ev) {
			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					break;
				case DragEvent.ACTION_DROP:
					ClassNodeView node;

					DragData data = (DragData) ev.getLocalState();
					NamedElement comingElement = data.getElement();
					if (comingElement instanceof EClass) {
						// this is a move operation
						node = (ClassNodeView) data.getSourceView();
					} else {
						// need to create new EClass
						if (!manager.allowCreateClass(comingElement)) {
							// class of same name may have existed
							break;
						}

						EClass clazz = manager.createClass(comingElement);
						// remove source view from its parent if any
						View source = data.getSourceView();
						if (source != null) {
							ViewGroup parent = (ViewGroup) source.getParent();
							parent.removeView(source);
						}
						// create new class element view
						node = ClassNodeView.create(getActivity());
						node.setOnLongClickListener(mElementLongClickListener);
						node.setOnDragListener(mClassOnDragListener);
						node.setOnDragListenerForTitle(mClassTitleOnDragListener);
						node.setOnDragListenerForSection(0, mAttrSectionOnDragListener);
						node.setTitle(clazz.getName());
						node.setTag(clazz);
						ViewGroup group = (ViewGroup) v;
						node.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
						group.addView(node);
					}
					float x = (ev.getX() - node.getMeasuredWidth() / 2);
					float y = (ev.getY() - node.getMeasuredHeight() / 2);
					node.moveTo(x, y);
					if (BuildConfig.DEBUG) {
						Log.d(LOG, "Node padding left: " + node.getPaddingLeft());
					}
					break;
			}
			return true;
		}
	}

	/**
	 * @author difujia
	 *         Handles dragging another ClassElementView to this.
	 */
	class ClassOnDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent ev) {
			DragData data = (DragData) ev.getLocalState();
			NamedElement comingElement = data.getElement();
			EClass myClazz = (EClass) v.getTag();

			if (manager.allowConnect(myClazz, comingElement) == false) {
				// a workaround for a issue that child view returns true in OnDrag,
				// parent view won't receive start and end event,
				// but still get other events.
				return false;
			}

			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setBackgroundResource(R.drawable.bg_class_highlight);
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					v.setBackgroundResource(R.drawable.bg_class);
					break;
				case DragEvent.ACTION_DROP:
					if (BuildConfig.DEBUG) {
						Log.d(LOG, "drop in class");
					}
					ClassNodeView that = (ClassNodeView) data.getSourceView();
					ClassNodeView self = (ClassNodeView) v;
					if (self != that) {
						LinkView link = new LinkView(getActivity());
						TextView labelForSelf = ViewFactory.createLabel(getActivity());
						TextView labelForThat = ViewFactory.createLabel(getActivity());
						canvas.addView(labelForSelf, 0);
						canvas.addView(labelForThat, 0);
						canvas.addView(link, 0);
						LinkBinder binder = new LinkBinder(self, labelForSelf, that, labelForThat, link);
						self.addNodeListener(binder);
						that.addNodeListener(binder);
					} else {
						// TODO implement create ref to self

					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					v.setBackgroundResource(R.drawable.bg_class);
					break;
			}
			return true;
		}

	}

	class ClassTitleOnDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent ev) {
			DragData data = (DragData) ev.getLocalState();
			NamedElement comingElement = data.getElement();
			EClass myClazz = (EClass) v.getTag();
			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					boolean canCopy = manager.allowCopyName(comingElement, myClazz);
					return canCopy;
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setBackgroundResource(R.drawable.bg_section_highlight);
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					v.setBackgroundResource(R.drawable.bg_section);
					break;
				case DragEvent.ACTION_DROP:
					manager.copyName(comingElement, myClazz);
					if (comingElement.isDestroyed()) {
						// remove its view
						ViewGroup parent = (ViewGroup) data.getSourceView().getParent();
						parent.removeView(data.getSourceView());
					}
					ElementView title = (ElementView) v;
					title.setText(myClazz.getName());
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					v.setBackgroundResource(R.drawable.bg_section);
					break;
			}
			return true;
		}

	}

	class RefOnDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	class SectionOnDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent ev) {
			DragData data = (DragData) ev.getLocalState();
			NamedElement comingElement = data.getElement();
			EClass myClazz = (EClass) v.getTag();

			switch (ev.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					boolean isAllowed = manager.allowAddAttrToClass(comingElement, myClazz);
					Log.d(LOG, "can accept child: " + isAllowed);
					return isAllowed;
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setBackgroundResource(R.drawable.bg_section_highlight);
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					v.setBackgroundResource(R.drawable.bg_section);
					break;
				case DragEvent.ACTION_DROP:
					ElementView elementView = (ElementView) data.getSourceView();
					if (elementView != null) {
						ViewGroup parent = (ViewGroup) elementView.getParent();
						if (v == parent) {
							// drop in the same section, do nothing
							break;
						} else {
							// drag from another section, remove it
							parent.removeView(elementView);
						}

					}

					EAttribute attr = manager.addAttrToClass(comingElement, myClazz);

					if (attr != null) {
						// add new ElementView
						if (elementView == null) {
							elementView = new ElementView(getActivity());
							elementView.setOnLongClickListener(mElementLongClickListener);
						}
						elementView.setTag(attr);
						elementView.setText(attr.getName());
						ViewGroup section = (ViewGroup) v;
						section.addView(elementView);
					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					v.setBackgroundResource(R.drawable.bg_section);
					break;
			}
			return true;
		}
	}

	class ElementViewOnLongClickListener implements OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
			DragData data = new DragDataBuilder().setElement((NamedElement) v.getTag()).setSourceView(v).build();
			v.startDrag(null, shadowBuilder, data, 0);
			return true;
		}
	}

	class TextElementOnLongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
			EText text = (EText) v.getTag();
			DragData data = new DragDataBuilder().setElement(text).build();
			v.startDrag(null, shadowBuilder, data, 0);
			return true;
		}
	}

}
