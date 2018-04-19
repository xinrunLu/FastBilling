package com.luxinrun.fastbilling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class RegisterOrLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_visitor;
    private Button btn_register;
    private Button btn_login;
    private ImageButton ivBtn_app_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register_or_login);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        btn_visitor = (Button) findViewById(R.id.btn_visitor);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        ivBtn_app_exit = (ImageButton) findViewById(R.id.ivBtn_app_exit);
        ivBtn_app_exit.setOnClickListener(this);
        btn_visitor.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesData.get_login_state(this).equals(Constant.STATE_LOGIN)){
            Intent intent = new Intent();
            intent.setClass(this, FragmentMain.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_visitor:
                //游客模式下进入用户名默认为当前的时间
                String username = String.valueOf(System.currentTimeMillis()/1000);
                //保存与游客用户名
                SharedPreferencesData.save_username(this, username);
                //保存游客模式
                SharedPreferencesData.save_login_state(this, Constant.STATE_VISITOR);
                //跳转到主界面
                intent.setClass(this, FragmentMain.class);
                startActivity(intent);
                RegisterOrLoginActivity.this.finish();
                break;
            case R.id.btn_register:
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ivBtn_app_exit:
                this.finish();
                break;
        }
    }
}
