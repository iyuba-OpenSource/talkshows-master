package com.iyuba.talkshow.constant;

import android.content.Context;

import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.wordtest.data.login.LoginType;
import com.tencent.vasdolly.helper.ChannelReaderUtil;

/**
 * @title: 新的配置文件
 * @date: 2023/8/29 16:16
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class ConfigData {

    /****************************审核配置***************************/
    /*********************人教版处理*******************/
    //是否需要校验审核
    public static final boolean renVerifyCheck = true;

    public static int getRenLimitChannelId(String channel){
        switch (channel){
            case "huawei":
                return 6111;
            case "xiaomi":
                return 6112;
            case "oppo":
                return 6113;
            case "vivo":
                return 6114;
        }
        return 6110;
    }

    /****微课显示控制******/
    //是否需要控制显示
    public static final boolean mocVerifyCheck = true;

    public static int getMocLimitChannelId(String channel){
        switch (channel){
            case "huawei":
                return 6101;
            case "xiaomi":
                return 6102;
            case "oppo":
                return 6103;
            case "vivo":
                return 6104;
        }
        return 6100;
    }

    /************视频审核***********/
    //是否进行视频控制
    public static boolean videoVerifyCheck = false;
    //视频审核的渠道id
    public static int getVideoLimitChannelId(String channel){
        return 0;
    }

    /***************小说审核***********/
    //是否进行小说控制
    public static boolean novelVerifyCheck = false;
    //小说审核的渠道id
    public static int getNovelLimitChannelId(String channel){
        return 0;
    }

    /***********内容审核*********/
    //是否进行内容审核(小猪英语)
    public static boolean lessonVerifyCheck = false;
    //内容审核的渠道id
    public static int getLessonLimitChannelId(String channel){
        return 0;
    }

    /*****************************广告配置**************************/
    //有道广告
    public static final String YOUDAO_AD_SPLASH_CODE = "a710131df1638d888ff85698f0203b46";//开屏
    public static final String YOUDAO_AD_STEAM_CODE = "3438bae206978fec8995b280c49dae1e";//信息流
    public static final String YOUDAO_AD_BANNER_CODE = "230d59b7c0a808d01b7041c2d127da95";//banner

    //爱语吧广告
    //com.iyuba.peiyin 开屏 0059
    //com.iyuba.peiyin banner 0060
    public static final String IYUBA_AD_SPLASH_CODE = "0059";
    public static final String IYUBA_AD_BANNER_CODE = "0060";

    /*****************************登录配置**************************/
    //登录类型
    public static final String loginType = LoginType.loginByVerify;

    /*****************************mob配置**************************/
    //mob的key
    public static final String mob_key = "2b2dd57c7f208";
    //mob的secret
    public static final String mob_secret = "fe3d196f187a3b114e53c8a768b24bf0";

    /******************************友盟配置*************************/
    public static final String umeng_key = "5863823045297d2ed3000dde";

    /******************************微信配置*************************/
    //微信appid--用于微信分享、微信支付、小程序登录等
    public static final String wx_key = "wxd1347f79e153a461";
    //小程序原始id
    public static final String wx_small_name = "gh_8510360c80f7";

    /******************************单词配置**************************/
    //是否开启单词显示
    public static boolean showWord(Context context){
        return true;
    }
    //默认显示的单词类型
    public static final String default_word_show_type = "primary";//junior-初中，primary-小学
    //默认显示的单词类型id
    public static final String default_word_series_id = "313";
    //默认显示的单词课程名称
    public static final String default_word_book_title = "1年级上(新起点)";
    //默认显示的单词书籍id
    public static final int default_word_book_id = 205;

    /********************************课程配置****************************/
    //是否开启课程显示
    public static boolean showLesson(Context context){
        return false;
    }
    //类型
    public static final String default_lesson_junior_type = TypeLibrary.BookType.junior_primary;
    //教材大类型
    public static final String default_lesson_junior_big_type = "人教版";
    //教材小类型id
    public static final String default_lesson_junior_small_type_id = "313";
    //书籍id
    public static final String default_lesson_junior_book_id = "205";
    //书籍名称
    public static final String default_lesson_junior_book_name = "1年级上(新起点)";

    /********************************临时配置**************************/
    //oppo渠道要求暂时屏蔽小猪佩奇和加菲猫内容
    public static boolean isOppoCopyrightLimit(Context context){
        /*String channel = ChannelReaderUtil.getChannel(context);
        if (channel.equals("oppo")){
            return true;
        }
        return false;*/

        return false;
    }

    //屏蔽中小学英语的内容(oppo渠道)
    public static boolean isBlockJuniorEnglish(Context context){
        /*String channel = ChannelReaderUtil.getChannel(context);
        if (channel.equals("")){
            return true;
        }*/

        return false;
    }
}
