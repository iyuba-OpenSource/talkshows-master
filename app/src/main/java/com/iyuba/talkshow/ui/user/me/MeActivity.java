//package com.iyuba.talkshow.ui.user.me;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.CompoundButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//
//import com.iyuba.headlinelibrary.HeadlineType;
//import com.iyuba.imooclib.ui.record.PurchaseRecordActivity;
//import com.iyuba.lib_common.data.Constant;
//import com.iyuba.lib_common.util.LibGlide3Util;
//import com.iyuba.lib_user.manager.UserInfoManager;
//import com.iyuba.module.favor.ui.BasicFavorActivity;
//import com.iyuba.module.headlinetalk.ui.mytalk.MyTalkActivity;
//import com.iyuba.module.user.IyuUserManager;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.constant.App;
//import com.iyuba.talkshow.data.manager.ConfigManager;
//import com.iyuba.talkshow.databinding.ActivityMeBinding;
//import com.iyuba.talkshow.event.LoginOutEvent;
//import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
//import com.iyuba.talkshow.ui.deletlesson.LessonDeleteActivity;
//import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
//import com.iyuba.talkshow.ui.user.collect.CollectionActivity;
//import com.iyuba.talkshow.ui.user.download.DownloadActivity;
//import com.iyuba.talkshow.ui.user.login.ChangeNameActivity;
//import com.iyuba.talkshow.ui.user.me.dubbing.MyDubbingActivity;
//import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
//import com.iyuba.talkshow.ui.wallet.WalletHistoryActivity;
//import com.iyuba.talkshow.ui.web.WebActivity;
//import com.iyuba.talkshow.ui.words.WordNoteActivity;
//import com.iyuba.talkshow.util.MD5;
//import com.iyuba.talkshow.util.ToastUtil;
//import com.umeng.analytics.MobclickAgent;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//
//import javax.inject.Inject;
//
//import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;
//import personal.iyuba.personalhomelibrary.ui.message.MessageActivity;
//import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryActivity;
//import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryType;
//
//public class MeActivity extends BaseViewBindingActivity<ActivityMeBinding> implements MeMvpView {
//
//    @Inject
//    MePresenter mPresenter;
//    @Inject
//    ConfigManager configManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setSupportActionBar(binding.toolbar.listToolbar);
//        activityComponent().inject(this);
//        mPresenter.attachView(this);
//        setClick();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(mContext);
//        Log.e("MeActivity", "setDes getUserImageUrl " + mPresenter.getUserImageUrl());
//        /*Glide.with(mContext)
//                .load(mPresenter.getUserImageUrl())
//                .asBitmap()
//                .signature(new StringSignature(System.currentTimeMillis()+""))
//                .transform(new CircleTransform(mContext))
//                .placeholder(R.drawable.default_avatar)
//                .into(binding.meUserImage);*/
//        LibGlide3Util.loadCircleImg(mContext,mPresenter.getUserImageUrl(),R.drawable.default_avatar,binding.meUserImage);
//        if (UserInfoManager.getInstance().isLogin()) {
//            binding.meUsernameTv.setText(UserInfoManager.getInstance().getUserName());
//        }
//        if (UserInfoManager.getInstance().isLogin()) {
//            binding.meUserinfoContainer.setVisibility(View.VISIBLE);
//            binding.meUnloginRl.setVisibility(View.GONE);
//            binding.meLogoutBtn.setVisibility(View.VISIBLE);
////            binding.meClearUserBtn.setVisibility(View.VISIBLE);
//        } else {
//            binding.meUserinfoContainer.setVisibility(View.GONE);
//            binding.meUnloginRl.setVisibility(View.VISIBLE);
//            binding.meLogoutBtn.setVisibility(View.GONE);
////            binding.meClearUserBtn.setVisibility(View.GONE);
//        }
//        if (UserInfoManager.getInstance().isVip()) {
//            binding.meVipstateIv.setImageResource(R.drawable.vip);
//        } else {
//            binding.meVipstateIv.setImageResource(R.drawable.no_vip);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(mContext);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPresenter.detachView();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return true;
//    }
//
//    public void startChangeName() {
//        if (!UserInfoManager.getInstance().isLogin()) {
//            ToastUtil.showToast(mContext, "登录后才能修改用户名。");
//            NewLoginUtil.startToLogin(this);
//            return;
//        }
//        Intent intent  = new Intent(mContext, ChangeNameActivity.class);
//        intent.putExtra("url", "http://m." + Constant.Web.WEB_SUFFIX + "m_login/inputPhonefp.jsp");
//        intent.putExtra("title", "密码修改");
//        startActivity(intent);
//    }
//
//    //点击头像
//    void clickUser() {
//        startActivity(PersonalHomeActivity.buildIntent (this,
//                Integer.parseInt(String.valueOf(UserInfoManager.getInstance().getUserId())),
//                UserInfoManager.getInstance().getUserName(), 0));
//    }
//
//    //消息中心
//    void clickMessage(){
//        startActivity(new Intent(this, MessageActivity.class));//需要登录
//    }
//
//    void clickDownload() {
//        Intent intent = new Intent(mContext, DownloadActivity.class);
//        startActivity(intent);
//    }
//
//    //我的收藏
//    void clickCollect() {
//        Intent intent = new Intent(mContext, CollectionActivity.class);
//        startActivity(intent);
//    }
//
//    //我的配音
//    void clickDubbing() {
//        Intent intent = new Intent(mContext, MyDubbingActivity.class);
//        startActivity(intent);
//    }
//
//    //视频配音
//    void clickVideoDubbing(){
//        UserInfoManager.getInstance().initUserInfo();
//
//        ArrayList<String> types = new ArrayList<>();
//        types.add(HeadlineType.SMALLVIDEO);
//        startActivity(MyTalkActivity.buildIntent(this,types));
//    }
//
//    //我的单词
//    void startMyWord(){
//        Intent intent = new Intent(mContext, WordNoteActivity.class);
//        startActivity(intent);
//    }
//
//    //我的视频收藏
//    void clickVideoCollect(){
//        Intent intent = new Intent(mContext, BasicFavorActivity.class);
//        startActivity(intent);
//    }
//
//    //积分商城
//    void integralClick() {
//        String url = "http://m."+ Constant.Web.WEB_SUFFIX+"mall/index.jsp?"
//                + "&uid=" + UserInfoManager.getInstance().getUserId()
//                + "&sign=" + MD5.getMD5ofStr("iyuba" + UserInfoManager.getInstance().getUserId() + "camstory")
//                + "&username=" + UserInfoManager.getInstance().getUserName()
//                + "&platform=android&appid=" + App.APP_ID;
//
//        startActivity(WebActivity.buildIntent(mContext, url,
//                "积分明细",
//                "http://api."+ Constant.Web.WEB_SUFFIX+"credits/useractionrecordmobileList1.jsp?uid=" + UserInfoManager.getInstance().getUserId())
//        );
//    }
//
//    //微课购买记录
//    void clickMocShopHistory(){
//        startActivity(PurchaseRecordActivity.buildIntent(this));
//    }
//
//    //钱包历史记录
//    void clickWalletHistory(){
//        WalletHistoryActivity.start(this);
//    }
//
//    void clickLogin() {
//        NewLoginUtil.startToLogin(this);
//    }
//
//    void clickLogout() {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_logout, null);
//        TextView remindText = view.findViewById(R.id.remindText);
//        remindText.setText(R.string.logout_alert);
//        AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle(getString(R.string.alert_title))
//                .setView(view)
//                .create();
//        dialog.show();
//        TextView agreeNo = view.findViewById(R.id.text_no_agree);
//        TextView agree = view.findViewById(R.id.text_agree);
//        agreeNo.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//        agree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                mPresenter.loginOut();
//                showToast(R.string.login_out_success);
//                EventBus.getDefault().post(new LoginOutEvent());
//                mContext.finish();
//
//                //个人信息退出
//                IyuUserManager.getInstance().logout();
//            }
//        });
//    }
//
//    /*void clickClearUser() {
//        new AlertDialog.Builder(mContext).setTitle(getString(R.string.alert_title))
//                .setMessage(getString(R.string.clear_user_alert))
//                .setPositiveButton(getString(R.string.alert_btn_ok),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                mPresenter.clearUser();
////                                showToast(R.string.login_out_success);
//                                EventBus.getDefault().post(new LoginOutEvent());
//                                finish();
//                            }
//                        })
//                .setNeutralButton(getString(R.string.alert_btn_cancel),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        }).show();
//    }*/
//
//    void clickVIP() {
//        startActivity(new Intent(mContext, NewVipCenterActivity.class));
//    }
//
//    public void showToast(int resId) {
//        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
//    }
//
//    void setClick(){
////        binding.meUsernameTv.setOnClickListener(v -> startChangeName());
////        binding.meChangeUsername.setOnClickListener(v -> startChangeName());
//        binding.meVipRl.setOnClickListener(v -> clickVIP());
////        binding.meClearUserBtn.setOnClickListener(v -> clickClearUser());
//        binding.meLogoutBtn.setOnClickListener(v -> clickLogout());
//        binding.meLoginBtn.setOnClickListener(v -> clickLogin());
//        binding.integral.setOnClickListener(v -> integralClick());
//        binding.meWordsRl.setOnClickListener(v -> startMyWord());
//        binding.meVideoCollectRl.setOnClickListener(v-> clickVideoCollect());
//        binding.meStudySummary.setOnClickListener(v -> clickStudyReport());
//        binding.meClockData.setOnClickListener(v -> clickClockReport());
//        binding.meDubbingRl.setOnClickListener(v -> clickDubbing());
//        binding.meCollectRl.setOnClickListener(v -> clickCollect());
////        binding.meDownloadRl.setOnClickListener(v -> clickDownload());
//        binding.meMessage.setOnClickListener(v -> clickMessage());
//        binding.meUserImage.setOnClickListener(v -> clickUser());
//        if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
//            binding.userUsage.setVisibility(View.VISIBLE);
//            binding.userUsage.setOnClickListener(v -> clickUsage());
//            binding.userProtocl.setText("隐私政策");
//        } else {
//            binding.userUsage.setVisibility(View.GONE);
//        }
//        binding.userProtocl.setOnClickListener(v -> clickProtocol());
//        binding.userProtocl.setVisibility(View.GONE);
//        binding.userUsage.setVisibility(View.GONE);
//        binding.meChangeUsername.setVisibility(View.GONE);
//        if (App.APP_ID == 280) {
//            binding.meVipRl.setVisibility(View.GONE);
//            binding.meMessage.setVisibility(View.GONE);
//            binding.integral.setVisibility(View.GONE);
//            binding.meDownloadRl.setVisibility(View.VISIBLE);
//            binding.tvDownload.setText("自动跟读提交");
//            binding.downloadCheckbox.setChecked(configManager.isAutoDubbing());
//            binding.downloadCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    configManager.setAutoDubbing(isChecked);
//                }
//            });
//        }
//
//        //增加视频配音
//        binding.meVideoDubbingRl.setOnClickListener(v->{
//            clickVideoDubbing();
//        });
//
//        //增加微课购买记录
//        binding.mocShopHistory.setOnClickListener(v->{
//            clickMocShopHistory();
//        });
//
//        //增加钱包历史记录
//        binding.walletHistory.setOnClickListener(v->{
//            clickWalletHistory();
//        });
//    }
//
//    private void clickUsage() {
//        Intent intent = WebActivity.buildIntent(mContext, App.Url.USAGE_URL + App.APP_NAME_CH, "用户协议");
//        mContext.startActivity(intent);
//    }
//    private void clickProtocol() {
//        if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
//            Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
//            mContext.startActivity(intent);
//        } else {
//            Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL+ App.APP_NAME_CH, "用户隐私协议");
//            mContext.startActivity(intent);
//        }
//    }
//
//    private void startDeleteDownloads() {
//        LessonDeleteActivity.start(this);
//    }
//
//    void clickStudyReport() {
//        if (UserInfoManager.getInstance().isLogin()) {
//            UserInfoManager.getInstance().initUserInfo();
//            String[] types = new String[]{
//                    SummaryType.LISTEN,
////                    SummaryType.WORD,//去掉单词
//                    SummaryType.EVALUATE,
//                    SummaryType.MOOC
//            };
//            startActivity(SummaryActivity.getIntent(mContext, Constant.EVAL_TYPE, types,0));
//        } else {
//            clickLogin();
//        }
//    }
//
//    private void clickClockReport() {
//        if (UserInfoManager.getInstance().isLogin()) {
//            Intent intent = new Intent(mContext, CalendarActivity.class);
//            startActivity(intent);
//        } else {
//            showToastShort("请登录后再进行打卡记录报告");
//            clickLogin();
//        }
//    }
//}
