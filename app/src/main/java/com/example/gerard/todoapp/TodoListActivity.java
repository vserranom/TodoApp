package com.example.gerard.todoapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoListActivity extends DrawerActivity {
    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.todo_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);

        for (int i = 1; i <= 30; i++) {
            myTodoRecyclerViewAdapter.getList().add(new TodoItem("TODO " + i));
        }
        myTodoRecyclerViewAdapter.notifyDataSetChanged();
    }
}

class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder> {
    private List<TodoItem> todoItemList;

    public MyTodoRecyclerViewAdapter() {
        this.todoItemList = new ArrayList<>();
    }

    public List<TodoItem> getList(){
        return todoItemList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        TodoItem todoItem = todoItemList.get(i);

        customViewHolder.textView.setText(todoItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != todoItemList ? todoItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.todo_title);
        }
    }
}
