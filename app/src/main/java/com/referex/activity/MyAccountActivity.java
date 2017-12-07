package com.referex.activity;

import android.app.ProgressDialog;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.referex.R;
import com.referex.adapter.MyAccountAdapter;
import com.referex.fragment.CandidateDetailDialogFragment;
import com.referex.model.MyAccount;
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


public class MyAccountActivity extends AppCompatActivity {
    
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    RecyclerView rvReferralList;
    TextView tvMyReferrals;
    TextView tvViewAll;
    TextView tvName;
    TextView tvEmail;
    RelativeLayout rlBack;
    SwipeRefreshLayout swipeRefreshLayout;
    
    
    MyAccountAdapter myAccountAdapter;
    List<MyAccount> myReferralList = new ArrayList<> ();
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_account);
        initView ();
        initData ();
        initListener ();
        setData ();
        
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (MyAccountActivity.this);
        Utils.setTypefaceToAllViews (this, clMain);
        userDetailsPref = UserDetailsPref.getInstance ();

      /*  myReferralList.add(new MyAccount("Total Pending","2"));
        myReferralList.add(new MyAccount("Total Rejected","1"));
        myReferralList.add(new MyAccount("Total Aborted","1"));
        myReferralList.add(new MyAccount("Total Interview Scheduled","2"));
        myReferralList.add(new MyAccount("Total Offer Made","2"));
        myReferralList.add(new MyAccount("Total Joined","2"));

*/
    
        tvName.setText (userDetailsPref.getStringPref (this, UserDetailsPref.USER_NAME));
        tvEmail.setText (userDetailsPref.getStringPref (this, UserDetailsPref.USER_EMAIL));
        swipeRefreshLayout.setRefreshing (false);
        myAccountAdapter = new MyAccountAdapter (this, myReferralList);
        rvReferralList.setAdapter (myAccountAdapter);
        rvReferralList.setHasFixedSize (true);
        rvReferralList.addItemDecoration (new SimpleDividerItemDecoration (this));
        rvReferralList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvReferralList.setItemAnimator (new DefaultItemAnimator ());
        
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvReferralList = (RecyclerView) findViewById (R.id.rvList);
        tvEmail = (TextView) findViewById (R.id.tvEmail);
        tvName = (TextView) findViewById (R.id.tvName);
        tvViewAll = (TextView) findViewById (R.id.tvViewAll);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
    
    
    }
    
    private void initListener () {
        tvViewAll.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                final android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                CandidateDetailDialogFragment frag = new CandidateDetailDialogFragment ();
                frag.show (ft, "");
                
            }
        });
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                setData ();
            }
        });
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    
    }
    
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.ACCOUNT, true);
            
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.ACCOUNT,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    
                                    
                                    if (! error) {
                                        swipeRefreshLayout.setRefreshing (false);
                                        JSONArray jsonArrayPerformance = jsonObj.getJSONArray (AppConfigTags.PERFORMANCE);
                                        
                                        for (int i = 0; i < jsonArrayPerformance.length (); i++) {
                                            JSONObject jsonObjectPerformance = jsonArrayPerformance.getJSONObject (i);
                                            myReferralList.add (i, new MyAccount (
                                                    jsonObjectPerformance.getString (AppConfigTags.TEXT),
                                                    jsonObjectPerformance.getString (AppConfigTags.VALUE),
                                                    jsonObjectPerformance.getJSONArray (AppConfigTags.CANDIDATES).toString ()
                                            
                                            ));
                                        }
                                        myAccountAdapter.notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (MyAccountActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
            Utils.showSnackBar (MyAccountActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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