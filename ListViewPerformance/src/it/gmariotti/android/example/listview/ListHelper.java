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
package it.gmariotti.android.example.listview;

import java.util.ArrayList;

import android.app.Activity;

public class ListHelper {

	protected static final String longText="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	
	protected static ArrayList<MyObj> buildData() {
		ArrayList<MyObj> list = new ArrayList<MyObj>();
		for (int i = 1; i < 1000; i++) {
			MyObj obj = new MyObj("text" + i, "text text text 2  ",longText);
			list.add(obj);
		}
		return list;
	}

	// -----------------------------------------------------------------------------------------------------------

	public static MyBadAdapter buildBadAdapter(Activity context,
			int textViewResourceId) {

		ArrayList<MyObj> list = buildData();
		MyBadAdapter bad = new MyBadAdapter(context, textViewResourceId);
		bad.addAll(list);
		return bad;
	}

	public static RecycleAdapter buildRecycleAdapter(Activity context,
			int textViewResourceId) {

		ArrayList<MyObj> list = buildData();
		RecycleAdapter recycle = new RecycleAdapter(context, textViewResourceId);
		recycle.addAll(list);
		return recycle;
	}
	
	public static ViewHolderAdapter buildViewHolderAdapter(Activity context,
			int textViewResourceId) {

		ArrayList<MyObj> list = buildData();
		ViewHolderAdapter viewHolder = new ViewHolderAdapter(context, textViewResourceId);
		viewHolder.addAll(list);
		return viewHolder;
	}

	// -----------------------------------------------------------------------------------------------------------

}
