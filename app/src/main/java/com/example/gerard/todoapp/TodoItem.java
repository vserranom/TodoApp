package com.example.gerard.todoapp;

import android.net.Uri;

import java.util.Date;

/**
 * Created by gerard on 23/01/2017.
 */

public class TodoItem {
    String title;
    String deadline;
    Uri imageUri;

    public TodoItem(String title, String deadline, Uri imageUri){
        this.title = title;
        this.deadline = deadline;
        this.imageUri = imageUri;
    }

    public String getTitle(){
        return title;
    }
    public String getDeadline() {
        return deadline;
    }
    public Uri getImageUri(){
        return imageUri;
    }
}

