package com.example.doggydine;

public class ScheduleItem {
    private String title;
    private String location;
    private String time;
    private String memo;

    public ScheduleItem() {
    }

    public ScheduleItem(String title, String location, String time, String memo) {
        this.title = title;
        this.location = location;
        this.time = time;
        this.memo = memo;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getMemo() {
        return memo;
    }
}
