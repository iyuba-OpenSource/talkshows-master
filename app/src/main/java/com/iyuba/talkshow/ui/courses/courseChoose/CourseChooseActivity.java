package com.iyuba.talkshow.ui.courses.courseChoose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.AppCheckResponse;
import com.iyuba.talkshow.databinding.ActivityCourseChooseBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.tencent.vasdolly.helper.ChannelReaderUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 课程的选书界面
 */
public class CourseChooseActivity extends BaseViewBindingActivity<ActivityCourseChooseBinding> {

    public static final String FLAG = "FLAG";


    @Inject
    ConfigManager configManager;
    @Inject
    DataManager dataManager;

    //    private String[] titleid;
//    private String[] title;
    private int flag;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        flag = getIntent().getIntExtra(FLAG, 300);

        loadCheck();
    }

    public static void start(Context context, int flag) {
        Intent intent = new Intent();
        intent.setClass(context, CourseChooseActivity.class);
        intent.putExtra(FLAG, flag);
        context.startActivity(intent);
    }

    private void initFragment() {
        binding.lessonType.setVisibility(View.INVISIBLE);
        //设置样式1
        binding.toolbarTwo.getRoot().setVisibility(View.GONE);
        setSupportActionBar(binding.toolbarOne);

        ChooseCourseFragment fragment = ChooseCourseFragment.build(ConfigData.default_word_series_id, flag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    private void initSpinnner() {
//        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.course_page_drop,android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource
//                (android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinner.setSelection(position);
//                if (position == 0){
//                    titleid = titleidRenjiao ;
//                    title = titleRenjiao ;
//                }else {
//                    titleid  =titleidWaiyan ;
//                    title  =titleWaiyan ;
//                }
//                initListener();
//                setViewPager();
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }


//    private void initSeries() {
//        titleidRenjiao =getResources().getStringArray(R.array.course_page_catagory_id_renjiao);
//        titleRenjiao =getResources().getStringArray(R.array.course_page_catagory_renjiao);
//        titleidWaiyan =getResources().getStringArray(R.array.course_page_catagory_id_waiyan);
//        titleWaiyan =getResources().getStringArray(R.array.course_page_catagory_waiyan);
//        titleid = titleidRenjiao ;
//        title = titleRenjiao ;
//    }

//    private void initListener() {
//        //TabLayout切换时导航栏图片处理
//        tabs.setupWithViewPager(viewPager);
//
//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {//选中图片操作
//                        viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {//未选中图片操作
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }

//    private void setViewPager() {
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int i) {
//                return ChooseCourseFragment.build(titleid[i],flag);
//            }
//
//            @Override
//            public int getCount() {
//                return title.length;
//            }
//
//            @Nullable
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return title[position];
//            }
//        });
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//    }

    //这里如果显示需要限制人教版，则再次进行接口处理；如果接口中也是这样的，则直接限制人教版
    private void loadCheck(){
        String channel = ChannelReaderUtil.getChannel(this);
        if (ConfigData.renVerifyCheck&&AbilityControlManager.getInstance().isLimitPep()){
            startLoading();

            int verifyId = ConfigData.getRenLimitChannelId(channel);
            dataManager.getAppCheckStatus(verifyId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AppCheckResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            closeLoading();

                            AbilityControlManager.getInstance().setLimitPep(true);
                            initFragment();
                        }

                        @Override
                        public void onNext(AppCheckResponse response) {
                            closeLoading();

                            if (response.getResult().equals("0")){
                                AbilityControlManager.getInstance().setLimitPep(false);
                            }else {
                                AbilityControlManager.getInstance().setLimitPep(true);
                            }

                            initFragment();
                        }
                    });
        }else {
            AbilityControlManager.getInstance().setLimitPep(false);
            initFragment();
        }
    }

    //显示加载
    private void startLoading(){
        closeLoading();

        if (loadingDialog==null){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage("正在加载课程信息");
        loadingDialog.show();
    }

    //关闭加载
    private void closeLoading(){
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }
}
