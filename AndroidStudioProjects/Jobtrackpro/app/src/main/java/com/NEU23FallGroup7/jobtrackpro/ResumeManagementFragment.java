package com.NEU23FallGroup7.jobtrackpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ResumeManagementFragment extends Fragment {
    ArrayList<Resumes> resumeList = new ArrayList<>();
    SearchView searchView;
    RecyclerView recyclerView;
    FloatingActionButton add;
    ResumeAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("Resumes");

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = now.format(formatter);

    public ResumeManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // test data
        Resumes resume = new Resumes("test1", "www.baidu.com", formattedDateTime);
        resumeList.add(resume);
        Resumes resume2 = new Resumes("test2", "www.google.com", formattedDateTime);
        resumeList.add(resume2);

        View view = inflater.inflate(R.layout.fragment_resume_management, container, false);
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ResumeAdapter(resumeList,getContext());
        recyclerView.setAdapter(adapter);

        add = view.findViewById(R.id.fab_add_Resume);

        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return true;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment=new UploadResume();
                dialogFragment.show(requireActivity().getSupportFragmentManager(),"upload resume");
            }
        });
    }

    public void searchList(String text){
        ArrayList<Resumes> searchList = new ArrayList<>();
        for (Resumes resume : resumeList){
            if (resume.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(resume);
            }
        }
        adapter.searchDataList(searchList);
    }
}