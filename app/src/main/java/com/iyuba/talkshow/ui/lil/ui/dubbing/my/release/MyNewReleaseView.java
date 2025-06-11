package com.iyuba.talkshow.ui.lil.ui.dubbing.my.release;

import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_release;
import com.iyuba.lib_common.ui.mvp.BaseView;

import java.util.List;

public interface MyNewReleaseView extends BaseView {

    //显示数据
    void showData(List<Dubbing_publish_release.Data> list);

    //显示错误信息
    void showError(String showMsg);
}
