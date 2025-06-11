package com.iyuba.talkshow.ui.lil.ui.dubbing.my.release;

import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_release;
import com.iyuba.lib_common.model.remote.manager.DubbingManager;
import com.iyuba.lib_common.ui.mvp.BasePresenter;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_user.manager.UserInfoManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyNewReleasePresenter extends BasePresenter<MyNewReleaseView> {

    //获取发布的配音数据
    private Disposable getPublishDis;

    @Override
    public void detachView() {
        super.detachView();

        LibRxUtil.unDisposable(getPublishDis);
    }

    //获取发布的配音数据
    public void getPublishData(){
        checkViewAttach();
        LibRxUtil.unDisposable(getPublishDis);
        DubbingManager.getPublishData(UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Dubbing_publish_release>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getPublishDis = d;
                    }

                    @Override
                    public void onNext(Dubbing_publish_release bean) {
                        if (bean!=null&&bean.isResult()){
                            getMvpView().showData(bean.getData());
                        }else {
                            getMvpView().showError(bean.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError("获取发布的配音数据异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}