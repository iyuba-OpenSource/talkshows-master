package com.iyuba.talkshow.ui.lil.event;

/**
 * @title: 刷新操作
 * @date: 2023/10/24 11:09
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class RefreshEvent {

    /****************************************标志****************************/
    public static final String USER_INFO = "user_info";


    /*****************************************事件****************************/
    private String type;

    public RefreshEvent(String type) {
        this.type = type;
    }

    public static String getUserInfo() {
        return USER_INFO;
    }

    public String getType() {
        return type;
    }
}
