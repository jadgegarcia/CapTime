package com.example.captime.helperclass;

public class TimeHelperClass {
    private int minute;
    private int hour;

    public TimeHelperClass(int minute, int hour) {
        this.minute = minute;
        this.hour = hour;
    }

    public TimeHelperClass() {
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "TimeHelperClass{" +
                "minute=" + minute +
                ", hour=" + hour +
                '}';
    }


}
