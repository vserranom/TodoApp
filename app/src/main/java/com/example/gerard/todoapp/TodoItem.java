package com.example.gerard.todoapp;


import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gerard on 23/01/2017.
 */

public class TodoItem {
    public String userId;
    public String username;
    public String title;
    public String deadline;
    public String imageUri;

    public TodoItem(){

    }

    public TodoItem(String userId, String username, String title, String deadline, String imageUri){
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.deadline = deadline;
        this.imageUri = imageUri;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("username", username);
        result.put("title", title);
        result.put("deadline", deadline);
        result.put("imageUri", imageUri);

        return result;
    }
}

