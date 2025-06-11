package com.iyuba.talkshow.constant;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.ui.courses.wordChoose.WordTypeHelper;

/**
 * App
 */
public interface App {
    int APP_ID = 249;
    String APP_NAME_EN = "talkShow";
    String APP_NAME_CH = "英语口语秀";
    String SHARE_NAME_EN = "talkshow";
    String APP_NAME_WEIXIN = "英语口语秀";
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
//    boolean APP_CHECK_VIVO = false;
    int APP_SHARE_WXMINIPROGRAM = 0;
    int APP_SHARE_PART = 0;
    int APP_SHARE_HIDE = 0;
    String SMS_APP_KEY = "16788bea16a01";
    String SMS_APP_SECRET = "5809c25db8e9137004f0b4d244e256c7";
    String APP_SAVE_DIR = "appUpdate";
    String UNDERLINE = "_";

    String APK_SUFFIX = ".apk";
    String AD_FOLDER = "ad";
    String ALIPAY_APP_ID = "2016050601368497";
    String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCt+H1XQ65goakPmPhlh+tWbkAWMJUkI0i2Q3ksY2jZA8MEdjqzynJulYLsKhOIafWQIN+FsLeXXOinq/DQmMPuu+hqJwVXohimcMTLYY1WCSj1fqEdXOCuNzShQJV4KXVp6r/BZ46UVzoNnZFQtCVAMRxdzLUQEM+zGUV/CSd3yJH4xUR45lDzUi7mYMR66bkjTXvtrYJRILDYlqpaLlnR91/rmkOz5wHXaCW5qW+haJ6UftwS+EiBKYRmGiu0m6OQtFk1OI2elTyIm1M5Cnv748o0n90uQFEZu8hiAJDXi+eh/WiqZkPfWU1A/Hm1SXzceYPR5MHY8Mo6a279fOINAgMBAAECggEBAJbcN9z3fPyg01kKPsTUs7hUjNBxOrOGwWQEaMycO+yMfY2d9NO6B7drgYfICP4vXFmqmAp3rwzb7YiQ6pgJEUcxvZ5nzhMrJpMWkSEIrCZidRlFwPMUemW4y7PVrIfx87Zwce6GHbb3WQk7jSrdvLvImp+gh5ax7VqS3JgH2Sjd3+LGBIlkhZW9Ew5pcUoV5ol1Hss3cxfqlRV9uT3UyAR+tZxa8rwreoJTjl9A7ni8ZhXtwSA7AecIAUtSLX7FJfeLt7g4Juo84ovuDNYZM73ZDJzloJLjrP/roMSdbjtL1SdxzHEwVFlwpQ8bevY4XB2y4Dwtgx67eTMKUAHKO+UCgYEA1h8V6ec66ryfZi8AgklZVuXTUCeBJrvfcqZHDbvNZQRtfbc52BTirtpcRaVPHwEAxeYvjRpitDiIDcSeTrcRjZEIgvyzLCYfLIv8J42wS7aK5Zin5gDxIvpiAO8HrsuCoXxP1a+pyjhD3cV9vVPHfxD8hFv0KJWgFOQK8wa8j/MCgYEAz/8VHGY/5JojewvMdcOjRe6FvfykQBcD7lo6GoYXBMnVIv+MgsdYcJECFpTRUXq9KNanUHUmIXdZIb/cUhr0uDlCXqo3oGOJzXEBBlNfg4Gg5rVoz+9rcUK2nraLaKZTslwLR2ySlawWnjEJT2A1gazhMZLutuSbtJnRNOvvRf8CgYEAyrtmATAI7aYn+hT4k2MlboxuFg9BTk5Fk4Tx84PkRgf6LzSjVP75XfqrsNmC32UQuU9nqF7aI67+yqJmDTtyKCqw92yRrHRvwbrMxRp3WEh+nEJ8fd5YcfjFgALRsGNJzOIpqLYIucmqIDlUA0Vmtt17aUqzExYQGpeL8mxnbpcCgYBlTihKHMMh5LFDTQvYj+EGPpaFYnfdf1g6z1ddc9HiUyusUCtvxwgcS4Ro2zLYLJ/VNDdpyKU5x5dyCLCWjOqEj97znJRbWh/UICYPnqv2sTxdIh5aqJH8KDIqO17LKbe6N3qG3yrGG3sosVmHf6SP9FP6gUYjblUoMYLj88YmoQKBgH3SHXePRIcSBYKCzm5+iX8hgEf1Zvw7JR1w+R3oJMjzbGNnls7yOy2mMFl74IP3e7wPs+xwbyb4OB/8SKILueAEQ4ysOO9jH9eNrAbfvvUz1F8DfEGhGYOB0B0UR2405yIkafEcIo8l6i4yoPtBPHR+c09D18I1o3oiblIrXvd+";

    int COURSE_TYPE = WordTypeHelper.DEFAULT_TYPE;

    String DEFAULT_WORDS = "middle";
    String DEFAULT_TYPE = "primary";

    //是否判断为儿童类型
    class Apk {
        public static boolean isChild() {
            return App.APP_ID == 256;
        }
    }

    class Url {
        public static String APP_ICON_URL = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/04e5635d7d64b4f21177fb61b23bbed880d83a7fd";
        //北京爱语吧
        public static String USAGE_URL = Constant.Url.PROTOCOL_BJIYB_USAGE;
        public static String PROTOCOL_URL = Constant.Url.PROTOCOL_BJIYB_PRIVACY;
        public static String CHILD_PROTOCOL_URL = Constant.Url.CHILD_PROTOCOL_BJIYB_PRIVACY;
        public static String VIP_AGREEMENT_URL = Constant.Url.VIP_AGREEMENT_BJIYB_PRIVACY;

        public static String SHARE_APP_URL = Constant.Url.APP_SHARE_URL + APP_ID;
    }

    /****oaid升级****/
    //证书需要每年更换-2023年01月12日
    //oaid的证书名称
    String oaid_pem = "com.iyuba.talkshow.cert.pem";
    //是否升级oaid
    boolean OAID_UPDATE = true;

    /****是否展示单词界面****/
    //默认的数据
    int DEFAULT_BOOKID = 205;
    String DEFAULT_TITLE = "1年级上(新起点)";

    String DEFAULT_SERIESID = "313";
    String DEFAULT_SERIES_TITLE = "新起点";

    String DEFAULT_SOURCE = "人教版";

    String DEFAULT_LESSON_TYPE = "primary";//junior-初中，primary-小学

}
