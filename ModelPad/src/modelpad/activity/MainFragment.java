package modelpad.activity;

import modelpad.model.Level;
import modelpad.model.ModelFactory;
import modelpad.model.SolutionManager;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hb.views.PinnedSectionListView;

public class MainFragment extends Fragment {

	static final String LOG = "MainFragment";

	private FrameLayout mCanvas;

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
		mCanvas.setOnDragListener(new CanvasOnDragListener(getActivity(), mCanvas, manager));
		super.onActivityCreated(savedInstanceState);
	}

}
