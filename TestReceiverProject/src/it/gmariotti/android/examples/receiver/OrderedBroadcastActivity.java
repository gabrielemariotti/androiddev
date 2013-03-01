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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * This is an example of WHAT NOT TO DO !!
 * 
 */
public class OrderedBroadcastActivity extends SherlockActivity {

	private static final String TAG = "OrderedBroadcastActivity";
	protected static final String TEST_ACTION = "it.gmariotti.android.example.receiver.intent.action.TEST_ORDER";
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_orderedreceiver);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Button t1 = (Button) findViewById(R.id.test1);
		if (t1 != null) {
			t1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					test1(v);
				}
			});
		}
	}

	private void test1(View v) {
		sendOrderedBroadcast(new Intent(TEST_ACTION), null,
				new ResultReceiver(), null, 0,
				getString(R.string.text_initialdata1), null);
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

	
	class ResultReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) 
	    {
	        Log.d(TAG,"ResultReceiver");
	        Log.d(TAG,"getResultCode() = " + getResultCode());
	        Log.d(TAG,"getResultData() = " + getResultData());
		    
	        Bundle extras = intent.getExtras();
			if (extras != null) {
				for (String key : extras.keySet()) {
					Log.d(TAG, "key [" + key + "]: " + extras.get(key));
				}
			}
			
			Bundle resultExtras = getResultExtras(true);
			if (resultExtras != null) {
				for (String key : resultExtras.keySet()) {
					Log.i(TAG, "Result Extra key=" + key + ":" + resultExtras.get(key));
				}
			}
	        
	    }
	}
}
