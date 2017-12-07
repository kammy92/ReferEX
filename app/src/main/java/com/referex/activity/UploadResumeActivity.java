package com.referex.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.referex.utils.AppConfigTags;
import com.referex.utils.AppConfigURL;
import com.referex.utils.Constants;
import com.referex.utils.FilePath;
import com.referex.utils.FlowLayout;
import com.referex.utils.NetworkConnection;
import com.referex.utils.SetTypeFace;
import com.referex.utils.TypefaceSpan;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import mabbas007.tagsedittext.TagsEditText;

public class UploadResumeActivity extends AppCompatActivity implements TagsEditText.TagsEditListener {
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName ();
    TextView tvUploadResume;
    TextView tvTitle;
    TextView tvSelectResume;
    EditText etName;
    EditText etEmail;
    EditText etMobile;
    TextView tvJobPosition;
    TextView tvNoSkills;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    String jobPosition;
    int jobId;
    RelativeLayout rlBack;
    TextView tvFileSelected;
    Button btAddSkills;
    FlowLayout chipsBoxLayout;
    ArrayList<String> skillsArrayList = new ArrayList<String> ();
    ArrayList<String> skillsSelectedArrayList = new ArrayList<String> ();
    String FileName = "";
    private int PICK_PDF_REQUEST = 1;
    private String selectedFilePath;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_resume);
        initView ();
        initData ();
        initListener ();
        getExtras ();
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        jobPosition = intent.getStringExtra (AppConfigTags.JOB_POSITION);
        jobId = intent.getIntExtra (AppConfigTags.JOB_ID, 0);
        tvJobPosition.setText (jobPosition);
    }
    
    private void initData () {
        requestStoragePermission ();
        progressDialog = new ProgressDialog (this);
        skillsArrayList.addAll (Arrays.asList (new String[] {"C", "C++", "Java", "Android", "HTML", "PHP", "Hadoop", "Tableau", "iOS"}));
        Utils.setTypefaceToAllViews (this, tvSelectResume);
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvUploadResume = (TextView) findViewById (R.id.buttonUpload);
        tvSelectResume = (TextView) findViewById (R.id.editTextName);
        tvJobPosition = (TextView) findViewById (R.id.tvJobPosition);
        etName = (EditText) findViewById (R.id.etName);
        etEmail = (EditText) findViewById (R.id.etEmail);
        etMobile = (EditText) findViewById (R.id.etMobile);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvFileSelected = (TextView) findViewById (R.id.etFileSelected);
        btAddSkills = (Button) findViewById (R.id.btAddSkills);
        tvNoSkills = (TextView) findViewById (R.id.tvNoSkills);
        chipsBoxLayout = (FlowLayout) findViewById (R.id.chips_box_layout);
        
        Utils.setTypefaceToAllViews (this, tvSelectResume);
    }
    
    private void initListener () {
        tvSelectResume.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                showFileChooser ();
    
            }
        });
        tvUploadResume.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
    
                SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_name));
                s.setSpan (new TypefaceSpan (UploadResumeActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                s2.setSpan (new TypefaceSpan (UploadResumeActivity.this, Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_mobile));
                s3.setSpan (new TypefaceSpan (UploadResumeActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString (getResources ().getString (R.string.please_enter_valid_mobile));
                s4.setSpan (new TypefaceSpan (UploadResumeActivity.this, Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString (getResources ().getString (R.string.please_enter_valid_mobile));
                s5.setSpan (new TypefaceSpan (UploadResumeActivity.this, Constants.font_name), 0, s5.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s6.setSpan (new TypefaceSpan (UploadResumeActivity.this, Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                if (etName.getText ().toString ().trim ().length () == 0 && etEmail.getText ().toString ().length () == 0 && etMobile.getText ().toString ().length () == 0) {
                    etName.setError (s);
                    etEmail.setError (s2);
                    etMobile.setError (s3);
                } else if (etName.getText ().toString ().trim ().length () == 0) {
                    etName.setError (s);
                } else if (etEmail.getText ().toString ().trim ().length () == 0) {
                    etEmail.setError (s2);
                } else if (! Utils.isValidEmail1 (etEmail.getText ().toString ())) {
                    etEmail.setError (s6);
                } else if (etMobile.getText ().toString ().trim ().length () == 0) {
                    etMobile.setError (s3);
                } else {
                    try {
                        switch (Utils.isValidMobile (etMobile.getText ().toString ())) {
                            case 1:
                                etMobile.setError (s4);
                                break;
                            case 2:
                                etMobile.setError (s4);
                                break;
                            case 3:
                                uploadMultipart ();
                                break;
                            case 4:
                                etMobile.setError (s3);
                                break;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace ();
                        etMobile.setError (s4);
                    }
                }
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
        etMobile.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etMobile.setError (null);
                }
            }
    
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
    
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btAddSkills.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ArrayList<Integer> skillPositionList = new ArrayList<Integer> ();
                for (int i = 0; i < skillsSelectedArrayList.size (); i++) {
                    for (int j = 0; j < skillsArrayList.size (); j++) {
                        if (skillsSelectedArrayList.get (i).equalsIgnoreCase (skillsArrayList.get (j))) {
                            skillPositionList.add (j);
                        }
                    }
                }
                Integer[] ints = new Integer[skillPositionList.size ()];
                int i = 0;
                for (Integer n : skillPositionList) {
                    ints[i++] = n;
                }
                new MaterialDialog.Builder (UploadResumeActivity.this)
                        .title ("Skills")
                        .items (skillsArrayList)
                        .typeface (SetTypeFace.getTypeface (UploadResumeActivity.this), SetTypeFace.getTypeface (UploadResumeActivity.this))
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                chipsBoxLayout.removeAllViews ();
                                skillsSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    tvNoSkills.setVisibility (View.GONE);
                                    btAddSkills.setText ("ADD/EDIT");
                                } else {
                                    tvNoSkills.setVisibility (View.VISIBLE);
                                    btAddSkills.setText ("ADD");
                                }
                                for (int i = 0; i < text.length; i++) {
                                    skillsSelectedArrayList.add (text[i].toString ());
                                    final TextView t = new TextView (UploadResumeActivity.this);
                                    t.setLayoutParams (params);
                                    t.setTypeface (SetTypeFace.getTypeface (UploadResumeActivity.this));
                                    t.setPadding (8, 8, 8, 8);
                                    t.setText (text[i]);
                                    t.setTextColor (Color.WHITE);
//                                    t.setCompoundDrawablePadding (10);
//                                    final int finalI = i;
//                                    t.setOnClickListener (new View.OnClickListener () {
//                                        @Override
//                                        public void onClick (View view) {
//                                            chipsBoxLayout.removeViewAt (finalI);
//                                        }
//                                    });
//                                    t.setCompoundDrawablesWithIntrinsicBounds (0, 0, R.drawable.ic_cancel, 0);
                                    t.setBackgroundResource (R.drawable.square);
                                    chipsBoxLayout.addView (t);
                                }
                                return true;
                            }
                        })
                        .neutralText ("CLEAR")
                        .onNeutral (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.clearSelectedIndices ();
                            }
                        })
                        .positiveText ("SELECT")
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss ();
                            }
                        })
                        .autoDismiss (false)
                        .alwaysCallInputCallback ()
                        .show ();
            }
        });
    }
    
    public void uploadMultipart () {
        if (selectedFilePath != null) {
            Utils.showProgressDialog (progressDialog, "Uploading Resume...", true);
//            progressDialog = ProgressDialog.show (UploadResumeActivity.this, "", "Uploading File...", true);
            new Thread (new Runnable () {
                @Override
                public void run () {
                    //creating new thread to handle Http Operations
                    uploadFile (selectedFilePath);
                }
            }).start ();
        } else {
            uploadProfileDetailsToServer (etName.getText ().toString ().trim (), etEmail.getText ().toString ().trim (), etMobile.getText ().toString ().trim (), String.valueOf (jobId), "");
        }
    }
    
    private void showFileChooser () {
        Intent intent = new Intent ();
        //sets the select file to all types of files
        intent.setType ("*/*");
        //allows to select data and return it
        intent.setAction (Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        try {
            startActivityForResult (Intent.createChooser (intent, "Select a File to Upload"), PICK_PDF_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText (UploadResumeActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show ();
        }
        
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
    
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }
                Uri selectedFileUri = data.getData ();
                selectedFilePath = FilePath.getPath (this, selectedFileUri);
                Log.i (TAG, "Selected File Path:" + selectedFilePath);
    
                if (selectedFilePath != null && ! selectedFilePath.equals ("")) {
                    String[] parts = selectedFilePath.split ("/");
                    final String fileName = parts[parts.length - 1];
                    tvFileSelected.setText (fileName);
        
                    tvFileSelected.setVisibility (View.VISIBLE);
                } else {
                    tvFileSelected.setVisibility (View.GONE);
                    Toast.makeText (this, "Cannot upload file to server", Toast.LENGTH_SHORT).show ();
                }
            }
        }
    }
    
    
    public int uploadFile (final String selectedFilePath) {
        
        int serverResponseCode = 0;
        
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        
        
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File (selectedFilePath);
        
        
        String[] parts = selectedFilePath.split ("/");
        final String fileName = parts[parts.length - 1];
        
        if (! selectedFile.isFile ()) {
            progressDialog.dismiss ();
            
            runOnUiThread (new Runnable () {
                @Override
                public void run () {
                    tvFileSelected.setText ("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream (selectedFile);
                URL url = new URL (AppConfigURL.UPLOAD_URL);
                connection = (HttpURLConnection) url.openConnection ();
                connection.setDoInput (true);//Allow Inputs
                connection.setDoOutput (true);//Allow Outputs
                connection.setUseCaches (false);//Don't use a cached Copy
                connection.setRequestMethod ("POST");
                connection.setRequestProperty ("Connection", "Keep-Alive");
                connection.setRequestProperty ("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty ("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty ("uploaded_file", selectedFilePath);
                
                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream (connection.getOutputStream ());
                
                //writing bytes to data outputstream
                dataOutputStream.writeBytes (twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes ("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);
                
                dataOutputStream.writeBytes (lineEnd);
                
                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available ();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min (bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];
                
                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read (buffer, 0, bufferSize);
                
                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write (buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available ();
                    bufferSize = Math.min (bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read (buffer, 0, bufferSize);
                }
                
                dataOutputStream.writeBytes (lineEnd);
                dataOutputStream.writeBytes (twoHyphens + boundary + twoHyphens + lineEnd);
                
                serverResponseCode = connection.getResponseCode ();
                String serverResponseMessage = connection.getResponseMessage ();
                
                BufferedReader br = new BufferedReader (new InputStreamReader ((connection.getInputStream ())));
                StringBuilder sb = new StringBuilder ();
                String output;
                while ((output = br.readLine ()) != null) {
                    sb.append (output);
                }
                
                try {
                    JSONObject jsonObject = new JSONObject (sb.toString ());
                    FileName = jsonObject.getString ("file_name");
                    Log.i ("RenameFile", jsonObject.getString ("file_name"));
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
                
                Log.i (TAG, "Server Response is: " + sb.toString () + serverResponseMessage + ": " + serverResponseCode);
                if (serverResponseCode == 200) {
                    runOnUiThread (new Runnable () {
                        @Override
                        public void run () {
                            uploadProfileDetailsToServer (etName.getText ().toString ().trim (), etEmail.getText ().toString ().trim (),
                                    etMobile.getText ().toString ().trim (), String.valueOf (jobId), FileName);
                
                            tvFileSelected.setText (fileName);
                        }
                    });
                } else {
                    progressDialog.dismiss ();
                }
                
                //closing the input and output streams
                fileInputStream.close ();
                dataOutputStream.flush ();
                dataOutputStream.close ();
            } catch (FileNotFoundException e) {
                e.printStackTrace ();
                Toast.makeText (UploadResumeActivity.this, "File Not Found", Toast.LENGTH_SHORT).show ();
                progressDialog.dismiss ();
            } catch (MalformedURLException e) {
                e.printStackTrace ();
                Toast.makeText (UploadResumeActivity.this, "URL error!", Toast.LENGTH_SHORT).show ();
                progressDialog.dismiss ();
            } catch (IOException e) {
                e.printStackTrace ();
                Toast.makeText (UploadResumeActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show ();
                progressDialog.dismiss ();
            }
            return serverResponseCode;
        }
    }
    
    
    //Requesting permission
    private void requestStoragePermission () {
        if (ContextCompat.checkSelfPermission (this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale (this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions (this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText (this, "Permission granted now you can upload resume", Toast.LENGTH_LONG).show ();
            } else {
                Toast.makeText (this, "Oops you just denied the permission", Toast.LENGTH_LONG).show ();
            }
        }
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
    
    private void uploadProfileDetailsToServer (final String name, final String email, final String mobile, final String job_id, final String fileName) {
        if (NetworkConnection.isNetworkAvailable (UploadResumeActivity.this)) {
            
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SUBMIT_PROFILE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SUBMIT_PROFILE,
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
    
                                        MaterialDialog dialog = new MaterialDialog.Builder (UploadResumeActivity.this)
                                                .limitIconToDefaultSize ()
                                                .content (message)
                                                .positiveText ("ok")
                                                .canceledOnTouchOutside (false)
                                                .typeface (SetTypeFace.getTypeface (UploadResumeActivity.this), SetTypeFace.getTypeface (UploadResumeActivity.this))
                                                .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                    @Override
                                                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        finish ();
    
                                                    }
                                                }).build ();
                                        dialog.show ();
    
    
                                    } else {
                                        Utils.showSnackBar (UploadResumeActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (UploadResumeActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (UploadResumeActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (UploadResumeActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.NAME, name);
                    params.put (AppConfigTags.EMAIL, email);
                    params.put (AppConfigTags.MOBILE, mobile);
                    params.put (AppConfigTags.JOB_ID, job_id);
                    params.put (AppConfigTags.RESUME, fileName);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (UploadResumeActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (UploadResumeActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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