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
package it.gmariotti.android.examples.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;


public class EvernoteListLayout extends LinearLayout implements
		View.OnClickListener {

	private Adapter list;
	private View.OnClickListener mListener;

	public EvernoteListLayout(Context context) {
		super(context);
	}

	public EvernoteListLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public EvernoteListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onClick(View v) {
		if (mListener!=null)
			mListener.onClick(v);
	}

	public void setList(Adapter list) {
		this.list = list;
		
		//Popolute list
		if (this.list!=null){
			for (int i=0;i<this.list.getCount();i++){
				View item= list.getView(i, null,null);
				this.addView(item);
			}
		}
		
	}

	public void setmListener(View.OnClickListener mListener) {
		this.mListener = mListener;
	}
	
	

}
