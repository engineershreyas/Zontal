package com.shreyas.zontal.zontal;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by shreyashirday on 1/2/16.
 */
public class Zontal extends Application {


    @Override
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }

}
