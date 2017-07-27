package com.referex.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.referex.R;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by actiknow on 7/25/17.
 */
public class FilterActivity extends AppCompatActivity {
    Spinner spSalary;
    TagsEditText locationEditText;
    TextView tvRole;
    TextView tvEducation;
    TextView tvFunctionalArea;
    TextView tv1;
    TextView tv2;
    TextView tvApply;
    TextView tvTitle;
    TextView tvBack;
    RelativeLayout rlBack;
    EditText etEducation;
    EditText etRole;
    EditText etFunctionalArea;
    EditText etLocation;
    ImageView ivFilter;
    ArrayList<String> functionalAreaList = new ArrayList<>();
    String[] salaryArray = {"0-3", "3-6", "8-10", "10-15", "15-40"};

    ArrayList<String> roleList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        spSalary = (Spinner) findViewById(R.id.spSalary);
        etLocation = (EditText) findViewById(R.id.etLocation);
        tvRole = (TextView) findViewById(R.id.tvRole);
        tvEducation = (TextView) findViewById(R.id.tvEducation);
        tvFunctionalArea = (TextView) findViewById(R.id.tvFunctionalArea);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tvApply = (TextView) findViewById(R.id.tvApply);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvBack = (TextView) findViewById(R.id.tvBack);
        rlBack=(RelativeLayout)findViewById(R.id.rlBack);

        etEducation = (EditText) findViewById(R.id.etEducation);
        etFunctionalArea = (EditText) findViewById(R.id.etFunctionalArea);
        etRole = (EditText) findViewById(R.id.etRole);

        Utils.setTypefaceToAllViews (this, etEducation);
        Utils.setTypefaceToAllViews (this, etFunctionalArea);
        Utils.setTypefaceToAllViews (this, etEducation);
        Utils.setTypefaceToAllViews (this, spSalary);
        Utils.setTypefaceToAllViews (this, tvRole);
        Utils.setTypefaceToAllViews (this, tvEducation);
        Utils.setTypefaceToAllViews (this, tvFunctionalArea);
        Utils.setTypefaceToAllViews (this, tv1);
        Utils.setTypefaceToAllViews (this, tv2);
        Utils.setTypefaceToAllViews (this, tvApply);
        Utils.setTypefaceToAllViews (this, tvBack);
        Utils.setTypefaceToAllViews (this, tvTitle);

    }


    private void initData() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, salaryArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSalary.setAdapter(spinnerArrayAdapter);


        roleList.add("Programmer");
        roleList.add("Designer");
        roleList.add("Analyst");
        roleList.add("Manager");


        functionalAreaList.add("Software Engineer");
        functionalAreaList.add("Java Developer");
        functionalAreaList.add("Finance Management");
        functionalAreaList.add("PHP Developer");
        functionalAreaList.add("IOS Developer");

        cityList.add("Ahemdabad");
        cityList.add("Bangalore");
        cityList.add("Bhopal");
        cityList.add("Chennai");
        cityList.add("Delhi");
        cityList.add("Faridabad");
        cityList.add("Gurgaon");
        cityList.add("Ghaziabad");
        cityList.add("Gandhinagar");
        cityList.add("Grater Noida");
        cityList.add("Hyderabad");
        cityList.add("Indore");
        cityList.add("Indore");
        cityList.add("Kolkata");
        cityList.add("Mumbai");
        cityList.add("Pune");

    }

    private void initListener() {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        etFunctionalArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FilterActivity.this)
                        .title("Functional Area")
                        .items(functionalAreaList)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                String value = "";
                                for (int i = 0; i < text.length; i++) {
                                    if (i == 0) {
                                        value = text[0].toString();
                                    } else {
                                        value = value + "," + text[i].toString();
                                    }
                                    etFunctionalArea.setText(value);
                                    //Log.e("Text",""+text[i].toString()+",");
                                }
                                return true;
                            }
                        })
                        .positiveText("Submit")
                        .show();
            }
        });

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FilterActivity.this)
                        .title("Select City")
                        .items(cityList)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                String value = "";
                                for (int i = 0; i < text.length; i++) {
                                    if (i == 0) {
                                        value = text[0].toString();
                                    } else {
                                        value = value + "," + text[i].toString();
                                    }
                                    etLocation.setText(value);
                                    //Log.e("Text",""+text[i].toString()+",");
                                }
                                return true;
                            }
                        })
                        .positiveText("Submit")
                        .show();
            }
        });


        etRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FilterActivity.this)
                        .title("Role")
                        .items(roleList)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                String value = "";
                                for (int i = 0; i < text.length; i++) {
                                    if (i == 0) {
                                        value = text[0].toString();
                                    } else {
                                        value = value + "," + text[i].toString();
                                    }

                                    etRole.setText(value);
                                    //Log.e("Text",""+text[i].toString()+",");
                                }
                                return true;
                            }
                        })
                        .positiveText("Submit")
                        .show();
            }
        });


    }







}
