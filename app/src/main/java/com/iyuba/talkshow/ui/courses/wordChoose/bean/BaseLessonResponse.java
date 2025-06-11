package com.iyuba.talkshow.ui.courses.wordChoose.bean;

import java.util.List;

/**
 * @desction:
 * @date: 2023/2/1 19:10
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class BaseLessonResponse<T>{

    public int result;
    public T data;
    public String message;
    public List<String> lessonType;
}
