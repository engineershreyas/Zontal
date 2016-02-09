package com.shreyas.zontal.zontal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shreyas.zontal.zontal.utils.ChatFactory;

public class NewChatActivity extends AppCompatActivity {

    private EditText eUsername, eText;
    private ButtonRectangle rectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        eUsername = (EditText)findViewById(R.id.enter_username_field);
        eText     = (EditText)findViewById(R.id.enter_text_field);

        rectBtn   = (ButtonRectangle)findViewById(R.id.send_msg_btn);

        rectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msgUsername = eUsername.getText().toString();
                final String msgText     = eText.getText().toString();

                final ParseUser myUser = ParseUser.getCurrentUser();

                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();

                userQuery.whereEqualTo("username",msgUsername);

                userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(final ParseUser object, ParseException e) {

                        if(e == null){
                            if(object != null){

                                ParseQuery<ParseObject> chatQuery = ChatFactory.findChat(myUser,object);

                                chatQuery.countInBackground(new CountCallback() {
                                    @Override
                                    public void done(int count, ParseException exc) {

                                        if(exc == null) {

                                            if (count > 0) {


                                                ParseObject message =  ChatFactory.createMessage(msgText,myUser,object);
                                                message.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException ex) {

                                                        if(ex == null){

                                                            finish();
                                                            Intent i = new Intent(NewChatActivity.this,ChatListActivity.class);
                                                            startActivity(i);

                                                        }
                                                        else{
                                                            Toast.makeText(NewChatActivity.this,"Error: " + ex.getMessage(),Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                });

                                            } else {


                                                ParseObject chat = ChatFactory.createChat(myUser,object);

                                                chat.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException ex) {

                                                        if(ex == null){

                                                            ParseObject message = ChatFactory.createMessage(msgText,myUser,object);

                                                            message.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException ah) {

                                                                    if(ah != null) {

                                                                        Toast.makeText(NewChatActivity.this, "Error: " + ah.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{

                                                                        finish();
                                                                        Intent i = new Intent(NewChatActivity.this,ChatListActivity.class);
                                                                        startActivity(i);

                                                                    }


                                                                }
                                                            });

                                                        }else{
                                                            Toast.makeText(NewChatActivity.this,"Error: " + ex.getMessage(),Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                });


                                            }

                                        }
                                        else{

                                            Toast.makeText(NewChatActivity.this,"Error: " + exc.getMessage(),Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                });

                            }
                            else{

                                Toast.makeText(NewChatActivity.this,"Oops, this user doesn't exist!",Toast.LENGTH_LONG).show();

                            }
                        }
                        else{

                            Toast.makeText(NewChatActivity.this,"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });




    }
}
