package com.example.ysapir.tasker.localeDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ysapir.tasker.localeDB.contracts.DBHandler;
import com.example.ysapir.tasker.models.Employee;
import com.example.ysapir.tasker.models.Task;

import java.util.ArrayList;

/**
 * Created by ysapir on 3/25/2016.
 */
public class DBHandlerImpl implements DBHandler {
    private static DBHandlerImpl taskDAO;
    private LocaleDBInitializer DBHelper;

    private DBHandlerImpl(Context context) {
        DBHelper = new LocaleDBInitializer(context);
    }

    public static DBHandlerImpl getInstance(Context context) {
        if (taskDAO == null) {
            taskDAO = new DBHandlerImpl(context);
        }
        return taskDAO;
    }


    @Override
    public int addTask(Task task) {
        SQLiteDatabase db = null;
        int max = 0;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues taskValues = new ContentValues();
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_NAME, task.getName());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_DATE, task.getDate());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_TIME, task.getTime());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_ASSIGNEE, task.getEmployee());
            db.insert(TaskColumnNamesContainer.TaskEntry.TABLE_NAME, null, taskValues);
        } finally {
            if (db != null) {
                String query = "SELECT MAX(id) FROM " + TaskColumnNamesContainer.TaskEntry.TABLE_NAME;
                db = DBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    max = cursor.getInt(0);
                }
                db.close();
            }
        }
        return max;
    }

    @Override
    public void updateTask(Task task) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues taskValues = new ContentValues();
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_NAME, task.getName());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_DATE, task.getDate());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_TIME, task.getTime());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_CATEGORY, task.getCategory());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_PRIORITY, task.getPriority());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_LOCATION, task.getLocation());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_STATUS, task.getStatus());
            taskValues.put(TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_ASSIGNEE, task.getEmployee());
            db.update(TaskColumnNamesContainer.TaskEntry.TABLE_NAME,
                    taskValues, TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_ID + " = ?",
                    new String[]{String.valueOf(task.getId())});

        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Task getTask(int id) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.query(TaskColumnNamesContainer.TaskEntry.TABLE_NAME,
                    new String[]{TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_ID,
                            TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_NAME,
                            TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_TIME,
                            TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_DATE,
                            TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_CATEGORY,
                            TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_PRIORITY,
                            TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_LOCATION,
                            TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_STATUS},
                    TaskColumnNamesContainer.TaskEntry.COLUMN_TASK_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            Task task = new Task();
            if (cursor != null) {
                task.setId(cursor.getInt(0));
                task.setName(cursor.getString(1));
                task.setDate(cursor.getString(2));
                task.setTime(cursor.getString(3));
                task.setCategory(cursor.getString(4));
                task.setPriority(cursor.getString(5));
                task.setLocation(cursor.getString(6));
                task.setStatus(cursor.getString(7));
                task.setEmployee(cursor.getString(8));
            }
            if (cursor != null) {
                cursor.close();
            }
            return task;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        SQLiteDatabase db = null;
        ArrayList<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM " + TaskColumnNamesContainer.TaskEntry.TABLE_NAME;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(cursor.getInt(0));
                    task.setName(cursor.getString(1));
                    task.setDate(cursor.getString(2));
                    task.setTime(cursor.getString(3));
                    task.setCategory(cursor.getString(4));
                    task.setPriority(cursor.getString(5));
                    task.setStatus(cursor.getString(6));
                    task.setEmployee(cursor.getString(7));
                    task.setLocation(cursor.getString(8));
                    tasks.add(task);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return tasks;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addMember(Employee employee) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            ContentValues teamValues = new ContentValues();
            teamValues.put(TaskColumnNamesContainer.MemberEntry.COLUMN_EMPLOYEE_USERNAME, employee.getUsername());
            db.insert(TaskColumnNamesContainer.MemberEntry.TABLE_NAME, null, teamValues);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void removeMember(Employee employee) {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getReadableDatabase();
            db.delete(TaskColumnNamesContainer.MemberEntry.TABLE_NAME,
                    TaskColumnNamesContainer.MemberEntry.COLUMN_EMPLOYEE_USERNAME + " = ?",
                    new String[]{employee.getUsername()});
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public ArrayList<Employee> getMembers() {
        SQLiteDatabase db = null;
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM " + TaskColumnNamesContainer.MemberEntry.TABLE_NAME;
        try {
            db = DBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Employee employee = new Employee();
                    employee.setUsername(cursor.getString(0));
                    employees.add(employee);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return employees;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}