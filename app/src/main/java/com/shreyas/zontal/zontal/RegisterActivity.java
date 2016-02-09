package com.shreyas.zontal.zontal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends ActionBarActivity {

    private final String LOG_TAG = "RegisterActivity";

    private EditText username, password, confirmPassword, email, phoneNumber;
    private Button finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.register_username);
        password = (EditText)findViewById(R.id.register_password);
        confirmPassword = (EditText)findViewById(R.id.register_confirm_password);
        email = (EditText)findViewById(R.id.register_email);
        phoneNumber = (EditText)findViewById(R.id.register_number);

        finish = (Button)findViewById(R.id.register_done_btn);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(LOG_TAG,"finish btn clicked");

                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();
                String emailText = email.getText().toString();
                String phoneText = phoneNumber.getText().toString();


                if (!passwordText.equals(confirmPasswordText)) {

                    Zontal.showToast("Oops, the two passwords don't match!");

                }

                ParseUser newUser = new ParseUser();

                newUser.setUsername(usernameText);
                newUser.setPassword(passwordText);
                newUser.setEmail(emailText);
                newUser.put("phone", phoneText);


                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        Log.d(LOG_TAG,"signing up user");

                        if(e == null){

                            Log.d(LOG_TAG,"sign up success");

                            Intent i = new Intent(RegisterActivity.this,ProfileActivity.class);
                            startActivity(i);

                        }
                        else{

                            Log.e(LOG_TAG,"sign up fail : " + e.getMessage());

                            Zontal.showToast(e.getMessage());

                        }
                    }
                });



            }
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
