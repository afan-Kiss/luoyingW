package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangrenwei on 2019-11-20 02:07.
 */
@Entity
public class FrendsEntity implements Parcelable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String userid;//用户ID
    public int fsate;//是否好友(1好友 2新的朋友)
    public String mag;//附加信息
    public String head_img;//用户头像
    public String username;//用户名
    public String nickname;//用户昵称
    public String card;//用户唯一标识
    public int disturb;//是否勿扰(0否 1是)
    public int black;//是否黑名单(0否 1是)
    public String userCard;//当前用户card

    protected FrendsEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userid = in.readString();
        fsate = in.readInt();
        mag = in.readString();
        head_img = in.readString();
        username = in.readString();
        nickname = in.readString();
        card = in.readString();
        disturb = in.readInt();
        black = in.readInt();
        userCard = in.readString();
    }

    @Generated(hash = 1637472211)
    public FrendsEntity(Long id, String userid, int fsate, String mag, String head_img,
                        String username, String nickname, String card, int disturb, int black,
                        String userCard) {
        this.id = id;
        this.userid = userid;
        this.fsate = fsate;
        this.mag = mag;
        this.head_img = head_img;
        this.username = username;
        this.nickname = nickname;
        this.card = card;
        this.disturb = disturb;
        this.black = black;
        this.userCard = userCard;
    }

    @Generated(hash = 1021005237)
    public FrendsEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(userid);
        dest.writeInt(fsate);
        dest.writeString(mag);
        dest.writeString(head_img);
        dest.writeString(username);
        dest.writeString(nickname);
        dest.writeString(card);
        dest.writeInt(disturb);
        dest.writeInt(black);
        dest.writeString(userCard);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getFsate() {
        return this.fsate;
    }

    public void setFsate(int fsate) {
        this.fsate = fsate;
    }

    public String getMag() {
        return this.mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getHead_img() {
        return this.head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public int getDisturb() {
        return this.disturb;
    }

    public void setDisturb(int disturb) {
        this.disturb = disturb;
    }

    public int getBlack() {
        return this.black;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    public String getUserCard() {
        return this.userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public static final Creator<FrendsEntity> CREATOR = new Creator<FrendsEntity>() {
        @Override
        public FrendsEntity createFromParcel(Parcel in) {
            return new FrendsEntity(in);
        }

        @Override
        public FrendsEntity[] newArray(int size) {
            return new FrendsEntity[size];
        }
    };
}
