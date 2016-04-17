package com.shenkar.ysapir.tasker.localeDB.contracts;

import com.shenkar.ysapir.tasker.models.Employee;
import com.shenkar.ysapir.tasker.models.Task;

import java.util.ArrayList;

/**
 * Created by ysapir on 3/25/2016.
 */
public interface DBHandler {
    int addTask(Task task);
    void updateTask(Task task);
    Task getTask(int id);
    ArrayList<Task> getTasks();
    void addMember(Employee employee);
    void removeMember(Employee employee);
    ArrayList<Employee> getMembers();

}
