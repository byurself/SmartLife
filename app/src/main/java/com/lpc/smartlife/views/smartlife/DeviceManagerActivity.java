package com.lpc.smartlife.views.smartlife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;

public class DeviceManagerActivity extends BaseActivity {

    ImageButton ibBackToRoomInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);

        setImmersiveWindows();
        init();
    }

    public void init(){
        ibBackToRoomInfo = findViewById(R.id.ibBackToRoomInfo);

        ibBackToRoomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_manager;
    }
}