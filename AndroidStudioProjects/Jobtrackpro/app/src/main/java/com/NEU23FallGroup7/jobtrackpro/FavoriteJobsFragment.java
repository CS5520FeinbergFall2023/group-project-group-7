package com.NEU23FallGroup7.jobtrackpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NEU23FallGroup7.jobtrackpro.Models.Category;
import com.NEU23FallGroup7.jobtrackpro.Models.Company;
import com.NEU23FallGroup7.jobtrackpro.Models.Jobs;
import com.NEU23FallGroup7.jobtrackpro.Models.Location;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class FavoriteJobsFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Jobs> jobList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("favoriteJobs");

    public FavoriteJobsFragment() {
    }

    Date now = new Date();
    ArrayList<String> area = new ArrayList<>();


    Location location = new Location(area, "Boston, MA");


    Jobs job = new Jobs(
            "We are looking for an IT Solutions Architect who is a self-starter, passionate about developing and managing cutting-edge IT solutions. Reporting directly to the president, you will have the autonomy to strategize, implement, and manage IT projects with a significant focus on GPT AI integration. You will maintain our existing systems while also spearheading the development of new, innovative solutions to streamline business operations.",
            "salary_is_predicted",
            "permanent",
            now,
            "id",
            "redirect_url",
            location,
            new Category("java","java"),
            "Full Stack Developer",
            new Company("Microsoft"),
            "full_time",
            100000,
            200000,
            1.1,
            1.1,
            true
    );


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
        //TODO: add favorite jobs to firebase
        //TODO: Retrieve favorite jobs from firebase

        return view;
    }

}


