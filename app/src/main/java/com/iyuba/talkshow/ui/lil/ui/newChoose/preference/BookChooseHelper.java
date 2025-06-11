//package com.iyuba.talkshow.ui.lil.ui.newChoose.preference;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import com.iyuba.lib_common.util.LibResUtil;
//import com.iyuba.talkshow.constant.App;
//import com.iyuba.talkshow.data.manager.ConfigManager;
//
//public class BookChooseHelper {
//    private static BookChooseHelper instance;
//    public static BookChooseHelper getInstance(){
//        if (instance==null){
//            synchronized (BookChooseHelper.class){
//                if (instance==null){
//                    instance = new BookChooseHelper();
//                }
//            }
//        }
//        return instance;
//    }
//
//    private SharedPreferences preferences;
//    private String spName = "kouyu_show_file";
//
//    public BookChooseHelper(){
//        if (preferences==null){
//            preferences = LibResUtil.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
//        }
//    }
//
//    //保存课程内容
//    public void saveCourseId(int id){
//        preferences.edit().putInt(ConfigManager.Key.COURSE_ID,id).apply();
//    }
//
//    public int getCourseId(){
//        return preferences.getInt(ConfigManager.Key.COURSE_ID, App.DEFAULT_BOOKID);
//    }
//
//    public void saveCourseTitle(String title){
//        preferences.edit().putString(ConfigManager.Key.COURSE_TITLE,title).apply();
//    }
//
//    public String getCourseTitle(){
//        return preferences.getString(ConfigManager.Key.COURSE_TITLE,App.DEFAULT_TITLE);
//    }
//
//    //保存单词内容
//    public void saveWordId(int id){
//        preferences.edit().putInt(ConfigManager.Key.WORD_ID,id).apply();
//    }
//
//    public int getWordId(){
//        return preferences.getInt(ConfigManager.Key.WORD_ID,App.DEFAULT_BOOKID);
//    }
//
//    public void saveWordTitle(String title){
//        preferences.edit().putString(ConfigManager.Key.WORD_TITLE,title).apply();
//    }
//
//    public String getWordTitle(){
//        return preferences.getString(ConfigManager.Key.WORD_TITLE,App.DEFAULT_TITLE);
//    }
//}
