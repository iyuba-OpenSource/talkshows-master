package com.iyuba.talkshow.ui.rank;

import com.iyuba.talkshow.data.model.RankingListBean;
import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

/**
 * RankMvpView
 *
 * @author wayne
 * @date 2018/2/6
 */
public interface RankMvpView extends MvpView {
    void showRankingList(List<RankingListBean.DataBean> data);

    void showMoreRankList(List<RankingListBean.DataBean> data);

    void showUserInfo(RankingListBean rankingListBean);

    void showLoadingDialog();

    void dismissLoadingDialog();

}
