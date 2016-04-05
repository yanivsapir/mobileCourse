package com.example.ysapir.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ysapir.tasker.localeDB.DBHandlerImpl;
import com.example.ysapir.tasker.localeDB.contracts.DBHandler;
import com.example.ysapir.tasker.models.Task;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateEditTaskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<String> categoryOptions;
    private List<String> employeeOptions;
    private EditText taskNameEditText;
    private EditText locationEditText;
    Spinner categorySpinner;
    Spinner employeeSpinner;
    String priority = "Normal";
    DBHandler dbHandler;

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
        employeeOptions = new ArrayList<>(Arrays.asList("E1", "E2", "E3"));
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        employeeSpinner = (Spinner) findViewById(R.id.employeeSpinner);
        ArrayAdapter categoryAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, categoryOptions);
        ArrayAdapter employeeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, employeeOptions);
        categorySpinner.setAdapter(categoryAdapter);
        employeeSpinner.setAdapter(employeeAdapter);

        if(Constants.chosenTask != null){
            taskNameEditText.setText(Constants.chosenTask.getName());
            locationEditText.setText(Constants.chosenTask.getLocation());
            categorySpinner.setSelection(categoryAdapter.getPosition(Constants.chosenTask.getCategory()));
            employeeSpinner.setSelection(employeeAdapter.getPosition(Constants.chosenTask.getEmployee()));
            Constants.date = Constants.chosenTask.getDate();
            Constants.time = Constants.chosenTask.getTime();
        }
        else{
            taskNameEditText.setText("");
            locationEditText.setText("");
            categorySpinner.setSelection(0);
            employeeSpinner.setSelection(0);
        }
        dbHandler = DBHandlerImpl.getInstance(getApplicationContext());
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
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
        parseObject.put("employee", employeeSpinner.getSelectedItem().toString());
        Constants.chosenTask.setEmployee(employeeSpinner.getSelectedItem().toString());
        parseObject.put("date", Constants.date);
        Constants.chosenTask.setDate(Constants.date);
        parseObject.put("time", Constants.time);
        Constants.chosenTask.setTime(Constants.time);
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

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}
