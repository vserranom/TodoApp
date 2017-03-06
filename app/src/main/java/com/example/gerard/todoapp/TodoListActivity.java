package com.example.gerard.todoapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoListActivity extends DrawerActivity implements
        DrawerActivity.FabClickListener,
        NewTodoDialog.NewTodoDialogListener {

    interface DialogPositiveClickListener {
       void onDialogPositiveClick(String title, String deadline, String imageUri);
    }

    String TAG = "TodoListActivity";

    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;
    DatabaseReference mDatabase;

    DialogPositiveClickListener dialogPositiveClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setFabClickListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.todo_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(myTodoRecyclerViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onFabClick() {
        new NewTodoDialog().show(getSupportFragmentManager(), "NewTodoDialogFragment");
    }

    public void setOnDialogPositiveClickListener(DialogPositiveClickListener listener){
        dialogPositiveClickListener = listener;
    }

    @Override
    public void onDialogPositiveClick(String title, String deadline, String imageUri) {
        dialogPositiveClickListener.onDialogPositiveClick(title, deadline, imageUri);
    }
}



class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder>
        implements ItemTouchHelperAdapter {

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

        customViewHolder.todoTitle.setText(todoItem.title);
        customViewHolder.todoDeadline.setText(todoItem.deadline);
        if(todoItem.imageUri != null) {
            Uri uri = Uri.parse(todoItem.imageUri);
            // TODO: Solicitar permisos
            // https://developer.android.com/training/permissions/requesting.html
            customViewHolder.todoImage.setImageURI(uri);
        }
    }

    @Override
    public int getItemCount() {
        return (null != todoItemList ? todoItemList.size() : 0);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // TODO: update Database
        Collections.swap(todoItemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        // TODO: update Database
        todoItemList.remove(position);
        notifyItemRemoved(position);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView todoTitle;
        protected TextView todoDeadline;
        protected ImageView todoImage;

        public CustomViewHolder(View view) {
            super(view);
            this.todoTitle = (TextView) view.findViewById(R.id.todo_title);
            this.todoDeadline = (TextView) view.findViewById(R.id.todo_deadline);
            this.todoImage = (ImageView) view.findViewById(R.id.todo_image);
        }
    }
}

interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}

class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}