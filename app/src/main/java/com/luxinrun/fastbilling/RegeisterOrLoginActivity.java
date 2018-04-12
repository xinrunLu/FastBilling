package com.luxinrun.fastbilling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class RegeisterOrLoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_visitor;
    private Button btn_regeister;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester_or_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_regester_or_login);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        btn_visitor = (Button) findViewById(R.id.btn_visitor);
        btn_regeister = (Button) findViewById(R.id.btn_regeister);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_visitor.setOnClickListener(this);
        btn_regeister.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_visitor:
                intent.setClass(RegeisterOrLoginActivity.this,MainActivity.class);
                startActivity(intent);
                RegeisterOrLoginActivity.this.finish();
                break;
            case R.id.btn_regeister:
                intent.setClass(RegeisterOrLoginActivity.this,RegeisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                intent.setClass(RegeisterOrLoginActivity.this,LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}
