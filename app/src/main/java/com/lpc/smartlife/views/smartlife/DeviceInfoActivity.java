package com.lpc.smartlife.views.smartlife;

import android.os.Bundle;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;

public class DeviceInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_info;
    }
}