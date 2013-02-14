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
package it.gmariotti.android.examples.preference;

import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;

/**
 * Preference Summary
 * 
 */
public class PreferencesFragmentSummary extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	RingtonePreference mRingtoneOnListener; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_summary);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

		initSummary();
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		if (mRingtoneOnListener!=null) mRingtoneOnListener.setOnPreferenceChangeListener(null); //Pay attention: it is just an example
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		// Update summary
		updatePrefsSummary(sharedPreferences, findPreference(key));
	}

	/**
	 * Update summary
	 * 
	 * @param sharedPreferences
	 * @param pref
	 */
	protected void updatePrefsSummary(SharedPreferences sharedPreferences,
			Preference pref) {

		if (pref == null)
			return;

		if (pref instanceof ListPreference) {
			// List Preference
			ListPreference listPref = (ListPreference) pref;
			listPref.setSummary(listPref.getEntry());

		} else if (pref instanceof EditTextPreference) {
			// EditPreference
			EditTextPreference editTextPref = (EditTextPreference) pref;
			editTextPref.setSummary(editTextPref.getText());

		} else if (pref instanceof MultiSelectListPreference) {
			// MultiSelectList Preference
			MultiSelectListPreference mlistPref = (MultiSelectListPreference) pref;
			String summaryMListPref = "";
			String and = "";

			// Retrieve values
			Set<String> values = mlistPref.getValues();
			for (String value : values) {
				// For each value retrieve index
				int index = mlistPref.findIndexOfValue(value);
				// Retrieve entry from index
				CharSequence mEntry = index >= 0
						&& mlistPref.getEntries() != null ? mlistPref
						.getEntries()[index] : null;
				if (mEntry != null) {
					// add summary
					summaryMListPref = summaryMListPref + and + mEntry;
					and = ";";
				}
			}
			// set summary
			mlistPref.setSummary(summaryMListPref);

		} else if (pref instanceof RingtonePreference) {
			// RingtonePreference
			RingtonePreference rtPref = (RingtonePreference) pref;
			String uri;
			if (rtPref != null) {
				uri = sharedPreferences.getString(rtPref.getKey(), null);
				if (uri != null) {
					Ringtone ringtone = RingtoneManager.getRingtone(
							getActivity(), Uri.parse(uri));

					pref.setSummary(ringtone.getTitle(getActivity()));
				}
			}

		} else if (pref instanceof NumberPickerPreference) {
			// My NumberPicker Preference
			NumberPickerPreference nPickerPref = (NumberPickerPreference) pref;
			nPickerPref.setSummary(nPickerPref.getValue());
		}

	}

	/*
	 * Init summary
	 */
	protected void initSummary() {
		int pcsCount=getPreferenceScreen().getPreferenceCount();
		for (int i = 0; i < pcsCount; i++) {
			initPrefsSummary(getPreferenceManager().getSharedPreferences(),
					getPreferenceScreen().getPreference(i));
		}
	}

	/*
	 * Init single Preference
	 */
	protected void initPrefsSummary(SharedPreferences sharedPreferences,
			Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory pCat = (PreferenceCategory) p;
			int pcCatCount= pCat.getPreferenceCount();
			for (int i = 0; i < pcCatCount; i++) {
				initPrefsSummary(sharedPreferences, pCat.getPreference(i));
			}
		} else {
			updatePrefsSummary(sharedPreferences, p);
			if (p instanceof RingtonePreference){
				p.setOnPreferenceChangeListener(new RingToneOnPreferenceChangeListener());
				mRingtoneOnListener=(RingtonePreference)p; //Pay attention: it is just an example
			}
		}
	}

	
	/**
	 * Listener for RingTonePreference
	 * It does not fire onSharedPreferenceChanged when a ringtone is selected
	 *
	 */
	class RingToneOnPreferenceChangeListener implements
			OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference pref, Object newValue) {
			if (newValue != null && newValue instanceof String) {
				String uri = (String) newValue;
				Ringtone ringtone = RingtoneManager.getRingtone(getActivity(),
						Uri.parse(uri));
				pref.setSummary(ringtone.getTitle(getActivity()));
			}
			return true;
		}
	}
	
	

}