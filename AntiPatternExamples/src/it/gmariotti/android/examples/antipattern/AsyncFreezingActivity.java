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
package it.gmariotti.android.examples.antipattern;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * This is an example of WHAT NOT TO DO !!
 * 
 */
public class AsyncFreezingActivity extends SherlockActivity {

	protected AsyncTask mAsyncTask = null;

	private static final String TAG = "AsyncFreezingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.async);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	
	/**
	 * 
	 */
	private void newEvent() {
		Toast.makeText(this, getString(R.string.text_newevent), 1000).show();
	}

	/**
	 * Launch Async Task
	 */
	private void launchAsyncTask() {
		Toast.makeText(this, getString(R.string.text_asynctask), 1000).show();
		new AsyncFreezyTask(this).execute();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.freezing_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_refresh:
			launchAsyncTask();
			return true;
		case R.id.menu_newevent:
			newEvent();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	

	

}
