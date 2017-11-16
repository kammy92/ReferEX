package com.referex.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.referex.R;
import com.referex.model.CandidateDetails;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class CandidateDetailAdapter extends RecyclerView.Adapter<CandidateDetailAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    
    private Activity activity;
    private List<CandidateDetails> candidateList = new ArrayList<CandidateDetails> ();
    
    public CandidateDetailAdapter (Activity activity, List<CandidateDetails> candidateList) {
        this.activity = activity;
        this.candidateList = candidateList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_candidate_detail, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        
        final CandidateDetails candidateList1 = candidateList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvName);
        Utils.setTypefaceToAllViews (activity, holder.tvEmail);
        Utils.setTypefaceToAllViews (activity, holder.tvNumber);
        Utils.setTypefaceToAllViews (activity, holder.tvStatus);
        
        
        holder.tvName.setText (candidateList1.getName ());
        holder.tvNumber.setText (candidateList1.getNumber ());
        holder.tvEmail.setText (candidateList1.getEmail ());
        holder.tvStatus.setText (candidateList1.getTitle () + " - " + "(" + candidateList1.getDescription () + ")");
        
        
    }
    
    @Override
    public int getItemCount () {
        return candidateList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvEmail;
        TextView tvNumber;
        TextView tvStatus;
        
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            tvEmail = (TextView) view.findViewById (R.id.tvEmail);
            tvNumber = (TextView) view.findViewById (R.id.tvNumber);
            tvStatus = (TextView) view.findViewById (R.id.tvStatus);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            
            
        }
    }
}


