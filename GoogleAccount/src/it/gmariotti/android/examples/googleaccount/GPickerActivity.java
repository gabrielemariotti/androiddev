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

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class GPickerActivity extends SherlockActivity {

	static final int PICK_ACCOUNT_REQUEST=1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpicker);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_gpicker, menu);
		return true;
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

	/**
	 * Google Picker Account
	 */
	private void launchGooglePicker() {
		
		Intent googlePicker =AccountPicker.newChooseAccountIntent(null,null,new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},true,null,null,null,null) ;
		startActivityForResult(googlePicker,PICK_ACCOUNT_REQUEST);
	}
	
	
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Toast.makeText(this,"Account Name="+accountName, 3000).show();
        }
	}
	
}
