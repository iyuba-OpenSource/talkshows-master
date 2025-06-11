package com.iyuba.iyubamovies.manager;

import android.os.Environment;
import android.util.Log;

import com.iyuba.module.commonvar.CommonVars;

/**
 * Created by iyuba on 2018/1/16.
 */

public class IMoviesConstant {
    //4729911
    public static String UserId="0";
    public static String Vipstatus= "0";
    public static String App_id = "0";
    public static String App_Name = "看一看";
    public static String VIDEO_PLAY_URL="http://tv." + CommonVars.domain + "/series/";
    public static String SHARE_PLAY_URL="http://m." + CommonVars.domain + "/news.html?type=series&id=";
    public static String BuildVideoPlayOrDownLoadURL(String seriseid,String voaid){
        Log.e("play_url",VIDEO_PLAY_URL+seriseid+"/"+voaid+".mp4");
        return VIDEO_PLAY_URL+seriseid+"/"+voaid+".mp4";
    }
    public static String FILE_PATH = Environment.getExternalStorageDirectory() + "/iyuba/imoviesdl";;

    public static String BuildShareSeriseURL(String voaid){
        return SHARE_PLAY_URL+voaid;
    }
    public static String VIPCENTER_ACTION = "vip.center";
}
