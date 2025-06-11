package com.iyuba.talkshow.ui.main.home;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.SyncService;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.AdNativeResponse;
import com.iyuba.talkshow.data.model.AllWordsRespons;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.Header;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.FragmentHomeBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.ui.about.AboutActivity;
import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
import com.iyuba.talkshow.ui.courses.common.OpenFlag;
import com.iyuba.talkshow.ui.courses.courseChoose.CourseChooseActivity;
import com.iyuba.talkshow.ui.courses.coursedetail.CourseDetailActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.lil.ui.imooc.ImoocActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.ui.main.MainMvpView;
import com.iyuba.talkshow.ui.main.MainPresenter;
import com.iyuba.talkshow.ui.main.VoaListAdapter;
import com.iyuba.talkshow.ui.main.drawer.SlidingMenuFragment;
import com.iyuba.talkshow.ui.search.SearchActivity;
import com.iyuba.talkshow.ui.series.SeriesActivity;
import com.iyuba.talkshow.ui.sign.SignActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.SendBookPop;
import com.iyuba.talkshow.util.AdInfoFlowUtil;
import com.iyuba.talkshow.util.DialogFactory;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.BookLevels;
import com.iyuba.wordtest.entity.TalkShowWords;
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

import timber.log.Timber;

/**
 * 主界面
 */
public class HomeFragment extends BaseViewBindingFragmet<FragmentHomeBinding> implements MainMvpView {

    private static final String TAG = "HomeFragment";

    int pageNum = 1;

    AdInfoFlowUtil adInfoFlowUtil;

    @Inject
    MainPresenter homePresenter;
    @Inject
    VoaListAdapter mVoaListAdapter;
    @Inject
    ConfigManager configManager;

    SyncService syncService;
    private int defaultUi = 1;
    private long starttime;
    ActionBarDrawerToggle mDrawerToggle;
    private SendBookPop sendBookPop;

    private AdNativeResponse nativeResponse;
    private WordDataBase db;

    FragmentHomeBinding binding;
    private MainActivity activity2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity2 = (MainActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!AdBlocker.getInstance().shouldBlockAd()&&!UserInfoManager.getInstance().isVip()) {
            adInfoFlowUtil = new AdInfoFlowUtil(mContext, UserInfoManager.getInstance().isVip(), new AdInfoFlowUtil.Callback() {
                @Override
                public void onADLoad(List ads) {
                    try {
                        nativeResponse = new AdNativeResponse();
                        nativeResponse.setNativeResponse((NativeResponse) ads.get(0));
                        mVoaListAdapter.setAd(nativeResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).setAdRequestSize(1);
        }

        if (UserInfoManager.getInstance().isVip()) {
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Header.resetStartIndex(UserInfoManager.getInstance().isVip());
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
        db = WordDataBase.getInstance(getActivity());

        if (!App.APP_TENCENT_PRIVACY) {
            showFirstDialog();
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mainToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), activity2.getBinding().mainDrawerLayout, binding.mainToolbar, 0, 0);
        activity2.getBinding().mainDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        homePresenter.enterGroup();

        initSlidingMenu();
        initPersonal();
//        initWord();

        if (new Random().nextInt(300) <= 25) {
            binding.recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        showSendBookDialog();
                    } catch (Exception var2) {
                    }
                }
            }, 500);
        }
        if (UserInfoManager.getInstance().isLogin()) {
            IMoviesApp.setUser(String.valueOf(UserInfoManager.getInstance().getUserId()), UserInfoManager.getInstance().getVipStatus());
        }

//        mSwipeRefreshLayout.setRefreshing(true);
        homePresenter.getVoas();
        homePresenter.loadLoop();
        homePresenter.getSeriesses();
//        mMainPresenter.getAllWords();
        homePresenter.checkVersion();
        mVoaListAdapter.setVoaCallback(voaCallback);
        mVoaListAdapter.setLoopCallback(loopCallback);
        mVoaListAdapter.setDataChangeCallback(adapterDataRefreshCallback);
        mVoaListAdapter.setCourseClickCallback(coursechooseCallback);
        mVoaListAdapter.setDailyBonusCallback(dailyBonusCallback);
        mVoaListAdapter.setMobCallback(mobCallback);
        binding.refreshLayout.setOnRefreshListener(mOnRefreshListener);
        binding.recyclerView.setAdapter(mVoaListAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), MainActivity.SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mVoaListAdapter.getItemList().get(position) instanceof Voa ? 1 : 2;
            }
        });
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。

        setClick();
    }

    @Override
    public void init() {

    }

    @Override
    public void showVoas(List<Voa> voas) {
        mVoaListAdapter.setVoas(voas);
        if (adInfoFlowUtil!=null){
            adInfoFlowUtil.refreshAd();
        }
        mVoaListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showVoasByCategory(List<Voa> voas, CategoryFooter category) {
        mVoaListAdapter.setVoasByCategory(voas, category);
    }

    @Override
    public void showVoasEmpty() {
        mVoaListAdapter.setVoas(Collections.<Voa>emptyList());
        mVoaListAdapter.notifyDataSetChanged();
        if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            Toast.makeText(getActivity(), R.string.empty_content, Toast.LENGTH_LONG).show();
        } else {
            showToast("请检查是否开启了数据网络，然后下拉刷新获取数据。");
        }
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
            Timber.tag("startDetailAcivity====").e("执行了");
            startActivity(DetailActivity.buildIntent(getActivity(), voa, true));
        }
    }

    @Override
    public void showReloadView() {
        binding.reloadView.reload.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissReloadView() {
        binding.reloadView.reload.setVisibility(View.GONE);
    }

    @Override
    public void dismissRefreshingView() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlertDialog(String msg, DialogInterface.OnClickListener listener) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle(R.string.alert_title);
        alert.setMessage(msg);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.alert_btn_ok), listener);
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
    public void showMoewNewList(List<Voa> voas, CategoryFooter category) {
        mVoaListAdapter.setVoasByCategory(voas, category);
    }

    @Override
    public void onWordsReady(AllWordsRespons allWordsRespons) {
        db.getTalkShowWordsDao().insertWord(allWordsRespons.getData());
    }

    @Override
    public void refreshBookWord(int book_id, int version, List<TalkShowWords> words) {
        WordDataBase.getInstance(mContext).getTalkShowWordsDao().insertWord(words);
        BookLevels levels = WordDataBase.getInstance(mContext).getBookLevelDao().getBookLevel(book_id);
        levels.version = version;
        WordDataBase.getInstance(mContext).getBookLevelDao().updateBookLevel(levels);
        ToastUtils.showShort("更新完成");
    }

    @Override
    public void goResultActivity(com.iyuba.talkshow.ui.main.LoginResult data) {
        /*if (data == null) {
            Log.e(TAG, "goResultActivity LoginResult is null. ");
            Toast.makeText(mContext, "请使用用户名密码登录。", Toast.LENGTH_SHORT).show();
            LoginUtil.toLogin(getActivity());
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

    /**
     * 刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                showToast(R.string.please_check_network);
                dismissRefreshingView();
                return;
            }
            homePresenter.syncSeries();
            homePresenter.syncVoa4Category();
            pageNum = 1;
            if (adInfoFlowUtil!=null){
                adInfoFlowUtil.reset();
            }
            homePresenter.loadVoas();
            homePresenter.loadLoop();
//                    adInfoFlowUtil.refreshAd();
        }
    };

    private void showSendBookDialog() {
//        sendBookPop = new SendBookPop(this, findViewById(R.id.container));
//        new AlertDialog.Builder(this).setMessage("现在给应用好评可以获得由爱语吧名师团队编写的电子书哦,先到先得~~")
//                .setNegativeButton("暂不考虑", null)
//                .setPositiveButton("去好评", (dialog, which) -> startActivity(new Intent(mContext, SendBookActivity.class))).setCancelable(false).show();
    }

    private void initPersonal() {
        try {
            UserInfoManager.getInstance().initUserInfo();
        } catch (Exception var) {
        }
    }

    private void showFirstDialog() {
        boolean isFirst = configManager.isFirstStart();
        Log.d("com.iyuba.talkshow", "showFirstDialog: " + isFirst);
        if (isFirst) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
                        Intent intent = WebActivity.buildIntent(mContext, Constant.Url.PROTOCOL_IYUYAN_PRIVACY + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
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
                        Intent intent = WebActivity.buildIntent(mContext, Constant.Url.PROTOCOL_IYUYAN_USAGE + App.APP_NAME_CH, "用户协议");
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

    private void setClick() {
        binding.reloadView.reload.setOnClickListener(v -> {
            homePresenter.loadVoas();
            homePresenter.loadLoop();
        });
    }

    private void initSlidingMenu() {
        SlidingMenuFragment menuFragment = SlidingMenuFragment.newInstance();
        FragmentTransaction transaction = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_menu_drawer, menuFragment);
        transaction.commit();
    }

    private void startIntent() {
        Intent intent = new Intent(getActivity(), SyncService.class);
        activity2.bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            syncService = ((SyncService.SyncBinder) service).getSyncService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

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

    VoaListAdapter.VoaCallback voaCallback = voa -> {
        if (voa.series() == 0) {
            startDetailActivity(voa);
        } else {
            startActivity(SeriesActivity.getIntent(mContext, voa.series() + "", String.valueOf(voa.category())));
        }
    };
    VoaListAdapter.LoopCallback loopCallback = new VoaListAdapter.LoopCallback() {
        @Override
        public void onLoopClick(int voaId) {
//            Timber.tag("listAdapter").e("loopCallback  点击了");
            Log.e(TAG, "onLoopClick voaId " + voaId);
            homePresenter.getVoa4Id(voaId);
//            if (mAccountManager.checkLogin()) {
//                mMainPresenter.getVoa4Id(voaId);
//            } else {
//                clickLogin();
//            }
        }
    };
    VoaListAdapter.DataChangeCallback adapterDataRefreshCallback = new VoaListAdapter.DataChangeCallback() {
        @Override
        public void onClick(View v, CategoryFooter category, int limit, String ids) {
            Timber.tag("listAdapter").e("adapterDataRefreshCallback  点击了");
            if (category.getType() == 0) {
                pageNum += 1;
                homePresenter.loadMoreNewVoas(pageNum, category);
            } else {
                homePresenter.flag = 1;
                homePresenter.loadMoreVoas(category, limit, ids);
            }
        }
    };
    View.OnClickListener dailyBonusCallback = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (UserInfoManager.getInstance().isLogin()) {
                Intent intent = new Intent(mContext, SignActivity.class);
                startActivity(intent);
            } else {
                NewLoginUtil.startToLogin(mContext);
            }
        }
    };
    VoaListAdapter.CourseChooseCallback coursechooseCallback = new VoaListAdapter.CourseChooseCallback() {
        @Override
        public void onClickCourse(int item) {
            if (!UserInfoManager.getInstance().isLogin()) {
                NewLoginUtil.startToLogin(getActivity());
                return;
            }
            if (item == 2) {
                UserInfoManager.getInstance().initUserInfo();

                ImoocActivity.start(mContext);
            } else if (configManager.getCourseId() == 0) {
                CourseChooseActivity.start(mContext, OpenFlag.TO_DETAIL);
            } else {
                mContext.startActivity(CourseDetailActivity.buildIntent(mContext, configManager.getCourseId(), configManager.getCourseTitle()));
            }
        }
    };
    VoaListAdapter.MobCallback mobCallback = new VoaListAdapter.MobCallback() {
        @Override
        public void onMobCheck() {
            NewLoginUtil.startToLogin(getActivity());
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        initPersonal();
        Log.d("com.iyuba.talkshow", "LoginEvent in main ");
        homePresenter.syncSeries();
        homePresenter.syncVoa4Category();
    }
}
