package com.example.gerard.todoapp;

import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends TodoListActivity implements
    TodoListActivity.DialogPositiveClickListener,
    DrawerActivity.NavClickListener {

    ChildEventListener currentListener;
    String currentReferenfce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOnDialogPositiveClickListener(this);
        setNavClickListener(this);

        setDatabaseListener("my");
    }

    public void writeNewTodo(final String title, final String deadline, final String imageUri){

        if(! title.isEmpty()) {
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


            String key = mDatabase.child("todos").push().getKey();
            TodoItem todo = new TodoItem(userId, username, title, deadline, imageUri);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/todos/" + key, todo);
            childUpdates.put("/user-todos/" + userId + "/" + key, todo);

            mDatabase.updateChildren(childUpdates);
        }
    }

    public void onDialogPositiveClick(String title, String deadline, String imageUri){
        writeNewTodo(title, deadline, imageUri);
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    public void setDatabaseListener(String which){
        if(currentListener != null) {
            FirebaseDatabase.getInstance().getReference(currentReferenfce).removeEventListener(currentListener);
        }
        myTodoRecyclerViewAdapter.getList().clear();
        myTodoRecyclerViewAdapter.notifyDataSetChanged();

        String queryRef = "todos";

        if(which.equals("my")) {
            queryRef = "user-todos/" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        currentReferenfce = queryRef;
        currentListener = FirebaseDatabase.getInstance().getReference(queryRef).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                TodoItem todo = dataSnapshot.getValue(TodoItem.class);

                myTodoRecyclerViewAdapter.getList().add(todo);
                myTodoRecyclerViewAdapter.notifyDataSetChanged();
                myTodoRecyclerViewAdapter.notifyItemInserted(myTodoRecyclerViewAdapter.getItemCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNavAllClick() {
        setDatabaseListener("all");
    }

    @Override
    public void onNavMyClick() {
        setDatabaseListener("my");
    }

    @Override
    public void onNavSignOutClick() {
        signOut();
    }
}
