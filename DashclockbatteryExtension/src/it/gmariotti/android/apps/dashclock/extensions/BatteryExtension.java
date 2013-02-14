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

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class BatteryExtension extends DashClockExtension {

	private static final String TAG = "BatteryExtension";

	@Override
	protected void onUpdateData(int reason) {

		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = getApplicationContext().registerReceiver(null,
				ifilter);

		// Level
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

		//health
		//int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
				
		// How are we charging?
		int chargePlug = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_PLUGGED, -1);
		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
		boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;

		String charge = "";
		if (usbCharge)
			charge = getString(R.string.charge_usb);
		else if (acCharge)
			charge = getString(R.string.charge_ac);
		else if (wirelessCharge)
			charge = getString(R.string.charge_wireless);

		// Are we charging / charged?
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
		boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
		//boolean isDischarging = status == BatteryManager.BATTERY_STATUS_DISCHARGING;

		String charging = getString(R.string.discharging);
		if (isFull)
			charging=getString(R.string.full);
		else if (isCharging)
			charging = getString(R.string.charging);
		
		
		
		//String technology = batteryStatus.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
		
		int temperature = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_TEMPERATURE, 0);
		int voltage = batteryStatus
				.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

		String umTemp=getString(R.string.celsius);
		String umMv=getString(R.string.mv);
		
		// Publish the extension data update.
		publishUpdate(new ExtensionData()
				.visible(true)
				.icon(R.drawable.ic_extension_battery)
				.status("" + level + "%")
				.expandedTitle("" + level + "% " + charging)
				.expandedBody(
						charge +  " - " + temperature/10 + umTemp+" - "
								+ voltage+umMv));

	}
}