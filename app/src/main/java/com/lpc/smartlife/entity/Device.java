package com.lpc.smartlife.entity;

import com.lpc.smartlife.R;

/**
 * @author byu_rself
 * @date 2021/12/26 20:37
 */
public class Device {

    public static final int FreeBuds3 = 1;
    public static final int ESP32 = 2;

    private Integer deviceId;
    private String deviceName;
    private Integer deviceImageId;
    private Integer roomId;
    private String userId;
    private Integer isConnected;
    private String macAddress;
    private Boolean isCheck;

    public Device() {
    }

    public Device(Integer deviceId, String deviceName, Integer deviceImageId, Integer roomId, String userId, Integer isConnected) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceImageId = deviceImageId;
        this.roomId = roomId;
        this.userId = userId;
        this.isConnected = isConnected;
    }

    public Boolean isCheck() {
        return isCheck;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceImageId() {
        switch (deviceImageId){
            case FreeBuds3:
                return R.mipmap.freebuds3;
            case ESP32:
                return R.mipmap.esp32;
        }
        return 0;
    }

    public void setDeviceImageId(Integer deviceImageId) {
        this.deviceImageId = deviceImageId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Integer isConnected) {
        this.isConnected = isConnected;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId=" + deviceId +
                ", deviceName='" + deviceName + '\'' +
                ", deviceImageId=" + deviceImageId +
                ", roomId=" + roomId +
                ", userId='" + userId + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }
}
