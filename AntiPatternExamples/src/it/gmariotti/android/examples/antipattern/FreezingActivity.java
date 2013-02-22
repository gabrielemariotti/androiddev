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

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * This is an example of WHAT NOT TO DO !!
 * 
 */
public class FreezingActivity extends SherlockActivity {

	protected FreezingUIReceiver mReceiver = null;

	private static final String TAG = "FreezingActivity";
	protected static final String FREEZYACTION = "it.gmariotti.android.examples.antipattern.FREEZING_ACTION";

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver); //take care to unregister
	}

	@Override
	protected void onResume() {
		super.onResume();

		mReceiver = new FreezingUIReceiver();
		registerReceiver(mReceiver, new IntentFilter(FREEZYACTION));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * 
	 */
	private void newEvent() {
		Toast.makeText(this, getString(R.string.text_newevent), 1000).show();
	}

	/**
	 * Launch Intent Broadcast
	 */
	private void launchBroadcast() {
		Toast.makeText(this, getString(R.string.text_broadcast), 1000).show();
		Intent i = new Intent(FREEZYACTION);
		sendBroadcast(i);
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
			launchBroadcast();
			return true;
		case R.id.menu_newevent:
			newEvent();
			return true;	
		}

		return super.onOptionsItemSelected(item);
	}

}
