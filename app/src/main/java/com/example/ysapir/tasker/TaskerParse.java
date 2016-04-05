package com.example.ysapir.tasker;

import android.app.Application;

import com.parse.Parse;


/**
 * Created by ysapir on 3/7/2016.
 */
public class TaskerParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }
}
