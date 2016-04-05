package com.example.ysapir.tasker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class WellcomeActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        SystemClock.sleep(3000);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(!sharedpreferences.getString("username","").equals("") &&
                sharedpreferences.getBoolean("isLogedIn",false)){
            Toast.makeText(WellcomeActivity.this, "Welcome back to TaskManager", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WellcomeActivity.this, TabbedTasksActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(WellcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
