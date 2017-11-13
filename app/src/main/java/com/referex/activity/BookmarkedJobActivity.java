package com.referex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.referex.R;
import com.referex.adapter.JobDescriptionAdapter;
import com.referex.model.JobDescription;
import com.referex.utils.AppConfigTags;
import com.referex.utils.AppConfigURL;
import com.referex.utils.Constants;
import com.referex.utils.NetworkConnection;
import com.referex.utils.SimpleDividerItemDecoration;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 27/07/2017.
 */

public class BookmarkedJobActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    List<JobDescription> jobDescriptionList = new ArrayList<> ();
    RelativeLayout rlInternetConnection;
    RelativeLayout rlNoResultFound;
    RecyclerView rvJobList;
    ImageView ivFilter;
    CoordinatorLayout clMain;

    JobDescriptionAdapter jobDescriptionAdapter;
    UserDetailsPref userDetailsPref;
    TextView tvTitle;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rlBack;
    
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_bookmarked_job);
        this.savedInstanceState = savedInstanceState;
        initView ();
        initData ();
        initListener ();
        setData ();
    }
    
    
    private void initView () {
        rlInternetConnection = (RelativeLayout) findViewById (R.id.rlInternetConnection);
        ivFilter = (ImageView) findViewById (R.id.ivFilter);
        rlNoResultFound = (RelativeLayout) findViewById (R.id.rlNoResultFound);
        rvJobList = (RecyclerView) findViewById (R.id.rvJobList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        Utils.setTypefaceToAllViews (this, tvTitle);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);

    }
    
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                swipeRefreshLayout.setRefreshing (false);
                setData ();
            }
        });
        
        ivFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ivFilter.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        Intent intent4 = new Intent (BookmarkedJobActivity.this, FilterActivity.class);
                        startActivity (intent4);
                        overridePendingTransition (R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                });
            }
        });

    }
    
    private void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        jobDescriptionList.clear ();
       /* jobDescriptionList.add (new JobDescription (1, "Android Developer - Java", "XYZ Pvt Ltd", "1-3 Years", "Bengaluru", "Android, Java, SDK, Mobile Development", true, true));
        jobDescriptionList.add (new JobDescription (2, "Java Full Stack Developer", "Premium", "3-5 Years", "Delhi NCR", "Java, Swings, Servlets, Applets, JavaScript, Java Advanced", true, true));
        */
        swipeRefreshLayout.setRefreshing (false);
        
        jobDescriptionAdapter = new JobDescriptionAdapter (this, jobDescriptionList);
        rvJobList.setAdapter (jobDescriptionAdapter);
        rvJobList.setHasFixedSize (true);
        rvJobList.addItemDecoration (new SimpleDividerItemDecoration (this));
        rvJobList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvJobList.setItemAnimator (new DefaultItemAnimator ());
    
    
    }
    
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (BookmarkedJobActivity.this)) {
            jobDescriptionList.clear ();
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_BOOKMARKED, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_BOOKMARKED,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayJobs = jsonObj.getJSONArray (AppConfigTags.JOBS);
                                        for (int i = 0; i < jsonArrayJobs.length (); i++) {
                                            JSONObject jsonObjectDescription = jsonArrayJobs.getJSONObject (i);
                                            JobDescription jobDescription = new JobDescription (
                                                    jsonObjectDescription.getInt (AppConfigTags.JOB_ID),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_TITLE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_DESCRIPTION),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MIN_EXPERIENCE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MAX_EXPERIENCE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_SKILLS),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_COMPANY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_LOCATION),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MIN_SALARY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MAX_SALARY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_POSTED_AT),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_TYPE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_EXPIRES_IN),
                                                    jsonObjectDescription.getBoolean (AppConfigTags.JOB_BOOKMARKED)
                                            );
                                            jobDescriptionList.add (i, jobDescription);
                                        }
                                        jobDescriptionAdapter.notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (BookmarkedJobActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    
                                }
                            } else {
                                Utils.showSnackBar (BookmarkedJobActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (BookmarkedJobActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (BookmarkedJobActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            Utils.showSnackBar (BookmarkedJobActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
}