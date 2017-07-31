package com.referex.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.referex.R;
import com.referex.utils.FlowLayout;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by actiknow on 7/25/17.
 */
public class SearchJobActivity extends AppCompatActivity {
    TextView tvSearch;
    TextView tvBack;
    
    Button btAddExperience;
    Button btAddLocation;
    Button btAddRole;
    Button btAddSkills;
    
    FlowLayout flExperience;
    FlowLayout flLocation;
    FlowLayout flRole;
    FlowLayout flSkills;
    
    RelativeLayout rlBack;
    
    ArrayList<String> skillsArrayList = new ArrayList<String> ();
    ArrayList<String> skillsSelectedArrayList = new ArrayList<String> ();
    
    ArrayList<String> locationArrayList = new ArrayList<String> ();
    ArrayList<String> locationSelectedArrayList = new ArrayList<String> ();
    
    ArrayList<String> roleArrayList = new ArrayList<String> ();
    ArrayList<String> roleSelectedArrayList = new ArrayList<String> ();
    
    ArrayList<String> experienceArrayList = new ArrayList<String> ();
    ArrayList<String> experienceSelectedArrayList = new ArrayList<String> ();
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_search_job);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initView () {
        tvSearch = (TextView) findViewById (R.id.tvSearch);
        tvBack = (TextView) findViewById (R.id.tvBack);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        
        btAddExperience = (Button) findViewById (R.id.btAddExperience);
        btAddLocation = (Button) findViewById (R.id.btAddLocation);
        btAddRole = (Button) findViewById (R.id.btAddRole);
        btAddSkills = (Button) findViewById (R.id.btAddSkills);
        
        flExperience = (FlowLayout) findViewById (R.id.flExperience);
        flLocation = (FlowLayout) findViewById (R.id.flLocation);
        flRole = (FlowLayout) findViewById (R.id.flRole);
        flSkills = (FlowLayout) findViewById (R.id.flSkills);
    }
    
    private void initData () {
        skillsArrayList.addAll (Arrays.asList (new String[] {"C", "C++", "Java", "Android", "HTML", "PHP", "Hadoop", "Tableau", "iOS"}));
//        skillsSelectedArrayList.addAll (skillsArrayList);
        
        locationArrayList.addAll (Arrays.asList (new String[] {"Delhi NCR", "Bangalore", "Pune", "Mumbai", "Hyderabad"}));
//        locationSelectedArrayList.addAll (locationArrayList);
        
        roleArrayList.addAll (Arrays.asList (new String[] {"Programmers", "Analyst", "Manager", "Designer"}));
//        roleSelectedArrayList.addAll (roleArrayList);
        
        experienceArrayList.addAll (Arrays.asList (new String[] {"0-1 Years", "1-2 Years", "2-3 Years", "3-4 Years", "4+ years"}));
//        experienceSelectedArrayList.addAll (experienceArrayList);
        
        
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins (5, 5, 5, 5);
        if (skillsSelectedArrayList.size () > 0) {
            btAddSkills.setText ("ADD/EDIT");
        } else {
            btAddSkills.setText ("ADD");
        }
        
        flSkills.removeAllViews ();
        flLocation.removeAllViews ();
        flRole.removeAllViews ();
        flExperience.removeAllViews ();
        TextView t;
        for (int i = 0; i < skillsSelectedArrayList.size (); i++) {
            t = new TextView (SearchJobActivity.this);
            t.setLayoutParams (params);
            t.setPadding (8, 8, 8, 8);
            t.setText (skillsSelectedArrayList.get (i));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flSkills.addView (t);
        }
        
        if (locationSelectedArrayList.size () > 0) {
            btAddLocation.setText ("ADD/EDIT");
        } else {
            btAddLocation.setText ("ADD");
        }
        for (int i = 0; i < locationSelectedArrayList.size (); i++) {
            t = new TextView (SearchJobActivity.this);
            t.setLayoutParams (params);
            t.setPadding (8, 8, 8, 8);
            t.setText (locationSelectedArrayList.get (i));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flLocation.addView (t);
        }
        
        if (roleSelectedArrayList.size () > 0) {
            btAddRole.setText ("ADD/EDIT");
        } else {
            btAddRole.setText ("ADD");
        }
        for (int i = 0; i < roleSelectedArrayList.size (); i++) {
            t = new TextView (SearchJobActivity.this);
            t.setLayoutParams (params);
            t.setPadding (8, 8, 8, 8);
            t.setText (roleSelectedArrayList.get (i));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flRole.addView (t);
        }
        
        if (experienceSelectedArrayList.size () > 0) {
            btAddExperience.setText ("ADD/EDIT");
        } else {
            btAddExperience.setText ("ADD");
        }
        for (int i = 0; i < experienceSelectedArrayList.size (); i++) {
            t = new TextView (SearchJobActivity.this);
            t.setLayoutParams (params);
            t.setPadding (8, 8, 8, 8);
            t.setText (experienceSelectedArrayList.get (i));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flExperience.addView (t);
        }
        
        Utils.setTypefaceToAllViews (this, tvBack);
    }
    
    private void initListener () {
        tvSearch.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
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
                
                new MaterialDialog.Builder (SearchJobActivity.this)
                        .title ("Skills")
                        .items (skillsArrayList)
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flSkills.removeAllViews ();
                                skillsSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    btAddSkills.setText ("ADD/EDIT");
                                } else {
                                    btAddSkills.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    skillsSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setLayoutParams (params);
                                    t.setPadding (8, 8, 8, 8);
                                    t.setText (text[i]);
                                    t.setTextColor (Color.WHITE);
                                    t.setBackgroundResource (R.drawable.square);
                                    flSkills.addView (t);
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
        
        btAddRole.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ArrayList<Integer> rolePositionList = new ArrayList<Integer> ();
                for (int i = 0; i < roleSelectedArrayList.size (); i++) {
                    for (int j = 0; j < roleArrayList.size (); j++) {
                        if (roleSelectedArrayList.get (i).equalsIgnoreCase (roleArrayList.get (j))) {
                            rolePositionList.add (j);
                        }
                    }
                }
                
                Integer[] ints = new Integer[rolePositionList.size ()];
                int i = 0;
                for (Integer n : rolePositionList) {
                    ints[i++] = n;
                }
                
                new MaterialDialog.Builder (SearchJobActivity.this)
                        .title ("Skills")
                        .items (roleArrayList)
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flRole.removeAllViews ();
                                roleSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    btAddRole.setText ("ADD/EDIT");
                                } else {
                                    btAddRole.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    roleSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setLayoutParams (params);
                                    t.setPadding (8, 8, 8, 8);
                                    t.setText (text[i]);
                                    t.setTextColor (Color.WHITE);
                                    t.setBackgroundResource (R.drawable.square);
                                    flRole.addView (t);
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
        
        btAddExperience.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ArrayList<Integer> experiencePositionList = new ArrayList<Integer> ();
                for (int i = 0; i < experienceSelectedArrayList.size (); i++) {
                    for (int j = 0; j < experienceArrayList.size (); j++) {
                        if (experienceSelectedArrayList.get (i).equalsIgnoreCase (experienceArrayList.get (j))) {
                            experiencePositionList.add (j);
                        }
                    }
                }
                
                Integer[] ints = new Integer[experiencePositionList.size ()];
                int i = 0;
                for (Integer n : experiencePositionList) {
                    ints[i++] = n;
                }
                
                new MaterialDialog.Builder (SearchJobActivity.this)
                        .title ("Experience")
                        .items (experienceArrayList)
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flExperience.removeAllViews ();
                                experienceSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    btAddExperience.setText ("ADD/EDIT");
                                } else {
                                    btAddExperience.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    experienceSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setLayoutParams (params);
                                    t.setPadding (8, 8, 8, 8);
                                    t.setText (text[i]);
                                    t.setTextColor (Color.WHITE);
                                    t.setBackgroundResource (R.drawable.square);
                                    flExperience.addView (t);
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
        
        btAddLocation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ArrayList<Integer> locationPositionList = new ArrayList<Integer> ();
                for (int i = 0; i < locationSelectedArrayList.size (); i++) {
                    for (int j = 0; j < locationArrayList.size (); j++) {
                        if (locationSelectedArrayList.get (i).equalsIgnoreCase (locationArrayList.get (j))) {
                            locationPositionList.add (j);
                        }
                    }
                }
                
                Integer[] ints = new Integer[locationPositionList.size ()];
                int i = 0;
                for (Integer n : locationPositionList) {
                    ints[i++] = n;
                }
                
                new MaterialDialog.Builder (SearchJobActivity.this)
                        .title ("Location")
                        .items (locationArrayList)
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flLocation.removeAllViews ();
                                locationSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    btAddLocation.setText ("ADD/EDIT");
                                } else {
                                    btAddLocation.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    locationSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setLayoutParams (params);
                                    t.setPadding (8, 8, 8, 8);
                                    t.setText (text[i]);
                                    t.setTextColor (Color.WHITE);
                                    t.setBackgroundResource (R.drawable.square);
                                    flLocation.addView (t);
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
}
