package com.shenkar.ysapir.tasker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import tasker.R;

import com.shenkar.ysapir.tasker.localeDB.DBHandlerImpl;
import com.shenkar.ysapir.tasker.localeDB.contracts.DBHandler;
import com.shenkar.ysapir.tasker.models.Task;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportTaskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner taskStatusSpinner;
    private DBHandler dbHandler;
    private boolean acceptStatus = true;
    private Switch acceptSwitch;
    private TextView categoryTextView;
    private TextView priorityTextView;
    private TextView locationTextView;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHandler = DBHandlerImpl.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        acceptSwitch = (Switch) findViewById(R.id.isAccepted);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        taskStatusSpinner = (Spinner) findViewById(R.id.taskStatusSpinner);
        List<String> taskStatusOptions = new ArrayList<>(Arrays.asList("WAITING", "IN PROGRESS", "DONE"));
        ArrayAdapter taskStatusAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, taskStatusOptions);
        taskStatusSpinner.setAdapter(taskStatusAdapter);
        taskStatusSpinner.setSelection(taskStatusAdapter.getPosition(Constants.chosenTask.getStatus()));
        acceptSwitch.setChecked(Constants.chosenTask.isAccepted());
        acceptSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acceptStatus = isChecked;
            }
        });

        Constants.date = Constants.chosenTask.getDate() == null ? "00-00-0000" : Constants.chosenTask.getDate();
        Constants.time = Constants.chosenTask.getTime() == null ? "00:00" : Constants.chosenTask.getTime();

        categoryTextView = (TextView) findViewById(R.id.taskCategory);
        categoryTextView.setText(Constants.chosenTask.getCategory());
        priorityTextView = (TextView) findViewById(R.id.taskPriority);
        priorityTextView.setText(Constants.chosenTask.getPriority());
        locationTextView = (TextView) findViewById(R.id.taskLocation);
        locationTextView.setText(Constants.chosenTask.getLocation());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_task, menu);
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

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void saveTask(View view){

        ParseObject parseObject = Constants.allParseObjectTasks.get(getTaskIndex(Constants.chosenTask));
        parseObject.put("isAccepted", acceptStatus ? 1 : 0);
        Constants.chosenTask.setIsAccepted(acceptStatus);
        parseObject.put("status", taskStatusSpinner.getSelectedItem().toString());
        Constants.chosenTask.setStatus(taskStatusSpinner.getSelectedItem().toString());
        parseObject.put("date", Constants.date);
        Constants.chosenTask.setDate(Constants.date);
        parseObject.put("time", Constants.time);
        Constants.chosenTask.setTime(Constants.time);
        int taskId = Constants.chosenTask.getId();
        dbHandler.updateTask(Constants.chosenTask);
        parseObject.saveEventually();
        Toast.makeText(getApplicationContext(), "Task # " + taskId + " was saved successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, TabbedTasksActivity.class);
        startActivity(intent);
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
