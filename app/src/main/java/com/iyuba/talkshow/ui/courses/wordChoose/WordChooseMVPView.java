package com.iyuba.talkshow.ui.courses.wordChoose;


import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.ui.base.MvpView;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.Series;

import java.util.List;

public interface WordChooseMVPView extends MvpView {
     void setMoreCourse(List<SeriesData> beans);
     void setCoures(List<SeriesData> beans);

     //展示类型数据
     void setLesson(List<Series> series);
     //展示类型的错误数据
     void setLessonFail(String showMsg);
}
