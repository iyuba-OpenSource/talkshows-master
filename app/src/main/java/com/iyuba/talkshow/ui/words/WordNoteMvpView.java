package com.iyuba.talkshow.ui.words;

import androidx.annotation.StringRes;
import android.view.ActionMode;
import android.view.MenuItem;


import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

interface WordNoteMvpView extends MvpView {

//    void setLoading(boolean isLoading);

    void setRecyclerEndless(boolean isEndless);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    void onLatestDataLoaded(List<Word> words, int total, boolean instantRefresh);

    void onMoreDataLoaded(List<Word> words, int page);

    //旧版本删除单词功能
//    void onDeleteAccomplish(int userId, ActionMode mode);

    //新的删除方法
    //删除完成
    void onDeleteAccomplish(int userId, MenuItem menuItem);
    //删除失败
    void onDeleteFail();
}
