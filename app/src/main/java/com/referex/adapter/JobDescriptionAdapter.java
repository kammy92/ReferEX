package com.referex.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.referex.R;
import com.referex.activity.UploadResumeActivity;
import com.referex.model.JobDescription;
import com.referex.utils.AppConfigTags;
import com.referex.utils.AppConfigURL;
import com.referex.utils.Constants;
import com.referex.utils.NetworkConnection;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


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
        Utils.setTypefaceToAllViews (activity, holder.tvJobDescription);
        Utils.setTypefaceToAllViews(activity, holder.tvLocation);
        Utils.setTypefaceToAllViews(activity, holder.tvSkill);

        holder.tvCompanyName.setText(jobDescription.getCompany_name());
        holder.tvTitle.setText (jobDescription.getJob_title ());
        holder.tvJobDescription.setText (jobDescription.getJob_description ());
        holder.tvLocation.setText(jobDescription.getLocation());
        holder.tvSkill.setText(jobDescription.getSkill());
        holder.tvTime.setText (Utils.getFormattedDate (activity, (Utils.getDateInMillis (jobDescription.getPosted_at ()))));
        holder.tvExperience.setText (jobDescription.getMin_experience () + " - " + jobDescription.getMax_experience () + "  Year (" + jobDescription.getJob_type () + ")");
        holder.tvSalary.setText (jobDescription.getMin_salary () + " - " + jobDescription.getMax_salary ());

            holder.ivHot.setVisibility (View.VISIBLE);
    
    
        if (jobDescription.is_bookmarked ()) {
            holder.ivRating.setImageResource (R.drawable.ic_bookmark_filled);
        } else {
            holder.ivRating.setImageResource (R.drawable.ic_bookmark);
        }

        holder.ivRating.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (jobDescription.is_bookmarked ()) {
                    jobDescription.setIs_bookmarked (false);
                    holder.ivRating.setImageResource (R.drawable.ic_bookmark);
                    updateBookMarkedJobStatus (jobDescription.getId ());

//                    holder.ivRating.setImageResource (R.drawable.ic_unselect_rating);
                    // updateFavouriteStatus (false, property.getId ());
                } else {
                    jobDescription.setIs_bookmarked (true);
                    holder.ivRating.setImageResource (R.drawable.ic_bookmark_filled);
                    updateBookMarkedJobStatus (jobDescription.getId ());
//                    holder.ivRating.setImageResource (R.drawable.ic_select_rating);
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
    
    public void updateBookMarkedJobStatus (int job_id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_BOOKMARKED_JOB + "/" + job_id, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_BOOKMARKED_JOB + "/" + job_id,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        
                                    } else {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    
                    Map<String, String> params = new Hashtable<String, String> ();
                    // params.put (AppConfigTags.id, "property_favourite");
                    
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (activity, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
        }
    }

    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvJobDescription;
        TextView tvCompanyName;
        TextView tvExperience;
        TextView tvTime;
        TextView tvLocation;
        TextView tvTitle;
        TextView tvSkill;
        TextView tvSalary;
        ImageView ivRating;
        ImageView ivHot;

        ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);
            tvJobDescription = (TextView) view.findViewById (R.id.tvJobDescription);
            tvTitle = (TextView) view.findViewById (R.id.tvTitle);
            tvCompanyName = (TextView) view.findViewById(R.id.tvCompanyName);
            tvExperience = (TextView) view.findViewById(R.id.tvExperience);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            tvSkill = (TextView) view.findViewById(R.id.tvSkill);
            tvTime = (TextView) view.findViewById (R.id.tvTime);
            ivRating = (ImageView) view.findViewById(R.id.ivRating);
            tvSalary = (TextView) view.findViewById (R.id.tvSalary);
            ivHot = (ImageView) view.findViewById (R.id.ivHot);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final JobDescription jobDescription = jobDescriptions.get (getLayoutPosition ());
            Intent intent = new Intent (activity, UploadResumeActivity.class);
//            Intent intent = new Intent (activity, UploadFileActivity.class);
            intent.putExtra (AppConfigTags.JOB_ID, jobDescription.getId ());
            intent.putExtra (AppConfigTags.JOB_POSITION, jobDescription.getJob_title ());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


        }
    }
    
    
}


