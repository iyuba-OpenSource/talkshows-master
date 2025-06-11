package com.iyuba.talkshow.ui.courses.wordChoose.bean;

import java.util.List;

/**
 * @desction:
 * @date: 2023/2/1 19:02
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class Series {

    public List<SeriesDatas> SeriesData;
    public String SourceType;

    public static class SeriesDatas {
        public int Category;
        public String SeriesName;
        public String lessonName;
        public String isVideo;
    }
}
