package com.iyuba.talkshow.ui.main.drawer;

import com.iyuba.talkshow.Constant;
import com.iyuba.talkshow.data.manager.AccountManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.User;
import com.iyuba.talkshow.injection.PerFragment;
import com.iyuba.talkshow.ui.base.BasePresenter;

import javax.inject.Inject;

import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.http.Http;
import com.iyuba.talkshow.http.HttpCallback;
import com.iyuba.module.commonvar.CommonVars;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Call;
import com.google.gson.Gson;
import android.text.TextUtils;
import com.iyuba.iyubamovies.manager.IMoviesConstant;
import com.iyuba.headlinelibrary.IHeadline;

/**
 * Created by Administrator on 2016/12/24/024.
 */

@PerFragment
public class SlidingMenuPresenter extends BasePresenter<SlidingMenuMvpView> {
    private final AccountManager mAccountManager;
    private final ConfigManager mConfigManager;

    @Inject
    public SlidingMenuPresenter(AccountManager mAccountManager, ConfigManager configManager) {
        this.mAccountManager = mAccountManager;
        this.mConfigManager = configManager;
    }

    public boolean checkLogin() {
        return mAccountManager.checkLogin();
    }

    public User getUser() {
        return mAccountManager.getUser();
    }

    public String getUserImageUrl() {
        return Constant.Url.getMiddleUserImageUrl(
                getUser().getUid(),
                mConfigManager.getPhotoTimestamp());
    }

    public boolean isVip() {
        return mAccountManager.isVip();
    }

    //新-更新域名
    public void requestHostUpdate(){
        String url = "http://111.198.52.105:8085/api/getDomain.jsp?appId="+ App.APP_ID+"&short1="+mConfigManager.getDomainShort()+"&short2="+mConfigManager.getDomainLong();
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    HostUpdate hostUpdate = new Gson().fromJson(response, HostUpdate.class);
                    if ((hostUpdate.result == 201 && hostUpdate.updateflg==1)
                            ||(hostUpdate.result == 200 && hostUpdate.updateflg == 0)){
                        if (!TextUtils.isEmpty(hostUpdate.short1) && !hostUpdate.short1.equalsIgnoreCase(mConfigManager.getDomainShort())){
                            mConfigManager.setDomainShort(hostUpdate.short1);
                            CommonVars.domain = hostUpdate.short1;
                            Constant.Web.WEB_SUFFIX = hostUpdate.short1 + "/";
                            // set new domains
                            IMoviesConstant.VIDEO_PLAY_URL="http://tv." + CommonVars.domain + "/series/";
                            IMoviesConstant.SHARE_PLAY_URL="http://m." + CommonVars.domain + "/news.html?type=series&id=";
                            setDomainShort();
                        }

                        if (!TextUtils.isEmpty(hostUpdate.short2) && !hostUpdate.short2.equalsIgnoreCase(mConfigManager.getDomainLong())){
                            mConfigManager.setDomainLong(hostUpdate.short2);
                            CommonVars.domainLong = hostUpdate.short2;
                            Constant.Web.WEB2_SUFFIX = hostUpdate.short2 + "/";
                            setDomainLong();
                        }

                        if (getMvpView()!=null){
                            getMvpView().showToast("服务更新成功");
                            getMvpView().hideLoadingDialog();

                            //更新共通模块
                            IHeadline.resetMseUrl();
                        }
                    }else {
                        if (getMvpView()!=null){
                            getMvpView().showToast("服务更新失败--"+hostUpdate.result+"--"+hostUpdate.updateflg);
                            getMvpView().hideLoadingDialog();
                        }
                    }
                }catch (Exception e){
                    if (e!=null){
                        if (getMvpView()!=null){
                            getMvpView().showToast("服务更新失败");
                            getMvpView().hideLoadingDialog();
                        }
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (e!=null){
                    if (getMvpView()!=null){
                        getMvpView().showToast("服务更新失败");
                        getMvpView().showLoadingDialog();
                    }
                }
            }
        });
    }
    class HostUpdate{
        private int result;
        private int updateflg;
        private String short1;
        private String short2;
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
        Constant.Url.PROTOCOL_VIVO_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocolpri.jsp?company=1&apptype=";
        Constant.Url.PROTOCOL_VIVO_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+ ":9001/api/protocoluse666.jsp?company=1&apptype=";
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
//        App.Url.APP_ICON_URL = "http://www."+ Constant.Web.WEB_SUFFIX+ "app_pig.png";
//        App.Url.USAGE_URL = Constant.Url.PROTOCOL_NET_USAGE;
//        App.Url.PROTOCOL_URL = Constant.Url.PROTOCOL_NET_PRIVACY;
//        App.Url.SHARE_APP_URL = "http://www."+ Constant.Web.WEB_SUFFIX+ "more";
        App.Url.APP_ICON_URL = "http://app."+ Constant.Web.WEB_SUFFIX+"android/images/Englishtalkshow/Englishtalkshow.png";
        App.Url.USAGE_URL = Constant.Url.PROTOCOL_VIVO_USAGE;
        App.Url.PROTOCOL_URL = Constant.Url.PROTOCOL_VIVO_PRIVACY;
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
}
