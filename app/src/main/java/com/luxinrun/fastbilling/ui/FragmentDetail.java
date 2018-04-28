package com.luxinrun.fastbilling.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.adapter.DetailRecyclerViewAdapter;
import com.luxinrun.fastbilling.assistent.DBHelper;

import java.util.ArrayList;
import java.util.Map;

public class FragmentDetail extends Fragment {

    private DetailRecyclerViewAdapter detailRecyclerViewAdapter;
    private RecyclerView detail_recyclerView;
    private DBHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment_detail = inflater.inflate(R.layout.fragment_detail, container, false);

        detail_recyclerView = (RecyclerView) fragment_detail.findViewById(R.id.detail_recyclerView);

        ArrayList<Map<String, Object>> data;
        dbHelper = new DBHelper(getActivity());
        data = dbHelper.cursorList();
        for (int i = 0; i < data.size();i++ ) {
            Log.d("lxr", (String)data.get(i).get("date_time").toString());
            Log.d("lxr", (String)data.get(i).get("exp_or_income_num").toString());
            Log.d("lxr", (String)data.get(i).get("exp_or_income_title").toString());
            Log.d("lxr", (String)data.get(i).get("classify_num").toString());
            Log.d("lxr", (String)data.get(i).get("classify_title").toString());
            Log.d("lxr", (String)data.get(i).get("money").toString());
            Log.d("lxr", (String)data.get(i).get("summary").toString());
            Log.d("lxr", (String)data.get(i).get("location").toString());
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(false);
        manager.setOrientation(LinearLayout.VERTICAL);
        detail_recyclerView.setLayoutManager(manager);
        detailRecyclerViewAdapter = new DetailRecyclerViewAdapter(getActivity(), data);
        detail_recyclerView.setAdapter(detailRecyclerViewAdapter);


        return fragment_detail;
    }
}
