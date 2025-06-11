package com.iyuba.talkshow.ui.user.login;

import com.iyuba.talkshow.ui.base.MvpView;

public interface LoginMvpView extends MvpView {
    void showWaitingDialog();

    void dismissWaitingDialog();

    void showToast(String message);

    void startImproveUser(int userId);
}
