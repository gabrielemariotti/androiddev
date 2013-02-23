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
package it.gmariotti.android.examples.dashclock.extensions.wht;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class WhatsappExtension extends DashClockExtension {

	private static final String TAG = "WhatsappExtension";
	private MessageManager mManager;
	private SomethingOnScreenReceiver mReceiver;

	@Override
	protected void onInitialize(boolean isReconnect) {
		super.onInitialize(isReconnect);
		if (!isReconnect) {
			mManager = MessageManager.getInstance(this);
			registerReceiver();
			mManager.setmReceiver(mReceiver);
		}
	}

	public void changeMessage() {
		onUpdateData(UPDATE_REASON_CONTENT_CHANGED);
	}

	@Override
	protected void onUpdateData(int reason) {
		if (mManager!=null){
			Log.d(TAG,"onUpdateData msgCount="+mManager.getmCount());
			if (mManager.getmCount() > 0) {
				// publish
				publishUpdateExtensionData();
			} else
				clearUpdateExtensionData();
		}
	}

	/**
	 * Clear DashClock
	 */
	private void clearUpdateExtensionData() {
		publishUpdate(null);
	}

	/**
	 * publishUpdata
	 */
	private void publishUpdateExtensionData() {
		
		if (mManager!=null){
			// Intent
			PackageManager pm = getPackageManager();
			Intent intentWht=pm.getLaunchIntentForPackage(WhtNotificationService.PACKAGE_NAME);
			
			int mCount=mManager.getmCount();
			ExtensionData data=
					new ExtensionData().visible(true)
					                   .icon(R.drawable.ic_extension_wht)
					                   .status(""+mCount)
					                   .expandedTitle(getResources().getQuantityString(
				                                R.plurals.messagecount, mCount,
				                                mCount));
			
			if (intentWht!=null)
				data.clickIntent(intentWht);
			
			// Publish the extension data update.
			publishUpdate(data);
		}
	}
	
	private void registerReceiver(){
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		localIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
		localIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
		MessageManager manager = MessageManager.getInstance();
		if (manager!=null){
			mReceiver = new SomethingOnScreenReceiver(manager);
			registerReceiver(mReceiver, localIntentFilter);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mReceiver!=null)
			unregisterReceiver(mReceiver);
	}
}