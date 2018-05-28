package com.luxinrun.fastbilling.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.assistent.MyUser;
import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.assistent.SharedPreferencesData;

import cn.bmob.v3.BmobUser;

public class FragmentMain extends Activity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentAdd fragmentAdd;
    private FragmentDetail fragmentDetail;
    private FragmentFind fragmentFind;

    private LinearLayout main_bottom_background;

    private View fragment_detail_layout;
    private View fragment_add_layout;
    private View fragment_find_layout;

    private ImageView fragment_detail_img;
    private ImageView fragment_add_img;
    private ImageView fragment_find_img;

    private TextView fragment_detail_tv;
    private TextView fragment_add_tv;
    private TextView fragment_find_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        main_bottom_background = (LinearLayout) findViewById(R.id.main_bottom_background);
        fragment_detail_layout = findViewById(R.id.fragment_detail_layout);
        fragment_add_layout = findViewById(R.id.fragment_add_layout);
        fragment_find_layout = findViewById(R.id.fragment_find_layout);
        fragment_detail_img = (ImageView) findViewById(R.id.fragment_detail_img);
        fragment_add_img = (ImageView) findViewById(R.id.fragment_add_img);
        fragment_find_img = (ImageView) findViewById(R.id.fragment_find_img);
        fragment_detail_tv = (TextView) findViewById(R.id.fragment_detail_tv);
        fragment_add_tv = (TextView) findViewById(R.id.fragment_add_tv);
        fragment_find_tv = (TextView) findViewById(R.id.fragment_find_tv);
        fragment_detail_layout.setOnClickListener(this);
        fragment_find_layout.setOnClickListener(this);
        fragment_add_layout.setOnClickListener(this);

        fragmentManager = getFragmentManager();
        setTabSelection(1);

        String login_state = SharedPreferencesData.get_login_state(this);
        //如果是登陆状态就判断是否过期
        if (login_state.equals(Constant.STATE_LOGIN)) {
            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            if (user != null) {
                Log.d("lxr","账号登陆模式="+login_state);

            } else {
                Log.d("lxr","缓存对象为空");
            }
        //游客模式
        } else {
            Log.d("lxr","游客="+SharedPreferencesData.get_username(this)+"/"+login_state);
        }


    }

    public void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                TextPaint paint_detail = fragment_detail_tv.getPaint();
                paint_detail.setFakeBoldText(true);
                fragment_detail_img.setImageResource(R.drawable.bottom_detail_pressed);
                fragment_detail_tv.setTextColor(getResources().getColor(R.color.colorWhite));
                main_bottom_background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (fragmentDetail == null) {
                    fragmentDetail = new FragmentDetail();
                    transaction.add(R.id.content, fragmentDetail);
                } else {
                    transaction.show(fragmentDetail);
                }
                break;
            case 1:
                TextPaint paint_add = fragment_add_tv.getPaint();
                paint_add.setFakeBoldText(true);
                fragment_add_img.setImageResource(R.drawable.bottom_add_pressed);
                fragment_add_tv.setTextColor(getResources().getColor(R.color.colorWhite));
                main_bottom_background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (fragmentAdd == null) {
                    fragmentAdd = new FragmentAdd();
                    transaction.add(R.id.content, fragmentAdd);
                } else {
                    transaction.show(fragmentAdd);
                }
                break;
            case 2:
                TextPaint paint_find = fragment_find_tv.getPaint();
                paint_find.setFakeBoldText(true);
                fragment_find_img.setImageResource(R.drawable.bottom_find_pressed);
                fragment_find_tv.setTextColor(getResources().getColor(R.color.colorWhite));
                main_bottom_background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (fragmentFind == null) {
                    fragmentFind = new FragmentFind();
                    transaction.add(R.id.content, fragmentFind);
                } else {
                    transaction.show(fragmentFind);
                }
                break;

        }
        transaction.commit();
    }

    private void clearSelection() {
        fragment_detail_img.setImageResource(R.drawable.bottom_detail_nor);
        fragment_add_img.setImageResource(R.drawable.bottom_add_nor);
        fragment_find_img.setImageResource(R.drawable.bottom_find_nor);
        fragment_detail_tv.setTextColor(getResources().getColor(R.color.tv_bottom_nor_color));
        fragment_add_tv.setTextColor(getResources().getColor(R.color.tv_bottom_nor_color));
        fragment_find_tv.setTextColor(getResources().getColor(R.color.tv_bottom_nor_color));
        TextPaint paint_detail = fragment_detail_tv.getPaint();
        TextPaint paint_add = fragment_add_tv.getPaint();
        TextPaint paint_find = fragment_find_tv.getPaint();
        paint_detail.setFakeBoldText(false);
        paint_add.setFakeBoldText(false);
        paint_find.setFakeBoldText(false);

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fragmentDetail != null) {
            transaction.hide(fragmentDetail);
        }
        if (fragmentAdd != null) {
            transaction.hide(fragmentAdd);
        }
        if (fragmentFind != null) {
            transaction.hide(fragmentFind);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_detail_layout:
                setTabSelection(0);
                break;
            case R.id.fragment_add_layout:
                setTabSelection(1);
                break;
            case R.id.fragment_find_layout:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }
}
