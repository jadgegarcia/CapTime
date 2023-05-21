package com.example.captime.helperclass;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventHelperClass {
    private String email;
    private String title;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private String color;

    private String note;
    public EventHelperClass(String email, String title, int day, int month, int year, int hour, int minute, String color, String note) {
        this.email = email;
        this.title = title;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.color = color;
        this.note = note;
    }

    public EventHelperClass() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "EventHelperClass{" +
                "email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                ", color='" + color + '\'' +
                '}';
    }


    public boolean compareEvent(EventHelperClass e) {
        int cnt = 0;
        if(title.equals(e.getTitle())) {
            cnt++;
        }
        if(note.equals(e.getNote())) {
            cnt++;
        }
        if(day == e.getDay()) {
            cnt++;
        }
        if(month == e.getMonth()) {
            cnt++;
        }
        if(year == e.getYear()) {
            cnt++;
        }
        if(hour == e.getHour()) {
            cnt++;
        }
        if(minute == e.getMinute()) {
            cnt++;
        }
        if(color.equals(e.getColor())) {
            cnt++;
        }
        if(email.equals(e.getEmail())) {
            cnt++;
        }


        if(cnt == 9) {
            return true;
        }
        return false;
    }
}
