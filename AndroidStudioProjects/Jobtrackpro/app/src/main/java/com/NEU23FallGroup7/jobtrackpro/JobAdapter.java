package com.NEU23FallGroup7.jobtrackpro;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.NEU23FallGroup7.jobtrackpro.Models.Jobs;
import com.google.android.material.chip.Chip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private final ArrayList<Jobs> list;
    private final Context context;
    private final Handler uiHandler;


    public JobAdapter(ArrayList<Jobs> list, Context context) {
        this.list = list;
        this.context = context;
        this.uiHandler = new Handler();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Jobs currentJob = list.get(position);
        holder.titleTextView.setText(currentJob.getTitle());
        holder.companyTextView.setText(currentJob.getCompany().getDisplay_name());
        String des = currentJob.getDescription();
        holder.descriptionText.setText(des.substring(0, Math.min(des.length(), 160)) + "... More Details");

        holder.locationTextView.setText(currentJob.getLocation().display_name);

        if (currentJob.getContract_type()!=null && !currentJob.getContract_type().equals("")) {
            holder.remoteChip.setVisibility(View.VISIBLE);
            holder.remoteChip.setText(currentJob.getContract_type());
        } else {
            holder.remoteChip.setVisibility(View.GONE);
        }
        if (currentJob.getContract_time()!=null && !currentJob.getContract_time().equals("")) {
            holder.fullTimeChip.setVisibility(View.VISIBLE);
            holder.fullTimeChip.setText(currentJob.getContract_time());
        } else {
            holder.fullTimeChip.setVisibility(View.GONE);
        }

        if (currentJob.getSalary_max()!=0) {
            holder.salaryChip.setVisibility(View.VISIBLE);
            holder.salaryChip.setText(String.valueOf(currentJob.getSalary_max()));
        } else {
            holder.salaryChip.setVisibility(View.GONE);
        }

        if (currentJob.isFavourite()) {
            holder.favoriteButton.setImageResource(R.drawable.baseline_star_rate_24);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.baseline_star_outline_24);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: go to job details page
                JobDetailsFragment jobDetailsFragment = new JobDetailsFragment();
                //pass the job id to the job details page
                Bundle bundle = new Bundle();
                bundle.putString("job_url", currentJob.getRedirect_url());
                jobDetailsFragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, jobDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:  add to or delete from the  favorite list to db

                // toggle state
                if(currentJob.isFavourite()){
                    currentJob.setFavourite(false);
                }
                else{
                    currentJob.setFavourite(true);
                }
                // update UI
                if (currentJob.isFavourite()) {
                    holder.favoriteButton.setImageResource(R.drawable.baseline_star_rate_24);
                } else {
                    holder.favoriteButton.setImageResource(R.drawable.baseline_star_outline_24);
                }
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //share the screenshot of the job
                viewShot(holder.cardView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView companyTextView;
        TextView locationTextView;
        Chip remoteChip;
        Chip fullTimeChip;
        Chip salaryChip;
        TextView descriptionText;
        ImageButton favoriteButton;
        ImageButton shareButton;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.job_title);
            companyTextView = itemView.findViewById(R.id.company_name);
            locationTextView = itemView.findViewById(R.id.location);
            remoteChip = itemView.findViewById(R.id.remote);
            fullTimeChip = itemView.findViewById(R.id.full_time);
            salaryChip = itemView.findViewById(R.id.salary);
            favoriteButton = itemView.findViewById(R.id.star_btn);
            shareButton = itemView.findViewById(R.id.share_btn);
            cardView = itemView.findViewById(R.id.job_card);
            descriptionText = itemView.findViewById(R.id.job_description);
        }
    }

    public void viewShot(View view) {
        Intent intent = null;
        try {
            Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            view.layout(0, 0, view.getLayoutParams().width = 1020, view.getLayoutParams().height = 700);
            view.draw(c);
            // Create a content URI for the bitmap
            Uri contentUri = getImageUri(view.getContext(), b);

            intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission
            view.getContext().startActivity(Intent.createChooser(intent, "Share Job"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent != null) {
            view.getContext().startActivity(Intent.createChooser(intent, "Share Job"));
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

}


