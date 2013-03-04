package it.gmariotti.android.examples.snippets;

import java.util.Date;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class RetrieveManifestInfoActivity extends SherlockActivity {

	private static final String TAG="RetrieveManifestInfoActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_manifest_info);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		retriveInfo();
	}

	
	/**
	 * The class PackageInfo exposes all the information contained within the Manifest.xml.
	 * We can easily get package name, version name,version code, last update time, permissions, services..
	 * 
	 */
	private void retriveInfo(){

		StringBuffer sb= new StringBuffer();
		
		try {
	        PackageInfo mInfo = getPackageManager().
	                           getPackageInfo(getPackageName(),PackageManager.GET_META_DATA);
	        Log.i("packageInfo", "PackageName = " + mInfo.packageName);
	        Log.i("packageInfo", "VersionName = " + mInfo.versionName);
	        Log.i("packageInfo", "VersionCode = " + mInfo.versionCode);
	        Log.i("packageInfo", "Last Update Time = " + new Date(mInfo.lastUpdateTime)); //Required API level 9
	        Log.i("packageInfo", "Permission = " + mInfo.permissions);
	        
	        sb.append("PackageName = " + mInfo.packageName);
	        sb.append("\n");
	        sb.append("VersionName = " + mInfo.versionName);
	        sb.append("\n");
	        sb.append("VersionCode = " + mInfo.versionCode);
	        sb.append("\n");
	        sb.append("Last Update Time = " + new Date(mInfo.lastUpdateTime));  //Required API level 9
	        sb.append("\n");
	        sb.append("Permission = " + mInfo.permissions);
	        
	        TextView tw=(TextView)findViewById(R.id.text_xml);
	        if (tw!=null)
	        	tw.setText(sb.toString());
	        
	    } catch (NameNotFoundException e) {
	         Log.e(TAG,"Error", e);
	    }
	}
	 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
