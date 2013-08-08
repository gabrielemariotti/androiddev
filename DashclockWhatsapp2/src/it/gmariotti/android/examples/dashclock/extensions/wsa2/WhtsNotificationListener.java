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
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class WhtsNotificationListener extends NotificationListenerService {
	
	
	private static final String TAG = LogUtils
			.makeLogTag(WhtsNotificationListener.class);
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		LOGD(TAG,"Notification Listener created!");
		MessageManager mManager=MessageManager.getInstance(this);
	}
	
	/**
	 * Add a single notification
	 * 
	 * @param sbn     statusbarnotification
	 * @param update  true to update the dashclock
	 */
	public void addNotification(StatusBarNotification sbn,boolean updateDash)  {
		
		if (sbn==null) return;
		
		if (sbn!=null && sbn.getPackageName().equalsIgnoreCase("com.whatsapp")){
			MessageManager mManager=MessageManager.getInstance(this);
			if (mManager!=null){
				mManager.addNotification(sbn.getNotification(),sbn.getPostTime(),updateDash);
			}
		}
		
	}
	
	
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		
		LOGD(TAG,"Notification Posted:\n");
		LOGD(TAG,"StatusBarNotification="+ sbn.toString());
		if (sbn.getNotification()!=null)
			LOGD(TAG,"Notification="+sbn.getNotification().toString());
		addNotification(sbn,true); 
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		LOGD(TAG,"Notification Removed:\n");
		if (sbn!=null && sbn.getPackageName().equalsIgnoreCase("com.whatsapp")){
			MessageManager mManager=MessageManager.getInstance(this);
			if (mManager!=null){
				mManager.clearAll();
			}
		}
	}


	

}
