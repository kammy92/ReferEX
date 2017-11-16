package com.referex.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.referex.R;
import com.referex.adapter.BottomSheetAdapter;
import com.referex.adapter.JobDescriptionAdapter;
import com.referex.fragment.FilterDialogFragment;
import com.referex.model.BottomSheet;
import com.referex.model.JobDescription;
import com.referex.utils.AppConfigTags;
import com.referex.utils.AppConfigURL;
import com.referex.utils.Constants;
import com.referex.utils.NetworkConnection;
import com.referex.utils.SetTypeFace;
import com.referex.utils.SimpleDividerItemDecoration;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

public class MainActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    RelativeLayout rlInternetConnection;
    RelativeLayout rlNoResultFound;
    UserDetailsPref userDetailsPref;
    TextView tvTitle;
    PieView pieView;
    View bottomSheet;
    ImageView ivSwipe;
    ImageView ivNavigation;
    List<BottomSheet> bottomSheetList = new ArrayList<> ();
    RecyclerView rvList, rvJobList;
    BottomSheetAdapter bottomSheetAdapter;
    double percentage;
    CoordinatorLayout clMain;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView ivFilter;
    JobDescriptionAdapter jobDescriptionAdapter;
    List<JobDescription> jobDescriptionList = new ArrayList<> ();
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        initListener();
        initDrawer ();
        initApplication ();
        recommendedJobList ();
        isLogin();

    }
    
    private void isLogin () {
        
        if (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY) == "") {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
            finish ();
        }
        if (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY) == "")
            finish ();
    }

    private void initView() {
        ivNavigation = (ImageView) findViewById (R.id.ivNavigation);
        rlInternetConnection = (RelativeLayout) findViewById (R.id.rlInternetConnection);
        rlNoResultFound = (RelativeLayout) findViewById (R.id.rlNoResultFound);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        pieView = (PieView) findViewById (R.id.pieView);
        bottomSheet = findViewById (R.id.bottom_sheet);
        ivSwipe = (ImageView) findViewById (R.id.ivSwipe);
        Utils.setTypefaceToAllViews (MainActivity.this, tvTitle);
        rvList = (RecyclerView) findViewById (R.id.rvList);
        rvJobList = (RecyclerView) findViewById (R.id.rvJobList);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
        ivFilter = (ImageView) findViewById (R.id.ivFilter);
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance ();
        pieView.setPercentageBackgroundColor (getResources ().getColor (R.color.pie_color_good));
        pieView.setInnerBackgroundColor (getResources ().getColor (R.color.primary));
        pieView.setTextColor (getResources ().getColor (R.color.text_color_white));
      /*  pieView.setInnerText(percentage+"%");
        pieView.setPercentageTextSize(Utils.pxFromDp(this, 8));
        pieView.setPercentage((float) percentage);*/
    
        mBottomSheetBehavior = BottomSheetBehavior.from (bottomSheet);
        mBottomSheetBehavior.setPeekHeight ((int) Utils.pxFromDp (this, 80));
        mBottomSheetBehavior.setState (BottomSheetBehavior.STATE_COLLAPSED);
    
    
        bottomSheetAdapter = new BottomSheetAdapter (MainActivity.this, bottomSheetList);
        rvList.setAdapter (bottomSheetAdapter);
        rvList.setHasFixedSize (true);
        rvList.setLayoutManager (new LinearLayoutManager (MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rvList.setItemAnimator (new DefaultItemAnimator ());
    
    
        jobDescriptionAdapter = new JobDescriptionAdapter (this, jobDescriptionList);
        rvJobList.setAdapter (jobDescriptionAdapter);
        rvJobList.setHasFixedSize (true);
        rvJobList.addItemDecoration (new SimpleDividerItemDecoration (this));
        rvJobList.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvJobList.setItemAnimator (new DefaultItemAnimator ());


    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                recommendedJobList ();
            }
        });
    
    
        ivFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                FilterDialogFragment frag = new FilterDialogFragment ();
                frag.show (ft, "");
            
            }
        });
    
    
        ivNavigation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                result.openDrawer ();
            }
        });
        mBottomSheetBehavior.setBottomSheetCallback (new BottomSheetBehavior.BottomSheetCallback () {
            @Override
            public void onStateChanged (@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        ivSwipe.setImageResource (R.drawable.swipe_down);
                        break;
                    default:
                        ivSwipe.setImageResource (R.drawable.swipe_up);
                        break;
                }
            }

            @Override
            public void onSlide (@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


    }
    
    private void initDrawer () {
        IProfile profile = new IProfile () {
            @Override
            public Object withName (String name) {
                return null;
            }

            @Override
            public StringHolder getName () {
                return null;
            }

            @Override
            public Object withEmail (String email) {
                return null;
            }

            @Override
            public StringHolder getEmail () {
                return null;
            }

            @Override
            public Object withIcon (Drawable icon) {
                return null;
            }

            @Override
            public Object withIcon (Bitmap bitmap) {
                return null;
            }

            @Override
            public Object withIcon (@DrawableRes int iconRes) {
                return null;
            }

            @Override
            public Object withIcon (String url) {
                return null;
            }

            @Override
            public Object withIcon (Uri uri) {
                return null;
            }

            @Override
            public Object withIcon (IIcon icon) {
                return null;
            }

            @Override
            public ImageHolder getIcon () {
                return null;
            }

            @Override
            public Object withSelectable (boolean selectable) {
                return null;
            }

            @Override
            public boolean isSelectable () {
                return false;
            }

            @Override
            public Object withIdentifier (long identifier) {
                return null;
            }

            @Override
            public long getIdentifier () {
                return 0;
            }
        };
        
        DrawerImageLoader.init (new AbstractDrawerImageLoader () {
            @Override
            public void set (ImageView imageView, Uri uri, Drawable placeholder) {
                if (uri != null) {
                    Glide.with (imageView.getContext ()).load (uri).placeholder (placeholder).into (imageView);
                }
            }

            @Override
            public void cancel (ImageView imageView) {
                Glide.clear (imageView);
            }

            @Override
            public Drawable placeholder (Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name ().equals (tag)) {
                    return DrawerUIUtils.getPlaceHolder (ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name ().equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp (56);
                } else if ("customUrlItem".equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (R.color.md_white_1000);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
    
                return super.placeholder (ctx, tag);
            }
        });
        
        
        headerResult = new AccountHeaderBuilder ()
                .withActivity (this)
                .withCompactStyle (false)
                .withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                .withTypeface (SetTypeFace.getTypeface (this))
                .withPaddingBelowHeader (false)
                .withSelectionListEnabled (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withProfileImagesVisible (false)
                .withOnlyMainProfileImageVisible (true)
                .withDividerBelowHeader (true)
                .withHeaderBackground (R.color.primary)
                .withSavedInstance (savedInstanceState)
                .withOnAccountHeaderListener (new AccountHeader.OnAccountHeaderListener () {
                    @Override
                    public boolean onProfileChanged (View view, IProfile profile, boolean currentProfile) {
                        Intent intent = new Intent (MainActivity.this, MainActivity.class);
                        startActivity (intent);
                        return false;
                    }
                })
                .build ();
        headerResult.addProfiles (new ProfileDrawerItem ()
                .withIcon (R.drawable.doctor)
                .withName (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_NAME))
                .withEmail (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL)));


        result = new DrawerBuilder()
                .withActivity (this)
                .withAccountHeader (headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Dashboard").withIcon (FontAwesome.Icon.faw_tachometer).withIdentifier (1).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Search job").withIcon (FontAwesome.Icon.faw_search).withIdentifier (2).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        //  new PrimaryDrawerItem().withName("Recommended Job").withIcon(FontAwesome.Icon.faw_thumbs_up).withIdentifier(3).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Bookmarked Job").withIcon (FontAwesome.Icon.faw_bookmark).withIdentifier (4).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Feedback").withIcon (FontAwesome.Icon.faw_comments).withIdentifier (5).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Promote this app").withIcon (FontAwesome.Icon.faw_bullhorn).withIdentifier (6).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                Intent intent2 = new Intent (MainActivity.this, SearchJobActivity.class);
                                startActivity (intent2);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 3:
                                Intent intent3 = new Intent (MainActivity.this, RecommendedJobActivity.class);
                                startActivity (intent3);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 4:
                                Intent intent4 = new Intent (MainActivity.this, BookmarkedJobActivity.class);
                                startActivity (intent4);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 5:
                                Intent intent5 = new Intent (MainActivity.this, FeedbackActivity.class);
                                startActivity (intent5);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 7:
                                showLogOutDialog ();
                                break;
                            case 8:

                            /*
                            case 4:
                                Intent intent3 = new Intent (MainActivity.this, AboutUsActivity.class);
                                startActivity (intent3);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 5:
                                Intent intent4 = new Intent (MainActivity.this, TestimonialActivity.class);
                                startActivity (intent4);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;

                            case 10:
                                showLogOutDialog ();
                                break;*/
                        }
                        return false;
                    }
                })
                .build ();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
    }
    
    @Override
    public void onBackPressed () {
        super.onBackPressed ();
    }
    
    private void showLogOutDialog () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .limitIconToDefaultSize ()
                .content ("Do you wish to Sign Out?")
                .positiveText ("Yes")
                .negativeText ("No")
                .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                        userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                        userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_NAME, "");
                        userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_LOGIN_KEY, "");
                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
    
    private void initApplication () {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_INIT, true);
            final PackageInfo finalPInfo = pInfo;
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_INIT,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    
                                    if (! error) {
                                        swipeRefreshLayout.setRefreshing (false);
                                        percentage = jsonObj.getDouble (AppConfigTags.PERFORMANCE_PERCENTAGE);
                                        
                                        pieView.setInnerText (percentage + "%");
                                        pieView.setPercentageTextSize (Utils.pxFromDp (MainActivity.this, 8));
                                        pieView.setPercentage ((float) percentage);
    
                                        PieAngleAnimation animation = new PieAngleAnimation (pieView);
                                        animation.setDuration (2000); //This is the duration of the animation in millis
                                        pieView.startAnimation (animation);
                                        pieView.setInnerTextVisibility (View.VISIBLE);
    
    
                                        JSONArray jsonArraySKills = jsonObj.getJSONArray (AppConfigTags.SKILLS);
                                        JSONArray jsonArrayLocation = jsonObj.getJSONArray (AppConfigTags.LOCATIONS);
    
                                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.SKILLS, jsonArraySKills.toString ());
                                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.LOCATION, jsonArrayLocation.toString ());
    
                                        JSONArray jsonArrayPerformance = jsonObj.getJSONArray (AppConfigTags.PERFORMANCE_STATUS);
                                        Log.e ("percentage", "" + percentage);
                                        for (int i = 0; i < jsonArrayPerformance.length (); i++) {
                                            JSONObject jsonObjectPerformance = jsonArrayPerformance.getJSONObject (i);
                                            bottomSheetList.add (i, new BottomSheet (
                                                    jsonObjectPerformance.getString (AppConfigTags.TEXT),
                                                    jsonObjectPerformance.getString (AppConfigTags.VALUE)
                                            ));
                                        }
                                        bottomSheetAdapter.notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<> ();
                    params.put ("app_version", String.valueOf (finalPInfo.versionCode));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
        }
    }
    
    public void recommendedJobList () {
        if (NetworkConnection.isNetworkAvailable (MainActivity.this)) {
            jobDescriptionList.clear ();
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_RECOMMENDED, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_RECOMMENDED,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        swipeRefreshLayout.setRefreshing (false);
                                        JSONArray jsonArrayJobs = jsonObj.getJSONArray (AppConfigTags.JOBS);
    
                                        for (int i = 0; i < jsonArrayJobs.length (); i++) {
                                            JSONObject jsonObjectDescription = jsonArrayJobs.getJSONObject (i);
                                            JobDescription jobDescription = new JobDescription (
                                                    jsonObjectDescription.getInt (AppConfigTags.JOB_ID),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_TITLE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_DESCRIPTION),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MIN_EXPERIENCE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MAX_EXPERIENCE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_SKILLS),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_COMPANY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_LOCATION),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MIN_SALARY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MAX_SALARY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_POSTED_AT),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_TYPE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_EXPIRES_IN),
                                                    jsonObjectDescription.getBoolean (AppConfigTags.JOB_BOOKMARKED)
                                            );
                                            jobDescriptionList.add (i, jobDescription);
                                        }
                                        
                                        
                                        jobDescriptionAdapter.notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    
                                }
                            } else {
                                Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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