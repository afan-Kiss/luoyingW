package com.hjq.demo.daerxiansheng.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhangrenwei on 2019-11-17 13:45.
 */
public class UserListEntity implements Parcelable {
//    public List<Entity> all_array;
    public int state;
    public String msg;
    public String head_img;
    public int fsate;
    public String nickname;
    public String username;
    public int gender;
    public String signature;
    public String region_country;
    public String region_province;
    public String region_city;
    public String card;

    public UserListEntity() {

    }

    protected UserListEntity(Parcel in) {
        state = in.readInt();
        msg = in.readString();
        head_img = in.readString();
        fsate = in.readInt();
        nickname = in.readString();
        username = in.readString();
        gender = in.readInt();
        signature = in.readString();
        region_country = in.readString();
        region_province = in.readString();
        region_city = in.readString();
        card = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(msg);
        dest.writeString(head_img);
        dest.writeInt(fsate);
        dest.writeString(nickname);
        dest.writeString(username);
        dest.writeInt(gender);
        dest.writeString(signature);
        dest.writeString(region_country);
        dest.writeString(region_province);
        dest.writeString(region_city);
        dest.writeString(card);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserListEntity> CREATOR = new Creator<UserListEntity>() {
        @Override
        public UserListEntity createFromParcel(Parcel in) {
            return new UserListEntity(in);
        }

        @Override
        public UserListEntity[] newArray(int size) {
            return new UserListEntity[size];
        }
    };
}
