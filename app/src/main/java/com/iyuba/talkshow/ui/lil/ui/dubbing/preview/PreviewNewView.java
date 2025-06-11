package com.iyuba.talkshow.ui.lil.ui.dubbing.preview;

import com.iyuba.lib_common.ui.mvp.BaseView;
import com.iyuba.talkshow.data.model.VoaText;

import java.util.List;

public interface PreviewNewView extends BaseView {

    //展示课程文本
    void showVoaText(List<VoaText> list);

    //显示发布状态
    void showPublishStatus(boolean isSuccess,int shuoshuoId);

    //显示保存草稿的状态
    void showSaveDraftStatus(boolean isSuccess);
}
