package com.lpc.smartlife.entity;

/**
 * @author byu_rself
 * @date 2021/12/26 20:57
 */
public class Room {
    private Integer roomId;
    private String roomName;
    private Integer deviceCount;
    private String userId;

    public Room(Integer roomId, String roomName, Integer deviceCount, String userId) {
        this.roomId = roomId;
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

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }
}
