package com.starrtc.demo.api;

public class ResponseBody {


    /**
     * state : 2
     * msg : 登录失败
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