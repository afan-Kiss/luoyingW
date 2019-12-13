package com.hjq.demo.model;

/**
 * 发送好友请求
 */
public class SendAddFriendModel {

    /**
     * state : 1
     * msg : 成功
     */

    private int state;
    private String msg;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
