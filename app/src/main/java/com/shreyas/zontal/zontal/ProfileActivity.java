package com.shreyas.zontal.zontal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class ProfileActivity extends ActionBarActivity {


    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private CircularImageView imageView;
    private TextView username;

    private ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = (CircularImageView)findViewById(R.id.profile_user_image);

        username = (TextView)findViewById(R.id.profile_username);

        currentUser = ParseUser.getCurrentUser();

        username.setText(currentUser.getUsername());

        ParseFile image = currentUser.getParseFile("image");

        if(image != null){
            Picasso.with(ProfileActivity.this).load(image.getUrl()).into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


            }
        });

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK){



                Bitmap bmp = (Bitmap)data.getExtras().get("data");


                int size = bmp.getRowBytes() * bmp.getHeight();

                ByteBuffer byteBuffer = ByteBuffer.allocate(size);

                bmp.copyPixelsToBuffer(byteBuffer);

                byte[] byteArray = byteBuffer.array();

                final ParseFile file = new ParseFile(byteArray);

                final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setTitle("Please wait...");

                progressDialog.show();

                file.saveInBackground(new SaveCallback() {
                                          @Override
                                          public void done(ParseException e) {

                                              progressDialog.dismiss();

                                              if(e == null){

                                                  currentUser.put("image",file);

                                                  currentUser.saveInBackground(new SaveCallback() {
                                                      @Override
                                                      public void done(ParseException e) {
                                                          if(e == null){

                                                              Picasso.with(ProfileActivity.this).load(file.getUrl()).into(imageView);

                                                          }else{

                                                              Zontal.showToast(e.getMessage());
                                                          }
                                                      }
                                                  });

                                              }
                                              else{
                                                  Zontal.showToast(e.getMessage());
                                              }

                                          }
                                      },
                        new ProgressCallback() {
                            @Override
                            public void done(Integer percentDone) {

                                progressDialog.setProgress(percentDone.intValue());


                            }
                        });



            }
            else{
                Zontal.showToast("Error taking picture!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
