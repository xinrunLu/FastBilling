package com.luxinrun.fastbilling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luxinrun.fastbilling.R;

public class ClassifyRecyclerViewAdapter extends RecyclerView.Adapter<ClassifyRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    private String[] mData;
    private int[] getBgSelected;
    private int[] getIconNor;
    private int[] getIconSelected;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private int clickTemp = 0;

    public ClassifyRecyclerViewAdapter(Context context, String[] data, int[] bgSelected, int[] iconNor, int[] iconSelected) {
        this.mContext = context;
        this.mData = data;
        this.getBgSelected = bgSelected;
        this.getIconNor = iconNor;
        this.getIconSelected = iconSelected;
    }

    public void updateData(String[] data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick((Integer)v.getTag());

        }
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public int getClickTemp(){
        return  clickTemp;
    }

    public void setClickTemp(int position){
        this.clickTemp = position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.adapter_classify, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.classify_tv.setText(mData[position]);
        holder.itemView.setTag(position);
        if (clickTemp == position){
            holder.classify_icon_bg.setBackgroundResource(R.drawable.classify_exp_icon_bg_pressed);
            holder.classify_img.setImageResource(getIconSelected[position]);
            holder.classify_tv.setTextColor(mContext.getResources().getColor(R.color.tv_selected_color));
        }else {
            holder.classify_icon_bg.setBackgroundResource(R.drawable.classify_icon_bg_nor);
            holder.classify_img.setImageResource(getIconNor[position]);
            holder.classify_tv.setTextColor(mContext.getResources().getColor(R.color.tv_nor_color));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView classify_tv;
        ImageView classify_img;
        RelativeLayout classify_icon_bg;

        public ViewHolder(View itemView) {
            super(itemView);
            classify_tv = (TextView) itemView.findViewById(R.id.classify_tv);
            classify_img = (ImageView) itemView.findViewById(R.id.classify_img);
            classify_icon_bg = (RelativeLayout) itemView.findViewById(R.id.classify_icon_bg);
        }
    }
}
