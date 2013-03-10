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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceFreezy extends Service {

	private static final String TAG = "ServiceFreezy";

	public ServiceFreezy() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// This is an example of WHAT NOT TO DO !!

		// A Service is not a separate process.
		// This method runs in Main Thread.... avoid long task in this method
		try {
			Thread.sleep(7500);
		} catch (Exception e) {
		}

		Log.d(TAG, "onStartCommand END");

		OwnThread thread = new OwnThread();
		thread.start();

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
	}

	private class OwnThread extends Thread {
		@Override
		public void run() {

			Log.d(TAG, "Separate Thread");
			try {
				Thread.sleep(10000);

			} catch (Exception e) {
			}
			Log.d(TAG, "Separate Thread END");
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
