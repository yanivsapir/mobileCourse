package com.shenkar.ysapir.tasker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import tasker.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private CheckBox chkbx;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        usernameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);
        emailEditText = (EditText)findViewById(R.id.email);
        phoneEditText = (EditText)findViewById(R.id.phoneNum);
        chkbx = (CheckBox) findViewById(R.id.chkbx);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }


    public void signUpUser(View view){

        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        ParseUser parseUser = new ParseUser();
        parseUser.getCurrentUser().logOut();
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.setEmail(email);
        parseUser.put("isManager", chkbx.isChecked() == true ? 1 : 0);
        parseUser.put("phone", phone);
        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putBoolean("isLogedIn", true);
                    editor.putBoolean("isManager", chkbx.isChecked());
                    Constants.username = username;
                    Constants.isManager = chkbx.isChecked();
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Successfully Signed up, please continue", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, NewTeamActivity.class);
                    startActivity(intent);

                } else {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Sign up Error", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
}
