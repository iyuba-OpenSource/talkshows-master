package com.iyuba.talkshow.ui.courses.coursedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ActivityCourseDetailBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.courses.common.OpenFlag;
import com.iyuba.talkshow.ui.courses.courseChoose.CourseChooseActivity;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.template.AdTemplateShowManager;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.template.AdTemplateViewBean;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.template.OnAdTemplateShowListener;

import java.util.List;

import javax.inject.Inject;


public class CourseDetailActivity extends BaseActivity implements CourseDetailMVPView{

    @Inject
    CourseDetailPresenter mPresenter ;

    CourseDetailAdapter listAdapter ;
    public static final String SERIES = "series";
    public static final String TITLE = "TITLE";

    private int series ;
    private String  title ;
    @Inject
    ConfigManager configManager;
    ActivityCourseDetailBinding binding ;

//    @OnClick(R.id.change_book)
    public void changeBook(){
        finish();

        //选书配置(暂时没必要删除，不选书就默认使用就行)
//        configManager.putCourseId(0);
//        configManager.putCourseTitle("");
        CourseChooseActivity.start(mContext, OpenFlag.TO_DETAIL);
    }

    public static Intent buildIntent(Context context , int series,String title){
        Intent intent = new Intent( context , CourseDetailActivity.class);
        intent.putExtra(SERIES,series);
        intent.putExtra(TITLE,title);
        return intent ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityComponent().inject(this);

        series = getIntent().getIntExtra(SERIES,212);
        title = getIntent().getStringExtra(TITLE);

        binding.title.setText(title);
        binding.leftLayout.setOnClickListener(v->{
            finish();
        });

        mPresenter.attachView(this);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new CourseDetailAdapter();
        binding.recycler.setAdapter(listAdapter);
        //设置分割线
        binding.recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
//        mPresenter.getVoas(series);
        binding.changeBook.setOnClickListener(v -> changeBook());

        //获取课程数据
        mPresenter.getVoaSeries(series);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭广告
        AdTemplateShowManager.getInstance().stopTemplateAd(adTemplateKey);
    }

    @Override
    public void showCourses(List<Voa> voas) {
        listAdapter.setVoas(voas);

        //设置信息广告
        refreshTemplateAd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change:
                finish();
                configManager.putCourseId(0);
                configManager.putCourseTitle("");
                startActivity(new Intent(mContext, CourseChooseActivity.class));

//                BookChooseHelper.getInstance().saveCourseId(0);
//                BookChooseHelper.getInstance().saveCourseTitle("");
//                BookChooseActivity.start(mContext,BookChooseActivity.tag_lesson);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*****************************设置新的信息流广告************************/
    //当前信息流广告的key
    private String adTemplateKey = CourseDetailActivity.class.getName();
    //模版广告数据
    private AdTemplateViewBean templateViewBean = null;
    //显示广告
    private void showTemplateAd() {
        if (templateViewBean == null) {
            templateViewBean = new AdTemplateViewBean(R.layout.item_ad_mix, R.id.template_container, R.id.ad_whole_body, R.id.native_main_image, R.id.native_title, binding.recycler, listAdapter, new OnAdTemplateShowListener() {
                @Override
                public void onLoadFinishAd() {

                }

                @Override
                public void onAdShow(String showAdMsg) {

                }

                @Override
                public void onAdClick() {

                }
            });
            AdTemplateShowManager.getInstance().setShowData(adTemplateKey, templateViewBean);
        }
        AdTemplateShowManager.getInstance().showTemplateAd(adTemplateKey,this);
    }

    //刷新广告操作[根据类型判断刷新还是隐藏]
    private void refreshTemplateAd(){
        if (!AdBlocker.getInstance().shouldBlockAd() && !UserInfoManager.getInstance().isVip()) {
            //先删除广告
            AdTemplateShowManager.getInstance().clearAd(adTemplateKey);
            //在加载广告
            showTemplateAd();
        } else {
            AdTemplateShowManager.getInstance().stopTemplateAd(adTemplateKey);
        }
    }
}
