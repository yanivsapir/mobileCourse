package com.shenkar.ysapir.tasker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import tasker.R;

import com.shenkar.ysapir.tasker.localeDB.DBHandlerImpl;
import com.shenkar.ysapir.tasker.localeDB.contracts.DBHandler;
import com.shenkar.ysapir.tasker.models.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TabbedTasksActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView allTasksListView;
    private List<Task> allTasksValues;
    private ListView waitingTasksListView;
    private List<Task> waitingTasksValues;
    private List<Task> tasks;
    private Set<Integer> taskIds;
    private FloatingActionButton addFAB;
    List<ParseObject> parseObjects;
    private int countNewTasks;
    DBHandler dbHandler;
    boolean firstUpdate = true;
    TabHost.TabSpec spec1;
    TabHost tabHost;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        parseObjects = new ArrayList<>();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        allTasksListView = (ListView) findViewById(R.id.allTasks);
        waitingTasksListView = (ListView) findViewById(R.id.notDoneTasks);
        allTasksValues = new ArrayList<>();
        waitingTasksValues = new ArrayList<>();
        Constants.allTasks = new ArrayList<>();
        taskIds = new HashSet<>();
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        spec1 = tabHost.newTabSpec("tab1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("All Tasks", null);
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("tab2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Waiting Tasks", null);
        tabHost.addTab(spec2);
        dbHandler = DBHandlerImpl.getInstance(getApplicationContext());
        tasks = dbHandler.getTasks();
        updateTasksLists();

        if(Constants.isManager == false){
            addFAB = (FloatingActionButton)findViewById(R.id.fab2);
            addFAB.hide();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
                while (true) {
                    try {
                        Thread.sleep(Constants.timeToUpdate);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> tasksObjects, ParseException e) {
                            if (e == null) {
                                Constants.allParseObjectTasks.clear();
                                for (ParseObject parseObject : tasksObjects) {
                                    Constants.allParseObjectTasks.add(parseObject);
                                }
                                updateTasksListsView();
                                Toast.makeText(TabbedTasksActivity.this, "tasks updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
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

    public void updateTasksLists() {
        if(firstUpdate){
            updateTasksListsView();
            firstUpdate = false;
        }else{
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> tasksObjects, ParseException e) {
                    if (e == null) {
                        Constants.allParseObjectTasks.clear();
                        for (ParseObject parseObject : tasksObjects) {
                            Constants.allParseObjectTasks.add(parseObject);
                        }
                        updateTasksListsView();
                    }
                }
            });
        }
    }

    public void updateTasksListsView() {

        countNewTasks = 0;
        allTasksValues.clear();
        waitingTasksValues.clear();
        Constants.allTasks.clear();
        parseObjects.clear();
        taskIds.clear();
        for (Task task : tasks) {
            if ((Constants.isManager && task.getTeam().equals(Constants.teamName)) || (!Constants.isManager && task.getEmployee().equals(Constants.username))) {
                if (!task.getStatus().equals("DONE"))
                    waitingTasksValues.add(task);
                allTasksValues.add(task);
                Constants.allTasks.add(task);
                taskIds.add(task.getId());
            }
        }
        Task task = new Task();
        task.setName("No current tasks");
        if (allTasksValues.isEmpty())
            allTasksValues.add(task);
        if (waitingTasksValues.isEmpty())
            waitingTasksValues.add(task);
        setListViewValues(1, allTasksValues);
        setListViewValues(2, waitingTasksValues);
        if (!Constants.allParseObjectTasks.isEmpty()) {
            parseObjects.addAll(Constants.allParseObjectTasks);
            for (ParseObject parseObject : parseObjects) {
                int id = Integer.parseInt(parseObject.get("id").toString());
                if (!taskIds.contains(id) && ((Constants.isManager && parseObject.get("team")
                        .toString().equals(Constants.teamName)) || (!Constants.isManager &&
                        parseObject.get("employee").toString().equals(Constants.username)))) {
                    Task newTask = new Task();
                    newTask.setId(Integer.parseInt(parseObject.get("id").toString()));
                    newTask.setName(parseObject.get("name").toString());
                    newTask.setStatus(parseObject.get("status").toString());
                    newTask.setTeam(parseObject.get("team").toString());
                    newTask.setIsAccepted(Boolean.parseBoolean(parseObject.get("isAccepted") == null ? "false" : parseObject.get("isAccepted").toString()));
                    newTask.setCategory(parseObject.get("category").toString());
                    newTask.setLocation(parseObject.get("location").toString());
                    newTask.setPriority(parseObject.get("priority").toString());
                    newTask.setTime(parseObject.get("time") == null ? "00:00" : parseObject.get("time").toString());
                    newTask.setDate(parseObject.get("date") == null ? "00-00-0000" : parseObject.get("date").toString());
                    newTask.setEmployee(parseObject.get("employee").toString());
                    tasks.add(newTask);
                    dbHandler.addTask(newTask);
                    if (!newTask.getStatus().equals("DONE"))
                        waitingTasksValues.add(newTask);
                    allTasksValues.add(newTask);
                    setListViewValues(1, allTasksValues);
                    setListViewValues(2, waitingTasksValues);
                    countNewTasks++;
                }
            }
        }
        String title = countNewTasks == 0 ? "All Tasks" : "All Tasks [" + countNewTasks + "]";
        ((TextView)tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(title);
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
                Intent intent;
                if(Constants.isManager)
                    intent = new Intent(TabbedTasksActivity.this, CreateEditTaskActivity.class);
                else
                    intent = new Intent(TabbedTasksActivity.this, ReportTaskActivity.class);
                startActivity(intent);
            }
        });
    }
}