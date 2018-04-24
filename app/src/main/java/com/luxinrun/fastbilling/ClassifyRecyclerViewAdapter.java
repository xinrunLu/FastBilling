package com.luxinrun.fastbilling;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassifyRecyclerViewAdapter extends RecyclerView.Adapter<ClassifyRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mData;
    private Context mContext;

    public ClassifyRecyclerViewAdapter (Context context, ArrayList<String> data){
        this.mContext = context;
        this.mData = data;
    }

    public void updateData(ArrayList<String> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.adapter_classify, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.classify_tv.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView classify_tv;
        ImageView classify_img;

        public ViewHolder(View itemView) {
            super(itemView);
            classify_tv = (TextView) itemView.findViewById(R.id.classify_tv);
            classify_img = (ImageView) itemView.findViewById(R.id.classify_img);
        }
    }
}
