package com.lpc.smartlife.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.utils.MyHttpConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/28 21:34
 */
public class RoomList {
    public static RoomList roomList = new RoomList();

    private List<Room> rooms;

    private RoomList() {
        rooms = new ArrayList<>();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getRoomsCount() {
        return String.valueOf(rooms.size());
    }

    public void addRoom(Room room){
        if (room == null) {
            return;
        }
        rooms.add(room);
    }

    public void getRoomList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection conn = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", User.user.getUserId());
                String response = conn.myPost("/getRoomList", json);
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray arrays = jsonObject.getJSONArray("data");

                rooms.removeAll(rooms);

                for (int i = 0; i < arrays.size(); i++) {
                    String s = arrays.get(i) + "";
                    JSONObject object = JSON.parseObject(s);
                    Room room = new Room(
                            object.getInteger("roomId"),
                            object.getString("roomName"),
                            object.getInteger("deviceCount"),
                            object.getString("userId"));

                    RoomList.roomList.addRoom(room);
                }
            }
        }).start();
    }
}
