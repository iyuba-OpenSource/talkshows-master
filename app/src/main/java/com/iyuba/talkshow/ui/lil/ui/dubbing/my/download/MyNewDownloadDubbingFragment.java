package com.iyuba.talkshow.ui.lil.ui.dubbing.my.download;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iyuba.lib_common.ui.BaseViewBindingFragment;
import com.iyuba.talkshow.databinding.LayoutListRefreshTitleBinding;

/**
 *  新的-配音下载界面
 */
public class MyNewDownloadDubbingFragment extends BaseViewBindingFragment<LayoutListRefreshTitleBinding> {

    public static MyNewDownloadDubbingFragment getInstance(){
        MyNewDownloadDubbingFragment fragment = new MyNewDownloadDubbingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**************************初始化****************************/
    private void initView(){

    }

    private void initList(){

    }

    /****************************刷新数据**************************/
}
