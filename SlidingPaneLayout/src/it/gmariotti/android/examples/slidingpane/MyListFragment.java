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
package it.gmariotti.android.examples.slidingpane;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyListFragment extends ListFragment {

	private ArrayAdapter<String> mAdapter;

	public static final String[] items = { "Item 1: xxxxxxxxxxxxxxxxx",
			"Item 2: xxxxxxxxxxxxxxxxx", "Item 3: xxxxxxxxxxxxxxxxx",
			"Item 4: xxxxxxxxxxxxxxxxx", "Item 5: xxxxxxxxxxxxxxxxx",
			"Item 6: xxxxxxxxxxxxxxxxx", "Item 7: xxxxxxxxxxxxxxxxx" };

	ListFragmentItemClickListener iItemClickListener;

	/** An interface for defining the callback method */
	public interface ListFragmentItemClickListener {
		/**
		 * This method will be invoked when an item in the ListFragment is
		 * clicked
		 */
		void onListFragmentItemClick(View view, int position);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, items);
		setListAdapter(mAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	/** A callback function, executed when this fragment is attached to an activity */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            /** This statement ensures that the hosting activity implements ListFragmentItemClickListener */
            iItemClickListener = (ListFragmentItemClickListener) activity;
        }catch(Exception e){
            Toast.makeText(activity.getBaseContext(), "Exception",Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.list, menu);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {

		/**
		 * Invokes the implementation of the method onListFragmentItemClick in
		 * the hosting activity
		 */
		iItemClickListener.onListFragmentItemClick(view, position);

	}

}
