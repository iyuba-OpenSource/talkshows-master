package com.iyuba.talkshow.ui.main.home;

import android.content.DialogInterface;

import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

/**
 * @title:
 * @date: 2023/9/1 16:06
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface HomeMvpView extends MvpView {

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

    void showAlertDialog(String msg, DialogInterface.OnClickListener listener);

    void startAboutActivity(String versionCode, String appUrl);

    void showMoewNewList(List<Voa> voas, CategoryFooter category);

    /*void goResultActivity(LoginResult data);*/
}
