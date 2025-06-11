package com.iyuba.talkshow.ui.about;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.iyuba.lib_user.event.UserRefreshEvent;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.remote.ClearUserResponse;
import com.iyuba.talkshow.data.remote.UserService;
import com.iyuba.talkshow.event.LoginOutEvent;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.FailOpera;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.StorageUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import cn.aigestudio.downloader.bizs.DLManager;
import cn.aigestudio.downloader.interfaces.IDListener;
import rx.Subscriber;
import rx.Subscription;

@ConfigPersistent
public class AboutPresenter extends BasePresenter<AboutMvpView> {

    private final DLManager mDLManager;
    private DataManager mDataManager;
    private Subscription mLoginSub;

    @Inject
    public AboutPresenter(DataManager dataManager, DLManager dlManager) {
        this.mDataManager = dataManager;
        this.mDLManager = dlManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mLoginSub);
    }
    /*註銷用戶*/
    public void clearUser(String password) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoginSub);
        mLoginSub = mDataManager.clearUser(UserInfoManager.getInstance().getUserName(), password)
                .compose(RxUtil.io2main())
                .subscribe(new Subscriber<ClearUserResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView() != null) {
                            if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                                getMvpView().showToast(R.string.please_check_network);
                            } else {
                                getMvpView().showToastShort("注销失败！请稍后再试。");
                            }
                        }
                    }

                    @Override
                    public void onNext(ClearUserResponse response) {
                        if (response == null) {
                            Log.e("AboutPresenter", "ClearUserResponse is null.");
                            getMvpView().showToastShort("注销失败！请稍后再试。");
                            return;
                        }
                        Log.e("AboutPresenter", "ClearUserResponse:getResult " + response.getResult());
                        if ("101".equals(response.getResult())) {
                            getMvpView().showToastShort("用户已经注销成功！");

                            //清除用户数据
                            UserInfoManager.getInstance().clearUserInfo();
                            EventBus.getDefault().post(new LoginOutEvent());

                            TalkShowApplication.getSubHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    List<Voa> getVoa1 = mDataManager.getVoa4Category("309");
                                    if ((getVoa1 != null) && (getVoa1.size() > 1)) {
                                        mDataManager.deleteVoa4Category("309");
                                    }
                                }
                            });
                        } else if (response.getResult().equals(UserService.Login.Result.Code.NOT_MATCHING + "")) {
                            getMvpView().showToastShort(UserService.Login.Result.Message.NOT_MATCHING);
                        } else {
                            getMvpView().showToastShort("注销失败！请稍后再试。");
                        }
                    }
                });
    }

    void downloadApk(String versionCode, String appUrl) {
        String filename = App.APP_NAME_EN + App.UNDERLINE + versionCode + App.APK_SUFFIX;
        String dir = StorageUtil.getAppDir((Context) getMvpView());
        File file = new File(dir, filename);
        if (file.exists()) {
            file.delete();
        }
        mDLManager.dlStart(appUrl, dir, filename, listener);
    }

    private IDListener listener = new IDListener() {

        @Override
        public void onStop(int progress) {
        }

        @Override
        public void onStart(String fileName, String realUrl, final int fileLength) {
            if (getMvpView() != null) {
                ((BaseActivity) getMvpView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getMvpView() != null) {
                            getMvpView().setDownloadMaxProgress(fileLength);
                        }
                    }
                });
            }
        }

        @Override
        public void onProgress(final int progress) {
            if (getMvpView() != null) {

                ((BaseActivity) getMvpView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getMvpView() != null) {
                            getMvpView().setDownloadProgress(progress);
                        }
                    }

                });
            }
        }

        @Override
        public void onPrepare() {
        }

        @Override
        public void onFinish(final File file) {
            if (getMvpView() != null) {

                ((BaseActivity) getMvpView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMvpView().setDownloadProgress((int) file.length());
                        getMvpView().setProgressVisibility(View.INVISIBLE);
                        //优化安装操作
                        FailOpera.installApk((Context) getMvpView(), file);
                    }
                });
            }
        }

        @Override
        public void onError(int status, String error) {
            if (getMvpView() != null) {
                if (!NetStateUtil.isConnected((Context) getMvpView())) {
                    getMvpView().showToast(R.string.please_check_network);
                } else {
                    getMvpView().showToast(R.string.request_fail);
                }
            }
        }
    };
}
