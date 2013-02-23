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

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class WhtNotificationService extends AccessibilityService {

	private final static String TAG="WhtNotificationService";
	public static final String PACKAGE_NAME = "com.whatsapp";
	
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

		Log.d(TAG,"new Event="+event.toString());
		
		
		MessageManager manager = MessageManager.getInstance();
		if (manager != null) {
			if (manager.getmReceiver()!=null && !manager.getmReceiver().isUserActive()){
				MessageWht msg = new MessageWht();
				msg.setText(event.getText());
				manager.notifyListener(msg);
			}
		}
	}

	@Override
	public void onInterrupt() {
		
	}


	
}
