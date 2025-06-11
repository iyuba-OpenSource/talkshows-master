package com.iyuba.talkshow.data.model.result;

import java.util.List;

/**
 * @desction:
 * @date: 2023/2/1 15:49
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class CourseChooseBookResponse {

    /**
     * result : 1
     * total : 12
     * data : [{"Category":"313","CreateTime":"2020-02-15 06:02:14.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/205.jpg","KeyWords":"小学英语","version":"123","DescCn":"New starting point for grade 1","SeriesCount":"33","SeriesName":"1年级上(新起点)","UpdateTime":"2021-12-07 06:12:21.0","HotFlg":"0","haveMicro":"0","Id":"205"},{"Category":"313","CreateTime":"2020-02-17 02:02:38.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/206.jpg","KeyWords":"小学英语","version":"23","DescCn":"New starting point for grade 1","SeriesCount":"40","SeriesName":"1年级下(新起点)","UpdateTime":"2020-03-23 08:03:04.0","HotFlg":"0","haveMicro":"0","Id":"206"},{"Category":"313","CreateTime":"2020-02-17 02:02:33.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/207.jpg","KeyWords":"小学英语","version":"20","DescCn":"New starting point for grade 2","SeriesCount":"34","SeriesName":"2年级上(新起点)","UpdateTime":"2021-12-07 06:12:33.0","HotFlg":"0","haveMicro":"0","Id":"207"},{"Category":"313","CreateTime":"2020-02-17 02:02:16.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/208.jpg","KeyWords":"小学英语","version":"32","DescCn":"New starting point for grade 2","SeriesCount":"39","SeriesName":"2年级下(新起点)","UpdateTime":"2020-03-23 08:03:14.0","HotFlg":"0","haveMicro":"0","Id":"208"},{"Category":"313","CreateTime":"2020-02-17 02:02:27.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/209.jpg","KeyWords":"小学英语","version":"128","DescCn":"New starting point for grade 3","SeriesCount":"64","SeriesName":"3年级上(新起点)","UpdateTime":"2021-12-07 06:12:44.0","HotFlg":"0","haveMicro":"0","Id":"209"},{"Category":"313","CreateTime":"2020-02-17 02:02:18.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/210.jpg","KeyWords":"小学英语","version":"101","DescCn":"New starting point for grade 3","SeriesCount":"60","SeriesName":"3年级下(新起点)","UpdateTime":"2020-03-23 08:03:44.0","HotFlg":"0","haveMicro":"0","Id":"210"},{"Category":"313","CreateTime":"2020-02-19 11:02:12.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/211.jpg","KeyWords":"小学英语","version":"115","DescCn":"New starting point for grade 4","SeriesCount":"62","SeriesName":"4年级上(新起点)","UpdateTime":"2021-12-07 06:12:00.0","HotFlg":"0","haveMicro":"0","Id":"211"},{"Category":"313","CreateTime":"2020-02-19 11:02:05.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/212.jpg","KeyWords":"小学英语","version":"82","DescCn":"New starting point for grade 4","SeriesCount":"66","SeriesName":"4年级下(新起点)","UpdateTime":"2021-12-07 06:12:00.0","HotFlg":"0","haveMicro":"0","Id":"212"},{"Category":"313","CreateTime":"2020-02-19 11:02:55.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/213.jpg","KeyWords":"小学英语","version":"46","DescCn":"New starting point for grade 5","SeriesCount":"55","SeriesName":"5年级上(新起点)","UpdateTime":"2021-12-07 06:12:12.0","HotFlg":"0","haveMicro":"0","Id":"213"},{"Category":"313","CreateTime":"2020-02-19 11:02:57.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/214.jpg","KeyWords":"小学英语","version":"51","DescCn":"New starting point for grade 5","SeriesCount":"56","SeriesName":"5年级下(新起点)","UpdateTime":"2021-12-07 06:12:57.0","HotFlg":"0","haveMicro":"0","Id":"214"},{"Category":"313","CreateTime":"2020-02-19 11:02:42.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/215.jpg","KeyWords":"小学英语","version":"75","DescCn":"New starting point for grade 6","SeriesCount":"67","SeriesName":"6年级上(新起点)","UpdateTime":"2021-12-07 06:12:44.0","HotFlg":"0","haveMicro":"0","Id":"215"},{"Category":"313","CreateTime":"2020-02-19 11:02:25.0","isVideo":"1","pic":"http://static2.iyuba.cn/images/voaseries/216.jpg","KeyWords":"小学英语","version":"53","DescCn":"New starting point for grade 6","SeriesCount":"58","SeriesName":"6年级下(新起点）","UpdateTime":"2021-12-07 06:12:26.0","HotFlg":"0","haveMicro":"0","Id":"216"}]
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
         * Category : 313
         * CreateTime : 2020-02-15 06:02:14.0
         * isVideo : 1
         * pic : http://static2.iyuba.cn/images/voaseries/205.jpg
         * KeyWords : 小学英语
         * version : 123
         * DescCn : New starting point for grade 1
         * SeriesCount : 33
         * SeriesName : 1年级上(新起点)
         * UpdateTime : 2021-12-07 06:12:21.0
         * HotFlg : 0
         * haveMicro : 0
         * Id : 205
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
