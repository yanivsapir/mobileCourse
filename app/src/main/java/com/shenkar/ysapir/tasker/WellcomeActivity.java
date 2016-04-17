package com.shenkar.ysapir.tasker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import tasker.R;

public class WellcomeActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        GoogleAnalyticsApplication application =  (GoogleAnalyticsApplication)getApplication();
        mTracker = application.getDefaultTracker();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                Constants.allParseObjectTasks = new ArrayList<>();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> tasksObjects, ParseException e) {
                        if (e == null) {
                            for (ParseObject parseObject : tasksObjects) {
                                Constants.allParseObjectTasks.add(parseObject);
                            }
                        }
                    }
                });

                if(!sharedpreferences.getString("username","").equals("") &&
                        sharedpreferences.getBoolean("isLogedIn",false)){
                    Constants.username = sharedpreferences.getString("username", "");
                    Constants.teamName = sharedpreferences.getString("teamName","");
                    Constants.isManager = sharedpreferences.getBoolean("isManager", false);
//                    Toast.makeText(WellcomeActivity.this, "Welcome back to TaskManager", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(WellcomeActivity.this, TabbedTasksActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(WellcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
