package com.referex.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by l on 25/07/2017.
 */

public class UploadResumeActivity extends AppCompatActivity implements TagsEditText.TagsEditListener {
    
    public static final String UPLOAD_URL = "https://project-referex-cammy92.c9users.io/api/upload_new.php";
    public static final String PDF_FETCH_URL = "http://internetfaqs.net/AndroidPdfUpload/getPdfs.php";
    private static final int STORAGE_PERMISSION_CODE = 123;
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
    private int PICK_PDF_REQUEST = 1;
    private Uri filePath;
    
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
                // uploadMultipart ();
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
                                uploadProfileDetailsToServer (etName.getText ().toString ().trim (), etEmail.getText ().toString ().trim (),
                                        etMobile.getText ().toString ().trim (), String.valueOf (jobId));
                    
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
        
        String name = tvSelectResume.getText ().toString ().trim ();
        String path = FilePath.getPath (this, filePath);
        if (path == null) {
            Toast.makeText (this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show ();
        } else {
            try {
                String uploadId = UUID.randomUUID ().toString ();
                new MultipartUploadRequest (this, uploadId, UPLOAD_URL)
                        .addFileToUpload (path, "pdf") //Adding file
                        .addParameter ("name", name)//Adding text parameter to the request
                        .setNotificationConfig (new UploadNotificationConfig ())
                        .setMaxRetries (2)
                        .startUpload (); //Starting the upload
            } catch (Exception exc) {
                Toast.makeText (this, exc.getMessage (), Toast.LENGTH_SHORT).show ();
            }
        }
    }
    
    
    private void showFileChooser () {
        Intent intent = new Intent ();
        intent.setType ("application/*");
        intent.addCategory (Intent.CATEGORY_OPENABLE);
        intent.setAction (Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult (Intent.createChooser (intent, "Select a File to Upload"), PICK_PDF_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText (UploadResumeActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show ();
        }
    }
    
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData () != null) {
            filePath = data.getData ();
    
        }
        
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData ();
                    String uriString = uri.toString ();
                    File myFile = new File (uriString);
                    String path = myFile.getAbsolutePath ();
                    String displayName = null;
                    
                    if (uriString.startsWith ("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getContentResolver ().query (uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst ()) {
                                displayName = cursor.getString (cursor.getColumnIndex (OpenableColumns.DISPLAY_NAME));
                                tvFileSelected.setText (displayName);
                                tvFileSelected.setVisibility (View.VISIBLE);
                            } else {
                                tvFileSelected.setVisibility (View.GONE);
                            }
                            
                        } finally {
                            cursor.close ();
                        }
                    } else if (uriString.startsWith ("file://")) {
                        displayName = myFile.getName ();
                        tvSelectResume.setText (displayName);
                        tvFileSelected.setVisibility (View.VISIBLE);
                    } else {
                        tvFileSelected.setVisibility (View.GONE);
                    }
                }
                break;
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
                Toast.makeText (this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show ();
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
    
    
    private void uploadProfileDetailsToServer (final String name, final String email, final String mobile, final String job_id) {
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



