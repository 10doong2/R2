package com.doongis.r2.ServerConnect;

import java.io.Serializable;

public class CycleVO implements Serializable {
    private int cycle_no;
    private String cycle_start_time;
    private String cycle_end_time;
    private String cycle_total_time;
    private String cycle_distance;
    private String cycle_speed;

    private String user_id;
    private String user_name;

    public int getCycle_no() {
        return cycle_no;
    }

    public void setCycle_no(int cycle_no) {
        this.cycle_no = cycle_no;
    }

    public String getCycle_start_time() {
        return cycle_start_time;
    }

    public void setCycle_start_time(String cycle_start_time) {
        this.cycle_start_time = cycle_start_time;
    }

    public String getCycle_end_time() {
        return cycle_end_time;
    }

    public void setCycle_end_time(String cycle_end_time) {
        this.cycle_end_time = cycle_end_time;
    }

    public String getCycle_total_time() {
        return cycle_total_time;
    }

    public void setCycle_total_time(String cycle_total_time) {
        this.cycle_total_time = cycle_total_time;
    }

    public String getCycle_distance() {
        return cycle_distance;
    }

    public void setCycle_distance(String cycle_distance) {
        this.cycle_distance = cycle_distance;
    }

    public String getCycle_speed() {
        return cycle_speed;
    }

    public void setCycle_speed(String cycle_speed) {
        this.cycle_speed = cycle_speed;
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
        return "CycleVO{" +
                "cycle_no=" + cycle_no +
                ", cycle_start_time='" + cycle_start_time + '\'' +
                ", cycle_end_time='" + cycle_end_time + '\'' +
                ", cycle_total_time='" + cycle_total_time + '\'' +
                ", cycle_distance='" + cycle_distance + '\'' +
                ", cycle_speed='" + cycle_speed + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
