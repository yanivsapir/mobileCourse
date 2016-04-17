package com.shenkar.ysapir.tasker.models;

/**
 * Created by ysapir on 3/25/2016.
 */
public class Employee {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "username='" + username + '\'' +
                '}';
    }
}
