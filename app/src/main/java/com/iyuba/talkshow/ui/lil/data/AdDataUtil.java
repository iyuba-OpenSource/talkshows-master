package com.iyuba.talkshow.ui.lil.data;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.R;

/**
 * 本地广告数据
 */
public class AdDataUtil {

    /***********开屏广告**********/
    //本地图片
    public static final int localSplashADPic = R.drawable.default_splash_ad;

    //本地链接图片
    public static String localSplashADPicUrl(){
        return "http://app."+ Constant.Web.WEB_SUFFIX+"dev/upload/1679379374314.jpg";
    }

    //本地跳转链接
    public static String localSplashADJumpUrl(){
        return "http://app."+Constant.Web.WEB_SUFFIX;
    }

    /**********banner广告**********/
    //本地图片
    public static final int localBannerADPic = R.drawable.default_banner_ad;

    //本地链接图片
    public static String localBannerADPicUrl(){
        return "http://app."+Constant.Web.WEB_SUFFIX+"/dev/upload/1679381438179.jpg";
    }

    //本地跳转链接
    public static String localBannerADJumpUrl(){
        return "http://app."+Constant.Web.WEB_SUFFIX;
    }

    /***********接口数据***********/
    //接口拼接图片
    public static String fixPicUrl(String picUrl){
        return "http://app."+Constant.Web.WEB_SUFFIX+"dev/"+picUrl;
    }

    //接口拼接链接
    public static String fixJumpUrl(String linkUrl){
        return linkUrl;
    }

    /**********广告类型************/
    public static final String AD_Youdao = "youdao";//有道
    public static final String AD_Web = "web";//web
    public static final String AD_Ads1 = "ads1";//倍孜
    public static final String AD_Ads2 = "ads2";//创见
    public static final String AD_Ads3 = "ads3";//头条穿山甲
    public static final String AD_Ads4 = "ads4";//广点通优量汇
    public static final String AD_Ads5 = "ads5";//快手
}
