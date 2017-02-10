package com.example.gerard.todoapp;

import android.net.Uri;

import java.util.Date;

/**
 * Created by gerard on 23/01/2017.
 */

public class TodoItem {
    String title;
    String deadline;

    public TodoItem(String title, String deadline){
        this.title = title;
        this.deadline = deadline;
    }

    public String getTitle(){
        return title;
    }
    public String getDeadline() {
        return deadline;
    }
}

