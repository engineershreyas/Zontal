package com.shreyas.zontal.zontal;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shreyas.zontal.zontal.utils.ChatListAdaper;

import java.util.List;


public class ChatListActivity extends ActionBarActivity {


    private ListView listView;
    private ChatListAdaper chatListAdaper;
    private ParseUser currentUser;
    private ButtonFloat floatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        listView = (ListView)findViewById(R.id.chat_headers_list);
        floatBtn = (ButtonFloat)findViewById(R.id.buttonFloat);

        currentUser = ParseUser.getCurrentUser();

        String userID = currentUser.getObjectId();

        ParseQuery<ParseObject> parseQuery  = ParseQuery.getQuery("Chat");
        parseQuery.whereContains("chatID",userID);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                chatListAdaper = new ChatListAdaper(ChatListActivity.this,objects);

                listView.setAdapter(chatListAdaper);

            }
        });

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
