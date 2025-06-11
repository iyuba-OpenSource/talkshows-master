package com.iyuba.talkshow.ui.user.me;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/24/024.
 */

@ConfigPersistent
public class MePresenter extends BasePresenter<MeMvpView> {
    private DataManager mDataManager;
    private ConfigManager mConfigManager;

    @Inject
    public MePresenter(ConfigManager configManager, DataManager dataManager) {
        this.mConfigManager = configManager;
        this.mDataManager = dataManager;
    }

    public String getUserImageUrl() {
        if (UserInfoManager.getInstance().isLogin()){
            return Constant.Url.getMiddleUserImageUrl(
                    UserInfoManager.getInstance().getUserId(),
                    mConfigManager.getPhotoTimestamp());
        }
        return null;
    }



    public void loginOut() {
        //删除用户信息
        UserInfoManager.getInstance().clearUserInfo();

        TalkShowApplication.getSubHandler().post(new Runnable() {
            @Override
            public void run() {
                List<Voa> getVoa1 = mDataManager.getVoa4Category("309");
                if ((getVoa1 != null) && (getVoa1.size() > 1)) {
                    mDataManager.deleteVoa4Category("309");
                }
                List<SeriesData> getVoa3 = mDataManager.getSeries4Category("309");
                if ((getVoa3 != null) && (getVoa3.size() > 1)) {
                    mDataManager.deleteSeries4Category("309");
                }
            }
        });
    }

    /*註銷用戶*/
    /*public void clearUser() {
        mDataManager.clearUser(UserInfoManager.getInstance().getUserName(), UserInfoManager.getInstance().getLoginPassword())
                .compose(RxUtil.io2main())
                .subscribe(new Subscriber<ClearUserResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showToastShort("注销失败 网络异常！");
                    }

                    @Override
                    public void onNext(ClearUserResponse response) {
                        mAccountManager.loginOut();
                        getMvpView().showToastShort("用户已经成功注销！");
                    }
                });
    }*/
}
