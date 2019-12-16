package com.hjq.demo.model;

import java.util.List;

/**
 * @author GF
 * @des 点赞列表
 * @date 2019/11/17
 */
public class DynamicUlikeBean {
    /**
     * state : 1
     * msg : 成功
     * all_array : [{"user_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","nickname":"范某人丶","fid":"65","cid":"96","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI=","ftime":"1573992460"},{"user_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","fid":"47","cid":"95","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA=","ftime":"1573827331"},{"user_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","nickname":"测试账号","fid":"6","cid":"17","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk=","ftime":"1573018975"},{"user_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","fid":"27","cid":"41","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA=","ftime":"1573460216"},{"user_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","fid":"38","cid":"53","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA=","ftime":"1573708818"},{"user_img":"http://api.kfexe.com/upload/20191104/20191104160718709.png","nickname":"周爷我怕怕","fid":"11","cid":"20","card":"MzA0MmJkOGM5NmRmY2VlZWEzYmE5MWUxNTA5YTdjOTQ=","ftime":"1573115502"},{"user_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","nickname":"测试账号","fid":"11","cid":"23","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk=","ftime":"1573131926"},{"user_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","fid":"11","cid":"29","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA=","ftime":"1573228481"},{"user_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","fid":"25","cid":"37","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA=","ftime":"1573311465"},{"user_img":"http://api.kfexe.com/upload/20191112/20191112182423348.png","nickname":"测试账号","fid":"7","cid":"36","card":"Yzc4Nzk2OThkZTNjN2I1OTM0YTE5NmQxNDcwOGYwOTk=","ftime":"1573290723"},{"user_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","fid":"7","cid":"39","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA=","ftime":"1573460211"},{"user_img":"http://api.kfexe.com/upload/20191104/20191104160718709.png","nickname":"周爷我怕怕","fid":"23","cid":"35","card":"MzA0MmJkOGM5NmRmY2VlZWEzYmE5MWUxNTA5YTdjOTQ=","ftime":"1573289610"},{"user_img":"http://api.kfexe.com/upload/20191102/20191102165259313.png","nickname":"樱花","fid":"23","cid":"40","card":"N2Y5MDVjMmNlZWVjM2Y3MTM3YWIyMjk3MTcxNDJhZjA=","ftime":"1573460213"},{"user_img":"http://api.kfexe.com/upload/20191115/20191115233044636.png","nickname":"范某人丶","fid":"50","cid":"97","card":"ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI=","ftime":"1573993381"}]
     * features_like : dc51f0e0cda80c5a385e27e610af5bad
     */

    private int state;
    private String msg;
    private String features_like;
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

    public String getFeatures_like() {
        return features_like;
    }

    public void setFeatures_like(String features_like) {
        this.features_like = features_like;
    }

    public List<AllArrayBean> getAll_array() {
        return all_array;
    }

    public void setAll_array(List<AllArrayBean> all_array) {
        this.all_array = all_array;
    }

    public static class AllArrayBean {
        /**
         * user_img : http://api.kfexe.com/upload/20191115/20191115233044636.png
         * nickname : 范某人丶
         * fid : 65
         * cid : 96
         * card : ZjJkYjViNDU5OWY5ZmZiNGNlMDQyY2QzMzk0NGIzYjI=
         * ftime : 1573992460
         */

        private String user_img;
        private String nickname;
        private String fid;
        private String cid;
        private String card;
        private String ftime;

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
    }
}
