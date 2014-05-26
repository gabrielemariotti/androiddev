/*
 * ******************************************************************************
 *  * Copyright (c) 2014 Gabriele Mariotti.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package it.gmariotti.android.examples.playservices.drive;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import it.gmariotti.android.examples.playservices.R;


public class GDriveActivity extends DriveBaseActivity {

    private static final String TAG = "GDriveActivity";
    private static final int REQUEST_CODE_PICKER = 10;

    //Button to save
    Button mSave;

    //Intent Picker
    IntentSender intentPicker;

    //Google Drive Folder Id
    DriveId mFolderDriveId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdrive);

        //Setup the Save Button
        mSave = (Button) findViewById(R.id.save_to_gdrive);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
                        if (intentPicker==null)
                            intentPicker = buildIntent();
                        //Start the picker to choose a folder
                        startIntentSenderForResult(
                                intentPicker, REQUEST_CODE_PICKER, null, 0, 0, 0);
                        }
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Unable to send intent", e);
                }
            }
        });
        mSave.setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICKER:
                intentPicker = null;

                if (resultCode == RESULT_OK) {
                    //Get the folder drive id
                    mFolderDriveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    if (mFolderDriveId != null) {
                        //Create the file on GDrive
                        Drive.DriveApi.newContents(mGoogleApiClient)
                                .setResultCallback(creatingFileResult);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);

        //Enable the save button and intentPicker
        if (intentPicker == null) {
            intentPicker = buildIntent();
        }

        mSave.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        super.onConnectionSuspended(cause);
        mSave.setEnabled(false);
    }

    private IntentSender buildIntent(){
        return Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[]{DriveFolder.MIME_TYPE})
                .build(mGoogleApiClient);
    }

    //----------------------------------------------------------------------------------
    // Callbacks
    //----------------------------------------------------------------------------------

    final private ResultCallback<DriveApi.ContentsResult> creatingFileResult = new
            ResultCallback<DriveApi.ContentsResult>() {
                @Override
                public void onResult(DriveApi.ContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents");
                        return;
                    }

                    DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, mFolderDriveId);
                    OutputStream outputStream = result.getContents().getOutputStream();

                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.sea);
                    ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
                    try {
                        outputStream.write(bitmapStream.toByteArray());
                    } catch (IOException e1) {
                        Log.i(TAG, "Unable to write file contents.");
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file") //Your title
                            .setMimeType("image/jpeg")  //Your mime type
                            .setStarred(false).build();

                    folder.createFile(mGoogleApiClient, changeSet, result.getContents())
                            .setResultCallback(fileCallback);
                }
            };




    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create the file");
                        return;
                    }

                    showMessage("File saved on Google Drive");
                }
            };

    //----------------------------------------------------------------------------------
    // Utility methods
    //----------------------------------------------------------------------------------


    /**
     * Shows a toast message.
     */
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
