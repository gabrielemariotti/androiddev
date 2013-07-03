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

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;

public class MainActivity extends ListActivity {

    private EditText mNewMessage;
    private ImageButton mNewMessageSend;
    private ViewHolderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = ListHelper.buildViewHolderAdapter(this,
                R.layout.list_item);
        setListAdapter(mAdapter);



        mNewMessage = (EditText) findViewById(R.id.newmsg);
        mNewMessageSend = (ImageButton) findViewById(R.id.newmsgsend);
        if (mNewMessageSend!=null){
            mNewMessageSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItem();
                }
            });
        }
    }



    private void addItem() {


        MyObj obj = new MyObj("Gabriele",mNewMessage.getText().toString());
        mAdapter.add(obj);

        mAdapter.notifyDataSetChanged();

    }


}
