package com.iyuba.lib_common.model.remote.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 配音-正式发布的数据
 */
public class Dubbing_publish_release implements Serializable {


    /**
     * result : true
     * data : [{"ImgSrc":"http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=15300794&size=big","UserName":"test010mnb","Title":"Muddy Puddles","Pic":"http://staticvip.iyuba.cn/images/voa/309680.jpg","type":"talkshow","CreateDate":"2024-03-13 18:35:05","score":48,"videoUrl":"video/voa/kouyu/2024/3/13/1710326105601.mp4","UserId":15300794,"agreeCount":0,"Title_cn":"第一季01集  泥坑","TopicId":309680,"id":21315502}]
     * count : 1
     * message : 查询成功
     */

    private Boolean result;
    private Integer count;
    private String message;
    private List<Data> data;

    public Boolean isResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        /**
         * ImgSrc : http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=15300794&size=big
         * UserName : test010mnb
         * Title : Muddy Puddles
         * Pic : http://staticvip.iyuba.cn/images/voa/309680.jpg
         * type : talkshow
         * CreateDate : 2024-03-13 18:35:05
         * score : 48
         * videoUrl : video/voa/kouyu/2024/3/13/1710326105601.mp4
         * UserId : 15300794
         * agreeCount : 0
         * Title_cn : 第一季01集  泥坑
         * TopicId : 309680
         * id : 21315502
         */

        private String ImgSrc;
        private String UserName;
        private String Title;
        private String Pic;
        private String type;
        private String CreateDate;
        private Integer score;
        private String videoUrl;
        private Integer UserId;
        private Integer agreeCount;
        private String Title_cn;
        private Integer TopicId;
        private Integer id;

        public String getImgSrc() {
            return ImgSrc;
        }

        public void setImgSrc(String ImgSrc) {
            this.ImgSrc = ImgSrc;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getPic() {
            return Pic;
        }

        public void setPic(String Pic) {
            this.Pic = Pic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String CreateDate) {
            this.CreateDate = CreateDate;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public Integer getUserId() {
            return UserId;
        }

        public void setUserId(Integer UserId) {
            this.UserId = UserId;
        }

        public Integer getAgreeCount() {
            return agreeCount;
        }

        public void setAgreeCount(Integer agreeCount) {
            this.agreeCount = agreeCount;
        }

        public String getTitle_cn() {
            return Title_cn;
        }

        public void setTitle_cn(String Title_cn) {
            this.Title_cn = Title_cn;
        }

        public Integer getTopicId() {
            return TopicId;
        }

        public void setTopicId(Integer TopicId) {
            this.TopicId = TopicId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
