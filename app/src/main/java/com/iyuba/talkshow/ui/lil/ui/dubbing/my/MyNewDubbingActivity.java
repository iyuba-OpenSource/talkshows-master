package com.iyuba.talkshow.ui.lil.ui.dubbing.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.iyuba.lib_common.ui.BaseViewBindingActivity;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.LayoutVp2TabTitleBinding;
import com.iyuba.talkshow.ui.lil.ui.dubbing.my.download.MyNewDownloadDubbingFragment;
import com.iyuba.talkshow.ui.lil.ui.dubbing.my.draft.MyNewDraftDubbingFragment;
import com.iyuba.talkshow.ui.lil.ui.dubbing.my.release.MyNewReleaseDubbingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 新的-我的配音界面
 */
public class MyNewDubbingActivity extends BaseViewBindingActivity<LayoutVp2TabTitleBinding> {

    public static void start(Context context){
        Intent intent = new Intent();
        intent.setClass(context, MyNewDubbingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initToolbar();
        initList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*****************************界面数据****************************/
    //顶部导航栏
    private void initToolbar(){
        binding.toolbar.leftImage.setImageResource(R.drawable.back);
        binding.toolbar.leftLayout.setOnClickListener(v->{
            finish();
        });
        binding.toolbar.title.setText("我的配音");
        binding.toolbar.rightLayout.setVisibility(View.VISIBLE);
        binding.toolbar.rightText.setVisibility(View.VISIBLE);
        binding.toolbar.rightText.setText("编辑");
        binding.toolbar.rightText.setOnClickListener(v->{
            //这里涉及到状态切换问题
            String showText = binding.toolbar.rightText.getText().toString();
            if (showText.equals("编辑")){

            }else if (showText.equals("删除")){

            }
        });
    }

    //界面显示
    private void initList(){
        List<Pair<String, Fragment>> pairList = new ArrayList<>();
        pairList.add(new Pair<>("已发布", MyNewReleaseDubbingFragment.getInstance()));
        pairList.add(new Pair<>("草稿箱", MyNewDraftDubbingFragment.getInstance()));
        pairList.add(new Pair<>("已下载", MyNewDownloadDubbingFragment.getInstance()));

        //tab显示
        for (int i = 0; i < pairList.size(); i++) {
            TabLayout.Tab tab = binding.tabLayout.newTab();
            tab.setText(pairList.get(i).first);
            binding.tabLayout.addTab(tab);
        }
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //界面显示
        MyNewDubbingAdapter dubbingAdapter = new MyNewDubbingAdapter(this,new ArrayList<>());
        binding.viewPager2.setAdapter(dubbingAdapter);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.getTabAt(position).select();

                //根据当前界面显示操作

            }
        });
    }

    //顶部按钮显示
    private void refreshTopBtn(){

    }
}
