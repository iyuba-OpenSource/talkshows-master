package com.iyuba.iyubamovies.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by iyuba on 2018/1/20.
 */

public class ImoviesViewSizeUtil {
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.widthPixels;
    }

//    public static int getScreenHeight(Context context) {
//        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
//        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//        return localDisplayMetrics.heightPixels - getStatusBarHeight(context);
//    }

//    public static int getStatusBarHeight(Context context) {
//        Class<?> c = null;
//        Object obj = null;
//        Field field = null;
//        int x = 0;
//        int statusBarHeight = 0;
//
//        try {
//            c = Class.forName("com.android.internal.R$dimen");
//            obj = c.newInstance();
//            field = c.getField("status_bar_height");
//            int x = Integer.parseInt(field.get(obj).toString());
//            statusBarHeight = context.getResources().getDimensionPixelSize(x);
//        } catch (Exception var7) {
//            var7.printStackTrace();
//        }
//
//        return statusBarHeight;
//    }
}
