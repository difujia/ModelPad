package modelpad.view;

import java.util.HashSet;
import java.util.Set;

import modelpad.activity.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ClassNodeView extends LinearLayout {

	interface NodeListener {
		void nodeMoved(View node);

		void nodeDestroyed(View node);
	}

	public static ClassNodeView create(Context ctx) {
		ClassNodeView node = new ClassNodeView(ctx);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		node.setLayoutParams(lp);
		node.setBackgroundResource(R.drawable.bg_class);
		node.setDividerDrawable(ctx.getResources().getDrawable(R.drawable.class_comp_divider));
		node.setShowDividers(SHOW_DIVIDER_MIDDLE);
		return node;
	}

	private ElementView mTitleView;
	private ViewGroup[] mSections = new ViewGroup[1];
	private Set<NodeListener> listeners = new HashSet<>();

	public ClassNodeView(Context ctx) {
		super(ctx);
		init();
	}

	public ClassNodeView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init();
	}

	public ClassNodeView(Context ctx, AttributeSet attrs, int defstyle) {
		super(ctx, attrs, defstyle);
		init();
	}

	private void init() {
		setOrientation(VERTICAL);
		View.inflate(getContext(), R.layout.class_components, this);
		mTitleView = (ElementView) findViewById(R.id.class_title);
		mSections[0] = (ViewGroup) findViewById(R.id.class_section_top);
	}

	public void setTitle(CharSequence title) {
		mTitleView.setText(title);
	}

	public void setOnDragListenerForSection(int position, OnDragListener listener) {
		mSections[position].setOnDragListener(listener);
	}

	public void setOnDragListenerForTitle(OnDragListener listener) {
		mTitleView.setOnDragListener(listener);
	}

	public void addNodeListener(NodeListener listener) {
		listeners.add(listener);
	}

	public void removeNodeListener(NodeListener listener) {
		listeners.remove(listener);
	}

	public void moveTo(float x, float y) {
		setX(x);
		setY(y);
		for (NodeListener l : listeners) {
			l.nodeMoved(this);
		}
	}

	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		this.mTitleView.setTag(tag);
		for (ViewGroup section : mSections) {
			section.setTag(tag);
		}
	}

	@Override
	public String toString() {
		return (String) mTitleView.getText();
	}
}
