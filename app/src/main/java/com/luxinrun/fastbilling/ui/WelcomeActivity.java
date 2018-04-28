package com.luxinrun.fastbilling.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.assistent.SharedPreferencesData;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(this, "bf4b72ed939aa8a668b48e30cfacd4a2");

        final String login_state = SharedPreferencesData.get_login_state(this);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if (login_state.equals(Constant.STATE_FIRST)) {
                    intent.setClass(WelcomeActivity.this, RegisterOrLoginActivity.class);

                } else {
                    intent.setClass(WelcomeActivity.this, FragmentMain.class);

                }
                startActivity(intent);
                WelcomeActivity.this.finish();

            }
        };
        timer.schedule(task, 3000);
    }
}
