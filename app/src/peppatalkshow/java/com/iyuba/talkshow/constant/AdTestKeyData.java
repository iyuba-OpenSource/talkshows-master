package com.iyuba.talkshow.constant;

/**
 * 广告测试的key数据
 *
 * 自用数据
 */
public interface AdTestKeyData {

    /**
     * 广告位key配置如下
     *
     * 穿山甲
     * com.iyuba.talkshow.pappa 开屏 0075
     * com.iyuba.talkshow.pappa Banner 0076
     * com.iyuba.talkshow.pappa 插屏 0354
     * com.iyuba.talkshow.pappa 模版 0355
     * com.iyuba.talkshow.pappa DrawVideo 0356
     * com.iyuba.talkshow.pappa 激励视频 0357
     *
     * 优量汇
     * com.iyuba.talkshow.pappa 开屏 0358
     * com.iyuba.talkshow.pappa Banner 0359
     * com.iyuba.talkshow.pappa 插屏 0360
     * com.iyuba.talkshow.pappa 模版 0361
     * com.iyuba.talkshow.pappa DrawVideo 0362
     * com.iyuba.talkshow.pappa 激励视频 0363
     *
     * 百度
     * com.iyuba.talkshow.pappa 开屏 0364
     * com.iyuba.talkshow.pappa 插屏 0365
     * com.iyuba.talkshow.pappa 模版 0366
     * com.iyuba.talkshow.pappa 激励视频 0367
     *
     * 快手
     * com.iyuba.talkshow.pappa 开屏 0368
     * com.iyuba.talkshow.pappa 插屏 0369
     * com.iyuba.talkshow.pappa 模版 0370
     * com.iyuba.talkshow.pappa DrawVideo 0371
     * com.iyuba.talkshow.pappa 激励视频 0372
     *
     *
     * 接口数据请在浏览器中查看 http://ai.iyuba.cn/mediatom/server/adplace?placeid=0372
     */

    //key值信息
    interface  KeyData{
        class SpreadAdKey{
            /**
             * 穿山甲 0075
             * 优量汇 0358
             * 百度 0364
             * 快手 0368
             */
            public static final String spread_youdao = "a710131df1638d888ff85698f0203b46";//有道
            public static final String spread_beizi = "";//倍孜
            public static final String spread_csj = "0075";//穿山甲
            public static final String spread_ylh = "0358";//优量汇
            public static final String spread_baidu = "0364";//百度
            public static final String spread_ks = "0368";//快手
        }

        class TemplateAdKey{
            /**
             * 穿山甲 0355
             * 优量汇 0361
             * 百度 0366
             * 快手 0370
             */
            public static final String template_youdao = "3438bae206978fec8995b280c49dae1e";//有道
            public static final String template_csj = "0355";//穿山甲
            public static final String template_ylh = "0361";//优量汇
            public static final String template_baidu = "0366";//百度
            public static final String template_ks = "0370";//快手
            public static final String template_vlion = "";//瑞狮
        }

        class BannerAdKey{
            /**
             * 穿山甲 0076
             * 优量汇 0359
             */
            public static final String banner_youdao = "230d59b7c0a808d01b7041c2d127da95";//有道
            public static final String banner_csj = "0076";//穿山甲
            public static final String banner_ylh = "0359";//优量汇
        }

        class InterstitialAdKey{
            /**
             * 穿山甲 0354
             * 优量汇 0360
             * 百度 0365
             * 快手 0369
             */
            public static final String interstitial_csj = "0354";//穿山甲
            public static final String interstitial_ylh = "0360";//优量汇
            public static final String interstitial_baidu = "0365";//百度
            public static final String interstitial_ks = "0369";//快手
        }

        class DrawVideoAdKey{
            /**
             * 穿山甲 0356
             * 优量汇 0362
             * 快手 0371
             */
            public static final String drawVideo_csj = "0356";//穿山甲
            public static final String drawVideo_ylh = "0362";//优量汇
            public static final String drawVideo_ks = "0371";//快手
        }

        class IncentiveAdKey{
            /**
             * 穿山甲 0357
             * 优量汇 0363
             * 百度 0367
             * 快手 0372
             */
            public static final String incentive_csj = "0357";//穿山甲
            public static final String incentive_ylh = "0363";//优量汇
            public static final String incentive_baidu = "0367";//百度
            public static final String incentive_ks = "0372";//快手
        }
    }
}
