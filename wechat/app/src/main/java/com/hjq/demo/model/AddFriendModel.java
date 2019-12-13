package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddFriendModel implements Parcelable {

    /**
     * state : 1
     * msg : 搜索成功
     * head_img : http://api.kfexe.com/headimg/default.jpg
     * nickname :
     * username : 18321520793
     * fsate : 3
     * region_country :
     * region_province :
     * region_city :
     * signature :
     * gender : 0
     * card : Y2QxMzY1ZGI0ZmIyOGM2YTJlOGZlNGQzZGE1MDIxNTQ=
     */

    private int state;
    private String msg;
    private String head_img;
    private String nickname;
    private String username;
    private String fsate;
    private String region_country;
    private String region_province;
    private String region_city;
    private String signature;
    private String gender;
    private String card;

    protected AddFriendModel(Parcel in) {
        state = in.readInt();
        msg = in.readString();
        head_img = in.readString();
        nickname = in.readString();
        username = in.readString();
        fsate = in.readString();
        region_country = in.readString();
        region_province = in.readString();
        region_city = in.readString();
        signature = in.readString();
        gender = in.readString();
        card = in.readString();
    }

    public static final Creator<AddFriendModel> CREATOR = new Creator<AddFriendModel>() {
        @Override
        public AddFriendModel createFromParcel(Parcel in) {
            return new AddFriendModel(in);
        }

        @Override
        public AddFriendModel[] newArray(int size) {
            return new AddFriendModel[size];
        }
    };

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
        dest.writeInt(state);
        dest.writeString(msg);
        dest.writeString(head_img);
        dest.writeString(nickname);
        dest.writeString(username);
        dest.writeString(fsate);
        dest.writeString(region_country);
        dest.writeString(region_province);
        dest.writeString(region_city);
        dest.writeString(signature);
        dest.writeString(gender);
        dest.writeString(card);
    }
}
