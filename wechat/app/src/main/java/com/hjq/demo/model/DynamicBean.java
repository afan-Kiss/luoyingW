package com.hjq.demo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author GF
 * @des 动态
 * @date 2019/11/16
 */
public class DynamicBean {
    /**
     * state : 1
     * msg : 成功
     * all_array : [{"user_img":"http://api.kfexe.com/upload/20191115/20191115182310667.png","user_name":"13395052726","release_time":"1573814642","release_val":"|八节课了劳斯莱斯给我看书吧","fid":"56","card":"M2UyMGJhZWI2ZmE4ZTVmOTgzNGQ4NWVlMjc4NzYyYmQ="},{"user_img":"http://api.kfexe.com/upload/20191115/20191115182310667.png","user_name":"13395052726","release_time":"1573814633","release_val":"|规划设计","fid":"55","card":"M2UyMGJhZWI2ZmE4ZTVmOTgzNGQ4NWVlMjc4NzYyYmQ="},{"user_img":"http://api.kfexe.com/aupload/1573285501.jpg","user_name":"流量监控","release_time":"1573808674","release_val":"http://api.kfexe.com/upload/20191115/20191115170428523.png|http://api.kfexe.com/upload/20191115/20191115170427557.png|http://api.kfexe.com/upload/20191115/20191115170427622.png|http://api.kfexe.com/upload/20191115/20191115170427589.png|http://api.kfexe.com/upload/20191115/20191115170429130.png|http://api.kfexe.com/upload/20191115/20191115170429435.png|http://api.kfexe.com/upload/20191115/20191115170432841.png|http://api.kfexe.com/upload/20191115/20191115170431608.png|http://api.kfexe.com/upload/20191115/20191115170433200.png|测试一下发布九张图片","fid":"52","card":"MGNiOGMwMjNjMzk3NWE1ZjQyZmM0YmVjZjljNTdjZTI="},{"user_img":"http://api.kfexe.com/aupload/1573285501.jpg","user_name":"流量监控","release_time":"1573808541","release_val":"http://api.kfexe.com/upload/20191115/20191115170221919.png|测试一下发布","fid":"51","card":"MGNiOGMwMjNjMzk3NWE1ZjQyZmM0YmVjZjljNTdjZTI="},{"user_img":"http://api.kfexe.com/aupload/1573793867.jpg","user_name":"发个广告","release_time":"1573806923","release_val":"|tulonm","fid":"50","card":"ZTBiYjU5YzkxMmExOTAzZjJiMTNkM2M0OWNkNzM2ZDQ="},{"user_img":"http://api.kfexe.com/upload/20191106/20191106152047539.png","user_name":"今天z","release_time":"1573805750","release_val":"http://api.kfexe.com/upload/20191115/20191115161550500.png|llooo","fid":"47","card":"ZGRiYTZlN2NhZmRmNTE4OGFhYjE3OTJhNzI0MWI0ZGY="},{"user_img":"http://api.kfexe.com/aupload/1573285501.jpg","user_name":"流量监控","release_time":"1573805691","release_val":"http://api.kfexe.com/upload/20191115/20191115161451347.png|ggjkk","fid":"45","card":"MGNiOGMwMjNjMzk3NWE1ZjQyZmM0YmVjZjljNTdjZTI="},{"user_img":"http://api.kfexe.com/aupload/1573285501.jpg","user_name":"流量监控","release_time":"1573795318","release_val":"|刚刚好回家","fid":"40","card":"MGNiOGMwMjNjMzk3NWE1ZjQyZmM0YmVjZjljNTdjZTI="},{"user_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","user_name":"测试账号","release_time":"1573638596","release_val":"|今天的第一场演唱会门票","fid":"38","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk="},{"user_img":"http://api.kfexe.com/headimg/default.jpg","user_name":"13559936636","release_time":"1573548604","release_val":"|东躲西藏现金","fid":"35","card":"MDUwMTUxOGFmOTdiOTBmODk3OGJjNDJjMmQ2NzlhYzQ="}]
     * features_circle : d565438731c9e51cfe1f96d31675423f
     * endpage : 3
     * page : 1
     */

    private int state;
    private String msg;
    private String features_circle;
    private int endpage;
    private int page;
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

    public String getFeatures_circle() {
        return features_circle;
    }

    public void setFeatures_circle(String features_circle) {
        this.features_circle = features_circle;
    }

    public int getEndpage() {
        return endpage;
    }

    public void setEndpage(int endpage) {
        this.endpage = endpage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<AllArrayBean> getAll_array() {
        return all_array;
    }

    public void setAll_array(List<AllArrayBean> all_array) {
        this.all_array = all_array;
    }

    public static class AllArrayBean implements MultiItemEntity {
        /**
         * user_img : http://api.kfexe.com/upload/20191115/20191115182310667.png
         * user_name : 13395052726
         * release_time : 1573814642
         * release_val : |八节课了劳斯莱斯给我看书吧
         * fid : 56
         * card : M2UyMGJhZWI2ZmE4ZTVmOTgzNGQ4NWVlMjc4NzYyYmQ=
         */

        private String user_img;
        private String user_name;
        private String release_time;
        private String release_val;
        private String fid;
        private String card;
        private int itemType = 2;
        private String circle_img;

        public String getCircle_img() {
            return circle_img;
        }

        public void setCircle_img(String circle_img) {
            this.circle_img = circle_img;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getRelease_time() {
            return release_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        public String getRelease_val() {
            return release_val;
        }

        public void setRelease_val(String release_val) {
            this.release_val = release_val;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
