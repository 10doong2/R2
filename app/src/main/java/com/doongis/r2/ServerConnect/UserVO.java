package com.doongis.r2.ServerConnect;

import java.io.Serializable;

public class UserVO implements Serializable{
    public static String user_id;
    public static String user_name;

    public UserVO(){}

    public UserVO(String user_id, String user_name){
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "UserVO [user_id=" + user_id + ", user_name=" + user_name + "]";
    }
}
