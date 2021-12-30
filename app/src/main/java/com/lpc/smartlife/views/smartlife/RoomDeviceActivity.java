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
import com.lpc.smartlife.message.CommunityMessageEvent;
import com.lpc.smartlife.utils.CreateDialog;
import com.lpc.smartlife.utils.Tools;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class RoomDeviceActivity extends BaseActivity {

    Room room;
    int index;
    List<Device> deviceList;

    ImageButton ibBacktoDeviceManager;
    TextView tv_RoomName;
    ImageView ivAddRoomDevice;
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
        ivAddRoomDevice = findViewById(R.id.ivAddRoomDevice);
        ivNoDevice = findViewById(R.id.ivNoDevice);
        tvNoDevice = findViewById(R.id.tvNoDevice);
        tvEditRoomName = findViewById(R.id.tvEditRoomName);
        editRoomName = findViewById(R.id.editRoomName);

        tv_RoomName.setText(room.getRoomName());
        tvEditRoomName.setText(room.getRoomName());

        initRoomDeviceRecyclerView();
        estimateEquipment();

        ivAddRoomDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConnectDialog();
            }
        });

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

    public void showConnectDialog() {
        createDialog = new CreateDialog(this, R.layout.connect_dialog, R.style.dialog, onClickListenerCancel, onClickListenerConnect, "");
        createDialog.show();
    }


    private View.OnClickListener onClickListenerConnect = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int n = 0, p = 0;
            Device device = new Device(null, "ESP32", 2, room.getRoomId(), room.getUserId(), 0);
            // 判断是否只连接一个设备
            for (int i = 0; i < createDialog.devices.size(); i++) {
                if (createDialog.devices.get(i).isCheck()) {
                    n++;
                    p = i;
                }
                if (n > 1) {
                    Tools.displayToast(view.getContext(), "一次只能连接一个设备");
                    return;
                }
            }
            // 如果没有选择的设备直接关闭对话框
            if (n == 0) {
                createDialog.cancel();
                return;
            }
            // 判断该设备是否已经存在在房间中
            if (isConnect(createDialog.devices.get(p), deviceList)) {
                device.setMacAddress(createDialog.devices.get(p).getMacAddress());
                deviceList.add(device);
                DeviceList.deviceList.add(device);
                Tools.displayToast(view.getContext(), "添加成功");
                estimateEquipment();
                deviceAdapter.notifyDataSetChanged();
                createDialog.cancel();
            } else {
                Tools.displayToast(view.getContext(), "设备已存在");
            }
        }

    };

    public boolean isConnect(Device device, List<Device> devices) {
        for (int i = 0; i < devices.size(); i++) {
            if (device.getMacAddress().equals(devices.get(i).getMacAddress()))
                return false;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUDPReceive(CommunityMessageEvent msg) {

        switch (msg.getCode()) {
            case CommunityMessageEvent.ClearText:
                break;
            case CommunityMessageEvent.UDPDataMessage:
                Device device = new Device(null,"ESP32", 2, room.getRoomId(), room.getUserId(),0);
                device.setMacAddress(msg.getMessage().split("\n")[0].replaceAll("\r",""));
                if(isConnect(device,createDialog.devices)){
                    createDialog.devices.add(device);
                    createDialog.connectDeviceAdapter.notifyDataSetChanged();
                }
                break;
            case CommunityMessageEvent.TCPDataMessage:
                break;
        }

    }

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