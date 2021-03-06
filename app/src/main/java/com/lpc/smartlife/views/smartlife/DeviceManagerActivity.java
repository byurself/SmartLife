package com.lpc.smartlife.views.smartlife;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.RoomAdapter;
import com.lpc.smartlife.entity.Room;
import com.lpc.smartlife.entity.RoomList;
import com.lpc.smartlife.entity.User;
import com.lpc.smartlife.message.LoginEventMessage;
import com.lpc.smartlife.utils.CreateDialog;
import com.lpc.smartlife.utils.MyHttpConnection;
import com.lpc.smartlife.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DeviceManagerActivity extends BaseActivity {

    ImageButton ibBackToRoomInfo;
    ImageView ivAddRoom;

    RecyclerView rvDeviceManager;
    RoomAdapter roomAdapter;

    View room;

    CreateDialog createDialog;

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
        roomAdapter.notifyDataSetChanged();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void init() {
        ibBackToRoomInfo = findViewById(R.id.ibBackToRoomInfo);
        ivAddRoom = findViewById(R.id.ivAddRoom);

        ivAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        ibBackToRoomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LayoutInflater inflater = getLayoutInflater().from(this);
        room = inflater.inflate(R.layout.room_layout, null);

        initRoomRecyclerView();
    }

    public void showDialog(View view) {
        createDialog = new CreateDialog(this, R.layout.add_room_name_dialog, R.style.dialog, onClickListenerCancel, onClickListenerConfirm, "");
        createDialog.show();
    }

    private View.OnClickListener onClickListenerCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createDialog.cancel();
        }
    };

    private View.OnClickListener onClickListenerConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (createDialog.et_home_name.getText().toString().trim().length() == 0)
                Tools.displayToast(view.getContext(), "????????????????????????");
            else {
                addRoom();
                createDialog.cancel();
            }
        }
    };

    public void addRoom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection conn = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", User.user.getUserId());
                json.put("roomName", createDialog.et_home_name.getText().toString().trim());
                String response = conn.myPost("/addRoom", json);

                RoomList.roomList.getRoomList();
                EventBus.getDefault().post("success");
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handle(String msg) {
        if ("success".equals(msg)) {
            roomAdapter.notifyDataSetChanged();
        }
    }

    public void initRoomRecyclerView() {
        rvDeviceManager = findViewById(R.id.rvDeviceManager);
        // ????????????
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvDeviceManager.setLayoutManager(manager);
        // ???????????????
        RoomList.roomList.getRoomList();
        roomAdapter = new RoomAdapter(this, RoomList.roomList.getRooms(), 1);
        rvDeviceManager.setAdapter(roomAdapter);
        // ???????????????
        rvDeviceManager.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // ????????????????????????????????????
        rvDeviceManager.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_manager;
    }
}