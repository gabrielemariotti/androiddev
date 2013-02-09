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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	

	public static final String[] options = { "Scenario 1 : Preference Activity",
											 "Scenario 1 : Preference Activity with ABS",
											 "Scenario 2 : Preference Fragment",
											 "Scenario 3 : Preference Headers (API Level 11)" ,
											 "Scenario 3 : Preference Headers with ABS (API Level 11)",
											 "Scenario 4 : Preference Headers with ABS all devices",
											 "Preference summary (API Level 11)"
										};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent=null;

		switch (position) {
			default:
			case 0:
				intent = new Intent(this, PreferencesActivityScenario1.class);
				break;
			case 1:
				intent = new Intent(this, PreferencesActivityABSScenario1.class);
				break;
			case 2:
				intent = new Intent(this, PreferencesActivityScenario2.class);
				break;
			case 3:
				 if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
					 Toast toast=Toast.makeText(this, "Only with API 11 or higher", 5000);
					 toast.show();
				 }else
					 intent = new Intent(this, PreferencesActivityScenario3.class);
				break;
			case 4:
				 if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
					 Toast toast=Toast.makeText(this, "Only with API 11 or higher", 5000);	
					 toast.show();
				 }else
					 intent = new Intent(this, PreferencesActivityABSScenario3.class);
				break;
			case 5:	
				intent = new Intent(this, PreferencesActivityABSScenario4.class);
				break;
			case 6:
				 if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
					 Toast toast=Toast.makeText(this, "Only with API 11 or higher", 5000);	
					 toast.show();
				 }else
					 intent = new Intent(this, PreferencesActivitySummary.class);
				break;
		}
		
		if (intent!=null)
			startActivity(intent);
	}

	
}
