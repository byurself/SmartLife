package com.lpc.smartlife.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.DeviceAdapter;
import com.lpc.smartlife.utils.MyHttpConnection;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/26 20:52
 */
public class DeviceList {
    public static DeviceList deviceList = new DeviceList();
    private List<Device> devices;

    private DeviceList() {
        devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        if (device == null) {
            return;
        }
        devices.add(device);
    }

    public void remove(int deviceId) {
        int roomId = 0;
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getDeviceId() == deviceId) {
                roomId = devices.get(i).getRoomId();
                devices.remove(i);
                break;
            }
        }
        RoomList.roomList.removeRoomCount(roomId, 1);
    }

    public List<Device> httpGetDeviceList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection conn = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", User.user.getUserId());
                String response = conn.myPost("/getDeviceList", json);
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray arrays = jsonObject.getJSONArray("data");

                devices.clear();

                if (arrays != null && !arrays.isEmpty()) {
                    for (int i = 0; i < arrays.size(); i++) {
                        String s = arrays.get(i) + "";
                        JSONObject object = JSON.parseObject(s);
                        Device device = new Device(
                                object.getInteger("deviceId"),
                                object.getString("deviceName"),
                                object.getInteger("deviceImageId"),
                                object.getInteger("roomId"),
                                object.getString("userId"),
                                object.getInteger("isConnected"));
                        device.setMacAddress(object.getString("macAddress"));

                        DeviceList.deviceList.addDevice(device);
                    }
                }
            }
        }).start();
        return devices;
    }

    public void add(Device device) {
        int index = RoomList.roomList.getIndex(device.getRoomId());
        RoomList.roomList.getRooms().get(index).setDeviceCount(getListByRoomId(device.getRoomId()).size() + 1);
        devices.add(device);
        httpAddDevice(device);
    }

    private void httpAddDevice(Device device) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int deviceImageId = 1;
                if (device.getDeviceImageId() == R.mipmap.esp32) {
                    deviceImageId = 2;
                }
                MyHttpConnection coon = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("deviceId", device.getDeviceId());
                json.put("deviceName", device.getDeviceName());
                json.put("deviceImageId", deviceImageId);
                json.put("roomId", device.getRoomId());
                json.put("userId", device.getUserId());
                json.put("macAddress", device.getMacAddress());
                String response = coon.myPost("/addDevice", json);
            }
        }).start();
    }

    public List<Device> getListByRoomId(int roomId) {
        List<Device> items = new ArrayList<>();
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getRoomId() == roomId)
                items.add(devices.get(i));
        }
        return items;
    }

    public void setDeviceList(List<Device> devices) {
        this.devices = devices;
    }

    public String getCount() {
        return String.valueOf(devices.size());
    }

    public List<Device> getDeviceListByUserIdAndRoomId(int roomId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection conn = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", User.user.getUserId());
                json.put("roomId", roomId);
                String response = conn.myPost("/getDeviceListByUserIdAndRoomId", json);
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray arrays = jsonObject.getJSONArray("data");

                devices.clear();

                if (arrays != null) {
                    for (int i = 0; i < arrays.size(); i++) {
                        String s = arrays.get(i) + "";
                        JSONObject object = JSON.parseObject(s);
                        Device device = new Device(
                                object.getInteger("deviceId"),
                                object.getString("deviceName"),
                                object.getInteger("deviceImageId"),
                                object.getInteger("roomId"),
                                object.getString("userId"),
                                object.getInteger("isConnected"));

                        DeviceList.deviceList.addDevice(device);
                    }
                }
            }
        }).start();
        return devices;
    }
}
