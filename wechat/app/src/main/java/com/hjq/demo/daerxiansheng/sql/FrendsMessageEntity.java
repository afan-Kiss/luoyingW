package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangrenwei on 2019-11-18 23:57.
 */
@Entity
public class FrendsMessageEntity implements Parcelable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String content;//消息内容
    public String toUserImage;//头像
    public int contentType;//1文本消息 2图片 3音频
    public int messageType;//消息类型 1好友 2群组 3虚拟app
    public long Duration;//语音消息时长(秒)
    public String toUid;//标识好友card
    public String toUserName;//发送对象用户昵称
    public long Time;//时间戳
    public String UserName;//用户手机号
    public int messageSendingType;//消息发送状态 1发送成功 2.发送失败 3.撤回 4.删除
    public String card;//当前用户唯一标识
    public String  toType;//发送接收消息类型 发送 1  接收 2
    public String message_id;

    protected FrendsMessageEntity(Parcel in) {
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
        card = in.readString();
        toType = in.readString();
        message_id = in.readString();
    }

    @Keep
    public FrendsMessageEntity(Long id, String content, String toUserImage, int contentType,
            int messageType, long Duration, String toUid, String toUserName, long Time, String UserName,
            int messageSendingType, String card, String toType,String message_id) {
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
        this.card = card;
        this.toType = toType;
        this.message_id = message_id;
    }

    @Generated(hash = 468498583)
    public FrendsMessageEntity() {
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
        dest.writeString(card);
        dest.writeString(toType);
        dest.writeString(message_id);
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

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getToType() {
        return this.toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public static final Creator<FrendsMessageEntity> CREATOR = new Creator<FrendsMessageEntity>() {
        @Override
        public FrendsMessageEntity createFromParcel(Parcel in) {
            return new FrendsMessageEntity(in);
        }

        @Override
        public FrendsMessageEntity[] newArray(int size) {
            return new FrendsMessageEntity[size];
        }
    };
}
