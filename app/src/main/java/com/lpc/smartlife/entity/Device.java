package com.lpc.smartlife.entity;

/**
 * @author byu_rself
 * @date 2021/12/26 20:37
 */
public class Device {
    private Integer deviceId;
    private String deviceName;
    private Integer deviceImageId;
    private Integer roomId;
    private String userId;
    private Integer isConnected;

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
        return deviceImageId;
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
