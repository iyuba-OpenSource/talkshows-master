package com.iyuba.talkshow.data.model;

import com.iyuba.talkshow.ui.courses.wordChoose.bean.Series;

import java.util.List;

public class LessonNewResponse {

    public int result;
    public primaryData data;
    public String message;
    public List<String> lessonType;

    public class primaryData {
        public List<Series> primary;
    }

}
