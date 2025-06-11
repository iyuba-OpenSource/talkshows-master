package com.iyuba.talkshow.data.model;

import java.util.List;

public class NewSeriesDataResponse {


    /**
     * result : 1
     * total : 5
     * data : [{"Category":"316","CreateTime":"2020-02-20 02:02:17.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/217.jpg","KeyWords":"初中英语","version":"68","DescCn":"7th grade master","SeriesCount":"21","SeriesName":"7年级上(人教版)","UpdateTime":"2020-03-23 08:03:19.0","HotFlg":"0","haveMicro":"1","Id":"217"},{"Category":"316","CreateTime":"2020-02-20 02:02:55.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/218.jpg","KeyWords":"初中英语","version":"154","DescCn":"7th grade lower","SeriesCount":"21","SeriesName":"7年级下(人教版)","UpdateTime":"2020-03-23 08:03:31.0","HotFlg":"0","haveMicro":"1","Id":"218"},{"Category":"316","CreateTime":"2020-02-20 02:02:24.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/219.jpg","KeyWords":"初中英语","version":"87","DescCn":"8th grade master","SeriesCount":"20","SeriesName":"8年级上(人教版)","UpdateTime":"2020-03-23 08:03:42.0","HotFlg":"0","haveMicro":"1","Id":"219"},{"Category":"316","CreateTime":"2020-02-20 02:02:50.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/220.jpg","KeyWords":"初中英语","version":"34","DescCn":"8th grade lower","SeriesCount":"18","SeriesName":"8年级下(人教版)","UpdateTime":"2020-03-23 08:03:53.0","HotFlg":"0","haveMicro":"1","Id":"220"},{"Category":"316","CreateTime":"2020-02-20 02:02:21.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/221.jpg","KeyWords":"初中英语","version":"19","DescCn":"Grade 9 Whole Person","SeriesCount":"28","SeriesName":"9年级全(人教版)","UpdateTime":"2020-03-23 08:03:27.0","HotFlg":"0","haveMicro":"1","Id":"221"}]
     */

    private String result;
    private int total;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
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
         * Category : 316
         * CreateTime : 2020-02-20 02:02:17.0
         * isVideo : 1
         * pic : http://static2.iyuba.cn/images/voaseries/217.jpg
         * KeyWords : 初中英语
         * version : 68
         * DescCn : 7th grade master
         * SeriesCount : 21
         * SeriesName : 7年级上(人教版)
         * UpdateTime : 2020-03-23 08:03:19.0
         * HotFlg : 0
         * haveMicro : 1
         * Id : 217
         */

        private String Category;
        private String CreateTime;
        private String isVideo;
        private String pic;
        private String KeyWords;
        private String version;
        private String DescCn;
        private String SeriesCount;
        private String SeriesName;
        private String UpdateTime;
        private String HotFlg;
        private String haveMicro;
        private String Id;

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(String isVideo) {
            this.isVideo = isVideo;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getKeyWords() {
            return KeyWords;
        }

        public void setKeyWords(String KeyWords) {
            this.KeyWords = KeyWords;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescCn() {
            return DescCn;
        }

        public void setDescCn(String DescCn) {
            this.DescCn = DescCn;
        }

        public String getSeriesCount() {
            return SeriesCount;
        }

        public void setSeriesCount(String SeriesCount) {
            this.SeriesCount = SeriesCount;
        }

        public String getSeriesName() {
            return SeriesName;
        }

        public void setSeriesName(String SeriesName) {
            this.SeriesName = SeriesName;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public String getHotFlg() {
            return HotFlg;
        }

        public void setHotFlg(String HotFlg) {
            this.HotFlg = HotFlg;
        }

        public String getHaveMicro() {
            return haveMicro;
        }

        public void setHaveMicro(String haveMicro) {
            this.haveMicro = haveMicro;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }
    }
}
