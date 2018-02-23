package com.referex.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.referex.R;
import com.referex.fragment.LoginFragment;
import com.referex.fragment.SignUpFragment;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();

        initListener();
        displayFirebaseRegId();

    }


    private void initData() {

        progressDialog = new ProgressDialog(LoginActivity.this);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        progressDialog = new ProgressDialog(LoginActivity.this);
        Utils.setTypefaceToAllViews(this, clMain);
        userDetailsPref = UserDetailsPref.getInstance();

    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);


    }

    private void initListener() {
    }

    private void displayFirebaseRegId() {
        Utils.showLog(Log.ERROR, "Firebase Reg ID:", userDetailsPref.getStringPref(LoginActivity.this, UserDetailsPref.USER_FIREBASE_ID), true);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "SIGN IN");
        adapter.addFragment(new SignUpFragment(), "SIGN UP");
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}