package com.iyuba.talkshow.constant;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.ui.courses.courseChoose.TypeHelper;

/**
 * App
 */
public interface App {
    int APP_ID = 249;
    String APP_NAME_EN = "talkShow";
    String APP_NAME_CH = "英语配音秀";
    String SHARE_NAME_EN = "talkshow";
    String APP_NAME_WEIXIN = "英语配音秀";
//    String APP_NAME_PRIVACY = "用户隐私协议";
    String APP_NAME_PRIVACY = "隐私政策";
    String PLATFORM = "android";
    boolean APP_CHECK_UPGRADE = true;
    boolean APP_TENCENT_PRIVACY = true;
    boolean APP_HUAWEI_PRIVACY = true;
    boolean APP_HUAWEI_COMPLAIN = false;
    boolean APP_MINI_PRIVACY = false;
    boolean APP_TENCENT_MOOC = true;
    boolean APP_CHECK_PERMISSION = false;
    boolean APP_CHECK_AGREE = false;
    boolean APP_CHECK_VIVO = false;
    int APP_SHARE_WXMINIPROGRAM = 0;
    int APP_SHARE_PART = 0;
    int APP_SHARE_HIDE = 0;
    String APP_SAVE_DIR = "appUpdate";
    String UNDERLINE = "_";
    String UMENG_KEY = "5863823045297d2ed3000dde";

    String APK_SUFFIX = ".apk";
    String AD_FOLDER = "ad";

    String DEFAULT_WORDS = "middle";
    int COURSE_TYPE = TypeHelper.DEFAULT_TYPE;

    //是否判断为儿童类型
    class Apk {
        public static boolean isChild() {
            return App.APP_ID == APP_ID;
        }
    }

    class Url {
        public static String APP_ICON_URL = "https://file.static.xiaomi.net/download/AppStore/05a17d5b8bff84845122b25a6d4d7da6b36748c90" ;
        //爱语言
        public static String USAGE_URL = Constant.Url.PROTOCOL_IYUYAN_USAGE;
        public static String PROTOCOL_URL = Constant.Url.PROTOCOL_IYUYAN_PRIVACY;
        public static String CHILD_PROTOCOL_URL = Constant.Url.CHILD_PROTOCOL_IYUYAN_PRIVACY;
        public static String VIP_AGREEMENT_URL = Constant.Url.VIP_AGREEMENT_BJIYY_PRIVACY;

        public static String SHARE_APP_URL = Constant.Url.APP_SHARE_URL + APP_ID;
    }

    /****oaid升级****/
    //证书需要每年更换-2023年01月12日
    //oaid的证书名称
    String oaid_pem = "";
    //是否升级oaid
    boolean OAID_UPDATE = false;

    /****是否展示单词界面****/
    //单词界面界面展示开关
    boolean OPEN_WORD = true;
    //默认的数据
    int DEFAULT_BOOKID = 205;
    String DEFAULT_TITLE = "1年级上(新起点)";

    String DEFAULT_SERIESID = "313";
    String DEFAULT_SERIES_TITLE = "新起点";

    String DEFAULT_SOURCE = "人教版";

    String DEFAULT_LESSON_TYPE = "primary";//junior-初中，primary-小学
}
