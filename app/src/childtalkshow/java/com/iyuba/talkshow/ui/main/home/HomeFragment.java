package com.iyuba.talkshow.ui.main.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.AdNativeResponse;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.FragmentHomeBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.LoginResult;
import com.iyuba.talkshow.ui.about.AboutActivity;
import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
import com.iyuba.talkshow.ui.courses.common.OpenFlag;
import com.iyuba.talkshow.ui.courses.courseChoose.CourseChooseActivity;
import com.iyuba.talkshow.ui.courses.coursedetail.CourseDetailActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.ui.main.MainMvpView;
import com.iyuba.talkshow.ui.main.MainPresenter;
import com.iyuba.talkshow.ui.main.VoaListAdapter;
import com.iyuba.talkshow.ui.main.drawer.SlidingMenuFragment;
import com.iyuba.talkshow.ui.search.SearchActivity;
import com.iyuba.talkshow.ui.sign.SignActivity;
import com.iyuba.talkshow.ui.widget.divider.MainGridItemDivider;
import com.iyuba.talkshow.util.AdInfoFlowUtil;
import com.iyuba.talkshow.util.DialogFactory;
import com.iyuba.talkshow.util.NetStateUtil;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
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

/**
 * 主界面
 */
public class HomeFragment extends BaseViewBindingFragmet<FragmentHomeBinding> implements MainMvpView {

    private static final String TAG = "HomeFragment";

    private boolean ifresh = false;

    int pageNum = 1;

    AdInfoFlowUtil adInfoFlowUtil;
    ActionBarDrawerToggle mDrawerToggle;

    private boolean isFirst;

    @Inject
    MainPresenter homePresenter;
    @Inject
    VoaListAdapter mVoaListAdapter;
    @Inject
    ConfigManager configManager;

    FragmentHomeBinding binding;

    private MainActivity activity2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity2 = (MainActivity) context;
    }

    VoaListAdapter.VoaCallback voaCallback = new VoaListAdapter.VoaCallback() {
        @Override
        public void onVoaClick(Voa voa) {
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
    View.OnClickListener dailyBonusCallback = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (UserInfoManager.getInstance().isLogin()) {
                startActivity(new Intent(mContext, SignActivity.class));
            } else {
                showToast("请登录后再进行打卡");
                NewLoginUtil.startToLogin(mContext);
            }
        }
    };
    VoaListAdapter.MobCallback mobCallback = new VoaListAdapter.MobCallback() {
        @Override
        public void onMobCheck() {
            NewLoginUtil.startToLogin(mContext);
        }
    };
    VoaListAdapter.CourseChooseCallback coursechooseCallback = new VoaListAdapter.CourseChooseCallback() {
        @Override
        public void onClickCourse(int item) {
            if (!UserInfoManager.getInstance().isLogin()) {
                NewLoginUtil.startToLogin(mContext);
                return;
            }

            if (item == 2) {
                if (UserInfoManager.getInstance().isLogin()){
                    UserInfoManager.getInstance().initUserInfo();
                }
                ArrayList<Integer> typeIdFilter = new ArrayList<>();
                typeIdFilter.add(3);
                typeIdFilter.add(27);
                startActivity(MobClassActivity.buildIntent(mContext, 3, true, typeIdFilter));
            } else if (configManager.getCourseId() == 0) {
                CourseChooseActivity.start(mContext, OpenFlag.TO_DETAIL);
            } else {
                mContext.startActivity(CourseDetailActivity.buildIntent(mContext, configManager.getCourseId(), configManager.getCourseTitle()));
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        ifresh = false;
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
    public void onResume() {
        super.onResume();

        if (!AdBlocker.getInstance().shouldBlockAd()){
            adInfoFlowUtil = new AdInfoFlowUtil(mContext, true, new AdInfoFlowUtil.Callback() {
                @Override
                public void onADLoad(List ads) {
                    try {
                        AdNativeResponse nativeResponse = new AdNativeResponse();
                        nativeResponse.setNativeResponse((NativeResponse) ads.get(0));
                        if (!UserInfoManager.getInstance().isVip()) {
                            mVoaListAdapter.setAd(nativeResponse);
                        } else {
                            mVoaListAdapter.removeAd();
                        }
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(),container,false);
        fragmentComponent().inject(this);
        homePresenter.attachView(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

        if (UserInfoManager.getInstance().isLogin()){
            initPersonal();
        }
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.mainToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), activity2.getBinding().mainDrawerLayout, binding.mainToolbar, 0, 0);
        activity2.getBinding().mainDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        homePresenter.attachView(this);
        homePresenter.enterGroup();
        initSlidingMenu();
        homePresenter.enterGroup();
        if (new Random().nextInt(300) <= 25) {
            binding.recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        showSendBookDialog();
                    } catch (Exception var2) { }
                }
            }, 500);
        }

        if (UserInfoManager.getInstance().isLogin()) {
            IMoviesApp.setUser(String.valueOf(UserInfoManager.getInstance().getUserId()), UserInfoManager.getInstance().getVipStatus());
        }

        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        binding.refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        homePresenter.getVoas();
        homePresenter.loadLoop();
        homePresenter.getSeriesses();
        homePresenter.checkVersion();
        mVoaListAdapter.setVoaCallback(voaCallback);
        mVoaListAdapter.setLoopCallback(loopCallback);
        mVoaListAdapter.setDataChangeCallback(adapterDataRefreshCallback);
        mVoaListAdapter.setCourseClickCallback(coursechooseCallback);
        mVoaListAdapter.setDailyBonusCallback(dailyBonusCallback);
        mVoaListAdapter.setMobCallback(mobCallback);
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> refreshData());
        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreVoas());
        binding.recyclerView.setAdapter(mVoaListAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), MainActivity.SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mVoaListAdapter.getItemList().get(position) instanceof Voa ? 1 : 2;
            }
        });
        binding.recyclerView.addItemDecoration(new MainGridItemDivider(getActivity()));
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。

        setClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
        EventBus.getDefault().unregister(this);
    }

    private void initPersonal() {
        try {
            UserInfoManager.getInstance().initUserInfo();
        } catch (Exception var) {}
    }

    /*private void showFirstDialog() {
        isFirst = configManager.isFirstStart();
        Log.d("com.iyuba.talkshow", "showFirstDialog: " + isFirst);
        if (isFirst) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (App.APP_NAME_PRIVACY.equalsIgnoreCase("隐私政策")) {
                        Intent intent = WebActivity.buildIntent(mContext, Constant.Url.PROTOCOL_BJIYB_PRIVACY + App.APP_NAME_CH, App.APP_NAME_PRIVACY);
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
                        Intent intent = WebActivity.buildIntent(mContext, Constant.Url.PROTOCOL_BJIYB_USAGE + App.APP_NAME_CH, "用户协议");
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
    }*/

    private void loadMoreVoas() {
        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            showToast(R.string.please_check_network);
            dismissRefreshingView();
            return;
        }
        pageNum += 1;
        homePresenter.loadMoreChildNews(pageNum);
    }

    private void refreshData() {
        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            showToast(R.string.please_check_network);
            dismissRefreshingView();
            return;
        }
        homePresenter.syncVoa4Category();
        homePresenter.syncSeries();
        pageNum = 1;
        Log.e("onrefresh", "刷新了！");
        if (adInfoFlowUtil!=null){
            adInfoFlowUtil.reset();
        }
        homePresenter.loadVoas();
        homePresenter.loadLoop();
        ifresh = true;
    }

    private void showSendBookDialog() {
//        sendBookPop = new SendBookPop(this, findViewById(R.id.container));
//        new AlertDialog.Builder(this).setMessage("现在给应用好评可以获得由爱语吧名师团队编写的电子书哦,先到先得~~")
//                .setNegativeButton("暂不考虑", null)
//                .setPositiveButton("去好评", (dialog, which) -> startActivity(new Intent(mContext, SendBookActivity.class))).setCancelable(false).show();
    }

    private void setClick(){
        binding.reloadView.reload.setOnClickListener(v->{
            homePresenter.loadVoas();
            homePresenter.loadLoop();
        });
    }

    private void initSlidingMenu() {
        SlidingMenuFragment menuFragment = SlidingMenuFragment.newInstance();
        FragmentTransaction transaction = ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_menu_drawer, menuFragment);
        transaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public void showVoas(List<Voa> voas) {
        mVoaListAdapter.setVoas(voas);
        mVoaListAdapter.notifyDataSetChanged();
        if (adInfoFlowUtil!=null){
            adInfoFlowUtil.refreshAd();
        }
    }

    @Override
    public void showVoasByCategory(List<Voa> voas, CategoryFooter category) {
        mVoaListAdapter.setVoasByCategory(voas, category);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
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
                startActivity(DetailActivity.buildIntent(getActivity(), voa, true));
            }
            ifresh = false;
        } else {
            Log.e("startDetailAcivity", "Voa is null.");
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
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
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
    public void showMoreVoas(List<Voa> voas) {
        mVoaListAdapter.addVoas(voas);
        mVoaListAdapter.notifyDataSetChanged();
        if (adInfoFlowUtil!=null){
            adInfoFlowUtil.refreshAd();
        }
    }

    @Override
    public void goResultActivity(LoginResult data) {
        /*if (data == null) {
            Log.e(TAG, "goResultActivity LoginResult is null. ");
            Toast.makeText(mContext, "请使用用户名密码登录。", Toast.LENGTH_SHORT).show();
            NewLoginUtil.startToLogin(mContext);
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
    public void init() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        initPersonal();
        Log.d("com.iyuba.talkshow", "LoginEvent in main ");
        homePresenter.syncSeries();
        homePresenter.syncVoa4Category();
    }

    private boolean isExit = false;
    public void onBack(){
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
}
