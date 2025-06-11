package com.iyuba.talkshow.ui.main;

import android.content.DialogInterface.OnClickListener;

import com.iyuba.talkshow.data.model.AllWordsRespons;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.ui.base.MvpView;
import com.iyuba.wordtest.entity.TalkShowWords;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public interface MainMvpView extends MvpView {
    void showVoas(List<Voa> voas);

    void showVoasByCategory(List<Voa> voas, CategoryFooter categoroy);

    void showVoasEmpty();

    void showError();

    void setBanner(List<LoopItem> loopItemList);

    void showToast(String text);

    void showToast(int resId);

    void startDetailActivity(Voa voa);

    void showReloadView();

    void dismissReloadView();

    void dismissRefreshingView();

    void showAlertDialog(String msg, OnClickListener listener);

    void startAboutActivity(String versionCode, String appUrl);

    void showMoewNewList(List<Voa> voas, CategoryFooter category);

    void onWordsReady(AllWordsRespons allWordsRespons);

    void refreshBookWord(int book_id , int version, List<TalkShowWords> words);

    void goResultActivity(LoginResult data);
}
