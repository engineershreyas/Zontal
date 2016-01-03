package com.shreyas.zontal.zontal;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.parse.Parse;

/**
 * Created by shreyashirday on 1/2/16.
 */
public class Zontal extends Application {

    private static Context ctx;

    @Override
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

        ctx = this;
    }

    public static void showToast(String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT);
    }

}
