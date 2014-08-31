package modelpad.activity.home;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import modelpad.activity.R;
import modelpad.activity.editor.EditorActivity;
import modelpad.metamodel.Level;
import modelpad.utils.GameDataLoader;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class HomeActivity extends Activity {

	private static final String TAG = "MainActivity";

	private GridView mGrid;
	private TableOfContentGridAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGrid = (GridView) findViewById(R.id.grid_toc);
		mAdapter = new TableOfContentGridAdapter(this);
		mGrid.setAdapter(mAdapter);

		AssetManager asset = getAssets();
		String[] levelFileNames = null;
		try {
			levelFileNames = asset.list("levels");
		} catch (IOException e) {
			e.printStackTrace();
		}

		final List<String> filePaths = Lists.newArrayList();

		for (String fileName : levelFileNames) {
			String path = "levels/" + fileName;
			try {
				// TODO use AsyncTask
				mAdapter.add(GameDataLoader.getLevelByFilePath(this, path));
				filePaths.add(path);
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}

		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Level level = mAdapter.getItem(position);
				Intent intent = new Intent(HomeActivity.this, EditorActivity.class);
				intent.putExtra("level", level);
				try {
					Level unmodifiableLevel = GameDataLoader.getLevelByFilePath(HomeActivity.this,
							filePaths.get(position));
					intent.putExtra("unmodifiableLevel", unmodifiableLevel);
				} catch (IOException e) {
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Log.d("main activity", "create menu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class TableOfContentGridAdapter extends ArrayAdapter<Level> {

		public TableOfContentGridAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.grid_cell, parent, false);
			}
			Level level = getItem(position);
			TextView titleView = (TextView) view.findViewById(R.id.grid_cell_title);
			TextView descriptionView = (TextView) view.findViewById(R.id.grid_cell_text);

			titleView.setText(level.getTitle());
			descriptionView.setText(level.getSubtitle());

			return view;
		}
	}
}
