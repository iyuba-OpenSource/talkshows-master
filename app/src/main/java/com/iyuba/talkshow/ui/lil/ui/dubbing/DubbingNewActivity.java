package com.iyuba.talkshow.ui.lil.ui.dubbing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.ui.base.BaseActivity;

/**
 * 新的配音容器界面
 */
public class DubbingNewActivity extends BaseActivity {

    private DubbingNewFragment fragment;

    public static void start(Context context, Voa voa, String audioUrl, String videoUrl){
        Intent intent = new Intent();
        intent.setClass(context,DubbingNewActivity.class);
        intent.putExtra(StrLibrary.data,voa);
        intent.putExtra(StrLibrary.audio,audioUrl);
        intent.putExtra(StrLibrary.video,videoUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_container);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***************************初始化********************************/
    private void initFragment(){
        Voa mVoa = getIntent().getParcelableExtra(StrLibrary.data);
        String audioUrl = getIntent().getStringExtra(StrLibrary.audio);
        String videoUrl = getIntent().getStringExtra(StrLibrary.video);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = DubbingNewFragment.getInstance(mVoa,audioUrl,videoUrl);
        transaction.add(R.id.container,fragment).show(fragment).commitNowAllowingStateLoss();
    }
}
