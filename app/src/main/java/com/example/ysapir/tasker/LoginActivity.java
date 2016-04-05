package com.example.ysapir.tasker;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        usernameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);
    }

    public void loginUser(View view)
    {
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if(username.matches("") || password.matches(""))
        {
            Toast.makeText(LoginActivity.this, "Username & Password are requeired fields", Toast.LENGTH_LONG).show();
            return;
        }

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putBoolean("isLogedIn", true);
                    editor.commit();
                    Toast.makeText(LoginActivity.this, "Welcome back to TaskManager", Toast.LENGTH_LONG).show();
                    Intent mainview = new Intent(LoginActivity.this, TabbedTasksActivity.class);
                    startActivity(mainview);
                } else {
                    Toast.makeText(LoginActivity.this, "Username or Password incorrect \n please retype your credentials", Toast.LENGTH_LONG).show();
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }
                return;
            }
        });
    }

    public void signUpUser(View view){
        Intent signUpView = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signUpView);
        finish();
    }
}