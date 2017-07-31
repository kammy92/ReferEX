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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.referex.R;
import com.referex.utils.AppConfigTags;
import com.referex.utils.FilePath;
import com.referex.utils.FlowLayout;
import com.referex.utils.Utils;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    ProgressDialog progressDialog;
    String jobPosition;
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
        tvJobPosition.setText (jobPosition);
    }
    
    private void initData () {
        requestStoragePermission ();
        progressDialog = new ProgressDialog (this);
    
        skillsArrayList.addAll (Arrays.asList (new String[] {"C", "C++", "Java", "Android", "HTML", "PHP", "Hadoop", "Tableau", "iOS"}));
        
        Utils.setTypefaceToAllViews (this, tvSelectResume);
    }
    
    private void initView () {
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
                uploadMultipart ();
                
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
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                chipsBoxLayout.removeAllViews ();
                                skillsSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    btAddSkills.setText ("ADD/EDIT");
                                } else {
                                    btAddSkills.setText ("ADD");
                                }
                                for (int i = 0; i < text.length; i++) {
                                    skillsSelectedArrayList.add (text[i].toString ());
                                    final TextView t = new TextView (UploadResumeActivity.this);
                                    t.setLayoutParams (params);
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
        tvSelectResume.setText (path);
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
    
}



