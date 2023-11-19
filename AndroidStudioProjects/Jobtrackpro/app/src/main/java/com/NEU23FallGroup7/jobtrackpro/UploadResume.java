package com.NEU23FallGroup7.jobtrackpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;

public class UploadResume extends AppCompatActivity {
    private ActivityResultLauncher<Intent> filePickerLauncher;

    Button upload_btn;
    EditText resume_name;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        upload_btn = findViewById(R.id.uploadButton);
        resume_name = findViewById(R.id.resumeName);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Resumes");

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFiles();
            }
        });

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    UploadFiles(data.getData());
                }
            }
        });
    }

    private void selectFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        filePickerLauncher.launch(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            UploadFiles(data.getData());
        }
    }

    private void UploadFiles(Uri data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Uploading");
        builder.setCancelable(false);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);

        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

        AlertDialog progressDialog = builder.create();
        progressDialog.show();

        StorageReference reference = storageReference.child("Resumes/" + System.currentTimeMillis() + ".PDF");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                SimpleDateFormat formatter  = new SimpleDateFormat("HH:MM:SS");
                Resumes resumes = new Resumes(resume_name.getText().toString(), uri.toString(), formatter.format(System.currentTimeMillis()));
                databaseReference.child(databaseReference.push().getKey()).setValue(resumes);

                Toast.makeText(UploadResume.this, "File uploaded successfully !", Toast.LENGTH_SHORT);

                progressDialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            }
        });
    }
}