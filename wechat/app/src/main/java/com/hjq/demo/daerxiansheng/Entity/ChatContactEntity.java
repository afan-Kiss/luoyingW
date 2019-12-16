package com.hjq.demo.daerxiansheng.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-22.
 * 简述: <搜索 聊天消息/联系人/群组实体类>
 */
public class ChatContactEntity implements Parcelable {
    public String id;//用户或群组id
    public String name;//用户或群组名称
    public String imageUrl;//头像
    public int messageCount;//聊天消息数量
    public String card;//唯一标识
    public boolean isContact;//是否是群组 true 是群组 false不是群组
    public String  phone;//用户手机号
    public String content;//聊天消息
    public String time;//时间戳
    public ChatContactEntity() {

    }

    protected ChatContactEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        messageCount = in.readInt();
        card = in.readString();
        isContact = in.readByte() != 0;
        phone = in.readString();
        content = in.readString();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeInt(messageCount);
        dest.writeString(card);
        dest.writeByte((byte) (isContact ? 1 : 0));
        dest.writeString(phone);
        dest.writeString(content);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatContactEntity> CREATOR = new Creator<ChatContactEntity>() {
        @Override
        public ChatContactEntity createFromParcel(Parcel in) {
            return new ChatContactEntity(in);
        }

        @Override
        public ChatContactEntity[] newArray(int size) {
            return new ChatContactEntity[size];
        }
    };
}
