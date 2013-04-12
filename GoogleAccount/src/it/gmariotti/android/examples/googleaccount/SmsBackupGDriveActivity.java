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
package it.gmariotti.android.examples.googleaccount;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.UserDataHandler;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

public class SmsBackupGDriveActivity extends SherlockActivity {

	private static String TAG = "SmsBackupGDriveActivity";

	static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION_FOLDER = 2;

	private final static String PREF_ACCOUNT_NAME = "ACCOUNT_NAME";

	private GoogleAccountCredential mCredential;
	private static Drive mService;
	private String mAccountName;

	private static final String MIME_TEXT_PLAIN = "text/plain";
	private static final String MIME_FOLDER = "application/vnd.google-apps.folder";
	private static final String FOLDER_NAME = "backup";

	private boolean useDataFolder=false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_backup_gdrive);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/*
	 * 
	 */
	private StringBuilder readSmsInbox() {
		Cursor cursor = getSmsInboxCursor();
		StringBuilder messages = new StringBuilder();
		String and = "";

		if (cursor != null) {
			final String[] columns = cursor.getColumnNames();
			while (cursor.moveToNext()) {
				messages.append(and);
				// long id = cursor.getLong(SmsQuery._ID);
				long contactId = cursor.getLong(SmsQuery.PERSON);
				String address = cursor.getString(SmsQuery.ADDRESS);
				// long threadId = cursor.getLong(SmsQuery.THREAD_ID);
				// final long date = cursor.getLong(SmsQuery.DATE);

				final Map<String, String> msgMap = new HashMap<String, String>(
						columns.length);

				for (int i = 0; i < columns.length; i++) {
					String value = cursor.getString(i);
					msgMap.put(columns[i], cursor.getString(i));
					messages.append(columns[i]);
					messages.append("=");
					messages.append(value);
					messages.append(";");
				}
				and = "\n";

				
				/**
				 * Retrieve display name
				 * 
				 */
				String displayName = address;

				if (contactId > 0) {
					// Retrieve from Contacts with contact id
					Cursor contactCursor = tryOpenContactsCursorById(contactId);
					if (contactCursor != null) {
						if (contactCursor.moveToFirst()) {
							displayName = contactCursor
									.getString(RawContactsQuery.DISPLAY_NAME);
						} else {
							contactId = 0;
						}
						contactCursor.close();
					}
				} else {
					if (contactId <= 0) {
						// Retrieve with address
						Cursor contactCursor = tryOpenContactsCursorByAddress(address);
						if (contactCursor != null) {
							if (contactCursor.moveToFirst()) {
								displayName = contactCursor
										.getString(ContactsQuery.DISPLAY_NAME);
							}
							contactCursor.close();
						}
					}
				}

				messages.append("displayName");
				messages.append("=");
				messages.append(displayName);
				messages.append(";");

			}
		}

		return messages;
	}

	/**
	 * Retrieve sms
	 * 
	 * @return
	 */
	private Cursor getSmsInboxCursor() {

		try {
			return getContentResolver().query(
					TelephonyProviderConstants.Sms.CONTENT_URI,
					SmsQuery.PROJECTION, SmsQuery.WHERE_INBOX, null,
					TelephonyProviderConstants.Sms.DEFAULT_SORT_ORDER);
		} catch (Exception e) {
			Log.e(TAG,
					"Error accessing conversations cursor in SMS/MMS provider",
					e);
			return null;
		}
	}

	private Cursor tryOpenContactsCursorById(long contactId) {
		try {
			return getContentResolver().query(
					ContactsContract.RawContacts.CONTENT_URI.buildUpon()
							.appendPath(Long.toString(contactId)).build(),
					RawContactsQuery.PROJECTION, null, null, null);

		} catch (Exception e) {
			Log.e(TAG, "Error accessing contacts provider", e);
			return null;
		}
	}

	private Cursor tryOpenContactsCursorByAddress(String phoneNumber) {
		try {
			return getContentResolver().query(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon()
							.appendPath(Uri.encode(phoneNumber)).build(),
					ContactsQuery.PROJECTION, null, null, null);

		} catch (Exception e) {
			// Can be called by the content provider
			// java.lang.IllegalArgumentException: URI:
			// content://com.android.contacts/phone_lookup/
			Log.w(TAG, "Error looking up contact name", e);
			return null;
		}
	}

	private interface SmsQuery {
		String[] PROJECTION = { TelephonyProviderConstants.Sms._ID,
				TelephonyProviderConstants.Sms.ADDRESS,
				TelephonyProviderConstants.Sms.PERSON,
				TelephonyProviderConstants.Sms.THREAD_ID,
				TelephonyProviderConstants.Sms.BODY,
				TelephonyProviderConstants.Sms.DATE,
				TelephonyProviderConstants.Sms.TYPE,
				TelephonyProviderConstants.Sms.READ,
				TelephonyProviderConstants.Sms.DATE_SENT,
				TelephonyProviderConstants.Sms.ERROR_CODE,
				TelephonyProviderConstants.Sms.STATUS,
				TelephonyProviderConstants.Sms.SUBJECT,
				TelephonyProviderConstants.Sms.PROTOCOL,
				TelephonyProviderConstants.Sms.SERVICE_CENTER

		};

		int _ID = 0;
		int ADDRESS = 1;
		int PERSON = 2;
		int THREAD_ID = 3;
		int BODY = 4;
		int DATE = 5;
		int TYPE = 6;
		int READ = 7;
		int DATE_SENT = 8;
		int ERROR_CODE = 9;
		int STATUS = 10;
		int SUBJECT = 11;
		int PROTOCOL = 12;
		int SERVICE_CENTER = 13;

		String WHERE_INBOX = TelephonyProviderConstants.Sms.TYPE + " = "
				+ TelephonyProviderConstants.Sms.MESSAGE_TYPE_INBOX;

	}

	private interface RawContactsQuery {
		String[] PROJECTION = { ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, };

		int DISPLAY_NAME = 0;
	}

	private interface ContactsQuery {
		String[] PROJECTION = { ContactsContract.Contacts.DISPLAY_NAME, };

		int DISPLAY_NAME = 0;
	}

	/**
	 * Google Picker Account
	 */
	private void launchGooglePicker() {

		Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
				new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, true,
				null, null, null, null);
		startActivityForResult(googlePicker, REQUEST_ACCOUNT_PICKER);
	}

	/**
	 * Create a folder
	 */
	/*
	private void createFolder() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (mService == null)
					initService();

				try {
					File folder = _createFolder();
				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent, REQUEST_AUTHORIZATION_FOLDER);
				} catch (IOException e) {
					Log.e(TAG, "Error in create folder", e);
				}

			}
		});
		t.start();

	}*/

	/**
	 * internal create Folder
	 * 
	 * @return
	 * @throws UserRecoverableAuthIOException
	 * @throws IOException
	 */
	private File _createFolder() throws UserRecoverableAuthIOException,
			IOException {

		if (useDataFolder) return null;
		
		File folder = _isFolderExists();
		if (folder != null)
			return folder;

		File body = new File();
		body.setTitle(FOLDER_NAME);
		body.setMimeType(MIME_FOLDER);

		File file = mService.files().insert(body).execute();
		return file;

	}

	/**
	 * Verify if folder exists
	 * 
	 * @return
	 * @throws UserRecoverableAuthIOException
	 * @throws IOException
	 */
	private File _isFolderExists() throws UserRecoverableAuthIOException,
			IOException {
			
		Files.List request = mService
				.files()
				.list()
				.setQ("mimeType = '" + MIME_FOLDER + "' and title = '"
						+ FOLDER_NAME + "' ");

		FileList files = request.execute();

		if (files != null) {
			for (File file : files.getItems()) {
				return file;
			}
		}
		return null;
	}

	/**
	 * Init service
	 */
	private void initService() {
		mCredential = GoogleAccountCredential.usingOAuth2(this,
				DriveScopes.DRIVE,"https://www.googleapis.com/auth/drive.appdata");
		if (mAccountName == null) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			mAccountName = prefs.getString(PREF_ACCOUNT_NAME, null);
		}

		if (mAccountName != null) {
			mCredential.setSelectedAccountName(mAccountName);
			mService = getDriveService(mCredential);
		}
	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).build();
	}

	/**
	 * Backup SMSs to GDrive
	 */
	private void backupSmsToGDrive() {
		
		// Check for use data folder
		CheckBox checkbox=(CheckBox)findViewById(R.id.appdatafolder);
		if (checkbox!=null && checkbox.isChecked())
			useDataFolder=true;
		else
			useDataFolder=false;
		
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					showToast("Start process");
					
					// Retrieve sms
					StringBuilder messages = readSmsInbox();
					Log.i(TAG, messages.toString());

					// Create sms backup Folder
					File folder=null;
					if (mService == null)
						initService();
					
					if (!useDataFolder)
						folder= createBackupFolder();

					// File's metadata.
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
							Locale.ITALIAN).format(new Date());
					
					File body = new File();
					body.setTitle("Sms Backup " + timeStamp);
					body.setMimeType("text/plain");
					body.setDescription("Backup sms");

					// Set the parent folder.
					if (useDataFolder)
						body.setParents(Arrays.asList(new ParentReference().setId("appdata")));
					else
						body.setParents(Arrays.asList(new ParentReference()
							.setId(folder.getId())));

					ByteArrayContent content = ByteArrayContent.fromString(
							"text/plain", messages.toString());
					File file = mService.files().insert(body, content).execute();
					
					if (file != null) {
						showToast("Sms backup uploaded: " + file.getTitle());
					}

				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent, REQUEST_AUTHORIZATION_FOLDER);
				} catch (IOException e) {
					Log.e("TAG", "Error in backup", e);
				}
			}

			
			private File createBackupFolder() {
				

				File folder=null;
				try {
					folder = _createFolder();
					
				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent, REQUEST_AUTHORIZATION_FOLDER);
				} catch (IOException e) {
					Log.e(TAG, "Error in create folder", e);
				}
				
				return folder;
			}			

		});
		t.start();
	}

	
	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:

			if (resultCode == RESULT_OK) {
				mAccountName = data
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(this);
				prefs.edit().putString(PREF_ACCOUNT_NAME, mAccountName)
						.commit();

				if (mAccountName != null) {
					backupSmsToGDrive();
				}

			}
			break;
		case REQUEST_AUTHORIZATION_FOLDER:
			if (resultCode == RESULT_OK) {
				backupSmsToGDrive();
			} else {
				startActivityForResult(mCredential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
			break;
		
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_sms_backup_gdrive,
				menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_backup:
			launchGooglePicker();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
