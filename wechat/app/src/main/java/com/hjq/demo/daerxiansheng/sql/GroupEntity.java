package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangrenwei on 2019-11-20 02:21.
 */
@Entity
public class GroupEntity implements Parcelable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String groupImg;//群组头像
    public String groupName;//群组名称
    public int disturb;//是否勿扰(0否 1是)
    public String groupNotic;//群组公告
    public String card;//群组唯一标识
    public String userCard;//当前用户card

    protected GroupEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        groupImg = in.readString();
        groupName = in.readString();
        disturb = in.readInt();
        groupNotic = in.readString();
        card = in.readString();
        userCard = in.readString();
    }

    @Generated(hash = 36531034)
    public GroupEntity(Long id, String groupImg, String groupName, int disturb,
            String groupNotic, String card, String userCard) {
        this.id = id;
        this.groupImg = groupImg;
        this.groupName = groupName;
        this.disturb = disturb;
        this.groupNotic = groupNotic;
        this.card = card;
        this.userCard = userCard;
    }

    @Generated(hash = 954040478)
    public GroupEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(groupImg);
        dest.writeString(groupName);
        dest.writeInt(disturb);
        dest.writeString(groupNotic);
        dest.writeString(card);
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

    public String getGroupImg() {
        return this.groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getDisturb() {
        return this.disturb;
    }

    public void setDisturb(int disturb) {
        this.disturb = disturb;
    }

    public String getGroupNotic() {
        return this.groupNotic;
    }

    public void setGroupNotic(String groupNotic) {
        this.groupNotic = groupNotic;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getUserCard() {
        return this.userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public static final Creator<GroupEntity> CREATOR = new Creator<GroupEntity>() {
        @Override
        public GroupEntity createFromParcel(Parcel in) {
            return new GroupEntity(in);
        }

        @Override
        public GroupEntity[] newArray(int size) {
            return new GroupEntity[size];
        }
    };
}
