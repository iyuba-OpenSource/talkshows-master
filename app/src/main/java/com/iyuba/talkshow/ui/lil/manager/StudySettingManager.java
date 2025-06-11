package com.iyuba.talkshow.ui.lil.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.util.LibResUtil;

/**
 * @title: 学习界面设置管理
 * @date: 2024/1/3 14:31
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class StudySettingManager {
    private static final String TAG = "StudySettingManager";
    private static StudySettingManager instance;

    public static StudySettingManager getInstance(){
        if (instance==null){
            synchronized (StudySettingManager.class){
                if (instance==null){
                    instance = new StudySettingManager();
                }
            }
        }
        return instance;
    }

    //缓存名称
    private static final String SP_NAME = TAG;
    //缓存参数
    private static final String sp_content_language = "content_language";//内容语言-与账号无关
    private static final String sp_content_playMode = "content_playMode";//内容音频播放模式-与账号无关
    private static final String sp_content_playSpeed = "content_playSpeed ";//内容音频播放倍速-与账号有关
    private static final String sp_content_studyReport = "content_studyReport";//内容音频学习报告-与账号无关
    private static final String sp_content_textSync = "content_textSync";//内容文本滚动-与账号无关
    //缓存数据
    private SharedPreferences preferences;

    private SharedPreferences getPreferences(){
        if (preferences==null){
            preferences = LibResUtil.getInstance().getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public void setContentLanguage(String languageType){
        getPreferences().edit().putString(sp_content_language,languageType).apply();
    }

    public String getContentLanguage(){
        return getPreferences().getString(sp_content_language, TypeLibrary.TextShowType.ALL);
    }

    public void setContentPlayMode(String playMode){
        getPreferences().edit().putString(sp_content_playMode,playMode).apply();
    }

    public String getContentPlayMode(){
        return getPreferences().getString(sp_content_playMode,TypeLibrary.PlayModeType.ORDER_PLAY);
    }

    public void setContentPlaySpeed(int userId,float playSpeed){
        String param = sp_content_playSpeed+"_"+userId;
        getPreferences().edit().putFloat(param,playSpeed).apply();
    }

    public float getContentPlaySpeed(int userId){
        String param = sp_content_playSpeed+"_"+userId;
        return getPreferences().getFloat(param,1.0f);
    }

    public void setContentStudyReport(boolean showReport){
        getPreferences().edit().putBoolean(sp_content_studyReport,showReport).apply();
    }

    public boolean showContentStudyReport(){
        return getPreferences().getBoolean(sp_content_studyReport,true);
    }

    public void setContentTextSync(boolean textSync){
        getPreferences().edit().putBoolean(sp_content_textSync,textSync).apply();
    }

    public boolean getContentTextSync(){
        return getPreferences().getBoolean(sp_content_textSync,true);
    }
}
