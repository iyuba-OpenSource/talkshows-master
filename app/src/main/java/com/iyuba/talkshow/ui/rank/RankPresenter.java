package com.iyuba.talkshow.ui.rank;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.RankingListBean;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.RxUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * RankPresenter
 *
 * @author wayne
 * @date 2018/2/6
 */
@ConfigPersistent
public class RankPresenter extends BasePresenter<RankMvpView> {

    private final DataManager mDataManager;
    private int total = 20;
    private int start = 0;
    private String type;

    private Subscription mRankingListSub;

    @Inject
    public RankPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mRankingListSub);
    }

    public void loadDayRank() {
        start = 0;
        type = "D";
        loadRankList(type, start);
    }

    public void loadWeekRank() {
        start = 0;
        type = "W";
        loadRankList(type, start);
    }

    public void loadMonthRank() {
        start = 0;
        type = "M";
        loadRankList(type, start);
    }

    public void loadRankList(String type, final int start) {
        getMvpView().showLoadingDialog();
        RxUtil.unsubscribe(mRankingListSub);
        mRankingListSub = mDataManager.getRankingList(UserInfoManager.getInstance().getUserId(), type, start, total)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RankingListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().dismissLoadingDialog();
                        getMvpView().showToastShort(R.string.error_loading);
                    }

                    @Override
                    public void onNext(RankingListBean rankingListBean) {
                        getMvpView().dismissLoadingDialog();
                        int result = rankingListBean.getResult();
//                        if (result > 0) {
//                            setStartPos();
//                            if (start == 0) {
//                                getMvpView().showUserInfo(rankingListBean);
//                                getMvpView().showRankingList(rankingListBean.getData());
//                            } else {
//                                getMvpView().showMoreRankList(rankingListBean.getData());
//                            }
//                        }
                        if (result > 0) {
                            setStartPos();
                        }

                        getMvpView().showUserInfo(rankingListBean);
                        if (start == 0) {
                            getMvpView().showRankingList(rankingListBean.getData());
                        } else {
                            getMvpView().showMoreRankList(rankingListBean.getData());
                        }
                    }
                });
    }

    private void setStartPos() {
        start += 20;
    }

    public void loadMoreListData() {
        loadRankList(type, start);
    }
}
