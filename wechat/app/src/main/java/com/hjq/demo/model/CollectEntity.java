package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Created by zhangrenwei QQ1282447073
 * @date 2019-11-16.
 * 简述: <收藏item实体类>
 */
public class CollectEntity implements Parcelable {

    public int state;
    public String features_collection;
    public List<CollectListEntity> all_array;


    public CollectEntity() {
    }

    protected CollectEntity(Parcel in) {
        state = in.readInt();
        features_collection = in.readString();
        all_array = in.createTypedArrayList(CollectListEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(features_collection);
        dest.writeTypedList(all_array);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectEntity> CREATOR = new Creator<CollectEntity>() {
        @Override
        public CollectEntity createFromParcel(Parcel in) {
            return new CollectEntity(in);
        }

        @Override
        public CollectEntity[] newArray(int size) {
            return new CollectEntity[size];
        }
    };
}
