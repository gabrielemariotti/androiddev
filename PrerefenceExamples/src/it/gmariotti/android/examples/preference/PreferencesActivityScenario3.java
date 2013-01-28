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
import android.preference.PreferenceActivity;

/**
 * Scenario 3: Preference Headers
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PreferencesActivityScenario3 extends PreferenceActivity {

  /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
    	loadHeadersFromResource(R.xml.preference_headers_scenario3, target);
    }
	

}