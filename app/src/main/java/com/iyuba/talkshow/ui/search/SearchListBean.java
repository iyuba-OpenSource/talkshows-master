package com.iyuba.talkshow.ui.search;

import java.util.List;

/**
 * SearchListBean
 *
 * @author wayne
 * @date 2018/2/10
 */
public class SearchListBean {
    /**
     * total : 5
     * data : [{"textFind":0,"CreatTime":"2018-01-30 18:27:40.0","Category":"306","Title":"\u201c火中取栗\u201d的勇敢飞鸟","Sound":"/201801/306027.mp3","Pic":"http://static."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"images/voa/306027.jpg","VoaId":"306027","Url":"","DescCn":"大自然真是神奇的造物者，一场大火是对草原的毁灭，却又给勇敢鸟儿们以契机。造物主只馈赠有准备的人！","Title_cn":"\u201c火中取栗\u201d的勇敢飞鸟","PublishTime":"","HotFlg":"1","titleFind":1,"ReadCount":"108"},{"textFind":0,"CreatTime":"2017-02-28 00:00:00.0","Category":"308","Title":"Mysteries of the Unseen World","Sound":"/201702/308009.mp3","Pic":"http://static."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"images/voa/308009.jpg","VoaId":"308009","Url":"","DescCn":"蝴蝶飞不过沧海，然而蜻蜓却能。看看蜻蜓的飞行神技能","Title_cn":"自然界中最出色的飞行员","PublishTime":"","HotFlg":"4","titleFind":1,"ReadCount":"2196"},{"textFind":0,"CreatTime":"2017-02-28 00:00:00.0","Category":"305","Title":"How Guys Should Act on a First Date VS How They Do Act","Sound":"/201702/305011.mp3","Pic":"http://static."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"images/voa/305011.jpg","VoaId":"305011","Url":"","DescCn":"尴尬~","Title_cn":"男生第一次约会理想与现实中的距离","PublishTime":"","HotFlg":"4","titleFind":1,"ReadCount":"1019"},{"textFind":0,"CreatTime":"2017-02-28 00:00:00.0","Category":"305","Title":"容易误会的中式英语","Sound":"/201702/305012.mp3","Pic":"http://static."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"images/voa/305012.jpg","VoaId":"305012","Url":"","DescCn":"中国人爱说\u201c一起玩\u201d，但和老外聊天不要用play这个词哦。为什么？一起来看看","Title_cn":"容易误会的中式英语","PublishTime":"","HotFlg":"4","titleFind":1,"ReadCount":"1177"},{"textFind":0,"CreatTime":"2016-08-10 18:49:40.0","Category":"310","Title":"My Songs Know What You Did In The Dark","Sound":"/201608/310001.mp3","Pic":"http://static."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"images/voa/310001.jpg","VoaId":"310001","Url":"","DescCn":"Fall Out Boy 2013年度全新专辑《Save Rock and Roll》主打歌。首发气势不凡，蝉联英国摇滚单曲金榜8周冠军。音乐片段仅供学习！","Title_cn":"我的歌知道你在黑暗中做了什么","PublishTime":"","HotFlg":"3","titleFind":1,"ReadCount":"2294"}]
     */

    private String total;
    private List<DataBean> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * textFind : 0
         * CreatTime : 2018-01-30 18:27:40.0
         * Category : 306
         * Title : “火中取栗”的勇敢飞鸟
         * Sound : /201801/306027.mp3
         * Pic : http://static."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"images/voa/306027.jpg
         * VoaId : 306027
         * Url :
         * DescCn : 大自然真是神奇的造物者，一场大火是对草原的毁灭，却又给勇敢鸟儿们以契机。造物主只馈赠有准备的人！
         * Title_cn : “火中取栗”的勇敢飞鸟
         * PublishTime :
         * HotFlg : 1
         * titleFind : 1
         * ReadCount : 108
         */

        private int textFind;
        private String CreatTime;
        private String Category;
        private String Title;
        private String Sound;
        private String Pic;
        private String VoaId;
        private String Url;
        private String DescCn;
        private String Title_cn;
        private String PublishTime;
        private String HotFlg;
        private int titleFind;
        private String ReadCount;

        //增加video字段
        private String Video;

        public String getVideo() {
            return Video;
        }

        public void setVideo(String video) {
            Video = video;
        }

        public int getTextFind() {
            return textFind;
        }

        public void setTextFind(int textFind) {
            this.textFind = textFind;
        }

        public String getCreatTime() {
            return CreatTime;
        }

        public void setCreatTime(String CreatTime) {
            this.CreatTime = CreatTime;
        }

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getSound() {
            return Sound;
        }

        public void setSound(String Sound) {
            this.Sound = Sound;
        }

        public String getPic() {
            return Pic;
        }

        public void setPic(String Pic) {
            this.Pic = Pic;
        }

        public String getVoaId() {
            return VoaId;
        }

        public void setVoaId(String VoaId) {
            this.VoaId = VoaId;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public String getDescCn() {
            return DescCn;
        }

        public void setDescCn(String DescCn) {
            this.DescCn = DescCn;
        }

        public String getTitle_cn() {
            return Title_cn;
        }

        public void setTitle_cn(String Title_cn) {
            this.Title_cn = Title_cn;
        }

        public String getPublishTime() {
            return PublishTime;
        }

        public void setPublishTime(String PublishTime) {
            this.PublishTime = PublishTime;
        }

        public String getHotFlg() {
            return HotFlg;
        }

        public void setHotFlg(String HotFlg) {
            this.HotFlg = HotFlg;
        }

        public int getTitleFind() {
            return titleFind;
        }

        public void setTitleFind(int titleFind) {
            this.titleFind = titleFind;
        }

        public String getReadCount() {
            return ReadCount;
        }

        public void setReadCount(String ReadCount) {
            this.ReadCount = ReadCount;
        }
    }
}
