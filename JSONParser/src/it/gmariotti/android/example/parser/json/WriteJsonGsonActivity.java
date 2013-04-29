package it.gmariotti.android.example.parser.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

@SuppressLint("WorldWriteableFiles")
public class WriteJsonGsonActivity extends SherlockActivity {

	TextView jsonView;

	private static String TAG = "WriteJsonActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_json);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		jsonView = (TextView) findViewById(R.id.jsontext);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_write_json, menu);
		return true;
	}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	private void writeJson() {

		String jsonString = JsonHelper.writeGson();
		Log.i(TAG, jsonString);

		BufferedWriter writer = null;
		try {

			// Use MODE_WORLD_WRITEABLE|MODE_WORLD_READABLE only for test
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(
					"jsonfile", MODE_WORLD_WRITEABLE | MODE_WORLD_READABLE)));
			writer.write(jsonString);
			writer.close();

			// Update ui
			if (jsonView != null)
				jsonView.setText(jsonString);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_save:
			writeJson();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
