package com.lpc.smartlife.entity;

/**
 * @author byu_rself
 * @date 2021/12/26 20:57
 */
public class Room {
    private Integer roomId;
    private String roomName;
    private String deviceCount;
    private String userId;

    public Room(String roomName, String deviceCount, String userId) {
        this.roomName = roomName;
        this.deviceCount = deviceCount;
        this.userId = userId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(String deviceCount) {
        this.deviceCount = deviceCount;
    }

    public void add(Device device){
        DeviceList.deviceList.getDeviceList().add(device);
        DeviceList.deviceList.httpAddDevice(device);
    }
}
