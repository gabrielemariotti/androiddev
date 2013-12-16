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
import android.content.Context;
import android.os.Bundle;
import android.os.UserManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mMultiEntryValue;
    private TextView mChoiceEntryValue;
    private TextView mBooleanEntryValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rp_activity_main);

        mMultiEntryValue = (TextView) findViewById(R.id.multi_values);
        mChoiceEntryValue = (TextView) findViewById(R.id.choice_value);
        mBooleanEntryValue = (TextView) findViewById(R.id.boolean_value);

        Bundle mRestrictionsBundle =
                ((UserManager) getSystemService(Context.USER_SERVICE))
                        .getApplicationRestrictions(getPackageName());

        if (mRestrictionsBundle != null) {

            if (mRestrictionsBundle.containsKey(MyRestrictedProfileBroadcast.KEY_BOOLEAN))
                mBooleanEntryValue.setText("" + mRestrictionsBundle.getBoolean(MyRestrictedProfileBroadcast.KEY_BOOLEAN));
            else
                mBooleanEntryValue.setText("");

            if (mRestrictionsBundle.containsKey(MyRestrictedProfileBroadcast.KEY_CHOICE)) {
                mChoiceEntryValue.setText(mRestrictionsBundle.getString(MyRestrictedProfileBroadcast.KEY_CHOICE));
            }else
                mChoiceEntryValue.setText("");

            if (mRestrictionsBundle.containsKey(MyRestrictedProfileBroadcast.KEY_MULTI_SELECT)) {
                String[] values = mRestrictionsBundle.getStringArray(MyRestrictedProfileBroadcast.KEY_MULTI_SELECT);
                StringBuffer sb = new StringBuffer();
                String and = "";
                for (String value : values) {
                    sb.append(and);
                    sb.append(value);
                    and = "-";
                }
                mMultiEntryValue.setText(sb.toString());
            }else{
                mMultiEntryValue.setText("");
            }
        }
    }


}
