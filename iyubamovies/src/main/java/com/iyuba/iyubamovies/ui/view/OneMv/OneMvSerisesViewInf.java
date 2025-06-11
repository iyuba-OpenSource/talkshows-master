package com.iyuba.iyubamovies.ui.view.OneMv;

import android.content.Context;

import com.iyuba.iyubamovies.model.OneSerisesData;
import com.iyuba.iyubamovies.network.result.ImoviesCommentData;

import java.util.List;

/**
 * Created by iyuba on 2018/1/15.
 */

public interface OneMvSerisesViewInf {
    void showToast(String content);
    void setSerisesList(List<OneSerisesData> datas);
    void setClickSerisesViewData(OneSerisesData data);
    void setTotalSerises(String totalSerises);
    void onrefreshfinish();
    void onrefreshloadmorefinish();
    void setImoviesDetail(String detail);
    void setCommentslistData(List<ImoviesCommentData> datas);
    void addCommentslistData(List<ImoviesCommentData> datas);
    void setCommentSize(String size);
    void showDialog(String data);
    void dismissDialog();
    void sendSuccess();
    void sendUnSuccess();
    void changeCollectSrc(int id);
    Context getContext();
    void startDLService();
    void showAlertDialog(String data);
}
