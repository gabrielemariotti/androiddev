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
package it.gmariotti.android.examples.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class GenericPreferenceFragment extends PreferenceFragment {
  
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		int preferenceFile_toLoad=-1;
		
		 String settings = getArguments().getString("settings");
	     if (Constants.SETTING_UPDATE.equals(settings)) {
		        // Load the preferences from an XML resource
		        preferenceFile_toLoad= R.xml.preference_update;
	     }else if (Constants.SETTING_DISPLAY.equals(settings)) {
		        // Load the preferences from an XML resource
		        preferenceFile_toLoad=R.xml.preference_display;
	     }else if (Constants.SETTING_NOTIFY.equals(settings)) {
		        // Load the preferences from an XML resource
		        preferenceFile_toLoad=R.xml.preference_notify;
		 }
		
	     addPreferencesFromResource(preferenceFile_toLoad);
	}
}