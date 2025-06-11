package com.iyuba.talkshow.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.iyuba.headlinelibrary.event.HeadlineGoVIPEvent;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivityNew;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivityNew;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;
import com.iyuba.talkshow.Constant;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.manager.AccountManager;
import com.iyuba.talkshow.data.manager.ChildLockManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.AdNativeResponse;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.LoginResult;
import com.iyuba.talkshow.event.PayEvent;
import com.iyuba.talkshow.ui.about.AboutActivity;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.child.ChildLockActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.main.drawer.SlidingMenuFragment;
import com.iyuba.talkshow.ui.search.SearchActivity;
import com.iyuba.talkshow.ui.sign.SignActivity;
import com.iyuba.talkshow.ui.user.login.LoginActivity;
import com.iyuba.talkshow.ui.user.register.submit.RegisterSubmitActivity;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.SendBookPop;
import com.iyuba.talkshow.ui.widget.divider.MainGridItemDivider;
import com.iyuba.talkshow.util.AdInfoFlowUtil;
import com.iyuba.talkshow.util.DialogFactory;
import com.iyuba.talkshow.util.NetStateUtil;
import com.mob.secverify.OAuthPageEventCallback;
import com.mob.secverify.PreVerifyCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.VerifyCallback;
import com.mob.secverify.common.exception.VerifyException;
import com.mob.secverify.datatype.VerifyResult;
import com.mob.secverify.exception.VerifyErr;
import com.mob.secverify.ui.component.CommonProgressDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.youdao.sdk.nativeads.NativeResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import personal.iyuba.personalhomelibrary.PersonalHome;
import personal.iyuba.personalhomelibrary.data.model.HeadlineTopCategory;
import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class MainActivity extends BaseActivity implements MainMvpView {
    public static final String TAG = "MainActivity";
    private static final int REQUEST_SDCARD_PERMISSION = 1;
    private static final int LOCATION_PERMISSION = 2;

    public static final int DONG_MAN_SIZE = 4;
    public static final int TING_GE_SIZE = 4;
    public static final int SPAN_COUNT = 2;
    private boolean ifresh = false;

    int pageNum = 1;

    AdInfoFlowUtil adInfoFlowUtil;

    @Inject
    MainPresenter mMainPresenter;
    @Inject
    VoaListAdapter mVoaListAdapter;
    @Inject
    AccountManager mAccountManager;
    @Inject
    ConfigManager configManager;

    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mSwipeMenuRecyclerView;
    @BindView(R.id.reload)
    View mReloadView;
    private int defaultUi = 1;
    private long starttime;
    ActionBarDrawerToggle mDrawerToggle;
    private SendBookPop sendBookPop;

    //是否需要处理马甲包
    private boolean isVestBagCheck = false;

    VoaListAdapter.VoaCallback voaCallback = new VoaListAdapter.VoaCallback() {
        @Override
        public void onVoaClick(Voa voa) {
            Log.e("VoaListAdapt", "voaCallback  点击了");
            startDetailActivity(voa);
        }
    };
    VoaListAdapter.LoopCallback loopCallback = new VoaListAdapter.LoopCallback() {
        @Override
        public void onLoopClick(int voaId) {
            mMainPresenter.getVoa4Id(voaId);
        }
    };
    VoaListAdapter.DataChangeCallback adapterDataRefreshCallback = new VoaListAdapter.DataChangeCallback() {
        @Override
        public void onClick(View v, CategoryFooter category, int limit, String ids) {
            mMainPresenter.flag = 1;
            mMainPresenter.loadMoreVoas(category, limit, ids);
        }
    };
    View.OnClickListener dailyBonusCallback = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mAccountManager.checkLogin()) {
                startActivity(new Intent(mContext, SignActivity.class));
            } else {
                showToast("请登录后再进行打卡");
//                startActivity(new Intent(mContext, LoginActivity.class));
                LoginUtil.toLogin(this);
            }
        }
    };
    private boolean isFirst;
    private VoaListAdapter.CourseChooseCallback coursechooseCallback = new VoaListAdapter.CourseChooseCallback() {
        @Override
        public void onClickCourse() {
//            mContext.startActivity(SeriesActivity.getIntent(mContext, "201", App.DEFAULT_SERIESID));
            if (mAccountManager.checkLogin()) {
                mAccountManager.initMocUser();
                ArrayList<Integer> typeIdFilter = new ArrayList<>();
                typeIdFilter.add(3);
                typeIdFilter.add(27);
                startActivity(MobClassActivity.buildIntent(mContext, 3, true, typeIdFilter));
            } else {
                showToast("请登录后再进行微课学习");
                LoginUtil.toLogin(this);
            }
//            if (configManager.getCourseId() == 0) {
//                CourseChooseActivity.start(mContext, OpenFlag.TO_DETAIL);
//            } else {
//                mContext.startActivity(CourseDetailActivity.buildIntent(mContext, configManager.getCourseId(), configManager.getCourseTitle()));
//            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        ifresh = false;
    }

    @Override
    public boolean isSwipeBackEnable() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mMainPresenter.attachView(this);
        setSupportActionBar(mToolbar);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        if (!App.APP_TENCENT_PRIVACY) {
            showFirstDialog();
        }
        preVerify();
        initPersonal();
        initSlidingMenu();
        if (new Random().nextInt(300) <= 25) {
            mSwipeMenuRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        showSendBookDialog();
                    } catch (Exception var2) { }
                }
            }, 500);
        }
        if (mAccountManager.getUser() != null) {
            IMoviesApp.setUser(mAccountManager.getUser().getUid() + "", mAccountManager.getUser().getVipStatus() + "");
        }
        mDrawerToggle.syncState();

        //马甲包判断处理(vivo版本)
        if (App.APP_SAME_CHECK&&VestBagUtil.getInstance().getCheck()){
            isVestBagCheck = true;
        }

        if (isVestBagCheck){
            mSwipeRefreshLayout.setEnableRefresh(false);
            mSwipeRefreshLayout.setEnableLoadMore(false);
            mMainPresenter.getSeriesList(App.DEFAULT_SERIESID);
            return;
        }

        mSwipeRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
//        mSwipeRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
//        mMainPresenter.getVoas();
        mMainPresenter.loadLoop();
        mMainPresenter.getSeriesList(App.DEFAULT_SERIESID);
        mMainPresenter.getVoa4Category();
        mMainPresenter.checkVersion();
        mVoaListAdapter.setVoaCallback(voaCallback);
        mVoaListAdapter.setLoopCallback(loopCallback);
        mVoaListAdapter.setDataChangeCallback(adapterDataRefreshCallback);
        mVoaListAdapter.setCourseClickCallback(coursechooseCallback);
        mVoaListAdapter.setDailyBonusCallback(dailyBonusCallback);
        mVoaListAdapter.setMobCallback(mobCallback);
        mSwipeRefreshLayout.setOnRefreshListener(refreshLayout -> refreshData());
//        mSwipeRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreVoas());
        mSwipeMenuRecyclerView.setAdapter(mVoaListAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mVoaListAdapter.getItemList().get(position) instanceof SeriesData ? 1 : 2;
            }
        });
        mSwipeMenuRecyclerView.addItemDecoration(new MainGridItemDivider(this));
        mSwipeMenuRecyclerView.setLayoutManager(layoutManager);
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。

        //显示青少年模式
        showChildLock();
    }

    private void initPersonal() {
        PersonalHome.setMainPath("com.iyuba.talkshow.ui.main.MainActivity");
        try {
            mAccountManager.initPersonHome();
            mAccountManager.initMocUser();
        } catch (Exception var) {}
    }

    private void showFirstDialog() {
        isFirst = configManager.isFirstStart();
        Log.d("com.iyuba.talkshow", "showFirstDialog: " + isFirst);
        if (isFirst) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
                        Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, "用户隐私协议");
                        mContext.startActivity(intent);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                    ds.setUnderlineText(true);
                }
            };
//            if (getIntent().getExtras().getBoolean("showDialog",false)){
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_privacy, null);
            TextView remindText = view.findViewById(R.id.remindText);
            String remindString = getResources().getString(R.string.user_protocol);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(remindString);
            if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
                spannableStringBuilder.setSpan(clickableSpan, remindString.indexOf(App.APP_NAME_PRIVACY), remindString.indexOf(App.APP_NAME_PRIVACY) + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ClickableSpan clickableUsage = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = WebActivity.buildIntent(mContext, App.Url.USAGE_URL + App.APP_NAME_CH, "用户协议");
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(getResources().getColor(R.color.colorPrimary));
                        ds.setUnderlineText(true);
                    }
                };
                spannableStringBuilder.setSpan(clickableUsage, remindString.indexOf("用户协议"), remindString.indexOf("用户协议") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannableStringBuilder.setSpan(clickableSpan, remindString.indexOf("用户协议"), remindString.indexOf("用户协议") + 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            remindText.setText(spannableStringBuilder);
            remindText.setMovementMethod(LinkMovementMethod.getInstance());

            AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("个人信息保护政策")
                    .setView(view)
                    .setCancelable(false)
                    .create();
            dialog.show();
            TextView agreeNo = view.findViewById(R.id.text_no_agree);
            TextView agree = view.findViewById(R.id.text_agree);
            agreeNo.setOnClickListener(v -> ToastUtils.showShort("请同意~"));
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    configManager.setFirstStart(false);
                    TalkShowApplication.initUMMob();
                }
            });
        } else {
            TalkShowApplication.initUMMob();
        }
    }

    private void loadMoreVoas() {
        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            showToast(R.string.please_check_network);
            dismissRefreshingView();
            return;
        }
        pageNum += 1;
        mMainPresenter.loadMoreChildNews(pageNum);
    }

    private void refreshData() {
        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            showToast(R.string.please_check_network);
            dismissRefreshingView();
            return;
        }
        mMainPresenter.syncVoa4Category(App.DEFAULT_SERIESID);
        mMainPresenter.syncSeries(App.DEFAULT_SERIESID);
        pageNum = 1;
        Log.e("onrefresh", "刷新了！");
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.reset();
        }
//        mMainPresenter.loadVoas();
        mMainPresenter.loadLoop();
        ifresh = true;
    }

    private void showSendBookDialog() {
//        sendBookPop = new SendBookPop(this, findViewById(R.id.container));
//        new AlertDialog.Builder(this).setMessage("现在给应用好评可以获得由爱语吧名师团队编写的电子书哦,先到先得~~")
//                .setNegativeButton("暂不考虑", null)
//                .setPositiveButton("去好评", (dialog, which) -> startActivity(new Intent(mContext, SendBookActivity.class))).setCancelable(false).show();
    }

    @OnClick(R.id.reload)
    public void onClickReload() {
        mMainPresenter.loadVoas();
        mMainPresenter.loadLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adInfoFlowUtil == null) {
            try {
                adInfoFlowUtil = new AdInfoFlowUtil(mContext, true, new AdInfoFlowUtil.Callback() {
                    @Override
                    public void onADLoad(List ads) {
                        try {
                            AdNativeResponse nativeResponse = new AdNativeResponse();
                            nativeResponse.setNativeResponse((NativeResponse) ads.get(0));
                            if (!mAccountManager.isVip()) {
                                mVoaListAdapter.setAd(nativeResponse);
                            } else {
                                Log.d("com.iyuba.talkshow", "onADLoad: is vip");
                                mVoaListAdapter.removeAd();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setAdRequestSize(1);
            } catch (Exception var2) { }
        }
        if (!mAccountManager.isVip()) {

        } else {
            Log.d("com.iyuba.talkshow", "onADLoad: is vip");
            mVoaListAdapter.removeAd();
        }
        MobclickAgent.onResume(this);
    }

    public void onRequestSDCardSuccess() {
        mMainPresenter.getWelcomePic();
    }

    public void onRequestSDCardFail() {
        // TODO
    }

    void onLocationGranted() {
        mMainPresenter.login();
    }

    void onLocationDenied() {
        mMainPresenter.loginWithoutLocation();
    }

    @Subscribe
    public void paySuccess(PayEvent event) {
        mMainPresenter.loginWithoutLocation();
    }

    private void initSlidingMenu() {
        SlidingMenuFragment menuFragment = SlidingMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_menu_drawer, menuFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.search:
                startActivity(new Intent(mContext, SearchActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ifresh = true;
    }

    @Override
    public void showAlertDialog(String msg, DialogInterface.OnClickListener ocl) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.alert_title);
        alert.setMessage(msg);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.alert_btn_ok), ocl);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.alert_btn_cancel),
                (dialog, which) -> {
                });
        alert.show();
    }

    @Override
    public void startAboutActivity(String versionCode, String appUrl) {
        Intent intent = AboutActivity.buildIntent(this, versionCode, appUrl);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mMainPresenter.detachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showVoas(List<Voa> voas) {
        mVoaListAdapter.setVoas(voas);
        mVoaListAdapter.notifyDataSetChanged();
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.refreshAd();
        }
    }

    @Override
    public void showVoasByCategory(List<Voa> voas, CategoryFooter category) {
        mVoaListAdapter.setVoasByCategory(voas, category);
    }

    @Override
    public void setChoise(List<SeriesData> list) {

        //处理马甲包
        if (isVestBagCheck){
            VestBagAdapter adapter = new VestBagAdapter(this,list);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            mSwipeMenuRecyclerView.setLayoutManager(manager);
            mSwipeMenuRecyclerView.setAdapter(adapter);
            return;
        }

        mVoaListAdapter.setSeries(list);
        mVoaListAdapter.notifyDataSetChanged();
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.refreshAd();
        }
    }

    @Override
    public void showMoreVoas(List<Voa> voas) {
        mVoaListAdapter.addVoas(voas);
        mVoaListAdapter.notifyDataSetChanged();
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.refreshAd();
        }
    }

    @Override
    public void showVoasEmpty() {
        mVoaListAdapter.setVoas(Collections.<Voa>emptyList());
        mVoaListAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_content, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading)).show();
    }

    @Override
    public void setBanner(List<LoopItem> loopItemList) {
        mVoaListAdapter.setBanner(loopItemList);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startDetailActivity(Voa voa) {
        if (voa != null) {
            Log.e("startDetailAcivity", "执行了");
            if (!ifresh) {
                //这里需要限制
//                startActivity(DetailActivity.buildIntent(this, voa, true));
                startActivity(DetailActivity.buildIntentLimit(this, voa, true));
            }
            ifresh = false;
        } else {
            Log.e("startDetailAcivity", "Voa is null.");
        }
    }

    @Override
    public void showReloadView() {
        mReloadView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissReloadView() {
        mReloadView.setVisibility(View.GONE);
    }

    @Override
    public void dismissRefreshingView() {
        mSwipeRefreshLayout.finishRefresh();
        mSwipeRefreshLayout.finishLoadMore();
    }

    private boolean isExit = false;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        if (isExit) {
            finish();
        } else {
            isExit = true;
            showToast(getString(R.string.one_more_exit));
            Timer timber = new Timer();
            timber.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 3000);
        }
    }

    VoaListAdapter.MobCallback mobCallback = new VoaListAdapter.MobCallback() {
        @Override
        public void onMobCheck() {
            LoginUtil.toLogin(this);
        }
    };

    public void clickLogin() {
        boolean isVerifySupport = SecVerify.isVerifySupport();
        Log.e(TAG, "SecVerify.isVerifySupport()  " + isVerifySupport);
        if (isVerifySupport && Constant.User.isPreVerifyDone) {
            verify();
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    }
    /**
     * 建议提前调用预登录接口，可以加快免密登录过程，提高用户体验
     */
    private void preVerify() {
        //设置在1000-10000之内
//        SecVerify.setTimeOut(5000);
        //移动的debug tag 是CMCC-SDK,电信是CT_ 联通是PriorityAsyncTask
        SecVerify.setDebugMode(true);
//        SecVerify.setUseCache(true);
        SecVerify.preVerify(new PreVerifyCallback() {
            @Override
            public void onComplete(Void data) {
                if (Constant.User.devMode) {
                    Toast.makeText(mContext, "预登录成功", Toast.LENGTH_LONG).show();
                }
                Constant.User.isPreVerifyDone = true;
                Log.e(TAG, "onComplete.isPreVerifyDone  " + Constant.User.isPreVerifyDone);
                SecVerify.autoFinishOAuthPage(false);
//                SecVerify.setUiSettings(CustomizeUtils.customizeUi());
            }

            @Override
            public void onFailure(VerifyException e) {
                Constant.User.isPreVerifyDone = false;
                Log.e(TAG, "onFailure.isPreVerifyDone  " + Constant.User.isPreVerifyDone);
                String errDetail = null;
                if (e != null){
                    errDetail = e.getMessage();
                }
                Log.e(TAG, "onFailure errDetail " + errDetail);
                if (Constant.User.devMode) {
                    // 登录失败
                    Log.e(TAG, "preVerify failed", e);
                    // 错误码
                    int errCode = e.getCode();
                    // 错误信息
                    String errMsg = e.getMessage();
                    // 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
                    String msg = "错误码: " + errCode + "\n错误信息: " + errMsg;
                    if (!TextUtils.isEmpty(errDetail)) {
                        msg += "\n详细信息: " + errDetail;
                    }
                    Log.e(TAG,msg);
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 免密登录
     */
    private void verify() {
        Log.e(TAG, "Verify called");
        CommonProgressDialog.showProgressDialog(mContext);
        //需要在verify之前设置
        SecVerify.OtherOAuthPageCallBack(new OAuthPageEventCallback() {
            @Override
            public void initCallback(OAuthPageEventResultCallback cb) {
                cb.pageOpenCallback(new PageOpenedCallback() {
                    @Override
                    public void handle() {
                        Log.i(TAG, System.currentTimeMillis() + " pageOpened");
                        Log.e(TAG,(System.currentTimeMillis() - starttime) + "ms is the time pageOpen take ");
                    }
                });
                cb.loginBtnClickedCallback(new LoginBtnClickedCallback() {
                    @Override
                    public void handle() {
                        Log.i(TAG, System.currentTimeMillis() + " loginBtnClicked");
                    }
                });
                cb.agreementPageClosedCallback(new AgreementPageClosedCallback() {
                    @Override
                    public void handle() {
                        Log.i(TAG, System.currentTimeMillis() + " agreementPageClosed");
                    }
                });
                cb.agreementPageOpenedCallback(new AgreementClickedCallback() {
                    @Override
                    public void handle() {
                        Log.i(TAG, System.currentTimeMillis() + " agreementPageOpened");
                    }
                });
                cb.cusAgreement1ClickedCallback(new CusAgreement1ClickedCallback() {
                    @Override
                    public void handle() {
                        Log.i(TAG, System.currentTimeMillis() + " cusAgreement1ClickedCallback");
                    }
                });
                cb.cusAgreement2ClickedCallback(new CusAgreement2ClickedCallback() {
                    @Override
                    public void handle() {
                        Log.i(TAG, System.currentTimeMillis() + " cusAgreement2ClickedCallback");
                    }
                });
                cb.checkboxStatusChangedCallback(new CheckboxStatusChangedCallback() {
                    @Override
                    public void handle(boolean b) {
                        Log.i(TAG,System.currentTimeMillis() + " current status is " + b);
                    }
                });
                cb.pageCloseCallback(new PageClosedCallback() {
                    @Override
                    public void handle() {
                        Log.i(TAG, System.currentTimeMillis() + " pageClosed");
                        CommonProgressDialog.dismissProgressDialog();
                    }
                });
            }
        });
        starttime = System.currentTimeMillis();
        SecVerify.verify(new VerifyCallback() {
            @Override
            public void onOtherLogin() {
                CommonProgressDialog.dismissProgressDialog();
                // 用户点击“其他登录方式”，处理自己的逻辑
                Log.e(TAG, "onOtherLogin called");
                startActivity(new Intent(mContext, LoginActivity.class));
            }
            @Override
            public void onUserCanceled() {
                CommonProgressDialog.dismissProgressDialog();
                SecVerify.finishOAuthPage();
                // 用户点击“关闭按钮”或“物理返回键”取消登录，处理自己的逻辑
                Log.e(TAG, "onUserCanceled called");
            }
            @Override
            public void onComplete(VerifyResult data) {
                // 获取授权码成功，将token信息传给应用服务端，再由应用服务端进行登录验证，此功能需由开发者自行实现
                Log.e(TAG, "onComplete called");
                tokenToPhone(data);
            }
            @Override
            public void onFailure(VerifyException e) {
                CommonProgressDialog.dismissProgressDialog();
                //TODO处理失败的结果
                Log.e(TAG, "onFailure called");
                startActivity(new Intent(mContext, LoginActivity.class));
                if (Constant.User.devMode) {
                    showExceptionMsg(e);
                }
            }
        });
    }

    private void tokenToPhone(VerifyResult data) {
        CommonProgressDialog.dismissProgressDialog();
        if (data != null) {
            Log.e(TAG, "data.getOperator()  " + data.getOperator());
            Log.e(TAG, "data.getOpToken()  " + data.getOpToken());
            Log.e(TAG, "data.getToken()  " + data.getToken());
            // 获取授权码成功，将token信息传给应用服务端，再由应用服务端进行登录验证，此功能需由开发者自行实现
            CommonProgressDialog.showProgressDialog(mContext);
            mMainPresenter.registerToken(data.getToken(), data.getOpToken(), data.getOperator());
        } else {
            Log.e(TAG, "tokenToPhone data is null.");
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    public void showExceptionMsg(VerifyException e) {
        // 登录失败
        if (defaultUi == 1) {
            //失败之后不会自动关闭授权页面，需要手动关闭
            SecVerify.finishOAuthPage();
        }
        CommonProgressDialog.dismissProgressDialog();
        Log.e(TAG, "verify failed", e);
        // 错误码
        int errCode = e.getCode();
        // 错误信息
        String errMsg = e.getMessage();
        // 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
        Throwable t = e.getCause();
        String errDetail = null;
        if (t != null) {
            errDetail = t.getMessage();
        }

        String msg = "错误码: " + errCode + "\n错误信息: " + errMsg;
        if (!TextUtils.isEmpty(errDetail)) {
            msg += "\n详细信息: " + errDetail;
        }
        if (!Constant.User.devMode) {
            msg = "当前网络不稳定";
            if (errCode == VerifyErr.C_NO_SIM.getCode()
                    || errCode == VerifyErr.C_UNSUPPORTED_OPERATOR.getCode()
                    || errCode == VerifyErr.C_CELLULAR_DISABLED.getCode()) {
                msg = errMsg;
            }
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goResultActivity(LoginResult data) {
        if (data == null) {
            Log.e(TAG, "goResultActivity LoginResult is null. ");
            Toast.makeText(mContext, "请使用用户名密码登录。", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, LoginActivity.class));
        } else if (!TextUtils.isEmpty(data.getPhone())) {
            String randNum = "" + System.currentTimeMillis();
            String user = "iyuba" + randNum.substring(randNum.length() - 4) + data.getPhone().substring(data.getPhone().length() - 4);
            String pass = data.getPhone().substring(data.getPhone().length() - 6);
            Log.e(TAG, "goResultActivity.user  " + user);
            Intent intent = new Intent(mContext, RegisterSubmitActivity.class);
            intent.putExtra(RegisterSubmitActivity.PhoneNum, data.getPhone());
            intent.putExtra(RegisterSubmitActivity.UserName, user);
            intent.putExtra(RegisterSubmitActivity.PassWord, pass);
            intent.putExtra(RegisterSubmitActivity.RegisterMob, 1);
            startActivity(intent);
        } else {
            Log.e(TAG, "goResultActivity LoginResult is ok. ");
        }
        SecVerify.finishOAuthPage();
        CommonProgressDialog.dismissProgressDialog();
    }

    //登录响应
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        Log.e(TAG, "onEvent LoginEvent");
        initPersonal();
    }

    //微课跳转黄金会员购买
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyVIPEvent event) {
        Log.e(TAG, "onEvent ImoocBuyVIPEvent");
        if (mAccountManager.checkLogin()) {
            Intent intent = new Intent(mContext, NewVipCenterActivity.class);
            intent.putExtra(NewVipCenterActivity.HUI_YUAN, NewVipCenterActivity.HUANGJIN);
            startActivity(intent);
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    /**
     * 视频下载后点击
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DLItemEvent dlEvent) {
        BasicDLPart dlPart = dlEvent.items.get(dlEvent.position);

        switch (dlPart.getType()) {
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(this,
                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivity.getIntent2Me(this,
                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
        }

    }

    /**
     * 获取视频模块“现在升级的点击”
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlineGoVIPEvent headlineGoVIPEvent) {
        Intent intent = new Intent(mContext, NewVipCenterActivity.class);
        startActivity(intent);
    }

    //消息中心中部分item的跳转拦截处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArtDataSkipEvent event) {
        personal.iyuba.personalhomelibrary.data.model.Voa voa = event.voa;
        //文章跳转
        switch (event.type) {
            case "news":
                HeadlineTopCategory topCategory = event.headline;
                startActivity(TextContentActivity.getIntent2Me(mContext,
                        topCategory.id, topCategory.Title, topCategory.TitleCn, topCategory.type
                        , topCategory.Category, topCategory.CreatTime, topCategory.getPic(), topCategory.Source));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivityNew.getIntent2Me(mContext,
                        voa.categoryString, voa.title, voa.title_cn,
                        voa.getPic(),event.type, String.valueOf(voa.voaid), voa.sound));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivityNew.getIntent2Me(mContext,
                        voa.categoryString, voa.title, voa.title_cn, voa.getPic(),
                        event.type, String.valueOf(voa.voaid), voa.sound));//voa.getVipAudioUrl()
                break;
        }
    }

    //微课显示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MocShowResponse event) {
        if (mAccountManager.checkLogin()) {
            if (mAccountManager.isMocShow()) {
                mVoaListAdapter.setMocShow(true);
            } else {
                mVoaListAdapter.setMocShow(false);
            }
        } else {
            mVoaListAdapter.setMocShow(false);
        }
    }

    //显示儿童锁的功能
    private void showChildLock(){
        int version = ChildLockManager.getInstance().getVersion(this);
        if (version<App.child_lock_version){
            showChildLockDialog();
        }
    }

    //儿童锁弹窗显示
    private void showChildLockDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this,R.style.DialogTheme).create();
        dialog.setCancelable(false);
        dialog.show();

        Window window = dialog.getWindow();
        if (window!=null){
            window.setContentView(R.layout.dialog_child_lock);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = dm.widthPixels*4/5;

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = width;
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(lp);

            TextView title = window.findViewById(R.id.tv_title);
            title.setText("青少年模式");

            TextView childLock = window.findViewById(R.id.child_lock);
            childLock.setOnClickListener(v->{
                dialog.dismiss();

                ChildLockManager.getInstance().setVersion(this,App.child_lock_version);

                ChildLockActivity.start(MainActivity.this);
            });

            TextView next = window.findViewById(R.id.tv_enter);
            next.setText("暂不设置");
            next.setOnClickListener(v->{
                dialog.dismiss();

                ChildLockManager.getInstance().setVersion(this,App.child_lock_version);
            });
        }
    }
}
