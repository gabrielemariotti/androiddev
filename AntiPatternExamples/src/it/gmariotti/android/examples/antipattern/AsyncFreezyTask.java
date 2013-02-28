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
package it.gmariotti.android.examples.antipattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AsyncFreezyTask extends AsyncTask<Void, Integer, String> {
	
	private final Activity activity;
	private static final String TAG = "AsyncFreezyTask";

	private ProgressBar progress;
	private int count = 0;
	StringBuilder sb;

	public AsyncFreezyTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		progress = (ProgressBar) activity.findViewById(R.id.asyncTaskProgress);

		// This is an example of WHAT NOT TO DO !! This method runs
		// in Main UI Thread

		networkCall();  //You should make network call in doInBackground() method
		
		Log.d(TAG, "onPreExecute");
		
		//This is an example of WHAT NOT TO DO !!
		try{
			Thread.sleep(5000);
		}catch(Exception e){}
		
		Log.d(TAG, "onPreExecute END");
		
	}

	@Override
	protected String doInBackground(Void... params) {
		String ret = null;
		count = params.length;
		for (int i = 0; i < count; i++) {
			publishProgress(i);
		}
		if (sb!=null)
			ret = sb.toString();
		return ret;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		progress.setMax(count);
		progress.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(String result) {

		// This is an example of WHAT NOT TO DO !! This method runs
		// in Main UI Thread
		parsing(result);

		Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Network call.... You should make network call in doInBackground() method
	 * !!!
	 */
	private void networkCall() {
		// something...
	}

	private void parsing(String br) {
		// something...
	}
}
