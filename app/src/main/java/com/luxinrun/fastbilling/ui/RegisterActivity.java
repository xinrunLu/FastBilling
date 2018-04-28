package com.luxinrun.fastbilling.ui;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.luxinrun.fastbilling.assistent.MyUser;
import com.luxinrun.fastbilling.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    // UI references.
    private EditText edit_register_username;
    private EditText edit_register_email;
    private EditText edit_register_password;
    private Button btn_finish_register;
    private ImageButton ivBtn_register_exit;

    //注册的三要素
    private String username;
    private String email;
    private String password;
    private boolean IS_EXIST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_register_username = (EditText) findViewById(R.id.edit_register_username);
        edit_register_email = (EditText) findViewById(R.id.edit_register_email);
        edit_register_password = (EditText) findViewById(R.id.edit_register_password);

        btn_finish_register = (Button) findViewById(R.id.btn_finish_register);
        btn_finish_register.setOnClickListener(this);

        ivBtn_register_exit = (ImageButton) findViewById(R.id.ivBtn_register_exit);
        ivBtn_register_exit.setOnClickListener(this);




    }

    /**
     * 检查输入的用户名，邮箱是否已经注册过
     * @param tag
     * @return
     */
    private boolean isExist(String tag, String text) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo(tag, text);
        query.findObjects(new FindListener<MyUser>() {

            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        Log.d("lxr", "无重复");
                        IS_EXIST = false;
                    } else {
                        Log.d("lxr", "有相同");
                        IS_EXIST = true;
                    }
                } else {
                    Log.d("lxr", "抛出异常，请检查");
                    IS_EXIST = true;
                }
            }

        });
        Log.d("lxr", "IS_EXIST="+IS_EXIST);
        return IS_EXIST;
    }

    private boolean isUsernameValid(String username) {
        return username.length() > 4 ;
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void regeister_account() {
        // 重置错误信息为空
        edit_register_username.setError(null);
        edit_register_email.setError(null);
        edit_register_password.setError(null);

        // 获取用户输入的用户名，邮箱，密码
        username = edit_register_username.getText().toString();
        email = edit_register_email.getText().toString();
        password = edit_register_password.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // 检查用户名
        if (TextUtils.isEmpty(username)) {
            edit_register_username.setError(getString(R.string.error_field_required));
            focusView = edit_register_username;
            cancel = true;
        }else if (!isUsernameValid(username)){
            edit_register_username.setError(getString(R.string.error_invalid_username));
            focusView = edit_register_username;
            cancel = true;
        }else if (isExist("username",username)) {
            edit_register_username.setError(getString(R.string.error_exist_username));
            focusView = edit_register_username;
            cancel = true;
        }

        // 检查密码
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edit_register_password.setError(getString(R.string.error_invalid_password));
            focusView = edit_register_password;
            cancel = true;
        }

        // 检查邮箱
        if (TextUtils.isEmpty(email)) {
            edit_register_email.setError(getString(R.string.error_field_required));
            focusView = edit_register_email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            edit_register_email.setError(getString(R.string.error_invalid_email));
            focusView = edit_register_email;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // 启用Bmob注册账号
            MyUser user = new MyUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.signUp(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if (e == null) {
                        Toast.makeText(RegisterActivity.this,R.string.success_register,Toast.LENGTH_SHORT).show();
                        RegisterActivity.this.finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,R.string.failed_register,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish_register:
                regeister_account();
                break;
            case R.id.ivBtn_register_exit:
                this.finish();
                break;

        }
    }
}

