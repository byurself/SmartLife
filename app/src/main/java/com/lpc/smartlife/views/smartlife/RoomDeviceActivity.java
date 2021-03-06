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
import com.lpc.smartlife.utils.CommunityInterface;
import com.lpc.smartlife.utils.CreateDialog;
import com.lpc.smartlife.utils.Tools;
import com.lpc.smartlife.utils.UDP;

import org.greenrobot.eventbus.EventBus;
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

    CommunityInterface community;

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

        community = UDP.getInstance("192.168.137.1", 9000);

        EventBus.getDefault().register(this);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                Tools.displayToast(view.getContext(), "????????????????????????");
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
            List<Device> httpGetDeviceList = DeviceList.deviceList.httpGetDeviceList();
            int deviceId = httpGetDeviceList.get(httpGetDeviceList.size() - 1).getDeviceId() + 1;
            Device device = new Device(deviceId, "ESP32", 2, room.getRoomId(), room.getUserId(), 0);
            // ?????????????????????????????????
            for (int i = 0; i < createDialog.devices.size(); i++) {
                if (createDialog.devices.get(i).isCheck()) {
                    n++;
                    p = i;
                }
                if (n > 1) {
                    Tools.displayToast(view.getContext(), "??????????????????????????????");
                    return;
                }
            }
            // ????????????????????????????????????????????????
            if (n == 0) {
                createDialog.cancel();
                return;
            }
            // ?????????????????????????????????????????????
            if (isConnect(createDialog.devices.get(p), deviceList)) {
                device.setMacAddress(createDialog.devices.get(p).getMacAddress());
                deviceList.add(device);
                DeviceList.deviceList.add(device);
                Tools.displayToast(view.getContext(), "????????????");
                estimateEquipment();
                deviceAdapter.notifyDataSetChanged();
                createDialog.cancel();
            } else {
                Tools.displayToast(view.getContext(), "???????????????");
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
                List<Device> httpGetDeviceList = DeviceList.deviceList.httpGetDeviceList();
                int deviceId = httpGetDeviceList.get(httpGetDeviceList.size() - 1).getDeviceId() + 1;
                Device device = new Device(deviceId, "ESP32", 2, room.getRoomId(), room.getUserId(), 0);
                device.setMacAddress(msg.getMessage().split("\n")[0].replaceAll("\r", ""));
                if (isConnect(device, createDialog.devices)) {
                    createDialog.devices.add(device);
                    createDialog.connectDeviceAdapter.notifyDataSetChanged();
                }
                break;
            case CommunityMessageEvent.TCPDataMessage:
                break;
        }

    }

    // ?????????????????????,????????????????????????????????????????????????????????????????????????
    public void estimateEquipment() {
        if (deviceList == null || deviceList.isEmpty()) {
            ivNoDevice.setImageDrawable(getDrawable(R.mipmap.no_device));
            ivNoDevice.setAlpha(100);
            tvNoDevice.setText("????????????");
        } else {
            ivNoDevice.setImageDrawable(getDrawable(R.color.white));
            ivNoDevice.setAlpha(0);
            tvNoDevice.setText("");
        }
    }

    public void initRoomDeviceRecyclerView() {
        rvRoomDevice = findViewById(R.id.rvRoomDevice);
        // ????????????
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvRoomDevice.setLayoutManager(manager);
        // ???????????????
        deviceList = DeviceList.deviceList.getListByRoomId(room.getRoomId());
        deviceAdapter = new DeviceAdapter(this, deviceList, R.layout.room_device, null, this);
        rvRoomDevice.setAdapter(deviceAdapter);
        // ???????????????
        rvRoomDevice.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // ????????????????????????????????????
        rvRoomDevice.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_device;
    }
}