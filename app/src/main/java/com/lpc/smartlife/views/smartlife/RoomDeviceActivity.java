package com.lpc.smartlife.views.smartlife;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.DeviceAdapter;
import com.lpc.smartlife.entity.Device;
import com.lpc.smartlife.entity.DeviceList;
import com.lpc.smartlife.entity.Room;
import com.lpc.smartlife.entity.RoomList;
import com.lpc.smartlife.utils.CreateDialog;
import com.lpc.smartlife.utils.Tools;

import java.util.List;

public class RoomDeviceActivity extends BaseActivity {

    Room room;
    int index;
    List<Device> deviceList;

    ImageButton ibBacktoDeviceManager;
    TextView tv_RoomName;
    ImageView ivAddRoom;
    ImageView ivNoDevice;
    TextView tvNoDevice;
    TextView tvEditRoomName;
    LinearLayout editRoomName;

    RecyclerView rvRoomDevice;
    DeviceAdapter deviceAdapter;

    CreateDialog createDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_device);

        setImmersiveWindows();

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
        tv_RoomName = findViewById(R.id.tv_RoomName);
        ivAddRoom = findViewById(R.id.ivAddRoom);
        ivNoDevice = findViewById(R.id.ivNoDevice);
        tvNoDevice = findViewById(R.id.tvNoDevice);
        tvEditRoomName = findViewById(R.id.tvEditRoomName);
        editRoomName = findViewById(R.id.editRoomName);

        tv_RoomName.setText(room.getRoomName());
        tvEditRoomName.setText(room.getRoomName());

        initRoomDeviceRecyclerView();
        estimateEquipment();

//        ivAddRoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        editRoomName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        ibBacktoDeviceManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showDialog(View view) {
        createDialog = new CreateDialog(this, R.layout.edit_room_dialog, R.style.dialog, onClickListenerCancel, onClickListenerConfirm, tvEditRoomName.getText().toString().trim());
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
                Tools.displayToast(view.getContext(), "房间名称不能为空");
            else {
                room.setRoomName(createDialog.et_home_name.getText().toString().trim());
                RoomList.roomList.getRooms().get(index).setRoomName(room.getRoomName());
                RoomList.roomList.httpUpdateRoomByRoomId(room);
                tv_RoomName.setText(room.getRoomName());
                tvEditRoomName.setText(room.getRoomName());
                createDialog.cancel();
            }
        }
    };

    // 判断是否有设备,没有设备显示无设备的图片和文字，否则显示设备数量
    public void estimateEquipment() {
        if (deviceList == null || deviceList.isEmpty()) {
            ivNoDevice.setImageDrawable(getDrawable(R.mipmap.no_device));
            ivNoDevice.setAlpha(100);
            tvNoDevice.setText("没有设备");
        } else {
            ivNoDevice.setImageDrawable(getDrawable(R.color.white));
            ivNoDevice.setAlpha(0);
            tvNoDevice.setText("");
        }
    }

    public void initRoomDeviceRecyclerView() {
        rvRoomDevice = findViewById(R.id.rvRoomDevice);
        // 布局设置
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvRoomDevice.setLayoutManager(manager);
        // 设置适配器
        deviceList = DeviceList.deviceList.getListByRoomId(room.getRoomId());
        deviceAdapter = new DeviceAdapter(this, deviceList, R.layout.room_device, null, this);
        rvRoomDevice.setAdapter(deviceAdapter);
        // 设置分割线
        rvRoomDevice.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // 设置增加或删除条目的动画
        rvRoomDevice.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_device;
    }
}