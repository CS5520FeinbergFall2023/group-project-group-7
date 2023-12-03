package com.NEU23FallGroup7.jobtrackpro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.NEU23FallGroup7.jobtrackpro.util.Dialog;
import com.NEU23FallGroup7.jobtrackpro.util.GetCurTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ResumeAdapter extends RecyclerView.Adapter<MyViewHolder> {
    ArrayList<Resumes> list;
    private final Context context;

    public ResumeAdapter(ArrayList<Resumes> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_resume, parent, false);
        return new MyViewHolder(itemView).linkAdapter(this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Resumes currentResume = list.get(position);
        holder.name.setText(currentResume.getName());
        holder.date.setText(currentResume.getDate());
    }

    public void searchDataList(ArrayList<Resumes> searchList){
        list = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder implements Dialog.DialogClickListener, UpdateResume.UpdateListener{
    TextView name;
    TextView date;
    private ResumeAdapter adapter;
    Dialog dialog;

    public MyViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.resumeName);
        date = itemView.findViewById(R.id.UpdateDate);
        itemView.findViewById(R.id.imageDelete).setOnClickListener(view ->
        {
            dialog.showDialog(
                    itemView.getContext(),
                    "Ok",
                    "Cancel",
                    "Delete",
                    "Are you sure you want to delete this document?",
                    this
            );
        });

        itemView.findViewById(R.id.resumeCard).setOnClickListener(view ->{
            UpdateResume updateResume = new UpdateResume();
            int position = getAdapterPosition();
            Bundle bundle = new Bundle();
            bundle.putString("resume_name", adapter.list.get(position).getName());
            updateResume.setArguments(bundle);
            updateResume.setUpdateListener(this);
            updateResume.show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), "update_resume_dialog");
        });
    }

    public MyViewHolder linkAdapter(ResumeAdapter adapter){
        this.adapter = adapter;
        return this;
    }
    @Override
    public void onPositiveClick() {
        adapter.list.remove(getAdapterPosition());
        adapter.notifyItemRemoved(getAdapterPosition());
    }

    @Override
    public void onUpdate(String newName) {
        int position = getAdapterPosition();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String curTime = GetCurTime.getTime();
        adapter.list.get(position).setName(newName);
        adapter.list.get(position).setDate(curTime);
        adapter.notifyItemChanged(position);
        Toast.makeText(itemView.getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
    }
}
