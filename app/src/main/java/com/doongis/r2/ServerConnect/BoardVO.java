package com.doongis.r2.ServerConnect;

import java.io.Serializable;

public class BoardVO implements Serializable {

    private int board_no;
    private String board_name;

    public int getBoard_no() {
        return board_no;
    }
    public void setBoard_no(int board_no) {
        this.board_no = board_no;
    }
    public String getBoard_name() {
        return board_name;
    }
    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    @Override
    public String toString() {
        return "BoardVO [board_no=" + board_no + ", board_name=" + board_name + "]";
    }



}
