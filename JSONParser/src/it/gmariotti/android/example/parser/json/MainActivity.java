/*******************************************************************************
 * Copyright 2013 Gabriele Mariotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
