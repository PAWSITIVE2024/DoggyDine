package com.example.doggydine;

public class TodoItem {
    private String task;
    private int year, month, dayOfMonth;

    public TodoItem() {
    }

    public TodoItem(String task, int year, int month, int dayOfMonth) {
        this.task = task;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public String getTask() {
        return task;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
