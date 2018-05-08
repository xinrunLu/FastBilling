package com.luxinrun.fastbilling.ui;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment_detail = inflater.inflate(R.layout.fragment_detail, container, false);

        initID(fragment_detail);

        refreshData("0");


        detailRecyclerViewAdapter.setOnItemClickListener(new DetailRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                detailRecyclerViewAdapter.deletItem(position);
                Log.d("lxr",position+"!");
            }


        });

        return fragment_detail;
    }

    private void initID(View fragment_detail) {
        detail_recyclerView = (RecyclerView) fragment_detail.findViewById(R.id.detail_recyclerView);
    }


    private void refreshData(String expORincome) {

        dbHelper = new DBHelper(getActivity());
        data = dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), expORincome);
        for (int i = 0; i < data.size(); i++) {
            Log.d("lxr", (String) data.get(i).get("date_time").toString());
            Log.d("lxr", (String) data.get(i).get("exp_or_income_num").toString());
            Log.d("lxr", (String) data.get(i).get("exp_or_income_title").toString());
            Log.d("lxr", (String) data.get(i).get("classify_num").toString());
            Log.d("lxr", (String) data.get(i).get("classify_title").toString());
            Log.d("lxr", (String) data.get(i).get("money").toString());
            Log.d("lxr", (String) data.get(i).get("summary").toString());
            Log.d("lxr", (String) data.get(i).get("location").toString());
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(false);
        manager.setOrientation(LinearLayout.VERTICAL);
        detail_recyclerView.setLayoutManager(manager);
        detailRecyclerViewAdapter = new DetailRecyclerViewAdapter(getActivity(), data);
        detail_recyclerView.setAdapter(detailRecyclerViewAdapter);
        setHeader(detail_recyclerView, expORincome);
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
        detail_exp_title.setText((CURRENT_MONTH + 1) + getString(R.string.month_exp));
        detail_exp_tv.setText(Constant.get_totle_money(dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), "0")));
        detail_income_title.setText((CURRENT_MONTH + 1) + getString(R.string.month_income));
        detail_income_tv.setText(Constant.get_totle_money(dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), "1")));
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
            refreshData("0");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_detail_income:
                refreshData("1");
                break;
            case R.id.layout_detail_exp:
                refreshData("0");
                break;

        }
    }
}
