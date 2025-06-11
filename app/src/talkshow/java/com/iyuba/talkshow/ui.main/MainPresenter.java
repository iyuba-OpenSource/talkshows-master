package com.iyuba.talkshow.ui.main;

import com.iyuba.lib_user.listener.UserinfoCallbackListener;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.AdManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.manager.VersionManager;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.ui.user.login.UidBean;
import com.iyuba.talkshow.util.GetLocation;
import com.iyuba.talkshow.util.RxUtil;

import javax.inject.Inject;

import cn.aigestudio.downloader.bizs.DLManager;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {
    private ConfigManager mConfigManager;
    private final DataManager mDataManager;
    private final AdManager mAdManager;
    private final DLManager mDLManager;
    private final VersionManager mVersionManager;
    private final GetLocation mGetLocation;

    private Subscription mUidSub;
    private Subscription mUserInfoSub;

    @Inject
    public MainPresenter(DataManager dataManager, DLManager dlManager,ConfigManager configManager,
                         AdManager adManager,
                         GetLocation getLocation, VersionManager versionManager) {
        mDataManager = dataManager;
        mDLManager = dlManager;
        mConfigManager = configManager;
        mAdManager = adManager;
        mGetLocation = getLocation;
        mVersionManager = versionManager;
    }

    @Override
    public void detachView() {
        super.detachView();

        RxUtil.unsubscribe(mUidSub);
        RxUtil.unsubscribe(mUserInfoSub);
    }

    /**************************微信登录回调***********************/
    //获取小程序的用户id
    public void getSmallUid(String token){
        checkViewAttached();
        RxUtil.unsubscribe(mUidSub);
        mUidSub = mDataManager.getUid(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UidBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showWxLoginMsg("未获取到用户信息");
                        }
                    }

                    @Override
                    public void onNext(UidBean uidBean) {
                        if (getMvpView()!=null){
                            if (uidBean.getResult() == 200){
                                getUserInfo(uidBean.getUid());
                            }else {
                                getMvpView().showWxLoginMsg("未获取到用户信息");
                            }
                        }
                    }
                });
    }

    //获取用户的信息
    public void getUserInfo(int uid){
        /*checkViewAttached();
        RxUtil.unsubscribe(mUserInfoSub);
        mUserInfoSub = mDataManager.getUserInfo(uid,uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showWxLoginMsg("获取用户信息失败");
                        }
                    }

                    @Override
                    public void onNext(UserData useresult) {
                        if (getMvpView()!=null){

                            if (useresult!=null){
                                User theuser = new User();
                                theuser.setUid(uid);
                                theuser.setUsername(useresult.username);
                                theuser.setNickname(useresult.nickname);
                                theuser.setAmount(useresult.amount);
                                theuser.setEmail(useresult.email);
                                theuser.setPhone(useresult.mobile);
                                theuser.setImgSrc(useresult.middle_url);
                                theuser.setVipStatus(Integer.parseInt(useresult.vipStatus));
                                theuser.setExpireTime(TimeUtil.tansDateFrom1970(useresult.expireTime * 1000));
                                mAccountManager.setUser(theuser);
                                mAccountManager.saveUser(theuser);

                                getMvpView().showWxLoginResult();
                            }else {
                                getMvpView().showWxLoginMsg("未获取到用户信息，请使用其他方式登陆");
                            }
                        }
                    }
                });*/

        UserInfoManager.getInstance().getRemoteUserInfo(uid, new UserinfoCallbackListener() {
            @Override
            public void onSuccess() {
                getMvpView().showWxLoginResult();
            }

            @Override
            public void onFail(String errorMsg) {
                getMvpView().showWxLoginMsg("未获取到用户信息，请使用其他方式登陆");
            }
        });
    }
}
