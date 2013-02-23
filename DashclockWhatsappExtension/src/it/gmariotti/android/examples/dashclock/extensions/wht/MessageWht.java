package it.gmariotti.android.examples.dashclock.extensions.wht;

import java.util.ArrayList;
import java.util.List;

public class MessageWht {

	private ArrayList<String> text;

	public ArrayList<String> getText() {
		return text;
	}

	public void setText(List<CharSequence> text) {
		if (text != null) {
			this.text = new ArrayList<String>();
			for (CharSequence c1 : text) {
				this.text.add(c1.toString());
			}
		}
	}
}
