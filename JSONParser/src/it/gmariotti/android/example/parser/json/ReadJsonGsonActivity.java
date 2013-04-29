package it.gmariotti.android.example.parser.json;

import it.gmariotti.android.example.parser.json.JsonHelper.MyObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ReadJsonGsonActivity extends SherlockActivity {

	TextView jsonView;
	
	private static String TAG="ReadJsonActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_json);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		jsonView = (TextView) findViewById(R.id.jsontext);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_read_json, menu);
		return true;
	}

	private void readJson() {

		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					openFileInput("jsonfile")));
			String line;
			StringBuffer content = new StringBuffer();
			char[] buffer = new char[1024];
			int num;

			while ((num = input.read(buffer)) > 0) {
				content.append(buffer, 0, num);
			}

			MyObject myobj = JsonHelper.parseGson(content.toString());
			if (myobj != null) {
				// Update ui
				if (jsonView != null)
					jsonView.setText(myobj.toString());
					Log.i(TAG,myobj.toString());
			}

			/*
			 * while ((line = input.readLine()) != null) { buffer.append(line +
			 * eol); }
			 */
		} catch (Exception e) {
			Log.e(TAG,"Error while reading ",e);
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
			readJson();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
