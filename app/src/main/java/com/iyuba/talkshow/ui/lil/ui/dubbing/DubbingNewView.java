package com.iyuba.talkshow.ui.lil.ui.dubbing;

import com.iyuba.lib_common.ui.mvp.BaseView;
import com.iyuba.talkshow.data.model.VoaText;

import java.util.List;

public interface DubbingNewView extends BaseView {

    //展示课程文本
    void showVoaText(List<VoaText> list);

    //展示评测结果数据
    void showEval(boolean isSuccess,String showMsg);
}
