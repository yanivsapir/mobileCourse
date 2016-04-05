package com.example.ysapir.tasker.localeDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ysapir on 3/25/2016.
 */
public class LocaleDBInitializer  extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "TaskerDB";
    // Table names
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_TASKS = "tasks";
    //Employee Table Column names
    private static final String KEY_USERNAME = "username";
    // Tasks Table Columns names
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ASSIGNEE = "assignee";

    // Tables create statement
    private static final String CREATE_EMPLOYEES_TABLE = "CREATE TABLE "
            + TABLE_EMPLOYEES + "(" + KEY_USERNAME + " TEXT PRIMARY KEY);";
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + KEY_TASK_ID + " INTEGER PRIMARY KEY," + KEY_TASK_NAME + " TEXT," + KEY_TIME
            + " TEXT,"+ KEY_DATE + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_PRIORITY
            + " TEXT," + KEY_STATUS + " TEXT," + KEY_ASSIGNEE + " TEXT," + KEY_LOCATION + " TEXT);";
    public LocaleDBInitializer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
}
