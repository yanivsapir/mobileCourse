package com.example.ysapir.tasker;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.ysapir.tasker.localeDB.DBHandlerImpl;
import com.example.ysapir.tasker.localeDB.contracts.DBHandler;
import com.example.ysapir.tasker.models.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TabbedTasksActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView allTasksListView;
    private List<Task> allTasksValues;
    private ListView waitingTasksListView;
    private List<Task> waitingTasksValues;
    private List<Task> tasks;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_tasks);
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

        allTasksListView = (ListView) findViewById(R.id.allTasks);
        waitingTasksListView = (ListView) findViewById(R.id.notDoneTasks);
        allTasksValues = new ArrayList<>();
        waitingTasksValues = new ArrayList<>();
        Constants.allTasks = new ArrayList<>();
        Constants.allParseObjectTasks = new ArrayList<>();
        updateTasksLists();
        final TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("tab1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("All Tasks", null);
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("tab2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Waiting Tasks", null);
        tabHost.addTab(spec2);
        dbHandler = DBHandlerImpl.getInstance(getApplicationContext());
        tasks = dbHandler.getTasks();
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
        getMenuInflater().inflate(R.menu.tabbed_tasks, menu);
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

    public void updateTasksLists(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tasksObjects, ParseException e) {
                if (e == null) {
                    allTasksValues.clear();
                    waitingTasksValues.clear();
                    Constants.allTasks.clear();
                    for (Task task : tasks) {
                        if (!task.getStatus().equals("DONE"))
                            waitingTasksValues.add(task);
                        allTasksValues.add(task);
                        Constants.allTasks.add(task);
                    }
                    setListViewValues(1, allTasksValues);
                    setListViewValues(2, waitingTasksValues);
                    for (ParseObject parseObject : tasksObjects) {
                        Constants.allParseObjectTasks.add(parseObject);
                    }
                } else {
                    allTasksValues.clear();
                    waitingTasksValues.clear();
                    Constants.allTasks.clear();
                    for (Task task : tasks) {
                        if (!task.getStatus().equals("DONE"))
                            waitingTasksValues.add(task);
                        allTasksValues.add(task);
                        Constants.allTasks.add(task);
                    }
                    setListViewValues(1, allTasksValues);
                    setListViewValues(2, waitingTasksValues);
                }
            }
        });
    }

    public void updtaeTaskLists(View view){
        updateTasksLists();
    }

    public void createNewTask(View view){
        Constants.chosenTask = null;
        Intent intent = new Intent(this, CreateEditTaskActivity.class);
        startActivity(intent);
    }

    public void setListViewValues(final int listViewNum, List<Task> values){
        ListView listView = listViewNum == 1 ? allTasksListView : waitingTasksListView;
        ArrayAdapter<Task> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView listView = listViewNum == 1 ? allTasksListView : waitingTasksListView;
                Constants.chosenTask = (Task) listView.getItemAtPosition(position);
                Intent intent = new Intent(TabbedTasksActivity.this, CreateEditTaskActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "Position :" + position + "  ListItem : " + Constants.chosenTask, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}