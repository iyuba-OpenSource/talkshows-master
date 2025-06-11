package com.iyuba.talkshow.ui.lil.ui.lesson.study;

import com.iyuba.lib_common.bean.ChapterDetailBean;
import com.iyuba.lib_common.ui.mvp.BaseView;

import java.util.List;

/**
 * @title:
 * @date: 2024/1/3 15:12
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface StudyView extends BaseView {

    //展示数据
    void showData(List<ChapterDetailBean> list);

    //显示加载
    void showLoading(String msg);

    //收藏文章
    void showCollectArticle(boolean isSuccess,boolean isCollect);
}
