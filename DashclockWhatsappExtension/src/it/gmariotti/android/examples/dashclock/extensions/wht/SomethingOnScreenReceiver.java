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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SomethingOnScreenReceiver extends BroadcastReceiver {

	private boolean userActive = false;
	private boolean onScreen = false;
	
	private MessageManager mManager;
	
	public SomethingOnScreenReceiver(MessageManager manager){
		mManager=manager;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			onScreen = true;
			userActive=false;
		}else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
			onScreen=false;
			userActive=false;
		}else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
			userActive=true;
			if (onScreen && mManager!=null){
				mManager.clearMessages();
			}
		}
	}

	public boolean isUserActive() {
		return userActive;
	}

}
