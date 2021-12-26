package com.lpc.smartlife.entity;

import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.utils.MyHttpConnection;

/**
 * @author byu_rself
 * @date 2021/12/26 21:04
 */
public class User {
    public static User user = new User();

    private String userId;
    private String userName;
    private String password;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        changeUserMessage();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        changeUserMessage();
    }

    private void changeUserMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection coon = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", userId);
                json.put("userName", userName);
                json.put("password", password);
                String response = coon.doPost("/userChange/", json);
            }
        }).start();
    }
}
