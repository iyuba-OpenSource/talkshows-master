package com.iyuba.talkshow.ui.main.drawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.talkshow.Constant;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.manager.AccountManager;
import com.iyuba.talkshow.data.manager.ChildLockManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.LoginOutEvent;
import com.iyuba.talkshow.event.ModifyUserNameEvent;
import com.iyuba.talkshow.ui.about.AboutActivity;
import com.iyuba.talkshow.ui.about.SendBookActivity;
import com.iyuba.talkshow.ui.base.BaseFragment;
import com.iyuba.talkshow.ui.child.ChildLockActivity;
import com.iyuba.talkshow.ui.feedback.FeedbackActivity;
import com.iyuba.talkshow.ui.help.HelpUseActivity;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.ui.main.video.SmallVideoActivity;
import com.iyuba.talkshow.ui.rank.RankActivity;
import com.iyuba.talkshow.ui.user.collect.CollectionActivity;
import com.iyuba.talkshow.ui.user.login.LoginActivity;
import com.iyuba.talkshow.ui.user.me.dubbing.MyDubbingActivity;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.words.WordNoteActivity;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.talkshow.util.Util;
import com.iyuba.talkshow.util.glide.CircleTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import personal.iyuba.personalhomelibrary.ui.groupChat.GroupChatManageActivity;
import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;

import com.iyuba.talkshow.ui.widget.LoadingDialog;

import com.iyuba.headlinelibrary.IHeadline;
import personal.iyuba.personalhomelibrary.PersonalHomeManager;
import personal.iyuba.personalhomelibrary.event.UserPhotoChangeEvent;
import com.iyuba.module.commonvar.CommonVars;
import com.iyuba.talkshow.util.login.LoginUtil;
import com.mob.secverify.SecVerify;

/**
 * Created by Administrator on 2016/12/24/024.
 */

public class SlidingMenuFragment extends BaseFragment implements SlidingMenuMvpView {
    @Inject
    SlidingMenuPresenter mPresenter;

    @BindView(R.id.user_image)
    ImageView userImageView;
    @BindView(R.id.username_textview)
    TextView usernameTextView;
    @BindView(R.id.watch_video)
    RelativeLayout watch_video;
    @BindView(R.id.set_layout)
    RelativeLayout words;
    @BindView(R.id.tv_complain)
    TextView complain;
    @BindView(R.id.tv_about)
    TextView about;
    @BindView(R.id.group_name)
    TextView groupName;
    @BindView(R.id.add_iyuba_group)
    RelativeLayout iyuGroup;

    @BindView(R.id.my_credits)
    RelativeLayout credits;
    @BindView(R.id.recommend_layout)
    RelativeLayout recommendLayout;
    @BindView(R.id.help_layout)
    View help_layout;
    @BindView(R.id.set_permission)
    RelativeLayout setPermission;
    @BindView(R.id.agree_checkbox)
    CheckBox agreeCheckbox;
    @BindView(R.id.set_agree)
    RelativeLayout setAgree;
    @BindView(R.id.set_protocol)
    RelativeLayout setProtocol;
    @BindView(R.id.set_usage)
    RelativeLayout setUsage;
    @Inject
    ConfigManager configManager;
    @Inject
    AccountManager mAccountManager;

    //小视频
    @BindView(R.id.small_video)
    RelativeLayout smallVideo;
    //青少年模式
    @BindView(R.id.child_lock)
    RelativeLayout childLock;

    //加载弹窗
    private LoadingDialog loadingDialog;
    //服务更新
    @BindView(R.id.hostUpdate_layout)
    RelativeLayout hostUpdate;

    public static SlidingMenuFragment newInstance() {
        return new SlidingMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sliding_menu, container, false);
        ButterKnife.bind(this, root);
        fragmentComponent().inject(this);
        mPresenter.attachView(this);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setUserUI();
        EventBus.getDefault().register(this);
//        help_layout.setVisibility(View.GONE);
        watch_video.setVisibility(View.GONE);
//        iyuGroup.setVisibility(View.GONE);
        groupName.setText(getString(R.string.app_name) + "官方群");
        about.setText("关于" + getString(R.string.app_name));
        if (App.APP_HUAWEI_COMPLAIN) {
            complain.setText(getString(R.string.about_complain));
            words.setVisibility(View.VISIBLE);
            words.setOnClickListener(v -> {
                Util.startQQ(mContext, "572828703");
            });
        }
        credits.setVisibility(View.GONE);
        if (App.APP_SHARE_HIDE > 0) {
            recommendLayout.setVisibility(View.GONE);
        }
        if (App.APP_CHECK_PERMISSION) {
            setPermission.setVisibility(View.VISIBLE);
        } else {
            setPermission.setVisibility(View.GONE);
        }
        if (App.APP_CHECK_AGREE) {
            setAgree.setVisibility(View.VISIBLE);
            agreeCheckbox.setChecked(configManager.isCheckAgree());
            agreeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        configManager.setCheckAgree(true);
                        showToastShort("您已经同意使用协议及隐私政策，请正常使用本应用。");
                        TalkShowApplication.initUMMob();
                    } else {
                        new AlertDialog.Builder(mContext).setTitle(getString(R.string.alert_title))
                                .setMessage("如果您不同意使用协议及隐私政策，本应用使用功能受限。")
                                .setPositiveButton(getString(R.string.permission_cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                configManager.setCheckAgree(false);
                                                agreeCheckbox.setChecked(configManager.isCheckAgree());
                                            }
                                        })
                                .setNeutralButton(getString(R.string.permission_ok),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                agreeCheckbox.setChecked(configManager.isCheckAgree());
                                            }})
                                .setCancelable(false)
                                .show();
                    }
                }
            });
        } else {
            setAgree.setVisibility(View.GONE);
        }

        //更新服务
        hostUpdate.setVisibility(View.VISIBLE);
        //这里注意修改共通模块的url
        IHeadline.resetMseUrl();
        String extraUrl = "http://iuserspeech."+CommonVars.domain+":9001/test/ai/";
        IHeadline.setExtraMseUrl(extraUrl);
        String extraMergeUrl = "http://iuserspeech."+CommonVars.domain+":9001/test/merge/";
        IHeadline.setExtraMergeAudioUrl(extraMergeUrl);

        if (mAccountManager.checkLogin()){
            //这里增加微课是否展示的信息
            mAccountManager.controlMocShow(String.valueOf(mAccountManager.getUid()));
        }

        //小视频
        smallVideo.setVisibility(View.VISIBLE);

        //关闭个人中心和视频模块的分享
        PersonalHomeManager.enableShare = false;
        IHeadline.setEnableShare(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.about_layout)
    void clickAbout() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    //头像-跳转到个人中心
    @OnClick(R.id.user_image)
    void clickImage() {
        if (mAccountManager.checkLogin()) {
            mAccountManager.initPersonHome();

            startActivity(PersonalHomeActivity.buildIntent(mContext,
                    mAccountManager.getUid(), mAccountManager.getUserName(), 0));
        } else {
            startToLogin();
        }
    }

    //个人中心-跳转到我的界面
    @OnClick(R.id.person_center_btn)
    void clickHomeButton() {
        if (mAccountManager.checkLogin()) {
            startActivity(new Intent(mContext, MeActivity.class));
        } else {
            startToLogin();
        }
    }

    @OnClick(R.id.feedback_layout)
    void clickFeedback() {
        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    @OnClick(R.id.recommend_layout)
    void clickRecommend() {
        Share.prepareMessage(getActivity());
    }

    @OnClick(R.id.rank)
    void rank() {
        if (App.APP_CHECK_AGREE) {
            if (!configManager.isCheckAgree()) {
                ToastUtil.showToast(mContext, "选中下方的同意使用协议及隐私政策才能使用此功能");
                return;
            }
        }
        if (mPresenter.checkLogin()) {
            startActivity(new Intent(mContext, RankActivity.class));
        } else {
            startToLogin();
        }
    }

    @OnClick(R.id.rr)
    void rr() {
        return;
    }

    @OnClick(R.id.vip_center)
    void vip_center() {
        if (App.APP_CHECK_AGREE) {
            if (!configManager.isCheckAgree()) {
                ToastUtil.showToast(mContext, "选中下方的同意使用协议及隐私政策才能使用此功能");
                return;
            }
        }
        if (mPresenter.checkLogin()) {
            startActivity(new Intent(mContext, NewVipCenterActivity.class));
        } else {
            startToLogin();
        }
    }

    @OnClick(R.id.add_iyuba_group)
    void iyuba_group() {
        if (App.APP_CHECK_AGREE) {
            if (!configManager.isCheckAgree()) {
                ToastUtil.showToast(mContext, "选中下方的同意使用协议及隐私政策才能使用此功能");
                return;
            }
        }
        if (mAccountManager.checkLogin()) {
            //增加群组的举报功能
            //因为这个版本的个人中心模块问题，举报功能的api不能使用了，等下个版本解决
//            PersonalHomeManager.UserComplain = true;

            GroupChatManageActivity.start(mContext, 9930, getString(R.string.iyuba_group),true);
//            GroupChatManageActivity.start(mContext, 0, getString(R.string.iyuba_group), true);
        } else {
            startToLogin();
        }
    }

    @OnClick(R.id.help_layout)
    void clickHelp() {
        startActivity(HelpUseActivity.buildIntent(getActivity(), false));
    }

    @OnClick(R.id.set_permission)
    void clickPermission() {
        Log.e("SlidingMenuFragment", "clickPermission " + "package:" + mContext.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }

    @OnClick(R.id.set_protocol)
    void clickProtocol() {
        Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
        mContext.startActivity(intent);
    }

    @OnClick(R.id.set_usage)
    void clickUsage() {
        Intent intent = WebActivity.buildIntent(mContext, App.Url.USAGE_URL + App.APP_NAME_CH, "使用协议");
        mContext.startActivity(intent);
    }

    //更新服务
    @OnClick(R.id.hostUpdate_layout)
    void clickHostUpdate(){
        showLoadingDialog();
        mPresenter.requestHostUpdate();
    }

    //小视频
    @OnClick(R.id.small_video)
    void clickSmallVideo(){
        if (mAccountManager.checkLogin()){
            //这里判断是否为青少年模式
            if (ChildLockManager.getInstance().isChildLock(getActivity(),false)){
                IHeadline.setEnableShare(false);
            }else {
                IHeadline.setEnableShare(true);
            }

            SmallVideoActivity.start(getActivity());
        }else {
            startToLogin();
        }
    }

    //青少年模式
    @OnClick(R.id.child_lock)
    void clickChildLock(){
        ChildLockActivity.start(getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginOutEvent event) {
        userImageView.setImageResource(R.drawable.default_avatar);
        usernameTextView.setText(getString(R.string.login_tip));

        //将数据回调出去，关闭微课
        EventBus.getDefault().post(new MocShowResponse());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        usernameTextView.setText(MessageFormat.format(getString(R.string.welcome_back),
                mPresenter.getUser().getUsername())
        );
        if (NetStateUtil.isConnected(getActivity())) {
            Glide.clear(userImageView);
            Glide.with(mContext)
                    .load(mPresenter.getUserImageUrl())
                    .asBitmap()
                    .signature(new StringSignature(System.currentTimeMillis() + ""))
                    .transform(new CircleTransform(mContext))
                    .placeholder(R.drawable.default_avatar)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userImageView);

            //这里加载一遍微课的显示数据
            mAccountManager.controlMocShow(String.valueOf(mAccountManager.getUid()));
        } else {
            userImageView.setImageResource(R.drawable.default_avatar);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ModifyUserNameEvent event) {
        usernameTextView.setText(
                MessageFormat.format(getString(R.string.welcome_back), event.name));
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserUI();
    }

    private void setUserUI() {
        if (mPresenter.checkLogin()) {
            usernameTextView.setText(
                    MessageFormat.format(getString(R.string.welcome_back),
                            mPresenter.getUser().getUsername()));
            if (NetStateUtil.isConnected(getActivity())) {
                Glide.clear(userImageView);
                Glide.with(mContext)
                        .load(mPresenter.getUserImageUrl())
                        .asBitmap()
                        .signature(new StringSignature(System.currentTimeMillis() + ""))
                        .transform(new CircleTransform(mContext))
                        .placeholder(R.drawable.default_avatar)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(userImageView);
            } else {
                userImageView.setImageResource(R.drawable.default_avatar);
            }
        } else {
            userImageView.setImageResource(R.drawable.default_avatar);
            usernameTextView.setText(getString(R.string.login_tip));
        }
    }


    @OnClick(R.id.send_books)
    public void onSendBooksClicked() {
        startActivity(new Intent(mContext, SendBookActivity.class));
    }

    @OnClick(R.id.watch_video)
    public void onWatchVideoClicked() {
//        startActivity(new Intent(mContext, MovieActivity.class));
    }

    @Override
    public void showToast(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getActivity(),msg);
            }
        });
    }

    @Override
    public void showLoadingDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog==null){
                    loadingDialog = new LoadingDialog(getActivity());
                }
                if (!loadingDialog.isShowing()){
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
                if (loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    //用户更换头像的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserPhotoChangeEvent event){
        Glide.clear(userImageView);
        Glide.with(mContext)
                .load(mPresenter.getUserImageUrl())
                .asBitmap()
                .signature(new StringSignature(System.currentTimeMillis() + ""))
                .transform(new CircleTransform(mContext))
                .placeholder(R.drawable.default_avatar)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(userImageView);
    }

    private void startToLogin(){
        LoginUtil.login(getActivity(), NewLoginActivity.WECHAT_SMALL,null);
    }
}
