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
package it.gmariotti.android.apps.dashclock.extensions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import static it.gmariotti.android.apps.dashclock.extensions.LogUtils.LOGD;
import static it.gmariotti.android.apps.dashclock.extensions.LogUtils.LOGI;
import static it.gmariotti.android.apps.dashclock.extensions.LogUtils.LOGE;


public class BatteryExtension extends DashClockExtension {

	private static final String TAG = "BatteryExtension";

	public static final String PREF_BATTERY = "pref_Battery";
	public static final String PREF_BATTERY_CHARGE = "pref_battery_charge";
	public static final String PREF_BATTERY_VOLTAGE = "pref_battery_voltage";
	public static final String PREF_BATTERY_TEMP = "pref_battery_temp";
	public static final String PREF_BATTERY_HEALTH = "pref_battery_health";
	public static final String PREF_BATTERY_REALTIME = "pref_battery_realtime";
	
	//Receiver for ACTION_BATTERY_CHANGED
	private BatteryChangeReceiver mBatteryChangeReceiver=null;  
	
	// Prefs
	protected boolean prefCharge = true;
	protected boolean prefTemp = true;
	protected boolean prefVoltage = true;
	protected boolean prefHealth = true;
	protected boolean prefRealtime = false;

	// Value
	private int level;
	private String charging;
	private String charge;
	private int voltage;
	private int temperature;
	private String umTemp;
	private String umVoltage = "";
	private String health;

	@Override
	protected void onInitialize(boolean isReconnect) {
		super.onInitialize(isReconnect); 
		if (!isReconnect) {
			readPreferences();
			
			//Listener for change battery: I prefer listen only for power change. 
			//You can change it with ACTION_BATTERY_CHANGED
			IntentFilter filter=new IntentFilter();
			filter.addAction(Intent.ACTION_POWER_CONNECTED);
			filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
			
			getApplicationContext().registerReceiver(mBatteryReceiver,
					filter);
			
			//User can choose for ACTION_BATTERY_CHANGED
			registerChangeReceiver();
		}
	}

	@Override
	protected void onUpdateData(int reason) {
		LOGD(TAG, "onUpdate "+reason);
		// Read Preferences
		readPreferences();

		//Register and Unregister changeReceiver
		registerChangeReceiver();
		
		// readBatteryData
		readBatteryData(null);

		// Todo : preferences
		umTemp = getString(R.string.celsius);
		umVoltage = getString(R.string.mv);

		// publish
		publishUpdateExtensionData();
	}
	

	private void readBatteryData(Intent batteryStatus) {

		if (batteryStatus == null) {
			IntentFilter ifilter = new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED);
			batteryStatus = getApplicationContext().registerReceiver(null,
					ifilter);
		}

		// Level
		level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		LOGD(TAG,"level="+level);
		
		 // health
		int healthState = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
		int healthId = getResources().getIdentifier("battery_health_" + healthState,
					"string", this.getPackageName());
		health = getString(healthId);
		LOGD(TAG,"healthState="+healthState); 

		// How are we charging?
		int chargePlug = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_PLUGGED, -1);
		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
		boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;
		
		
		charge = null;
		if (usbCharge || acCharge || wirelessCharge) {
			int resId = getResources().getIdentifier("charge_" + chargePlug,
					"string", this.getPackageName());
			charge = getString(resId);
		}

		/*
		 * if (usbCharge) charge = getString(R.string.charge_usb); else if
		 * (acCharge) charge = getString(R.string.charge_ac); else if
		 * (wirelessCharge) charge = getString(R.string.charge_wireless);
		 */

		// Are we charging / charged?
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		// boolean isCharging = status ==
		// BatteryManager.BATTERY_STATUS_CHARGING;
		// boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
		// boolean isDischarging = status ==
		// BatteryManager.BATTERY_STATUS_DISCHARGING;
		LOGD(TAG, "status="+status);

		int resIdCharging = getResources().getIdentifier("charging_" + status,
				"string", this.getPackageName());
		charging = getString(resIdCharging);

		/*
		 * charging = getString(R.string.discharging); if (isFull) charging =
		 * getString(R.string.full); else if (isCharging) charging =
		 * getString(R.string.charging);
		 */

		// String technology =
		// batteryStatus.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

		temperature = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_TEMPERATURE, 0);
		voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
		LOGD(TAG,"temperature="+temperature);
		LOGD(TAG,"voltage="+voltage);
	}

	/**
	 * publishUpdata
	 */
	private void publishUpdateExtensionData() {

		// Intent
		Intent powerUsageIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
		// ResolveInfo resolveInfo =
		// getPackageManager().resolveActivity(powerUsageIntent, 0);

		String and = "";
		StringBuffer sb = new StringBuffer();
		if (prefCharge && charge != null) {
			sb.append(charge);
			and = " - ";
		}

		if (prefTemp) {
			sb.append(and);
			sb.append(temperature / 10);
			sb.append(umTemp);
			and = " - ";
		}

		if (prefVoltage) {
			sb.append(and);
			sb.append(voltage);
			sb.append(umVoltage);
			and = " - ";
		}
		
		if (prefHealth) {
			sb.append(and);
			sb.append(health);
			and = " - ";
		}

		// Publish the extension data update.
		publishUpdate(new ExtensionData().visible(true)
				.icon(R.drawable.ic_extension_battery).status("" + level + "%")
				.expandedTitle("" + level + "% " + charging)
				.expandedBody(sb.toString()).clickIntent(powerUsageIntent));
	}

	/**
	 * Read preference
	 */
	private void readPreferences() {
		// Get preference value.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefVoltage = sp.getBoolean(PREF_BATTERY_VOLTAGE, true);
		prefCharge = sp.getBoolean(PREF_BATTERY_CHARGE, true);
		prefTemp = sp.getBoolean(PREF_BATTERY_TEMP, true);
		prefHealth = sp.getBoolean(PREF_BATTERY_HEALTH, true);
		prefRealtime = sp.getBoolean(PREF_BATTERY_REALTIME, false);
	}

	
	private void registerChangeReceiver(){
		//User can choose for ACTION_BATTERY_CHANGED
		LOGD(TAG,"real time="+prefRealtime);
		if (prefRealtime){
			//Register receiver if not already registered
			 if (mBatteryChangeReceiver==null){
				 IntentFilter ifilter = new IntentFilter(
						 Intent.ACTION_BATTERY_CHANGED);
				 mBatteryChangeReceiver=new BatteryChangeReceiver();
				 getApplicationContext().registerReceiver(mBatteryChangeReceiver,
						 ifilter);
			 }
		}else{
			//I don't want real time
			//If receiver has already register, unregister it
			if (mBatteryChangeReceiver!=null){
				getApplicationContext().unregisterReceiver(mBatteryChangeReceiver);
				mBatteryChangeReceiver=null;
			}
		}
	}
	
	private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			onUpdateData(UPDATE_REASON_CONTENT_CHANGED);
		}
	};

	
	private class BatteryChangeReceiver extends BroadcastReceiver {
		
		public void onReceive(Context context, Intent intent) {
			LOGD(TAG,"BatteryChangeReceiver");
			onUpdateData(UPDATE_REASON_CONTENT_CHANGED);
		}
	}
}