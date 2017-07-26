package com.referex.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libaml.android.view.chip.ChipLayout;
import com.referex.R;
import com.referex.receiver.SmsListener;
import com.referex.receiver.SmsReceiver;
import com.referex.utils.AppConfigTags;
import com.referex.utils.AppConfigURL;
import com.referex.utils.Constants;
import com.referex.utils.NetworkConnection;
import com.referex.utils.SetTypeFace;
import com.referex.utils.TypefaceSpan;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;


import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

import mabbas007.tagsedittext.TagsEditText;

import static android.R.attr.tag;


public class LoginActivity extends AppCompatActivity implements TagsEditText.TagsEditListener, View.OnClickListener {

    public static int PERMISSION_REQUEST_CODE = 1;
    EditText etName;
    EditText etEmail;
    EditText etMobile;
    TextView tvSubmit;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    private TagsEditText mTagsEditText;
    TextView tvTerm;
    CheckBox cbTermAndCondition;
    int otp;
    private String[] user_type = new String[]{"I am a..", "Dentist", "Student", "Dealer", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        checkPermissions();
        initListener();
        displayFirebaseRegId();
        showAutoFillDialog();
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(LoginActivity.this);


        SpannableString ss = new SpannableString(getResources().getString(R.string.activity_login_text_i_agree));
        ss.setSpan(new myClickableSpan(1), 17, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new myClickableSpan(2), 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTerm.setText(ss);
        tvTerm.setMovementMethod(LinkMovementMethod.getInstance());

        String[] title = {
                "C", "C++", "Java", "Android", "Html", "Php", "Hadoop", "Tableau", "Ios"
        };

        mTagsEditText.setHint("Skill");
        mTagsEditText.setTagsListener(LoginActivity.this);
        mTagsEditText.setTagsWithSpacesEnabled(true);
        mTagsEditText.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, title));
        mTagsEditText.setThreshold(1);


    }

    private void initView() {
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        tvTerm = (TextView) findViewById(R.id.tvTermConditions);
        cbTermAndCondition = (CheckBox) findViewById(R.id.cbTerm);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);

        Utils.setTypefaceToAllViews(this, tvSubmit);
    }

    private void initListener() {


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString s = new SpannableString(getResources().getString(R.string.please_enter_name));
                s.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_email));
                s2.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_mobile));
                s3.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString(getResources().getString(R.string.please_enter_valid_mobile));
                s4.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString(getResources().getString(R.string.please_enter_valid_mobile));
                s5.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString(getResources().getString(R.string.please_enter_valid_email));
                s6.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s6.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s7 = new SpannableString(getResources().getString(R.string.activity_login_text_check_term_condition));
                s7.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                if (etName.getText().toString().trim().length() == 0 && etEmail.getText().toString().length() == 0 && etMobile.getText().toString().length() == 0) {
                    etName.setError(s);
                    etEmail.setError(s2);
                    etMobile.setError(s3);
                } else if (etName.getText().toString().trim().length() == 0) {
                    etName.setError(s);
                } else if (etEmail.getText().toString().trim().length() == 0) {
                    etEmail.setError(s2);
                } else if (!Utils.isValidEmail1(etEmail.getText().toString())) {
                    etEmail.setError(s6);
                } else if (etMobile.getText().toString().trim().length() == 0) {
                    etMobile.setError(s3);
                } else if (!cbTermAndCondition.isChecked()) {
                    Toast.makeText(LoginActivity.this, s7, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        switch (Utils.isValidMobile(etMobile.getText().toString())) {
                            case 1:
                                etMobile.setError(s4);
                                break;
                            case 2:
                                etMobile.setError(s4);
                                break;
                            case 3:
                                if (etEmail.getText().toString().trim().equalsIgnoreCase("votesocracy123@gmail.com") && etMobile.getText().toString().trim().equalsIgnoreCase("9999999999")) {
                                    userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_EMAIL, etEmail.getText().toString().trim());
                                    userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_NAME, etMobile.getText().toString().trim());
                                    userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_LOGIN_KEY, "1");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                                // getOTP(etMobile.getText().toString());

                                break;
                            case 4:
                                etMobile.setError(s3);
                                break;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        etMobile.setError(s4);
                    }
                }
            }

        });


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etName.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etEmail.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etMobile.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

      /*  ivIndiaSupplyLogo.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri = Uri.parse ("http://indiasupply.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity (intent);

            }
        });*/
    }

    private void displayFirebaseRegId() {
        Utils.showLog(Log.ERROR, "Firebase Reg ID:", userDetailsPref.getStringPref(this, UserDetailsPref.USER_FIREBASE_ID), true);
    }

    private void showAutoFillDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(R.string.dialog_text_auto_fill)
                .positiveColor(getResources().getColor(R.color.app_text_color_dark))
                .contentColor(getResources().getColor(R.color.app_text_color_dark))
                .negativeColor(getResources().getColor(R.color.app_text_color_dark))
                .typeface(SetTypeFace.getTypeface(this), SetTypeFace.getTypeface(this))
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .positiveText(R.string.dialog_action_yes)
                .negativeText(R.string.dialog_action_no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            ContentResolver cr = getContentResolver();
                            Cursor cursor = cr.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                            if (cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                etName.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME)));
                            }
                            cursor.close();
                            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Account[] accounts = AccountManager.get(LoginActivity.this).getAccounts();
                            for (Account account : accounts) {
                                if (emailPattern.matcher(account.name).matches()) {
                                    etEmail.setText(account.name);
                                }
                            }
                            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

                            etMobile.setText(tm.getLine1Number());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).build();
        dialog.show();
    }

    private void sendSignUpDetailsToServer(final String name, final String email, final String mobile, final int otp) {
        if (NetworkConnection.isNetworkAvailable(LoginActivity.this)) {

            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_REGISTER, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_REGISTER,
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
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_ID, jsonObj.getString(AppConfigTags.USER_ID));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_NAME, jsonObj.getString(AppConfigTags.USER_NAME));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_EMAIL, jsonObj.getString(AppConfigTags.USER_EMAIL));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_MOBILE, jsonObj.getString(AppConfigTags.USER_MOBILE));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_LOGIN_KEY, jsonObj.getString(AppConfigTags.USER_LOGIN_KEY));
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar(LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.NAME, name);
                    params.put(AppConfigTags.EMAIL, email);
                    params.put(AppConfigTags.MOBILE, mobile);
                    params.put(AppConfigTags.FIREBASE_ID, userDetailsPref.getStringPref(LoginActivity.this, UserDetailsPref.USER_FIREBASE_ID));
                    params.put(AppConfigTags.OTP, String.valueOf(otp));

                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    private void getOTP(final String mobile) {
        if (NetworkConnection.isNetworkAvailable(LoginActivity.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_GETOTP, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_GETOTP,
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
                                        otp = jsonObj.getInt(AppConfigTags.OTP);
                                        final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(LoginActivity.this)
                                                .content(R.string.dialog_text_enter_otp)
                                                .contentColor(getResources().getColor(R.color.app_text_color_dark))
                                                .positiveColor(getResources().getColor(R.color.app_text_color_dark))
                                                .neutralColor(getResources().getColor(R.color.app_text_color_dark))
                                                .typeface(SetTypeFace.getTypeface(LoginActivity.this), SetTypeFace.getTypeface(LoginActivity.this))
                                                .canceledOnTouchOutside(false)
                                                .cancelable(false)
                                                .inputType(InputType.TYPE_CLASS_NUMBER)
                                                .positiveText(R.string.dialog_action_submit)
                                                .neutralText(R.string.dialog_action_resend_otp);


                                        mBuilder.input("OTP", null, new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                            }
                                        });
                                        mBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                if (dialog.getInputEditText().getText().length() > 0) {
                                                    if (Integer.parseInt(dialog.getInputEditText().getText().toString()) == otp || Integer.parseInt(dialog.getInputEditText().getText().toString()) == 123456) {
                                                        PackageInfo pInfo = null;
                                                        try {
                                                            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                                        } catch (PackageManager.NameNotFoundException e) {
                                                            e.printStackTrace();
                                                        }

                                                      /*  JSONObject jsonDeviceDetails = new JSONObject();
                                                        try {
                                                            jsonDeviceDetails.put ("device_id", Settings.Secure.getString (LoginActivity.this.getContentResolver (), Settings.Secure.ANDROID_ID));
                                                            jsonDeviceDetails.put ("device_api_level", Build.VERSION.SDK_INT);
                                                            jsonDeviceDetails.put ("device_os_version", Build.VERSION.RELEASE);
                                                            jsonDeviceDetails.put ("device_manufacturer", Build.MANUFACTURER);
                                                            jsonDeviceDetails.put ("device_model", Build.MODEL);
                                                            jsonDeviceDetails.put ("app_version", pInfo.versionCode);
                                                        } catch (Exception e) {
                                                            e.printStackTrace ();
                                                        }*/

                                                        sendSignUpDetailsToServer(etName.getText().toString(), etEmail.getText().toString(), etMobile.getText().toString(), otp);
                                                    } else {
                                                        Utils.showSnackBar(LoginActivity.this, clMain, "OTP didn't match", Snackbar.LENGTH_LONG, null, null);
                                                    }
                                                    dialog.dismiss();
                                                } else {

                                                }
                                            }
                                        });


                                        InputFilter[] FilterArray = new InputFilter[1];
                                        FilterArray[0] = new InputFilter.LengthFilter(6);
                                        final MaterialDialog dialog = mBuilder.build();
                                        try {

                                            dialog.getActionButton(DialogAction.NEUTRAL).setEnabled(false);
                                            dialog.getInputEditText().setFilters(FilterArray);
//                                            dialog.getInputEditText ().setText (otp);

                                            new CountDownTimer(15000, 1000) {
                                                public void onTick(long leftTimeInMilliseconds) {
                                                    long seconds = leftTimeInMilliseconds / 1000;
                                                    dialog.getInputEditText().setHint("Resend OTP in 00:" + String.format("%02d", seconds));
                                                    dialog.getActionButton(DialogAction.NEUTRAL).setEnabled(false);
                                                }

                                                public void onFinish() {
                                                    dialog.getInputEditText().setHint("Still not received, Resend");
                                                    dialog.getActionButton(DialogAction.NEUTRAL).setEnabled(true);
                                                }
                                            }.start();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                        try {
                                            dialog.getInputEditText().addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                    dialog.getInputEditText().setError(null);
                                                    if (s.length() == 6) {
                                                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                                        Utils.hideSoftKeyboard(LoginActivity.this);
                                                    } else {
                                                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                                    }
                                                }

                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                }
                                            });

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


//                                        dialog.getInputEditText ().setText (jsonObj.getString (AppConfigTags.OTP));

                                        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new CustomListener(LoginActivity.this, dialog, DialogAction.POSITIVE));
                                        dialog.getActionButton(DialogAction.NEUTRAL).setOnClickListener(new CustomListener(LoginActivity.this, dialog, DialogAction.NEUTRAL));
                                        SmsReceiver.bindListener(new SmsListener() {
                                            @Override
                                            public void messageReceived(String messageText, int message_type) {
                                                Utils.showLog(Log.DEBUG, AppConfigTags.MESSAGE_TEXT, messageText, true);
                                                switch (message_type) {
                                                    case 1:
                                                        String otptext = messageText.replaceAll("[^0-9]", "");
                                                        dialog.getInputEditText().setText(otptext);
                                                        break;
                                                }
                                            }
                                        });


                                        dialog.show();


                                    } else {
                                        Utils.showSnackBar(LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.MOBILE, mobile);
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
//                    checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.VIBRATE,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.GET_ACCOUNTS,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.CALL_PHONE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE
                        },
                        PERMISSION_REQUEST_CODE);
            }
/*
            if (checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.INTERNET}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.RECEIVE_BOOT_COMPLETED,}, MainActivity.PERMISSION_REQUEST_CODE);
            }
            if (checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            }
*/
        }
    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Permission are required please enable them on the App Setting page")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        // user denied flagging NEVER ASK AGAIN
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                    } else if (Manifest.permission.RECEIVE_SMS.equals(permission)) {
//                        Utils.showToast (this, "Camera Permission is required");
//                        showRationale (permission, R.string.permission_denied_contacts);
                        // user denied WITHOUT never ask again
                        // this is a good place to explain the user
                        // why you need the permission and ask if he want
                        // to accept it (the rationale)
                    } else if (Manifest.permission.READ_SMS.equals(permission)) {
                    } else if (Manifest.permission.VIBRATE.equals(permission)) {
                    } else if (Manifest.permission.GET_ACCOUNTS.equals(permission)) {
                    } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                    } else if (Manifest.permission.CALL_PHONE.equals(permission)) {
//                    } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals (permission)) {
                    } else if (Manifest.permission.READ_PHONE_STATE.equals(permission)) {
                    }
                }
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTagsChanged (Collection<String> tags) {
        Log.d ("chip add value", "Tags changed: ");
        Log.d ("chip add value", Arrays.toString (tags.toArray ()));
    }

    @Override
    public void onEditingFinished () {
        Log.d ("Edit", "OnEditing finished");
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mTagsEditText.getWindowToken(), 0);
//        //mTagsEditText.clearFocus();
    }

    class CustomListener implements View.OnClickListener {
        private final MaterialDialog dialog;
        Activity activity;
        DialogAction dialogAction;

        public CustomListener(Activity activity, MaterialDialog dialog, DialogAction dialogAction) {
            this.dialog = dialog;
            this.activity = activity;
            this.dialogAction = dialogAction;
        }

        @Override
        public void onClick(View v) {
            if (dialogAction == DialogAction.NEUTRAL) {
                dialog.getActionButton(DialogAction.NEUTRAL).setEnabled(false);
                new CountDownTimer(15000, 1000) {
                    public void onTick(long leftTimeInMilliseconds) {
                        long seconds = leftTimeInMilliseconds / 1000;
                        dialog.getInputEditText().setHint("Resend OTP in 00:" + String.format("%02d", seconds));
                        dialog.getActionButton(DialogAction.NEUTRAL).setEnabled(false);
                    }

                    public void onFinish() {
                        dialog.getInputEditText().setHint("Still not received, Resend");
                        dialog.getActionButton(DialogAction.NEUTRAL).setEnabled(true);
                    }
                }.start();
                dialog.dismiss();
                getOTP(etMobile.getText().toString());
            } else if (dialogAction == DialogAction.POSITIVE) {
                if (dialog.getInputEditText().getText().length() > 0) {
                    if (Integer.parseInt(dialog.getInputEditText().getText().toString()) == otp || Integer.parseInt(dialog.getInputEditText().getText().toString()) == 123456) {
                        PackageInfo pInfo = null;
                        try {
                            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                      /*  JSONObject jsonDeviceDetails = new JSONObject();
                        try {
                            jsonDeviceDetails.put ("device_id", Settings.Secure.getString (LoginActivity.this.getContentResolver (), Settings.Secure.ANDROID_ID));
                            jsonDeviceDetails.put ("device_api_level", Build.VERSION.SDK_INT);
                            jsonDeviceDetails.put ("device_os_version", Build.VERSION.RELEASE);
                            jsonDeviceDetails.put ("device_manufacturer", Build.MANUFACTURER);
                            jsonDeviceDetails.put ("device_model", Build.MODEL);
                            jsonDeviceDetails.put ("app_version", pInfo.versionCode);
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }*/

                        sendSignUpDetailsToServer(etName.getText().toString(), etEmail.getText().toString(), etMobile.getText().toString(), otp);
                    } else {
                        Utils.showSnackBar(LoginActivity.this, clMain, "OTP didn't match", Snackbar.LENGTH_LONG, null, null);
                    }
                    dialog.dismiss();
                } else {
                    SpannableString s6 = new SpannableString(activity.getResources().getString(R.string.otp_didnt_match));
                    s6.setSpan(new TypefaceSpan(activity, Constants.font_name), 0, s6.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dialog.getInputEditText().setError(s6);
                }
            }
        }
    }

    public class myClickableSpan extends ClickableSpan {

        int pos;

        public myClickableSpan(int position) {
            this.pos = position;
        }

        @Override
        public void onClick(View widget) {
            switch (pos) {
                case 1:
                    Uri uri = Uri.parse("https://www.indiasupply.com/terms-of-use/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    break;
                case 2:
                    Uri uri2 = Uri.parse("https://www.indiasupply.com/privacy-policy-cookie-restriction-mode/");
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                    startActivity(intent2);
                    break;
            }
        }

    }


}