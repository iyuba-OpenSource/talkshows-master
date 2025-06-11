package com.iyuba.talkshow.data.model.result;

import java.util.List;

/**
 * @desction:
 * @date: 2023/2/1 15:48
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class CourseChooseTypeResponse {

    /**
     * result : 200
     * data : {"primary":[{"SeriesData":[{"Category":"313","SeriesName":"新起点","isVideo":"1","lessonName":"primaryEnglish"},{"Category":"314","SeriesName":"PEP","isVideo":"1","lessonName":"primaryEnglish"},{"Category":"315","SeriesName":"精通","isVideo":"1","lessonName":"primaryEnglish"}],"SourceType":"人教版"},{"SeriesData":[{"Category":"320","SeriesName":"一起点","isVideo":"1","lessonName":"primaryEnglish"},{"Category":"319","SeriesName":"三起点","isVideo":"1","lessonName":"primaryEnglish"}],"SourceType":"北师版"},{"SeriesData":[{"Category":"336","SeriesName":"北京版","isVideo":"0","lessonName":"primaryEnglish"}],"SourceType":"北京版"},{"SeriesData":[{"Category":"337","SeriesName":"新概念","isVideo":"0","lessonName":"concept"}],"SourceType":"新概念"}]}
     * message : Success
     * lessonType : ["primary"]
     */

    private String result;
    private DataBean data;
    private String message;
    private List<String> lessonType;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getLessonType() {
        return lessonType;
    }

    public void setLessonType(List<String> lessonType) {
        this.lessonType = lessonType;
    }

    public static class DataBean {
        private List<PrimaryBean> primary;

        public List<PrimaryBean> getPrimary() {
            return primary;
        }

        public void setPrimary(List<PrimaryBean> primary) {
            this.primary = primary;
        }

        public static class PrimaryBean {
            /**
             * SeriesData : [{"Category":"313","SeriesName":"新起点","isVideo":"1","lessonName":"primaryEnglish"},{"Category":"314","SeriesName":"PEP","isVideo":"1","lessonName":"primaryEnglish"},{"Category":"315","SeriesName":"精通","isVideo":"1","lessonName":"primaryEnglish"}]
             * SourceType : 人教版
             */

            private String SourceType;
            private List<SeriesDataBean> SeriesData;

            public String getSourceType() {
                return SourceType;
            }

            public void setSourceType(String SourceType) {
                this.SourceType = SourceType;
            }

            public List<SeriesDataBean> getSeriesData() {
                return SeriesData;
            }

            public void setSeriesData(List<SeriesDataBean> SeriesData) {
                this.SeriesData = SeriesData;
            }

            public static class SeriesDataBean {
                /**
                 * Category : 313
                 * SeriesName : 新起点
                 * isVideo : 1
                 * lessonName : primaryEnglish
                 */

                private String Category;
                private String SeriesName;
                private String isVideo;
                private String lessonName;

                public String getCategory() {
                    return Category;
                }

                public void setCategory(String Category) {
                    this.Category = Category;
                }

                public String getSeriesName() {
                    return SeriesName;
                }

                public void setSeriesName(String SeriesName) {
                    this.SeriesName = SeriesName;
                }

                public String getIsVideo() {
                    return isVideo;
                }

                public void setIsVideo(String isVideo) {
                    this.isVideo = isVideo;
                }

                public String getLessonName() {
                    return lessonName;
                }

                public void setLessonName(String lessonName) {
                    this.lessonName = lessonName;
                }
            }
        }
    }
}
