package com.iyuba.talkshow.ui.main.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.ActivityVideoSmallBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;

public class SmallVideoActivity extends BaseViewBindingActivity<ActivityVideoSmallBinding> {

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

        //替换为小视频
        IHeadlineManager.appId = String.valueOf(App.APP_ID);
        IHeadlineManager.appName = App.APP_NAME_EN;

        String[] types = new String[]{
                HeadlineType.SMALLVIDEO
        };

        Bundle videoBundle = new Bundle();
        videoBundle.putInt("page_count",10);
        videoBundle.putBoolean("show_back",true);
        videoBundle.putStringArray("type",types);
        DropdownTitleFragmentNew fragmentNew = DropdownTitleFragmentNew.newInstance(videoBundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(),fragmentNew);
        transaction.show(fragmentNew);
        transaction.commit();
    }
}
