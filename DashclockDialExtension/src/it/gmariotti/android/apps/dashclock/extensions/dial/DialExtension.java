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
package it.gmariotti.android.apps.dashclock.extensions.dial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class DialExtension extends DashClockExtension {

	private static final String TAG = "DialExtension";
	
	public static final String PREF_FAVORITE_CONTACT = "pref_dial_contact";
	
	// Prefs
	protected Contact prefContact = null;
	
	@Override
	protected void onUpdateData(int reason) {
		
		//Read Preferences
		readPreferences();
		
		//publish
		publishUpdateExtensionData();
	}

	
	/**
	 * publish Update data
	 */
	private void publishUpdateExtensionData() {
		
		if (prefContact!=null){
			//Intent
			Intent dialIntent = new Intent (Intent.ACTION_DIAL,	Uri.parse("tel:"+prefContact.getPhoneNumber()));
					
			// Publish the extension data update.
			publishUpdate(new ExtensionData()
					.visible(true)
					.icon(R.drawable.ic_extension_dial)
					.status(getString(R.string.dial))
					.expandedTitle(getString(R.string.dial))
					.expandedBody(
							prefContact.getDisplayName())
					.clickIntent(dialIntent));
		}
	}
	

	/**
	 * Read preference
	 */
	private void readPreferences() {
		// Get preference value.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String _idPrefContact = sp.getString(PREF_FAVORITE_CONTACT, null);
		prefContact=Contact.loadData(getApplicationContext(), _idPrefContact);
				
	}
	
	
}