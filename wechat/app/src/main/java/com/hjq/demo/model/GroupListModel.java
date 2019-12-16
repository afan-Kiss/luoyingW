package com.hjq.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupListModel implements Parcelable {


    /**
     * state : 1
     * msg : 成功
     * all_array : [{"disturb":"0","group_img":"http://api.kfexe.com/upload/20191108/20191108182411787.png","group_name":"","group_notic":"","card":"ZTQ4MmU2NGIxYTVkYzFjNmZlOTllMmZhZTA0M2EwMmY=","user_list":[{"fsate":"1","user_id":"6","founder":"1","head_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","username":"17306000000","nickname":"周董","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk="},{"fsate":"1","user_id":"5","founder":"3","head_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","username":"17364583794","nickname":"范某人丶","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI="}]},{"disturb":"0","group_img":"http://api.kfexe.com/upload/20191115/20191115150837414.png","group_name":"干哈呢","group_notic":"嗯","card":"OWFhMTIzNTNjMGFjYWQ4YTVmOTJhMjFiMDgwZmM5NmI=","user_list":[{"fsate":"1","user_id":"9","founder":"1","head_img":"http://api.kfexe.com/upload/20191111/20191111113058286.png","username":"17788889999","nickname":"周董","card":"NjM2YjgxNjhkNGIzMDUwZGUyMTEyOWE5ZWU3MTEyNWE="},{"fsate":"1","user_id":"8","founder":"3","head_img":"http://api.kfexe.com/aupload/1573027895.jpg","username":"13399205123","nickname":"范爷我怕怕","card":"YzFlMDY4YWI1ZWQwZmIzZDA5ZTJiODIzOTg5NjYyN2Y="},{"fsate":"1","user_id":"5","founder":"2","head_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","username":"17364583794","nickname":"范某人丶","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI="},{"fsate":"1","user_id":"6","founder":"3","head_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","username":"17306000000","nickname":"测试账号","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk="},{"fsate":"1","user_id":"2","founder":"2","head_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","username":"17306001832","nickname":"樱花","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA="},{"fsate":"1","user_id":"4","founder":"3","head_img":"http://api.kfexe.com/upload/20191104/20191104160718709.png","username":"18866667777","nickname":"周爷我怕怕","card":"MzA0MmJkOGM5NmRmY2VlZWEzYmE5MWUxNTA5YTdjOTQ="},{"fsate":"1","user_id":"1","founder":"2","head_img":"http://api.kfexe.com/upload/20191106/20191106152047539.png","username":"17306001831","nickname":"今天z","card":"ZGRiYTZlN2NhZmRmNTE4OGFhYjE3OTJhNzI0MWI0ZGY="},{"fsate":"1","user_id":"12","founder":"3","head_img":"http://api.kfexe.com/aupload/1573285501.jpg","username":"13395052724","nickname":"流量监控","card":"MGNiOGMwMjNjMzk3NWE1ZjQyZmM0YmVjZjljNTdjZTI="},{"fsate":"1","user_id":"15","founder":"3","head_img":"http://api.kfexe.com/upload/20191115/20191115182310667.png","username":"13395052726","nickname":"13395052726","card":"M2UyMGJhZWI2ZmE4ZTVmOTgzNGQ4NWVlMjc4NzYyYmQ="}]},{"disturb":"0","group_img":"http://api.kfexe.com/upload/20191116/20191116193744585.png","group_name":"","group_notic":"","card":"YzVmZjAwMWRiMWQ1YjAwNTg2Y2VlZGVmMWE1OGNhOTE=","user_list":[{"fsate":"1","user_id":"5","founder":"1","head_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","username":"17364583794","nickname":"范某人丶","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI="},{"fsate":"1","user_id":"8","founder":"3","head_img":"http://api.kfexe.com/aupload/1573027895.jpg","username":"13399205123","nickname":"范爷我怕怕","card":"YzFlMDY4YWI1ZWQwZmIzZDA5ZTJiODIzOTg5NjYyN2Y="},{"fsate":"1","user_id":"10","founder":"3","head_img":"http://api.kfexe.com/aupload/1573116470.jpg","username":"13474666245","nickname":"碎娃","card":"NWVmMGRiZjkzMzk4ZTI1NDk4YjY4MmMzOWJhNTk4YjY="}]}]
     * features_group : c5391cea870510dc95bdcfa72e786a28
     */

    public int state;
    public String msg;
    public String features_group;
    @SerializedName("all_array")
    public List<AllArrayBean> all_array;


    protected GroupListModel(Parcel in) {
        state = in.readInt();
        msg = in.readString();
        features_group = in.readString();
    }

    public static final Creator<GroupListModel> CREATOR = new Creator<GroupListModel>() {
        @Override
        public GroupListModel createFromParcel(Parcel in) {
            return new GroupListModel(in);
        }

        @Override
        public GroupListModel[] newArray(int size) {
            return new GroupListModel[size];
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

    public String getFeatures_group() {
        return features_group;
    }

    public void setFeatures_group(String features_group) {
        this.features_group = features_group;
    }

    public List<AllArrayBean> getAll_array() {
        return all_array;
    }

    public void setAll_array(List<AllArrayBean> all_array) {
        this.all_array = all_array;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(msg);
        dest.writeString(features_group);
    }

    public static class AllArrayBean implements Parcelable {
        /**
         * disturb : 0
         * group_img : http://api.kfexe.com/upload/20191108/20191108182411787.png
         * group_name :
         * group_notic :
         * card : ZTQ4MmU2NGIxYTVkYzFjNmZlOTllMmZhZTA0M2EwMmY=
         * user_list : [{"fsate":"1","user_id":"6","founder":"1","head_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","username":"17306000000","nickname":"周董","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk="},{"fsate":"1","user_id":"5","founder":"3","head_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","username":"17364583794","nickname":"范某人丶","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI="}]
         */

        public String disturb;
        public String group_img;
        public String group_name;
        public String group_notic;
        public String card;
        @SerializedName("user_list")
        public List<UserListBean> user_list;


        protected AllArrayBean(Parcel in) {
            disturb = in.readString();
            group_img = in.readString();
            group_name = in.readString();
            group_notic = in.readString();
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

        public String getDisturb() {
            return disturb;
        }

        public void setDisturb(String disturb) {
            this.disturb = disturb;
        }

        public String getGroup_img() {
            return group_img;
        }

        public void setGroup_img(String group_img) {
            this.group_img = group_img;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getGroup_notic() {
            return group_notic;
        }

        public void setGroup_notic(String group_notic) {
            this.group_notic = group_notic;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(disturb);
            dest.writeString(group_img);
            dest.writeString(group_name);
            dest.writeString(group_notic);
            dest.writeString(card);
        }


        public static class UserListBean implements Parcelable {
            /**
             * fsate : 1
             * user_id : 6
             * founder : 1
             * head_img : http://api.kfexe.com/upload/20191112/20191112182423348.png
             * username : 17306000000
             * nickname : 周董
             * card : Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk=
             */

            public String fsate;
            public String user_id;
            public String founder;
            public String head_img;
            public String username;
            public String nickname;
            public String card;


            protected UserListBean(Parcel in) {
                fsate = in.readString();
                user_id = in.readString();
                founder = in.readString();
                head_img = in.readString();
                username = in.readString();
                nickname = in.readString();
                card = in.readString();
            }

            public static final Creator<UserListBean> CREATOR = new Creator<UserListBean>() {
                @Override
                public UserListBean createFromParcel(Parcel in) {
                    return new UserListBean(in);
                }

                @Override
                public UserListBean[] newArray(int size) {
                    return new UserListBean[size];
                }
            };

            public String getFsate() {
                return fsate;
            }

            public void setFsate(String fsate) {
                this.fsate = fsate;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getFounder() {
                return founder;
            }

            public void setFounder(String founder) {
                this.founder = founder;
            }

            public String getHead_img() {
                return head_img;
            }

            public void setHead_img(String head_img) {
                this.head_img = head_img;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
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
                dest.writeString(fsate);
                dest.writeString(user_id);
                dest.writeString(founder);
                dest.writeString(head_img);
                dest.writeString(username);
                dest.writeString(nickname);
                dest.writeString(card);
            }
        }
    }
}
