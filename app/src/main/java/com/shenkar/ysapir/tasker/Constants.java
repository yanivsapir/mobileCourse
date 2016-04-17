package com.shenkar.ysapir.tasker;

import com.shenkar.ysapir.tasker.models.Task;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by ysapir on 3/19/2016.
 */
public class Constants {

    public static String username;
    public static Task chosenTask;
    public static List<Task> allTasks;
    public static List<ParseObject> allParseObjectTasks;
    public static String date;
    public static String time;
    public static boolean isManager;
    public static String teamName;
    public static int timeToUpdate = 300000;
}
