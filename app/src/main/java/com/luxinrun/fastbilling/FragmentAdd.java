package com.luxinrun.fastbilling;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import cn.bmob.v3.BmobUser;

public class FragmentAdd extends Fragment implements View.OnClickListener {

    private TextView tv_income_title;
    private TextView tv_exp_title;
    private ImageView btn_personal;

    private View view;
    private PopupWindow pop_personal_window;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment_add = inflater.inflate(R.layout.fragment_add, container, false);
        tv_income_title = (TextView) fragment_add.findViewById(R.id.tv_income_title);
        tv_income_title.setOnClickListener(this);
        tv_exp_title = (TextView) fragment_add.findViewById(R.id.tv_exp_title);
        tv_exp_title.setOnClickListener(this);
        btn_personal = (ImageView) fragment_add.findViewById(R.id.btn_personal);
        btn_personal.setOnClickListener(this);



        return fragment_add;
    }


    //弹出个人设置窗口
    private void show_pop_personal() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pop_personal, null);
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
                tv_username.setText((String)BmobUser.getObjectByKey("username"));
            } else {
                Log.d("lxr","缓存对象为空");
            }
            //游客模式显示存储的时间
        } else {
            tv_username.setText("游客"+SharedPreferencesData.get_username(getActivity()));
        }

        pop_personal_window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_exp_title:
                tv_exp_title.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_exp_title.setBackgroundResource(R.drawable.tv_exp_bg_selected);
                tv_income_title.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_income_title.setBackgroundResource(R.drawable.tv_income_bg_nor);
                break;

            case R.id.tv_income_title:
                tv_exp_title.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_exp_title.setBackgroundResource(R.drawable.tv_exp_bg_nor);
                tv_income_title.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_income_title.setBackgroundResource(R.drawable.tv_income_bg_selected);
                break;
            case R.id.btn_personal:
                show_pop_personal();
                break;
            //点击透明部分退出pop
            case R.id.pop_personal_outside:
                pop_personal_window.dismiss();
                break;
            case R.id.layout_backup:
                break;
            case R.id.layout_share:
                break;
            case R.id.layout_about:
                break;
            case R.id.layout_feedback:
                break;
        }
    }
}
