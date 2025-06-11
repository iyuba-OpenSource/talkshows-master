package com.iyuba.talkshow.ui.list;

import android.content.Context;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class ListPresenter extends BasePresenter<ListMvpView> {

    private final DataManager mDataManager;
    private Subscription mLoadNewSub;
    private Subscription mLoadMoreSub;
    private Subscription mLoadByNetSub;
    private Subscription mLoadSub;

    @Inject
    public ListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mLoadNewSub);
        RxUtil.unsubscribe(mLoadMoreSub);
        RxUtil.unsubscribe(mLoadSub);
        RxUtil.unsubscribe(mLoadByNetSub);
    }

    public void loadVoas() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadByNetSub);
        mLoadByNetSub = mDataManager.getMinVoaId()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
                            loadNewVoas(321001);
                        }
                    }

                    @Override
                    public void onNext(Integer id) {
                        if (id != null) {
                            loadNewVoas(id);
                        }
                    }
                });
    }

    private void loadNewVoas(int maxId) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadNewSub);
        mLoadNewSub = mDataManager.syncVoas(maxId, UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<Voa>, Observable<List<Voa>>>() {
                    @Override
                    public Observable<List<Voa>> call(List<Voa> voas) {
                        return mDataManager.getVoas();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Voa>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dismissRefreshingView();
                        if (!NetStateUtil.isConnected((Context) getMvpView())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
                            getMvpView().showToast(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        getMvpView().dismissRefreshingView();
                        if (voas.isEmpty()) {
                            getMvpView().showVoasEmpty();
                        } else {
                            getMvpView().showVoas(voas);
                        }
                    }
                });
    }

    public Subscription loadVoas(int category, String level, int pageNum, int pageSize, Subscriber subscriber) {
        return mDataManager.getVoa(category, level, pageNum, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadNewVoas(int category, String level, int pageNum, int pageSize) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSub);
        getMvpView().showLoadingDialog();
        mLoadSub = loadVoas(category, level, pageNum, pageSize, new Subscriber<List<Voa>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getMvpView().dismissLoadingDialog();
                if (!NetStateUtil.isConnected((Context) getMvpView())) {
                    getMvpView().showToast(R.string.please_check_network);
                } else {
                    getMvpView().showToast(R.string.request_fail);
                }
            }

            @Override
            public void onNext(List<Voa> voas) {
                getMvpView().dismissLoadingDialog();

                //这里针对数据进行控制下
                if (category==309){
                    getMvpView().showVoasEmpty();
                    return;
                }

                if (voas.isEmpty()) {
                    getMvpView().showVoasEmpty();
                } else {
                    getMvpView().showVoas(voas);
                }
            }
        });
    }

    public void loadMoreVoas(int category, String level, int pageNum, int pageSize) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadMoreSub);
        mLoadMoreSub = loadVoas(category, level, pageNum, pageSize, new Subscriber<List<Voa>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!NetStateUtil.isConnected((Context) getMvpView())) {
                    getMvpView().showToast(R.string.please_check_network);
                } else {
                    getMvpView().showToast(R.string.request_fail);
                }
            }

            @Override
            public void onNext(List<Voa> voas) {
                getMvpView().showMoreVoas(voas);
            }
        });
    }
}
