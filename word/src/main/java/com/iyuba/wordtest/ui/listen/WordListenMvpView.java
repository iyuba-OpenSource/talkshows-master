package com.iyuba.wordtest.ui.listen;

import com.iyuba.module.mvp.MvpView;
import com.iyuba.wordtest.entity.WordEntity;

/**
 * @desction:
 * @date: 2023/2/7 10:18
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public interface WordListenMvpView extends MvpView {

    void showSearchResult(WordEntity entity);

    void showText(String msg);
}
