package com.hjq.demo.model;

import java.util.List;

public class CharModel {

    /**
     * state : 1
     * msg : 成功
     * all_array : [{"id":"600","rval":"asdasdqwdasdasd","rtype":"1","rclass":"1","rtime":"1574005414","duration":"0","disturb":"0","nickname":"范某人丶","group_name":"","group_id":"","group_img":"","user_id":"5","head_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","user_card":"","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI="}]
     * features_chat : 623582cef10e4a6e79625fbdee4030d9
     */

    private int state;
    private String msg;
    private String features_chat;
    private List<AllArrayBean> all_array;

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

    public String getFeatures_chat() {
        return features_chat;
    }

    public void setFeatures_chat(String features_chat) {
        this.features_chat = features_chat;
    }

    public List<AllArrayBean> getAll_array() {
        return all_array;
    }

    public void setAll_array(List<AllArrayBean> all_array) {
        this.all_array = all_array;
    }

    public static class AllArrayBean {
        /**
         * id : 600
         * rval : asdasdqwdasdasd
         * rtype : 1
         * rclass : 1
         * rtime : 1574005414
         * duration : 0
         * disturb : 0
         * nickname : 范某人丶
         * group_name :
         * group_id :
         * group_img :
         * user_id : 5
         * head_img : http://api.kfexe.com/upload/20191115/20191115233044636.png
         * user_card :
         * card : ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI=
         */

        private String id;
        private String rval;
        private String rtype;
        private String rclass;
        private String rtime;
        private long duration;
        private String disturb;
        private String nickname;
        private String group_name;
        private String group_id;
        private String group_img;
        private String user_id;
        private String head_img;
        private String user_card;
        private String card;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRval() {
            return rval;
        }

        public void setRval(String rval) {
            this.rval = rval;
        }

        public String getRtype() {
            return rtype;
        }

        public void setRtype(String rtype) {
            this.rtype = rtype;
        }

        public String getRclass() {
            return rclass;
        }

        public void setRclass(String rclass) {
            this.rclass = rclass;
        }

        public String getRtime() {
            return rtime;
        }

        public void setRtime(String rtime) {
            this.rtime = rtime;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getDisturb() {
            return disturb;
        }

        public void setDisturb(String disturb) {
            this.disturb = disturb;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGroup_img() {
            return group_img;
        }

        public void setGroup_img(String group_img) {
            this.group_img = group_img;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getUser_card() {
            return user_card;
        }

        public void setUser_card(String user_card) {
            this.user_card = user_card;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }
    }
}
