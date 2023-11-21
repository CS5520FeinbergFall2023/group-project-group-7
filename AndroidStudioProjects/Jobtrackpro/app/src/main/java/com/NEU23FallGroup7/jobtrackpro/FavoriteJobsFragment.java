package com.NEU23FallGroup7.jobtrackpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;

public class FavoriteJobsFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Jobs> jobList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("favoriteJobs");

    public FavoriteJobsFragment() {

    }

    Time now = new Time(System.currentTimeMillis());

    Jobs job = new Jobs("123456789", "Software Engineer",
            "Microsoft", "Vancouver", "BC", "Canada", now,
            "90000/year", "USD", "Microsoft Teams is the hub for teamwork in Office 365 that integrates all the people, content, and tools your team needs to be more engaged and effective. It is core to Microsoft’s modern work, modern life & modern education value proposition. The Microsoft Teams Calling & Meetings group is focused on intelligent, real-time connected/collaboration experiences. This is also the group which will work with partner teams on device (Surface, Rigel, HoloLens) efforts which could involve Teams.",
            "Remote",
            "", "https://www.google.com", true);



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_jobs, container, false);
        jobList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            jobList.add(job);
        }
        recyclerView = view.findViewById(R.id.job_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        JobAdapter adapter = new JobAdapter(jobList,getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

}


