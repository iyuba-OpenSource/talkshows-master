package com.iyuba.talkshow.ui.lil.ui.login;

import android.content.Context;

import com.iyuba.lib_user.data.NewLoginType;

/**
 * @title:
 * @date: 2023/8/25 18:12
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class NewLoginUtil {

    //跳转类型
    public static void startToLogin(Context context){
        NewLoginActivity.start(context, NewLoginType.getInstance().getCurLoginType());
    }
}
