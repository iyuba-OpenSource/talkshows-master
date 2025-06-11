package com.iyuba.lib_common.ui.mvp;

/**
 * @desction:
 * @date: 2023/3/15 17:48
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public interface IPresenter<V extends BaseView> {

    void attachView(V v);

    void detachView();
}
