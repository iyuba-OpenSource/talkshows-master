package com.iyuba.talkshow.ui.detail;

import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.event.LoginResult;
import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

public interface DetailMvpView extends MvpView {

    void showToast(int resId);

    void showToast(String message);

    void setIsCollected(boolean isCollected);

//    void setCollectTvText(int resId);

    void showVoaTextLit(List<VoaText> voaTextList);

    void onDeductIntegralSuccess(int type);

    void showPdfFinishDialog(String url);
    void goResultActivity(LoginResult data);

    void showLoading(String text);

    void hideLoading();
}
