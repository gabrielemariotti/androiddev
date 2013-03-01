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

import android.app.IntentService;
import android.content.Intent;

public class ReceiverTestService extends IntentService {

	
	public ReceiverTestService() {
		super("ReceiverTestService");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		try{
			Thread.sleep(2000);
		}catch (Exception e){e.printStackTrace();}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		//Only for test, send a broadcast intent
		Intent mIntent= new Intent();
		mIntent.setAction(Constants.INTENT_ACTION);
		mIntent.putExtra(Constants.INTENT_EXTRA, "Additional info");
		sendBroadcast(mIntent);
	}
	
}
