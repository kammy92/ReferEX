package com.referex.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.referex.R;
import com.referex.utils.AppConfigTags;
import com.referex.utils.AppConfigURL;
import com.referex.utils.Constants;
import com.referex.utils.NetworkConnection;
import com.referex.utils.SetTypeFace;
import com.referex.utils.TypefaceSpan;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;


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
        progressDialog = new ProgressDialog (FeedbackActivity.this);
    }
    
    private void initView () {
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvSubmit = (TextView) findViewById (R.id.tvSubmit);
        etName = (EditText) findViewById (R.id.etName);
        etEmail = (EditText) findViewById (R.id.etEmail);
        etFeedback = (EditText) findViewById (R.id.etFeedback);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
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
    
    
        etName.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etName.setError (null);
                }
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etEmail.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etEmail.setError (null);
                }
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    
        etFeedback.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etFeedback.setError (null);
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
            
                SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_name));
                s.setSpan (new TypefaceSpan (FeedbackActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                s2.setSpan (new TypefaceSpan (FeedbackActivity.this, Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_feedback));
                s3.setSpan (new TypefaceSpan (FeedbackActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            
                if (etName.getText ().toString ().trim ().length () == 0 && etEmail.getText ().toString ().length () == 0 && etFeedback.getText ().toString ().length () == 0) {
                    etName.setError (s);
                    etEmail.setError (s2);
                    etFeedback.setError (s3);
                } else if (etName.getText ().toString ().trim ().length () == 0) {
                    etName.setError (s);
                } else if (etEmail.getText ().toString ().trim ().length () == 0) {
                    etEmail.setError (s2);
                } else if (etFeedback.getText ().toString ().trim ().length () == 0) {
                    etFeedback.setError (s3);
                } else {
                    Utils.hideSoftKeyboard (FeedbackActivity.this);
                    sendCredentialsToServer (etName.getText ().toString (), etEmail.getText ().toString (), etFeedback.getText ().toString ());
                
                }
            }
        });
    
    }
    
    
    private void sendCredentialsToServer (final String name, final String email, final String feedback) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FEEDBACK, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_FEEDBACK,
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
                                        MaterialDialog dialog = new MaterialDialog.Builder (FeedbackActivity.this)
                                                .limitIconToDefaultSize ()
                                                .content (message)
                                                .positiveText ("ok")
                                                .canceledOnTouchOutside (false)
                                                .typeface (SetTypeFace.getTypeface (FeedbackActivity.this), SetTypeFace.getTypeface (FeedbackActivity.this))
                                                .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                    @Override
                                                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        finish ();
                                                        
                                                    }
                                                }).build ();
                                        dialog.show ();
                                        
                                        
                                    } else {
                                        Utils.showSnackBar (FeedbackActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (FeedbackActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (FeedbackActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (FeedbackActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    
                    params.put (AppConfigTags.NAME, name);
                    params.put (AppConfigTags.EMAIL, email);
                    params.put (AppConfigTags.FEEDBACK, feedback);
                    
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (FeedbackActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (FeedbackActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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



