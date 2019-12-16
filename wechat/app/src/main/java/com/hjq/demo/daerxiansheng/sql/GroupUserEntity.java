package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangrenwei on 2019-11-20 02:24.
 */
@Entity
public class GroupUserEntity implements Parcelable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String user_id;//用户ID
    public int founder;//用户权限(1群主 2管理 3普通成员)
    public int fsate;//是否好友(1是 0否)
    public String head_img;//用户头像
    public String username;//用户名
    public String nickname;//用户昵称
    public String card;//用户唯一标识
    public String groupCard;//群组唯一标识


    protected GroupUserEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        user_id = in.readString();
        founder = in.readInt();
        fsate = in.readInt();
        head_img = in.readString();
        username = in.readString();
        nickname = in.readString();
        card = in.readString();
        groupCard = in.readString();
    }

    @Generated(hash = 1250695428)
    public GroupUserEntity(Long id, String user_id, int founder, int fsate, String head_img,
            String username, String nickname, String card, String groupCard) {
        this.id = id;
        this.user_id = user_id;
        this.founder = founder;
        this.fsate = fsate;
        this.head_img = head_img;
        this.username = username;
        this.nickname = nickname;
        this.card = card;
        this.groupCard = groupCard;
    }

    @Generated(hash = 440491583)
    public GroupUserEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(user_id);
        dest.writeInt(founder);
        dest.writeInt(fsate);
        dest.writeString(head_img);
        dest.writeString(username);
        dest.writeString(nickname);
        dest.writeString(card);
        dest.writeString(groupCard);
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

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getFounder() {
        return this.founder;
    }

    public void setFounder(int founder) {
        this.founder = founder;
    }

    public int getFsate() {
        return this.fsate;
    }

    public void setFsate(int fsate) {
        this.fsate = fsate;
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

    public String getGroupCard() {
        return this.groupCard;
    }

    public void setGroupCard(String groupCard) {
        this.groupCard = groupCard;
    }

    public static final Creator<GroupUserEntity> CREATOR = new Creator<GroupUserEntity>() {
        @Override
        public GroupUserEntity createFromParcel(Parcel in) {
            return new GroupUserEntity(in);
        }

        @Override
        public GroupUserEntity[] newArray(int size) {
            return new GroupUserEntity[size];
        }
    };
}
