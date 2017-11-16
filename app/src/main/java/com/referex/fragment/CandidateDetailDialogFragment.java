package com.referex.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.referex.R;
import com.referex.adapter.CandidateDetailAdapter;
import com.referex.model.CandidateDetails;
import com.referex.utils.AppConfigTags;
import com.referex.utils.SimpleDividerItemDecoration;
import com.referex.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by actiknow on 7/25/17.
 */
public class CandidateDetailDialogFragment extends DialogFragment {
    
    ImageView ivCancel;
    RecyclerView rvCandidateList;
    CandidateDetailAdapter candidateDetailAdapter;
    List<CandidateDetails> candidateDetailsList = new ArrayList<> ();
    TextView tvTitle;
    String candidates;
    
    
    public static CandidateDetailDialogFragment newInstance (String candidates) {
        CandidateDetailDialogFragment fragment = new CandidateDetailDialogFragment ();
        Log.e ("Candidate2", candidates);
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.CANDIDATES, candidates);
        fragment.setArguments (args);
        return fragment;
    }
    
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
        View root = inflater.inflate (R.layout.fragment_dialog_candidate_details, container, false);
        initView (root);
        initData ();
        initListener ();
        getExtras ();
        setData ();
        
        return root;
    }
    
    private void getExtras () {
        Bundle bundle = this.getArguments ();
        candidates = bundle.getString (AppConfigTags.CANDIDATES);
        Log.e ("candidates", candidates);
        
        
    }
    
    private void setData () {
        try {
            JSONArray jsonArrayCandidate = new JSONArray (candidates);
            for (int i = 0; i < jsonArrayCandidate.length (); i++) {
                JSONObject jsonObjectCandidate = jsonArrayCandidate.getJSONObject (i);
                candidateDetailsList.add (new CandidateDetails (i,
                        jsonObjectCandidate.getString (AppConfigTags.NAME),
                        jsonObjectCandidate.getString (AppConfigTags.EMAIL),
                        jsonObjectCandidate.getString (AppConfigTags.MOBILE),
                        jsonObjectCandidate.getString (AppConfigTags.JOB_TITLE),
                        jsonObjectCandidate.getString (AppConfigTags.JOB_DESCRIPTION)));
                
            }
            candidateDetailAdapter.notifyDataSetChanged ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        
        
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rvCandidateList = (RecyclerView) root.findViewById (R.id.rvCandidateList);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);

      /*  candidateDetailsList.add(new CandidateDetails(1, "Rahul jain", "Rahul.jain@actiknowbi.com", "8527875036", "Status-1"));
        candidateDetailsList.add(new CandidateDetails(2, "Sudhanshu Sharma", "sudhanshu.sharma@actiknowb.com", "8527875036", "Status-2"));
        candidateDetailsList.add(new CandidateDetails(3, "Karman singh", "Karman.singh@actiknowbi.com", "8527875036", "Status-3"));
        candidateDetailsList.add(new CandidateDetails(4, "Rahul jain", "Rahul.jain@actiknowbi.com", "8527875036", "Status-4"));
        candidateDetailsList.add(new CandidateDetails(5, "Rahul jain", "Rahul.jain@actiknowbi.com", "8527875036", "Status-5"));

*/
        candidateDetailAdapter = new CandidateDetailAdapter (getActivity (), candidateDetailsList);
        rvCandidateList.setAdapter (candidateDetailAdapter);
        rvCandidateList.setHasFixedSize (true);
        rvCandidateList.addItemDecoration (new SimpleDividerItemDecoration (getActivity ()));
        rvCandidateList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvCandidateList.setItemAnimator (new DefaultItemAnimator ());
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        
    }
    
    
}






