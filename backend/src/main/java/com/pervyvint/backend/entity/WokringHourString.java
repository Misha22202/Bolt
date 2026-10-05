package com.pervyvint.backend.entity;

public class WokringHourString {

    private String day;

    private String time;

    public WokringHourString() {
    }

    public WokringHourString(String day, String time) {
        this.day = day;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
