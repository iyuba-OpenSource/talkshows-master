package com.iyuba.talkshow.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.efs.sdk.base.core.util.NetworkUtil;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.event.HeadlineGoVIPEvent;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivityNew;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivityNew;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
import com.iyuba.headlinelibrary.ui.video.VideoMiniContentActivity;
import com.iyuba.imooclib.event.ImoocBuyIyubiEvent;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.event.ImoocPayCourseEvent;
import com.iyuba.lib_common.event.RefreshDataEvent;
import com.iyuba.lib_common.util.LibSPUtil;
import com.iyuba.lib_user.data.NewLoginType;
import com.iyuba.lib_user.listener.UserinfoCallbackListener;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;
import com.iyuba.module.favor.data.model.BasicFavorPart;
import com.iyuba.module.favor.event.FavorItemEvent;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.User;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ActivityMain2Binding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.LoginResult;
import com.iyuba.talkshow.event.WxLoginEvent;
import com.iyuba.talkshow.newce.WordstepFragment;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.lil.event.RefreshEvent;
import com.iyuba.talkshow.ui.lil.fix.FixLoginSession;
import com.iyuba.talkshow.ui.lil.manager.TempDataManager;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.main.drawer.SlidingMenuFragment;
import com.iyuba.talkshow.ui.main.home.HomeFragment;
import com.iyuba.talkshow.ui.vip.buyiyubi.BuyIyubiActivity;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.ui.vip.payorder.PayOrderActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.util.ToastUtil;
import com.mob.secverify.PreVerifyCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.common.exception.VerifyException;

import org.apache.commons.codec.binary.Base64;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import personal.iyuba.personalhomelibrary.data.model.HeadlineTopCategory;
import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

//public class MainActivity extends BaseActivity implements MainMvpView {
//    public static final String TAG = "MainActivity";
//    private static final int REQUEST_SDCARD_PERMISSION = 1;
//    private static final int LOCATION_PERMISSION = 2;
//
//    public static final int DONG_MAN_SIZE = 4;
//    public static final int TING_GE_SIZE = 4;
//    public static final int SPAN_COUNT = 2;
//    private boolean ifresh = false;
//
//    int pageNum = 1;
//
//    AdInfoFlowUtil adInfoFlowUtil;
//
//    @Inject
//    MainPresenter mMainPresenter;
//    @Inject
//    VoaListAdapter mVoaListAdapter;
//    @Inject
//    AccountManager mAccountManager;
//    @Inject
//    ConfigManager configManager;
//
//    @BindView(R.id.main_drawer_layout)
//    DrawerLayout mDrawerLayout;
//    @BindView(R.id.main_toolbar)
//    Toolbar mToolbar;
//    @BindView(R.id.refresh_layout)
//    SmartRefreshLayout mSwipeRefreshLayout;
//    @BindView(R.id.recycler_view)
//    RecyclerView mSwipeMenuRecyclerView;
//    @BindView(R.id.reload)
//    View mReloadView;
//    private int defaultUi = 1;
//    private long starttime;
//    ActionBarDrawerToggle mDrawerToggle;
//    private SendBookPop sendBookPop;
//
//    //是否需要处理马甲包
//    private boolean isVestBagCheck = false;
//    //加载弹窗
//    private LoadingDialog loadingDialog;
//
//    VoaListAdapter.VoaCallback voaCallback = new VoaListAdapter.VoaCallback() {
//        @Override
//        public void onVoaClick(Voa voa) {
//            Log.e("VoaListAdapt", "voaCallback  点击了");
//            startDetailActivity(voa);
//        }
//    };
//    VoaListAdapter.LoopCallback loopCallback = new VoaListAdapter.LoopCallback() {
//        @Override
//        public void onLoopClick(int voaId) {
//            mMainPresenter.getVoa4Id(voaId);
//        }
//    };
//    VoaListAdapter.DataChangeCallback adapterDataRefreshCallback = new VoaListAdapter.DataChangeCallback() {
//        @Override
//        public void onClick(View v, CategoryFooter category, int limit, String ids) {
//            mMainPresenter.flag = 1;
//            mMainPresenter.loadMoreVoas(category, limit, ids);
//        }
//    };
//    View.OnClickListener dailyBonusCallback = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if (mAccountManager.checkLogin()) {
//                startActivity(new Intent(mContext, SignActivity.class));
//            } else {
////                showToast("请登录后再进行打卡");
//                LoginUtil.toLogin(MainActivity.this);
//            }
//        }
//    };
//    private boolean isFirst;
//    private VoaListAdapter.CourseChooseCallback coursechooseCallback = new VoaListAdapter.CourseChooseCallback() {
//        @Override
//        public void onClickCourse() {
//            if (mAccountManager.checkLogin()) {
//                mAccountManager.initMocUser();
//                ArrayList<Integer> typeIdFilter = new ArrayList<>();
//                typeIdFilter.add(3);
//                typeIdFilter.add(27);
//                startActivity(MobClassActivity.buildIntent(mContext, 3, true, typeIdFilter));
//            } else {
////                showToast("请登录后再进行微课学习");
//                LoginUtil.toLogin(MainActivity.this);
//            }
////            if (configManager.getCourseId() == 0) {
////                CourseChooseActivity.start(mContext, OpenFlag.TO_DETAIL);
////            } else {
////                mContext.startActivity(CourseDetailActivity.buildIntent(mContext, configManager.getCourseId(), configManager.getCourseTitle()));
////            }
//        }
//    };
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ifresh = false;
//    }
//
//    @Override
//    public boolean isSwipeBackEnable() {
//        return false;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        super.onCreate(savedInstanceState);
//        activityComponent().inject(this);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
//        mMainPresenter.attachView(this);
//        setSupportActionBar(mToolbar);
//
//        OAIDHelper.getInstance().getOAID();
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        if (!App.APP_TENCENT_PRIVACY) {
//            showFirstDialog();
//        }
//        initPersonal();
//        initSlidingMenu();
//        if (new Random().nextInt(300) <= 25) {
//            mSwipeMenuRecyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        showSendBookDialog();
//                    } catch (Exception var2) {
//                    }
//                }
//            }, 500);
//        }
//        if (mAccountManager.getUser() != null) {
//            IMoviesApp.setUser(mAccountManager.getUser().getUid() + "", mAccountManager.getUser().getVipStatus() + "");
//        }
//        mDrawerToggle.syncState();
//
//        mSwipeRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
////        mSwipeRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
////        mMainPresenter.getVoas();
//        mMainPresenter.loadLoop();
//        mMainPresenter.getSeriesList(App.DEFAULT_SERIESID);
//        mMainPresenter.getVoa4Category();
//        mMainPresenter.checkVersion();
//        mVoaListAdapter.setVoaCallback(voaCallback);
//        mVoaListAdapter.setLoopCallback(loopCallback);
//        mVoaListAdapter.setDataChangeCallback(adapterDataRefreshCallback);
//        mVoaListAdapter.setCourseClickCallback(coursechooseCallback);
//        mVoaListAdapter.setDailyBonusCallback(dailyBonusCallback);
//        mVoaListAdapter.setMobCallback(mobCallback);
//        mSwipeRefreshLayout.setOnRefreshListener(refreshLayout -> refreshData());
////        mSwipeRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreVoas());
//        mSwipeMenuRecyclerView.setAdapter(mVoaListAdapter);
//        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return mVoaListAdapter.getItemList().get(position) instanceof SeriesData ? 1 : 2;
//            }
//        });
//        mSwipeMenuRecyclerView.addItemDecoration(new MainGridItemDivider(this));
//        mSwipeMenuRecyclerView.setLayoutManager(layoutManager);
//        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
//        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//    }
//
//    private void initPersonal() {
//        PersonalHome.setMainPath("com.iyuba.talkshow.ui.main.MainActivity");
//        try {
//            mAccountManager.initPersonHome();
//            mAccountManager.initMocUser();
//        } catch (Exception var) {
//        }
//    }
//
//    private void showFirstDialog() {
//        isFirst = configManager.isFirstStart();
//        Log.d("com.iyuba.talkshow", "showFirstDialog: " + isFirst);
//        if (isFirst) {
//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
//                        Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
//                        mContext.startActivity(intent);
//                    } else {
//                        Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, "用户隐私协议");
//                        mContext.startActivity(intent);
//                    }
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    ds.setColor(getResources().getColor(R.color.colorPrimary));
//                    ds.setUnderlineText(true);
//                }
//            };
////            if (getIntent().getExtras().getBoolean("showDialog",false)){
//            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_privacy, null);
//            TextView remindText = view.findViewById(R.id.remindText);
//            String remindString = getResources().getString(R.string.user_protocol);
//            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(remindString);
//            if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
//                spannableStringBuilder.setSpan(clickableSpan, remindString.indexOf(App.APP_NAME_PRIVACY), remindString.indexOf(App.APP_NAME_PRIVACY) + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                ClickableSpan clickableUsage = new ClickableSpan() {
//                    @Override
//                    public void onClick(View widget) {
//                        Intent intent = WebActivity.buildIntent(mContext, App.Url.USAGE_URL + App.APP_NAME_CH, "用户协议");
//                        mContext.startActivity(intent);
//                    }
//
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
//                        ds.setColor(getResources().getColor(R.color.colorPrimary));
//                        ds.setUnderlineText(true);
//                    }
//                };
//                spannableStringBuilder.setSpan(clickableUsage, remindString.indexOf("用户协议"), remindString.indexOf("用户协议") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            } else {
//                spannableStringBuilder.setSpan(clickableSpan, remindString.indexOf("用户协议"), remindString.indexOf("用户协议") + 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            remindText.setText(spannableStringBuilder);
//            remindText.setMovementMethod(LinkMovementMethod.getInstance());
//
//            AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("个人信息保护政策")
//                    .setView(view)
//                    .setCancelable(false)
//                    .create();
//            dialog.show();
//            TextView agreeNo = view.findViewById(R.id.text_no_agree);
//            TextView agree = view.findViewById(R.id.text_agree);
//            agreeNo.setOnClickListener(v -> ToastUtils.showShort("请同意~"));
//            agree.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    configManager.setFirstStart(false);
//                    TalkShowApplication.initUMMob();
//                }
//            });
//        } else {
//            TalkShowApplication.initUMMob();
//        }
//    }
//
//    private void loadMoreVoas() {
//        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
//            showToast(R.string.please_check_network);
//            dismissRefreshingView();
//            return;
//        }
//        pageNum += 1;
//        mMainPresenter.loadMoreChildNews(pageNum);
//    }
//
//    private void refreshData() {
//        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
//            showToast(R.string.please_check_network);
//            dismissRefreshingView();
//            return;
//        }
//        mMainPresenter.syncVoa4Category(App.DEFAULT_SERIESID);
//        mMainPresenter.syncSeries(App.DEFAULT_SERIESID);
//        pageNum = 1;
//        Log.e("onrefresh", "刷新了！");
//        if (adInfoFlowUtil != null) {
//            adInfoFlowUtil.reset();
//        }
////        mMainPresenter.loadVoas();
//        mMainPresenter.loadLoop();
//        ifresh = true;
//    }
//
//    private void showSendBookDialog() {
////        sendBookPop = new SendBookPop(this, findViewById(R.id.container));
////        new AlertDialog.Builder(this).setMessage("现在给应用好评可以获得由爱语吧名师团队编写的电子书哦,先到先得~~")
////                .setNegativeButton("暂不考虑", null)
////                .setPositiveButton("去好评", (dialog, which) -> startActivity(new Intent(mContext, SendBookActivity.class))).setCancelable(false).show();
//    }
//
//    @OnClick(R.id.reload)
//    public void onClickReload() {
//        mMainPresenter.loadVoas();
//        mMainPresenter.loadLoop();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (adInfoFlowUtil == null && !AdBlocker.getInstance().shouldBlockAd()) {
//            try {
//                adInfoFlowUtil = new AdInfoFlowUtil(mContext, true, new AdInfoFlowUtil.Callback() {
//                    @Override
//                    public void onADLoad(List ads) {
//                        try {
//                            AdNativeResponse nativeResponse = new AdNativeResponse();
//                            nativeResponse.setNativeResponse((NativeResponse) ads.get(0));
//                            if (!mAccountManager.isVip()) {
//                                mVoaListAdapter.setAd(nativeResponse);
//                            } else {
//                                Log.d("com.iyuba.talkshow", "onADLoad: is vip");
//                                mVoaListAdapter.removeAd();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).setAdRequestSize(1);
//            } catch (Exception var2) {
//            }
//        }
//        if (mAccountManager.isVip()) {
//            Log.d("com.iyuba.talkshow", "onADLoad: is vip");
//            mVoaListAdapter.removeAd();
//        }
//        MobclickAgent.onResume(this);
//    }
//
//    private void initSlidingMenu() {
//        SlidingMenuFragment menuFragment = SlidingMenuFragment.newInstance();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.main_menu_drawer, menuFragment);
//        transaction.commit();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    mDrawerLayout.closeDrawers();
//                } else {
//                    mDrawerLayout.openDrawer(GravityCompat.START);
//                }
//                return true;
//            case R.id.search:
//                startActivity(new Intent(mContext, SearchActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        ifresh = true;
//    }
//
//    @Override
//    public void showAlertDialog(String msg, DialogInterface.OnClickListener ocl) {
//        AlertDialog alert = new AlertDialog.Builder(this).create();
//        alert.setTitle(R.string.alert_title);
//        alert.setMessage(msg);
//        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.alert_btn_ok), ocl);
//        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.alert_btn_cancel),
//                (dialog, which) -> {
//                });
//        alert.show();
//    }
//
//    @Override
//    public void startAboutActivity(String versionCode, String appUrl) {
//        Intent intent = AboutActivity.buildIntent(this, versionCode, appUrl);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//        mMainPresenter.detachView();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void showVoas(List<Voa> voas) {
//        mVoaListAdapter.setVoas(voas);
//        mVoaListAdapter.notifyDataSetChanged();
//        if (adInfoFlowUtil != null) {
//            adInfoFlowUtil.refreshAd();
//        }
//    }
//
//    @Override
//    public void showVoasByCategory(List<Voa> voas, CategoryFooter category) {
//        mVoaListAdapter.setVoasByCategory(voas, category);
//    }
//
//    @Override
//    public void setChoise(List<SeriesData> list) {
//        //处理马甲包
//        if (isVestBagCheck) {
//            VestBagAdapter adapter = new VestBagAdapter(this, list);
//            LinearLayoutManager manager = new LinearLayoutManager(this);
//            mSwipeMenuRecyclerView.setLayoutManager(manager);
//            mSwipeMenuRecyclerView.setAdapter(adapter);
//            return;
//        }
//
//        mVoaListAdapter.setSeries(list);
//        mVoaListAdapter.notifyDataSetChanged();
//        if (adInfoFlowUtil != null) {
//            adInfoFlowUtil.refreshAd();
//        }
//    }
//
//    @Override
//    public void showMoreVoas(List<Voa> voas) {
//        mVoaListAdapter.addVoas(voas);
//        mVoaListAdapter.notifyDataSetChanged();
//        if (adInfoFlowUtil != null) {
//            adInfoFlowUtil.refreshAd();
//        }
//    }
//
//    @Override
//    public void showVoasEmpty() {
//        mVoaListAdapter.setVoas(Collections.<Voa>emptyList());
//        mVoaListAdapter.notifyDataSetChanged();
//        Toast.makeText(this, R.string.empty_content, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void showError() {
//        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading)).show();
//    }
//
//    @Override
//    public void setBanner(List<LoopItem> loopItemList) {
//        mVoaListAdapter.setBanner(loopItemList);
//    }
//
//    @Override
//    public void showToast(String text) {
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void showToast(int resId) {
//        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void startDetailActivity(Voa voa) {
//        if (voa != null) {
//            Log.e("startDetailAcivity", "执行了");
//            if (!ifresh) {
//                startActivity(DetailActivity.buildIntentLimit(this, voa, true));
//            }
//            ifresh = false;
//        } else {
//            Log.e("startDetailAcivity", "Voa is null.");
//        }
//    }
//
//    @Override
//    public void showReloadView() {
//        mReloadView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void dismissReloadView() {
//        mReloadView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void dismissRefreshingView() {
//        mSwipeRefreshLayout.finishRefresh();
//        mSwipeRefreshLayout.finishLoadMore();
//    }
//
//    private boolean isExit = false;
//
//    @Override
//    public void onBackPressed() {
//        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawers();
//            return;
//        }
//        if (isExit) {
//            finish();
//        } else {
//            isExit = true;
//            showToast(getString(R.string.one_more_exit));
//            Timer timber = new Timer();
//            timber.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    isExit = false;
//                }
//            }, 3000);
//        }
//    }
//
//    VoaListAdapter.MobCallback mobCallback = new VoaListAdapter.MobCallback() {
//        @Override
//        public void onMobCheck() {
//            clickLogin();
//        }
//    };
//
//    public void clickLogin() {
//        LoginUtil.toLogin(this);
//    }
//
//    @Override
//    public void goResultActivity(LoginResult data) {
//        if (data == null) {
//            Log.e(TAG, "goResultActivity LoginResult is null. ");
//            Toast.makeText(mContext, "请使用用户名密码登录。", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(mContext, LoginActivity.class));
//        } else if (!TextUtils.isEmpty(data.getPhone())) {
//            String randNum = "" + System.currentTimeMillis();
//            String user = "iyuba" + randNum.substring(randNum.length() - 4) + data.getPhone().substring(data.getPhone().length() - 4);
//            String pass = data.getPhone().substring(data.getPhone().length() - 6);
//            Log.e(TAG, "goResultActivity.user  " + user);
//            Intent intent = new Intent(mContext, RegisterSubmitActivity.class);
//            intent.putExtra(RegisterSubmitActivity.PhoneNum, data.getPhone());
//            intent.putExtra(RegisterSubmitActivity.UserName, user);
//            intent.putExtra(RegisterSubmitActivity.PassWord, pass);
//            intent.putExtra(RegisterSubmitActivity.RegisterMob, 1);
//            startActivity(intent);
//        } else {
//            Log.e(TAG, "goResultActivity LoginResult is ok. ");
//        }
//        SecVerify.finishOAuthPage();
//        CommonProgressDialog.dismissProgressDialog();
//    }
//
//    //登录响应
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(LoginEvent event) {
//        Log.e(TAG, "onEvent LoginEvent");
//        initPersonal();
//    }
//
//
//    /*********************************微课操作******************************/
//    /**
//     * 微课购买黄金会员跳转
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ImoocBuyVIPEvent event) {
//        Log.e(TAG, "onEvent ImoocBuyVIPEvent");
//        if (mAccountManager.checkLogin()) {
//            Intent intent = new Intent(mContext, NewVipCenterActivity.class);
//            intent.putExtra(NewVipCenterActivity.HUI_YUAN, NewVipCenterActivity.HUANGJIN);
//            startActivity(intent);
//        } else {
//            startActivity(new Intent(mContext, LoginActivity.class));
//        }
//    }
//
//    /**
//     * 微课跳转爱语币购买界面
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ImoocBuyIyubiEvent event){
//        if (mAccountManager.checkLogin()){
//            startActivity(new Intent(this, BuyIyubiActivity.class));
//        }else {
//            LoginUtil.toLogin(this);
//        }
//    }
//
//    /**
//     * 微课直购
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ImoocPayCourseEvent event){
//        if (!mAccountManager.checkLogin()){
//            LoginUtil.toLogin(this);
//            return;
//        }
//
//        String desc = "花费"+event.price+"购买微课("+event.body+")";
//        String subject = "微课直购";
//        Intent intent = PayOrderActivity.buildIntent(this,desc,event.price,subject,event.body,subject,event.courseId,event.productId);
//        startActivity(intent);
//    }
//
//    /********************************视频模块********************************/
//    //视频下载后点击
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(DLItemEvent dlEvent) {
//        BasicDLPart dlPart = dlEvent.items.get(dlEvent.position);
//
//        switch (dlPart.getType()) {
//            case "voa":
//            case "csvoa":
//            case "bbc":
//            case "song":
//                startActivity(AudioContentActivity.getIntent2Me(this,
//                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
//                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
//                break;
//            case "voavideo":
//            case "meiyu":
//            case "ted":
//            case "bbcwordvideo":
//            case "topvideos":
//            case "japanvideos":
//                startActivity(VideoContentActivity.getIntent2Me(this,
//                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
//                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
//                break;
//        }
//
//    }
//
//    //视频模块评测中升级会员跳转
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(HeadlineGoVIPEvent headlineGoVIPEvent) {
//        Intent intent = new Intent(mContext, NewVipCenterActivity.class);
//        startActivity(intent);
//    }
//
//    //消息中心中部分item的跳转拦截处理
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ArtDataSkipEvent event) {
//        personal.iyuba.personalhomelibrary.data.model.Voa voa = event.voa;
//        //文章跳转
//        switch (event.type) {
//            case "news":
//                HeadlineTopCategory topCategory = event.headline;
//                startActivity(TextContentActivity.getIntent2Me(mContext,
//                        topCategory.id, topCategory.Title, topCategory.TitleCn, topCategory.type
//                        , topCategory.Category, topCategory.CreatTime, topCategory.getPic(), topCategory.Source));
//                break;
//            case "voa":
//            case "csvoa":
//            case "bbc":
//            case "song":
//                startActivity(AudioContentActivityNew.getIntent2Me(mContext,
//                        voa.categoryString, voa.title, voa.title_cn,
//                        voa.getPic(), event.type, String.valueOf(voa.voaid), voa.sound));
//                break;
//            case "voavideo":
//            case "meiyu":
//            case "ted":
//            case "bbcwordvideo":
//            case "topvideos":
//            case "japanvideos":
//                startActivity(VideoContentActivityNew.getIntent2Me(mContext,
//                        voa.categoryString, voa.title, voa.title_cn, voa.getPic(),
//                        event.type, String.valueOf(voa.voaid), voa.sound));//voa.getVipAudioUrl()
//                break;
//        }
//    }
//
//    /**************************微信登录回调*******************************/
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(WxLoginEvent event) {
//        if (!NetworkUtil.isConnected(this)) {
//            ToastUtil.showToast(this, "网络链接已断开，请链接网络后重试");
//            return;
//        }
//
//        String token = FixLoginSession.getInstance().getWxSmallToken();
//
//        if (!TextUtils.isEmpty(token)&&event.getErrCode() == 0) {
//            startLoading("正在获取用户信息");
//
//            mMainPresenter.getSmallUid(token);
//        } else {
//            ToastUtil.showToast(this, "微信一键登录失败，请重试或者更换登录方式");
//        }
//    }
//
//    //显示加载
//    private void startLoading(String msg){
//        if (loadingDialog==null){
//            loadingDialog = new LoadingDialog(this);
//        }
//        loadingDialog.setMessage(msg);
//        loadingDialog.show();
//    }
//
//    //关闭加载
//    private void closeLoading(){
//        if (loadingDialog!=null&&loadingDialog.isShowing()){
//            loadingDialog.dismiss();
//        }
//    }
//
//    //微信登录信息显示
//    @Override
//    public void showWxLoginMsg(String msg) {
//        closeLoading();
//        ToastUtil.showToast(this,msg);
//    }
//
//    //微信登录结果显示
//    @Override
//    public void showWxLoginResult() {
//        showWxLoginMsg("微信登录完成~");
//        EventBus.getDefault().post(new LoginEvent());
//    }
//}

public class MainActivity extends BaseViewBindingActivity<ActivityMain2Binding> implements MainMvpView{

    private static final String TAG = "MainActivity2";

    private final static String HOME = "home";
    private final static String WORD = "word";
    private final static String VIDEO = "video";

    public static final int DONG_MAN_SIZE = 4;
    public static final int TING_GE_SIZE = 4;
    public static final int SPAN_COUNT = 2;

    private HomeFragment homeFragment;
    private WordstepFragment wordFragment;
    private DropdownTitleFragmentNew videoFragment;

    @Inject
    MainPresenter mMainPresenter;
    @Inject
    ConfigManager configManager;

    //加载弹窗
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mMainPresenter.attachView(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //设置单词的默认数据
        configManager.putCourseId(ConfigData.default_word_book_id);
        configManager.putCourseTitle(ConfigData.default_word_book_title);

        //主页
        binding.llHome.setVisibility(AbilityControlManager.getInstance().isLimitLesson()?View.GONE:View.VISIBLE);
        binding.llHome.setOnClickListener(v->{
            updateUI(v.getId());
        });
        //单词
        binding.llWord.setVisibility((ConfigData.showWord && binding.llHome.getVisibility()==View.VISIBLE)?View.VISIBLE:View.GONE);
        binding.llWord.setOnClickListener(v->{
            updateUI(v.getId());
        });
        //视频
        binding.llVideo.setVisibility(AbilityControlManager.getInstance().isLimitVideo() ?View.GONE:View.GONE);
        binding.llVideo.setOnClickListener(v->{
            updateUI(v.getId());
        });

        //进来后首先初始化用户信息
        initUserInfo();
        //根据配置设置秒验预加载
        NewLoginType.getInstance().setCurLoginType(ConfigData.loginType);
        if (SecVerify.isVerifySupport()&&ConfigData.loginType.equals(NewLoginType.loginByVerify)){
            SecVerify.preVerify(new PreVerifyCallback() {
                @Override
                public void onComplete(Void unused) {
                    TempDataManager.getInstance().setMobVerify(true);
                }

                @Override
                public void onFailure(VerifyException e) {
                    TempDataManager.getInstance().setMobVerify(false);
                }
            });
        }

        initSlideMenu();

        //显示界面
        if (AbilityControlManager.getInstance().isLimitLesson()){
            updateUI(binding.llWord.getId());
        }else {
            updateUI(binding.llHome.getId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initSlideMenu(){
        SlidingMenuFragment menuFragment = SlidingMenuFragment.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_menu_drawer, menuFragment);
        transaction.commit();
    }

    private void updateUI(int pageId){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment1 = manager.findFragmentByTag(HOME);
        Fragment fragment2 = manager.findFragmentByTag(VIDEO);
        Fragment fragment3 = manager.findFragmentByTag(WORD);

        if (fragment1!=null){
            homeFragment = (HomeFragment) fragment1;
        }

        if (fragment2!=null){
            videoFragment = (DropdownTitleFragmentNew) fragment2;
        }

        if (fragment3!=null){
            wordFragment = (WordstepFragment) fragment3;
        }

        resetView();
        switch (pageId){
            case R.id.ll_home:
                if (homeFragment == null){
                    homeFragment = new HomeFragment();
                    hideFragment(transaction,wordFragment,videoFragment);
                    transaction.add(R.id.frame_layout,homeFragment,HOME);
                }else {
                    hideFragment(transaction,wordFragment,videoFragment);
                    transaction.show(homeFragment);
                }

                binding.ivHome.setSelected(true);
                binding.tvHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.ll_word:
                if (wordFragment == null){
                    wordFragment = WordstepFragment.build(configManager.getWordId(),configManager.getWordShowType());
                    hideFragment(transaction,homeFragment,videoFragment);
                    transaction.add(R.id.frame_layout,wordFragment,WORD);
                }else {
                    hideFragment(transaction,homeFragment,videoFragment);
                    transaction.show(wordFragment);
                }

                binding.ivWord.setSelected(true);
                binding.tvWord.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.ll_video:
                if (videoFragment == null){
                    //替换为小视频
                    IHeadlineManager.appId = String.valueOf(App.APP_ID);
                    IHeadlineManager.appName = App.APP_NAME_EN;

                    String[] types = new String[]{
                            HeadlineType.SMALLVIDEO
                    };

                    Bundle videoBundle = new Bundle();
                    videoBundle.putInt("page_count",10);
                    videoBundle.putBoolean("show_back",false);
                    videoBundle.putStringArray("type",types);
                    videoFragment = DropdownTitleFragmentNew.newInstance(videoBundle);

                    hideFragment(transaction,homeFragment,wordFragment);
                    transaction.add(R.id.frame_layout,videoFragment,VIDEO);
                }else {
                    hideFragment(transaction,homeFragment,wordFragment);
                    transaction.show(videoFragment);
                }

                binding.ivVideo.setSelected(true);
                binding.tvVideo.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }

        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction, Fragment...fragments){
        for (Fragment fragment:fragments){
            if (fragment!=null){
                transaction.hide(fragment);
            }
        }
    }

    private void resetView(){
        binding.ivHome.setSelected(false);
        binding.tvHome.setTextColor(getResources().getColor(R.color.bottom_text_color));
        binding.ivWord.setSelected(false);
        binding.tvWord.setTextColor(getResources().getColor(R.color.bottom_text_color));
        binding.ivVideo.setSelected(false);
        binding.tvVideo.setTextColor(getResources().getColor(R.color.bottom_text_color));
    }

    @Override
    public void onBackPressed() {
        if (isSlideOpen()) {
            closeSlide();
            return;
        }
        homeFragment.onBack();
    }

    /**********************************侧边栏*******************************/
    public boolean isSlideOpen(){
        return binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void openSlide(){
        binding.mainDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeSlide(){
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /*********************************微课操作******************************/
    /**
     * 微课购买黄金会员跳转
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyVIPEvent event) {
        Log.e(TAG, "onEvent ImoocBuyVIPEvent");
        if (UserInfoManager.getInstance().isLogin()) {
            Intent intent = new Intent(mContext, NewVipCenterActivity.class);
            intent.putExtra(NewVipCenterActivity.HUI_YUAN, NewVipCenterActivity.HUANGJIN);
            startActivity(intent);
        } else {
            NewLoginUtil.startToLogin(this);
        }
    }

    /**
     * 微课跳转爱语币购买界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyIyubiEvent event){
        if (UserInfoManager.getInstance().isLogin()){
            startActivity(new Intent(this, BuyIyubiActivity.class));
        }else {
            NewLoginUtil.startToLogin(this);
        }
    }

    /**
     * 微课直购
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocPayCourseEvent event){
        if (!UserInfoManager.getInstance().isLogin()){
            NewLoginUtil.startToLogin(this);
            return;
        }

        String desc = "花费"+event.price+"购买微课("+event.body+")";
        String subject = "微课直购";
        Intent intent = PayOrderActivity.buildIntent(this,desc,event.price,subject,event.body,event.courseId,event.productId,PayOrderActivity.Order_moc);
        startActivity(intent);
    }

    /********************************视频模块********************************/
    //视频下载后点击
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

    //视频模块评测中升级会员跳转
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
                        voa.getPic(), event.type, String.valueOf(voa.voaid), voa.sound));
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

    //共通模块的收藏界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FavorItemEvent fEvent) {
        //收藏页面点击
        if (fEvent == null) {
            ToastUtil.show(this,"目前暂时不支持跳转");
            return;
        }
        BasicFavorPart fPart = fEvent.items.get(fEvent.position);
        goFavorItem(fPart);
    }

    private void goFavorItem(BasicFavorPart part) {
        switch (part.getType()) {
            case "news":
            case "voa":
            case "csvoa":
            case "bbc":
                startActivity(AudioContentActivityNew.buildIntent(this, part.getCategoryName(), part.getTitle(), part.getTitleCn(), part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;
            case "song":
                startActivity(AudioContentActivity.buildIntent(this, part.getCategoryName(), part.getTitle(), part.getTitleCn(), part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivityNew.buildIntent(this, part.getCategoryName(), part.getTitle(), part.getTitleCn(), part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;
//            case "series":
//                Intent intent = SeriesActivity.buildIntent(this, part.getSeriesId(), part.getId());
//                startActivity(intent);
//                break;
            case "smallvideo":
                startActivity(VideoMiniContentActivity.buildIntentForOne(this, part.getId(), 0, 1, 1));
                break;
        }
    }

    /**************************微信登录回调*******************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WxLoginEvent event) {
        if (!NetworkUtil.isConnected(this)) {
            ToastUtil.showToast(this, "网络链接已断开，请链接网络后重试");
            return;
        }

        String token = FixLoginSession.getInstance().getWxSmallToken();

        if (!TextUtils.isEmpty(token)&&event.getErrCode() == 0) {
            startLoading("正在获取用户信息");

            mMainPresenter.getSmallUid(token);
        } else {
            ToastUtil.showToast(this, "微信一键登录失败，请重试或者更换登录方式");
        }
    }

    //显示加载
    private void startLoading(String msg){
        if (loadingDialog==null){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(msg);
        loadingDialog.show();
    }

    //关闭加载
    private void closeLoading(){
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    //微信登录信息显示
    @Override
    public void showWxLoginMsg(String msg) {
        closeLoading();
        ToastUtil.showToast(this,msg);
    }

    //微信登录结果显示
    @Override
    public void showWxLoginResult() {
        closeLoading();
        showWxLoginMsg("微信登录完成~");
        EventBus.getDefault().post(new LoginEvent());
    }

    /*******************************无用功能******************************/
    @Override
    public void showToastShort(int resId) {

    }

    @Override
    public void showToastShort(String message) {

    }

    @Override
    public void showToastLong(int resId) {

    }

    @Override
    public void showToastLong(String message) {

    }

    @Override
    public void showVoas(List<Voa> voas) {

    }

    @Override
    public void showVoasByCategory(List<Voa> voas, CategoryFooter categoroy) {

    }

    @Override
    public void showVoasEmpty() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void setBanner(List<LoopItem> loopItemList) {

    }

    @Override
    public void showToast(String text) {

    }

    @Override
    public void showToast(int resId) {

    }

    @Override
    public void startDetailActivity(Voa voa) {

    }

    @Override
    public void showReloadView() {

    }

    @Override
    public void dismissReloadView() {

    }

    @Override
    public void dismissRefreshingView() {

    }

    @Override
    public void showAlertDialog(String msg, DialogInterface.OnClickListener listener) {

    }

    @Override
    public void startAboutActivity(String versionCode, String appUrl) {

    }

    @Override
    public void setChoise(List<SeriesData> list) {

    }

    @Override
    public void showMoreVoas(List<Voa> voas) {

    }

    @Override
    public void goResultActivity(LoginResult data) {

    }

    /*******************************回调操作*****************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event){
        if (event.getType().equals(RefreshEvent.USER_INFO)){
            //刷新用户信息
            if (UserInfoManager.getInstance().isLogin()){
                getUserinfo(UserInfoManager.getInstance().getUserId());
            }
        }
    }

    //广告刷新用户信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){
        if (event.getType().equals(RefreshEvent.USER_INFO)){
            //刷新用户信息
            if (UserInfoManager.getInstance().isLogin()){
                getUserinfo(UserInfoManager.getInstance().getUserId());
            }
        }
    }

    /*****************************新的登录操作*************************/
    //初始化账号信息
    private void initUserInfo(){
        if (UserInfoManager.getInstance().isLogin()){
            getUserinfo(UserInfoManager.getInstance().getUserId());
        }else {
            //获取原来的信息进行处理
            int oldUserId = getOldUserData();
            if (oldUserId>0){
                getUserinfo(oldUserId);
            }
        }
    }

    //获取用户信息
    private void getUserinfo(int userId){
        UserInfoManager.getInstance().getRemoteUserInfo(userId, new UserinfoCallbackListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new LoginEvent());
            }

            @Override
            public void onFail(String errorMsg) {
                int a = 0;
            }
        });
    }

    //获取原来的id信息
    private int getOldUserData(){
        SharedPreferences preferences = LibSPUtil.getPreferences(this,"kouyu_show_file");
        String oldData = preferences.getString("mUser","");

        //这里顺便把账号信息取出来
        String userName = preferences.getString("start_ad_name","");
        String password = preferences.getString("start_ad_start_time","");
        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(password)){
            UserInfoManager.getInstance().saveAccountAndPwd(userName,password);
            //销毁之前的数据
            preferences.edit().putString("start_ad_name","").apply();
            preferences.edit().putString("start_ad_start_time","").apply();
        }

        if (TextUtils.isEmpty(oldData)){
            return 0;
        }

        try {
            byte[] b = Base64.decodeBase64(oldData.getBytes());
            InputStream bis = new ByteArrayInputStream(b);
            ObjectInputStream ois = new ObjectInputStream(bis); // something wrong
            User user = (User) ois.readObject();
            ois.close();

            //这里获取到数据后不要删除，只删除用户信息就行了，因为还有其他的数据需要使用
            preferences.edit().putString("mUser","").apply();

            return user.getUid();
        }catch (Exception e){
            return 0;
        }
    }

    //设置广告测试界面
    /*private void showAdTestPage(){
        if (AdTestKeyData.ConfigData.isOpenListAdTest && !UserInfoManager.getInstance().isVip()){
            AdFixListFragment listFragment = AdFixListFragment.getInstance(false);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_ad,listFragment).show(listFragment).commitNowAllowingStateLoss();

            //是否开启界面
            if (AdTestKeyData.ConfigData.isShowListAdTestPage){
                binding.showMain.setVisibility(View.GONE);
                binding.rlMark.setVisibility(View.GONE);
            }else {
                binding.showMain.setVisibility(View.VISIBLE);
                binding.rlMark.setVisibility(View.VISIBLE);
            }
        }
    }*/
}
