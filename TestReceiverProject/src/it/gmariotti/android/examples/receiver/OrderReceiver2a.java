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

import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OrderReceiver2a extends BroadcastReceiver {

	private static final String TAG = "OrderReceiver2a";

	public OrderReceiver2a() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		Log.i(TAG, "new Intent Received 2a");
		Log.i(TAG, "Intent Action:" + intent.getAction());

		if (intent.getData() != null)
			Log.i(TAG, "Intent Data:" + intent.getData().toString());

		
		Bundle extras = intent.getExtras();
		if (extras != null) {
			for (String key : extras.keySet()) {
				Log.i(TAG, "Intent Extra key=" + key + ":" + extras.get(key));
			}
		}

		Log.i(TAG, "getResultCode() = " + getResultCode());
		Log.i(TAG, "getResultData() = " + getResultData());

		setResultCode(getResultCode() + 1);
		setResultData(getResultData() + context.getString(R.string.text_data2));
		
		Bundle resultExtras = getResultExtras(true);
		if (resultExtras != null) {
			for (String key : resultExtras.keySet()) {
				Log.i(TAG, "Result Extra key=" + key + ":" + resultExtras.get(key));
			}
			resultExtras.putString("data2a", context.getString(R.string.text_data2));
		}
		
		setResultExtras(resultExtras);

	}
}
