package com.iyuba.talkshow.ui.courses.courseChoose;


import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

public interface ChooseCourseMVPView extends MvpView {

     void setCoures(List<SeriesData> beans);
}
