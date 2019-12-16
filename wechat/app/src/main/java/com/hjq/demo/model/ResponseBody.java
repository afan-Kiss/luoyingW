package com.hjq.demo.model;

public class ResponseBody {


    /**
     * state : 2
     * msg : 登录失败
     */

    private int state;
    private String msg;
    private String rid;

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

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
