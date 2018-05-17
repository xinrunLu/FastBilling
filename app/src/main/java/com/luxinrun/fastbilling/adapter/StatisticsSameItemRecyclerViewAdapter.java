package com.luxinrun.fastbilling.adapter;

import android.app.Activity;
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

public class StatisticsSameItemRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsSameItemRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<Map<String, Object>> mData;
    private Activity mContext;
    private OnItemClickListener mOnItemClickListener;

    public StatisticsSameItemRecyclerViewAdapter(Activity context, ArrayList<Map<String, Object>> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void updateData(ArrayList<Map<String, Object>> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick((Integer) v.getTag());

        }
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.adapter_same_classify_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String date = mData.get(position).get("same_classify_date").toString();
        String summary = mData.get(position).get("same_classify_summary").toString();
        String money = mData.get(position).get("same_classify_money").toString();
        holder.same_item_num.setText((position + 1) + ".");
        holder.same_item_date.setText(date);
        holder.same_item_summary.setText(summary);
        holder.same_item_money.setText(money);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView same_item_num;
        TextView same_item_date;
        TextView same_item_summary;
        TextView same_item_money;

        public ViewHolder(View itemView) {
            super(itemView);
            same_item_num = (TextView) itemView.findViewById(R.id.same_item_num);
            same_item_date = (TextView) itemView.findViewById(R.id.same_item_date);
            same_item_summary = (TextView) itemView.findViewById(R.id.same_item_summary);
            same_item_money = (TextView) itemView.findViewById(R.id.same_item_money);
        }
    }
}
