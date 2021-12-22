package com.lpc.smartlife.views.userloginabout;

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

public class ForgetActivity extends BaseActivity {

    EditText editTextAccount;
    EditText editTextNewPassword;
    EditText editTextRePassword;
    EditText editTextCode;
    Button btnGetCode;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        setImmersiveWindows();
        initToolbar();
        initViews();
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


    private void initViews() {
        editTextAccount = findViewById(R.id.editTextAccount);
        editTextNewPassword = findViewById(R.id.editTextPassword);
        editTextRePassword = findViewById(R.id.editTextRePassword);
        editTextCode = findViewById(R.id.editTextCode);
        btnGetCode = findViewById(R.id.btnGetCode);
        btnSubmit = findViewById(R.id.btnUserRegister);

        editTextNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextAccount.getText().toString().trim().length() == 0) {
                    Tools.displayToast(ForgetActivity.this, "账号不能为空");
                } else if (editTextNewPassword.getText().toString().trim().length() == 0) {
                    Tools.displayToast(ForgetActivity.this, "新密码不能为空");
                } else if (!editTextNewPassword.getText().toString().trim().equals(editTextRePassword.getText().toString().trim())) {
                    Tools.displayToast(ForgetActivity.this, "两次密码不一致");
                } else if (editTextCode.getText().toString().trim().length() == 0) {
                    Tools.displayToast(ForgetActivity.this, "验证码不能为空");
                } else {
                    doSubmit();
                }
            }
        });
    }

    private void doSubmit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection connection = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", editTextAccount.getText().toString());
                json.put("newPassword", editTextNewPassword.getText().toString());
                json.put("vcode", editTextCode.getText().toString());
                String response = connection.doPost("http://ysdk.kystu.cn/api/userForgetPassword/", json);
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
                json.put("type", 1);
                String response = connection.doPost("http://ysdk.kystu.cn/api/sendVertifyCode/", json);
                LoginEventMessage message = JSONObject.parseObject(response, LoginEventMessage.class);
                message.setFlag(1);
                EventBus.getDefault().post(message);
            }
        }).start();
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
        if (msg.getCode() != 0) {
            Tools.displayToast(this, msg.getInfo());
        } else {
            Tools.displayToast(ForgetActivity.this, msg.getInfo());
            if (msg.getFlag() == 0) {
                finish();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
    }
}