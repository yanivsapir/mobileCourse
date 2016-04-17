package com.shenkar.ysapir.tasker.models;



/**
 * Created by ysapir on 3/20/2016.
 */
public class Task {

    private int id;
    private String name;
    private String category;
    private String status;
    private String priority;
    private String employee;
    private String location;
    private String team;
    private String date;
    private String time;
    private boolean isAccepted;

    public Task() {}

    public Task(int id, String name, String category, String status, String priority,
                String employee, String location,String date, String time, String team, boolean isAccepted) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
        this.priority = priority;
        this.employee = employee;
        this.location = location;
        this.date = date;
        this.time = time;
        this.team = team;
        this.isAccepted = isAccepted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (isAccepted != task.isAccepted) return false;
        if (!name.equals(task.name)) return false;
        if (!category.equals(task.category)) return false;
        if (!status.equals(task.status)) return false;
        if (!priority.equals(task.priority)) return false;
        if (!employee.equals(task.employee)) return false;
        if (!location.equals(task.location)) return false;
        if (!team.equals(task.team)) return false;
        if (!date.equals(task.date)) return false;
        return time.equals(task.time);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + priority.hashCode();
        result = 31 * result + employee.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + team.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + (isAccepted ? 1 : 0);
        return result;
    }

    public String fullToString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", employee='" + employee + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", team=" + team +
                ", isAccepted=" + isAccepted +
                '}';
    }

    @Override
    public String toString() {
        return name;
    }
}
