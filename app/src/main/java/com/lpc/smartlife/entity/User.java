package com.lpc.smartlife.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.utils.MyHttpConnection;

import java.lang.reflect.Field;

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
        getUserInfo(userId);
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void getUserInfo(String userId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection coon = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("userId", userId);
                String response = coon.myPost("/getUserNickName", json);
                JSONObject jsonObject = JSON.parseObject(response);
                String nickName = jsonObject.getString("nickName");
                user.setUserName(nickName);
            }
        }).start();
    }
}
