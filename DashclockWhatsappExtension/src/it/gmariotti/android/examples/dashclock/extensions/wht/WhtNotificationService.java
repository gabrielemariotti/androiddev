package it.gmariotti.android.examples.dashclock.extensions.wht;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.IntentFilter;
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
