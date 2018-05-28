package com.luxinrun.fastbilling.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.assistent.MyUser;
import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.assistent.SharedPreferencesData;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private EditText edit_login_username;
    private EditText edit_login_password;
    private Button btn_login;
    private ImageButton ivBtn_login_exit;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivBtn_login_exit = (ImageButton) findViewById(R.id.ivBtn_login_exit);
        ivBtn_login_exit.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        edit_login_username = (EditText) findViewById(R.id.edit_login_username);
        edit_login_password = (EditText) findViewById(R.id.edit_login_password);

    }


    private boolean isUsernameValid(String username) {
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void login_account() {
        // 重置错误信息为空
        edit_login_username.setError(null);
        edit_login_password.setError(null);
        // 获取用户输入的用户名，密码
        username = edit_login_username.getText().toString();
        password = edit_login_password.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // 检查用户名
        if (TextUtils.isEmpty(username)) {
            edit_login_username.setError(getString(R.string.error_field_required));
            focusView = edit_login_username;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            edit_login_username.setError(getString(R.string.error_invalid_username));
            focusView = edit_login_username;
            cancel = true;
        }

        // 检查密码
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edit_login_password.setError(getString(R.string.error_invalid_password));
            focusView = edit_login_password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            MyUser user = new MyUser();
            user.setUsername(username);
            user.setPassword(password);
            user.login(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if (e == null){
                        Toast.makeText(LoginActivity.this,R.string.success_login_cn,Toast.LENGTH_SHORT).show();
                        //登陆成功，保存为登陆状态
                        SharedPreferencesData.save_login_state(LoginActivity.this, Constant.STATE_LOGIN);
                        SharedPreferencesData.save_username(LoginActivity.this, username);
                        Intent intent = new Intent();
                        intent.putExtra("username",username);
                        intent.putExtra("password",password);
                        LoginActivity.this.setResult(Constant.LOGIN_SUCCESS, intent);
                        LoginActivity.this.finish();
                    }else {
                        Toast.makeText(LoginActivity.this,R.string.failed_login_cn,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_login:
                login_account();
                break;
            case R.id.btn_forget_password:
                break;
            case R.id.btn_have_no_account:
                break;
            case R.id.ivBtn_login_exit:
                this.finish();
                break;
        }
    }
}

