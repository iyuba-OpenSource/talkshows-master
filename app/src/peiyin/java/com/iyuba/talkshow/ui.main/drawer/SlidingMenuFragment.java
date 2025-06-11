package com.iyuba.talkshow.ui.main.drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.lib_common.util.LibUrlSpellUtil;
import com.iyuba.lib_user.event.UserRefreshEvent;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.commonvar.CommonVars;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.databinding.FragmentSlidingMenuBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.LoginOutEvent;
import com.iyuba.talkshow.event.ModifyUserNameEvent;
import com.iyuba.talkshow.ui.about.AboutActivity;
import com.iyuba.talkshow.ui.base.BaseFragment;
import com.iyuba.talkshow.ui.feedback.FeedbackActivity;
import com.iyuba.talkshow.ui.help.HelpUseActivity;
import com.iyuba.talkshow.ui.lil.ui.ad.ui.AdContainerActivity;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.AdShowUtil;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.rank.RankActivity;
import com.iyuba.talkshow.ui.user.collect.CollectionActivity;
import com.iyuba.talkshow.ui.user.me.me_new.MeNewActivity;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.ui.words.WordNoteActivity;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.talkshow.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import personal.iyuba.personalhomelibrary.PersonalHomeManager;
import personal.iyuba.personalhomelibrary.event.UserPhotoChangeEvent;
import personal.iyuba.personalhomelibrary.ui.groupChat.GroupChatManageActivity;


/**
 * Created by Administrator on 2016/12/24/024.
 */

public class SlidingMenuFragment extends BaseFragment implements SlidingMenuMvpView {


    //布局样式
    private FragmentSlidingMenuBinding binding;
    //加载弹窗
    private LoadingDialog loadingDialog;
    @Inject
    SlidingMenuPresenter mPresenter;
    @Inject
    ConfigManager configManager;

    //这里判断下是否是从视频模块出来的，因为视频模块点击广告后没有回调事件，手动触发
    private boolean isToVideo = false;

    public static SlidingMenuFragment newInstance() {
        return new SlidingMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlidingMenuBinding.inflate(inflater,container,false);
        fragmentComponent().inject(this);
        mPresenter.attachView(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);

        initView();
        initClick();
        setData();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserUI();

        //如果从视频模块中出来，则刷新数据
        if (isToVideo){
            isToVideo = false;
            UserInfoManager.getInstance().getRemoteUserInfo(UserInfoManager.getInstance().getUserId(), null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
    }

    //设置数据
    private void setData(){
        //这里注意修改共通模块的url
        IHeadline.resetMseUrl();
        String extraUrl = "http://iuserspeech." + CommonVars.domain + ":9001/test/ai/";
        IHeadline.setExtraMseUrl(extraUrl);
        String extraMergeUrl = "http://iuserspeech." + CommonVars.domain + ":9001/test/merge/";
        IHeadline.setExtraMergeAudioUrl(extraMergeUrl);

        //增加一个个人中心分享的屏蔽功能，因为微博的key出现了问题
        PersonalHomeManager.enableShare = false;

        binding.groupName.setText(getString(R.string.app_name) + "官方群");
        binding.tvAbout.setText("关于" + getString(R.string.app_name));
    }

    //初始化样式
    private void initView(){
        //隐藏学习按钮
        binding.studyMarkBtn.setVisibility(View.GONE);
        //隐藏分享按钮
        binding.recommendLayout.setVisibility(View.GONE);
        //隐藏青少年模式
        binding.childLock.setVisibility(View.GONE);
        //隐藏检查权限
        binding.setPermission.setVisibility(View.GONE);
        //隐藏我的积分
        binding.myCredits.setVisibility(View.GONE);
        //隐藏好评送书
        binding.sendBooks.setVisibility(View.GONE);
        //隐藏同意隐私政策
        binding.setAgree.setVisibility(View.GONE);
        //隐藏美剧大片
        binding.watchVideo.setVisibility(View.GONE);
        //隐藏更新单词
        binding.setLayout.setVisibility(View.GONE);
        //隐藏激励视频
        binding.adRewardVideo.setVisibility(View.GONE);
        //隐藏小视频
        binding.smallVideo.setVisibility(View.GONE);
        //隐藏广告入口
        binding.adLayout.setVisibility(View.INVISIBLE);
    }

    //初始化点击
    private void initClick() {
        //学习记录
        binding.studyMarkBtn.setOnClickListener(v -> {
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            MeNewActivity.start(getActivity());
        });
        //举报
        binding.complainLayout.setOnClickListener(v -> {
            try {
                Util.startQQ(getActivity(), "572828703");
            } catch (Exception e) {
                showToastShort("您的设备尚未安装QQ客户端，举报功能需要使用QQ");
                e.printStackTrace();
            }
        });
        //关于
        binding.aboutLayout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutActivity.class));
        });
        //头像
        binding.userPic.setOnClickListener(v -> {
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            MeNewActivity.start(getActivity());
        });
        //意见反馈
        binding.feedbackLayout.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), FeedbackActivity.class));
        });
        //推荐给好友
        binding.recommendLayout.setOnClickListener(v->{
            Share.prepareMessage(getActivity());
        });
        //排行榜
        binding.rank.setOnClickListener(v->{
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            startActivity(new Intent(mContext, RankActivity.class));
        });
        //会员中心
        binding.vipCenter.setOnClickListener(v->{
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            startActivity(new Intent(mContext, NewVipCenterActivity.class));
        });
        //官方群
        binding.addIyubaGroup.setOnClickListener(v->{
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            if (configManager.getQQOfficial() > 0) {
                GroupChatManageActivity.start(mContext, configManager.getQQOfficial(), App.APP_NAME_CH + "官方群",true);
            } else {
                GroupChatManageActivity.start(mContext, 9968, "小学英语官方群",true);
            }
        });
        //使用帮助
        binding.helpLayout.setOnClickListener(v->{
            startActivity(HelpUseActivity.buildIntent(getActivity(), false));
        });
        //检查权限
        binding.setPermission.setOnClickListener(v->{
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);
        });
        //隐私政策
        binding.setProtocol.setOnClickListener(v->{
            Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
            mContext.startActivity(intent);
        });
        //用户协议
        binding.setUsage.setOnClickListener(v->{
            Intent intent = WebActivity.buildIntent(mContext, App.Url.USAGE_URL + App.APP_NAME_CH, "使用协议");
            mContext.startActivity(intent);
        });
        //更新服务
        binding.hostUpdateLayout.setOnClickListener(v->{
            showLoadingDialog();
            mPresenter.requestHostUpdate();
        });
        //我的积分
        binding.myCredits.setOnClickListener(v->{
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            Intent intent = new Intent(mContext, CollectionActivity.class);
            startActivity(intent);
        });
        //送书
        binding.sendBooks.setOnClickListener(v->{
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            Intent intent = new Intent(mContext, WordNoteActivity.class);
            startActivity(intent);
        });
        //激励视频
        binding.adRewardVideo.setOnClickListener(v->{
            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            AdContainerActivity.start(getActivity(), AdShowUtil.NetParam.AdShowPosition.show_rewardVideo);
        });
    }

    @Override
    public void showToast(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getActivity(), msg);
            }
        });
    }

    @Override
    public void showLoadingDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(getActivity());
                }
                if (!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
            }
        });
    }

    @Override
    public void hideLoadingDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }
        });
    }

    /****************************************回调操作*****************************/
    //用户更换头像的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserPhotoChangeEvent event){
        LibGlide3Util.loadCircleImg(getActivity(),LibUrlSpellUtil.fixUserPicUrl(UserInfoManager.getInstance().getUserId()),R.drawable.default_avatar,binding.userPic);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginOutEvent event) {
        binding.userPic.setImageResource(R.drawable.default_avatar);
        binding.userName.setText(getString(R.string.login_tip));
    }

    //登录
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        setUserUI();
    }

    //刷新用户名称
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ModifyUserNameEvent event) {
        binding.userName.setText(event.name);
        if (UserInfoManager.getInstance().isVip()) {
            binding.vipImage.setVisibility(View.VISIBLE);
        } else {
            binding.vipImage.setVisibility(View.INVISIBLE);
        }
        binding.userMoney.setText("钱包余额:" + UserInfoManager.getInstance().getMoney() + "元");
    }

    //刷新用户信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserRefreshEvent refreshEvent) {
        setUserUI();
    }

    private void startToLogin() {
        NewLoginUtil.startToLogin(getActivity());
    }

    private void setUserUI() {
        if (UserInfoManager.getInstance().isLogin()) {
            binding.userName.setText(UserInfoManager.getInstance().getUserName());
            if (UserInfoManager.getInstance().isVip()) {
                binding.vipImage.setVisibility(View.VISIBLE);
            } else {
                binding.vipImage.setVisibility(View.INVISIBLE);

            }
            binding.userMoney.setText("钱包余额："+UserInfoManager.getInstance().getMoney()+"元");
            if (NetStateUtil.isConnected(mContext)) {
                LibGlide3Util.loadCircleImg(mContext, LibUrlSpellUtil.fixUserPicUrl(UserInfoManager.getInstance().getUserId()),R.drawable.default_avatar,binding.userPic);
            } else {
                binding.userPic.setImageResource(R.drawable.default_avatar);
            }
        } else {
            binding.userPic.setImageResource(R.drawable.default_avatar);
            binding.userName.setText(getString(R.string.login_tip));
            binding.vipImage.setVisibility(View.INVISIBLE);
            binding.userMoney.setText("");
        }
    }
}
