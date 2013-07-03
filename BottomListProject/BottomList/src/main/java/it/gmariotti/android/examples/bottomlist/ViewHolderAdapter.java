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
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Good Adapter.
 * 
 * 
 */
public class ViewHolderAdapter extends ArrayAdapter<MyObj> {

	private static String TAG = "RecycleAdapter";

	private Activity mContext;
	private LayoutInflater mInflater;

	public ViewHolderAdapter(Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class ViewHolder {
		TextView name;
		TextView longtext;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d(TAG, "position=" + position);

		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.longtext = (TextView) convertView
					.findViewById(R.id.longtext);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MyObj data = getItem(position);
		holder.name.setText(data.name);
		holder.longtext.setText(data.longText);

		return convertView;

	}

}