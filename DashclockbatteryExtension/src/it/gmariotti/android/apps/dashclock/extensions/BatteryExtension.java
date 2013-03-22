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

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class BatteryExtension extends DashClockExtension {

	private static final String TAG = "BatteryExtension";

	public static final String PREF_BATTERY = "pref_Battery";
	public static final String PREF_BATTERY_CHARGE = "pref_battery_charge";
	public static final String PREF_BATTERY_VOLTAGE = "pref_battery_voltage";
	public static final String PREF_BATTERY_TEMP = "pref_battery_temp";

	// Prefs
	protected boolean prefCharge = true;
	protected boolean prefTemp = true;
	protected boolean prefVoltage = true;

	// Value
	private int level;
	private String charging;
	private String charge;
	private int voltage;
	private int temperature;
	private String umTemp;
	private String umVoltage = "";

	@Override
	protected void onInitialize(boolean isReconnect) {
		super.onInitialize(isReconnect); 
		if (!isReconnect) {
			//Listener for change battery: I prefer listen only for power change. 
			//You can change it with ACTION_BATTERY_CHANGED
			IntentFilter filter=new IntentFilter();
			filter.addAction(Intent.ACTION_POWER_CONNECTED);
			filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
			
			getApplicationContext().registerReceiver(mBatteryReceiver,
					filter);
		}
	}

	@Override
	protected void onUpdateData(int reason) {
		// Read Preferences
		readPreferences();

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

		// health
		// int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH,
		// -1);

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
	}

	
	private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			onUpdateData(UPDATE_REASON_CONTENT_CHANGED);
		}
	};

}