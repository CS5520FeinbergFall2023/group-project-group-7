package com.NEU23FallGroup7.jobtrackpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class UpdateResume extends androidx.fragment.app.DialogFragment {
    Button update_btn;
    EditText resume_name;
    TextView title;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_upload, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        update_btn = view.findViewById(R.id.uploadButton);
        resume_name = view.findViewById(R.id.resumeName);
        title = view.findViewById(R.id.titleResume);
        title.setText("Update Resume");
        update_btn.setText("Update");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String resumeName = bundle.getString("resume_name", "");
            resume_name.setText(resumeName);
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateButtonClick();
                dismiss();
            }
        });
    }

    public interface UpdateListener {
        void onUpdate(String newName);
    }

    private UpdateListener updateListener;

    public void setUpdateListener(UpdateListener listener) {
        this.updateListener = listener;
    }

    private void onUpdateButtonClick() {
        if (updateListener != null) {
            String newName = resume_name.getText().toString();
            updateListener.onUpdate(newName);
        }
    }

}
