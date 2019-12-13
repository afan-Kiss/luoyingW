package com.hjq.demo.model;

import java.util.List;

/**
 * @author GF
 * @des 评论列表
 * @date 2019/11/17
 */
public class DynamicUcomment {
    /**
     * state : 1
     * msg : 成功
     * all_array : [{"user_img":"http://api.kfexe.com/upload/20191104/20191104160718709.png","nickname":"周爷我怕怕","fid":"29","cid":"37","card":"MzA0MmJkOGM5NmRmY2VlZWEzYmE5MWUxNTA5YTdjOTQ=","ftime":"1573531669","textval":"我们"}]
     * features_comment : a5bfc9e07964f8dddeb95fc584cd965d
     */

    private int state;
    private String msg;
    private String features_comment;
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

    public String getFeatures_comment() {
        return features_comment;
    }

    public void setFeatures_comment(String features_comment) {
        this.features_comment = features_comment;
    }

    public List<AllArrayBean> getAll_array() {
        return all_array;
    }

    public void setAll_array(List<AllArrayBean> all_array) {
        this.all_array = all_array;
    }

    public static class AllArrayBean {
        /**
         * user_img : http://api.kfexe.com/upload/20191104/20191104160718709.png
         * nickname : 周爷我怕怕
         * fid : 29
         * cid : 37
         * card : MzA0MmJkOGM5NmRmY2VlZWEzYmE5MWUxNTA5YTdjOTQ=
         * ftime : 1573531669
         * textval : 我们
         */

        private String user_img;
        private String nickname;
        private String fid;
        private String cid;
        private String card;
        private String ftime;
        private String textval;

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }

        public String getTextval() {
            return textval;
        }

        public void setTextval(String textval) {
            this.textval = textval;
        }
    }
}
