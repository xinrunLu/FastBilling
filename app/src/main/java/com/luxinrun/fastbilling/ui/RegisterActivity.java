package com.luxinrun.fastbilling.ui;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.luxinrun.fastbilling.assistent.MyUser;
import com.luxinrun.fastbilling.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    // UI references.
    private EditText edit_register_phone;
    private EditText edit_register_sms;
    private TextView btn_get_sms;
    private Button btn_finish_register;
    private ImageButton ivBtn_register_exit;

    //注册的三要素
    private String username;
    private String password;
    private boolean IS_EXIST;
    private boolean IS_GET_SMS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_register_phone = (EditText) findViewById(R.id.edit_register_phone);
        edit_register_sms = (EditText) findViewById(R.id.edit_register_sms);
        btn_get_sms = (TextView) findViewById(R.id.btn_get_sms);
        btn_get_sms.setOnClickListener(this);

        btn_finish_register = (Button) findViewById(R.id.btn_finish_register);
        btn_finish_register.setOnClickListener(this);

        ivBtn_register_exit = (ImageButton) findViewById(R.id.ivBtn_register_exit);
        ivBtn_register_exit.setOnClickListener(this);




    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {

        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    IS_GET_SMS = true;
                } else{
                    // TODO 处理错误的结果
                    IS_GET_SMS = false;
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setPassword("888888");
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
                } else{
                    // TODO 处理错误的结果
                    Log.d("lxr","短信验证失败！");
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };


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
        return username.length() == 11 ;
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void get_phone_sms() {
        // 重置错误信息为空
        edit_register_phone.setError(null);

        // 获取用户输入的手机号
        username = edit_register_phone.getText().toString();
        password = "888888";
        boolean cancel = false;
        View focusView = null;

        // 检查用户名
        if (TextUtils.isEmpty(username)) {
            edit_register_phone.setError(getString(R.string.error_field_required));
            focusView = edit_register_phone;
            cancel = true;
        }else if (!isUsernameValid(username)){
            edit_register_phone.setError(getString(R.string.error_invalid_username));
            focusView = edit_register_phone;
            cancel = true;
        }else if (isExist("username",username)) {
            edit_register_phone.setError(getString(R.string.error_exist_username));
            focusView = edit_register_phone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //获取验证码
            sendCode("86",username);
        }
    }

    private void register_account() {
        // 重置错误信息为空
        edit_register_phone.setError(null);

        // 获取用户输入的手机号
        username = edit_register_phone.getText().toString();
        password = "888888";
        boolean cancel = false;
        View focusView = null;

        // 检查用户名
        if (TextUtils.isEmpty(username)) {
            edit_register_phone.setError(getString(R.string.error_field_required));
            focusView = edit_register_phone;
            cancel = true;
        }else if (!isUsernameValid(username)){
            edit_register_phone.setError(getString(R.string.error_invalid_username));
            focusView = edit_register_phone;
            cancel = true;
        }else if (isExist("username",username)) {
            edit_register_phone.setError(getString(R.string.error_exist_username));
            focusView = edit_register_phone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // 启用Bmob注册账号
            MyUser user = new MyUser();
            user.setUsername(username);
            user.setPassword(password);
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
            case R.id.btn_get_sms:
                get_phone_sms();
                break;
            case R.id.btn_finish_register:
                if (IS_GET_SMS){
                    submitCode("86",username,edit_register_sms.getText().toString());
                }else {
                    Log.d("lxr","注册账号 失败！");
                }
                break;
            case R.id.ivBtn_register_exit:
                this.finish();
                break;

        }
    }
}

