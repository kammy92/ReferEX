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

public class SearchResultActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    RelativeLayout rlInternetConnection;
    RelativeLayout rlNoResultFound;
    UserDetailsPref userDetailsPref;
    TextView tvTitle;
    RecyclerView rvJobList;
    CoordinatorLayout clMain;
    SwipeRefreshLayout swipeRefreshLayout;
    JobDescriptionAdapter jobDescriptionAdapter;
    List<JobDescription> jobDescriptionList = new ArrayList<> ();
    String minExp;
    String maxExp;
    String minSalary;
    String maxSalary;
    String location;
    String skill;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_search_result);
        this.savedInstanceState = savedInstanceState;
        initView ();
        initData ();
        initListener ();
        initBundle ();
        recommendedJobList ();
        
        
    }
    
    private void initBundle () {
        Bundle extras = getIntent ().getExtras ();
        
        if (extras != null) {
            minExp = extras.getString (AppConfigTags.MIN_EXP);
            maxExp = extras.getString (AppConfigTags.MAX_EXP);
            minSalary = extras.getString (AppConfigTags.MIN_SALARY);
            maxSalary = extras.getString (AppConfigTags.MAX_SALARY);
            location = extras.getString (AppConfigTags.LOCATIONS);
            skill = extras.getString (AppConfigTags.SKILLS);
        }
    }
    
    
    private void initView () {
        
        rlInternetConnection = (RelativeLayout) findViewById (R.id.rlInternetConnection);
        rlNoResultFound = (RelativeLayout) findViewById (R.id.rlNoResultFound);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        
        Utils.setTypefaceToAllViews (SearchResultActivity.this, tvTitle);
        
        rvJobList = (RecyclerView) findViewById (R.id.rvJobList);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
        
    }
    
    private void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        
        jobDescriptionAdapter = new JobDescriptionAdapter (this, jobDescriptionList);
        rvJobList.setAdapter (jobDescriptionAdapter);
        rvJobList.setHasFixedSize (true);
        rvJobList.addItemDecoration (new SimpleDividerItemDecoration (this));
        rvJobList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvJobList.setItemAnimator (new DefaultItemAnimator ());
        
        
    }
    
    private void initListener () {
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                recommendedJobList ();
            }
        });
    }
    
    @Override
    public void onBackPressed () {
        super.onBackPressed ();
    }
    
    
    public void recommendedJobList () {
        if (NetworkConnection.isNetworkAvailable (SearchResultActivity.this)) {
            jobDescriptionList.clear ();
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SEARCH, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SEARCH,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                rlNoResultFound.setVisibility (View.GONE);
                                rvJobList.setVisibility (View.VISIBLE);
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        swipeRefreshLayout.setRefreshing (false);
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
                                    Utils.showSnackBar (SearchResultActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    
                                }
                            } else {
                                rlNoResultFound.setVisibility (View.VISIBLE);
                                rvJobList.setVisibility (View.GONE);
                                Utils.showSnackBar (SearchResultActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (SearchResultActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.MIN_EXP, minExp);
                    params.put (AppConfigTags.MAX_EXP, maxExp);
                    params.put (AppConfigTags.MIN_SALARY, minSalary);
                    params.put (AppConfigTags.MAX_SALARY, maxSalary);
                    params.put (AppConfigTags.LOCATIONS, location);
                    params.put (AppConfigTags.SKILLS, skill);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (SearchResultActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            Utils.showSnackBar (SearchResultActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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
