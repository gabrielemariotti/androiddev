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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

public class GDriveActivity extends SherlockListActivity {

	private static String TAG = "GDriveActivity";

	static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION_FOLDER = 2;
	static final int REQUEST_AUTHORIZATION_FILE = 3;
	static final int REQUEST_AUTHORIZATION_SEARCH = 4;
	static final int REQUEST_AUTHORIZATION_GETFILE = 5;

	private final static String PREF_ACCOUNT_NAME = "ACCOUNT_NAME";

	private GoogleAccountCredential mCredential;
	private static Drive mService;
	private String mAccountName;

	private static final String MIME_TEXT_PLAIN = "text/plain";
	private static final String MIME_FOLDER = "application/vnd.google-apps.folder";
	private static final String FOLDER_NAME = "backup";

	public static final String[] options = {
			"Snippet 1: Select Google Account", "Snippet 2: Create a folder",
			"Snippet 3: Create a file in a folder", "Snippet 4: Search a file or folder",
			"Snippet 5: Retrieve files in a folder" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, options));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		switch (position) {
		default:
		case 0:
			launchGooglePicker();
			break;
		case 1:
			createFolder();
			break;
		case 2:
			createFile();
			break;
		case 3:
			searchFolder();
			break;
		case 4:
			retrieveFiles();
			break;
		}
	}

	/**
	 * Retrieves files in a folder
	 */
	private void retrieveFiles() {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (mService == null)
					initService();
				try {
					File folder = _isFolderExists();
					if (folder != null) {

						Files.List request = mService
								.files()
								.list()
								.setQ("mimeType = '" + MIME_TEXT_PLAIN
										+ "' and '" + folder.getId()
										+ "' in parents ");

						FileList files = request.execute();

						if (files != null) {
							for (File file : files.getItems()) {

								// Meta data
								Log.i(TAG, "Title: " + file.getTitle());
								Log.i(TAG, "Description: " + file.getDescription());
								Log.i(TAG, "MIME type: " + file.getMimeType());

								String content=getContentFile(file);
								if (content!=null)
									showToast("Folder : [title]=" + folder.getTitle()
													+ " [id]=" + folder.getId()
												    + " File : [title]=" + file.getTitle() 
												    + " [id]=" + file.getId()
												    + " [content]=" + content
											);
								
							}

						}
					}
				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent,
							REQUEST_AUTHORIZATION_GETFILE);
				} catch (Exception e) {
					Log.e("TAG", "Error in upload", e);
				}
			}
		});
		t.start();

	}

	private String getContentFile(File file) {
		String result;
		
		if (file!=null && file.getDownloadUrl() != null
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

	/**
	 * Search folder
	 */
	private void searchFolder() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (mService == null)
					initService();
				try {
					File folder = _isFolderExists();
					if (folder != null) {
						showToast("Folder : [title]=" + folder.getTitle()
								+ " [id]=" + folder.getId());
					}

				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent, REQUEST_AUTHORIZATION_SEARCH);
				} catch (Exception e) {
					Log.e("TAG", "Error in upload", e);
				}
			}
		});
		t.start();

	}

	private void createFile() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (mService == null)
					initService();

				try {
					// Create Folder if it doesn't exist
					File folder = _createFolder();

					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
							Locale.ITALIAN).format(new Date());

					File body = new File();
					body.setTitle("Example file " + timeStamp);
					body.setMimeType(MIME_TEXT_PLAIN);
					body.setDescription("File Description");

					String bodyText = " Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt "
							+ "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco labor....";

					// Set the parent folder.
					body.setParents(Arrays.asList(new ParentReference()
							.setId(folder.getId())));

					ByteArrayContent content = ByteArrayContent.fromString(
							"text/plain", bodyText);
					File file = mService.files().insert(body, content)
							.execute();
					if (file != null) {
						showToast("File created: " + file.getTitle());
					}
				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent, REQUEST_AUTHORIZATION_FILE);
				} catch (IOException e) {
					Log.e("TAG", "Error in upload", e);
				}
			}
		});
		t.start();
	}

	/**
	 * Create a folder
	 */
	private void createFolder() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				if (mService == null)
					initService();

				try {
					File folder = _createFolder();
					if (folder != null) {
						showToast("Folder created : [title]="
								+ folder.getTitle() + " [id]=" + folder.getId());
					}
				} catch (UserRecoverableAuthIOException e) {
					Intent intent = e.getIntent();
					startActivityForResult(intent, REQUEST_AUTHORIZATION_FOLDER);
				} catch (IOException e) {
					Log.e(TAG, "Error in create folder", e);
				}

			}
		});
		t.start();

	}

	private File _createFolder() throws UserRecoverableAuthIOException,
			IOException {

		File folder = _isFolderExists();
		if (folder != null)
			return folder;

		File body = new File();
		body.setTitle(FOLDER_NAME);
		body.setMimeType(MIME_FOLDER);

		File file = mService.files().insert(body).execute();
		return file;

	}

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
				Log.i(TAG, "Folder exists [title]=" + file.getTitle()
						+ " [id]=" + file.getId());
				return file;
			}
		}

		return null;
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

			}
			break;
		case REQUEST_AUTHORIZATION_FOLDER:
			if (resultCode == RESULT_OK) {
				createFolder();
			} else {
				startActivityForResult(mCredential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
			break;
		case REQUEST_AUTHORIZATION_FILE:
			if (resultCode == RESULT_OK) {
				createFile();
			} else {
				startActivityForResult(mCredential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
			break;
		case REQUEST_AUTHORIZATION_SEARCH:
			if (resultCode == RESULT_OK) {
				searchFolder();
			} else {
				startActivityForResult(mCredential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
			break;
		case REQUEST_AUTHORIZATION_GETFILE:
			if (resultCode == RESULT_OK) {
				retrieveFiles();
			} else {
				startActivityForResult(mCredential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
			break;
		}
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,
						Toast.LENGTH_LONG).show();
				Log.i(TAG, toast);
			}
		});
	}

	/**
	 * Init service
	 */
	private void initService() {
		mCredential = GoogleAccountCredential.usingOAuth2(this,
				DriveScopes.DRIVE);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_add:
			launchGooglePicker();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
