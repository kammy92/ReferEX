package com.referex.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.referex.R;
import com.referex.fragment.CandidateDetailDialogFragment;
import com.referex.model.MyAccount;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    
    private Activity activity;
    private List<MyAccount> myAccountList = new ArrayList<MyAccount> ();
    
    public MyAccountAdapter (Activity activity, List<MyAccount> myAccountList) {
        this.activity = activity;
        this.myAccountList = myAccountList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_account, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        
        final MyAccount myAccountList1 = myAccountList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvText);
        Utils.setTypefaceToAllViews (activity, holder.tvValue);
        
        
        holder.tvText.setText (myAccountList1.getText ());
        holder.tvValue.setText ("" + myAccountList1.getValue ());
        
        
    }
    
    @Override
    public int getItemCount () {
        return myAccountList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvText;
        TextView tvValue;
        
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvText = (TextView) view.findViewById (R.id.tvTotalReferrals);
            tvValue = (TextView) view.findViewById (R.id.tvTotalReferralsCount);
            
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            MyAccount myAccount = myAccountList.get (getLayoutPosition ());
            if (myAccount.getValue ().equalsIgnoreCase ("0")) {
            
            } else {
                android.app.FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
                CandidateDetailDialogFragment dialog = new CandidateDetailDialogFragment ().newInstance (myAccount.getCandidates ());
                dialog.show (ft, "Candidate");
            }
            
        }
    }
}


