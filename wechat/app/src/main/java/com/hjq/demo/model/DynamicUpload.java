package com.hjq.demo.model;

/**
 * @author GF
 * @des ${TODO}
 * @date 2019/11/17
 */
public class DynamicUpload {
    /**
     * state : 1
     * img : http://api.kfexe.com/upload/20191117/20191117142708385.jpg
     * msg : 上传成功
     */

    private int state;
    private String img;
    private String msg;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
