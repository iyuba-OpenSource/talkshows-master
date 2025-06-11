package com.iyuba.talkshow.ui.lil.ui.dubbing.my.release;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iyuba.lib_common.ui.BaseViewBindingFragment;
import com.iyuba.talkshow.databinding.LayoutListRefreshTitleBinding;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * 新的-配音发布界面
 */
public class MyNewReleaseDubbingFragment extends BaseViewBindingFragment<LayoutListRefreshTitleBinding> {

    //适配器
    private MyNewReleaseAdapter adapter;
    //数据

    public static MyNewReleaseDubbingFragment getInstance(){
        MyNewReleaseDubbingFragment fragment = new MyNewReleaseDubbingFragment();
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

    /******************************初始化******************************/
    private void initView(){
        binding.toolbar.getRoot().setVisibility(View.GONE);
    }

    private void initList(){
        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshData();
        });


    }

    /******************************刷新数据******************************/
    private void refreshData(){

    }
}
