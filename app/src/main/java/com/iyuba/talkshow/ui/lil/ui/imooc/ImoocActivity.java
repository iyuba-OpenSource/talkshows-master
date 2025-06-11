package com.iyuba.talkshow.ui.lil.ui.imooc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.lib_common.ui.BaseViewBindingActivity;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.LayoutContainerBinding;
import com.iyuba.talkshow.ui.lil.ui.video.SmallVideoFragment;

public class ImoocActivity extends BaseViewBindingActivity<LayoutContainerBinding> {

    public static void start(Context context){
        Intent intent = new Intent();
        intent.setClass(context,ImoocActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //显示的界面
        ImoocFragment imoocFragment = ImoocFragment.getInstance(true);
        //展示数据
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container,imoocFragment).show(imoocFragment).commitNowAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
