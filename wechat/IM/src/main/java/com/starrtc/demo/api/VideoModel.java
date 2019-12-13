package com.starrtc.demo.api;

/**
 * @author GF
 * @des 视频地址
 * @date 2019/11/21
 */
public class VideoModel {
    /**
     * state : 1
     * msg : 成功
     * av_id : 26
     * server_ip : 47.75.36.80
     * nickname : 周爷我怕怕
     * head_img : http://api.kfexe.com/upload/20191104/20191104160718709.png
     * phone : 18866667777
     */

    private int state;
    private String msg;
    private int av_id;
    private String server_ip;
    private String nickname;
    private String head_img;
    private String phone;

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

    public int getAv_id() {
        return av_id;
    }

    public void setAv_id(int av_id) {
        this.av_id = av_id;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
