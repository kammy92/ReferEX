package com.referex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.referex.R;
import com.referex.utils.Utils;

/**
 * Created by l on 25/07/2017.
 */

public class FeedbackActivity extends AppCompatActivity {
    
    
    TextView tvSubmit;
    TextView tvTitle;
    EditText etName;
    EditText etEmail;
    EditText etFeedback;
    RelativeLayout rlBack;
    
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_feedback);
        initView ();
        initData ();
        initListener ();
        getExtras ();
        
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        // jobPosition = intent.getStringExtra (AppConfigTags.JOB_POSITION);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, tvSubmit);
    }
    
    private void initView () {
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvSubmit = (TextView) findViewById (R.id.tvSubmit);
        etName = (EditText) findViewById (R.id.etName);
        etEmail = (EditText) findViewById (R.id.etEmail);
        etFeedback = (EditText) findViewById (R.id.etFeedback);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
    }
}



