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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * NumberPickerPreference 
 * 
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NumberPickerPreference extends DialogPreference {

	private static final Integer DEFAULT_VALUE = 1;
	NumberPicker mNumberPicker;
	Integer mCurrentValue;

	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.numberpicker_dialog);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogIcon(null);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {

		// When the user selects "OK", persist the new value
		if (positiveResult) {
			int newValue = mNumberPicker.getValue();
			if (callChangeListener(newValue)) {	
				mCurrentValue = newValue;
				persistInt(newValue);
			}
			
			//setSummary(mCurrentValue);
		}

		super.onDialogClosed(positiveResult);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		if (restorePersistedValue) {
			// Restore existing state
			mCurrentValue = this.getPersistedInt(DEFAULT_VALUE);
		} else {
			// Set default state from the XML attribute
			mCurrentValue = (Integer) defaultValue;
			persistInt(mCurrentValue);
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, DEFAULT_VALUE);
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		super.setDefaultValue(defaultValue);
		mCurrentValue = defaultValue instanceof Integer ? (Integer) defaultValue
				: Integer.parseInt(defaultValue.toString());
	}
	
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		this.mNumberPicker = (NumberPicker) view
				.findViewById(R.id.pref_num_picker);
		mNumberPicker.setMaxValue(25);
		mNumberPicker.setMinValue(1);
		mNumberPicker.setValue(mCurrentValue);
		if (mCurrentValue!=null)
			persistInt(mCurrentValue);
		//setSummary(mCurrentValue);
	}
	
	@Override
	public void setSummary(int value) {
		String minute="";
		if (value==1)
			minute=getContext().getString(R.string.settings_minute);
		else
			minute=getContext().getString(R.string.settings_minutes);
		setSummary(String.valueOf(value)+" " + minute);
	}
	
	
	public Integer getValue(){
		return mCurrentValue;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		// Check whether this Preference is persistent (continually saved)
		if (isPersistent()) {
			// No need to save instance state since it's persistent, use
			// superclass state
			return superState;
		}

		// Create instance of custom BaseSavedState
		final SavedState myState = new SavedState(superState);
		// Set the state's value with the class member that holds current
		// setting value
		myState.value = mCurrentValue;
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// Check whether we saved the state in onSaveInstanceState
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save the state, so call superclass
			super.onRestoreInstanceState(state);
			return;
		}

		// Cast state to custom BaseSavedState and pass to superclass
		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());

		// Set this Preference's widget to reflect the restored state
		mNumberPicker.setValue(myState.value);
	}

	private static class SavedState extends BaseSavedState {
		// Member that holds the setting's value
		// Change this data type to match the type saved by your Preference
		int value;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
			// Get the current preference's value
			value = source.readInt(); // Change this to read the appropriate
										// data type
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			// Write the preference's value
			dest.writeInt(value); // Change this to write the appropriate data
									// type
		}

		// Standard creator object using an instance of this class
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}