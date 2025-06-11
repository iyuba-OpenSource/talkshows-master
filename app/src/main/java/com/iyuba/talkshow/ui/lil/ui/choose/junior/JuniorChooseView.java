package com.iyuba.talkshow.ui.lil.ui.choose.junior;

import android.util.Pair;

import com.iyuba.lib_common.model.local.entity.BookEntity_junior;
import com.iyuba.lib_common.ui.mvp.BaseView;

import java.util.List;

/**
 * @title:
 * @date: 2023/5/22 09:31
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface JuniorChooseView extends BaseView {

    //展示类型数据
    void showTypeData(List<Pair<String,List<Pair<String,String>>>> list);

    //展示书籍数据
    void showBookData(List<BookEntity_junior> list);

    //刷新书籍数据
    void refreshBookData();

    //展示人教版审核数据
    void showPepVerifyData(boolean isFirst);
}
