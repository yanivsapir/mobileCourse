package com.shenkar.ysapir.tasker.localeDB;

import android.provider.BaseColumns;

/**
 * Created by ysapir on 3/25/2016.
 */
public class TaskColumnNamesContainer {

    public static final class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "employees";
        public static final String COLUMN_EMPLOYEE_USERNAME = "username";
    }
    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_ID = "id";
        public static final String COLUMN_TASK_NAME = "name";
        public static final String COLUMN_TASK_TIME = "time";
        public static final String COLUMN_TASK_DATE = "date";
        public static final String COLUMN_TASK_CATEGORY = "category";
        public static final String COLUMN_TASK_PRIORITY = "priority";
        public static final String COLUMN_TASK_LOCATION = "location";
        public static final String COLUMN_TASK_STATUS = "status";
        public static final String COLUMN_TASK_ASSIGNEE = "assignee";
        public static final String COLUMN_TASK_TEAMNAME = "teamname";
        public static final String COLUMN_TASK_ISACCEPTED = "isaccepted";
    }
}
