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

import it.gmariotti.android.examples.slidingpane.MyListFragment.ListFragmentItemClickListener;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.view.ViewTreeObserver;

public class MainActivity extends Activity implements
		ListFragmentItemClickListener {

	private SlidingPaneLayout mSlidingLayout;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mActionBar = getActionBar();

		mSlidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);

		mSlidingLayout.setPanelSlideListener(new SliderListener());
		mSlidingLayout.openPane();

		mSlidingLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new FirstLayoutListener());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar up action should open the slider if it is currently
		 * closed, as the left pane contains content one level up in the
		 * navigation hierarchy.
		 */
		if (item.getItemId() == android.R.id.home && !mSlidingLayout.isOpen()) {
			mSlidingLayout.openPane();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This panel slide listener updates the action bar accordingly for each
	 * panel state.
	 */
	private class SliderListener extends
			SlidingPaneLayout.SimplePanelSlideListener {
		@Override
		public void onPanelOpened(View panel) {
			Toast.makeText(panel.getContext(), "Opened", Toast.LENGTH_SHORT)
					.show();

			panelOpened();
		}

		@Override
		public void onPanelClosed(View panel) {
			Toast.makeText(panel.getContext(), "Closed", Toast.LENGTH_SHORT)
					.show();

			panelClosed();
		}

		@Override
		public void onPanelSlide(View view, float v) {
		}
	}

	@Override
	public void onListFragmentItemClick(View view, int position) {
		mActionBar.setTitle(MyListFragment.items[position]);
		mSlidingLayout.closePane();
	}

	/**
	 * 
	 * @param panel
	 */
	private void panelClosed() {
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);

		getFragmentManager().findFragmentById(R.id.content_pane)
				.setHasOptionsMenu(true);
		getFragmentManager().findFragmentById(R.id.list_pane)
				.setHasOptionsMenu(false);

	}

	/**
	 * 
	 * @param panel
	 */
	private void panelOpened() {
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);

		if (mSlidingLayout.isSlideable()) {
			getFragmentManager().findFragmentById(R.id.content_pane)
					.setHasOptionsMenu(false);
			getFragmentManager().findFragmentById(R.id.list_pane)
					.setHasOptionsMenu(true);
		} else {
			getFragmentManager().findFragmentById(R.id.content_pane)
					.setHasOptionsMenu(true);
			getFragmentManager().findFragmentById(R.id.list_pane)
					.setHasOptionsMenu(false);
		}
	}

	/**
	 * This global layout listener is used to fire an event after first layout
	 * occurs and then it is removed. This gives us a chance to configure parts
	 * of the UI that adapt based on available space after they have had the
	 * opportunity to measure and layout.
	 */
	private class FirstLayoutListener implements
			ViewTreeObserver.OnGlobalLayoutListener {
		@Override
		public void onGlobalLayout() {

			if (mSlidingLayout.isSlideable() && !mSlidingLayout.isOpen()) {
				panelClosed();
			} else {
				panelOpened();
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				mSlidingLayout.getViewTreeObserver()
						.removeOnGlobalLayoutListener(this);
			} else {
				mSlidingLayout.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
			}
		}
	}
}
