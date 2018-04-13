package com.luxinrun.fastbilling;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegeisterActivity extends AppCompatActivity implements View.OnClickListener {


    // UI references.
    private EditText edit_regeister_username;
    private EditText edit_regeister_email;
    private EditText edit_regeister_password;
    private Button btn_finish_regeister;

    //注册的三要素
    private String username;
    private String email;
    private String password;
    private boolean IS_EXIST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regeister);

        edit_regeister_username = (EditText) findViewById(R.id.edit_regesiter_username);
        edit_regeister_email = (EditText) findViewById(R.id.edit_regeister_email);
        edit_regeister_password = (EditText) findViewById(R.id.edit_regeister_password);

        btn_finish_regeister = (Button) findViewById(R.id.btn_finish_regeister);
        btn_finish_regeister.setOnClickListener(this);


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
        edit_regeister_username.setError(null);
        edit_regeister_email.setError(null);
        edit_regeister_password.setError(null);

        // 获取用户输入的用户名，邮箱，密码
        username = edit_regeister_username.getText().toString();
        email = edit_regeister_email.getText().toString();
        password = edit_regeister_password.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // 检查用户名
        if (TextUtils.isEmpty(username)) {
            edit_regeister_username.setError(getString(R.string.error_field_required));
            focusView = edit_regeister_username;
            cancel = true;
        }else if (!isUsernameValid(username)){
            edit_regeister_username.setError(getString(R.string.error_invalid_username));
            focusView = edit_regeister_username;
            cancel = true;
        }else if (isExist("username",username)) {
            edit_regeister_username.setError(getString(R.string.error_exist_username));
            focusView = edit_regeister_username;
            cancel = true;
        }

        // 检查密码
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edit_regeister_password.setError(getString(R.string.error_invalid_password));
            focusView = edit_regeister_password;
            cancel = true;
        }

        // 检查邮箱
        if (TextUtils.isEmpty(email)) {
            edit_regeister_email.setError(getString(R.string.error_field_required));
            focusView = edit_regeister_email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            edit_regeister_email.setError(getString(R.string.error_invalid_email));
            focusView = edit_regeister_email;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // 启用Bmob注册账号
            BmobUser bu = new BmobUser();
            bu.setUsername(username);
            bu.setPassword(password);
            bu.setEmail(email);
//            bu.signUp(new SaveListener<MyUser>() {
//                @Override
//                public void done(MyUser myUser, BmobException e) {
//                    if (e == null) {
//                        Log.d("lxr", "success");
//                    } else {
//                        Log.d("lxr", "fail");
//                    }
//                }
//            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish_regeister:
                regeister_account();
                break;
        }
    }
}

