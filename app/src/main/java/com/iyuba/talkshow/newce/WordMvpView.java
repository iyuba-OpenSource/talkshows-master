package com.iyuba.talkshow.newce;

import com.iyuba.talkshow.ui.base.MvpView;

/**
 * Created by carl shen on 2020/11/16.
 */

public interface WordMvpView extends MvpView {
    void showLoadingDialog();
    void dismissLoadingDialog();
//    void goResultActivity(LoginResult data);
}
