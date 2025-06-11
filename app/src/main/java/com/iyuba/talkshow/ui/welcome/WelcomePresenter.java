package com.iyuba.talkshow.ui.welcome;

import android.content.Context;
import android.util.Log;

import com.iyuba.iyubamovies.manager.IMoviesConstant;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.module.commonvar.CommonVars;
import com.iyuba.talkshow.BuildConfig;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.manager.AdManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.AppCheckResponse;
import com.iyuba.talkshow.data.model.result.GetAdData1;
import com.iyuba.talkshow.data.remote.AdService;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.GetLocation;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.StorageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

import javax.inject.Inject;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class WelcomePresenter extends BasePresenter<WelcomeMvpView> {
    private final DataManager mDataManager;
    private final ConfigManager mConfigManager;
    private final GetLocation mGetLocation;
    private final AdManager mAdManager;
    private Subscription mGetWelcomeSub;

    @Inject
    public WelcomePresenter(DataManager mDataManager,
                            ConfigManager configManager, GetLocation getLocation,
                            AdManager adManager) {
        this.mDataManager = mDataManager;
        this.mConfigManager = configManager;
        this.mGetLocation = getLocation;
        this.mAdManager = adManager;
    }

    @Override
    public void attachView(WelcomeMvpView mvpView) {
        super.attachView(mvpView);
//        mPromotionHelper = new PromotionChannelHelper((Activity) getMvpView());
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mGetWelcomeSub);

        RxUtil.unsubscribe(mCheckSub);
        RxUtil.unsubscribe(mVerifyMocSub);
        RxUtil.unsubscribe(mVerifyLessonSub);
        RxUtil.unsubscribe(mVerifyVideoSub);
    }

    public void checkDbUpgrade() {
        TalkShowApplication.getSubHandler().post(() -> {
            mDataManager.checkDbUpgrade();
        });
    }

    public void requestQQGroupNumber() {
//        BrandUtil.requestQQGroupNumber(mDataManager.getPreferencesHelper(), mAccountManager.getUid(), App.APP_ID);
        //去除原有的域名更新操作
        /*String url = "https://raw.githubusercontent.com/OldManLi-1996/OldManLi-1996/main/" + App.APP_ID;
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    Log.e("WelcomeActivity", "github response = " + response);
                    DomainBean bean = new Gson().fromJson(response, DomainBean.class);
                    if (!TextUtils.isEmpty(bean.domain) && !bean.domain.equalsIgnoreCase(mConfigManager.getDomain())) {
                        mConfigManager.setDomain(bean.domain);
                    }
                    if (!TextUtils.isEmpty(bean.short1) && !bean.short1.equalsIgnoreCase(mConfigManager.getDomainShort())) {
                        mConfigManager.setDomainShort(bean.short1);
                        CommonVars.domain = bean.short1;
                        Constant.Web.WEB_SUFFIX = bean.short1 + "/";
                        Log.e("WelcomeActivity", "github response update domain = " + Constant.Web.WEB_SUFFIX);
                        // set new domains
                        IMoviesConstant.VIDEO_PLAY_URL="http://tv." + CommonVars.domain + "/series/";
                        IMoviesConstant.SHARE_PLAY_URL="http://m." + CommonVars.domain + "/news.html?type=series&id=";
                        setDomainShort();
                    }
                    if (!TextUtils.isEmpty(bean.short2) && !bean.short2.equalsIgnoreCase(mConfigManager.getDomainLong())) {
                        mConfigManager.setDomainLong(bean.short2);
                        CommonVars.domainLong = bean.short2;
                        Constant.Web.WEB2_SUFFIX = bean.short2 + "/";
                        Log.e("WelcomeActivity", "github response update domainLong = " + CommonVars.domainLong);
                        setDomainLong();
                    }
                } catch (Exception e) {
                    if (e != null) {
                        Log.e("WelcomeActivity", "github response Exception = " + e.getMessage());
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (e != null) {
                    Log.e("WelcomeActivity", "github onError = " + e.getMessage());
                }
            }
        });*/
    }
    class DomainBean {
        public String domain;
        public String short1;
        public String short2;
    }
    private void setDomainShort() {
        // set domain for update
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_STATIC, Constant.HTTP_STATIC + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_STATIC2, Constant.HTTP_STATIC2 + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_AI, Constant.HTTP_AI + CommonVars.domain.replace("/","")+":9001/");
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAINS_AI, Constant.HTTPS_AI + CommonVars.domain.replace("/","")+":9001/");
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_USERSPEECH, Constant.HTTP_USERSPEECH + CommonVars.domain.replace("/","")+":9001/");
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_WORD, Constant.HTTP_WORD + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_M, Constant.HTTP_M + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_STATICVIP, Constant.HTTP_STATICVIP + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_APP,  Constant.HTTP_APP + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_APPS, Constant.HTTP_APPS + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_VOA, Constant.HTTP_VOA + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_DEV, Constant.HTTP_DEV + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_CMS, Constant.HTTP_CMS + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_API,  Constant.HTTP_API + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_DAXUE, Constant.HTTP_DAXUE + CommonVars.domain);
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_VIP, Constant.HTTP_VIP + CommonVars.domain);
        // set the new domain for web
        Constant.Web.EVALUATE_URL_CORRECT = "http://iuserspeech." + Constant.Web.WEB_SUFFIX.replace("/","") + ":9001/test/ai/";
        Constant.Web.WordBASEURL = "http://word." + Constant.Web.WEB_SUFFIX + "words/";
        Constant.Web.VIP_VIDEO_PREFIX = "http://staticvip." + Constant.Web.WEB_SUFFIX + "video/voa/";
        Constant.Web.VIDEO_PREFIX = "http://staticvip." + Constant.Web.WEB_SUFFIX + "video/voa/";
        Constant.Web.VIDEO_PREFIX_NEW = "http://m." + Constant.Web.WEB_SUFFIX + "voaS/playPY.jsp?apptype=";
        Constant.Web.VIP_SOUND_PREFIX = "http://staticvip." + Constant.Web.WEB_SUFFIX + "sounds/voa/";
        Constant.Web.SOUND_PREFIX = "http://staticvip." + Constant.Web.WEB_SUFFIX + "sounds/voa/";
        // set the new domain for url
        Constant.Url.PROTOCOL_BBC_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocolpri.jsp?company=6&apptype=";
        Constant.Url.PROTOCOL_BBC_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocoluse666.jsp?company=6&apptype=";
        Constant.Url.PROTOCOL_NET_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocolpri.jsp?company=7&apptype=";
        Constant.Url.PROTOCOL_NET_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocoluse666.jsp?company=7&apptype=";
        Constant.Url.PROTOCOL_BJIYB_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocolpri.jsp?company=1&apptype=";
        Constant.Url.PROTOCOL_BJIYB_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocoluse666.jsp?company=1&apptype=";
        Constant.Url.PROTOCOL_IYUYAN_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpri.jsp?company=3&apptype=";
        Constant.Url.PROTOCOL_IYUYAN_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocoluse666.jsp?company=3&apptype=";
        Constant.Url.APP_ICON_URL = "http://app."+ Constant.Web.WEB_SUFFIX+"android/images/Englishtalkshow/Englishtalkshow.png";
        Constant.Url.APP_SHARE_URL = "http://voa."+ Constant.Web.WEB_SUFFIX+ "voa/shareApp.jsp?appType=";
        Constant.Url.WEB_PAY = "http://app."+ Constant.Web.WEB_SUFFIX+"wap/servlet/paychannellist?";
        Constant.Url.AD_PIC = "http://dev."+ Constant.Web.WEB_SUFFIX+ "";
        Constant.Url.MORE_APP = "http://app."+ Constant.Web.WEB_SUFFIX+"android";
        Constant.Url.COMMENT_VOICE_BASE = "http://voa."+ Constant.Web.WEB_SUFFIX+"voa/";
        Constant.Url.VOA_IMG_BASE = "http://staticvip."+ Constant.Web.WEB_SUFFIX+"images/voa/";
        Constant.Url.SHUOSHUO_PREFIX = "http://staticvip."+ Constant.Web.WEB_SUFFIX+"video/voa/";
        Constant.Url.NEW_DUBBING_PREFIX = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/";
        Constant.Url.MY_DUBBING_PREFIX = "http://voa."+ Constant.Web.WEB_SUFFIX+"voa/talkShowShare.jsp?shuoshuoId=";

        App.Url.APP_ICON_URL = "http://app."+ Constant.Web.WEB_SUFFIX+"android/images/Englishtalkshow/Englishtalkshow.png";
        App.Url.USAGE_URL = Constant.Url.PROTOCOL_BJIYB_USAGE;
        App.Url.PROTOCOL_URL = Constant.Url.PROTOCOL_BJIYB_PRIVACY;
        App.Url.SHARE_APP_URL = Constant.Url.APP_SHARE_URL + App.APP_ID;
    }
    private void setDomainLong() {
        // set domain long for update
        RetrofitUrlManager.getInstance().putDomain(Constant.DOMAIN_LONG_API, Constant.HTTP_LONG_API + CommonVars.domainLong);
        // set the new domain long for url
        Constant.Url.EMAIL_REGILTER = "http://api."+ Constant.Web.WEB2_SUFFIX+ "v2/api.iyuba?protocol=11002&app=meiyu";
        Constant.Url.PHONE_REGISTER = "http://api."+ Constant.Web.WEB2_SUFFIX+ "v2/api.iyuba?platform=android&app=meiyu&protocol=11002";
        Constant.Url.USER_IMAGE = "http://api."+ Constant.Web.WEB2_SUFFIX+ "v2/api.iyuba?";
    }
    public void setDefaultWeb() {
        if (!CommonVars.domain.equalsIgnoreCase(mConfigManager.getDomainShort())) {
            CommonVars.domain = mConfigManager.getDomainShort();
            // set new domains
            IMoviesConstant.VIDEO_PLAY_URL="http://tv." + CommonVars.domain + "/series/";
            IMoviesConstant.SHARE_PLAY_URL="http://m." + CommonVars.domain + "/news.html?type=series&id=";
        }
        if (Constant.Web.WEB_SUFFIX.equalsIgnoreCase(mConfigManager.getDomainShort() + "/")) {
            Log.e("WelcomeActivity", "setDefaultWeb no need update domain as no change. ");
        } else {
            Constant.Web.WEB_SUFFIX = mConfigManager.getDomainShort() + "/";
            Log.e("WelcomeActivity", "setDefaultWeb update web domain = " + Constant.Web.WEB_SUFFIX);
            setDomainShort();
        }
        if (!CommonVars.domainLong.equalsIgnoreCase(mConfigManager.getDomainLong())) {
            CommonVars.domainLong = mConfigManager.getDomainLong();
            Log.e("WelcomeActivity", "setDefaultWeb update CommonVars.domainLong = " + CommonVars.domainLong);
        }
        if (Constant.Web.WEB2_SUFFIX.equalsIgnoreCase(mConfigManager.getDomainLong() + "/")) {
            Log.e("WelcomeActivity", "setDefaultWeb no need for domain long as no change. ");
        } else {
            Constant.Web.WEB2_SUFFIX = mConfigManager.getDomainLong() + "/";
            Log.e("WelcomeActivity", "setDefaultWeb update web2 domain = " + Constant.Web.WEB2_SUFFIX);
            setDomainLong();
        }
    }

    /****************审核接口********************/
    //审核
    private Subscription mCheckSub;
    //获取人教版的审核状态
    public void verifyRenPep(String channel){
        int verifyId = ConfigData.getRenLimitChannelId(channel);
        checkViewAttached();
        RxUtil.unsubscribe(mCheckSub);
        mCheckSub = mDataManager.getAppCheckStatus(verifyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<AppCheckResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AbilityControlManager.getInstance().setLimitPep(false);
                    }

                    @Override
                    public void onNext(AppCheckResponse response) {
                        if (response.getResult().equals("0")){
                            AbilityControlManager.getInstance().setLimitPep(false);
                        }else {
                            AbilityControlManager.getInstance().setLimitPep(true);
                        }
                    }
                });
    }

    /******************微课审核*******************/
    //微课
    private Subscription mVerifyMocSub;
    //微课是否需要限制
    public void verifyMoc(String channel){
        int verifyMocId = ConfigData.getMocLimitChannelId(channel);

        checkViewAttached();
        RxUtil.unsubscribe(mVerifyMocSub);
        mVerifyMocSub = mDataManager.getAppCheckStatus(verifyMocId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppCheckResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AbilityControlManager.getInstance().setLimitMoc(false);
                    }

                    @Override
                    public void onNext(AppCheckResponse response) {
                        if (response.getResult().equals("0")){
                            AbilityControlManager.getInstance().setLimitMoc(false);
                        }else {
                            AbilityControlManager.getInstance().setLimitMoc(true);
                        }
                    }
                });
    }

    /******************视频审核*******************/
    //视频
    private Subscription mVerifyVideoSub;
    //视频是否需要限制
    public void verifyVideo(String channel){
        int verifyVideoId = ConfigData.getVideoLimitChannelId(channel);

        checkViewAttached();
        RxUtil.unsubscribe(mVerifyVideoSub);
        mVerifyVideoSub = mDataManager.getAppCheckStatus(verifyVideoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppCheckResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AbilityControlManager.getInstance().setLimitVideo(false);
                    }

                    @Override
                    public void onNext(AppCheckResponse response) {
                        if (response.getResult().equals("0")){
                            AbilityControlManager.getInstance().setLimitVideo(false);
                        }else {
                            AbilityControlManager.getInstance().setLimitVideo(true);
                        }
                    }
                });
    }

    /******************课程审核*******************/
    //课程
    private Subscription mVerifyLessonSub;
    //课程是否需要限制
    public void verifyLesson(String channel){
        int verifyLessonId = ConfigData.getLessonLimitChannelId(channel);

        checkViewAttached();
        RxUtil.unsubscribe(mVerifyLessonSub);
        mVerifyLessonSub = mDataManager.getAppCheckStatus(verifyLessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppCheckResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AbilityControlManager.getInstance().setLimitLesson(false);
                    }

                    @Override
                    public void onNext(AppCheckResponse response) {
                        if (response.getResult().equals("0")){
                            AbilityControlManager.getInstance().setLimitLesson(false);
                        }else {
                            AbilityControlManager.getInstance().setLimitLesson(true);
                        }
                    }
                });
    }
}
