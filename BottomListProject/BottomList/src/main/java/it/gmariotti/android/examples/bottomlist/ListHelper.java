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
package it.gmariotti.android.examples.bottomlist;

import android.app.Activity;

import java.util.ArrayList;

public class ListHelper {

    protected static ArrayList<MyObj> buildData() {
        ArrayList<MyObj> list = new ArrayList<MyObj>();

        MyObj obj = new MyObj("Gabriele", "This is the first message");
        list.add(obj);

        MyObj obj2 = new MyObj("Andrea", "This is the second message");
        list.add(obj2);

        MyObj obj3 = new MyObj("Gabriele", "This is the third message");
        list.add(obj3);

        return list;
    }

    // -----------------------------------------------------------------------------------------------------------


    public static ViewHolderAdapter buildViewHolderAdapter(Activity context,
                                                           int textViewResourceId) {

        ArrayList<MyObj> list = buildData();
        ViewHolderAdapter viewHolder = new ViewHolderAdapter(context, textViewResourceId);
        viewHolder.addAll(list);
        return viewHolder;
    }

    // -----------------------------------------------------------------------------------------------------------

}
