package modelpad.activity.editor;

import modelpad.activity.R;
import modelpad.datamodel.Level;
import modelpad.datamodel.ModelFactory;
import modelpad.datamodel.SolutionManager;
import modelpad.datamodel.Validator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;

public class EditorActivity extends Activity {

	private static final String TAG = "EditorActivity";
	public static final String SOLUTION_CHANGE = "solution.changed.broadcast";

	private SolutionManager mManager = new SolutionManager();
	private Level mUntouchLevel;
	private Validator mValidator;

	private BroadcastReceiver mReceiver;

	private ProgressBar mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mProgress = (ProgressBar) findViewById(R.id.progressbar);

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
		canvas.setOnDragListener(new CanvasOnDragListener(this, canvas, mManager));

		mUntouchLevel = (Level) getIntent().getSerializableExtra("unmodifiableLevel");

		updateProgress();

		View detail = findViewById(R.id.check);
		detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ValidationFragment dialog = ValidationFragment.create(mValidator);
				dialog.show(getFragmentManager(), "dialog");
			}
		});
	}

	@Override
	protected void onResume() {
		setupBroadcastReceiver();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
	}

	private void setupBroadcastReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				updateProgress();
			}
		};
		IntentFilter filter = new IntentFilter(SOLUTION_CHANGE);
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
	}

	private void updateProgress() {
		new ValidationTask().execute(0);
	}

	private class ValidationTask extends AsyncTask<Integer, Void, Validator> {

		@Override
		protected Validator doInBackground(Integer... params) {
			if (mValidator == null) {
				mValidator = new Validator(mManager.getSolution(), mUntouchLevel.getExpectedSolution());
			} else {
				mValidator.validate();
			}
			return mValidator;
		}

		@Override
		protected void onPostExecute(Validator result) {
			super.onPostExecute(result);
			int progress = 100 * result.getAllMatched().size() / result.getAllExpected().size();
			mProgress.setProgress(progress);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.editor, menu);
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
				ValidationFragment dialog = ValidationFragment.create(new Validator(mManager.getSolution(),
						mUntouchLevel.getExpectedSolution()));
				dialog.show(getFragmentManager(), "dialog");
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
