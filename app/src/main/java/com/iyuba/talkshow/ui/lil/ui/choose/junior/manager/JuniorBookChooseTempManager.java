package com.iyuba.talkshow.ui.lil.ui.choose.junior.manager;

import android.content.SharedPreferences;

import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.lib_common.util.LibSPUtil;
import com.iyuba.talkshow.constant.ConfigData;

/**
 * @title: 中小学的书籍管理-临时
 * @date: 2023/4/27 09:47
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description: 主要用于区分单词和课程的数据内容，以后合并的话就是使用另一个
 */
public class JuniorBookChooseTempManager {
    private static final String TAG = "JuniorBookChooseManager";

    private static JuniorBookChooseTempManager instance;

    public static JuniorBookChooseTempManager getInstance(){
        if (instance==null){
            synchronized (JuniorBookChooseTempManager.class){
                if (instance==null){
                    instance = new JuniorBookChooseTempManager();
                }
            }
        }
        return instance;
    }

    //存储的信息
    private static final String SP_NAME = TAG;
    private static final String SP_BOOK_TYPE = "type";
    private static final String SP_BOOK_BIG_TYPE = "bigType";
    private static final String SP_BOOK_SMALL_TYPE_ID = "smallTypeId";
    private static final String SP_BOOK_ID = "bookId";
    private static final String SP_BOOK_NAME = "bookName";

    private SharedPreferences preferences;

    private SharedPreferences getPreference(){
        if (preferences==null){
            preferences = LibSPUtil.getPreferences(LibResUtil.getInstance().getContext(), SP_NAME);
        }
        return preferences;
    }

    public String getBookType(){
        return LibSPUtil.loadString(getPreference(),SP_BOOK_TYPE, ConfigData.default_lesson_junior_type);
    }

    public void setBookType(String bookType){
        LibSPUtil.putString(getPreference(),SP_BOOK_TYPE,bookType);
    }

    public String getBookBigType(){
        return LibSPUtil.loadString(getPreference(),SP_BOOK_BIG_TYPE, ConfigData.default_lesson_junior_big_type);
    }

    public void setBookBigType(String bookBigType){
        LibSPUtil.putString(getPreference(),SP_BOOK_BIG_TYPE,bookBigType);
    }

    public String getBookSmallTypeId(){
        return LibSPUtil.loadString(getPreference(),SP_BOOK_SMALL_TYPE_ID, ConfigData.default_lesson_junior_small_type_id);
    }

    public void setBookSmallTypeId(String bookSmallType){
        LibSPUtil.putString(getPreference(),SP_BOOK_SMALL_TYPE_ID,bookSmallType);
    }

    public String getBookId(){
        return LibSPUtil.loadString(getPreference(),SP_BOOK_ID, ConfigData.default_lesson_junior_book_id);
    }

    public void setBookId(String bookId){
        LibSPUtil.putString(getPreference(),SP_BOOK_ID,bookId);
    }

    public String getBookName(){
        return LibSPUtil.loadString(getPreference(),SP_BOOK_NAME, ConfigData.default_lesson_junior_book_name);
    }

    public void setBookName(String bookName){
        LibSPUtil.putString(getPreference(),SP_BOOK_NAME,bookName);
    }
}
