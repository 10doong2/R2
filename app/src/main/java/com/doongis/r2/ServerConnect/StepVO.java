package com.doongis.r2.ServerConnect;

import java.io.Serializable;


public class StepVO implements Serializable {

    private int step_no;
    private String step_start_time;
    private String step_end_time;
    private String step_total_time;
    private String step_goal;
    private String step_step;

    private String user_id;
    private String user_name;

    public int getStep_no() {
        return step_no;
    }

    public void setStep_no(int step_no) {
        this.step_no = step_no;
    }

    public String getStep_start_time() {
        return step_start_time;
    }

    public void setStep_start_time(String step_start_time) {
        this.step_start_time = step_start_time;
    }

    public String getStep_end_time() {
        return step_end_time;
    }

    public void setStep_end_time(String step_end_time) {
        this.step_end_time = step_end_time;
    }

    public String getStep_total_time() {
        return step_total_time;
    }

    public void setStep_total_time(String step_total_time) {
        this.step_total_time = step_total_time;
    }

    public String getStep_goal() {
        return step_goal;
    }

    public void setStep_goal(String step_goal) {
        this.step_goal = step_goal;
    }

    public String getStep_step() {
        return step_step;
    }

    public void setStep_step(String step_step) {
        this.step_step = step_step;
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
        return "StepVO{" +
                "step_no=" + step_no +
                ", step_start_time='" + step_start_time + '\'' +
                ", step_end_time='" + step_end_time + '\'' +
                ", step_total_time='" + step_total_time + '\'' +
                ", step_goal='" + step_goal + '\'' +
                ", step_step='" + step_step + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
