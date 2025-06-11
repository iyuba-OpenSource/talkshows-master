package com.iyuba.talkshow.ui.main.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.AdNativeResponse;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.FragmentHomeBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.LoginResult;
import com.iyuba.talkshow.ui.about.AboutActivity;
import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.lil.ui.imooc.ImoocActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.ui.main.VestBagAdapter;
import com.iyuba.talkshow.ui.main.VoaListAdapter;
import com.iyuba.talkshow.ui.main.drawer.SlidingMenuFragment;
import com.iyuba.talkshow.ui.search.SearchActivity;
import com.iyuba.talkshow.ui.sign.SignActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.ui.widget.divider.MainGridItemDivider;
import com.iyuba.talkshow.util.AdInfoFlowUtil;
import com.iyuba.talkshow.util.DialogFactory;
import com.iyuba.talkshow.util.NetStateUtil;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.analytics.MobclickAgent;
import com.youdao.sdk.common.OAIDHelper;
import com.youdao.sdk.nativeads.NativeResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import personal.iyuba.personalhomelibrary.PersonalHome;

public class HomeFragment extends BaseViewBindingFragmet<FragmentHomeBinding> implements HomeMvpView {

    public static final String TAG = "MainActivity";
    private static final int REQUEST_SDCARD_PERMISSION = 1;
    private static final int LOCATION_PERMISSION = 2;

    public static final int DONG_MAN_SIZE = 4;
    public static final int TING_GE_SIZE = 4;
    public static final int SPAN_COUNT = 2;
    private boolean ifresh = false;

    int pageNum = 1;

    AdInfoFlowUtil adInfoFlowUtil;

    private FragmentHomeBinding binding;
    private MainActivity activity2;
    ActionBarDrawerToggle mDrawerToggle;

    @Inject
    HomePresenter homePresenter;
    @Inject
    VoaListAdapter mVoaListAdapter;
    @Inject
    ConfigManager configManager;

    //是否需要处理马甲包
    private boolean isVestBagCheck = false;
    //加载弹窗
    private LoadingDialog loadingDialog;

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
            homePresenter.getVoa4Id(voaId);
        }
    };
    VoaListAdapter.DataChangeCallback adapterDataRefreshCallback = new VoaListAdapter.DataChangeCallback() {
        @Override
        public void onClick(View v, CategoryFooter category, int limit, String ids) {
            homePresenter.flag = 1;
            homePresenter.loadMoreVoas(category, limit, ids);
        }
    };
    VoaListAdapter.MobCallback mobCallback = new VoaListAdapter.MobCallback() {
        @Override
        public void onMobCheck() {
            clickLogin();
        }
    };

    View.OnClickListener dailyBonusCallback = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (UserInfoManager.getInstance().isLogin()) {
                startActivity(new Intent(mContext, SignActivity.class));
            } else {
//                showToast("请登录后再进行打卡");
                NewLoginUtil.startToLogin(getActivity());
            }
        }
    };
    private boolean isFirst;
    private VoaListAdapter.CourseChooseCallback coursechooseCallback = new VoaListAdapter.CourseChooseCallback() {
        @Override
        public void onClickCourse() {
            //这里切换为微课显示
            if (UserInfoManager.getInstance().isLogin()) {
                ImoocActivity.start(getActivity());
            } else {
                NewLoginUtil.startToLogin(getActivity());
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity2 = (MainActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        ifresh = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adInfoFlowUtil == null && !AdBlocker.getInstance().shouldBlockAd()) {
            try {
                adInfoFlowUtil = new AdInfoFlowUtil(mContext, true, new AdInfoFlowUtil.Callback() {
                    @Override
                    public void onADLoad(List ads) {
                        try {
                            AdNativeResponse nativeResponse = new AdNativeResponse();
                            nativeResponse.setNativeResponse((NativeResponse) ads.get(0));
                            if (!UserInfoManager.getInstance().isVip()) {
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
            } catch (Exception var2) {
            }
        }
        if (UserInfoManager.getInstance().isVip()) {
            Log.d("com.iyuba.talkshow", "onADLoad: is vip");
            mVoaListAdapter.removeAd();
        }
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        ifresh = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        homePresenter.detachView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        fragmentComponent().inject(this);
        homePresenter.attachView(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

        OAIDHelper.getInstance().getOAID();

        if (!App.APP_TENCENT_PRIVACY) {
            showFirstDialog();
        }
        if (UserInfoManager.getInstance().isLogin()){
            initPersonal();
            IMoviesApp.setUser(String.valueOf(UserInfoManager.getInstance().getUserId()), UserInfoManager.getInstance().getVipStatus());
        }

        //侧边栏
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mainToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), activity2.getBinding().mainDrawerLayout, binding.mainToolbar, 0, 0);
        activity2.getBinding().mainDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        initSlidingMenu();

        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
//        mSwipeRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
//        mMainPresenter.getVoas();
        homePresenter.loadLoop();
        homePresenter.getSeriesList(App.DEFAULT_SERIESID);
        homePresenter.getVoa4Category();
        homePresenter.checkVersion();
        mVoaListAdapter.setVoaCallback(voaCallback);
        mVoaListAdapter.setLoopCallback(loopCallback);
        mVoaListAdapter.setDataChangeCallback(adapterDataRefreshCallback);
        mVoaListAdapter.setCourseClickCallback(coursechooseCallback);
        mVoaListAdapter.setDailyBonusCallback(dailyBonusCallback);
        mVoaListAdapter.setMobCallback(mobCallback);
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> refreshData());
//        mSwipeRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreVoas());
        binding.recyclerView.setAdapter(mVoaListAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mVoaListAdapter.getItemList().get(position) instanceof SeriesData ? 1 : 2;
            }
        });
        binding.recyclerView.addItemDecoration(new MainGridItemDivider(getActivity()));
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。

        initClick();
    }

    @Override
    public void init() {

    }

    /************************************初始化************************************/
    private void initPersonal() {
        PersonalHome.setMainPath("com.iyuba.talkshow.ui.main.MainActivity");
        UserInfoManager.getInstance().initUserInfo();
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

    private void refreshData() {
        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            showToast(R.string.please_check_network);
            dismissRefreshingView();
            return;
        }
        homePresenter.syncVoa4Category(App.DEFAULT_SERIESID);
        homePresenter.syncSeries(App.DEFAULT_SERIESID);
        pageNum = 1;
        Log.e("onrefresh", "刷新了！");
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.reset();
        }
//        mMainPresenter.loadVoas();
        homePresenter.loadLoop();
        ifresh = true;
    }

    private void initClick(){
        binding.loadingView.getRoot().setOnClickListener(v->{
            homePresenter.loadVoas();
            homePresenter.loadLoop();
        });
    }

    /***********************************回调接口*********************************/
    @Override
    public void showAlertDialog(String msg, DialogInterface.OnClickListener ocl) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
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
        Intent intent = AboutActivity.buildIntent(getActivity(), versionCode, appUrl);
        startActivity(intent);
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
        if (isVestBagCheck) {
            VestBagAdapter adapter = new VestBagAdapter(getActivity(), list);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            binding.recyclerView.setLayoutManager(manager);
            binding.recyclerView.setAdapter(adapter);
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
        Toast.makeText(getActivity(), R.string.empty_content, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(getActivity(), getString(R.string.error_loading)).show();
    }

    @Override
    public void setBanner(List<LoopItem> loopItemList) {
        mVoaListAdapter.setBanner(loopItemList);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startDetailActivity(Voa voa) {
        if (voa != null) {
            Log.e("startDetailAcivity", "执行了");
            if (!ifresh) {
                startActivity(DetailActivity.buildIntentLimit(getActivity(), voa, true));
            }
            ifresh = false;
        } else {
            Log.e("startDetailAcivity", "Voa is null.");
        }
    }

    @Override
    public void showReloadView() {
        binding.loadingView.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissReloadView() {
        binding.loadingView.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void dismissRefreshingView() {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
    }

    public void clickLogin() {
        NewLoginUtil.startToLogin(getActivity());
    }

    @Override
    public void goResultActivity(LoginResult data) {
        /*if (data == null) {
            Log.e(TAG, "goResultActivity LoginResult is null. ");
            Toast.makeText(mContext, "请使用用户名密码登录。", Toast.LENGTH_SHORT).show();
            LoginUtil.toLogin(mContext);
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
        CommonProgressDialog.dismissProgressDialog();*/
    }

    private boolean isExit = false;

    public void onBack() {
        if (isExit) {
            getActivity().finish();
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

    /*****************下列方法未在此界面使用***************/
    @Override
    public void showWxLoginMsg(String msg) {

    }

    @Override
    public void showWxLoginResult() {

    }

    /************************************回调响应*********************************/
    //登录响应
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        Log.e(TAG, "onEvent LoginEvent");
        initPersonal();
    }

    /*************************************侧边栏*********************************/
    private void initSlidingMenu() {
        SlidingMenuFragment menuFragment = SlidingMenuFragment.getInstance();
        FragmentTransaction transaction = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_menu_drawer, menuFragment);
        transaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (activity2.isSlideOpen()) {
                    activity2.closeSlide();
                } else {
                    activity2.openSlide();
                }
                return true;
            case R.id.search:
                startActivity(new Intent(mContext, SearchActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
