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
package it.gmariotti.android.example.actionbarcompat.navigationdrawer;

/**
 * 
 * Model per item menu
 * 
 * @author gabriele
 *
 */
public class NsMenuItemModel {

	public int title;
	public int iconRes;
	public int counter;
	public boolean isHeader;

	public NsMenuItemModel(int title, int iconRes,boolean header,int counter) {
		this.title = title;
		this.iconRes = iconRes;
		this.isHeader=header;
		this.counter=counter;
	}
	
	public NsMenuItemModel(int title, int iconRes,boolean header){
		this(title,iconRes,header,0);
	}
	
	public NsMenuItemModel(int title, int iconRes) {
		this(title,iconRes,false);
	}
	
	
	
}
