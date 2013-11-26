package it.gmariotti.android.examples.appops;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        launchOps();
        initAppOpsManager();

    }

    /**
     * Uses AppOpsManager
     */
    private void initAppOpsManager() {
        //Don't use this code!
        String packageName = "it.gmariotti.android.examples.appops";

        AppOpsManager mAppOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);

        PackageManager mPm = getPackageManager();
        PackageInfo mPackageInfo=null;
        try {
            mPackageInfo = mPm.getPackageInfo(packageName,
                    PackageManager.GET_DISABLED_COMPONENTS |
                            PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (Exception e) {
        }

        //mAppOps.checkPackage(mPackageInfo.applicationInfo.uid,packageName);

        int permission= mAppOps.noteOp(AppOpsManager.OPSTR_COARSE_LOCATION, mPackageInfo.applicationInfo.uid, mPackageInfo.packageName);
        Log.d("OPS","permission="+permission);

        /*
        int permission2= mAppOps.checkOp(AppOpsManager.OPSTR_COARSE_LOCATION, mPackageInfo.applicationInfo.uid, mPackageInfo.packageName);
        Log.d("OPS","permission2="+permission2);
*       */
    }

    /**
     * Launches AppOpsSummary
     */
    private void launchOps() {

        Intent intent = new Intent("android.settings.SETTINGS");
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                "com.android.settings.applications.AppOpsSummary");
        startActivity(intent);
    }

}
