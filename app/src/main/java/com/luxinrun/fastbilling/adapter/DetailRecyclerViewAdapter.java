package com.luxinrun.fastbilling.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.assistent.Constant;

import java.util.ArrayList;
import java.util.Map;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<Map<String, Object>> mData;
    private Activity mContext;
    private OnItemClickListener mOnItemClickListener;
    private int clickTemp = 0;

    public DetailRecyclerViewAdapter(Activity context, ArrayList<Map<String, Object>> data) {
        this.mContext = context;
        this.mData = data;

    }

    public void updateData(ArrayList<Map<String, Object>> data) {
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
        View view = View.inflate(mContext, R.layout.adapter_detail, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int[] resIds_bg = Constant.changeDrawableArray(mContext, R.array.classify_exp_bg_selected);
        int[] resIds_icon = Constant.changeDrawableArray(mContext, R.array.classify_exp_icon_selected);
        String date_time = (String)mData.get(position).get("date_time").toString();
        int exp_or_income_num = Integer.valueOf((String)mData.get(position).get("exp_or_income_num").toString());
        String exp_or_income_title = (String)mData.get(position).get("exp_or_income_title").toString();
        int classify_num = Integer.valueOf((String)mData.get(position).get("classify_num").toString());
        String classify_title = (String)mData.get(position).get("classify_title").toString();
        String money = (String)mData.get(position).get("money").toString();
        String summary = (String)mData.get(position).get("summary").toString();
        String location = (String)mData.get(position).get("location").toString();
        holder.detail_title.setText(classify_title);
        holder.detail_summary.setText(summary);
        holder.detail_money.setText(money);
        holder.detail_date.setText(date_time);
        holder.detail_icon_bg.setBackgroundResource(resIds_bg[position]);
        holder.detail_img.setImageResource(resIds_icon[position]);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView detail_money;
        TextView detail_date;
        TextView detail_title;
        TextView detail_summary;
        ImageView detail_img;
        RelativeLayout detail_icon_bg;

        public ViewHolder(View itemView) {
            super(itemView);
            detail_money = (TextView) itemView.findViewById(R.id.detail_money);
            detail_title = (TextView) itemView.findViewById(R.id.detail_title);
            detail_summary = (TextView) itemView.findViewById(R.id.detail_summary);
            detail_date = (TextView) itemView.findViewById(R.id.detail_date);
            detail_img = (ImageView) itemView.findViewById(R.id.detail_img);
            detail_icon_bg = (RelativeLayout) itemView.findViewById(R.id.detail_icon_bg);
        }
    }
}
