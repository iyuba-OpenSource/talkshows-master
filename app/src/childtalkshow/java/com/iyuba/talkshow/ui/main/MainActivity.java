package com.iyuba.talkshow.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
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
import com.iyuba.talkshow.data.model.User;
import com.iyuba.talkshow.databinding.ActivityMain2Binding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.lil.manager.TempDataManager;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.main.drawer.SlidingMenuFragment;
import com.iyuba.talkshow.ui.main.home.HomeFragment;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
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

import personal.iyuba.personalhomelibrary.data.model.HeadlineTopCategory;
import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;
//
///**
// * Created by Administrator on 2016/11/12 0012.
// */
//
//public class MainActivity extends BaseActivity implements MainMvpView {
//    public static final String TAG = "MainActivity";
//    private static final int REQUEST_SDCARD_PERMISSION = 1;
//    private static final int LOCATION_PERMISSION = 2;
//
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
//    VoaListAdapter.VoaCallback voaCallback = new VoaListAdapter.VoaCallback() {
//        @Override
//        public void onVoaClick(Voa voa) {
//            Log.e("VoaListAdapt", "voaCallback  点击了");
//            startDetailActivity(voa);
//        }
//    };
//
//    VoaListAdapter.LoopCallback loopCallback = new VoaListAdapter.LoopCallback() {
//        @Override
//        public void onLoopClick(int voaId) {
//            mMainPresenter.getVoa4Id(voaId);
//        }
//    };
//
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
//                showToast("请登录后再进行打卡");
////                startActivity(new Intent(mContext, LoginActivity.class));
//                clickLogin();
//            }
//        }
//    };
//    private boolean isFirst;
//    private VoaListAdapter.CourseChooseCallback coursechooseCallback = new VoaListAdapter.CourseChooseCallback() {
//        @Override
//        public void onClickCourse(int item) {
//            if (!mAccountManager.checkLogin()) {
//                clickLogin();
//                return;
//            }
//            if (item == 2) {
//                mAccountManager.initMocUser();
//                ArrayList<Integer> typeIdFilter = new ArrayList<>();
//                typeIdFilter.add(3);
//                typeIdFilter.add(27);
//                startActivity(MobClassActivity.buildIntent(mContext, 3, true, typeIdFilter));
//            } else if (configManager.getCourseId() == 0) {
//                CourseChooseActivity.start(mContext, OpenFlag.TO_DETAIL);
//            } else {
//                mContext.startActivity(CourseDetailActivity.buildIntent(mContext, configManager.getCourseId(), configManager.getCourseTitle()));
//            }
//        }
//    };
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(LoginEvent event) {
//        initPersonal();
//        Log.d("com.iyuba.talkshow", "LoginEvent in main ");
//        mMainPresenter.syncSeries();
//        mMainPresenter.syncVoa4Category();
//    }
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
//    }
//
//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        if (!App.APP_TENCENT_PRIVACY) {
//            showFirstDialog();
//        }
//        preVerify();
//        initPersonal();
//        mMainPresenter.attachView(this);
//        setSupportActionBar(mToolbar);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();
//        mMainPresenter.enterGroup();
////        StatusBarUtil.setTranslucentForImageView(this, null);
//        initSlidingMenu();
//        if (new Random().nextInt(300) <= 25) {
//            mSwipeMenuRecyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        showSendBookDialog();
//                    } catch (Exception var2) { }
//                }
//            }, 500);
//        }
//        if (mAccountManager.getUser() != null) {
//            IMoviesApp.setUser(mAccountManager.getUser().getUid() + "", mAccountManager.getUser().getVipStatus() + "");
//        }
//
//        mSwipeRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
//        mSwipeRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
//        mMainPresenter.getVoas();
//        mMainPresenter.loadLoop();
//        mMainPresenter.getSeriesses();
//        mMainPresenter.checkVersion();
//        mVoaListAdapter.setVoaCallback(voaCallback);
//        mVoaListAdapter.setLoopCallback(loopCallback);
//        mVoaListAdapter.setDataChangeCallback(adapterDataRefreshCallback);
//        mVoaListAdapter.setCourseClickCallback(coursechooseCallback);
//        mVoaListAdapter.setDailyBonusCallback(dailyBonusCallback);
//        mVoaListAdapter.setMobCallback(mobCallback);
//        mSwipeRefreshLayout.setOnRefreshListener(refreshLayout -> refreshData());
//        mSwipeRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreVoas());
//        mSwipeMenuRecyclerView.setAdapter(mVoaListAdapter);
//        final GridLayoutManager layoutManager = new GridLayoutManager(this, MainData.SPAN_COUNT);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return mVoaListAdapter.getItemList().get(position) instanceof Voa ? 1 : 2;
//            }
//        });
//        mSwipeMenuRecyclerView.addItemDecoration(new MainGridItemDivider(this));
//        mSwipeMenuRecyclerView.setLayoutManager(layoutManager);
//        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
//        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//
//        //增加个人中心的信息
//        if (mAccountManager.checkLogin()){
//            User user = new User();
//            user.uid = mAccountManager.getUid();
//            user.name = mAccountManager.getUserName();
//            user.vipStatus = mAccountManager.isVip()?"1":"0";
//            IyuUserManager.getInstance().setCurrentUser(user);
//        }
//    }
//
//    private void initPersonal() {
//        try {
//            mAccountManager.initPersonHome();
//            mAccountManager.initMocUser();
//        } catch (Exception var) {}
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
//                        Intent intent = WebActivity.buildIntent(mContext, Constant.Url.PROTOCOL_VIVO_PRIVACY + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
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
//                        Intent intent = WebActivity.buildIntent(mContext, Constant.Url.PROTOCOL_VIVO_USAGE + App.APP_NAME_CH, "用户协议");
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
//        mMainPresenter.syncVoa4Category();
//        mMainPresenter.syncSeries();
//        pageNum = 1;
//        Log.e("onrefresh", "刷新了！");
//        if (adInfoFlowUtil!=null){
//            adInfoFlowUtil.reset();
//        }
//        mMainPresenter.loadVoas();
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
//        //屏蔽广告
//        if (!AdBlocker.getInstance().shouldBlockAd()){
//            adInfoFlowUtil = new AdInfoFlowUtil(mContext, true, new AdInfoFlowUtil.Callback() {
//                @Override
//                public void onADLoad(List ads) {
//                    try {
//                        AdNativeResponse nativeResponse = new AdNativeResponse();
//                        nativeResponse.setNativeResponse((NativeResponse) ads.get(0));
//                        if (!mAccountManager.isVip()) {
//                            mVoaListAdapter.setAd(nativeResponse);
//                        } else {
//                            Log.d("com.iyuba.talkshow", "onADLoad: is vip");
//                            mVoaListAdapter.removeAd();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).setAdRequestSize(1);
//
//        }
//        if (!mAccountManager.isVip()) {
//
//        } else {
//            Log.d("com.iyuba.talkshow", "onADLoad: is vip");
//            mVoaListAdapter.removeAd();
//        }
//        MobclickAgent.onResume(this);
//    }
//
//    public void onRequestSDCardSuccess() {
//        mMainPresenter.getWelcomePic();
//    }
//
//    public void onRequestSDCardFail() {
//        // TODO
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
//        if (adInfoFlowUtil!=null){
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
//    public void showMoewNewList(List<Voa> voas, CategoryFooter category) {
//        mVoaListAdapter.setVoasByCategory(voas, category);
//    }
//
//    @Override
//    public void showMoreVoas(List<Voa> voas) {
//        mVoaListAdapter.addVoas(voas);
//        mVoaListAdapter.notifyDataSetChanged();
//        if (adInfoFlowUtil!=null){
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
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
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
//                startActivity(DetailActivity.buildIntent(this, voa, true));
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
//        boolean isVerifySupport = SecVerify.isVerifySupport();
//        Log.e(TAG, "SecVerify.isVerifySupport()  " + isVerifySupport);
//        if (isVerifySupport && Constant.User.isPreVerifyDone) {
//            verify();
//        } else {
//            startActivity(new Intent(mContext, LoginActivity.class));
//        }
//    }
//    /**
//     * 建议提前调用预登录接口，可以加快免密登录过程，提高用户体验
//     */
//    private void preVerify() {
//        //设置在1000-10000之内
//        SecVerify.setTimeOut(5000);
//        //移动的debug tag 是CMCC-SDK,电信是CT_ 联通是PriorityAsyncTask
//        SecVerify.setDebugMode(true);
////        SecVerify.setUseCache(true);
//        SecVerify.preVerify(new PreVerifyCallback() {
//            @Override
//            public void onComplete(Void data) {
//                if (Constant.User.devMode) {
//                    Toast.makeText(mContext, "预登录成功", Toast.LENGTH_LONG).show();
//                }
//                Constant.User.isPreVerifyDone = true;
//                Log.e(TAG, "onComplete.isPreVerifyDone  " + Constant.User.isPreVerifyDone);
//                SecVerify.autoFinishOAuthPage(false);
////                SecVerify.setUiSettings(CustomizeUtils.customizeUi());
//            }
//
//            @Override
//            public void onFailure(VerifyException e) {
//                Constant.User.isPreVerifyDone = false;
//                Log.e(TAG, "onFailure.isPreVerifyDone  " + Constant.User.isPreVerifyDone);
//                String errDetail = null;
//                if (e != null){
//                    errDetail = e.getMessage();
//                }
//                Log.e(TAG, "onFailure errDetail " + errDetail);
//                if (Constant.User.devMode) {
//                    // 登录失败
//                    Log.e(TAG, "preVerify failed", e);
//                    // 错误码
//                    int errCode = e.getCode();
//                    // 错误信息
//                    String errMsg = e.getMessage();
//                    // 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
//                    String msg = "错误码: " + errCode + "\n错误信息: " + errMsg;
//                    if (!TextUtils.isEmpty(errDetail)) {
//                        msg += "\n详细信息: " + errDetail;
//                    }
//                    Log.e(TAG,msg);
//                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    /**
//     * 免密登录
//     */
//    private void verify() {
//        Log.e(TAG, "Verify called");
//        CommonProgressDialog.showProgressDialog(mContext);
//        //需要在verify之前设置
//        SecVerify.OtherOAuthPageCallBack(new OAuthPageEventCallback() {
//            @Override
//            public void initCallback(OAuthPageEventResultCallback cb) {
//                cb.pageOpenCallback(new PageOpenedCallback() {
//                    @Override
//                    public void handle() {
//                        Log.i(TAG, System.currentTimeMillis() + " pageOpened");
//                        Log.e(TAG,(System.currentTimeMillis() - starttime) + "ms is the time pageOpen take ");
//                    }
//                });
//                cb.loginBtnClickedCallback(new LoginBtnClickedCallback() {
//                    @Override
//                    public void handle() {
//                        Log.i(TAG, System.currentTimeMillis() + " loginBtnClicked");
//                    }
//                });
//                cb.agreementPageClosedCallback(new AgreementPageClosedCallback() {
//                    @Override
//                    public void handle() {
//                        Log.i(TAG, System.currentTimeMillis() + " agreementPageClosed");
//                    }
//                });
//                cb.agreementPageOpenedCallback(new AgreementClickedCallback() {
//                    @Override
//                    public void handle() {
//                        Log.i(TAG, System.currentTimeMillis() + " agreementPageOpened");
//                    }
//                });
//                cb.cusAgreement1ClickedCallback(new CusAgreement1ClickedCallback() {
//                    @Override
//                    public void handle() {
//                        Log.i(TAG, System.currentTimeMillis() + " cusAgreement1ClickedCallback");
//                    }
//                });
//                cb.cusAgreement2ClickedCallback(new CusAgreement2ClickedCallback() {
//                    @Override
//                    public void handle() {
//                        Log.i(TAG, System.currentTimeMillis() + " cusAgreement2ClickedCallback");
//                    }
//                });
//                cb.checkboxStatusChangedCallback(new CheckboxStatusChangedCallback() {
//                    @Override
//                    public void handle(boolean b) {
//                        Log.i(TAG,System.currentTimeMillis() + " current status is " + b);
//                    }
//                });
//                cb.pageCloseCallback(new PageClosedCallback() {
//                    @Override
//                    public void handle() {
//                        Log.i(TAG, System.currentTimeMillis() + " pageClosed");
//                        CommonProgressDialog.dismissProgressDialog();
//                    }
//                });
//            }
//        });
//        starttime = System.currentTimeMillis();
//        SecVerify.verify(new VerifyCallback() {
//            @Override
//            public void onOtherLogin() {
//                CommonProgressDialog.dismissProgressDialog();
//                // 用户点击“其他登录方式”，处理自己的逻辑
//                Log.e(TAG, "onOtherLogin called");
//                startActivity(new Intent(mContext, LoginActivity.class));
//            }
//            @Override
//            public void onUserCanceled() {
//                CommonProgressDialog.dismissProgressDialog();
//                SecVerify.finishOAuthPage();
//                // 用户点击“关闭按钮”或“物理返回键”取消登录，处理自己的逻辑
//                Log.e(TAG, "onUserCanceled called");
//            }
//            @Override
//            public void onComplete(VerifyResult data) {
//                // 获取授权码成功，将token信息传给应用服务端，再由应用服务端进行登录验证，此功能需由开发者自行实现
//                Log.e(TAG, "onComplete called");
//                tokenToPhone(data);
//            }
//            @Override
//            public void onFailure(VerifyException e) {
//                CommonProgressDialog.dismissProgressDialog();
//                //TODO处理失败的结果
//                Log.e(TAG, "onFailure called");
//                startActivity(new Intent(mContext, LoginActivity.class));
//                if (Constant.User.devMode) {
//                    showExceptionMsg(e);
//                }
//            }
//        });
//    }
//
//    private void tokenToPhone(VerifyResult data) {
//        CommonProgressDialog.dismissProgressDialog();
//        if (data != null) {
//            Log.e(TAG, "data.getOperator()  " + data.getOperator());
//            Log.e(TAG, "data.getOpToken()  " + data.getOpToken());
//            Log.e(TAG, "data.getToken()  " + data.getToken());
//            // 获取授权码成功，将token信息传给应用服务端，再由应用服务端进行登录验证，此功能需由开发者自行实现
//            CommonProgressDialog.showProgressDialog(mContext);
//            mMainPresenter.registerToken(data.getToken(), data.getOpToken(), data.getOperator());
//        } else {
//            Log.e(TAG, "tokenToPhone data is null.");
//            startActivity(new Intent(mContext, LoginActivity.class));
//        }
//    }
//
//    public void showExceptionMsg(VerifyException e) {
//        // 登录失败
//        if (defaultUi == 1) {
//            //失败之后不会自动关闭授权页面，需要手动关闭
//            SecVerify.finishOAuthPage();
//        }
//        CommonProgressDialog.dismissProgressDialog();
//        Log.e(TAG, "verify failed", e);
//        // 错误码
//        int errCode = e.getCode();
//        // 错误信息
//        String errMsg = e.getMessage();
//        // 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
//        Throwable t = e.getCause();
//        String errDetail = null;
//        if (t != null) {
//            errDetail = t.getMessage();
//        }
//
//        String msg = "错误码: " + errCode + "\n错误信息: " + errMsg;
//        if (!TextUtils.isEmpty(errDetail)) {
//            msg += "\n详细信息: " + errDetail;
//        }
//        if (!Constant.User.devMode) {
//            msg = "当前网络不稳定";
//            if (errCode == VerifyErr.C_NO_SIM.getCode()
//                    || errCode == VerifyErr.C_UNSUPPORTED_OPERATOR.getCode()
//                    || errCode == VerifyErr.C_CELLULAR_DISABLED.getCode()) {
//                msg = errMsg;
//            }
//        }
//        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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
//    /**
//     * 视频下载后点击
//     */
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
//    /**
//     * 获取视频模块“现在升级的点击”
//     */
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
//                        voa.getPic(),event.type, String.valueOf(voa.voaid), voa.sound));
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
//}

//上边是旧的界面，下边是新的界面
//每个项目样式不一样
public class MainActivity extends BaseViewBindingActivity<ActivityMain2Binding> {

    private static final String TAG = "MainActivity2";

    private final static String HOME = "home";
    private final static String VIDEO = "video";

    public static final int DONG_MAN_SIZE = 4;
    public static final int TING_GE_SIZE = 4;
    public static final int SPAN_COUNT = 2;

    private HomeFragment homeFragment;
    private DropdownTitleFragmentNew videoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        binding.llHome.setOnClickListener(v->{
            updateUI(v.getId());
        });
        binding.llVideo.setOnClickListener(v->{
            updateUI(v.getId());
        });

        initSlideMenu();
        updateUI(R.id.ll_home);

        //处理微课的显示
        if (AbilityControlManager.getInstance().isLimitMoc()){
            binding.llBottom.setVisibility(View.GONE);
        }else {
            binding.llBottom.setVisibility(View.VISIBLE);
        }

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initSlideMenu(){
        SlidingMenuFragment menuFragment = SlidingMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_menu_drawer, menuFragment);
        transaction.commit();
    }

    private void updateUI(int pageId){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment1 = manager.findFragmentByTag(HOME);
        Fragment fragment2 = manager.findFragmentByTag(VIDEO);

        if (fragment1!=null){
            homeFragment = (HomeFragment) fragment1;
        }

        if (fragment2!=null){
            videoFragment = (DropdownTitleFragmentNew) fragment2;
        }

        resetView();
        switch (pageId){
            case R.id.ll_home:
                if (homeFragment == null){
                    homeFragment = new HomeFragment();
                    hideFragment(transaction,videoFragment);
                    transaction.add(R.id.frame_layout,homeFragment,HOME);
                }else {
                    hideFragment(transaction,videoFragment);
                    transaction.show(homeFragment);
                }

                binding.ivHome.setSelected(true);
                binding.tvHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.ll_video:
                if (videoFragment == null){
                    //替换为小视频
                    IHeadlineManager.appId = String.valueOf(App.APP_ID);
                    IHeadlineManager.appName = App.APP_NAME_EN;

                    String[] types = new String[]{
                            HeadlineType.SMALLVIDEO
                    };

                    Bundle videoBundle = DropdownTitleFragmentNew.buildArguments(10,types,false);
                    videoFragment = DropdownTitleFragmentNew.newInstance(videoBundle);

                    hideFragment(transaction,homeFragment);
                    transaction.add(R.id.frame_layout,videoFragment,VIDEO);
                }else {
                    hideFragment(transaction,homeFragment);
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

    //微课跳转购买黄金会员
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyVIPEvent event) {
        if (UserInfoManager.getInstance().isLogin()) {
            Intent intent = new Intent(mContext, NewVipCenterActivity.class);
            intent.putExtra(NewVipCenterActivity.HUI_YUAN, NewVipCenterActivity.HUANGJIN);
            startActivity(intent);
        } else {
            NewLoginUtil.startToLogin(this);
        }
    }

    /**
     * 视频收藏界面点击跳转
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FavorItemEvent fEvent) {
        //收藏页面点击
        if (fEvent == null) {
            ToastUtil.show(this,"目前暂时不支持跳转");
            return;
        }
        BasicFavorPart part = fEvent.items.get(fEvent.position);
        switch (part.getType()){
//            case "news":
//            case "voa":
//            case "csvoa":
//                TitleTed titleTed = toTitleted(part);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Constants.BUNDLE.KEY, titleTed);
//                startActivity(DetailActivity.class, bundle);
//                break;
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

    //侧边栏
    public boolean isSlideOpen(){
        return binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void openSlide(){
        binding.mainDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeSlide(){
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
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
}
