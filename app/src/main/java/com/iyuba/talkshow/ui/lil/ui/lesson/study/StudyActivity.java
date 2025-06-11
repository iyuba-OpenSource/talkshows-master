package com.iyuba.talkshow.ui.lil.ui.lesson.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.bean.ChapterDetailBean;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.ui.BaseViewBindingActivity;
import com.iyuba.lib_common.util.LibHelpUtil;
import com.iyuba.lib_common.util.LibStackUtil;
import com.iyuba.lib_common.util.LibToastUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.LayoutVp2TabTitleLoadingBinding;
import com.iyuba.talkshow.ui.lil.dialog.LoadingDialog;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlayEvent;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlaySession;
import com.iyuba.talkshow.ui.lil.ui.lesson.study.content.ContentFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 学习界面
 * @date: 2024/1/3 13:29
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description: 通用类型（小学英语、初中英语、小说内容）
 */
public class StudyActivity extends BaseViewBindingActivity<LayoutVp2TabTitleLoadingBinding> implements StudyView{

    //类型
    private String types;
    //书籍的id
    private String bookId;
    //voaId
    private String voaId;
    //当前章节的位置
    private int position = 0;
    //本章节的数据
    private BookChapterBean chapterBean;
    //本章节的详情数据
    private List<ChapterDetailBean> detailList;

    //数据
    private StudyPresenter presenter;
    //适配器
    private StudyAdapter adapter;

    //原文界面
    private ContentFragment contentFragment;

    public static void start(Context context, String types, String bookId, String voaId, int position){
        Intent intent = new Intent();
        intent.setClass(context,StudyActivity.class);
        intent.putExtra(StrLibrary.types,types);
        intent.putExtra(StrLibrary.bookId,bookId);
        intent.putExtra(StrLibrary.voaid,voaId);
        intent.putExtra(StrLibrary.position,position);
        context.startActivity(intent);
    }

    public static Intent buildIntent(Context context,String types,String bookId,String voaId){
        Intent intent = new Intent();
        intent.setClass(context,StudyActivity.class);
        intent.putExtra(StrLibrary.types,types);
        intent.putExtra(StrLibrary.bookId,bookId);
        intent.putExtra(StrLibrary.voaid,voaId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        types = getIntent().getStringExtra(StrLibrary.types);
        bookId = getIntent().getStringExtra(StrLibrary.bookId);
        voaId = getIntent().getStringExtra(StrLibrary.voaid);
        position = getIntent().getIntExtra(StrLibrary.position,0);

        presenter = new StudyPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initToolbar();
        initList();

        presenter.getChapterDetail(types,bookId,voaId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        presenter.detachView();
    }

    /*****************************初始化**************************/
    private void initToolbar(){
        //标题
        binding.toolbar.title.setText("课程详情");
        binding.toolbar.leftImage.setImageResource(R.drawable.ic_back_white_new);
        binding.toolbar.leftLayout.setOnClickListener(v->{
            LibStackUtil.getInstance().finishCur();
        });
    }

    private void initList(){
        binding.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        binding.tabLayout.setTabIndicatorFullWidth(false);
        binding.tabLayout.setTabTextColors(getResources().getColor(R.color.gray), getResources().getColor(R.color.colorPrimaryDark));
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

        adapter = new StudyAdapter(this,new ArrayList<>());
        binding.viewPager2.setAdapter(adapter);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

            }
        });
    }

    /*****************************刷新数据*************************/
    //刷新数据
    private void refreshData(){
        chapterBean = presenter.getChapterData(types,voaId);
        if (chapterBean==null){
            updateUi(false,"暂无当前课程数据");
            return;
        }

        //标题
        binding.toolbar.title.setText(LibHelpUtil.transTitleStyle(chapterBean.getTitleEn()));
        //显示数据
        List<Pair<String,String>> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        titleList.add(new Pair<>(TypeLibrary.StudyPageType.read,"原文"));
        contentFragment = ContentFragment.getInstance(types,voaId,position,bookId);
        fragmentList.add(contentFragment);

        //刷新tab
        binding.tabLayout.removeAllTabs();
        for (int i = 0; i < titleList.size(); i++) {
            Pair<String,String> pair = titleList.get(i);
            TabLayout.Tab tab = binding.tabLayout.newTab();
            tab.setText(pair.second);
            binding.tabLayout.addTab(tab);
        }
        //刷新界面
        adapter.refreshData(fragmentList);

        //数据为1则不显示标题
        if (titleList.size()>1){
            binding.tabLayout.setVisibility(View.VISIBLE);
        }else {
            binding.tabLayout.setVisibility(View.GONE);
        }
    }

    /******************************回调数据*************************/
    @Override
    public void showData(List<ChapterDetailBean> list) {
        if (list!=null){
            if (list.size()>0){
                updateUi(false,null);

                detailList = list;
                refreshData();
            }else {
                updateUi(false,"暂无当前课程的详情数据");
            }
        }else {
            updateUi(false,"暂无当前课程数据");
        }
    }

    @Override
    public void showLoading(String msg) {
        updateUi(true,null);
    }

    @Override
    public void showCollectArticle(boolean isSuccess, boolean isCollect) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJuniorPlayEvent(JuniorBgPlayEvent event){
        if (event.getShowType().equals(JuniorBgPlayEvent.event_data_refresh)){
            if (contentFragment!=null){
                contentFragment.destroyReadFragment();
            }

            //数据刷新
            position = event.getDataIndex();
            chapterBean = JuniorBgPlaySession.getInstance().getVoaList().get(position);
            voaId = chapterBean.getVoaId();
            presenter.getChapterDetail(types,bookId,voaId);
        }
    }

    /********************************其他方法****************************/
    //界面刷新
    private void updateUi(boolean isLoading,String showMsg){
        if (isLoading){
            binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
            binding.loadingLayout.loadingProgress.setVisibility(View.VISIBLE);
            binding.loadingLayout.loadingMsg.setText("正在加载详情数据...");
            binding.loadingLayout.loadingBtn.setVisibility(View.INVISIBLE);
        }else {
            if (!TextUtils.isEmpty(showMsg)){
                binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
                binding.loadingLayout.loadingProgress.setVisibility(View.INVISIBLE);
                binding.loadingLayout.loadingMsg.setText(showMsg);
                binding.loadingLayout.loadingBtn.setVisibility(View.VISIBLE);
            }else {
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
            }
        }
    }
}
