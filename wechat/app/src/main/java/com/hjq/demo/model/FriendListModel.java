package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 通讯录好友列表
 */
public class FriendListModel implements Parcelable {


    /**
     * state : 1
     * msg : 成功
     * all_array : [{"black":"0","disturb":"0","user_id":"1","msg":"哈哈","fsate":"1","username":"17306001831","head_img":"http://api.kfexe.com/upload/20191106/20191106152047539.png","nickname":"今天z","card":"ZGRiYTZlN2NhZmRmNTE4OGFhYjE3OTJhNzI0MWI0ZGY="},{"black":"0","disturb":"0","user_id":"6","msg":"","fsate":"1","username":"17306000000","head_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","nickname":"测试账号","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk="},{"black":"0","disturb":"0","user_id":"4","msg":"","fsate":"1","username":"18866667777","head_img":"http://api.kfexe.com/upload/20191104/20191104160718709.png","nickname":"周爷我怕怕","card":"MzA0MmJkOGM5NmRmY2VlZWEzYmE5MWUxNTA5YTdjOTQ="},{"black":"0","disturb":"0","user_id":"10","msg":"","fsate":"1","username":"13474666245","head_img":"http://api.kfexe.com/aupload/1573116470.jpg","nickname":"碎娃","card":"NWVmMGRiZjkzMzk4ZTI1NDk4YjY4MmMzOWJhNTk4YjY="},{"black":"0","disturb":"0","user_id":"12","msg":"","fsate":"1","username":"13395052724","head_img":"http://api.kfexe.com/aupload/1573285501.jpg","nickname":"流量监控","card":"MGNiOGMwMjNjMzk3NWE1ZjQyZmM0YmVjZjljNTdjZTI="},{"black":"0","disturb":"0","user_id":"2","msg":"","fsate":"1","username":"17306001832","head_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA="},{"black":"0","disturb":"0","user_id":"9","msg":"1","fsate":"1","username":"17788889999","head_img":"http://api.kfexe.com/upload/20191111/20191111113058286.png","nickname":"周董","card":"NjM2YjgxNjhkNGIzMDUwZGUyMTEyOWE5ZWU3MTEyNWE="},{"black":"0","disturb":"0","user_id":"13","msg":"","fsate":"1","username":"13559936636","head_img":"http://api.kfexe.com/headimg/default.jpg","nickname":"13559936636","card":"MDUwMTUxOGFmOTdiOTBmODk3OGJjNDJjMmQ2NzlhYzQ="},{"black":"0","disturb":"0","user_id":"8","msg":"你好","fsate":"1","username":"13399205123","head_img":"http://api.kfexe.com/aupload/1573027895.jpg","nickname":"范爷我怕怕","card":"YzFlMDY4YWI1ZWQwZmIzZDA5ZTJiODIzOTg5NjYyN2Y="},{"black":"0","disturb":"0","user_id":"15","msg":"","fsate":"1","username":"13395052726","head_img":"http://api.kfexe.com/upload/20191115/20191115182310667.png","nickname":"13395052726","card":"M2UyMGJhZWI2ZmE4ZTVmOTgzNGQ4NWVlMjc4NzYyYmQ="},{"black":"0","disturb":"0","user_id":"14","msg":"","fsate":"1","username":"13395052728","head_img":"http://api.kfexe.com/aupload/1573793867.jpg","nickname":"发个广告","card":"ZTBiYjU5YzkxMmExOTAzZjJiMTNkM2M0OWNkNzM2ZDQ="}]
     * New_array : []
     * features_friends : 7a48932b26f04fccb55f0409ba3451fc
     */
    public int state;
    public String msg;
    public String features_friends;
    public List<AllArrayBean> all_array;
    public List<?> New_array;

    protected FriendListModel(Parcel in) {
        state = in.readInt();
        msg = in.readString();
        features_friends = in.readString();
    }

    public static final Creator<FriendListModel> CREATOR = new Creator<FriendListModel>() {
        @Override
        public FriendListModel createFromParcel(Parcel in) {
            return new FriendListModel(in);
        }

        @Override
        public FriendListModel[] newArray(int size) {
            return new FriendListModel[size];
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

    public List<AllArrayBean> getAll_array() {
        return all_array;
    }

    ;

    public void setAll_array(List<AllArrayBean> all_array) {
        this.all_array = all_array;
    }

    public List<?> getNew_array() {
        return New_array;
    }

    public void setNew_array(List<?> New_array) {
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

    public static class AllArrayBean implements Parcelable, MultiItemEntity {
        /**
         * black : 0
         * disturb : 0
         * user_id : 1
         * msg : 哈哈
         * fsate : 1
         * username : 17306001831
         * head_img : http://api.kfexe.com/upload/20191106/20191106152047539.png
         * nickname : 今天z
         * card : ZGRiYTZlN2NhZmRmNTE4OGFhYjE3OTJhNzI0MWI0ZGY=
         */

        public String black;
        public String disturb;
        public String user_id;
        public String msg;
        public String fsate;
        public String username;
        public String head_img;
        public String nickname;
        public String card;
        public String sortLetters;
        //是否是字母
        private Boolean isLetter = false;

        private int itemtype = 2;

        public void setItemtype(int itemtype) {
            this.itemtype = itemtype;
        }

        public Boolean getLetter() {
            return isLetter;
        }

        public void setLetter(Boolean letter) {
            isLetter = letter;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public AllArrayBean() {

        }

        public AllArrayBean(Parcel in) {
            black = in.readString();
            disturb = in.readString();
            user_id = in.readString();
            msg = in.readString();
            fsate = in.readString();
            username = in.readString();
            head_img = in.readString();
            nickname = in.readString();
            card = in.readString();
        }

        public static final Creator<AllArrayBean> CREATOR = new Creator<AllArrayBean>() {
            @Override
            public AllArrayBean createFromParcel(Parcel in) {
                return new AllArrayBean(in);
            }

            @Override
            public AllArrayBean[] newArray(int size) {
                return new AllArrayBean[size];
            }
        };

        public String getBlack() {
            return black;
        }

        public void setBlack(String black) {
            this.black = black;
        }

        public String getDisturb() {
            return disturb;
        }

        public void setDisturb(String disturb) {
            this.disturb = disturb;
        }

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
            dest.writeString(black);
            dest.writeString(disturb);
            dest.writeString(user_id);
            dest.writeString(msg);
            dest.writeString(fsate);
            dest.writeString(username);
            dest.writeString(head_img);
            dest.writeString(nickname);
            dest.writeString(card);
        }

        @Override
        public int getItemType() {
            return itemtype;
        }
    }
}
