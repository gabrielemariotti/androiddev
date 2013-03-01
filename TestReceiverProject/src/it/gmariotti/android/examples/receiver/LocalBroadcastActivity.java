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
package it.gmariotti.android.examples.receiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class LocalBroadcastActivity extends SherlockActivity {

	private MyTestReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_receiver);		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Button button= (Button) findViewById(R.id.buttonService);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	Intent serviceIntent=new Intent(v.getContext(),LocalBroadcastReceiverTestService.class);
		    	startService(serviceIntent);
		    }
		});
	}
	
	@Override
	protected void onPause() {
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
		lbm.unregisterReceiver( mReceiver ); //Important: If we don't use lbm we will retrieve java.lang.IllegalArgumentException: Receiver not registered: 

		super.onPause();
	}

	@Override
	protected void onResume() {
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
		mReceiver=new MyTestReceiver();
		lbm.registerReceiver( mReceiver,
				          new IntentFilter(Constants.INTENT_ACTION));
		super.onResume();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
