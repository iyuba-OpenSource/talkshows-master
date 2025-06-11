package com.iyuba.talkshow.ui.main;

import com.iyuba.talkshow.ui.base.MvpView;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public interface MainMvpView extends MvpView {
    //显示微信登录消息
    void showWxLoginMsg(String msg);
    //显示登录结果
    void showWxLoginResult();
}
