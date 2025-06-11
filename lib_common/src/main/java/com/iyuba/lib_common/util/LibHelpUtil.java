package com.iyuba.lib_common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @title: 辅助工具
 * @date: 2023/11/2 16:19
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LibHelpUtil {

    //这里统一处理下，将中文单引号处理成英文单引号
    public static String transTitleStyle(String titleStr){
        if (titleStr.contains("‘")){
            titleStr = titleStr.replace("‘","'");
        }

        if (titleStr.contains("’")){
            titleStr = titleStr.replace("’","'");
        }

        return titleStr;
    }

    /**
     * 获取程序的版本号
     *
     * @param context
     * @param packname
     * @return
     */
    public static String getAppVersion(Context context, String packname) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;
    }

    /*******************手机品牌判断*****************/
    //是否是oppo旗下的手机
    public static boolean isBelongToOppoPhone(){
        String brand = Build.BRAND.toLowerCase();
        switch (brand){
            case "oppo"://oppo
            case "oneplus"://一加
                return true;
        }
        return false;
    }
}
