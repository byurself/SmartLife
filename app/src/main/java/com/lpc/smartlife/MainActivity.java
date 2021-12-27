package com.lpc.smartlife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.message.LoginEventMessage;
import com.lpc.smartlife.views.smartlife.IndexActivity;
import com.lpc.smartlife.views.userloginabout.ChangePasswordActivity;
import com.lpc.smartlife.views.userloginabout.ForgetActivity;
import com.lpc.smartlife.views.userloginabout.RegisterActivity;
import com.lpc.smartlife.utils.MyHttpConnection;
import com.lpc.smartlife.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {

    private Boolean isShowingPassword = false;
    EditText editAccount;
    EditText editPassword;
    ImageButton btnPassword;
    Button btnLogin;
    CheckBox cbRemember;
    TextView textForget;
    TextView textChangePassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setImmersiveWindows();
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
        editAccount = findViewById(R.id.editTextAccount);
        editPassword = findViewById(R.id.editTextPassword);
        btnPassword = findViewById(R.id.btnShowHidePassword);
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRemember);
        textForget = findViewById(R.id.textForget);
        textChangePassword = findViewById(R.id.textChangePassword);
        btnRegister = findViewById(R.id.btnRegister);

        editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowingPassword) {
                    // 显示密码
                    btnPassword.setImageDrawable(getResources().getDrawable(R.mipmap.show_password));
                    editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    btnPassword.setImageDrawable(getResources().getDrawable(R.mipmap.hide_password));
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                isShowingPassword = !isShowingPassword;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editAccount.getText().toString().trim().length() == 0) {
                    Tools.displayToast(MainActivity.this, "账号不能为空");
                } else if (editPassword.getText().toString().trim().length() == 0) {
                    Tools.displayToast(MainActivity.this, "密码不能为空");
                } else {
                    doLogin();
                }
            }
        });

        textForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });

        textChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loadInfo();
    }

    private void doLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection connection = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", editAccount.getText().toString());
                json.put("password", editPassword.getText().toString());
                String response = connection.myPost("/userLogin", json);
                LoginEventMessage message = JSONObject.parseObject(response, LoginEventMessage.class);
                EventBus.getDefault().post(message);
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLogin(LoginEventMessage msg) {
        if (msg.getCode() == 0) {
            if (cbRemember.isChecked()) {//需要记住
                doRemember();
            } else {//需要忘记
                doForget();
            }
            Tools.displayToast(MainActivity.this, msg.getInfo());
            Intent intent = new Intent(MainActivity.this, IndexActivity.class);
            intent.putExtra("userId", editAccount.getText().toString());
            startActivityForResult(intent, 0);
        } else {
            Tools.displayToast(this, msg.getInfo());
        }
    }

    /**
     * 记住密码
     */
    private void doRemember() {
        SharedPreferences sharedPreferences = getSharedPreferences("qqShared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", editAccount.getText().toString());
        editor.putString("password", editPassword.getText().toString());
        editor.putBoolean("isRemember", true);
        editor.commit();
    }

    /**
     * 忘记密码
     */
    private void doForget() {
        SharedPreferences sharedPreferences = getSharedPreferences("qqShared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", "");
        editor.putString("password", "");
        editor.putBoolean("isRemember", false);
        editor.commit();
    }

    private void loadInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("qqShared", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isRemember", false)) {
            editAccount.setText(sharedPreferences.getString("account", ""));
            editPassword.setText(sharedPreferences.getString("password", ""));
            cbRemember.setChecked(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}