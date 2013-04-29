package it.gmariotti.android.example.parser.json;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;

public class MainActivity extends SherlockListActivity {

	public static final String[] options = { "Snippet 1: Write Json",
			"Snippet 2: Read Json",
			"Snippet 3: Write Json with GSON",
			"Snippet 4: Read Json with GSON",
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, options));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = null;

		switch (position) {
		default:
		case 0:
			intent = new Intent(this, WriteJsonActivity.class);
			break;

		case 1:
			intent = new Intent(this, ReadJsonActivity.class);
			break;
		
		case 2:
			intent = new Intent(this, WriteJsonGsonActivity.class);
			break;

		case 3:
			intent = new Intent(this, ReadJsonGsonActivity.class);
			break;
		}

		if (intent != null)
			startActivity(intent);
	}

}
