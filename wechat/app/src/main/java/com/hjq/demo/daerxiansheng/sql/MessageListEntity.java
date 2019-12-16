package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangrenwei on 2019-11-25 23:05.
 */

@Entity
public class MessageListEntity implements Parcelable,Cloneable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String message_id;//消息id
    public String rval;//消息内容
    public String rtype;//消息类型(1=好友 2=群组 3=虚拟APP)
    public String rclass;//1文本消息 2=图片 3=音频
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
    public int messCount;//消息数量
    public String currentuserCard;//当前用户的card


    protected MessageListEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        message_id = in.readString();
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
        messCount = in.readInt();
        currentuserCard = in.readString();
    }

    @Generated(hash = 209097053)
    public MessageListEntity(Long id, String message_id, String rval, String rtype, String rclass,
            String rtime, String duration, String disturb, String nickname, String user_id,
            String head_img, String user_card, String group_name, String group_id, String group_img,
            String card, int messCount, String currentuserCard) {
        this.id = id;
        this.message_id = message_id;
        this.rval = rval;
        this.rtype = rtype;
        this.rclass = rclass;
        this.rtime = rtime;
        this.duration = duration;
        this.disturb = disturb;
        this.nickname = nickname;
        this.user_id = user_id;
        this.head_img = head_img;
        this.user_card = user_card;
        this.group_name = group_name;
        this.group_id = group_id;
        this.group_img = group_img;
        this.card = card;
        this.messCount = messCount;
        this.currentuserCard = currentuserCard;
    }

    @Generated(hash = 637322382)
    public MessageListEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(message_id);
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
        dest.writeInt(messCount);
        dest.writeString(currentuserCard);
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

    public String getMessage_id() {
        return this.message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getRval() {
        return this.rval;
    }

    public void setRval(String rval) {
        this.rval = rval;
    }

    public String getRtype() {
        return this.rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getRclass() {
        return this.rclass;
    }

    public void setRclass(String rclass) {
        this.rclass = rclass;
    }

    public String getRtime() {
        return this.rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDisturb() {
        return this.disturb;
    }

    public void setDisturb(String disturb) {
        this.disturb = disturb;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHead_img() {
        return this.head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getUser_card() {
        return this.user_card;
    }

    public void setUser_card(String user_card) {
        this.user_card = user_card;
    }

    public String getGroup_name() {
        return this.group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_img() {
        return this.group_img;
    }

    public void setGroup_img(String group_img) {
        this.group_img = group_img;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public int getMessCount() {
        return this.messCount;
    }

    public void setMessCount(int messCount) {
        this.messCount = messCount;
    }

    public String getCurrentuserCard() {
        return this.currentuserCard;
    }

    public void setCurrentuserCard(String currentuserCard) {
        this.currentuserCard = currentuserCard;
    }

    public static final Creator<MessageListEntity> CREATOR = new Creator<MessageListEntity>() {
        @Override
        public MessageListEntity createFromParcel(Parcel in) {
            return new MessageListEntity(in);
        }

        @Override
        public MessageListEntity[] newArray(int size) {
            return new MessageListEntity[size];
        }
    };

    @Override
    public MessageListEntity clone() throws CloneNotSupportedException {
        MessageListEntity mMessageListEntity = null;
        try{
            mMessageListEntity=(MessageListEntity)super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return mMessageListEntity;
    }

}
