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
     * 倍孜
     * com.iyuba.talkshow 开屏 0637
     *
     * 穿山甲
     * com.iyuba.talkshow 开屏 0055
     * com.iyuba.talkshow Banner 0056
     * com.iyuba.talkshow 插屏 0638
     * com.iyuba.talkshow 模版 0639
     * com.iyuba.talkshow DrawVideo 0640
     * com.iyuba.talkshow 激励视频 0641
     *
     * 优量汇
     * com.iyuba.talkshow 开屏 0642
     * com.iyuba.talkshow Banner 0643
     * com.iyuba.talkshow 插屏 0644
     * com.iyuba.talkshow 模版 0645
     * com.iyuba.talkshow DrawVideo 0646
     * com.iyuba.talkshow 激励视频 0647
     *
     * 快手
     * com.iyuba.talkshow 开屏 0648
     * com.iyuba.talkshow 插屏 0649
     * com.iyuba.talkshow 模版 0650
     * com.iyuba.talkshow DrawVideo 0651
     * com.iyuba.talkshow 激励视频 0652
     *
     * 百度
     * com.iyuba.talkshow 开屏 0653
     * com.iyuba.talkshow 插屏 0654
     * com.iyuba.talkshow 模版 0655
     * com.iyuba.talkshow 激励视频 0656
     */

    //key值信息
    interface  KeyData{
        //开屏广告
        class SpreadAdKey{
            /**
             * 倍孜 0637
             * 穿山甲 0055
             * 优量汇 0642
             * 百度 0653
             * 快手 0648
             */
            public static final String spread_youdao = "a710131df1638d888ff85698f0203b46";//有道
            public static final String spread_beizi = "0637";//倍孜
            public static final String spread_csj = "0055";//穿山甲
            public static final String spread_ylh = "0642";//优量汇
            public static final String spread_baidu = "0653";//百度
            public static final String spread_ks = "0648";//快手
        }

        //信息流广告
        class TemplateAdKey{
            /**
             * 穿山甲 0639
             * 优量汇 0645
             * 百度 0655
             * 快手 0650
             */
            public static final String template_youdao = "3438bae206978fec8995b280c49dae1e";//有道
            public static final String template_csj = "0639";//穿山甲
            public static final String template_ylh = "0645";//优量汇
            public static final String template_baidu = "0655";//百度
            public static final String template_ks = "0650";//快手
            public static final String template_vlion = "";//瑞狮
        }

        //banner广告
        class BannerAdKey{
            /**
             * 穿山甲 0056
             * 优量汇 0643
             */
            public static final String banner_youdao = "230d59b7c0a808d01b7041c2d127da95";//有道
            public static final String banner_csj = "0056";//穿山甲
            public static final String banner_ylh = "0643";//优量汇
        }

        //插屏广告
        class InterstitialAdKey{
            /**
             * 穿山甲 0638
             * 优量汇 0644
             * 百度 0654
             * 快手 0649
             */
            public static final String interstitial_csj = "0638";//穿山甲
            public static final String interstitial_ylh = "0644";//优量汇
            public static final String interstitial_baidu = "0654";//百度
            public static final String interstitial_ks = "0649";//快手
        }

        class DrawVideoAdKey{
            /**
             * 穿山甲 0640
             * 优量汇 0646
             * 快手 0651
             */
            public static final String drawVideo_csj = "0640";//穿山甲
            public static final String drawVideo_ylh = "0646";//优量汇
            public static final String drawVideo_ks = "0651";//快手
        }

        //激励视频广告
        class IncentiveAdKey{
            /**
             * 穿山甲 0641
             * 优量汇 0647
             * 百度 0656
             * 快手 0652
             */
            public static final String incentive_csj = "0641";//穿山甲
            public static final String incentive_ylh = "0647";//优量汇
            public static final String incentive_baidu = "0656";//百度
            public static final String incentive_ks = "0652";//快手
        }
    }
}
