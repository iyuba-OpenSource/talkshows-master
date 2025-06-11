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
    /***********人教版审核*********/
    //是否进行人教版审核
    public static boolean renVerifyCheck = true;
    //人教版审核的渠道id
    public static int getRenLimitChannelId(String channel){
        switch (channel){
            case "huawei":
                return 6311;
            case "xiaomi":
                return 6312;
            case "oppo":
                return 6313;
            case "vivo":
                return 6314;
        }
        return 6310;
    }

    /************微课审核**********/
    //是否进行微课控制
    public static boolean mocVerifyCheck = true;
    //微课审核的渠道id
    public static int getMocLimitChannelId(String channel){
        switch (channel){
            case "huawei":
                return 6301;
            case "xiaomi":
                return 6302;
            case "oppo":
                return 6303;
            case "vivo":
                return 6304;
        }
        return 6300;
    }

    /************视频审核***********/
    //是否进行视频控制
    public static boolean videoVerifyCheck = false;
    //视频审核的渠道id
    public static int getVideoLimitChannelId(String channel){
        switch (channel){
            case "huawei":
                return 6321;
            case "xiaomi":
                return 6322;
            case "oppo":
                return 6323;
            case "vivo":
                return 6324;
        }
        return 6320;
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
    public static boolean lessonVerifyCheck = true;
    //内容审核的渠道id
    public static int getLessonLimitChannelId(String channel){
        switch (channel){
            case "huawei":
                return 6341;
            case "xiaomi":
                return 6342;
            case "oppo":
                return 6343;
            case "vivo":
                return 6344;
            case "honor":
                return 6345;
        }
        return 6340;
    }

    /*****************************广告配置**************************/
    //有道广告
    public static final String YOUDAO_AD_SPLASH_CODE = "a710131df1638d888ff85698f0203b46";//开屏
    public static final String YOUDAO_AD_STEAM_CODE = "3438bae206978fec8995b280c49dae1e";//信息流
    public static final String YOUDAO_AD_BANNER_CODE = "230d59b7c0a808d01b7041c2d127da95";//banner

    //爱语吧广告
    //com.iyuba.talkshow.pappa 开屏 0075
    //com.iyuba.talkshow.pappa banner 0076
    public static final String IYUBA_AD_SPLASH_CODE = "0075";
    public static final String IYUBA_AD_BANNER_CODE = "0076";

    /*****************************登录配置**************************/
    //登录类型
    public static final String loginType = LoginType.loginByVerify;

    /*****************************mob配置**************************/
    //mob的key
    public static final String mob_key = "351d58f090f34";
    //mob的secret
    public static final String mob_secret = "85c0e79a3ccaaabd74472607c66d9b80";

    /******************************友盟配置*************************/
    public static final String umeng_key = "5863823045297d2ed3000dde";

    /******************************微信配置*************************/
    //微信appid--用于微信分享、微信支付、小程序登录等
    public static final String wx_key = "wx02b3b557a8bb975c";
    //小程序原始id
    public static final String wx_small_name = "gh_532d09789954";

    /******************************单词配置**************************/
    //是否开启单词显示
    public static final boolean showWord = true;
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
        return true;
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
    /********************************临时配置**************************/
    //oppo渠道要求[暂时屏蔽小猪佩奇和加菲猫内容]
    public static boolean isOppoCopyrightLimit(Context context){
//        String channel = ChannelReaderUtil.getChannel(context);
//        if (channel.equals("oppo")){
//            return true;
//        }
        return false;
    }

    //屏蔽中小学英语的内容(oppo渠道)
    public static boolean isBlockJuniorEnglish(Context context){
//        String channel = ChannelReaderUtil.getChannel(context);
//        if (channel.equals("oppo")){
//            return true;
//        }

        return false;
    }
}
