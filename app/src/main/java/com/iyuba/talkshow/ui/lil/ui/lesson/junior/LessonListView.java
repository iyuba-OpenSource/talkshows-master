package com.iyuba.talkshow.ui.lil.ui.lesson.junior;

import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.ui.mvp.BaseView;

import java.util.List;

/**
 * @title:
 * @date: 2024/1/2 14:26
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface LessonListView extends BaseView {

    //展示列表数据
    void showData(List<BookChapterBean> list);

    //联网加载数据
    void loadNetData();
}
