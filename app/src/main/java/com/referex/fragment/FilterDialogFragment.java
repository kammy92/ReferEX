package com.referex.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.referex.R;
import com.referex.utils.FlowLayout;
import com.referex.utils.SetTypeFace;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by actiknow on 7/25/17.
 */
public class FilterDialogFragment extends DialogFragment {
    TextView tvApply;
    TextView tvBack;
    ImageView ivCancel;
    
    Button btAddExperience;
    Button btAddLocation;
    Button btAddRole;
    Button btAddSkills;
    
    FlowLayout flExperience;
    FlowLayout flLocation;
    FlowLayout flRole;
    FlowLayout flSkills;
    
    TextView tvNoExperience;
    TextView tvNoLocation;
    TextView tvNoRole;
    TextView tvNoSkills;
    
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
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    
    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.primary_dark));
        }
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_filter, container, false);
        initView (root);
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        tvApply = (TextView) root.findViewById (R.id.tvApply);
        tvBack = (TextView) root.findViewById (R.id.tvBack);
        
        btAddExperience = (Button) root.findViewById (R.id.btAddExperience);
        btAddLocation = (Button) root.findViewById (R.id.btAddLocation);
        btAddRole = (Button) root.findViewById (R.id.btAddRole);
        btAddSkills = (Button) root.findViewById (R.id.btAddSkills);
        
        flExperience = (FlowLayout) root.findViewById (R.id.flExperience);
        flLocation = (FlowLayout) root.findViewById (R.id.flLocation);
        flRole = (FlowLayout) root.findViewById (R.id.flRole);
        flSkills = (FlowLayout) root.findViewById (R.id.flSkills);
        
        tvNoExperience = (TextView) root.findViewById (R.id.tvNoExperience);
        tvNoLocation = (TextView) root.findViewById (R.id.tvNoLocation);
        tvNoRole = (TextView) root.findViewById (R.id.tvNoRole);
        tvNoSkills = (TextView) root.findViewById (R.id.tvNoSkills);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
    }
    
    private void initData () {
        skillsArrayList.addAll (Arrays.asList (new String[] {"C", "C++", "Java", "Android", "HTML", "PHP", "Hadoop", "Tableau", "iOS"}));
        skillsSelectedArrayList.addAll (skillsArrayList);
        
        locationArrayList.addAll (Arrays.asList (new String[] {"Delhi NCR", "Bangalore", "Pune", "Mumbai", "Hyderabad"}));
        locationSelectedArrayList.addAll (locationArrayList);
        
        roleArrayList.addAll (Arrays.asList (new String[] {"Programmers", "Analyst", "Manager", "Designer"}));
        roleSelectedArrayList.addAll (roleArrayList);
        
        experienceArrayList.addAll (Arrays.asList (new String[] {"0-1 Years", "1-2 Years", "2-3 Years", "3-4 Years", "4+ years"}));
        experienceSelectedArrayList.addAll (experienceArrayList);
        
        
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins (5, 5, 5, 5);
        if (skillsSelectedArrayList.size () > 0) {
            tvNoSkills.setVisibility (View.GONE);
            btAddSkills.setText ("ADD/EDIT");
        } else {
            tvNoSkills.setVisibility (View.VISIBLE);
            btAddSkills.setText ("ADD");
        }
        
        flSkills.removeAllViews ();
        flLocation.removeAllViews ();
        flRole.removeAllViews ();
        flExperience.removeAllViews ();
        TextView t;
        for (int i = 0; i < skillsSelectedArrayList.size (); i++) {
            t = new TextView (getActivity ());
            t.setLayoutParams (params);
            t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
            t.setPadding (8, 8, 8, 8);
            t.setText (skillsSelectedArrayList.get (i));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flSkills.addView (t);
        }
        
        if (locationSelectedArrayList.size () > 0) {
            tvNoLocation.setVisibility (View.GONE);
            btAddLocation.setText ("ADD/EDIT");
        } else {
            tvNoLocation.setVisibility (View.VISIBLE);
            btAddLocation.setText ("ADD");
        }
        for (int i = 0; i < locationSelectedArrayList.size (); i++) {
            t = new TextView (getActivity ());
            t.setLayoutParams (params);
            t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
            t.setPadding (8, 8, 8, 8);
            t.setText (locationSelectedArrayList.get (i));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flLocation.addView (t);
        }
        
        if (roleSelectedArrayList.size () > 0) {
            tvNoRole.setVisibility (View.GONE);
            btAddRole.setText ("ADD/EDIT");
        } else {
            tvNoRole.setVisibility (View.VISIBLE);
            btAddRole.setText ("ADD");
        }
        for (int i = 0; i < roleSelectedArrayList.size (); i++) {
            t = new TextView (getActivity ());
            t.setLayoutParams (params);
            t.setPadding (8, 8, 8, 8);
            t.setText (roleSelectedArrayList.get (i));
            t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flRole.addView (t);
        }
        
        if (experienceSelectedArrayList.size () > 0) {
            tvNoExperience.setVisibility (View.GONE);
            btAddExperience.setText ("ADD/EDIT");
        } else {
            tvNoExperience.setVisibility (View.VISIBLE);
            btAddExperience.setText ("ADD");
        }
        for (int i = 0; i < experienceSelectedArrayList.size (); i++) {
            t = new TextView (getActivity ());
            t.setLayoutParams (params);
            t.setPadding (8, 8, 8, 8);
            t.setText (experienceSelectedArrayList.get (i));
            t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
            t.setTextColor (Color.WHITE);
            t.setBackgroundResource (R.drawable.square);
            flExperience.addView (t);
        }
        
        Utils.setTypefaceToAllViews (getActivity (), tvBack);
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        
        
        tvApply.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getActivity ().finish ();
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
                
                new MaterialDialog.Builder (getActivity ())
                        .title ("Skills")
                        .items (skillsArrayList)
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
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
                                    t = new TextView (getActivity ());
                                    t.setLayoutParams (params);
                                    t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
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
                
                new MaterialDialog.Builder (getActivity ())
                        .title ("Skills")
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .items (roleArrayList)
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
                                flRole.removeAllViews ();
                                roleSelectedArrayList.clear ();
                                if (text.length > 0) {
                                    tvNoRole.setVisibility (View.GONE);
                                    btAddRole.setText ("ADD/EDIT");
                                } else {
                                    tvNoRole.setVisibility (View.VISIBLE);
                                    btAddRole.setText ("ADD");
                                }
                                TextView t;
                                for (int i = 0; i < text.length; i++) {
                                    roleSelectedArrayList.add (text[i].toString ());
                                    t = new TextView (getActivity ());
                                    t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
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
                
                new MaterialDialog.Builder (getActivity ())
                        .title ("Experience")
                        .items (experienceArrayList)
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
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
                                    t = new TextView (getActivity ());
                                    t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
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
                
                new MaterialDialog.Builder (getActivity ())
                        .title ("Location")
                        .items (locationArrayList)
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .itemsCallbackMultiChoice (ints, new MaterialDialog.ListCallbackMultiChoice () {
                            @Override
                            public boolean onSelection (MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins (5, 5, 5, 5);
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
                                    t = new TextView (getActivity ());
                                    t.setTypeface (SetTypeFace.getTypeface (getActivity ()));
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
