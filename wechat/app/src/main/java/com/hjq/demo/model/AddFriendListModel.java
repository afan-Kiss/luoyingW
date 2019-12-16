package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 新的朋友列表
 */

public class AddFriendListModel implements Parcelable {

    /**
     * state : 1
     * msg : 成功
     * all_array : []
     * New_array : [{"user_id":"5","msg":"hhhhhdm","fsate":"2","username":"17364583794","head_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","nickname":"范某人丶","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI="}]
     * features_friends : 02b1be0d48924c327124732726097157
     */

    private int state;
    private String msg;
    private String features_friends;
    private List<?> all_array;
    private List<NewArrayBean> New_array;

    protected AddFriendListModel(Parcel in) {
        state = in.readInt();
        msg = in.readString();
        features_friends = in.readString();
    }

    public static final Creator<AddFriendListModel> CREATOR = new Creator<AddFriendListModel>() {
        @Override
        public AddFriendListModel createFromParcel(Parcel in) {
            return new AddFriendListModel(in);
        }

        @Override
        public AddFriendListModel[] newArray(int size) {
            return new AddFriendListModel[size];
        }
    };

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFeatures_friends() {
        return features_friends;
    }

    public void setFeatures_friends(String features_friends) {
        this.features_friends = features_friends;
    }

    public List<?> getAll_array() {
        return all_array;
    }

    public void setAll_array(List<?> all_array) {
        this.all_array = all_array;
    }

    public List<NewArrayBean> getNew_array() {
        return New_array;
    }

    public void setNew_array(List<NewArrayBean> New_array) {
        this.New_array = New_array;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(msg);
        dest.writeString(features_friends);
    }

    public static class NewArrayBean implements Parcelable{
        /**
         * user_id : 5
         * msg : hhhhhdm
         * fsate : 2
         * username : 17364583794
         * head_img : http://api.kfexe.com/upload/20191115/20191115233044636.png
         * nickname : 范某人丶
         * card : ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI=
         */

        private String user_id;
        private String msg;
        private String fsate;
        private String username;
        private String head_img;
        private String nickname;
        private String card;

        protected NewArrayBean(Parcel in) {
            user_id = in.readString();
            msg = in.readString();
            fsate = in.readString();
            username = in.readString();
            head_img = in.readString();
            nickname = in.readString();
            card = in.readString();
        }

        public static final Creator<NewArrayBean> CREATOR = new Creator<NewArrayBean>() {
            @Override
            public NewArrayBean createFromParcel(Parcel in) {
                return new NewArrayBean(in);
            }

            @Override
            public NewArrayBean[] newArray(int size) {
                return new NewArrayBean[size];
            }
        };

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getFsate() {
            return fsate;
        }

        public void setFsate(String fsate) {
            this.fsate = fsate;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(user_id);
            dest.writeString(msg);
            dest.writeString(fsate);
            dest.writeString(username);
            dest.writeString(head_img);
            dest.writeString(nickname);
            dest.writeString(card);
        }
    }
}
