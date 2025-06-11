//package com.iyuba.talkshow.data;
//
//import java.util.List;
//
//public class SeriesResponse {
//
//
//    /**
//     * result : 1
//     * total : 10
//     * data : [{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"},{"Category":"","CreateTime":"2019-06-19 13:58:24.0","Title":"What can the country see","Sound":"http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3","Pic":"http://static.iyuba.cn/images/voa/309556.jpg","Flag":"1","Type":"series","DescCn":"佩奇一家开车去乡村去，在乡村它们可以看到什么呀？","Title_cn":"乡村能看到什么呀","series":"201","CategoryName":"","Id":"309556","ReadCount":"8"}]
//     * message : success
//     */
//
//    private int result;
//    private int total;
//    private String message;
//    private List<SeriesBean> data;
//
//    public int getResult() {
//        return result;
//    }
//
//    public void setResult(int result) {
//        this.result = result;
//    }
//
//    public int getTotal() {
//        return total;
//    }
//
//    public void setTotal(int total) {
//        this.total = total;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public List<SeriesBean> getData() {
//        return data;
//    }
//
//    public void setData(List<SeriesBean> data) {
//        this.data = data;
//    }
//
//    public static class SeriesBean {
//        /**
//         * Category :
//         * CreateTime : 2019-06-19 13:58:24.0
//         * Title : What can the country see
//         * Sound : http://staticvip.iyuba.cn/sounds/voa/201906/309556.mp3
//         * Pic : http://static.iyuba.cn/images/voa/309556.jpg
//         * Flag : 1
//         * Type : series
//         * DescCn : 佩奇一家开车去乡村去，在乡村它们可以看到什么呀？
//         * Title_cn : 乡村能看到什么呀
//         * series : 201
//         * CategoryName :
//         * Id : 309556
//         * ReadCount : 8
//         */
//
//        private String Category;
//        private String CreateTime;
//        private String Title;
//        private String Sound;
//        private String Pic;
//        private String Flag;
//        private String Type;
//        private String DescCn;
//        private String Title_cn;
//        private String series;
//        private String CategoryName;
//        private String Id;
//        private String ReadCount;
//
//        public String getCategory() {
//            return Category;
//        }
//
//        public void setCategory(String Category) {
//            this.Category = Category;
//        }
//
//        public String getCreateTime() {
//            return CreateTime;
//        }
//
//        public void setCreateTime(String CreateTime) {
//            this.CreateTime = CreateTime;
//        }
//
//        public String getTitle() {
//            return Title;
//        }
//
//        public void setTitle(String Title) {
//            this.Title = Title;
//        }
//
//        public String getSound() {
//            return Sound;
//        }
//
//        public void setSound(String Sound) {
//            this.Sound = Sound;
//        }
//
//        public String getPic() {
//            return Pic;
//        }
//
//        public void setPic(String Pic) {
//            this.Pic = Pic;
//        }
//
//        public String getFlag() {
//            return Flag;
//        }
//
//        public void setFlag(String Flag) {
//            this.Flag = Flag;
//        }
//
//        public String getType() {
//            return Type;
//        }
//
//        public void setType(String Type) {
//            this.Type = Type;
//        }
//
//        public String getDescCn() {
//            return DescCn;
//        }
//
//        public void setDescCn(String DescCn) {
//            this.DescCn = DescCn;
//        }
//
//        public String getTitle_cn() {
//            return Title_cn;
//        }
//
//        public void setTitle_cn(String Title_cn) {
//            this.Title_cn = Title_cn;
//        }
//
//        public String getSeries() {
//            return series;
//        }
//
//        public void setSeries(String series) {
//            this.series = series;
//        }
//
//        public String getCategoryName() {
//            return CategoryName;
//        }
//
//        public void setCategoryName(String CategoryName) {
//            this.CategoryName = CategoryName;
//        }
//
//        public String getId() {
//            return Id;
//        }
//
//        public void setId(String Id) {
//            this.Id = Id;
//        }
//
//        public String getReadCount() {
//            return ReadCount;
//        }
//
//        public void setReadCount(String ReadCount) {
//            this.ReadCount = ReadCount;
//        }
//    }
//}
