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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountManager;
import android.content.ContentValues;
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
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class SmsRestoreGDriveActivity extends SherlockActivity {

	private static String TAG = "SmsRestoreGDriveActivity";

	static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION_FOLDER = 2;

	private final static String PREF_ACCOUNT_NAME = "ACCOUNT_NAME";

	private GoogleAccountCredential mCredential;
	private static Drive mService;
	private String mAccountName;

	private static final String MIME_TEXT_PLAIN = "text/plain";
	private static final String MIME_FOLDER = "application/vnd.google-apps.folder";
	private static final String FOLDER_NAME = "backup";

	private boolean useDataFolder = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_restore_gdrive);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	
	private String getContentFile(File file) {
		String result;

		if (file != null && file.getDownloadUrl() != null
				&& file.getDownloadUrl().length() > 0) {

			try {
				GenericUrl downloadUrl = new GenericUrl(file.getDownloadUrl());

				HttpResponse resp = mService.getRequestFactory()
						.buildGetRequest(downloadUrl).execute();
				InputStream inputStream = null;

				try {
					inputStream = resp.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					StringBuilder content = new StringBuilder();
					char[] buffer = new char[1024];
					int num;

					while ((num = reader.read(buffer)) > 0) {
						content.append(buffer, 0, num);
					}
					result = content.toString();

				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
				}
			} catch (IOException e) {
				// An error occurred.
				e.printStackTrace();
				return null;
			}
		} else {
			// The file doesn't have any content stored on Drive.
			return null;
		}

		return result;
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
				DriveScopes.DRIVE,
				"https://www.googleapis.com/auth/drive.appdata");
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
	private void restoreSmsFromGDrive() {

		// Check for use data folder
		CheckBox checkbox = (CheckBox) findViewById(R.id.appdatafolder);
		if (checkbox != null && checkbox.isChecked())
			useDataFolder = true;
		else
			useDataFolder = false;

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					showToast("Start process");

					Map<String, Map<String, String>> messages = new HashMap<String, Map<String, String>>();

					// Create sms backup Folder
					File folder = null;
					if (mService == null)
						initService();

					String folderId = "appdata";

					if (!useDataFolder) {
						folder = _isFolderExists();
						if (folder != null)
							folderId = folder.getId();
					}

					if (folderId != null) {

						Files.List request = mService
								.files()
								.list()
								.setQ("mimeType = '" + MIME_TEXT_PLAIN
										+ "' and '" + folderId
										+ "' in parents ");

						FileList files = request.execute();

						if (files != null) {
							for (File file : files.getItems()) {

								// TODO: get only a file

								// Meta data
								Log.i(TAG, "Title: " + file.getTitle());
								Log.i(TAG,
										"Description: " + file.getDescription());
								Log.i(TAG, "MIME type: " + file.getMimeType());

								String content = getContentFile(file);
								if (content != null) {

									// Parse Json
									JSONObject json = new JSONObject(content);
									JSONArray msgs = (JSONArray) json
											.get("messages");
									for (int i = 0; i < msgs.length(); i++) {

										JSONObject json_dataSms = msgs
												.getJSONObject(i);
										Iterator<String> keys = json_dataSms
												.keys();
										Map<String, String> message = new HashMap<String, String>();
										String idKey = null;
										while (keys.hasNext()) {
											String key = (String) keys.next();
											message.put(key,
													json_dataSms.getString(key));

											idKey = json_dataSms
													.getString(TelephonyProviderConstants.Sms._ID);
										}
										// Put message in hashMap
										messages.put(idKey, message);
									}
									restore(messages);
									continue;
								}

							}
						}

					}
				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent, REQUEST_AUTHORIZATION_FOLDER);
				} catch (IOException e) {
					Log.e("TAG", "Error in backup", e);
				} catch (JSONException e) {
					Log.e("TAG", "Error in backup", e);
				}

			}

			private void restore(Map<String, Map<String, String>> messages) {

				if (messages != null) {

					for (Map.Entry<String, Map<String, String>> entry : messages
							.entrySet()) {
						String idkey = entry.getKey();
						Map<String, String> msg = entry.getValue();
						String type = msg
								.get(TelephonyProviderConstants.Sms.TYPE);

						ContentValues values = new ContentValues();
						values.put(TelephonyProviderConstants.Sms.BODY,
								msg.get(TelephonyProviderConstants.Sms.BODY));
						values.put(TelephonyProviderConstants.Sms.ADDRESS,
								msg.get(TelephonyProviderConstants.Sms.ADDRESS));
						values.put(TelephonyProviderConstants.Sms.TYPE,
								msg.get(TelephonyProviderConstants.Sms.TYPE));
						values.put(TelephonyProviderConstants.Sms.PROTOCOL, msg
								.get(TelephonyProviderConstants.Sms.PROTOCOL));
						values.put(
								TelephonyProviderConstants.Sms.SERVICE_CENTER,
								msg.get(TelephonyProviderConstants.Sms.SERVICE_CENTER));
						values.put(TelephonyProviderConstants.Sms.DATE,
								msg.get(TelephonyProviderConstants.Sms.DATE));
						values.put(TelephonyProviderConstants.Sms.STATUS,
								msg.get(TelephonyProviderConstants.Sms.STATUS));
						// values.put(TelephonyProviderConstants.Sms.THREAD_ID,
						// msg.get(TelephonyProviderConstants.Sms.THREAD_ID));
						values.put(TelephonyProviderConstants.Sms.READ,
								msg.get(TelephonyProviderConstants.Sms.READ));
						values.put(TelephonyProviderConstants.Sms.DATE_SENT,
								msg.get(TelephonyProviderConstants.Sms.DATE_SENT));

						/*
						 * for (Map.Entry<String, String> fields :
						 * msg.entrySet()) { String idfield = fields.getKey();
						 * String valueField = fields.getValue();
						 * 
						 * values.put(idfield,valueField); }
						 */

						if (type != null
								&& (Integer.parseInt(type) == TelephonyProviderConstants.Sms.MESSAGE_TYPE_INBOX || Integer
										.parseInt(type) == TelephonyProviderConstants.Sms.MESSAGE_TYPE_SENT)
								&& !smsExists(values)) {
							final Uri uri = getContentResolver().insert(
									TelephonyProviderConstants.Sms.CONTENT_URI,
									values);
							if (uri != null) {
								showToast("Restored message="
										+ msg.get(TelephonyProviderConstants.Sms.BODY));
							}
						} else {
							Log.d(TAG, "ignore");
						}
					}

				}
			}

			private boolean smsExists(ContentValues values) {
				// just assume equality on date+address+type
				Cursor c = getContentResolver()
						.query(TelephonyProviderConstants.Sms.CONTENT_URI,
								new String[] { "_id" },
								"date = ? AND address = ? AND type = ?",
								new String[] {
										values.getAsString(TelephonyProviderConstants.Sms.DATE),
										values.getAsString(TelephonyProviderConstants.Sms.ADDRESS),
										values.getAsString(TelephonyProviderConstants.Sms.TYPE) },
								null);

				boolean exists = false;
				if (c != null) {
					exists = c.getCount() > 0;
					c.close();
				}
				return exists;
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
					restoreSmsFromGDrive();
				}

			}
			break;
		case REQUEST_AUTHORIZATION_FOLDER:
			if (resultCode == RESULT_OK) {
				restoreSmsFromGDrive();
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
