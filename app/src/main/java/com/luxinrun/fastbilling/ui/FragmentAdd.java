package com.luxinrun.fastbilling.ui;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luxinrun.fastbilling.adapter.ClassifyRecyclerViewAdapter;
import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.assistent.DBHelper;
import com.luxinrun.fastbilling.assistent.MyUser;
import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.assistent.SharedPreferencesData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import cn.bmob.v3.BmobUser;

public class FragmentAdd extends Fragment implements View.OnClickListener {

    private DBHelper dbHelper;

    private TextView tv_add_input;
    private TextView tv_income_title;
    private TextView tv_exp_title;
    private EditText edit_add_summary;
    private ImageView btn_personal;
    private ImageView btn_finish;
    private Button num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, num_0, num_comma, num_del;

    private RecyclerView classify_recyclerView;
    private RecyclerView.LayoutManager gridLayoutManager;
    private ClassifyRecyclerViewAdapter classifyRecyclerViewAdapter;

    private PopupWindow pop_personal_window;
    //插入数据库的所需内容
    private String[] classify_detail_choose_title;
    private String add_date;
    private int add_expORincome_num = 0;//默认进来为0，为支出
    private String add_expORincome_title;
    private int add_classify_num = 0;
    private String add_classify_title;
    private String add_money = "0.00";
    private String add_summary;
    private String add_location;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment_add = inflater.inflate(R.layout.fragment_add, container, false);
        num_1 = (Button) fragment_add.findViewById(R.id.num_1);
        num_1.setOnClickListener(this);
        num_2 = (Button) fragment_add.findViewById(R.id.num_2);
        num_2.setOnClickListener(this);
        num_3 = (Button) fragment_add.findViewById(R.id.num_3);
        num_3.setOnClickListener(this);
        num_4 = (Button) fragment_add.findViewById(R.id.num_4);
        num_4.setOnClickListener(this);
        num_5 = (Button) fragment_add.findViewById(R.id.num_5);
        num_5.setOnClickListener(this);
        num_6 = (Button) fragment_add.findViewById(R.id.num_6);
        num_6.setOnClickListener(this);
        num_7 = (Button) fragment_add.findViewById(R.id.num_7);
        num_7.setOnClickListener(this);
        num_8 = (Button) fragment_add.findViewById(R.id.num_8);
        num_8.setOnClickListener(this);
        num_9 = (Button) fragment_add.findViewById(R.id.num_9);
        num_9.setOnClickListener(this);
        num_0 = (Button) fragment_add.findViewById(R.id.num_0);
        num_0.setOnClickListener(this);
        num_comma = (Button) fragment_add.findViewById(R.id.num_comma);
        num_comma.setOnClickListener(this);
        num_del = (Button) fragment_add.findViewById(R.id.num_del);
        num_del.setOnClickListener(this);

        tv_income_title = (TextView) fragment_add.findViewById(R.id.tv_income_title);
        tv_income_title.setOnClickListener(this);
        tv_exp_title = (TextView) fragment_add.findViewById(R.id.tv_exp_title);
        tv_exp_title.setOnClickListener(this);
        tv_add_input = (TextView) fragment_add.findViewById(R.id.tv_add_input);
        edit_add_summary = (EditText) fragment_add.findViewById(R.id.edit_add_summary);
        btn_personal = (ImageView) fragment_add.findViewById(R.id.btn_personal);
        btn_personal.setOnClickListener(this);
        btn_finish = (ImageView) fragment_add.findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);
        classify_recyclerView = (RecyclerView) fragment_add.findViewById(R.id.classify_recyclerView);

        //每次进来就默认为add_expORincome_num = 0；
        showExpOrIncomeData(add_expORincome_num);


        return fragment_add;
    }

    /**
     * @param which 0代表支出 1代表收入
     */
    private void showExpOrIncomeData(int which) {
        add_classify_num = 0;
        add_money = "0.00";
        tv_add_input.setText(add_money);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        gridLayoutManager.setReverseLayout(false);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        classify_recyclerView.setLayoutManager(gridLayoutManager);
        if (which == 0) {
            tv_exp_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv_exp_title.setBackgroundResource(R.drawable.tv_exp_bg_selected);
            tv_income_title.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_income_title.setBackgroundResource(R.drawable.tv_income_bg_nor);
            add_expORincome_num = 0;
            add_expORincome_title = getActivity().getString(R.string.title_exp);
            classify_detail_choose_title = Constant.changeStringArray(getActivity(), R.array.classify_title_exp);
            classifyRecyclerViewAdapter = new ClassifyRecyclerViewAdapter(getActivity(),
                    classify_detail_choose_title,
                    Constant.changeDrawableArray(getActivity(), R.array.classify_exp_bg_selected),
                    Constant.changeDrawableArray(getActivity(), R.array.classify_exp_icon_nor),
                    Constant.changeDrawableArray(getActivity(), R.array.classify_exp_icon_selected));
        } else if (which == 1) {
            tv_exp_title.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_exp_title.setBackgroundResource(R.drawable.tv_exp_bg_nor);
            tv_income_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv_income_title.setBackgroundResource(R.drawable.tv_income_bg_selected);
            add_expORincome_num = 1;
            add_expORincome_title = getActivity().getString(R.string.title_income);
            classify_detail_choose_title = Constant.changeStringArray(getActivity(), R.array.classify_title_income);
            classifyRecyclerViewAdapter = new ClassifyRecyclerViewAdapter(getActivity(),
                    classify_detail_choose_title,
                    Constant.changeDrawableArray(getActivity(), R.array.classify_income_bg_selected),
                    Constant.changeDrawableArray(getActivity(), R.array.classify_income_icon_nor),
                    Constant.changeDrawableArray(getActivity(), R.array.classify_income_icon_selected));
        }
        classify_recyclerView.setAdapter(classifyRecyclerViewAdapter);
        classifyRecyclerViewAdapter.setOnItemClickListener(new ClassifyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                add_classify_num = position;
                classifyRecyclerViewAdapter.setClickTemp(position);
                classifyRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }


    //弹出个人设置窗口
    private void show_pop_personal() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_personal, null);
        pop_personal_window = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        pop_personal_window.setFocusable(true);
        pop_personal_window.showAtLocation(getActivity().findViewById(R.id.btn_personal), Gravity.CENTER_VERTICAL, 0, 0);
        pop_personal_window.setOutsideTouchable(true);
        RelativeLayout layout_backup = (RelativeLayout) view.findViewById(R.id.layout_backup);
        layout_backup.setOnClickListener(this);
        RelativeLayout layout_share = (RelativeLayout) view.findViewById(R.id.layout_share);
        layout_share.setOnClickListener(this);
        RelativeLayout layout_about = (RelativeLayout) view.findViewById(R.id.layout_about);
        layout_about.setOnClickListener(this);
        RelativeLayout layout_feedback = (RelativeLayout) view.findViewById(R.id.layout_feedback);
        layout_feedback.setOnClickListener(this);
        LinearLayout pop_personal_outside = (LinearLayout) view.findViewById(R.id.pop_personal_outside);
        pop_personal_outside.setOnClickListener(this);
        TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
        //如果是登陆状态就显示用户名
        String login_state = SharedPreferencesData.get_login_state(getActivity());
        if (login_state.equals(Constant.STATE_LOGIN)) {
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            if (user != null) {
                tv_username.setText((String) BmobUser.getObjectByKey("username"));
            } else {
                Log.d("lxr", "缓存对象为空");
            }
            //游客模式显示存储的时间
        } else {
            tv_username.setText("游客" + SharedPreferencesData.get_username(getActivity()));
        }

        pop_personal_window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    //插入一条数据
    private void insertData() {
        dbHelper = new DBHelper(getActivity());
        ContentValues values = new ContentValues();
        if (add_money.equals("") || add_money.equals("0") || add_money.equals("0.0") || add_money.equals("0.00")) {
            Toast.makeText(getActivity(), R.string.error_money, Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(edit_add_summary.getText())) {
                add_summary = edit_add_summary.getText().toString();
            } else {
                add_summary = getActivity().getString(R.string.no_summary);
            }
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            add_date = df.format(day);
            values.put("date_time", add_date);
            values.put("exp_or_income_num", add_expORincome_num + "");
            values.put("exp_or_income_title", add_expORincome_title);
            values.put("classify_num", add_classify_num + "");
            values.put("classify_title", classify_detail_choose_title[add_classify_num]);
            values.put("money", add_money);
            values.put("summary", add_summary);
            values.put("location", "无锡");
            Log.d("lxr", "date_time=" + add_date);
            Log.d("lxr", "exp_or_income_num=" + add_expORincome_num + "");
            Log.d("lxr", "exp_or_income_title=" + add_expORincome_title);
            Log.d("lxr", "classify_num=" + add_classify_num + "");
            Log.d("lxr", "classify_title=" + classify_detail_choose_title[add_classify_num]);
            Log.d("lxr", "money=" + add_money);
            Log.d("lxr", "summary=" + add_summary);
            Log.d("lxr", "location=" + "无锡");
            dbHelper.insert(values);
            //数据恢复成初始数据
            edit_add_summary.setText("");
            classifyRecyclerViewAdapter.setClickTemp(0);
            classifyRecyclerViewAdapter.notifyDataSetChanged();
            add_classify_num = 0;
            add_money = "0.00";
            tv_add_input.setText(add_money);
        }
    }

    @Override
    public void onClick(View view) {
        if (add_money.equals("0.00")) {
            add_money = "";
        }
        switch (view.getId()) {
            case R.id.tv_exp_title:
                showExpOrIncomeData(0);
                break;
            case R.id.tv_income_title:
                showExpOrIncomeData(1);
                break;
            case R.id.btn_finish:
                insertData();
                break;
            case R.id.btn_personal:
                show_pop_personal();
                break;
            //点击透明部分退出pop
            case R.id.pop_personal_outside:
                pop_personal_window.dismiss();
                break;
            case R.id.layout_backup:
                pop_personal_window.dismiss();
                break;
            case R.id.layout_share:
                pop_personal_window.dismiss();
                break;
            case R.id.layout_about:
                pop_personal_window.dismiss();
                break;
            case R.id.layout_feedback:
                pop_personal_window.dismiss();
                break;
            case R.id.num_0:
                add_money = Constant.only_two_bit(add_money, "0");
                break;
            case R.id.num_1:
                add_money = Constant.only_two_bit(add_money, "1");
                break;
            case R.id.num_2:
                add_money = Constant.only_two_bit(add_money, "2");
                break;
            case R.id.num_3:
                add_money = Constant.only_two_bit(add_money, "3");
                break;
            case R.id.num_4:
                add_money = Constant.only_two_bit(add_money, "4");
                break;
            case R.id.num_5:
                add_money = Constant.only_two_bit(add_money, "5");
                break;
            case R.id.num_6:
                add_money = Constant.only_two_bit(add_money, "6");
                break;
            case R.id.num_7:
                add_money = Constant.only_two_bit(add_money, "7");
                break;
            case R.id.num_8:
                add_money = Constant.only_two_bit(add_money, "8");
                break;
            case R.id.num_9:
                add_money = Constant.only_two_bit(add_money, "9");
                break;
            case R.id.num_comma:
                add_money = Constant.have_comma_or_not(add_money);
                break;
            case R.id.num_del:
                add_money = Constant.del_num(add_money);
                break;
        }
        if (add_money.equals("")) {
            add_money = "0.00";
        }
        tv_add_input.setText(add_money);
    }
}
