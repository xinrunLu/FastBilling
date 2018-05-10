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

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<Map<String, Object>> mData;
    private Activity mContext;
    private OnItemClickListener mOnItemClickListener;
    private String expORincome;

    public StatisticsRecyclerViewAdapter(Activity context, ArrayList<Map<String, Object>> data, String expORincome) {
        this.mContext = context;
        this.mData = data;
        this.expORincome = expORincome;
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
        View view = View.inflate(mContext, R.layout.adapter_statistics, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int[] resIds_bg;
        int[] resIds_icon;
        int classify_num = Integer.valueOf(mData.get(position).get("classify_num").toString());
        String classify_title = mData.get(position).get("classify_title").toString();
        String money = mData.get(position).get("money").toString();
        holder.statistics_title.setText(classify_title);
        holder.statistics_money.setText(money);
        if (expORincome.equals("0")) {
            resIds_icon = Constant.changeDrawableArray(mContext, R.array.classify_exp_icon_selected);
            holder.statistics_icon_bg.setBackgroundResource(R.drawable.classify_exp_icon_bg_pressed);
            holder.statistics_img.setImageResource(resIds_icon[classify_num]);
        } else if (expORincome.equals("1")) {
            resIds_icon = Constant.changeDrawableArray(mContext, R.array.classify_income_icon_selected);
            holder.statistics_icon_bg.setBackgroundResource(R.drawable.classify_income_icon_bg_pressed);
            holder.statistics_img.setImageResource(resIds_icon[classify_num]);
        }


        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView statistics_money;
        TextView statistics_title;
        ImageView statistics_img;
        RelativeLayout statistics_icon_bg;

        public ViewHolder(View itemView) {
            super(itemView);
            statistics_money = (TextView) itemView.findViewById(R.id.statistics_money);
            statistics_title = (TextView) itemView.findViewById(R.id.statistics_title);
            statistics_img = (ImageView) itemView.findViewById(R.id.statistics_img);
            statistics_icon_bg = (RelativeLayout) itemView.findViewById(R.id.statistics_icon_bg);
        }
    }
}
