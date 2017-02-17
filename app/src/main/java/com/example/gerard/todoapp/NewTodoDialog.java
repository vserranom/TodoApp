package com.example.gerard.todoapp;

import android.app.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;


public class NewTodoDialog extends DialogFragment implements
    View.OnClickListener{

    static final int RC_IMAGE_PICK = 9000;

    public interface NewTodoDialogListener {
        void onDialogPositiveClick(NewTodoDialog dialog);
        void onDialogNegativeClick(NewTodoDialog dialog);
    }

    NewTodoDialogListener mListener;

    ImageView imageViewPreview;
    EditText editTextTitle;
    DatePicker datePickerDeadline;

    public String title;
    public String deadline;
    public String imageUri;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_todo, null);
        view.findViewById(R.id.image_button).setOnClickListener(this);

        editTextTitle = (EditText) view.findViewById(R.id.new_todo_title);
        datePickerDeadline = (DatePicker) view.findViewById(R.id.new_todo_deadline);
        imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

        builder.setTitle(R.string.dialog_new_todo)
                .setView(view)
                .setPositiveButton(R.string.dialog_new_todo_create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        title = editTextTitle.getText().toString();
                        deadline = datePickerDeadline.getDayOfMonth() + "/" + datePickerDeadline.getMonth() + "/" + datePickerDeadline.getYear();

                        mListener.onDialogPositiveClick(NewTodoDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_new_todo_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(NewTodoDialog.this);
                    }
                });
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            mListener = (NewTodoDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NewTodoDialogListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_button:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RC_IMAGE_PICK);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE_PICK) {
            if(data != null) {
                Uri uri = data.getData();
                imageViewPreview.setImageURI(uri);
                imageUri = uri.toString();
            }
        }
    }
}
