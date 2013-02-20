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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class Contact {

	private static String TAG="Contact";
	
	protected String _id;
	protected String displayName;
	protected String phoneNumber;
	
	/**
	 * Load data from UriContact
	 * 
	 * @param context     context
	 * @param uriContact  uriContact
	 */
	public static Contact loadData(Context context,String uriContact){
		
		if (context==null || uriContact==null) return null;
		
		Contact mContact=null;
		Log.d(TAG,"Load Data id="+uriContact);
		
		Uri uri= Uri.parse(uriContact);
		
		//Search for uri
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
        		Log.d(TAG,"cursor="+cursor);
                // DISPLAY_NAME = The display name for the contact.
                // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
        		mContact=new Contact();
        		//mContact.displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		mContact._id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        		Log.d(TAG, "id="+mContact._id);
        }
        cursor.close();
		
        if (mContact==null) return null;
		
      
    	// Return all the PHONE data for the contact.
    	String where = ContactsContract.Data.CONTACT_ID +  " = " + mContact._id + " AND " +
    						ContactsContract.Data.MIMETYPE + " = '" +
    						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
    						"'" ; 
    						//ContactsContract.Data.IS_SUPER_PRIMARY + " = 1";
    	String[] projection = new String[] {
				    	ContactsContract.Data.DISPLAY_NAME,
				    	ContactsContract.CommonDataKinds.Phone.NUMBER };
    	
    	Cursor dataCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
    						projection, where, null, null);
    	
    	
    	// Get the indexes of the required columns.
    	int nameIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
    	int phoneIdx =	dataCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
    	
    	if(dataCursor.moveToNext()){ 
    		mContact=new Contact();
	    	// Extract the name.
	    	mContact.displayName = dataCursor.getString(nameIdx);
	    	// Extract the phone number.
	    	mContact.phoneNumber = dataCursor.getString(phoneIdx);
	    	Log.d(TAG,"name="+ mContact.displayName + " (" + mContact.phoneNumber + ")");
    	}
    	dataCursor.close();

    	return mContact;
    	
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	
}
