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

import android.app.Notification;
import android.service.notification.StatusBarNotification;

public class MessageManager {

	private final static String TAG = "MessageManager";

	private WhtsExtension mDashExtension;
	private WhtsNotificationListener mNotificationListener;
	private static MessageManager sInstance;
	
	//----------------------------------------------------------------------------
	
	public static MessageManager getInstance(WhtsExtension context) {
		if (sInstance == null) 
			sInstance = new MessageManager();
		if (sInstance.mDashExtension==null)
			sInstance.mDashExtension=context;			
		return sInstance;
	}
	
	public static MessageManager getInstance(WhtsNotificationListener listener) {
		if (sInstance == null) 
			sInstance = new MessageManager();
		if (sInstance.mNotificationListener==null)
			sInstance.mNotificationListener=listener;
		return sInstance;
	}
	
	private MessageManager() {
		mCount = 0;
		mMsgs = new ArrayList<MessageWht>();
	}
	
	public void setListener(WhtsNotificationListener notificationListener) {
		this.mNotificationListener=notificationListener;
	}

	//----------------------------------------------------------------------------
	
	private ArrayList<MessageWht> mMsgs;
	private int mCount;

	
	public void addNotification(Notification notification, long postTime,boolean updateDash) {
		
		if (notification==null) return;
		
		String rawText=""+notification.tickerText;
		LOGD(TAG,"rawrext="+rawText);
		
		MessageWht msg=new MessageWht();
		msg.setText(rawText);
		mCount++;
		mMsgs.add(msg);
		
		if (updateDash && mDashExtension!=null)
			mDashExtension.onUpdateData(WhtsExtension.UPDATE_REASON_CONTENT_CHANGED);
		
	}	
	
	
	public void clearAll() {
		mCount=0;
		mMsgs=new ArrayList<MessageWht>();
		if (mDashExtension!=null)
			mDashExtension.onUpdateData(WhtsExtension.UPDATE_REASON_CONTENT_CHANGED);
	}
	
	
	public void getActivNotifications(StatusBarNotification[] sbns){
		mCount=0;
		mMsgs=new ArrayList<MessageWht>();
		LOGD(TAG,"activeNotification ");
		if (sbns!=null){
			LOGD(TAG,"activeNotification "+sbns.toString());
			for (StatusBarNotification sbn:sbns){
				mNotificationListener.addNotification(sbn,false);
			}
		}
		
		if (mDashExtension!=null)
			mDashExtension.onUpdateData(WhtsExtension.UPDATE_REASON_CONTENT_CHANGED);
	}
	
	
	//----------------------------------------------------------------------------------

	public WhtsExtension getmDashExtension() {
		return mDashExtension;
	}
	
	public int getmCount() {
		return mCount;
	}

	public WhtsNotificationListener getmNotificationListener() {
		return mNotificationListener;
	}

	public ArrayList<MessageWht> getmMsgs() {
		return mMsgs;
	}


	
		

	
	
	
}
