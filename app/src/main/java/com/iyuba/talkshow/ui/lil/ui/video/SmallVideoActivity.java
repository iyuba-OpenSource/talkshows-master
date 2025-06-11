package com.iyuba.talkshow.ui.lil.ui.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.LayoutContainerBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;

/**
 * 小视频界面
 */
public class SmallVideoActivity extends BaseViewBindingActivity<LayoutContainerBinding> {

    public static void start(Context context){
        context.startActivity(new Intent(context,SmallVideoActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //显示的界面
        SmallVideoFragment videoFragment = SmallVideoFragment.getInstance(true);
        //展示数据
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container,videoFragment).show(videoFragment).commitNowAllowingStateLoss();
    }
}
