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
package it.gmariotti.android.examples.dashclock.extensions.wsa2;

import static it.gmariotti.android.examples.dashclock.extensions.wsa2.LogUtils.LOGD;

import java.util.ArrayList;

import android.content.res.Resources;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class WhtsExtension extends DashClockExtension {

	private static final String TAG = LogUtils
			.makeLogTag(WhtsExtension.class);

	private MessageManager mManager;

	private String dashTitle;
	private String dashSubtitle;
	private String dashStatus;
	private int dashIcon;
	private boolean dashVisible = true;

	// ----------------------------------------------------

	@Override
	protected void onInitialize(boolean isReconnect) {
		super.onInitialize(isReconnect);
		LOGD(TAG, "onInitialize " + isReconnect);
		setUpdateWhenScreenOn(true);
		
		if (!isReconnect) {
			mManager = MessageManager.getInstance(this);
		}
	}

	@Override
	protected void onUpdateData(int reason) {
		LOGD(TAG, "onUpdate " + reason);
		
		readData();
		
		// publish
		publishUpdateExtensionData();

	}

	
	// -----------------------------------------------------------------------------------------------------
	
	
	private void publishUpdateExtensionData() {

		// Publish the extension data update.

		ExtensionData extData = new ExtensionData();
		if (dashVisible) {
			extData.visible(true).icon(dashIcon).status(dashStatus)
					.expandedTitle(dashTitle).expandedBody(dashSubtitle);
			
			extData.clickIntent(null);
		} else {
			extData.visible(false);
		}

		publishUpdate(extData);

	}	
	// ------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param object
	 */
	private void readData() {

		if (mManager == null)
			return;
		
		dashIcon=R.drawable.ic_extension_whts;

		int mCount = mManager.getmCount();
		LOGD(TAG,"count="+mCount);

		if (mCount > 0) {
			dashVisible = true;

			dashStatus = "" + mCount;
			
			Resources res = getResources();
			String book = res.getQuantityString(R.plurals.notifications,
					mCount, mCount);
			dashTitle = book;
		
			ArrayList<MessageWht> msgs = mManager.getmMsgs();
			if (msgs != null) {
				StringBuilder sb = new StringBuilder();
				String and = "";
				
				for (MessageWht msg:msgs) {
						sb.append(and);
						sb.append(msg.getText());
						and="\n";
				}
				
				dashSubtitle=sb.toString();
			}

		} else {
			dashVisible = false;
		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------

}
