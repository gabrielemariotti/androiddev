package it.gmariotti.android.examples.dashclock.extensions.wht;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
