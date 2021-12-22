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

public class ChangePasswordActivity extends BaseActivity {

    EditText editTextAccount;
    EditText editTextPassword;
    EditText editTextNewPassword;
    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setImmersiveWindows();
        initToolbar();
        initViews();
    }

    public void initViews() {
        editTextAccount = findViewById(R.id.editTextAccount);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNewPassword = findViewById(R.id.editTextCode);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextAccount.getText().toString().trim().length() == 0) {
                    Tools.displayToast(ChangePasswordActivity.this, "账号不能为空");
                } else if (editTextPassword.getText().toString().trim().length() == 0) {
                    Tools.displayToast(ChangePasswordActivity.this, "原密码不能为空");
                } else if (editTextNewPassword.getText().toString().trim().length() == 0) {
                    Tools.displayToast(ChangePasswordActivity.this, "新密码不能为空");
                } else {
                    doChangePassword();
                }
            }
        });
    }

    public void doChangePassword() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection connection = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", editTextAccount.getText().toString());
                json.put("password", editTextPassword.getText().toString());
                json.put("newPassword", editTextNewPassword.getText().toString());

                String response = connection.doPost("http://ysdk.kystu.cn/api/userChangePassword/", json);
                LoginEventMessage message = JSONObject.parseObject(response, LoginEventMessage.class);
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
        if (msg.getCode() != 0) {
            Tools.displayToast(this, msg.getInfo());
        } else {
            Tools.displayToast(ChangePasswordActivity.this, msg.getInfo());
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }
}