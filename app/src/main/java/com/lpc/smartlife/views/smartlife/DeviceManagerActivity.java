package com.lpc.smartlife.views.smartlife;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.RoomAdapter;
import com.lpc.smartlife.entity.RoomList;

public class DeviceManagerActivity extends BaseActivity {

    ImageButton ibBackToRoomInfo;

    RecyclerView rvDeviceManager;
    RoomAdapter roomAdapter;

    View room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);

        setImmersiveWindows();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRoomRecyclerView();
    }

    public void init(){
        ibBackToRoomInfo = findViewById(R.id.ibBackToRoomInfo);

        ibBackToRoomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LayoutInflater inflater = getLayoutInflater().from(this);
        room = inflater.inflate(R.layout.room_layout, null);
    }

    public void initRoomRecyclerView() {
        rvDeviceManager = findViewById(R.id.rvDeviceManager);
        // 布局设置
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvDeviceManager.setLayoutManager(manager);
        // 设置适配器
        RoomList.roomList.getRoomList();
        roomAdapter = new RoomAdapter(this, RoomList.roomList.getRooms());
        rvDeviceManager.setAdapter(roomAdapter);
        // 设置分割线
        rvDeviceManager.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // 设置增加或删除条目的动画
        rvDeviceManager.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_manager;
    }
}