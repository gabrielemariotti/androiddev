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

import java.util.ArrayList;

import android.util.Log;

public class MessageManager {

	private final static String TAG="MessageManager";
	
	private WhatsappExtension mWhtExtension;
	private static MessageManager sInstance;
	private SomethingOnScreenReceiver mReceiver;

	public static MessageManager getInstance(WhatsappExtension context) {
		if (sInstance == null) {
			sInstance = new MessageManager(context);
		}
		return sInstance;
	}

	public static MessageManager getInstance() {
		return sInstance;
	}

	private MessageManager(WhatsappExtension context) {
		mWhtExtension = context;
		mCount = 0;
		mMsgs = new ArrayList<MessageWht>();
	}

	
	private int mCount;
	private ArrayList<MessageWht> mMsgs;

	/**
	 * Notify for new Message
	 * @param msg
	 */
	public void notifyListener(MessageWht msg) {
		Log.d(TAG,"new Message");
		if (mWhtExtension != null){
			mCount++;
			mMsgs.add(msg);
			mWhtExtension.changeMessage();
		}
	}
	
	/**
	 * Reset counter and clear old messages
	 */
	public void clearMessages(){
		Log.d(TAG,"Clear Messages");
		mCount=0;
		mMsgs=new ArrayList<MessageWht>();
		if (mWhtExtension!=null)
			mWhtExtension.changeMessage();
	}

	public int getmCount() {
		return mCount;
	}

	public ArrayList<MessageWht> getmMsgs() {
		return mMsgs;
	}

	public SomethingOnScreenReceiver getmReceiver() {
		return mReceiver;
	}

	public void setmReceiver(SomethingOnScreenReceiver mReceiver) {
		this.mReceiver = mReceiver;
	}
	
	
}
