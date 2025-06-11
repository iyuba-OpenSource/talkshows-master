package com.iyuba.talkshow.ui.user.login;

import android.util.Log;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.ChangeNameResponse;
import com.iyuba.talkshow.data.remote.UserService;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class ChangeNamePresenter extends BasePresenter<ChangeNameMvpView> {
    private final DataManager mDataManager;

    private Subscription mRegisterSub;

    @Inject
    public ChangeNamePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mRegisterSub);
    }

    public void ChangeUserName(String uid, String username, String oldUsername) {
        checkViewAttached();
        RxUtil.unsubscribe(mRegisterSub);
        mRegisterSub = mDataManager.ChangeUserName(uid, username, oldUsername)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangeNameResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dismissWaitingDialog();
                        if(e != null) {
                            Log.e("ChangeNameActivity", "confirmPassword onError " + e.getMessage());
                        }
                        if(!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
                            getMvpView().showToast(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onNext(ChangeNameResponse response) {
                        getMvpView().dismissWaitingDialog();
                        if (response == null) {
                            getMvpView().showToast(R.string.request_fail);
                            return;
                        }
                        switch (response.result) {
                            case "121":
                                //使用20001接口重新获取数据
                                UserInfoManager.getInstance().getRemoteUserInfo(UserInfoManager.getInstance().getUserId(), null);

                                getMvpView().showToast(R.string.edit_success);
                                getMvpView().finishChangeActivity();
                                break;
                            case "0":
                            case "000":
                                getMvpView().showToast(UserService.Register.Result.Message.USERNAME_EXIST);
                                break;
                        }
                    }
                });
    }

}
