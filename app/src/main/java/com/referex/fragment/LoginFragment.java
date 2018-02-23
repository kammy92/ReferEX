package com.referex.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.referex.R;
import com.referex.activity.MainActivity;
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
 * Created by l on 25/09/2017.
 */

public class LoginFragment extends Fragment {

    EditText etUserName;
    EditText etPassword;
    TextView tvForgotPassword;
    TextView tvSignIn;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    PackageInfo info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(rootView);
        initData();
        initListener();
        displayFirebaseRegId();
        return rootView;
    }

    private void initView(View rootView) {
        etUserName = (EditText) rootView.findViewById(R.id.etUserName);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        tvSignIn = (TextView) rootView.findViewById(R.id.tvSignIn);
        tvForgotPassword = (TextView) rootView.findViewById(R.id.tvForgotPassword);
        clMain = (CoordinatorLayout) rootView.findViewById(R.id.clMain);
        Utils.setTypefaceToAllViews(getActivity(), tvSignIn);

    }

    private void displayFirebaseRegId() {
        Utils.showLog(Log.ERROR, "Firebase Reg ID:", userDetailsPref.getStringPref(getActivity(), UserDetailsPref.USER_FIREBASE_ID), true);
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        userDetailsPref = UserDetailsPref.getInstance();

    }

    private void initListener() {
    
        tvForgotPassword.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                if (event.getAction () == MotionEvent.ACTION_DOWN) {
                    tvForgotPassword.setTextColor (getResources ().getColor (R.color.secondary_text));
                } else if (event.getAction () == MotionEvent.ACTION_UP) {
                    tvForgotPassword.setTextColor (getResources ().getColor (R.color.text_color_white));
                    showForgotPasswordDialog ();
                }
                return true;
            }
        
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etUserName.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etPassword.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(getActivity());
                SpannableString s1 = new SpannableString(getResources().getString(R.string.please_enter_name));
                s1.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_password));
                s3.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (etUserName.getText().toString().trim().length() == 0 && etPassword.getText().toString().length() == 0) {
                    etUserName.setError(s1);
                    etPassword.setError(s3);
                } else if (etUserName.getText().toString().trim().length() == 0) {
                    etUserName.setError(s1);
                } else if (etPassword.getText().toString().trim().length() == 0) {
                    etPassword.setError(s3);
                } else {


                    PackageInfo pInfo = null;
                    try {
                        pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonDeviceDetails = new JSONObject();
                    try {
                        jsonDeviceDetails.put("device_id", Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
                        jsonDeviceDetails.put("device_api_level", Build.VERSION.SDK_INT);
                        jsonDeviceDetails.put("device_os_version", Build.VERSION.RELEASE);
                        jsonDeviceDetails.put("device_manufacturer", Build.MANUFACTURER);
                        jsonDeviceDetails.put("device_model", Build.MODEL);
                        jsonDeviceDetails.put("app_version_code", pInfo.versionCode);
                        jsonDeviceDetails.put("app_version_name", pInfo.versionName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sendLoginCredentialsToServer(etUserName.getText().toString().trim(), etPassword.getText().toString().trim(), jsonDeviceDetails.toString());
                 /*   userDetailsPref.putStringPref (getActivity(), UserDetailsPref.USER_LOGIN_KEY, "1");
                    Intent intent = new Intent (getActivity (), MainActivity.class);
                    intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity (intent);
                    getActivity ().overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);*/
                }
            }
        });

    }
    
    private void sendLoginCredentialsToServer(final String name, final String password, final String jsonDeviceDetails) {
        if (NetworkConnection.isNetworkAvailable(getActivity())) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_LOGIN, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_LOGIN,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
    
                                        userDetailsPref.putStringPref(getActivity(), UserDetailsPref.USER_NAME, jsonObj.getString(AppConfigTags.USER_NAME));
                                        userDetailsPref.putStringPref(getActivity(), UserDetailsPref.USER_EMAIL, jsonObj.getString(AppConfigTags.USER_EMAIL));
                                        userDetailsPref.putStringPref(getActivity(), UserDetailsPref.USER_MOBILE, jsonObj.getString(AppConfigTags.USER_MOBILE));
                                        userDetailsPref.putStringPref(getActivity(), UserDetailsPref.USER_LOGIN_KEY, jsonObj.getString(AppConfigTags.USER_LOGIN_KEY));
    
    
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar(getActivity(), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
    
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
    
                    params.put(AppConfigTags.USER_NAME, name);
                    params.put(AppConfigTags.DEVICE_DETAILS, jsonDeviceDetails);
                    params.put(AppConfigTags.PASSWORD, Utils.encrypt(password));
                    params.put(AppConfigTags.FIREBASE_ID, userDetailsPref.getStringPref(getActivity(), UserDetailsPref.USER_FIREBASE_ID));
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
    
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
        
        
    }
    
    private void showForgotPasswordDialog () {
        final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (getActivity ())
                .content ("Enter your Email Address")
                .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                .inputType (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                .alwaysCallInputCallback ()
                .canceledOnTouchOutside (true)
                .cancelable (true)
                .positiveText ("OK");
        
        mBuilder.input (null, null, new MaterialDialog.InputCallback () {
            @Override
            public void onInput (MaterialDialog dialog, CharSequence input) {
                // Do something
                dialog.getActionButton (DialogAction.POSITIVE).setEnabled (true);
            }
        });
        
        mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
            @Override
            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (dialog.getInputEditText ().getText ().toString ().length () > 0 && Utils.isValidEmail1 (dialog.getInputEditText ().getText ().toString ())) {
                    sendForgotPasswordRequestToServer (dialog.getInputEditText ().getText ().toString ());
                } else {
                    new MaterialDialog.Builder (getActivity ())
                            .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                            .content ("Invalid Email")
                            .positiveText ("OK")
                            .show ();
                }
            }
        });
        MaterialDialog dialog = mBuilder.build ();
        dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
        dialog.show ();
    }
    
    private void sendForgotPasswordRequestToServer (final String user_name) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.FORGOT_PASSWORD, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.FORGOT_PASSWORD,
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
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.USER_EMAIL, user_name);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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
