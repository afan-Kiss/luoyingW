package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QrcodeModel implements Parcelable {


    /**
     * state : 1
     * msg : 成功
     * head_img : http://api.kfexe.com/upload/20191115/20191115233044636.png
     * nickname : 范某人丶
     * remarkname :
     * member_grade : 0
     * username : 17364583794
     * fsate : 1
     * region_country : 中国大陆
     * region_province : 福建
     * region_city : 厦门
     * signature :
     * gender : 2
     * card : ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI=
     */

    private int state;
    private String msg;
    private String head_img;
    private String nickname;
    private String remarkname;
    private String member_grade;
    private String username;
    private String fsate;
    private String region_country;
    private String region_province;
    private String region_city;
    private String signature;
    private String gender;
    private String card;

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

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemarkname() {
        return remarkname;
    }

    public void setRemarkname(String remarkname) {
        this.remarkname = remarkname;
    }

    public String getMember_grade() {
        return member_grade;
    }

    public void setMember_grade(String member_grade) {
        this.member_grade = member_grade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFsate() {
        return fsate;
    }

    public void setFsate(String fsate) {
        this.fsate = fsate;
    }

    public String getRegion_country() {
        return region_country;
    }

    public void setRegion_country(String region_country) {
        this.region_country = region_country;
    }

    public String getRegion_province() {
        return region_province;
    }

    public void setRegion_province(String region_province) {
        this.region_province = region_province;
    }

    public String getRegion_city() {
        return region_city;
    }

    public void setRegion_city(String region_city) {
        this.region_city = region_city;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state);
        dest.writeString(this.msg);
        dest.writeString(this.head_img);
        dest.writeString(this.nickname);
        dest.writeString(this.remarkname);
        dest.writeString(this.member_grade);
        dest.writeString(this.username);
        dest.writeString(this.fsate);
        dest.writeString(this.region_country);
        dest.writeString(this.region_province);
        dest.writeString(this.region_city);
        dest.writeString(this.signature);
        dest.writeString(this.gender);
        dest.writeString(this.card);
    }

    public QrcodeModel() {
    }

    protected QrcodeModel(Parcel in) {
        this.state = in.readInt();
        this.msg = in.readString();
        this.head_img = in.readString();
        this.nickname = in.readString();
        this.remarkname = in.readString();
        this.member_grade = in.readString();
        this.username = in.readString();
        this.fsate = in.readString();
        this.region_country = in.readString();
        this.region_province = in.readString();
        this.region_city = in.readString();
        this.signature = in.readString();
        this.gender = in.readString();
        this.card = in.readString();
    }

    public static final Parcelable.Creator<QrcodeModel> CREATOR = new Parcelable.Creator<QrcodeModel>() {
        @Override
        public QrcodeModel createFromParcel(Parcel source) {
            return new QrcodeModel(source);
        }

        @Override
        public QrcodeModel[] newArray(int size) {
            return new QrcodeModel[size];
        }
    };
}
