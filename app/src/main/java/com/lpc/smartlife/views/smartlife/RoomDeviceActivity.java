package com.lpc.smartlife.views.smartlife;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.DeviceAdapter;
import com.lpc.smartlife.entity.Device;
import com.lpc.smartlife.entity.DeviceList;
import com.lpc.smartlife.entity.Room;
import com.lpc.smartlife.utils.CreateDialog;

import java.util.List;

public class RoomDeviceActivity extends BaseActivity {

    Room room;
    int index;
    List<Device> deviceList;

    ImageButton ibBacktoDeviceManager;
    TextView tvRoomDeviceCount;

    RecyclerView rvRoomDevice;
    DeviceAdapter deviceAdapter;

    CreateDialog createDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_device);

        Bundle bundle = getIntent().getExtras();
        room = new Room(
                bundle.getInt("roomId"),
                bundle.getString("roomName"),
                bundle.getInt("deviceCount"),
                bundle.getString("userId")
        );
        index = bundle.getInt("index");

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void init() {
        ibBacktoDeviceManager = findViewById(R.id.ibBacktoDeviceManager);
        tvRoomDeviceCount = findViewById(R.id.tvRoomDeviceCount);

//        tvRoomDeviceCount.setText(deviceList.size() + "个设备");
        initRoomDeviceRecyclerView();

        ibBacktoDeviceManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initRoomDeviceRecyclerView() {
        rvRoomDevice = findViewById(R.id.rvRoomDevice);
        // 布局设置
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvRoomDevice.setLayoutManager(manager);
        // 设置适配器
        deviceList = DeviceList.deviceList.getDeviceListByUserIdAndRoomId(room.getRoomId());
        deviceAdapter = new DeviceAdapter(this, deviceList, R.layout.room_device, tvRoomDeviceCount, this);
        rvRoomDevice.setAdapter(deviceAdapter);
        // 设置分割线
//        rvRoomDevice.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // 设置增加或删除条目的动画
        rvRoomDevice.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_device;
    }
}