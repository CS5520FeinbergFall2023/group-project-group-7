package com.NEU23FallGroup7.jobtrackpro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NEU23FallGroup7.jobtrackpro.Models.Jobs;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JobSearchFragment extends Fragment {

    private static final String TAG = "JobSearchFragment";

    private Context context;

    private ArrayList<Jobs> jobList = new ArrayList<>();
    private JobAdapter adapter;
    private RequestQueue queue;

    public static final String BASE_URL = "https://api.adzuna.com/v1/api/jobs/in/search/1?";
    public static final String QUES = "?";
    public static final String AND = "&";
    public static final String APP_ID = "app_id=" + BuildConfig.API_ID;
    public static final String APP_KEY = "app_key=" + BuildConfig.API_KEY;
    //TODO: add more query conditions from user input to what
    public static final String WHAT = "what=java";
    public static final String RESULTS_PER_PAGE = "results_per_page=20";
    public static final String SUFFIX = "&content-type=application/json";
    public static TextView titleTextView;
    public static RecyclerView recyclerView;

    public JobSearchFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_jobs, container, false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.job_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new JobAdapter(jobList,getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
    //TODO: add the query to worker thread
    //TODO: add more query conditions
    //Modify the UI to display more details of the job
    //what=javascript%20developer&what_exclude=java&where=london&sort_by=salary&salary_min=30000&full_time=1&permanent=1&
    // query parameters: results_per_page, what, what_exclude, what_and, what_or, where, sort_by, salary_min, salary_max, full_time, part_time, permanent
    //country, distance, location0, location1, location3, contract,company, category, max_days_old,
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + APP_ID + AND + APP_KEY + AND + RESULTS_PER_PAGE + AND + WHAT + SUFFIX, null,
                this::extractData, error -> {
            Toast.makeText(context, "Could not retrieve data. Error: " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "Could not retrieve access token. Error: " + error.getMessage());
        });

        queue.add(request);
    }

    private void extractData(JSONObject response) {
        Gson gson = new Gson();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                Type type = new TypeToken<Jobs>() {
                }.getType();
                Jobs job = gson.fromJson(result.toString(), type);
                Log.d(TAG, "Job title: " + job.getTitle());
                job.setFavourite(false) ;
                jobList.add(job);
                adapter.notifyDataSetChanged();
            }
            Toast.makeText(context, "Count: " + jobList.size(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Could not retrieve data. Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "Could not retrieve access token. Error: " + e.getMessage());
        }
    }
}

