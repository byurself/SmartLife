package com.lpc.smartlife.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.utils.MyHttpConnection;

import java.util.ArrayList;
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

    //获取设备信息
    public void getDeviceInfo(Room room) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection conn = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("roomId", room.getRoomId());
                json.put("userId", User.user.getUserId());
                String response = conn.doPost("/getDeviceList/", json);
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray arrays = jsonObject.getJSONArray("data");
                addDeviceInfo(arrays);
            }
        }).start();
    }

    //将设备信息添加到数组里
    private void addDeviceInfo(JSONArray arrays) {
        if (arrays == null)
            return;
        Device device;
        for (int i = 0; i < arrays.size(); i++) {
            JSONObject item = arrays.getJSONObject(i);
            device = new Device(item.getString("deviceName"), item.getInteger("deviceImageId"), item.getInteger("roomId"), item.getString("userId"));
            device.setDeviceId(item.getInteger("deviceId"));
            devices.add(device);
        }
    }

    public void httpAddDevice(Device device) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection coon = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("roomId", device.getRoomId());
                json.put("userId", device.getUserId());
                json.put("deviceImageId", device.getDeviceImageId());
                json.put("deviceName", device.getDeviceName());
                String response = coon.doPost("/addDeviceList/", json);
                JSONObject msg = JSONObject.parseObject(response);
                devices.get(devices.size() - 1).setDeviceId(msg.getInteger("deviceId"));
            }
        }).start();
    }

    public List<Device> getDeviceList() {
        return devices;
    }

    public void setDeviceList(List<Device> devices) {
        this.devices = devices;
    }

    public String getCount() {
        return String.valueOf(devices.size());
    }
}
