package com.referex.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.referex.R;
import com.referex.utils.Constants;
import com.referex.utils.TypefaceSpan;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;


public class ChangePasswordActivity extends AppCompatActivity {
    
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    EditText etOldPassword;
    EditText etNewPassword;
    EditText etConfirmPassword;
    TextView tvSubmit;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_change_password);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (ChangePasswordActivity.this);
        Utils.setTypefaceToAllViews (this, clMain);
        userDetailsPref = UserDetailsPref.getInstance ();
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        etNewPassword = (EditText) findViewById (R.id.etNewPassword);
        etOldPassword = (EditText) findViewById (R.id.etOldPassword);
        etConfirmPassword = (EditText) findViewById (R.id.etConfirmPassword);
    }
    
    private void initListener () {
        etNewPassword.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etNewPassword.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etOldPassword.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etOldPassword.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etConfirmPassword.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etConfirmPassword.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Utils.hideSoftKeyboard (ChangePasswordActivity.this);
                SpannableString s1 = new SpannableString (getResources ().getString (R.string.please_enter_password));
                s1.setSpan (new TypefaceSpan (ChangePasswordActivity.this, Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.Password_not_matched));
                s1.setSpan (new TypefaceSpan (ChangePasswordActivity.this, Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (etOldPassword.getText ().toString ().trim ().length () == 0 && etNewPassword.getText ().toString ().length () == 0 && etOldPassword.getText ().toString ().length () == 0) {
                    etNewPassword.setError (s1);
                    etOldPassword.setError (s1);
                    etConfirmPassword.setError (s1);
                } else if (etOldPassword.getText ().toString ().trim ().equalsIgnoreCase (etOldPassword.getText ().toString ().trim ())) {
                    etConfirmPassword.setError (s2);
                } else {
                    // sendLoginCredentialsToServer(etUserName.getText().toString().trim(), etPassword.getText().toString().trim(), jsonDeviceDetails.toString());
                }
            }
        });
    }
}