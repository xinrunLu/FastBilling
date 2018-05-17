package com.luxinrun.fastbilling.ui;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.adapter.DetailRecyclerViewAdapter;
import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.assistent.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class FragmentDetail extends Fragment implements View.OnClickListener {

    Calendar calendar = Calendar.getInstance();
    private int CURRENT_YEAR = calendar.get(Calendar.YEAR);
    private int CURRENT_MONTH = calendar.get(Calendar.MONTH);
    private int CURRENT_DAY = calendar.get(Calendar.DAY_OF_MONTH);
    private DetailRecyclerViewAdapter detailRecyclerViewAdapter;
    private RecyclerView detail_recyclerView;
    private DBHelper dbHelper;
    private ArrayList<Map<String, Object>> data;

    private TextView detail_budget_tv;
    private TextView detail_budget_title;
    private TextView detail_income_tv;
    private TextView detail_income_title;
    private TextView detail_exp_tv;
    private TextView detail_exp_title;
    private ImageView btn_statistics;

    private PopupWindow pop_delete_window;
    private String long_click_position;
    private String expORincome = "0";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment_detail = inflater.inflate(R.layout.fragment_detail, container, false);

        initID(fragment_detail);

        refreshData(expORincome);


        return fragment_detail;
    }

    private void initID(View fragment_detail) {
        detail_recyclerView = (RecyclerView) fragment_detail.findViewById(R.id.detail_recyclerView);
        btn_statistics = (ImageView) fragment_detail.findViewById(R.id.btn_statistics);
        btn_statistics.setOnClickListener(this);
    }


    private void refreshData(String expORincome) {

        dbHelper = new DBHelper(getActivity());
        data = dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), expORincome);
//        for (int i = 0; i < data.size(); i++) {
//            Log.d("lxr", (String) data.get(i).get("date_time").toString());
//            Log.d("lxr", (String) data.get(i).get("exp_or_income_num").toString());
//            Log.d("lxr", (String) data.get(i).get("exp_or_income_title").toString());
//            Log.d("lxr", (String) data.get(i).get("classify_num").toString());
//            Log.d("lxr", (String) data.get(i).get("classify_title").toString());
//            Log.d("lxr", (String) data.get(i).get("money").toString());
//            Log.d("lxr", (String) data.get(i).get("summary").toString());
//            Log.d("lxr", (String) data.get(i).get("location").toString());
//        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(false);
        manager.setOrientation(LinearLayout.VERTICAL);
        detail_recyclerView.setLayoutManager(manager);
        detailRecyclerViewAdapter = new DetailRecyclerViewAdapter(getActivity(), data);
        detail_recyclerView.setAdapter(detailRecyclerViewAdapter);
        setHeader(detail_recyclerView, expORincome);

        detailRecyclerViewAdapter.setOnItemLongClickListener(new DetailRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                show_delete_pop(position);
            }
        });
    }

    private void show_delete_pop(int position){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_delete, null);
        pop_delete_window = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        pop_delete_window.setFocusable(true);
        pop_delete_window.showAtLocation(getActivity().findViewById(R.id.detail_recyclerView), Gravity.CENTER_VERTICAL, 0, 0);
        pop_delete_window.setOutsideTouchable(true);
        TextView delete_title = (TextView) view.findViewById(R.id.delete_title);
        LinearLayout delete_btn_layout = (LinearLayout) view.findViewById(R.id.delete_btn_layout);
        RelativeLayout delete_null_layout = (RelativeLayout) view.findViewById(R.id.delete_null_layout);
        delete_btn_layout.setOnClickListener(this);
        delete_null_layout.setOnClickListener(this);
        String title = data.get(position).get("classify_title").toString();
        String money = data.get(position).get("money").toString();
        long_click_position = data.get(position).get("_id").toString();
        expORincome = data.get(position).get("exp_or_income_num").toString();
        delete_title.setText(title+"("+money+")");
    }

    private void delete_one_data(String position){
        dbHelper.delet(position);
        pop_delete_window.dismiss();
        refreshData(expORincome);
    }

    private void setHeader(RecyclerView view, String expORincome) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_detail, view, false);
        detailRecyclerViewAdapter.setHeadView(header);
        LinearLayout layout_detail_income = (LinearLayout) header.findViewById(R.id.layout_detail_income);
        LinearLayout layout_detail_exp = (LinearLayout) header.findViewById(R.id.layout_detail_exp);
        detail_budget_tv = (TextView) header.findViewById(R.id.detail_budget_tv);
        detail_budget_title = (TextView) header.findViewById(R.id.detail_budget_title);
        detail_income_tv = (TextView) header.findViewById(R.id.detail_income_tv);
        detail_income_title = (TextView) header.findViewById(R.id.detail_income_title);
        detail_exp_tv = (TextView) header.findViewById(R.id.detail_exp_tv);
        detail_exp_title = (TextView) header.findViewById(R.id.detail_exp_title);
        detail_budget_title.setText((CURRENT_MONTH + 1) + getString(R.string.month_budget));
        detail_budget_tv.setOnClickListener(this);
        detail_exp_title.setText((CURRENT_MONTH + 1) + getString(R.string.month_exp));
        String total_exp = Constant.get_totle_money(dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), "0"));
        detail_exp_tv.setText(total_exp);
        detail_income_title.setText((CURRENT_MONTH + 1) + getString(R.string.month_income));
        detail_income_tv.setText(Constant.get_totle_money(dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), "1")));
        detail_budget_tv.setText((10000.00-Float.parseFloat(total_exp))+"");
        if (expORincome.equals("0")){
            detail_exp_tv.setTextColor(getResources().getColor(R.color.num_exp_color));
            detail_exp_title.setTextColor(getResources().getColor(R.color.colorWhite));
            detail_income_tv.setTextColor(getResources().getColor(R.color.tv_nor_color));
            detail_income_title.setTextColor(getResources().getColor(R.color.tv_nor_color));
        }else if (expORincome.equals("1")){
            detail_income_tv.setTextColor(getResources().getColor(R.color.num_income_color));
            detail_income_title.setTextColor(getResources().getColor(R.color.colorWhite));
            detail_exp_tv.setTextColor(getResources().getColor(R.color.tv_nor_color));
            detail_exp_title.setTextColor(getResources().getColor(R.color.tv_nor_color));
        }
        layout_detail_income.setOnClickListener(this);
        layout_detail_exp.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d("lxr", "FragmentDetail=隐藏了");
        } else {
            Log.d("lxr", "FragmentDetail=显示了");
            refreshData(expORincome);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_detail_income:
                expORincome = "1";
                refreshData(expORincome);
                break;
            case R.id.layout_detail_exp:
                expORincome = "0";
                refreshData(expORincome);
                break;
            case R.id.detail_budget_tv:

                break;
            case R.id.delete_null_layout:
                pop_delete_window.dismiss();
                break;
            case R.id.delete_btn_layout:
                delete_one_data(long_click_position);
                break;
            case R.id.btn_statistics:
                Intent intent = new Intent();
                intent.setClass(getActivity(), StatisticsActivity.class);
                startActivity(intent);
                break;

        }
    }
}
