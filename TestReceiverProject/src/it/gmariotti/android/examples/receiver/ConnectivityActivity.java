package it.gmariotti.android.examples.receiver;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class ConnectivityActivity extends Activity {

	protected ConnectivityChangeReceiver mReceiver=null;
			
	@Override
	protected void onPause() {
		unregisterReceiver( mReceiver );
		super.onPause();
	}

	@Override
	protected void onResume() {
		mReceiver=new ConnectivityChangeReceiver();
		registerReceiver( mReceiver,
				          new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple);
	}

	
	

}
