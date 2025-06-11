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
     * com.iyuba.peiyin 开屏 0059
     * com.iyuba.peiyin Banner 0060
     * com.iyuba.peiyin 插屏 0833
     * com.iyuba.peiyin 模版 0834
     * com.iyuba.peiyin DrawVideo 0835
     * com.iyuba.peiyin 激励视频 0836
     *
     * 优量汇
     * com.iyuba.peiyin 开屏 0837
     * com.iyuba.peiyin Banner 0838
     * com.iyuba.peiyin 插屏 0839
     * com.iyuba.peiyin 模版 0840
     * com.iyuba.peiyin DrawVideo 0841
     * com.iyuba.peiyin 激励视频 0842
     *
     * 百度
     * com.iyuba.peiyin 开屏 0843
     * com.iyuba.peiyin 插屏 0844
     * com.iyuba.peiyin 模版 0845
     * com.iyuba.peiyin 激励视频 0846
     */

    //key值信息
    interface  KeyData{

        //开屏广告
        class SpreadAdKey{
            /**
             * 穿山甲 0059
             * 优量汇 0837
             * 百度 0843
             * 快手
             */
            public static final String spread_youdao = "a710131df1638d888ff85698f0203b46";//有道
            public static final String spread_beizi = "";//倍孜
            public static final String spread_csj = "0059";//穿山甲
            public static final String spread_ylh = "0837";//优量汇
            public static final String spread_baidu = "0843";//百度
            public static final String spread_ks = "";//快手
        }

        //信息流广告
        class TemplateAdKey{
            /**
             * 穿山甲 0834
             * 优量汇 0840
             * 百度 0845
             * 快手
             */
            public static final String template_youdao = "3438bae206978fec8995b280c49dae1e";//有道
            public static final String template_csj = "0834";//穿山甲
            public static final String template_ylh = "0840";//优量汇
            public static final String template_baidu = "0845";//百度
            public static final String template_ks = "";//快手
            public static final String template_vlion = "";
        }

        //banner广告
        class BannerAdKey{
            /**
             * 穿山甲 0060
             * 优量汇 0838
             */
            public static final String banner_youdao = "230d59b7c0a808d01b7041c2d127da95";//有道
            public static final String banner_csj = "0060";//穿山甲
            public static final String banner_ylh = "0838";//优量汇
        }

        //插屏广告
        class InterstitialAdKey{
            /**
             * 穿山甲 0833
             * 优量汇 0839
             * 百度 0844
             * 快手
             */
            public static final String interstitial_csj = "0833";//穿山甲
            public static final String interstitial_ylh = "0839";//优量汇
            public static final String interstitial_baidu = "0844";//百度
            public static final String interstitial_ks = "";//快手
        }

        class DrawVideoAdKey{
            /**
             * 穿山甲 0835
             * 优量汇 0841
             * 快手
             */
            public static final String drawVideo_csj = "0835";//穿山甲
            public static final String drawVideo_ylh = "0841";//优量汇
            public static final String drawVideo_ks = "";//快手
        }

        //激励视频广告
        class IncentiveAdKey{
            /**
             * 穿山甲 0836
             * 优量汇 0842
             * 百度 0846
             * 快手
             */
            public static final String incentive_csj = "0836";//穿山甲
            public static final String incentive_ylh = "0842";//优量汇
            public static final String incentive_baidu = "0846";//百度
            public static final String incentive_ks = "";//快手
        }
    }
}
