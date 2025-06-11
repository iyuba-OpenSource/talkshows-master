package com.iyuba.lib_common.data;

import com.iyuba.lib_common.util.LibEncodeUtil;

public interface Constant {
    public String DOMAIN_STATIC = "iyu_static";
    public String HTTP_STATIC = "http://staticvip.";
    public String DOMAIN_STATIC2 = "iyu_static2";
    public String HTTP_STATIC2 = "http://static2.";
    public String DOMAIN_AI = "iyu_ai";
    public String HTTP_AI = "http://iuserspeech.";
    public String DOMAINS_AI = "iys_ai";
    public String HTTPS_AI = "https://iuserspeech.";
    public String DOMAIN_USERSPEECH = "iyu_userspeech";
    public String HTTP_USERSPEECH = "http://iuserspeech.";
    public String DOMAIN_WORD = "iyu_word";
    public String HTTP_WORD = "http://word.";
    public String DOMAIN_M = "iyu_m";
    public String HTTP_M = "http://m.";
    public String DOMAIN_STATICVIP = "iyu_staticvip";
    public String HTTP_STATICVIP = "http://staticvip.";
    public String DOMAIN_APP = "iyu_app";
    public String HTTP_APP = "http://app.";
    public String DOMAIN_APPS = "iyu_apps";
    public String HTTP_APPS = "http://apps.";
    public String DOMAIN_VOA = "iyu_voa";
    public String HTTP_VOA = "http://voa.";
    public String DOMAIN_DEV = "iyu_dev";
    public String HTTP_DEV = "http://dev.";
    public String DOMAIN_CMS = "iyu_cms";
    public String HTTP_CMS = "http://cms.";
    public String DOMAIN_API = "iyu_api";
    public String HTTP_API = "http://api.";
    public String DOMAIN_APIS = "iyu_apis";
    public String HTTP_APIS = "http://apis.";
    public String DOMAIN_DAXUE = "iyu_daxue";
    public String HTTP_DAXUE = "http://daxue.";
    public String DOMAIN_VIP = "iyu_vip";
    public String HTTP_VIP = "http://vip.";
    public String DOMAIN_LONG_API = "iyu_long_api";
    public String HTTP_LONG_API = "http://api.";
    public static String PRIMARY_TYPE = "309,313,314,315,319,320";
    public static String EVAL_TYPE = "talkshow";
    public static String LESSON_TYPE = "primary";
    public static int PRODUCT_ID = 26;
    int PRODUCT_WORDS = 25;
    String YOUDAO_VIDEO_ID = "a0ffd83740c31d1965ebc73aa5560482";
    public static boolean EvaluateCorrect = true;

    class Web{
        public static String WEB_SUFFIX = "iyuba.cn/";
        public static String WEB2_SUFFIX = "iyuba.com.cn/";
        public static String EVALUATE_URL_CORRECT = "http://iuserspeech." + WEB_SUFFIX.replace("/","") + ":9001/test/ai/";
        public static String WordBASEURL = "http://word." + WEB_SUFFIX + "words/";
        public static String VIP_VIDEO_PREFIX = "http://staticvip." + WEB_SUFFIX + "video/voa/";
        public static String VIDEO_PREFIX_NEW = "http://m." + WEB_SUFFIX + "voaS/playPY.jsp?apptype=";
        public static String VIP_SOUND_PREFIX = "http://staticvip." + WEB_SUFFIX + "sounds/voa/";
        public static String SOUND_PREFIX = "http://staticvip." + WEB_SUFFIX + "sounds/voa/";

        //vip
        public static String VIDEO_VIP_PREFIX = "http://staticvip."+WEB_SUFFIX+"video/voa/";
        //普通
        public static String VIDEO_PREFIX = "http://static0." + WEB_SUFFIX + "video/voa/";
    }

    class User {
        public static final String AVATAR_FILENAME = "avatar.jpg";
        static final String IYUBA_V2 = "iyubaV2";
        public static boolean isPreVerifyDone = false;
        public static boolean devMode = false;


        public static String getRegisterByEmailSign(String username, String password, String email) {
            StringBuilder sb = new StringBuilder();
            return LibEncodeUtil.md5(
                    sb.append(11002)
                            .append(username)
                            .append(LibEncodeUtil.md5(password))
                            .append(email)
                            .append(IYUBA_V2)
                            .toString());
        }

        public static String getRegisterByPhoneSign(String username, String password) {
            StringBuilder sb = new StringBuilder();
            return LibEncodeUtil.md5(
                    sb.append(11002)
                            .append(username)
                            .append(LibEncodeUtil.md5(password))
                            .append(IYUBA_V2)
                            .toString());
        }

        public static String getLoginSign(String username, String password) {
            StringBuilder sb = new StringBuilder();
            return LibEncodeUtil.md5(
                    sb.append(11001)
                            .append(username)
                            .append(LibEncodeUtil.md5(password))
                            .append(IYUBA_V2)
                            .toString());
        }


        public static String getClearUserSign(int protocol , String username, String password) {
            return LibEncodeUtil.md5(protocol + username + LibEncodeUtil.md5(password)+ "iyubaV2");
        }

        public static String getUserInfoSign(int uid) {
            return LibEncodeUtil.md5("20001" + uid + IYUBA_V2);
        }

        public static String getUserBasicInfoSign(int uid) {
            return LibEncodeUtil.md5(20002 + uid + IYUBA_V2);
        }

        public static String editUserBasicInfoSign(int uid) {
            return LibEncodeUtil.md5(20003 + uid + IYUBA_V2);
        }
    }

    interface Voa {
        int DEFAULT_UID = 0;
        String MP4_SUFFIX = ".mp4";
        String MP3_SUFFIX = ".mp3";
        String WAV_SUFFIX = ".wav";
        String AAC_SUFFIX = ".aac";
        String AMR_SUFFIX = ".amr";
        String JPG_SUFFIX = ".jpg";
        String ZIP_SUFFIX = ".zip";

        String SEPARATOR = "/";
        String TMP_PREFIX = "tmp";
        String SILENT_AAC_NAME = "silent.aac";
        String MERGE_AAC_NAME = "merge.aac";
        int SILENT_PIECE_TIME = 100;
        String COMMENT_VOICE_NAME = "comment_voice";
        String COMMENT_VOICE_SUFFIX = ".amr";
        int MAX_DIFFICULTY = 5;
        String FEEDBACK_END = "\n来自口语秀";
        String MERGE_MP3_NAME = "merge.mp3";
        String SIGN_PNG_NAME = "sign.png";
    }

    class Url {
        //万能链接（这里直接拼接公司全称和app名称即可）
        //http://www.bbe.net.cn/protocolpri.jsp?apptype=%E4%B9%A6%E8%99%AB%E5%AD%A6%E8%8B%B1%E8%AF%AD&company=%E5%B1%B1%E4%B8%9C%E7%88%B1%E8%AF%AD%E5%90%A7%E4%BF%A1%E6%81%AF%E7%A7%91%E6%8A%80%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8
        public static String PROTOCOL_ALL_PRIVACY = "http://www.bbe.net.cn/protocolpri.jsp?company=&apptype=";
        public static String PROTOCOL_ALL_USAGE = "http://www.bbe.net.cn/protocoluse.jsp?company=&apptype=";
        //济南万云天
        public static String PROTOCOL_BBC_PRIVACY = "https://www.ibbc.net.cn/protocolpri.jsp?company=1&apptype=";
        public static String PROTOCOL_BBC_USAGE = "https://www.ibbc.net.cn/protocoluse.jsp?company=1&apptype=";
        //日照国东
        public static String PROTOCOL_NET_PRIVACY = "http://www.bbe.net.cn/protocolpri.jsp?company=1&apptype=";
        public static String PROTOCOL_NET_USAGE = "http://www.bbe.net.cn/protocoluse.jsp?company=1&apptype=";
        //日照国东临时链接
        public static String PROTOCOL_NET_PRIVACY_TEMP = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpri.jsp?company=7&apptype=";
        public static String PROTOCOL_NET_USAGE_TEMP = "http://iuserspeech."+Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocoluse.jsp?company=7&apptype=";
        //北京爱语吧
        public static String VIP_AGREEMENT_BJIYB_PRIVACY = "http://iuserspeech."+ Web.WEB_SUFFIX.replace("/","")+ ":9001/api/vipServiceProtocol.jsp?company=1&type=app";
        public static String CHILD_PROTOCOL_BJIYB_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpriForChildren.jsp?company=1&apptype=";
        public static String PROTOCOL_BJIYB_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpri.jsp?company=1&apptype=";
        public static String PROTOCOL_BJIYB_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocoluse.jsp?company=1&apptype=";
        //爱语言
        public static String VIP_AGREEMENT_BJIYY_PRIVACY = "http://iuserspeech."+ Web.WEB_SUFFIX.replace("/","")+ ":9001/api/vipServiceProtocol.jsp?company=2&type=app";
        public static String CHILD_PROTOCOL_IYUYAN_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpriForChildren.jsp?company=3&apptype=";
        public static String PROTOCOL_IYUYAN_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpri.jsp?company=3&apptype=";
        public static String PROTOCOL_IYUYAN_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocoluse.jsp?company=3&apptype=";
        //珠穆朗玛
        public static String PROTOCOL_ZMLM_PRIVACY = "http://www.qomolama.com.cn/protocolpri.jsp?company=5&apptype=";
        public static String PROTOCOL_ZMLM_USAGE = "http://www.qomolama.com.cn/protocoluse.jsp?company=5&apptype=";
        //山东爱语吧
        public static String VIP_AGREEMENT_SDIYB_PRIVACY = "http://iuserspeech."+ Web.WEB_SUFFIX.replace("/","")+ ":9001/api/vipServiceProtocol.jsp?company=3&type=app";
        public static String CHILD_PROTOCOL_SDIYB_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpriForChildren.jsp?company=4&apptype=";
        public static String PROTOCOL_SDIYB_PRIVACY = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocolpri.jsp?company=4&apptype=";
        public static String PROTOCOL_SDIYB_USAGE = "http://iuserspeech."+ Constant.Web.WEB_SUFFIX.replace("/","")+":9001/api/protocoluse.jsp?company=4&apptype=";


        public static String APP_ICON_URL = "http://app."+ Constant.Web.WEB_SUFFIX+"android/images/Englishtalkshow/Englishtalkshow.png";
        // 分享应用时是url
        public static String APP_SHARE_URL = "http://voa."+ Web.WEB_SUFFIX+ "voa/shareApp.jsp?appType=";
        // 邮箱注册的url
        public static String EMAIL_REGILTER = "http://api."+ Web.WEB2_SUFFIX+ "v2/api.iyuba?protocol=11002&app=meiyu";
        // 手机注册的url
        public static String PHONE_REGISTER = "http://api."+ Web.WEB2_SUFFIX+ "v2/api.iyuba?platform=android&app=meiyu&protocol=11002";
        // 用户头像url
        public static String USER_IMAGE = "http://api."+ Web.WEB2_SUFFIX+ "v2/api.iyuba?";
        public static String WEB_PAY = "http://app."+ Web.WEB_SUFFIX+"wap/servlet/paychannellist?";
        public static String AD_PIC = "http://dev."+ Web.WEB_SUFFIX;
        public static String MORE_APP = "http://app."+ Web.WEB_SUFFIX+"android";
        public static String COMMENT_VOICE_BASE = "http://voa."+ Web.WEB_SUFFIX+"voa/";

        public static String VOA_IMG_BASE = "http://staticvip."+ Web.WEB_SUFFIX+"images/voa/";
        public static final String JPG_SUFFIX = ".jpg";

        public static String SHUOSHUO_PREFIX = "http://staticvip."+ Web.WEB_SUFFIX+"video/voa/";
//        public static String DUBBING_PREFIX = "http://static."+ Web.WEB_SUFFIX;
        public static String NEW_DUBBING_PREFIX = "http://iuserspeech."+ Web.WEB_SUFFIX.replace("/","")+":9001/";
        public static String MY_DUBBING_PREFIX = "http://voa."+ Web.WEB_SUFFIX+"voa/talkShowShare.jsp?shuoshuoId=";
//        public static final String MY_DUBBING_PREFIX_VOA = "http://m."+ Web.WEB_SUFFIX+ "voaS/play.jsp?id=";
        public static final String MP4_SUFFIX = ".mp4";
//        public static final String URL_HEADER = "http://static."+ Web.WEB_SUFFIX;

        static final String AND = "&";
        static final String EQUALITY = "=";

        interface UserImageParam {
            interface Key {
                String PROTOCOL = "protocol";
                String UID = "uid";
                String SIZE = "size";
                String TIMESTAMP = "timestamp";
            }

            interface Value {
                int PROTOCOL = 10005;
                String SIZE_BIG = "big";
                String SIZE_MIDDLE = "middle";
            }
        }

        private static String getUserImageUrl(int uid, String size, String timestamp) {
            return USER_IMAGE +
                    UserImageParam.Key.PROTOCOL + EQUALITY + UserImageParam.Value.PROTOCOL
                    + AND + UserImageParam.Key.UID + EQUALITY + uid
                    + AND + UserImageParam.Key.SIZE + EQUALITY + size
                    + AND + UserImageParam.Key.TIMESTAMP + timestamp;
        }

        public static String getBigUserImageUrl(int uid, String timestamp) {
            return getUserImageUrl(uid, UserImageParam.Value.SIZE_BIG, timestamp);
        }

        public static String getMiddleUserImageUrl(int uid, String timestamp) {
            return getUserImageUrl(uid, UserImageParam.Value.SIZE_MIDDLE, timestamp);
        }

        public static String getVoaImg(int voaId) {
            return VOA_IMG_BASE + voaId + JPG_SUFFIX;
        }

        interface WebPay {
            interface Param {
                interface Key {
                    String OUT_USER = "out_user";
                    String APP_ID = "appid";
                    String AMOUNT = "amount";
                }
            }
        }

        public static String getWebPayUrl(int uid, int amount,int appId) {
            return WEB_PAY + WebPay.Param.Key.OUT_USER + EQUALITY + uid
                    + AND + WebPay.Param.Key.APP_ID + EQUALITY + appId
                    + AND + WebPay.Param.Key.AMOUNT + EQUALITY + amount;
        }

        public static String getAdPicUrl(String suffix) {
            return AD_PIC + suffix;
        }

        @Deprecated
        public static String getDubbingUrl(int id) {
            return SHUOSHUO_PREFIX + id + MP4_SUFFIX;
        }

        public static String getNewDubbingUrl(String id) {
            return NEW_DUBBING_PREFIX + id;
        }

        public static String getMyDubbingUrl(int backId) {
            return MY_DUBBING_PREFIX + backId;
        }

        public static String getMyDubbingUrl(int backId,String appName) {
            return MY_DUBBING_PREFIX + backId+"&apptype="+appName;
        }
    }

    //包名类型
    interface PackageName{
        String PACKAGE_talkshow = "com.iyuba.talkshow";
        String PACKAGE_peiyin = "com.iyuba.peiyin";
        String PACKAGE_child = "com.iyuba.talkshow.child";
        String PACKAGE_pappa = "com.iyuba.talkshow.pappa";
        String PACKAGE_pig = "com.iyuba.talkshow.pig";
    }
}
