package com.NEU23FallGroup7.jobtrackpro;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
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
        holder.companyTextView.setText(currentJob.getCompany());
        String des = currentJob.getDescription();
        holder.descriptionText.setText(des.substring(0, Math.min(des.length(), 160)) + "... More Details");

        if (currentJob.getState().equals("")) {
            holder.locationTextView.setText(currentJob.getCity());
        } else if (!currentJob.getCountry().equals("")) {
            holder.locationTextView.setText(currentJob.getCity() + ", " + currentJob.getState() + ", " + currentJob.getCountry());
        } else if (!currentJob.getState().equals("")) {
            holder.locationTextView.setText(currentJob.getCity() + ", " + currentJob.getState());
        }

        if (!currentJob.getRemote().equals("")) {
            holder.remoteChip.setVisibility(View.VISIBLE);
            holder.remoteChip.setText(currentJob.getRemote());
        } else {
            holder.remoteChip.setVisibility(View.GONE);
        }


        if (!currentJob.getFull_time().equals("")) {
            holder.fullTimeChip.setVisibility(View.VISIBLE);
            holder.fullTimeChip.setText(currentJob.getFull_time());
        } else {
            holder.fullTimeChip.setVisibility(View.GONE);
        }

        if (!currentJob.getSalary().equals("")) {
            holder.salaryChip.setVisibility(View.VISIBLE);
            holder.salaryChip.setText(currentJob.getSalary());
        } else {
            holder.salaryChip.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: go to job details page
            }

        });


        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: toggle the favorite state
                // add to or delete from the  favorite list

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
        return list.size();
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



