package com.iyuba.talkshow.ui.user.register.email;

import android.content.Context;

import com.iyuba.lib_user.listener.UserinfoCallbackListener;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.result.RegisterResponse;
import com.iyuba.talkshow.data.remote.UserService;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.GetLocation;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.ServiceMsgUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class RegisterPresenter extends BasePresenter<RegisterMvpView> {

    private final DataManager mDataManager;
    private final GetLocation mGetLocation;

    private boolean isSendRegisterRequest = false;
    private Subscription mRegisterSub;
    private Subscription mLoginSub;

    @Inject
    public RegisterPresenter(DataManager dataManager,
                             GetLocation getLocation) {
        this.mDataManager = dataManager;
        this.mGetLocation = getLocation;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mRegisterSub);
        RxUtil.unsubscribe(mLoginSub);
    }

    public void register(final String username, final String password, final String email) {
        checkViewAttached();
        RxUtil.unsubscribe(mRegisterSub);
        if (!isSendRegisterRequest) {
            isSendRegisterRequest = true;
            getMvpView().showWaitingDialog();
            mRegisterSub = mDataManager.registerByEmail(username, password, email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RegisterResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().dismissWaitingDialog();
                            if(!NetStateUtil.isConnected((Context) getMvpView())) {
                                getMvpView().showToast(R.string.please_check_network);
                            } else {
                                getMvpView().showToast(R.string.request_fail);
                            }
                        }

                        @Override
                        public void onNext(RegisterResponse registerResponse) {

                            String errMsg = null;
                            switch (registerResponse.result()) {
                                case UserService.Register.Result.Code.SUCCESS:
                                    registerSuccess(Integer.parseInt(registerResponse.uid()));
                                    break;
                                case UserService.Register.Result.Code.USERNAME_EXIST:
                                    isSendRegisterRequest = false;
                                    errMsg = UserService.Register.Result.Message.USERNAME_EXIST;
                                    break;
                                default:
                                    isSendRegisterRequest = false;
                                    errMsg = ServiceMsgUtil.showRegisterErrorMsg(String.valueOf(registerResponse.result()));
//                                    errMsg = UserService.Register.Result.Message.EMAIL_REGISTERED;
                                    break;
                            }
                            if(errMsg != null) {
                                getMvpView().dismissWaitingDialog();
                                getMvpView().showToast(errMsg);
                            }
                        }
                    });
        } else {
            getMvpView().showToast(((Context) getMvpView()).getString(R.string.register_operating));
        }
    }

    /*private void registerSuccess(final String username, final String password) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoginSub);

        *//*Location location = mGetLocation.getLocation();
        double latitude = location != null? location.getLatitude() : 0;
        double longitude = location != null?location.getLongitude() : 0;
        mLoginSub = mAccountManager.login(username, password,
                latitude, longitude, new OnLoginListener() {
                    @Override
                    public void onLoginSuccess(User user) {
                        getMvpView().dismissWaitingDialog();
                        getMvpView().startUploadImageActivity();
                        getMvpView().finishRegisterActivity();
                    }

                    @Override
                    public void onLoginFail(String errorMsg) {
                        getMvpView().dismissWaitingDialog();
                    }
                });*//*

        //使用新的登录操作
        UserInfoManager.getInstance().postRemoteAccountLogin(username, password, new UserinfoCallbackListener() {
            @Override
            public void onSuccess() {
                getMvpView().dismissWaitingDialog();
                getMvpView().startUploadImageActivity();
                getMvpView().finishRegisterActivity();
            }

            @Override
            public void onFail(String errorMsg) {
                getMvpView().dismissWaitingDialog();
            }
        });
    }*/

    private void registerSuccess(int userId){
        UserInfoManager.getInstance().getRemoteUserInfo(userId, new UserinfoCallbackListener() {
            @Override
            public void onSuccess() {
                getMvpView().dismissWaitingDialog();
                getMvpView().startUploadImageActivity();
                getMvpView().finishRegisterActivity();
            }

            @Override
            public void onFail(String errorMsg) {
                getMvpView().dismissWaitingDialog();
            }
        });
    }
}
