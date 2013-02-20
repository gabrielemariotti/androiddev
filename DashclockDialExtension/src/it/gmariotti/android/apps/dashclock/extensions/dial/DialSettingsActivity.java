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
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.provider.ContactsContract;
import android.util.Log;

public class DialSettingsActivity extends BaseSettingsActivity {

	private static final String TAG = "DialSettingsActivity";
	private static int PICK_CONTACT = 0;
	private Preference mContact;
	private String mContactKey="pref_dial_contact";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setIcon(R.drawable.ic_extension_dial);
    }

    @Override
    protected void setupSimplePreferencesScreen() {
        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.prefs_dial);

        //Get Custom contact Pref
        mContact = (Preference)findPreference(mContactKey);
        mContact.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(i, PICK_CONTACT);
                return true;
            }
        });
        
        setSummaryValue(null);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if ((requestCode == PICK_CONTACT) && (resultCode == RESULT_OK)) {
	    	 Log.d(TAG,"ActivityResult="+data.toString());
	    	 if (data != null) {
	    		   Uri uri = data.getData();
	    		   Log.d(TAG,"Uri="+uri);
	               String contactUri=  uri != null ? uri.toString() : "";
	               Log.d(TAG,"ContactUri="+contactUri);
	               
	               SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
	               editor.putString(mContactKey, contactUri);
	               editor.apply();
	               
	               setSummaryValue(contactUri);
	         }
	    }
    }
    
    
    /**
     * Set summary
     * @param contactUri
     */
    private void setSummaryValue(String contactUri){
    	
    	if (contactUri==null)
    		contactUri=getPreferenceManager().getSharedPreferences().getString(mContactKey, "");
    	
	    Contact contact = Contact.loadData(getApplicationContext(), contactUri);
	    if (contact!=null){
	     	   mContact.setSummary(contact.displayName);
	    }else{ 
	       	   mContact.setSummary(null);
	    }
   }
  	
}
