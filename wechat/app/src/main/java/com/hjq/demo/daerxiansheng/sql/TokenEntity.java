package com.hjq.demo.daerxiansheng.sql;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;


/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-23.
 * 简述: <密钥列表>
 */
@Entity
public class TokenEntity implements Parcelable {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    public Long id;//一条数据的id
    public String name;//令牌名称
    public String key;//令牌key
    public String code;//令牌验证码
    public String card;//当前用户card
    public long leftTime;//当前剩余倒计时
    public boolean isSelect;//是否选中
    public TokenEntity() {

    }

    protected TokenEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        key = in.readString();
        code = in.readString();
        card = in.readString();
        leftTime = in.readLong();
        isSelect = in.readByte() != 0;
    }

    @Generated(hash = 779530039)
    public TokenEntity(Long id, String name, String key, String code, String card, long leftTime,
            boolean isSelect) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.code = code;
        this.card = card;
        this.leftTime = leftTime;
        this.isSelect = isSelect;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(code);
        dest.writeString(card);
        dest.writeLong(leftTime);
        dest.writeByte((byte) (isSelect ? 1 : 0));
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public static final Creator<TokenEntity> CREATOR = new Creator<TokenEntity>() {
        @Override
        public TokenEntity createFromParcel(Parcel in) {
            return new TokenEntity(in);
        }

        @Override
        public TokenEntity[] newArray(int size) {
            return new TokenEntity[size];
        }
    };
}
