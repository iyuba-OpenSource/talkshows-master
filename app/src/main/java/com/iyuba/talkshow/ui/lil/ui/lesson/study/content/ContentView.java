package com.iyuba.talkshow.ui.lil.ui.lesson.study.content;

import com.iyuba.lib_common.ui.mvp.BaseView;

/**
 * @title:
 * @date: 2024/1/4 10:03
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface ContentView extends BaseView {

    /*********广告显示逻辑*********/
    //显示有道开屏广告
    void showYoudaoSplashAD(String picUrl,String linkUrl);

    //显示爱语吧sdk开屏广告
    void showIyubaSdkAD(String picUrl,String linkUrl);

    //显示网页开屏广告
    void showWebSplashAD(String picUrl,String linkUrl);
}
