package com.hjq.demo.model;

import java.io.Serializable;

/**
 * @author GF
 * @des ${TODO}
 * @date 2019/11/21
 */
public class AcceptVideoModel implements Serializable{

    /**
     * code : 13|4|3|5|6|7
     * rid :
     * ip : 47.75.36.80
     * aid : 2
     * nickname : 18791436821
     * head_img : http://api.kfexe.com/upload/20191127/c4bce30a2fad048f405a77567af83a75156.png
     * phone : 18791436821
     * card : MTViNjQ0ZWYwYTY1YTNjODUyNWUyZWVlY2ZiMDJlNjg=
     * type : 2
     * qlucard :
     * qlcard :
     * slcard :
     */

    private String code;
    private String rid;
    private String ip;
    private String aid;
    private String nickname;
    private String head_img;
    private String phone;
    private String card;
    private String type;
    private String qlucard;
    private String qlcard;
    private String slcard;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
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

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQlucard() {
        return qlucard;
    }

    public void setQlucard(String qlucard) {
        this.qlucard = qlucard;
    }

    public String getQlcard() {
        return qlcard;
    }

    public void setQlcard(String qlcard) {
        this.qlcard = qlcard;
    }

    public String getSlcard() {
        return slcard;
    }

    public void setSlcard(String slcard) {
        this.slcard = slcard;
    }
}
