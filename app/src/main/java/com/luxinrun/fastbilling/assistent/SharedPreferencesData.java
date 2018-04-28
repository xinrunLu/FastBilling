package com.luxinrun.fastbilling.assistent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesData {
    /**
     * 保存登陆状态
     **/
    public static void save_login_state(Context context, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySetting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login_state",value);
        editor.commit();
    }

    /**
     * 0代表账号登陆
     * 1代表游客模式
     * 2代表第一次开启应用
     **/
    public static String get_login_state(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySetting", Activity.MODE_PRIVATE);
        String login_state = sharedPreferences.getString("login_state", Constant.STATE_FIRST);
        return login_state;
    }

    /**
     * 保存用户名
     **/
    public static void save_username(Context context, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySetting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",value);
        editor.commit();
    }

    /**
     * 获取用户名
     **/
    public static String get_username(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySetting", Activity.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "no_username");
        return username;
    }


}
