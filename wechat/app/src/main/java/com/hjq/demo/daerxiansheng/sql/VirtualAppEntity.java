package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangrenwei on 2019-11-20 02:27.
 */
@Entity
public class VirtualAppEntity implements Parcelable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String app_img;//虚拟APP头像
    @SerializedName("app_name")
    public String appName;//虚拟APP名称
    @SerializedName("app_link")
    public String appLink;//虚拟APP链接
    public String app_number;//虚拟APP号
    public String app_founder;//虚拟APP创始人唯一标识
    public String userCard;//当前用户的card


    protected VirtualAppEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        app_img = in.readString();
        appName = in.readString();
        appLink = in.readString();
        app_number = in.readString();
        app_founder = in.readString();
        userCard = in.readString();
    }

    @Generated(hash = 2142668545)
    public VirtualAppEntity(Long id, String app_img, String appName, String appLink,
                            String app_number, String app_founder, String userCard) {
        this.id = id;
        this.app_img = app_img;
        this.appName = appName;
        this.appLink = appLink;
        this.app_number = app_number;
        this.app_founder = app_founder;
        this.userCard = userCard;
    }

    @Generated(hash = 575280157)
    public VirtualAppEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(app_img);
        dest.writeString(appName);
        dest.writeString(appLink);
        dest.writeString(app_number);
        dest.writeString(app_founder);
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

    public String getApp_img() {
        return this.app_img;
    }

    public void setApp_img(String app_img) {
        this.app_img = app_img;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppLink() {
        return this.appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getApp_number() {
        return this.app_number;
    }

    public void setApp_number(String app_number) {
        this.app_number = app_number;
    }

    public String getApp_founder() {
        return this.app_founder;
    }

    public void setApp_founder(String app_founder) {
        this.app_founder = app_founder;
    }

    public String getUserCard() {
        return this.userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public static final Creator<VirtualAppEntity> CREATOR = new Creator<VirtualAppEntity>() {
        @Override
        public VirtualAppEntity createFromParcel(Parcel in) {
            return new VirtualAppEntity(in);
        }

        @Override
        public VirtualAppEntity[] newArray(int size) {
            return new VirtualAppEntity[size];
        }
    };
}
