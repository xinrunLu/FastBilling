package com.luxinrun.fastbilling.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.assistent.Constant;

import java.util.ArrayList;
import java.util.Map;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeadView;

    private ArrayList<Map<String, Object>> mData;
    private Activity mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
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
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick((Integer) v.getTag());

        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null){
            mOnItemLongClickListener.onItemLongClick((Integer) v.getTag());
        }
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }


    public void setHeadView(View headView) {
        mHeadView = headView;
        notifyItemInserted(0);
    }

    public View getmHeadView() {
        return mHeadView;
    }

    public int getClickTemp() {
        return clickTemp;
    }

    public void setClickTemp(int position) {
        this.clickTemp = position;
    }


    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeadView == null ? position : position - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeadView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeadView);
        View view = View.inflate(mContext, R.layout.adapter_detail, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);
        int[] resIds_bg;
        int[] resIds_icon;
//        Log.d("lxr","po=popition"+pos+"-"+position);
        String date_time = (String) mData.get(pos).get("date_time").toString();
        int exp_or_income_num = Integer.valueOf((String) mData.get(pos).get("exp_or_income_num").toString());
        String exp_or_income_title = (String) mData.get(pos).get("exp_or_income_title").toString();
        int classify_num = Integer.valueOf((String) mData.get(pos).get("classify_num").toString());
        String classify_title = (String) mData.get(pos).get("classify_title").toString();
        String money = (String) mData.get(pos).get("money").toString();
        String summary = (String) mData.get(pos).get("summary").toString();
        String location = (String) mData.get(pos).get("location").toString();
        holder.detail_title.setText(classify_title);
        holder.detail_summary.setText(summary);
        holder.detail_money.setText(money);
        holder.detail_date.setText(date_time);
        if (exp_or_income_num == 0) {
            resIds_bg = Constant.changeDrawableArray(mContext, R.array.classify_exp_bg_selected);
            resIds_icon = Constant.changeDrawableArray(mContext, R.array.classify_exp_icon_selected);
            holder.detail_icon_bg.setBackgroundResource(resIds_bg[classify_num]);
            holder.detail_img.setImageResource(resIds_icon[classify_num]);
        } else if (exp_or_income_num == 1) {
            resIds_bg = Constant.changeDrawableArray(mContext, R.array.classify_income_bg_selected);
            resIds_icon = Constant.changeDrawableArray(mContext, R.array.classify_income_icon_selected);
            holder.detail_icon_bg.setBackgroundResource(resIds_bg[classify_num]);
            holder.detail_img.setImageResource(resIds_icon[classify_num]);
        }
        holder.itemView.setTag(pos);
    }

    @Override
    public int getItemCount() {
        return mData == null ? mData.size() : mData.size() + 1;
//        return mData == null ? 0 : mData.size();
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
