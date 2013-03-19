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
package it.gmariotti.android.examples.testabsproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockListActivity {

	public static final String[] options = {
			"Snippet 1: OptionsMenu in ActionBar",
			"Snippet 2: OptionsMenu in SherlockActionBar",
			"Snippet 3: OptionsMenu in ActionBar with Fragment",
			"Snippet 4: OptionsMenu in SherlockActionBar with Fragment"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		getSupportActionBar().setHomeButtonEnabled(false);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, options));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = null;

		switch (position) {
		default:
		case 0:
			intent = new Intent(this, ActionBarActivity.class);
			break;
		case 1:
			intent = new Intent(this, SherlockActionBarActivity.class);
			break;
		case 2:
			intent = new Intent(this, MyFragmentActivity.class);
			break;	
		case 3:
			intent = new Intent(this, MySherlockFragmentActivity.class);
			break;		
		}

		if (intent != null)
			startActivity(intent);
	}

}
