package com.lpc.smartlife.userloginabout;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.message.LoginEventMessage;
import com.lpc.smartlife.utils.MyHttpConnection;
import com.lpc.smartlife.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterActivity extends BaseActivity {

    EditText editTextAccount;
    EditText editTextPassword;
    EditText editTextPassword2;
    EditText editTextCode;
    Button btnGetCode;
    Button btnUserRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setImmersiveWindows();
        initToolbar();
        initViews();
    }

    public void initViews() {
        editTextAccount = findViewById(R.id.editTextAccount);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        editTextCode = findViewById(R.id.editTextCode);
        btnGetCode = findViewById(R.id.btnGetCode);
        btnUserRegister = findViewById(R.id.btnUserRegister);

        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        /**
         * 获取验证码
         */
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVCode();
            }
        });

        /**
         * 提交修改
         */
        btnUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextAccount.getText().toString().trim().length() == 0) {
                    Tools.displayToast(RegisterActivity.this, "账号不能为空");
                } else if (editTextPassword.getText().toString().trim().length() == 0) {
                    Tools.displayToast(RegisterActivity.this, "密码不能为空");
                } else if (!editTextPassword.getText().toString().trim().equals(editTextPassword2.getText().toString().trim())) {
                    Tools.displayToast(RegisterActivity.this, "两次密码不一致");
                } else if (editTextCode.getText().toString().trim().length() == 0) {
                    Tools.displayToast(RegisterActivity.this, "验证码不能为空");
                } else {
                    doRegister();
                }
            }
        });
    }

    private void doRegister() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection connection = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", editTextAccount.getText().toString());
                json.put("password", editTextPassword.getText().toString());
                json.put("vcode", editTextCode.getText().toString());
                String response = connection.doPost("http://ysdk.kystu.cn/api/userRegister/", json);
                LoginEventMessage message = JSONObject.parseObject(response, LoginEventMessage.class);
                message.setFlag(0);
                EventBus.getDefault().post(message);
            }
        }).start();
    }

    private void getVCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection connection = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", editTextAccount.getText().toString());
                json.put("type", 0);
                String response = connection.doPost("http://ysdk.kystu.cn/api/sendVertifyCode/", json);
                LoginEventMessage message = JSONObject.parseObject(response, LoginEventMessage.class);
                message.setFlag(1);
                EventBus.getDefault().post(message);
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSubmit(LoginEventMessage msg) {
        Tools.displayToast(RegisterActivity.this, msg.getInfo());
        if (msg.getFlag() == 0) {
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }
}