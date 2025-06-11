package com.iyuba.wordtest.data;

import android.content.Context;

/**
 * @desction:
 * @date: 2023/2/16 14:41
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class AppData {

    //logo链接
    public static final String APP_PIC = "http://static3.iyuba.cn/android/images/Primary%20school%20English/Primary%20school%20English_new.png";

    public static final int APP_SHARE_HIDE = 0;

    private static final String PACKAGE_CHILD_ENGLISH = "com.iyuba.talkshow.childenglish";
    private static final String PACKAGE_CHILD_ENGLISH_NEW = "com.iyuba.talkshow.childenglishnew";
    private static final String PACKAGE_XIAOXUE = "com.iyuba.xiaoxue";

    //获取微信的id
    public static String getWeChatId(Context context){
        String packageName = context.getPackageName();
        if (packageName.equals(PACKAGE_CHILD_ENGLISH)){
            return "wx6f3650b6c6690eaa";
        }else if (packageName.equals(PACKAGE_CHILD_ENGLISH_NEW)){
            return "wx182643cdcfc2b59f";
        }else if (packageName.equals(PACKAGE_XIAOXUE)){
            return "wx0caddc6313a39b37";
        }else {
            return "";
        }
    }

    //获取微信的小程序原始id
    public static String getWeChatName(Context context){
        String packageName = context.getPackageName();
        if (packageName.equals(PACKAGE_CHILD_ENGLISH)){
            return "gh_ce4ab26820ab";
        }else if (packageName.equals(PACKAGE_CHILD_ENGLISH_NEW)){
            return "gh_ce4ab26820ab";
        }else if (packageName.equals(PACKAGE_XIAOXUE)){
            return "gh_ce4ab26820ab";
        }else {
            return "";
        }
    }

    //获取微信的小程序id
    public static String getSmallId(Context context){
        String packageName = context.getPackageName();
        if (packageName.equals(PACKAGE_CHILD_ENGLISH)){
            return "wx775f21ca9f27e238";
        }else if (packageName.equals(PACKAGE_CHILD_ENGLISH_NEW)){
            return "wx775f21ca9f27e238";
        }else if (packageName.equals(PACKAGE_XIAOXUE)){
            return "wx775f21ca9f27e238";
        }else {
            return "";
        }
    }
}
