package com.referex.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.referex.R;
import com.referex.model.FilterLocation;
import com.referex.model.FilterSkills;
import com.referex.utils.AppConfigTags;
import com.referex.utils.FlowLayout;
import com.referex.utils.SetTypeFace;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by actiknow on 7/25/17.
 */
public class SearchJobActivity extends AppCompatActivity {
    TextView tvSearch;
    TextView tvBack;

    Button btAddExperience;
    Button btAddLocation;
    Button btAddSalary;
    Button btAddSkills;
    
    FlowLayout flExperience;
    FlowLayout flLocation;
    FlowLayout flSalary;
    FlowLayout flSkills;
    
    TextView tvNoExperience;
    TextView tvNoLocation;
    TextView tvNoSalary;
    TextView tvNoSkills;

    RelativeLayout rlBack;
    UserDetailsPref userDetailsPref;
    
    ArrayList<Integer> locationId = new ArrayList<Integer> ();
    ArrayList<Integer> skillsId = new ArrayList<Integer> ();
    
    ArrayList<FilterSkills> skillsArrayList = new ArrayList<FilterSkills> ();
    ArrayList<String> skillsSelectedArrayList = new ArrayList<String> ();
    ArrayList<String> skillsNameArrayList = new ArrayList<String> ();
    ArrayList<Integer> skillsIdArrayList = new ArrayList<Integer> ();
    
    ArrayList<FilterLocation> locationArrayList = new ArrayList<FilterLocation> ();
    ArrayList<String> locationSelectedArrayList = new ArrayList<String> ();
    ArrayList<String> locationNameArrayList = new ArrayList<String> ();
    ArrayList<Integer> locationIdArrayList = new ArrayList<Integer> ();
    
    
    ArrayList<String> salaryArrayList = new ArrayList<String> ();
    ArrayList<String> salarySelectedArrayList = new ArrayList<String> ();
    
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
        btAddSalary = (Button) findViewById (R.id.btAddSalary);
        btAddSkills = (Button) findViewById (R.id.btAddSkills);
        
        flExperience = (FlowLayout) findViewById (R.id.flExperience);
        flLocation = (FlowLayout) findViewById (R.id.flLocation);
        flSalary = (FlowLayout) findViewById (R.id.flSalary);
        flSkills = (FlowLayout) findViewById (R.id.flSkills);
    
        tvNoExperience = (TextView) findViewById (R.id.tvNoExperience);
        tvNoLocation = (TextView) findViewById (R.id.tvNoLocation);
        tvNoSalary = (TextView) findViewById (R.id.tvNoSalary);
        tvNoSkills = (TextView) findViewById (R.id.tvNoSkills);
    }
    
    private void initData () {
    
        userDetailsPref = UserDetailsPref.getInstance ();
        String skillsArray = userDetailsPref.getStringPref (SearchJobActivity.this, UserDetailsPref.SKILLS);
        Log.e ("skillsArray", skillsArray);
        try {
            JSONArray jsonArraySkills = new JSONArray (skillsArray);
            for (int i = 0; i < jsonArraySkills.length (); i++) {
                JSONObject jsonObjectSkill = jsonArraySkills.getJSONObject (i);
                skillsArrayList.add (new FilterSkills (
                        jsonObjectSkill.getInt (AppConfigTags.SKILL_ID),
                        jsonObjectSkill.getString (AppConfigTags.SKILL_NAME)
            
            
                ));
                skillsNameArrayList.add (jsonObjectSkill.getString (AppConfigTags.SKILL_NAME));
                skillsIdArrayList.add (jsonObjectSkill.getInt (AppConfigTags.SKILL_ID));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        String locationArray = userDetailsPref.getStringPref (SearchJobActivity.this, UserDetailsPref.LOCATION);
        try {
            JSONArray jsonArrayLocation = new JSONArray (locationArray);
            for (int i = 0; i < jsonArrayLocation.length (); i++) {
                JSONObject jsonObjectLocation = jsonArrayLocation.getJSONObject (i);
                locationArrayList.add (new FilterLocation (
                        jsonObjectLocation.getInt (AppConfigTags.LOCATION_ID),
                        jsonObjectLocation.getString (AppConfigTags.LOCATION_NAME)
            
            
                ));
                locationNameArrayList.add (jsonObjectLocation.getString (AppConfigTags.LOCATION_NAME));
                locationIdArrayList.add (jsonObjectLocation.getInt (AppConfigTags.LOCATION_ID));
            
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    
    
        //    skillsArrayList.addAll (Arrays.asList (new String[] {"C", "C++", "Java", "Android", "HTML", "PHP", "Hadoop", "Tableau", "iOS"}));
//        skillsSelectedArrayList.addAll (skillsArrayList);
    
        //    locationArrayList.addAll (Arrays.asList (new String[] {"Delhi NCR", "Bangalore", "Pune", "Mumbai", "Hyderabad"}));
//        locationSelectedArrayList.addAll (locationArrayList);
    
        salaryArrayList.addAll (Arrays.asList (new String[] {"10,000", "20,000", "30,000", "40,000", "50,000", "60,000+"}));
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
        flSalary.removeAllViews ();
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
    
        if (salarySelectedArrayList.size () > 0) {
            btAddSalary.setText ("ADD/EDIT");
        } else {
            btAddSalary.setText ("ADD");
        }
        for (int i = 0; i < salarySelectedArrayList.size (); i++) {
            t = new TextView (SearchJobActivity.this);
            t.setLayoutParams (params);
            t.setPadding (8, 8, 8, 8);
            t.setText (salarySelectedArrayList.get (i));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flSalary.addView (t);
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
                String location_id = "";
                String skills_id = "";
                ArrayList<Integer> salarySelectedInteger = new ArrayList<Integer> ();
    
                for (int h = 0; h < salarySelectedArrayList.size (); h++) {
                    salarySelectedInteger.add (parseInt (salarySelectedArrayList.get (h).replaceAll ("[-+.^:,]", "")));
                }
                int minsalary = salarySelectedInteger.get (0);
                int maxsalary = salarySelectedInteger.get (0);
                for (int k = 0; k < salarySelectedInteger.size (); k++) {
                    int numberminsalary = salarySelectedInteger.get (k);
                    int numbermaxsalary = salarySelectedInteger.get (k);
                    if (numberminsalary < minsalary) minsalary = numberminsalary;
                    if (salarySelectedInteger.contains ("60000")) {
                        maxsalary = parseInt ("60000");
                    } else {
                        if (numbermaxsalary > maxsalary)
                            maxsalary = numbermaxsalary;
                    }
                }
    
                for (int a = 0; a < skillsArrayList.size (); a++) {
                    for (int b = 0; b < skillsSelectedArrayList.size (); b++) {
                        if (skillsArrayList.get (a).getSkill ().equalsIgnoreCase (skillsSelectedArrayList.get (b))) {
                            skillsId.add (skillsArrayList.get (a).getId ());
                            if (b == 0) {
                                skills_id = "" + skillsArrayList.get (b).getId ();
                            } else {
                                skills_id = skills_id + "," + String.valueOf (skillsArrayList.get (b).getId ());
                    
                            }
                        }
                    }
        
                }
    
                for (int c = 0; c < locationArrayList.size (); c++) {
                    for (int d = 0; d < locationSelectedArrayList.size (); d++) {
                        if (locationArrayList.get (c).getLocation ().equalsIgnoreCase (locationSelectedArrayList.get (d))) {
                            if (d == 0) {
                                location_id = "" + locationArrayList.get (c).getId ();
                            } else {
                                location_id = location_id + "," + String.valueOf (locationArrayList.get (c).getId ());
                    
                            }
                        }
                    }
        
                }
    
                ArrayList<Integer> value = new ArrayList<> ();
                for (int k = 0; k < experienceSelectedArrayList.size (); k++) {
                    List<String> myList = new ArrayList<String> (Arrays.asList (experienceSelectedArrayList.get (k).replaceAll ("[^\\d.]", "").split ("")));
                    myList.remove (0);
                    Log.e ("SALAM", "" + myList);
                    for (int j = 0; j < myList.size (); j++) {
                        value.add (parseInt (myList.get (j)));
                    }
                }
    
                Log.e ("value", "" + value);
                int min = value.get (0);
                int maximum = value.get (0);
                for (int k = 0; k < value.size (); k++) {
                    int numbermin = value.get (k);
                    int numbermax = value.get (k);
                    if (numbermin < min) min = numbermin;
                    if (numbermax > maximum) maximum = numbermax;
                }
                Log.e ("etExperience", "Min - " + min + "Max -" + maximum);
                Log.e ("etEsalary", "Min - " + minsalary + "Max -" + maxsalary);
                Log.e ("SKIlls", skills_id);
                Log.e ("Location", location_id);
                Intent intent = new Intent (SearchJobActivity.this, SearchResultActivity.class);
                intent.putExtra (AppConfigTags.MIN_EXP, String.valueOf (min));
                intent.putExtra (AppConfigTags.MAX_EXP, String.valueOf (maximum));
                intent.putExtra (AppConfigTags.MIN_SALARY, String.valueOf (minsalary));
                intent.putExtra (AppConfigTags.MAX_SALARY, String.valueOf (maxsalary));
                intent.putExtra (AppConfigTags.LOCATIONS, location_id);
                intent.putExtra (AppConfigTags.SKILLS, skills_id);
                startActivity (intent);
                //finish ();
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
                // ArrayList<Integer> skillId = new ArrayList<Integer> ();
                for (int i = 0; i < skillsSelectedArrayList.size (); i++) {
                    for (int j = 0; j < skillsNameArrayList.size (); j++) {
                        if (skillsSelectedArrayList.get (i).equalsIgnoreCase (skillsNameArrayList.get (j))) {
                            skillPositionList.add (j);
                            //  skillsId.add(skillsArrayList.get(j).getId());
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
                        .items (skillsNameArrayList)
                        .typeface (SetTypeFace.getTypeface (SearchJobActivity.this), SetTypeFace.getTypeface (SearchJobActivity.this))
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flSkills.removeAllViews ();
                                skillsSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    tvNoSkills.setVisibility (View.GONE);
                                    btAddSkills.setText ("ADD/EDIT");
                                } else {
                                    tvNoSkills.setVisibility (View.VISIBLE);
                                    btAddSkills.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    skillsSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setLayoutParams (params);
                                    t.setTypeface (SetTypeFace.getTypeface (SearchJobActivity.this));
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
    
        btAddSalary.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ArrayList<Integer> salaryPositionList = new ArrayList<Integer> ();
                for (int i = 0; i < salarySelectedArrayList.size (); i++) {
                    for (int j = 0; j < salaryArrayList.size (); j++) {
                        if (salarySelectedArrayList.get (i).equalsIgnoreCase (salaryArrayList.get (j))) {
                            salaryPositionList.add (j);
                        }
                    }
                }
    
                Integer[] ints = new Integer[salaryPositionList.size ()];
                int i = 0;
                for (Integer n : salaryPositionList) {
                    ints[i++] = n;
                }
                
                new MaterialDialog.Builder (SearchJobActivity.this)
                        .title ("Skills")
                        .items (salaryArrayList)
                        .typeface (SetTypeFace.getTypeface (SearchJobActivity.this), SetTypeFace.getTypeface (SearchJobActivity.this))
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flSalary.removeAllViews ();
                                salarySelectedArrayList.clear ();
                                if (text.length > 0) {
                                    tvNoSalary.setVisibility (View.GONE);
                                    btAddSalary.setText ("ADD/EDIT");
                                } else {
                                    tvNoSalary.setVisibility (View.VISIBLE);
                                    btAddSalary.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    salarySelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setLayoutParams (params);
                                    t.setTypeface (SetTypeFace.getTypeface (SearchJobActivity.this));
                                    t.setPadding (8, 8, 8, 8);
                                    t.setText (text[i]);
                                    t.setTextColor (Color.WHITE);
                                    t.setBackgroundResource (R.drawable.square);
                                    flSalary.addView (t);

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
                        .typeface (SetTypeFace.getTypeface (SearchJobActivity.this), SetTypeFace.getTypeface (SearchJobActivity.this))
                        .items (experienceArrayList)
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flExperience.removeAllViews ();
                                experienceSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    tvNoExperience.setVisibility (View.GONE);
                                    btAddExperience.setText ("ADD/EDIT");
                                } else {
                                    tvNoExperience.setVisibility (View.VISIBLE);
                                    btAddExperience.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    experienceSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setLayoutParams (params);
                                    t.setTypeface (SetTypeFace.getTypeface (SearchJobActivity.this));
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
                    for (int j = 0; j < locationNameArrayList.size (); j++) {
                        if (locationSelectedArrayList.get (i).equalsIgnoreCase (locationNameArrayList.get (j))) {
                            locationPositionList.add (j);
                            locationId.add (locationArrayList.get (j).getId ());
                        }
                    }
                }
                Log.e ("locationArrayList", String.valueOf (locationId));

                Integer[] ints = new Integer[locationPositionList.size ()];
                int i = 0;
                for (Integer n : locationPositionList) {
                    ints[i++] = n;
                }
    
                int[] intId = new int[locationIdArrayList.size ()];
                int j = 0;
                for (int n : locationIdArrayList) {
                    intId[j++] = n;
                }
    
                Log.e ("INARRAY", "" + ints);
                new MaterialDialog.Builder (SearchJobActivity.this)
                        .title ("Location")
                        .items (locationNameArrayList)
                        // .itemsIds(intId)
                        .typeface (SetTypeFace.getTypeface (SearchJobActivity.this), SetTypeFace.getTypeface (SearchJobActivity.this))
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                Toast.makeText (SearchJobActivity.this, which.toString () + ": " + text.toString () + "", Toast.LENGTH_SHORT).show ();
                                flLocation.removeAllViews ();
                                locationSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    tvNoLocation.setVisibility (View.GONE);
                                    btAddLocation.setText ("ADD/EDIT");
                                } else {
                                    tvNoLocation.setVisibility (View.VISIBLE);
                                    btAddLocation.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    locationSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (SearchJobActivity.this);
                                    t.setTypeface (SetTypeFace.getTypeface (SearchJobActivity.this));
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
