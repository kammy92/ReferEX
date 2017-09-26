package com.referex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.referex.R;
import com.referex.adapter.JobDescriptionAdapter;
import com.referex.model.JobDescription;
import com.referex.utils.SimpleDividerItemDecoration;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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
        jobDescriptionList.add (new JobDescription (1, "Android Developer - Java", "XYZ Pvt Ltd", "1-3 Years", "Bengaluru", "Android, Java, SDK, Mobile Development", true, true));
        jobDescriptionList.add (new JobDescription (2, "Java Full Stack Developer", "Premium", "3-5 Years", "Delhi NCR", "Java, Swings, Servlets, Applets, JavaScript, Java Advanced", true, true));
        
        swipeRefreshLayout.setRefreshing (false);
        
        jobDescriptionAdapter = new JobDescriptionAdapter (this, jobDescriptionList);
        rvJobList.setAdapter (jobDescriptionAdapter);
        rvJobList.setHasFixedSize (true);
        rvJobList.addItemDecoration (new SimpleDividerItemDecoration (this));
        rvJobList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvJobList.setItemAnimator (new DefaultItemAnimator ());
        
        
    }
    
    
}