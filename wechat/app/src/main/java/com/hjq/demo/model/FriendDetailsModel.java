package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 好友详细信息
 */
public class FriendDetailsModel implements Parcelable {


    /**
     * state : 1
     * msg : 成功
     * head_img : http://api.kfexe.com/upload/20191112/20191112182423348.png
     * nickname : 测试账号
     * remarkname :
     * member_grade : 0
     * username : 17306000000
     * region_country : 中国大陆
     * region_province : 北京
     * region_city : 昌平
     * signature : 阿里来咯哦拉拉裤
     * gender : 2
     * card : Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk=
     */

    private int state;
    private String msg;
    private String head_img;
    private String nickname;
    private String remarkname;
    private String member_grade;
    private String username;
    private String region_country;
    private String region_province;
    private String region_city;
    private String signature;
    private String gender;
    private String card;

    protected FriendDetailsModel(Parcel in) {
        state = in.readInt();
        msg = in.readString();
        head_img = in.readString();
        nickname = in.readString();
        remarkname = in.readString();
        member_grade = in.readString();
        username = in.readString();
        region_country = in.readString();
        region_province = in.readString();
        region_city = in.readString();
        signature = in.readString();
        gender = in.readString();
        card = in.readString();
    }

    public static final Creator<FriendDetailsModel> CREATOR = new Creator<FriendDetailsModel>() {
        @Override
        public FriendDetailsModel createFromParcel(Parcel in) {
            return new FriendDetailsModel(in);
        }

        @Override
        public FriendDetailsModel[] newArray(int size) {
            return new FriendDetailsModel[size];
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
        dest.writeString(remarkname);
        dest.writeString(member_grade);
        dest.writeString(username);
        dest.writeString(region_country);
        dest.writeString(region_province);
        dest.writeString(region_city);
        dest.writeString(signature);
        dest.writeString(gender);
        dest.writeString(card);
    }
}
