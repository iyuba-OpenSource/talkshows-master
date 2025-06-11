package com.iyuba.talkshow.data.manager;

import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.local.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConfigManager {
    //类型数据
    public static final String LESSON_PRIMARY = "primary";
    public static final String LESSON_JUNIOR = "junior";

    private final PreferencesHelper mPreferencesHelper;

    public int getKouId() {
        return mPreferencesHelper.loadInt(Key.KOU_ID, App.DEFAULT_BOOKID);
    }

    public void putKouId(int courseId){
        mPreferencesHelper.putInt(Key.KOU_ID, courseId);
    }

    public String getKouTitle() {
        return mPreferencesHelper.loadString(Key.KOU_TITLE, App.DEFAULT_TITLE);
    }

    public void putKouTitle(String courseTitle){
        mPreferencesHelper.putString(Key.KOU_TITLE, courseTitle);
    }
    public int getKouCategory() {
        return mPreferencesHelper.loadInt(Key.KOU_CATEGORY, Integer.parseInt(App.DEFAULT_SERIESID));
    }

    public void putKouCategory(int courseId){
        mPreferencesHelper.putInt(Key.KOU_CATEGORY, courseId);
    }

    public int getKouType() {
        if (App.APP_ID == 259) {
            return mPreferencesHelper.loadInt(Key.KOU_TYPE, -1);
        } else {
            return mPreferencesHelper.loadInt(Key.KOU_TYPE, 0);
        }
    }

    public void putKouType(int courseId){
        mPreferencesHelper.putInt(Key.KOU_TYPE, courseId);
    }

    public int getKouClass() {
        return mPreferencesHelper.loadInt(Key.KOU_CLASS, 0);
    }

    public void putKouClass(int courseId){
        mPreferencesHelper.putInt(Key.KOU_CLASS, courseId);
    }

    public String getLessonType(){
        return mPreferencesHelper.loadString(Key.LESSON_TYPE,App.DEFAULT_LESSON_TYPE);
    }

    public void setLessonType(String lessonType){
        mPreferencesHelper.putString(Key.LESSON_TYPE,lessonType);
    }

    public int getCourseId() {
        return mPreferencesHelper.loadInt(Key.COURSE_ID, App.DEFAULT_BOOKID );
    }

    public void putCourseId(int courseId){
        mPreferencesHelper.putInt(Key.COURSE_ID, courseId);
    }

    public String getCourseTitle() {
        return mPreferencesHelper.loadString(Key.COURSE_TITLE, App.DEFAULT_TITLE );
    }

    public void putCourseTitle(String courseTitle){
        mPreferencesHelper.putString(Key.COURSE_TITLE, courseTitle);
    }

    public int getCourseCategory() {
        return mPreferencesHelper.loadInt(Key.COURSE_CATEGORY, Integer.parseInt(App.DEFAULT_SERIESID));
    }

    public void putCourseCategory(int courseId){
        mPreferencesHelper.putInt(Key.COURSE_CATEGORY, courseId);
    }

    public int getCourseType() {
        if (App.APP_ID == 259) {
            return mPreferencesHelper.loadInt(Key.COURSE_TYPE, -1);
        } else {
            return mPreferencesHelper.loadInt(Key.COURSE_TYPE, 0);
        }
    }

    public void putCourseType(int courseId){
        mPreferencesHelper.putInt(Key.COURSE_TYPE, courseId);
    }

    public int getCourseClass() {
        return mPreferencesHelper.loadInt(Key.COURSE_CLASS, 0);
    }

    public void putCourseClass(int courseId){
        mPreferencesHelper.putInt(Key.COURSE_CLASS, courseId);
    }

    public int getWordId() {
        return mPreferencesHelper.loadInt(Key.WORD_ID, App.DEFAULT_BOOKID );
    }

    public void putWordId(int courseId){
        mPreferencesHelper.putInt(Key.WORD_ID, courseId);
    }

    public String getWordTitle() {
        return mPreferencesHelper.loadString(Key.WORD_TITLE, ConfigData.default_word_book_title );
    }

    public void putWordTitle(String courseTitle){
        mPreferencesHelper.putString(Key.WORD_TITLE, courseTitle);
    }

    public int getWordCategory() {
//        return mPreferencesHelper.loadInt(Key.WORD_CATEGORY, Integer.parseInt(App.DEFAULT_SERIESID));
        return mPreferencesHelper.loadInt(Key.WORD_CATEGORY, Integer.parseInt(ConfigData.default_word_series_id));
    }

    public void putWordCategory(int courseId){
        mPreferencesHelper.putInt(Key.WORD_CATEGORY, courseId);
    }

    public int getWordType() {
        if (App.APP_ID == 259) {
            return mPreferencesHelper.loadInt(Key.WORD_TYPE, -1);
        } else {
            return mPreferencesHelper.loadInt(Key.WORD_TYPE, 0);
        }
    }

    public void putWordType(int courseId){
        mPreferencesHelper.putInt(Key.WORD_TYPE, courseId);
    }

    public int getWordClass() {
        return mPreferencesHelper.loadInt(Key.WORD_CLASS, 0 );
    }

    public void putWordClass(int courseId){
        mPreferencesHelper.putInt(Key.WORD_CLASS, courseId);
    }

    public String getDomain() {
        return mPreferencesHelper.loadString(Key.DOMAIN_NAME, "www.iyuba.cn");
    }

    public void setDomain(String domain) {
        mPreferencesHelper.putString(Key.DOMAIN_NAME, domain);
    }

    public String getDomainShort() {
        return mPreferencesHelper.loadString(Key.DOMAIN_SHORT1, "iyuba.cn");
    }

    public void setDomainShort(String domain1) {
        mPreferencesHelper.putString(Key.DOMAIN_SHORT1, domain1);
    }

    public String getDomainLong() {
        return mPreferencesHelper.loadString(Key.DOMAIN_SHORT2, "iyuba.com.cn");
    }

    public void setDomainLong(String domain2) {
        mPreferencesHelper.putString(Key.DOMAIN_SHORT2, domain2);
    }

    public interface Key {
        String DOMAIN_NAME = "domain_name";
        String DOMAIN_SHORT1 = "domain_short1";
        String DOMAIN_SHORT2 = "domain_short2";
        String AUTO_LOGIN = "auto_login";
        String LAST_UID = "last_uid";
        String FIRST_START = "false_start";
        String FIRST_HELP = "false_help";
        String FIRST_SEND_BOOK = "first_Send_book";
        String FIRST_SEND_FLAG = "first_Send_flag";
        String START_AD_URL = "start_ad_link";
        String START_AD_NAME = "start_ad_name";
        String START_AD_START_TIME = "start_ad_start_time";
        String PHOTO_TIMESTAMP = "photo_timestamp";
        String KOU_ID = "kou_id";
        String KOU_TITLE = "kou_title";
        String KOU_CATEGORY = "kou_category";
        String KOU_TYPE = "kou_type";
        String KOU_CLASS = "kou_class";
        String LESSON_TYPE = "lesson_type";
        String COURSE_ID = "course_id";
        String COURSE_TITLE = "course_title";
        String COURSE_CATEGORY = "course_category";
        String COURSE_TYPE = "course_type";
        String COURSE_CLASS = "course_class";
        String WORD_ID = "word_id";
        String WORD_TITLE = "word_title";
        String WORD_CATEGORY = "word_category";
        String WORD_TYPE = "word_type";
        String WORD_CLASS = "word_class";
        String STUDY_REPORT = "study_report";
        String ISLINSHI = "islinshi";
        String CHECK_AGREE = "check_agree";
        String QQ_EDITOR = "qq_editor";
        String QQ_TECHNICIAN = "qq_technician";
        String QQ_MANAGER = "qq_manager";
        String QQ_OFFICIAL = "qq_official";

        //视频倍速
        String VIDEO_SPEED = "video_speed";
        //自动配音播放
        String AUTO_DUBBING = "auto_dubbing";
        //单词类型
        String WORD_USE_TYPE = "word_use_type";
    }

    @Inject
    public ConfigManager(PreferencesHelper preferencesHelper) {
        this.mPreferencesHelper = preferencesHelper;
    }

    public boolean isAutoLogin() {
        return mPreferencesHelper.loadBoolean(Key.AUTO_LOGIN, true);
    }

    public void setAutoLogin(boolean autoLogin) {
        mPreferencesHelper.putBoolean(Key.AUTO_LOGIN, autoLogin);
    }

    public boolean isCheckAgree() {
        return mPreferencesHelper.loadBoolean(Key.CHECK_AGREE, true);
    }

    public void setCheckAgree(boolean autoLogin) {
        mPreferencesHelper.putBoolean(Key.CHECK_AGREE, autoLogin);
    }

    public int getLastUid() {
        return mPreferencesHelper.loadInt(Key.LAST_UID, 0);
    }

    public void putLastUid(int Id){
        mPreferencesHelper.putInt(Key.LAST_UID, Id);
    }

    public boolean isFirstStart() {
        return mPreferencesHelper.loadBoolean(Key.FIRST_START, true);
    }

    public void setFirstStart(boolean firstStart) {
        mPreferencesHelper.putBoolean(Key.FIRST_START, firstStart);
    }

    public boolean isFirstHelp() {
        return mPreferencesHelper.loadBoolean(Key.FIRST_HELP, true);
    }

    public void setFirstHelp(boolean firstStart) {
        mPreferencesHelper.putBoolean(Key.FIRST_HELP, firstStart);
    }

    public boolean isSendBook() {
        return mPreferencesHelper.loadBoolean(Key.FIRST_SEND_BOOK, false);
    }

    public void setSendBook(boolean firstStart) {
        mPreferencesHelper.putBoolean(Key.FIRST_SEND_BOOK, firstStart);
    }

    public int getSendFlag() {
        return mPreferencesHelper.loadInt(Key.FIRST_SEND_FLAG, 0);
    }

    public void setSendFlag(int courseId){
        mPreferencesHelper.putInt(Key.FIRST_SEND_FLAG, courseId);
    }

    public boolean isStudyReport(){
        return mPreferencesHelper.loadBoolean(Key.STUDY_REPORT,true);
    }

    public void setStudyReport(boolean report){
        mPreferencesHelper.putBoolean(Key.STUDY_REPORT, report);
    }

    public boolean isLinshi(){
        return mPreferencesHelper.loadBoolean(Key.ISLINSHI,false);
    }

    public void setLinshi(boolean islinshi){
        mPreferencesHelper.putBoolean(Key.ISLINSHI, islinshi);
    }

    public String getStartAdUrl() {
        return mPreferencesHelper.loadString(Key.START_AD_URL, null);
    }

    public void setStartAdUrl(String startAdUrl) {
        mPreferencesHelper.putString(Key.START_AD_URL, startAdUrl);
    }

    public String getAdName() {
        return mPreferencesHelper.loadString(Key.START_AD_NAME, null);
    }

    public void setAdName(String adName) {
        mPreferencesHelper.putString(Key.START_AD_NAME, adName);
    }

    public String getAdPass() {
        return mPreferencesHelper.loadString(Key.START_AD_START_TIME, null);
    }

    public void setAdPass(String adStartTime) {
        mPreferencesHelper.putString(Key.START_AD_START_TIME, adStartTime);
    }

    public String getPhotoTimestamp() {
        return mPreferencesHelper.loadString(Key.PHOTO_TIMESTAMP, "");
    }

    public void setPhotoTimestamp(String timestamp) {
        mPreferencesHelper.putString(Key.PHOTO_TIMESTAMP, timestamp);
    }

    public String getQQEditor() {
        return mPreferencesHelper.loadString(Key.QQ_EDITOR, "3099007489");
    }

    public void setQQEditor(String qqName) {
        mPreferencesHelper.putString(Key.QQ_EDITOR, qqName);
    }

    public String getQQTechnician() {
        return mPreferencesHelper.loadString(Key.QQ_TECHNICIAN, "2926711810");
    }

    public void setQQTechnician(String qqName) {
        mPreferencesHelper.putString(Key.QQ_TECHNICIAN, qqName);
    }

    public String getQQManager() {
        return mPreferencesHelper.loadString(Key.QQ_MANAGER, "572828703");
    }

    public void setQQManager(String qqName) {
        mPreferencesHelper.putString(Key.QQ_MANAGER, qqName);
    }

    public int getQQOfficial() {
        return mPreferencesHelper.loadInt(Key.QQ_OFFICIAL, 0);
    }

    public void setQQOfficial(int qqName) {
        mPreferencesHelper.putInt(Key.QQ_OFFICIAL, qqName);
    }

    public boolean isAutoDubbing() {
        return mPreferencesHelper.loadBoolean(Key.AUTO_DUBBING, true);
    }

    public void setAutoDubbing(boolean autoLogin) {
        mPreferencesHelper.putBoolean(Key.AUTO_DUBBING, autoLogin);
    }


    /******************************单词相关类型特殊处理*****************************/
    private static final String word_showType = "word_showType";//显示类型
    private static final String word_bookId = "word_bookId";//书籍id
    private static final String word_bookName = "word_bookName";//书籍名称
    private static final String word_typeId = "word_typeId";//分类id

    //单词显示的类型
    public String getWordShowType(){
        return mPreferencesHelper.loadString(word_showType, ConfigData.default_word_show_type);
    }

    public void setWordShowType(String wordShowType){
        mPreferencesHelper.putString(word_showType,wordShowType);
    }

    //单词的书籍id
    public int getWordShowBookId(){
        return mPreferencesHelper.loadInt(word_bookId, ConfigData.default_word_book_id);
    }

    public void setWordShowBookId(int wordBookId){
        mPreferencesHelper.putInt(word_bookId,wordBookId);
    }

    //单词的书籍名称
    public String getWordShowBookName(){
        return mPreferencesHelper.loadString(word_bookName, ConfigData.default_word_book_title);
    }

    public void setWordShowBookName(String wordBookName){
        mPreferencesHelper.putString(word_bookName,wordBookName);
    }

    //单词的类型id
    public String getWordShowTypeId(){
        return mPreferencesHelper.loadString(word_typeId, ConfigData.default_word_series_id);
    }

    public void setWordShowTypeId(String wordShowTypeId){
        mPreferencesHelper.putString(word_typeId,wordShowTypeId);
    }
}
