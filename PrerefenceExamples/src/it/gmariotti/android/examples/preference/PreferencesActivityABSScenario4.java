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
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

/**
 * Scenario 4: Preference Headers
 * 
 */
public class PreferencesActivityABSScenario4 extends SherlockPreferenceActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
        //LEGACY METHOD FOR OLDER DEVICES ----------------------------------------------------------- 
        
        String action = getIntent().getAction();
        int preferenceFile_toLoad=-1;
        //Manage single fragment with action parameter
        if (action != null && action.equals(Constants.SETTING_UPDATE)) {
        	preferenceFile_toLoad=R.xml.preference_update;
        }else if (action != null && action.equals(Constants.SETTING_DISPLAY)) {  
        	preferenceFile_toLoad=R.xml.preference_display;
        }else if (action != null && action.equals(Constants.SETTING_NOTIFY)) {  
        	preferenceFile_toLoad=R.xml.preference_notify;
        } else{
        	//Inflate main preference file for Android<3.0
	        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	            // Load the legacy preferences headers
	        	preferenceFile_toLoad=R.xml.preference_headers_legacy_scenario4;
	        }
        }
	        
	    if (preferenceFile_toLoad>-1){
        	addPreferencesFromResource(preferenceFile_toLoad);
        }
    
        //---------------------------------------------------------------------------------------------
        
    }
	
	
	/**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
    	loadHeadersFromResource(R.xml.preference_headers_scenario3, target);
    }
	

}