package com.hjq.demo.daerxiansheng.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hjq.demo.daerxiansheng.sql.MessageListEntity;

import java.util.List;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-16.
 * 简述: <消息列表item实体类>
 */
public class MessageEntity implements Parcelable {

    //    public String imageUrl;
//    public String time;
//    public String username;
//    public String content;
//    public int count;
    public int state;
    public String features_chat;
    public List<MessageListEntity> all_array;


    public MessageEntity() {
    }

    public static class Entity implements Parcelable {
        @SerializedName("id")
        public String message_id;//消息id
        public String id;//消息id
        public String rval;//消息内容
        public String rtype;//消息类型(1=好友 2=群组 3=虚拟APP)
        public String rclass;//文本消息 2=图片 3=音频
        public String rtime;//发送时间
        public String duration;//语音时长
        public String disturb;//是否勿扰(0否 1是)
        public String nickname;//发送人昵称
        public String user_id;//发送人id
        public String head_img;//发送人头像
        public String user_card;//发送人唯一标识
        public String group_name;//群组名称
        public String group_id;//群组id
        public String group_img;//群主头像
        public String card;//发送人唯一标识/群组唯一标识
        public int  messCount;//消息数量
        public Entity() {
        }

        protected Entity(Parcel in) {
            message_id = in.readString();
            id = in.readString();
            rval = in.readString();
            rtype = in.readString();
            rclass = in.readString();
            rtime = in.readString();
            duration = in.readString();
            disturb = in.readString();
            nickname = in.readString();
            user_id = in.readString();
            head_img = in.readString();
            user_card = in.readString();
            group_name = in.readString();
            group_id = in.readString();
            group_img = in.readString();
            card = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(message_id);
            dest.writeString(id);
            dest.writeString(rval);
            dest.writeString(rtype);
            dest.writeString(rclass);
            dest.writeString(rtime);
            dest.writeString(duration);
            dest.writeString(disturb);
            dest.writeString(nickname);
            dest.writeString(user_id);
            dest.writeString(head_img);
            dest.writeString(user_card);
            dest.writeString(group_name);
            dest.writeString(group_id);
            dest.writeString(group_img);
            dest.writeString(card);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Entity> CREATOR = new Creator<Entity>() {
            @Override
            public Entity createFromParcel(Parcel in) {
                return new Entity(in);
            }

            @Override
            public Entity[] newArray(int size) {
                return new Entity[size];
            }
        };
    }


    protected MessageEntity(Parcel in) {
        state = in.readInt();
        features_chat = in.readString();
        all_array = in.createTypedArrayList(MessageListEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(features_chat);
        dest.writeTypedList(all_array);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageEntity> CREATOR = new Creator<MessageEntity>() {
        @Override
        public MessageEntity createFromParcel(Parcel in) {
            return new MessageEntity(in);
        }

        @Override
        public MessageEntity[] newArray(int size) {
            return new MessageEntity[size];
        }
    };
}
