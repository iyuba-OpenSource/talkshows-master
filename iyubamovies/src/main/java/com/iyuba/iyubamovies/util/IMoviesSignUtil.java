package com.iyuba.iyubamovies.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by iyuba on 2018/1/15.
 */

public class IMoviesSignUtil {

    public static String getSignOfImoviesListApi(){
        Log.e("MD5之前的","iyuba"+ getCurrentDate()
                +"0");
        return IMoviesMD5.getMD5ofStr(
                "iyuba"+ getCurrentDate()
                        +"0"
        );
    }
    public static String getSignOfOneSerisesApi(){
        Log.e("MD5之前的","iyuba"+ getCurrentDate()
                +"series");
        return IMoviesMD5.getMD5ofStr(
                "iyuba"+ getCurrentDate()
                        +"series"
        );
    }
    public static String getCurrentDate(){
        long timeStamp = new Date().getTime()/1000+3600*8; //东八区;
        long days=timeStamp/86400;
        return Long.toString(days);
    }
    public static String getFormatDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.CHINA);

        return sdf.format(new Date());
    }
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }
}
