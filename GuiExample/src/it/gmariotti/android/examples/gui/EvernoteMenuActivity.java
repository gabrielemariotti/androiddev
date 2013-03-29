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
package it.gmariotti.android.examples.gui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EvernoteMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evernotemn);
		// Show the Up button in the action bar.
		setupActionBar();
		// populate list
		populateList();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	private void populateList() {
		
		
		// Create ArrayAdapter
		MyListAdapter mListAdapter = new MyListAdapter();
		EvernoteListLayout mLay = (EvernoteListLayout) findViewById(R.id.box_list_ev);
		if (mLay != null) {
			mLay.setList(mListAdapter);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evernote_menu, menu);
		return true;
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
		}
		return super.onOptionsItemSelected(item);
	}

	private class MyListAdapter extends BaseAdapter {
		
		String[] items = new String[] { "Title1", "Title2", "Title3", "Title4",
		"Title5" , "Title 6" , "Title 7" };		
		
		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
		        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.evernote_list_item, parent, false);
		    }
		    
			// setup our row
		    TextView text = (TextView) convertView.findViewById(R.id.ev_list_text);
		    text.setText(items[position]);
		    ImageView image = (ImageView) convertView.findViewById(R.id.ev_list_image);
		    image.setImageResource(R.drawable.ic_labels);
		    TextView textnumber = (TextView) convertView.findViewById(R.id.ev_list_textnumber);
		    textnumber.setText(""+position);
		    return convertView;

		}
	}
}
