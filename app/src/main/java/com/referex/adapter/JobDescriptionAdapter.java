package com.referex.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.referex.R;
import com.referex.activity.UploadResumeActivity;
import com.referex.model.JobDescription;
import com.referex.utils.AppConfigTags;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class JobDescriptionAdapter extends RecyclerView.Adapter<JobDescriptionAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;

    private Activity activity;
    private List<JobDescription> jobDescriptions = new ArrayList<JobDescription>();

    public JobDescriptionAdapter(Activity activity, List<JobDescription> jobDescriptions) {
        this.activity = activity;
        this.jobDescriptions = jobDescriptions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item_job_listing, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);

        final JobDescription jobDescription = jobDescriptions.get(position);

        Utils.setTypefaceToAllViews(activity, holder.tvCompanyName);
        Utils.setTypefaceToAllViews(activity, holder.tvExperience);
        Utils.setTypefaceToAllViews(activity, holder.tvJobName);
        Utils.setTypefaceToAllViews(activity, holder.tvLocation);
        Utils.setTypefaceToAllViews(activity, holder.tvSkill);

        holder.tvCompanyName.setText(jobDescription.getCompany_name());
        holder.tvJobName.setText(jobDescription.getJob_name());
        holder.tvLocation.setText(jobDescription.getLocation());
        holder.tvSkill.setText(jobDescription.getSkill());
        holder.tvExperience.setText(jobDescription.getExperience());
    
        if (jobDescription.is_hot ()) {
            holder.ivHot.setVisibility (View.VISIBLE);
        } else {
            holder.ivHot.setVisibility (View.INVISIBLE);
        }


        holder.ivRating.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (jobDescription.is_favorite ()) {
                    jobDescription.setIs_favorite (false);
                    holder.ivRating.setImageResource (R.drawable.ic_unselect_rating);
                   // updateFavouriteStatus (false, property.getId ());
                } else {
                    jobDescription.setIs_favorite (true);
                    holder.ivRating.setImageResource (R.drawable.ic_select_rating);
                 //   updateFavouriteStatus (true, property.getId ());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return jobDescriptions.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvJobName;
        TextView tvCompanyName;
        TextView tvExperience;
        TextView tvLocation;
        TextView tvSkill;
        ImageView ivRating;
        ImageView ivHot;
        ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);
            tvJobName = (TextView) view.findViewById(R.id.tvJobDescription);
            tvCompanyName = (TextView) view.findViewById(R.id.tvCompanyName);
            tvExperience = (TextView) view.findViewById(R.id.tvExperience);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            tvSkill = (TextView) view.findViewById(R.id.tvSkill);
            ivRating = (ImageView) view.findViewById(R.id.ivRating);
    
            ivHot = (ImageView) view.findViewById (R.id.ivHot);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final JobDescription jobDescription = jobDescriptions.get (getLayoutPosition ());
            Intent intent=new Intent(activity, UploadResumeActivity.class);
            intent.putExtra(AppConfigTags.JOB_POSITION,jobDescription.getJob_name());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


        }
    }
}


