package com.iyuba.talkshow.ui.user.me.me_new;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.imooclib.ui.record.PurchaseRecordActivity;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.ui.BaseViewBindingActivity;
import com.iyuba.lib_common.util.LibDateUtil;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.lib_common.util.LibStackUtil;
import com.iyuba.lib_common.util.LibUrlSpellUtil;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.favor.BasicFavor;
import com.iyuba.module.favor.ui.BasicFavorActivity;
import com.iyuba.module.headlinetalk.ui.mytalk.MyTalkActivity;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.AtyMeNewBinding;
import com.iyuba.talkshow.ui.lil.ui.dubbing.my.MyNewDubbingActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.user.collect.CollectionActivity;
import com.iyuba.talkshow.ui.user.me.CalendarActivity;
import com.iyuba.talkshow.ui.user.me.dubbing.MyDubbingActivity;
import com.iyuba.talkshow.ui.wallet.WalletHistoryActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.words.WordNoteActivity;
import com.iyuba.talkshow.util.MD5;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import personal.iyuba.personalhomelibrary.event.UserPhotoChangeEvent;
import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;
import personal.iyuba.personalhomelibrary.ui.message.MessageActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryType;

/**
 * @title: 我的-新界面
 * @date: 2024/1/9 14:01
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class MeNewActivity extends BaseViewBindingActivity<AtyMeNewBinding> {

    public static void start(Context context){
        Intent intent = new Intent();
        intent.setClass(context,MeNewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initToolbar();
        initView();
        initClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /***************************************初始化******************************/
    private void initToolbar(){
        binding.toolbar.title.setText("我的");
        binding.toolbar.leftImage.setImageResource(R.drawable.back);
        binding.toolbar.leftLayout.setOnClickListener(v->{
            LibStackUtil.getInstance().finishCur();
        });
        /*if (UserInfoManager.getInstance().isLogin()){
            binding.toolbar.rightText.setText("退出");
            binding.toolbar.rightLayout.setOnClickListener(v->{
                new AlertDialog.Builder(this)
                        .setMessage(getResources().getString(R.string.logout_alert))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserInfoManager.getInstance().clearUserInfo();
                                LibStackUtil.getInstance().finishCur();
                            }
                        }).setNegativeButton("取消",null)
                        .show();
            });
        }*/
    }

    private void initView(){
        if (UserInfoManager.getInstance().isLogin()){
            LibGlide3Util.loadCircleImg(this, LibUrlSpellUtil.fixUserPicUrl(UserInfoManager.getInstance().getUserId()),R.drawable.default_avatar,binding.userImg);
            binding.userName.setText(UserInfoManager.getInstance().getUserName());
            if (UserInfoManager.getInstance().isVip()){

                //这里处理下：5个小时之内的时间，则显示详情的会员时间；否则显示日期即可
                long outDateTime = 5*60*60*1000L;
                //判断显示到期时间
                long vipTime = UserInfoManager.getInstance().getVipTime();
                if (vipTime - System.currentTimeMillis() > outDateTime){
                    binding.vipTime.setText("到期时间："+ LibDateUtil.toDateStr(UserInfoManager.getInstance().getVipTime(), LibDateUtil.YMD));
                }else {
                    binding.vipTime.setText("到期时间："+ LibDateUtil.toDateStr(UserInfoManager.getInstance().getVipTime(), LibDateUtil.YMDHMS));
                }

                binding.vipTime.setTextColor(getResources().getColor(R.color.orange));
            }else {
                binding.vipTime.setText("普通用户");
                binding.vipTime.setTextColor(getResources().getColor(R.color.gray));
            }
        }else {
            binding.userImg.setImageResource(R.drawable.default_avatar);
            binding.userName.setText("未登录");
            binding.vipTime.setText("未登录");
            binding.vipTime.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    private void initClick(){
        binding.loginOut.setOnClickListener(v->{
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.logout_alert))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserInfoManager.getInstance().clearUserInfo();
                            LibStackUtil.getInstance().finishCur();
                        }
                    }).setNegativeButton("取消",null)
                    .show();
        });
        binding.userImg.setOnClickListener(v->{
            //个人中心
            UserInfoManager.getInstance().initUserInfo();
            startActivity(PersonalHomeActivity.buildIntent(this,UserInfoManager.getInstance().getUserId(), UserInfoManager.getInstance().getUserName(), 0));
        });
        binding.studyReportLayout.setOnClickListener(v->{
            //学习报告
            if (UserInfoManager.getInstance().isLogin()){
                UserInfoManager.getInstance().initUserInfo();
                String[] types = new String[]{
                        SummaryType.LISTEN,
//                    SummaryType.WORD,
                        SummaryType.EVALUATE,
                        SummaryType.MOOC
                };
                startActivity(SummaryActivity.getIntent(this, Constant.EVAL_TYPE, types,0));
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.signReportLayout.setOnClickListener(v->{
            //打卡报告
            if (UserInfoManager.getInstance().isLogin()){
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.articleCollectLayout.setOnClickListener(v->{
            //文章收藏
            if (UserInfoManager.getInstance().isLogin()){
                Intent intent = new Intent(this, CollectionActivity.class);
                startActivity(intent);
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.videoCollectLayout.setOnClickListener(v->{
            //视频收藏
            if (UserInfoManager.getInstance().isLogin()){
                //设置过滤
                List<String> typeFilter = new ArrayList<>();
                typeFilter.add(HeadlineType.SMALLVIDEO);
                BasicFavor.setTypeFilter(typeFilter);

                startActivity(BasicFavorActivity.buildIntent(this));
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.lessonDubbingLayout.setOnClickListener(v->{
            //课程配音
            if (UserInfoManager.getInstance().isLogin()){
                Intent intent = new Intent(this, MyDubbingActivity.class);
                startActivity(intent);

                //换成新的
//                MyNewDubbingActivity.start(this);
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.videoDubbingLayout.setOnClickListener(v->{
            //视频配音
            if (UserInfoManager.getInstance().isLogin()){
                UserInfoManager.getInstance().initUserInfo();

                ArrayList<String> types = new ArrayList<>();
                types.add(HeadlineType.SMALLVIDEO);
                startActivity(MyTalkActivity.buildIntent(this,types));
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.wordNoteLayout.setOnClickListener(v->{
            //我的生词
            if (UserInfoManager.getInstance().isLogin()){
                Intent intent = new Intent(this, WordNoteActivity.class);
                startActivity(intent);
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.walletHistoryLayout.setOnClickListener(v->{
            //钱包记录
            if (UserInfoManager.getInstance().isLogin()){
                WalletHistoryActivity.start(this);
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.payHistoryLayout.setOnClickListener(v->{
            //支付记录
            if (UserInfoManager.getInstance().isLogin()){
                startActivity(PurchaseRecordActivity.buildIntent(this));
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.messageCenterLayout.setOnClickListener(v->{
            //消息中心
            if (UserInfoManager.getInstance().isLogin()){
                startActivity(new Intent(this, MessageActivity.class));
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
        binding.integralStoreLayout.setOnClickListener(v->{
            //积分商城
            if (UserInfoManager.getInstance().isLogin()){
                String url = "http://m."+ Constant.Web.WEB_SUFFIX+"mall/index.jsp?"
                        + "&uid=" + UserInfoManager.getInstance().getUserId()
                        + "&sign=" + MD5.getMD5ofStr("iyuba" + UserInfoManager.getInstance().getUserId() + "camstory")
                        + "&username=" + UserInfoManager.getInstance().getUserName()
                        + "&platform=android&appid=" + App.APP_ID;

                startActivity(WebActivity.buildIntent(this, url,
                        "积分商城",
                        "积分明细",
                        "http://api."+ Constant.Web.WEB_SUFFIX+"credits/useractionrecordmobileList1.jsp?uid=" + UserInfoManager.getInstance().getUserId())
                );
            }else {
                NewLoginUtil.startToLogin(this);
            }
        });
    }


    /***************************************回调方法******************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(UserPhotoChangeEvent event){
        //刷新显示
        initView();
    }
}
