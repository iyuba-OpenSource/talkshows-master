package com.iyuba.talkshow;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.webkit.WebView;

import androidx.multidex.MultiDex;

import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.data.local.db.HLDBManager;
import com.iyuba.imooclib.IMooc;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.manager.AppInfoManager;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.BasicFavor;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.privacy.IPrivacy;
import com.iyuba.module.privacy.PrivacyInfoHelper;
import com.iyuba.share.ShareExecutor;
import com.iyuba.share.mob.MobShareExecutor;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.injection.component.ApplicationComponent;
import com.iyuba.talkshow.injection.component.DaggerApplicationComponent;
import com.iyuba.talkshow.injection.module.ApplicationModule;
import com.iyuba.talkshow.ui.lil.util.OAIDNewHelper;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.util.TimeUtil;
import com.iyuba.widget.unipicker.IUniversityPicker;
import com.mob.MobSDK;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.commonsdk.UMConfigure;
import com.yd.saas.ydsdk.manager.YdConfig;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YoudaoSDK;

import java.util.Date;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import personal.iyuba.personalhomelibrary.PersonalHome;
import personal.iyuba.personalhomelibrary.PersonalType;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
import timber.log.Timber;

public class TalkShowApplication extends Application {

    private static TalkShowApplication INSTANCE;

    ApplicationComponent mApplicationComponent;
    private static HandlerThread mHandlerThread;
    private static Handler mSubHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        //加载oaid的方法
        OAIDNewHelper.loadLibrary();
        //设置共通的数据
        LibResUtil.getInstance().setApplication(this);
        AppInfoManager.getInstance().initAppInfo(App.APP_ID,App.APP_NAME_EN,R.drawable.ic_launcher,getResources().getString(R.string.app_name),App.Url.PROTOCOL_URL,App.Url.USAGE_URL,App.Url.CHILD_PROTOCOL_URL,ConfigData.mob_key,ConfigData.mob_secret);
        //配置webview内容
        webViewSetPath(INSTANCE);
        //配置友盟
        String channel = ChannelReaderUtil.getChannel(this);
        UMConfigure.preInit(INSTANCE, ConfigData.umeng_key, channel);

        //配置调试信息
        configDebugSession();
    }

    public static void initUMMob() {
        IMooc.init(INSTANCE, "" + App.APP_ID, Constant.LESSON_TYPE);

        mHandlerThread = new HandlerThread("TalkShowApplication");
        mHandlerThread.start();
        mSubHandler = new Handler(mHandlerThread.getLooper());
        mSubHandler.post(new Runnable() {
            @Override
            public void run() {
                PersonalHome.init(INSTANCE, String.valueOf(App.APP_ID), App.APP_NAME_EN);
                //aar包中的功能没有了，后期根据要求再进行添加
//                PersonalHome.setDeletePhoto(false);
//                PersonalHome.setUserComplain(App.APP_HUAWEI_COMPLAIN);
                PersonalHome.setEnableEditNickname(true);
                PersonalHome.setCategoryType(PersonalType.VOA);
                PersonalHome.setMainPath(MainActivity.class.getName());
                IUniversityPicker.init(INSTANCE);
                if (App.APP_SHARE_HIDE > 0) {
                    PersonalHome.setEnableShare(false);
                    IMooc.setEnableShare(false);
                } else {
                    PersonalHome.setEnableShare(true);
                    IMooc.setEnableShare(true);
                }
                MobShareExecutor mobShare = new MobShareExecutor();
                if (App.APP_SHARE_PART > 0) {
                    mobShare.setPlatformHidden(new String[]{QQ.NAME, SinaWeibo.NAME});
                } else {
                    mobShare.setPlatformHidden(new String[]{SinaWeibo.NAME});
                }
                ShareExecutor.getInstance().setRealExecutor(mobShare);
                if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
                    IPrivacy.init(INSTANCE, App.Url.USAGE_URL + App.APP_NAME_CH, App.Url.PROTOCOL_URL + App.APP_NAME_CH);
                } else {
                    IPrivacy.init(INSTANCE, Constant.Url.PROTOCOL_IYUYAN_USAGE + App.APP_NAME_CH, Constant.Url.PROTOCOL_IYUYAN_PRIVACY + App.APP_NAME_CH);
                }
                PrivacyInfoHelper.getInstance().putApproved(true);
                //友盟
                if (!App.APP_TENCENT_PRIVACY) {
                    initUMMob();
                }
            }
        });
        DLManager.init(INSTANCE, 8);
        IMoviesApp.init(INSTANCE);
        BGASwipeBackHelper.init(INSTANCE, null);

        String channel = ChannelReaderUtil.getChannel(INSTANCE);
        UMConfigure.submitPolicyGrantResult(INSTANCE, true);
        UMConfigure.init(INSTANCE, ConfigData.umeng_key, channel, UMConfigure.DEVICE_TYPE_PHONE, "");
        //mob分享
        MobSDK.submitPolicyGrantResult(true);
        MobSDK.init(INSTANCE, ConfigData.mob_key, ConfigData.mob_secret);

        //视频模块
        IHeadline.init(INSTANCE, String.valueOf(App.APP_ID), App.APP_NAME_EN);
        //开启配音功能
        IHeadline.setEnableSmallVideoTalk(true);
        BasicDLDBManager.init(INSTANCE);
        BasicFavorDBManager.init(INSTANCE);
        BasicFavor.init(INSTANCE, String.valueOf(App.APP_ID));
        HLDBManager.init(INSTANCE);

        //增加爱语吧广告sdk的初始化
        YdConfig.getInstance().init(INSTANCE, String.valueOf(App.APP_ID));

        //有道
        try {
            // TODO: 2025/1/13 因为有道sdk的问题，这里不使用手动处理的方式，但是上边的system需要保留
            //先初始化oaid
            /*OAIDHelper.getInstance().init(INSTANCE);
            //升级oaid版本（2024-3-20，每年需要更换证书）
            OAIDNewHelper oaidNewHelper = new OAIDNewHelper(new OAIDNewHelper.AppIdsUpdater() {
                @Override
                public void onIdData(boolean isSupported, boolean isLimited, String oaid, String vaid, String aaid) {
                    if (isSupported && !isLimited){
                        OAIDHelper.getInstance().setOAID(oaid);
                    }
                }
            },"msaoaidsec",App.oaid_pem);
            oaidNewHelper.getDeviceIds(TalkShowApplication.getContext(),true,false,false);*/

            //设置有道sdk的一些功能
            YouDaoAd.getYouDaoOptions().setAllowSdkInitMSAToGetOAID(false);
            YouDaoAd.getNativeDownloadOptions().setConfirmDialogEnabled(true);
            YouDaoAd.getYouDaoOptions().setAppListEnabled(false);
            YouDaoAd.getYouDaoOptions().setPositionEnabled(false);
            YouDaoAd.getYouDaoOptions().setSdkDownloadApkEnabled(true);
            YouDaoAd.getYouDaoOptions().setDeviceParamsEnabled(false);
            YouDaoAd.getYouDaoOptions().setWifiEnabled(false);
            YouDaoAd.getYouDaoOptions().setCanObtainAndroidId(false);
            YoudaoSDK.init(INSTANCE);

            //屏蔽广告（默认1天）[测试情况下，请使用固定的时间；打包时请切换为动态时间]
            Date compileDate = TimeUtil.GLOBAL_SDF.parse("2000-01-01 00:00:00");
//            Date compileDate = TimeUtil.GLOBAL_SDF.parse(BuildConfig.COMPILE_DATETIME);
            //这里根据当前情况，扩展成3天
            Date adBlockDate = new Date(compileDate.getTime() + 7 * 24 * 60 * 60 * 1000);
            AdBlocker.getInstance().setBlockStartDate(adBlockDate);
        } catch (Exception arg1) {
            arg1.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        YoudaoSDK.terminate();
        if (mSubHandler != null) {
            mSubHandler.removeCallbacks(null);
            mSubHandler = null;
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    public static Handler getSubHandler() {
        return mSubHandler;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return INSTANCE.getApplicationContext();
    }

    public static TalkShowApplication getContext(Context context) {
        return (TalkShowApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    /**
     * Needed to replace the component with a test specific one
     */
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public static TalkShowApplication get(Context context) {
        return (TalkShowApplication) context.getApplicationContext();
    }


    public static TalkShowApplication getInstance() {
        return INSTANCE;
    }

    public void webViewSetPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(context);
            if (!"com.iyuba.peiyin".equals(processName) || !"com.iyuba.talkshow".equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    public String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    //配置调试信息
    private void configDebugSession(){
        /*UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.setEncryptEnabled(!BuildConfig.DEBUG);

        IMooc.setDebug(BuildConfig.DEBUG);

        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        //增加sqlitestudio软件的远程调试处理
        if (BuildConfig.DEBUG) {
            SQLiteStudioService.instance().start(INSTANCE);
        }*/
    }
}
