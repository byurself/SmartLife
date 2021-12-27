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

    public void addDevice(Device device) {
        if (device == null) {
            return;
        }
        devices.add(device);
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
