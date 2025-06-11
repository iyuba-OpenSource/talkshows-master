package com.iyuba.talkshow.ui.main.drawer;

import com.iyuba.talkshow.ui.base.MvpView;

/**
 * Created by Administrator on 2016/12/24/024.
 */

public interface SlidingMenuMvpView extends MvpView {
//    void goResultActivity(LoginResult data);

    void showToast(String msg);

    void showLoadingDialog();

    void hideLoadingDialog();
}
