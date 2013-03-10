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

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class IntentServiceFreezy extends IntentService {

	private static final String TAG = "IntentServiceFreezy";

	public IntentServiceFreezy() {
		super("IntentService");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// This is an example of WHAT NOT TO DO !!
		// You should not override this method for your IntentService.
		// This method runs in Main Thread.... avoid long task in this method
		try {
			Thread.sleep(7500);
		} catch (Exception e) {
		}

		Log.d(TAG, "onStartCommand END");

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		// This is an example of WHAT NOT TO DO !!
		// This method runs in Main Thread.... avoid long task in this method
		try {
			Thread.sleep(2500);
		} catch (Exception e) {
		}
		
		Log.d(TAG, "onCreate");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d(TAG, "Separate Thread");
		try {
			Thread.sleep(1000);

		} catch (Exception e) {
		}
		Log.d(TAG, "Separate Thread END");

	}

}
