package com.referex.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.referex.R;
import com.referex.model.Skill;
import com.referex.utils.AppConfigTags;
import com.referex.utils.AppConfigURL;
import com.referex.utils.Constants;
import com.referex.utils.FlowLayout;
import com.referex.utils.NetworkConnection;
import com.referex.utils.SetTypeFace;
import com.referex.utils.TypefaceSpan;
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
 * Created by l on 25/09/2017.
 */

public class SignUpFragment extends Fragment {

    public static int PERMISSION_REQUEST_CODE = 1;
    EditText etName;
    EditText etEmail;
    EditText etMobile;
    TextView tvSubmit;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    TextView tvTerm;
    CheckBox cbTermAndCondition;
    int otp;
    ArrayList<String> skillsArrayList = new ArrayList<String>();
    ArrayList<String> skillsSelectedArrayList = new ArrayList<String>();
    List<Skill> skillList = new ArrayList<>();
    FlowLayout chipsBoxLayout;
    Button btAddSkills;
    TextView tvNoSkills;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initView(rootView);
        initData();
        initListener();
        displayFirebaseRegId();
        getSkillList();

        return rootView;
    }



    private void initView(View rootView) {
        clMain = (CoordinatorLayout) rootView.findViewById(R.id.clMain);
        etName = (EditText) rootView.findViewById(R.id.etName);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etMobile = (EditText) rootView.findViewById(R.id.etMobile);
        tvTerm = (TextView) rootView.findViewById(R.id.tvTermConditions);
        cbTermAndCondition = (CheckBox) rootView.findViewById(R.id.cbTerm);
        tvSubmit = (TextView) rootView.findViewById(R.id.tvSubmit);
        tvNoSkills = (TextView) rootView.findViewById(R.id.tvNoSkills);
        btAddSkills = (Button) rootView.findViewById(R.id.btAddSkills);
        Utils.setTypefaceToAllViews(getActivity(), tvSubmit);
        chipsBoxLayout = (FlowLayout) rootView.findViewById(R.id.chips_box_layout);

    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(getActivity());


        // skillsArrayList.addAll (Arrays.asList (new String[] {"C", "C++", "Java", "Android", "HTML", "PHP", "Hadoop", "Tableau", "iOS"}));

        SpannableString ss = new SpannableString(getResources().getString(R.string.activity_login_text_i_agree));
        ss.setSpan(new myClickableSpan(1), 17, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new myClickableSpan(2), 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTerm.setText(ss);
        tvTerm.setMovementMethod(LinkMovementMethod.getInstance());



    }

    private void initListener() {


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString s = new SpannableString(getResources().getString(R.string.please_enter_name));
                s.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_email));
                s2.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_mobile));
                s3.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString(getResources().getString(R.string.please_enter_valid_mobile));
                s4.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString(getResources().getString(R.string.please_enter_valid_mobile));
                s5.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString(getResources().getString(R.string.please_enter_valid_email));
                s6.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s6.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s7 = new SpannableString(getResources().getString(R.string.activity_login_text_check_term_condition));
                s7.setSpan(new TypefaceSpan(getActivity(), Constants.font_name), 0, s7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
                    Toast.makeText(getActivity(), s7, Toast.LENGTH_LONG).show();
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
//

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

                                sendSignUpDetailsToServer(etName.getText().toString().trim(), etEmail.getText().toString().trim(),
                                        etMobile.getText().toString().trim(), jsonDeviceDetails.toString());

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

        btAddSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillsArrayList.clear();
                String skill_id = "";
                ArrayList<Integer> skillPositionList = new ArrayList<Integer>();
                for (int i = 0; i < skillsSelectedArrayList.size(); i++) {
                    for (int j = 0; j < skillList.size(); j++) {
                        if (skillsSelectedArrayList.get(i).equalsIgnoreCase(skillList.get(j).getSkill_name())) {
                            skillPositionList.add(j);
                            if (j == 0) {
                                skill_id = String.valueOf(skillList.get(j).getId());
                            } else {
                                skill_id = skill_id + "," + String.valueOf(skillList.get(j).getId());
                            }

                            Log.e("skill", skill_id);




                        }
                    }
                }

                Integer[] ints = new Integer[skillPositionList.size()];
                int i = 0;
                for (Integer n : skillPositionList) {
                    ints[i++] = n;
                }


                for (int k = 0; k < skillList.size(); k++) {
                    skillsArrayList.add(skillList.get(k).getSkill_name());
                }


                new MaterialDialog.Builder(getActivity())
                        .title("Skills")
                        .items(skillsArrayList)
                        .typeface(SetTypeFace.getTypeface(getActivity()), SetTypeFace.getTypeface(getActivity()))
                        .itemsCallbackMultiChoice(ints, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(5, 5, 5, 5);
                                chipsBoxLayout.removeAllViews();
                                skillsSelectedArrayList.clear();
                                if (text.length > 0) {
                                    tvNoSkills.setVisibility(View.GONE);
                                    btAddSkills.setText("ADD/EDIT");
                                } else {
                                    tvNoSkills.setVisibility(View.VISIBLE);
                                    btAddSkills.setText("ADD");
                                }
                                for (int i = 0; i < text.length; i++) {
                                    skillsSelectedArrayList.add(skillList.get(i).getSkill_name());
                                    final TextView t = new TextView(getActivity());
                                    t.setLayoutParams(params);
                                    t.setPadding(8, 8, 8, 8);
                                    t.setTypeface(SetTypeFace.getTypeface(getActivity()));
                                    t.setText(text[i]);
                                    t.setTextColor(Color.WHITE);
                                    t.setBackgroundResource(R.drawable.square);
                                    chipsBoxLayout.addView(t);
                                }
                                return true;
                            }
                        })
                        .neutralText("CLEAR")
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.clearSelectedIndices();
                            }
                        })
                        .positiveText("SELECT")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .autoDismiss(false)
                        .alwaysCallInputCallback()
                        .show();
            }
        });

    }



    private void displayFirebaseRegId() {
        Utils.showLog(Log.ERROR, "Firebase Reg ID:", userDetailsPref.getStringPref(getActivity(), UserDetailsPref.USER_FIREBASE_ID), true);

    }

    private void sendSignUpDetailsToServer(final String name, final String email, final String mobile, final String device_details) {
        if (NetworkConnection.isNetworkAvailable(getActivity())) {

            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SIGN_UP, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_SIGN_UP,
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

                                        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                                                .limitIconToDefaultSize()
                                                .content(message)
                                                .positiveText("ok")
                                                .canceledOnTouchOutside(false)
                                                .typeface(SetTypeFace.getTypeface(getActivity()), SetTypeFace.getTypeface(getActivity()))
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        getActivity().finish();

                                                    }
                                                }).build();
                                        dialog.show();


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
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.NAME, name);
                    params.put(AppConfigTags.EMAIL, email);
                    params.put(AppConfigTags.MOBILE, mobile);
                    params.put(AppConfigTags.SKILLS, "1,2");
                    params.put(AppConfigTags.FIREBASE_ID, userDetailsPref.getStringPref(getActivity(), UserDetailsPref.USER_FIREBASE_ID));
                    params.put(AppConfigTags.DEVICE_DETAILS, device_details);

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
            Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    private void getSkillList() {
        if (NetworkConnection.isNetworkAvailable(getActivity())) {
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SKILL, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.GET, AppConfigURL.URL_SKILL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            skillList.clear();
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {

                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        JSONArray jsonArray = jsonObj.getJSONArray(AppConfigTags.SKILLS);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            skillList.add(new Skill(
                                                    jsonObject.getInt(AppConfigTags.SKILL_ID),
                                                    0,
                                                    jsonObject.getString(AppConfigTags.SKILL_NAME),
                                                    true));
                                        }


                                    } else {
                                        // Utils.showSnackBar (getActivity(), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                            } else {

                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
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
