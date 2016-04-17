package com.shenkar.ysapir.tasker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import tasker.R;

import com.shenkar.ysapir.tasker.localeDB.DBHandlerImpl;
import com.shenkar.ysapir.tasker.localeDB.contracts.DBHandler;
import com.shenkar.ysapir.tasker.models.Task;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateEditTaskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<String> categoryOptions;
    private EditText taskNameEditText;
    private EditText locationEditText;
    Spinner categorySpinner;
    EditText employeeEditText;
    String priority = "Normal";
    DBHandler dbHandler;
    RadioGroup radioGroup;
    RadioButton radioButtonLow;
    RadioButton radioButtonNormal;
    RadioButton radioButtonUrgent;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        taskNameEditText = (EditText)findViewById(R.id.taskName);
        locationEditText = (EditText)findViewById(R.id.location);
        categoryOptions = new ArrayList<>(Arrays.asList("Computers", "Home", "Me"));
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        employeeEditText = (EditText) findViewById(R.id.employeeEditText);
        ArrayAdapter categoryAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, categoryOptions);
        categorySpinner.setAdapter(categoryAdapter);

        if(Constants.chosenTask != null){
            taskNameEditText.setText(Constants.chosenTask.getName());
            locationEditText.setText(Constants.chosenTask.getLocation());
            categorySpinner.setSelection(categoryAdapter.getPosition(Constants.chosenTask.getCategory()));
            employeeEditText.setText(Constants.chosenTask.getEmployee());
            Constants.date = Constants.chosenTask.getDate();
            Constants.time = Constants.chosenTask.getTime();


            radioGroup = (RadioGroup) findViewById(R.id.radio_group);
            radioButtonLow = (RadioButton) findViewById(R.id.radio_low);
            radioButtonNormal = (RadioButton) findViewById(R.id.radio_normal);
            radioButtonUrgent = (RadioButton) findViewById(R.id.radio_urgent);
            if(Constants.chosenTask.getPriority().equals("Low"))
                radioGroup.check(radioButtonLow.getId());
            else if(Constants.chosenTask.getPriority().equals("Normal"))
                radioGroup.check(radioButtonNormal.getId());
            else
                radioGroup.check(radioButtonUrgent.getId());
        }
        else{
            taskNameEditText.setText("");
            locationEditText.setText("");
            categorySpinner.setSelection(0);
            employeeEditText.setSelection(0);
        }
        dbHandler = DBHandlerImpl.getInstance(getApplicationContext());
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //========Scanner part====================================//
    public void openScanner(View view)//זה הפונקציה שהכפתור קורא לה
    {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            locationEditText.setText(scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //======================================================//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.interval) {
            Intent intent = new Intent(this, AutoUpdateActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isLogedIn", false);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Thanks for using our app, see you next time!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.inviteTeam) {
            Intent intent = new Intent(this, NewTeamActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void saveTask(View view){
        boolean isNewTask = false;
        ParseObject parseObject = null;
        if(Constants.chosenTask == null) {
            Constants.chosenTask = new Task();
            Constants.chosenTask.setStatus("WAITING");
            parseObject = new ParseObject("Task");
            parseObject.put("status", "WAITING");
            isNewTask = true;
        }
        else{
            parseObject = Constants.allParseObjectTasks.get(getTaskIndex(Constants.chosenTask));
        }
        parseObject.put("name", taskNameEditText.getText().toString());
        Constants.chosenTask.setName(taskNameEditText.getText().toString());
        parseObject.put("location", locationEditText.getText().toString());
        Constants.chosenTask.setLocation(locationEditText.getText().toString());
        parseObject.put("category", categorySpinner.getSelectedItem().toString());
        Constants.chosenTask.setCategory(categorySpinner.getSelectedItem().toString());
        parseObject.put("priority", priority);
        Constants.chosenTask.setPriority(priority);
        parseObject.put("employee", employeeEditText.getText().toString());
        Constants.chosenTask.setEmployee(employeeEditText.getText().toString());
        parseObject.put("team", Constants.teamName);
        Constants.chosenTask.setTeam(Constants.teamName);
        int taskId = 0;
        if(isNewTask){
            taskId = dbHandler.addTask(Constants.chosenTask);
            Constants.chosenTask.setId(taskId);
            parseObject.put("id",taskId);
        }
        else{
            taskId = Constants.chosenTask.getId();
            dbHandler.updateTask(Constants.chosenTask);
        }
        parseObject.saveEventually();
        Toast.makeText(getApplicationContext(), "Task # " + taskId + " was saved successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, TabbedTasksActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_normal:
                if (checked)
                    priority = "Normal";
                    break;
            case R.id.radio_urgent:
                if (checked)
                    priority = "Urgent";
                    break;
            case R.id.radio_low:
                if (checked)
                    priority = "Low";
                    break;
        }
    }

    public int getTaskIndex(Task chosenTask){
        int i = 0;
        for(ParseObject parseObject : Constants.allParseObjectTasks){
            if(Integer.parseInt(parseObject.get("id").toString()) == chosenTask.getId())
                return i;
            i++;
        }
        return -1;
    }
}
