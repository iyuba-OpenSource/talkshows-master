package com.iyuba.talkshow.data.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.result.AppUpdateDataBody;
import com.iyuba.talkshow.data.model.result.AppUpdateResponse;
import com.iyuba.talkshow.data.remote.VersionService;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VersionManager {

    private final DataManager mDataManager;

    public static int VERSION_CODE = 1;
    public static String VERSION_NAME = "1.0";

    static {
        VERSION_CODE = getCurrentVersionCode(TalkShowApplication.getInstance());
        VERSION_NAME = getCurrentVersionName(TalkShowApplication.getInstance());
    }

    @Inject
    VersionManager(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    private static String getCurrentVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            return context.getString(R.string.version);
        }
    }

    private static int getCurrentVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public void checkVersion(final AppUpdateCallBack callBack) {
        mDataManager.checkVersion(VERSION_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppUpdateResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AppUpdateResponse response) {
                        if(callBack != null) {
                            if(VersionService.CheckVersion.Result.Code.LATEST_VERSION.equals(response.getStatus())) {
                                callBack.appUpdateFailed();
                            } else {
                                AppUpdateDataBody data = response.getData();
                                callBack.appUpdateSave(data.getVersion(), data.getUrl());
                            }
                        }
                    }
                });
    }

    public interface AppUpdateCallBack {
        void appUpdateSave(String versionCode, String appUrl);

        void appUpdateFailed();
    }
}
