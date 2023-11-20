package com.NEU23FallGroup7.jobtrackpro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.MyViewHolder> {
    private ArrayList<Resumes> resumelist;

    public ResumeAdapter(ArrayList<Resumes> resumelist) {
        this.resumelist = resumelist;
    }

    // This method creates a new ViewHolder object for each item in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder object
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_resume, parent, false);
        return new MyViewHolder(itemView);
    }

    // This method returns the total
    // number of items in the data set
    @Override
    public int getItemCount() {
        return resumelist.size();
    }

    // This method binds the data to the ViewHolder object
    // for each item in the RecyclerView
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Resumes currentResume = resumelist.get(position);
        holder.name.setText(currentResume.getName());
        holder.date.setText(currentResume.getDate());
    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.resumeName);
            date = itemView.findViewById(R.id.UpdateDate);
        }
    }
}

