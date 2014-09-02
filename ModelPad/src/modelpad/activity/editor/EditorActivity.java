package modelpad.activity.editor;

import modelpad.activity.R;
import modelpad.datamodel.Level;
import modelpad.datamodel.ModelFactory;
import modelpad.datamodel.SolutionManager;
import modelpad.datamodel.Validator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;

public class EditorActivity extends Activity {

	private static final String TAG = "EditorActivity";

	private SolutionManager manager = new SolutionManager();
	private Level mUntouchLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		Level level = (Level) getIntent().getSerializableExtra("level");
		setTitle(level.getTitle());

		TextView text = (TextView) findViewById(R.id.editor_text);
		text.setText(level.getQuestion());
		text.setMovementMethod(ScrollingMovementMethod.getInstance());

		ElementSectionListAdapter adapter = new ElementSectionListAdapter(this);
		adapter.addSectionObject(ModelFactory.getHeaderClass());
		adapter.addSectionObject(ModelFactory.getHeaderAttr());
		adapter.addSectionObject(ModelFactory.getHeaderRef());

		adapter.addAll(level.getAllElements());

		PinnedSectionListView listView = (PinnedSectionListView) findViewById(R.id.editor_list);
		listView.setAdapter(adapter);

		FrameLayout canvas = (FrameLayout) findViewById(R.id.editor_canvas);
		canvas.setOnDragListener(new CanvasOnDragListener(this, canvas, manager));

		mUntouchLevel = (Level) getIntent().getSerializableExtra("unmodifiableLevel");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				Intent intent = NavUtils.getParentActivityIntent(this);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				NavUtils.navigateUpTo(this, intent);
				return true;
			case R.id.menu_editor_commit:
				ValidationFragment dialog = ValidationFragment.create(new Validator(manager.getSolution(),
						mUntouchLevel.getExpectedSolution()));
				dialog.show(getFragmentManager(), "dialog");
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
