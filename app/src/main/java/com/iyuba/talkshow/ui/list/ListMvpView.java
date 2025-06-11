package com.iyuba.talkshow.ui.list;

import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public interface ListMvpView extends MvpView {
    void showVoas(List<Voa> voas);

    void showMoreVoas(List<Voa> voas);

    void showVoasEmpty();

    void showError();

    void showToast(String msg);

    void showToast(int resId);

    void showLoadingDialog();

    void dismissLoadingDialog();

    void dismissRefreshingView();
}
