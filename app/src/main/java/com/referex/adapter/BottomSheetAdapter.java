package com.referex.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.referex.R;
import com.referex.model.BottomSheet;
import com.referex.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    
    private Activity activity;
    private List<BottomSheet> bottomSheetList = new ArrayList<BottomSheet> ();
    
    public BottomSheetAdapter (Activity activity, List<BottomSheet> bottomSheetList) {
        this.activity = activity;
        this.bottomSheetList = bottomSheetList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_bottom_sheet, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        
        final BottomSheet bottomSheetList1 = bottomSheetList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvText);
        Utils.setTypefaceToAllViews (activity, holder.tvValue);
        
        
        holder.tvText.setText (bottomSheetList1.getText ());
        holder.tvValue.setText ("" + bottomSheetList1.getValue ());
        
        
    }
    
    @Override
    public int getItemCount () {
        return bottomSheetList.size ();
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
            
            
        }
    }
}


