package it.gmariotti.android.examples.snippets;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class PluralFormsActivity extends SherlockActivity {

	private static final String TAG = "RetrieveManifestInfoActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plural_forms);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		retrivePlurals();
	}

	private void retrivePlurals() {

		TextView tw = (TextView) findViewById(R.id.text_xml);
		if (tw != null) {
			Resources res = getResources();
			String books0 = res.getQuantityString(R.plurals.books, 0,0);
			String books1 = res.getQuantityString(R.plurals.books, 1);
			String books2 = res.getQuantityString(R.plurals.books, 2);
			String booksN = res.getQuantityString(R.plurals.books, 10, 10);

			StringBuffer sb = new StringBuffer();
			sb.append(books0);
			sb.append("\n");
			sb.append(books1);
			sb.append("\n");
			sb.append(books2);
			sb.append("\n");
			sb.append(booksN);
			sb.append("\n");

			tw.setText(sb.toString());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
