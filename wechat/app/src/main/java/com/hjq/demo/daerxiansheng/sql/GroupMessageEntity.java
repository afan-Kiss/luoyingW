package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangrenwei on 2019-11-20 02:55.
 */
@Entity
public class GroupMessageEntity implements Parcelable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String content;//消息内容
    public String toUserImage;//头像
    public int contentType;//1文本消息 2图片 3音频
    public int messageType;//消息类型 1好友 2群组 3虚拟app
    public long Duration;//语音消息时长(秒)
    public String toUid;//发送对象唯一标识
    public String toUserName;//发送对象用户昵称
    public long Time;//时间戳
    public String UserName;//用户手机号
    public int messageSendingType;//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
    public String groupCard;//群组唯一标识
    public String userCard;//用户唯一标识

    protected GroupMessageEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        content = in.readString();
        toUserImage = in.readString();
        contentType = in.readInt();
        messageType = in.readInt();
        Duration = in.readLong();
        toUid = in.readString();
        toUserName = in.readString();
        Time = in.readLong();
        UserName = in.readString();
        messageSendingType = in.readInt();
        groupCard = in.readString();
        userCard = in.readString();
    }

    @Generated(hash = 1495453844)
    public GroupMessageEntity(Long id, String content, String toUserImage, int contentType,
            int messageType, long Duration, String toUid, String toUserName, long Time, String UserName,
            int messageSendingType, String groupCard, String userCard) {
        this.id = id;
        this.content = content;
        this.toUserImage = toUserImage;
        this.contentType = contentType;
        this.messageType = messageType;
        this.Duration = Duration;
        this.toUid = toUid;
        this.toUserName = toUserName;
        this.Time = Time;
        this.UserName = UserName;
        this.messageSendingType = messageSendingType;
        this.groupCard = groupCard;
        this.userCard = userCard;
    }

    @Generated(hash = 1992127110)
    public GroupMessageEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(content);
        dest.writeString(toUserImage);
        dest.writeInt(contentType);
        dest.writeInt(messageType);
        dest.writeLong(Duration);
        dest.writeString(toUid);
        dest.writeString(toUserName);
        dest.writeLong(Time);
        dest.writeString(UserName);
        dest.writeInt(messageSendingType);
        dest.writeString(groupCard);
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToUserImage() {
        return this.toUserImage;
    }

    public void setToUserImage(String toUserImage) {
        this.toUserImage = toUserImage;
    }

    public int getContentType() {
        return this.contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getDuration() {
        return this.Duration;
    }

    public void setDuration(long Duration) {
        this.Duration = Duration;
    }

    public String getToUid() {
        return this.toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getToUserName() {
        return this.toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public long getTime() {
        return this.Time;
    }

    public void setTime(long Time) {
        this.Time = Time;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public int getMessageSendingType() {
        return this.messageSendingType;
    }

    public void setMessageSendingType(int messageSendingType) {
        this.messageSendingType = messageSendingType;
    }

    public String getGroupCard() {
        return this.groupCard;
    }

    public void setGroupCard(String groupCard) {
        this.groupCard = groupCard;
    }

    public String getUserCard() {
        return this.userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public static final Creator<GroupMessageEntity> CREATOR = new Creator<GroupMessageEntity>() {
        @Override
        public GroupMessageEntity createFromParcel(Parcel in) {
            return new GroupMessageEntity(in);
        }

        @Override
        public GroupMessageEntity[] newArray(int size) {
            return new GroupMessageEntity[size];
        }
    };
}
