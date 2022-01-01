package com.lpc.smartlife.views.smartlife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.views.fragment.ESP32Fragment;
import com.lpc.smartlife.views.fragment.FreeBuds3Fragment;

public class DeviceInfoActivity extends BaseActivity {

    public static String macAddress;
    FragmentManager fragmentManager;
    Fragment ESP32Fragment;
    Fragment freeBuds3Fragment;

    TextView tv_DeviceName;
    TextView tvMacAddress;
    ImageView ibBackToRoomDevice;
    ImageView ivDeviceImage;

    String deviceName;
    int deviceImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        setImmersiveWindows();
        Intent intent = getIntent();
        Bundle info = intent.getExtras();
        deviceName = info.getString("deviceName");
        deviceImageId = info.getInt("deviceImageId");
        macAddress = info.getString("macAddress");
        init();
    }

    private void init() {
        tv_DeviceName = findViewById(R.id.tv_DeviceName);
        ivDeviceImage = findViewById(R.id.ivDeviceImage);
        ibBackToRoomDevice = findViewById(R.id.ibBackToRoomDevice);
        tvMacAddress = findViewById(R.id.tvMacAddress);

        tv_DeviceName.setText(deviceName);
        ivDeviceImage.setImageDrawable(getDrawable(deviceImageId));
        tvMacAddress.setText(macAddress);

        ESP32Fragment = new ESP32Fragment();
        freeBuds3Fragment = new FreeBuds3Fragment();
        fragmentManager = getSupportFragmentManager();
        if (deviceName.equals("ESP32")) {
            fragmentManager.beginTransaction().add(R.id.flDeviceInfo, ESP32Fragment, "esp32").commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.flDeviceInfo, freeBuds3Fragment, "freeBuds3").commit();
        }

        ibBackToRoomDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_info;
    }
}