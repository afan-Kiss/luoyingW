package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author GF
 * @des ${TODO}
 * @date 2019/11/21
 */
public class CollectListEntity implements Parcelable,Cloneable{
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    private String ctime;//收藏时间
    private String nickname;//用户昵称
    private String cval;//收藏内容
    private String cid;//收藏id
    private String ctype;//收藏类型 0=文本 1=图片

    protected CollectListEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        ctime = in.readString();
        nickname = in.readString();
        cval = in.readString();
        if (in.readByte() == 0) {
            cid = null;
        } else {
            cid = in.readString();
        }
        ctype = in.readString();
    }

    public static final Creator<CollectListEntity> CREATOR = new Creator<CollectListEntity>() {
        @Override
        public CollectListEntity createFromParcel(Parcel in) {
            return new CollectListEntity(in);
        }

        @Override
        public CollectListEntity[] newArray(int size) {
            return new CollectListEntity[size];
        }
    };

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCval() {
        return cval;
    }

    public void setCval(String cval) {
        this.cval = cval;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
