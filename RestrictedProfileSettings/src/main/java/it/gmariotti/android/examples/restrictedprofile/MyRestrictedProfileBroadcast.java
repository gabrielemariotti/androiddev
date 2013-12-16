/*
 * ******************************************************************************
 *  * Copyright (c) 2013 Gabriele Mariotti.
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

package it.gmariotti.android.examples.restrictedprofile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;

public class MyRestrictedProfileBroadcast extends BroadcastReceiver {

    public static String KEY_BOOLEAN = "rp_key_boolean";
    public static String KEY_CHOICE = "rp_key_choice";
    public static String KEY_MULTI_SELECT = "rp_key_multiselect";


    @Override
    public void onReceive(final Context context, Intent intent) {

        final PendingResult result = goAsync();

        final Bundle currentRestrictions =
                intent.getBundleExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE);

        new Thread() {
            public void run() {
                ArrayList<RestrictionEntry> list = initRestrictionEntries(context, currentRestrictions);

                Bundle extras = new Bundle();
                extras.putParcelableArrayList(Intent.EXTRA_RESTRICTIONS_LIST, list);
                result.setResult(Activity.RESULT_OK, null, extras);
                result.finish();
                return;
            }
        }.start();

    }


    /**
     * Init the Restriction Entries List
     *
     * @param context
     * @return
     */
    private ArrayList<RestrictionEntry> initRestrictionEntries(Context context, Bundle currentRestrictions) {

        if (context == null) return null;

        ArrayList<RestrictionEntry> list = new ArrayList<RestrictionEntry>();

        RestrictionEntry entry1 = new RestrictionEntry(KEY_BOOLEAN, true);  //default value!
        entry1.setTitle(context.getString(R.string.entry1_title));
        entry1.setDescription(context.getString(R.string.entry1_description));
        entry1.setType(RestrictionEntry.TYPE_BOOLEAN);

        //Retrieve current value
        if (currentRestrictions != null) {
            entry1.setSelectedState(currentRestrictions.getBoolean(KEY_BOOLEAN, true));
        }

        list.add(entry1);

        RestrictionEntry entry2 = new RestrictionEntry(KEY_CHOICE, (String) null);
        entry2.setTitle(context.getString(R.string.entry2_title));
        entry2.setDescription(context.getString(R.string.entry2_description));
        entry2.setType(RestrictionEntry.TYPE_CHOICE);
        entry2.setChoiceEntries(context, R.array.choice_entries);

        Resources res = context.getResources();
        String[] values = res.getStringArray(R.array.choice_values);
        entry2.setChoiceValues(values);

        //Retrieve current value
        if (currentRestrictions != null) {
            if (currentRestrictions.containsKey(KEY_CHOICE)) {
                entry2.setSelectedString(currentRestrictions.getString(KEY_CHOICE));

            } else {
                entry2.setSelectedString(values[0]); //Default
            }
        }
        list.add(entry2);

        RestrictionEntry entry3 = new RestrictionEntry(KEY_MULTI_SELECT, (String[]) null);
        entry3.setTitle(context.getString(R.string.entry3_title));
        entry3.setDescription(context.getString(R.string.entry3_description));
        entry3.setType(RestrictionEntry.TYPE_MULTI_SELECT);
        entry3.setChoiceEntries(context, R.array.choice_entries);

        entry3.setChoiceValues(values);
        String[] selected= new String[1];
        selected[0]= values[0];
        //Retrieve current value
        if (currentRestrictions != null) {
            if (currentRestrictions.containsKey(KEY_MULTI_SELECT)) {
                entry3.setAllSelectedStrings(currentRestrictions.getStringArray(KEY_MULTI_SELECT));
            } else {
                entry3.setAllSelectedStrings(selected); //Default
            }
        }

        list.add(entry3);

        return list;
    }

}
