package com.iyuba.talkshow.ui.search;

import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

/**
 * SearchMvpView
 *
 * @author wayne
 * @date 2018/2/10
 */
public interface SearchMvpView extends MvpView {
    void showVoas(List<Voa> list);

    void dismissLoading();

    void showLoading();

    void showEmptyVoas();

    void resetAd();
}
