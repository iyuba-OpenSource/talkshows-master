package com.iyuba.talkshow.ui.detail;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.event.RefreshDataEvent;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.util.LibRxTimer;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.play.OnStateChangeListener;
import com.iyuba.play.Player;
import com.iyuba.play.State;
import com.iyuba.talkshow.BuildConfig;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.ChildLockManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.databinding.ActivityDetailBinding;
import com.iyuba.talkshow.event.DownloadEvent;
import com.iyuba.talkshow.event.LoginResult;
import com.iyuba.talkshow.event.SeekbarEvent;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.detail.introduction.IntroductionFragment;
import com.iyuba.talkshow.ui.detail.ranking.RankingFragment;
import com.iyuba.talkshow.ui.detail.recommend.RecommendFragment;
import com.iyuba.talkshow.ui.dubbing.dialog.download.DownloadDialog;
import com.iyuba.talkshow.ui.lil.dialog.LoadingDialog;
import com.iyuba.talkshow.ui.lil.event.DownloadFileEvent;
import com.iyuba.talkshow.ui.lil.manager.ListenStudyManager;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.AdShowUtil;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.banner.AdBannerShowManager;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.banner.AdBannerViewBean;
import com.iyuba.talkshow.ui.lil.ui.ad.util.upload.AdUploadManager;
import com.iyuba.talkshow.ui.lil.ui.dubbing.DubbingNewActivity;
import com.iyuba.talkshow.ui.lil.ui.dubbing.DubbingNewPresenter;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.main.drawer.Share;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.BubblePopupWindow;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.ScreenUtils;
import com.iyuba.talkshow.util.StorageUtil;
import com.iyuba.talkshow.util.TimeUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.talkshow.util.UploadStudyRecordUtil;
import com.iyuba.talkshow.util.Util;
import com.iyuba.talkshow.util.VerifyUtil;
import com.iyuba.talkshow.util.VoaMediaUtil;
import com.iyuba.talkshow.util.videoView.BaseVideoControl;
import com.iyuba.wordtest.lil.fix.util.LibPermissionDialogUtil;
import com.jaeger.library.StatusBarUtil;
import com.permissionx.guolindev.PermissionX;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.analytics.MobclickAgent;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import devcontrol.DevControlActivity;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * 详情界面
 */
public class DetailActivity extends BaseViewBindingActivity<ActivityDetailBinding> implements DetailMvpView {
    public static final String TAG = "DetailActivity";
    private static final String VOA = "voa";
    private static final String BACK_TO_MAIN = "back_to_main";

    private static final String VOA_LIST = "voaList";
    private static final String TAG_DATA = "tagData";

    // 数据标识位
    private static final int TAG_LIMIT = 0;
    private static final int TAG_NO_LIMIT = 1;
    private static final int TAG_AUTO = 2;

    //循环数据
    private ArrayList<Voa> playVoaList = null;
    private int selectPosition = 0;

    private Voa mVoa;//这里处理下，经常有voaId错误的情况，在赋值的时候设置一个假的数据进去
    private boolean mIsPause;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Inject
    public DetailPresenter mPresenter;

    @Inject
    DetailDownloadPresenter mDownloadPresenter;

    Subscription s;
    private long mCurPosition;

    @Inject
    public DataManager mDataManager;
    @Inject
    ConfigManager configManager;

    UploadStudyRecordUtil studyRecordUpdateUtil;
    private int defaultUi = 1;
    private long starttime;
    private DownloadDialog mDownloadDialog;

    private NormalVideoControl mVideoControl;
    private MyOnTouchListener listener;
    private Player mAdPlayer;
    private VideoAdStateWatcher mVideoAdStateWatcher;


    //针对下载操作进行处理
    //当前是否正在保存到相册
    private boolean isSaveVideoToAlbum = false;
    //是否正在下载所需资源
    private boolean isDownloading = false;

    //播放准备
    OnPreparedListener videoPreparedListener = new OnPreparedListener() {
        @Override
        public void onPrepared() {
            if (mCurPosition != 0) {
                pauseVideoPlayer("0");
            } else {
                if (!mIsPause) {
                    binding.videoView.start();
                }
            }

            //检查是否进行处理
//            if (checkNoVip()){
//                handler.sendEmptyMessageDelayed(100,500);
//            }
        }
    };

    //播放完成
    OnCompletionListener videoCompletionListener = () -> {
        //这里要求增加视频播放完成后的
        ListenStudyManager.getInstance().setEndTime(System.currentTimeMillis());
        mPresenter.submitListenReport(mVoa.voaId());

        //小猪英语单独处理
        if (isDealByPackageName()) {
            if (playVoaList != null && (selectPosition < playVoaList.size() - 1)) {
                playNext();
            } else {
                binding.videoView.restart();
            }
        } else {
            binding.videoView.restart();
        }
//        binding.videoView.setOnPreparedListener(() -> pauseVideoPlayer("1"));
    };
    private static final int REQUECT_CODE_RECORD_AUDIO = 1111;
    private boolean collectFlag;

    public static Intent buildIntent(Context context, Voa voa, boolean backToMain) {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(VOA, voa);
        intent.putExtra(BACK_TO_MAIN, backToMain);
        return intent;
    }

    public static Intent buildIntentFromRecommend(Context context, Voa voa, boolean backToMain) {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        intent.putExtra(VOA, voa);
        intent.putExtra(BACK_TO_MAIN, backToMain);
        return intent;
    }

    //这里增加处理方式，将获取的数据进行处理并且进行自动播放处理
    //受到限制但是需要自动播放
    public static Intent buildIntentAutoPlay(Context context, List<Voa> voaList, Voa voa, boolean backToMain) {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //获取数据
        ArrayList<Voa> temp = new ArrayList<>();
        for (Voa data : voaList) {
            temp.add(data);
        }
        intent.putExtra(VOA_LIST, temp);
        intent.putExtra(VOA, voa);
        intent.putExtra(TAG_DATA, TAG_AUTO);

        intent.putExtra(BACK_TO_MAIN, backToMain);
        return intent;
    }

    //这里增加处理方式，获取数据名称或者其他信息，不是第一集则限制
    //受到限制不会自动播放
    public static Intent buildIntentLimit(Context context, Voa voa, boolean backToMain) {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(VOA, voa);
        intent.putExtra(BACK_TO_MAIN, backToMain);
        intent.putExtra(TAG_DATA, TAG_LIMIT);
        return intent;
    }

    //这里增加处理方式，不受限制
    //不受限制
    public static Intent buildIntentNoLimit(Context context, Voa voa, boolean backToMain) {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(VOA, voa);
        intent.putExtra(BACK_TO_MAIN, backToMain);
        intent.putExtra(TAG_DATA, TAG_NO_LIMIT);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ResourcesCompat.getColor(getResources(), R.color.status_bar_video, getTheme()));
        activityComponent().inject(this);
        mPresenter.attachView(this);
        mDownloadPresenter.attachView(this);
        EventBus.getDefault().register(this);
        mVoa = getIntent().getParcelableExtra(VOA);
        mPresenter.setVoa(mVoa);

        //提交当前时间
        ListenStudyManager.getInstance().setStartTime(System.currentTimeMillis());
        //获取文本数据
        mPresenter.getLessonText(mVoa.voaId());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        fixData();

        mDownloadPresenter.init(mVoa);
        initVideo();
        initView();
        initClick();
        initFragment();
        studyRecordUpdateUtil = new UploadStudyRecordUtil(UserInfoManager.getInstance().isLogin(),
                mContext, UserInfoManager.getInstance().getUserId(), mVoa.voaId(), "0", "1");//这里的mode由原来的2改为1
        if (mDownloadPresenter.checkFileExist()) {
            binding.otherLayout.ivDownload.setVisibility(View.GONE);
            mDownloadPresenter.getVoaTexts(mVoa.voaId());
        } else {
            mDownloadPresenter.syncVoaTexts(mVoa.voaId());
        }

        mDownloadDialog = new DownloadDialog(this);
        mDownloadDialog.setmOnDownloadListener(new DownloadDialog.OnDownloadListener() {
            @Override
            public void onContinue() {
                mDownloadDialog.dismiss();
            }

            @Override
            public void onCancel() {
                mDownloadPresenter.cancelDownload();
                finish();
            }
        });

        //这里根据需求，增加会员和非会员的处理
        //会员可以投屏，非会员不能投屏
        if (!isNoLimit()) {
            mVideoControl.showTVButton(false);
        } else {
            mVideoControl.showTVButton(true);
        }

        if (UserInfoManager.getInstance().isVip()) {
            mVideoControl.showSpeedButton(true);
        } else {
            mVideoControl.showSpeedButton(false);
        }

        //增加数据保存
        mPresenter.saveNoExistVoa(mVoa);

        //展示广告
        refreshAd();
    }


    private void initClick() {
        binding.otherLayout.collect.setOnClickListener(v -> onCollectClick());
        binding.otherLayout.icMore.setOnClickListener(v -> clickMore());
        binding.otherLayout.dubbing.setOnClickListener(v -> onDubbingClick());
        binding.otherLayout.ivDownload.setOnClickListener(v -> download());
        binding.otherLayout.refreshOrig.setOnClickListener(v -> refresh());
    }

    public void initVideo() {
        listener = new MyOnTouchListener(this);
        listener.setSingleTapListener(mSingleTapListener);
        mVideoControl = new NormalVideoControl(this);
        mVideoControl.setPlayPauseDrawables(getResources().getDrawable(R.drawable.play), getResources().getDrawable(R.drawable.pause));
        mVideoControl.setMode(BaseVideoControl.Mode.SHOW_AUTO);
        mVideoControl.setBackCallback(() -> {
            if (!isDownloading) {
                finish();
            } else {
                mDownloadDialog.show();
            }
        });
        //操作倍速
        mVideoControl.setToSpeedCallback(() -> {
            showSpeedDialog();
        });

        mVideoControl.setToTvCallBack(() -> s = mDataManager.getSeriesList(mVoa.voaId(), mVoa.series(), 1, 10)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Func1<List<Voa>, Observable<Pair<List<String>, List<String>>>>) voas -> {
                    List<String> stringUrl = new ArrayList<>();
                    List<String> stringTitle = new ArrayList<>();
                    for (int i = 0; i < voas.size(); i++) {
                        //替换vip的video链接
                        stringUrl.add(VoaMediaUtil.getVideoVipUrl(voas.get(i).video()));
                        stringTitle.add(voas.get(i).titleCn() + "");
                    }
                    Pair<List<String>, List<String>> pair = new Pair<>(stringUrl, stringTitle);
                    return Observable.just(pair);
                })
                .subscribe(listListPair -> chooseDevice(
                        //替换vip的video链接
//                        VoaMediaUtil.getVideoVipUrl(mVoa.voaId()),
                        VoaMediaUtil.getVideoVipUrl(mVoa.video()),
                        mVoa.titleCn(), listListPair.first, listListPair.second)));
        binding.videoView.setControls(mVideoControl);
//        mVideoControl.setOnTouchListener(listener);
        binding.videoView.setOnPreparedListener(videoPreparedListener);
        binding.videoView.setOnCompletionListener(videoCompletionListener);
//        binding.videoView.setVideoURI(mPresenter.getVideoUri());
        setVideoViewParams();

        //这里屏蔽广告信息
        if (!AdBlocker.getInstance().shouldBlockAd() && !UserInfoManager.getInstance().isVip()) {
            initAdViews();
        }
        if (UserInfoManager.getInstance().isVip()) {
            binding.videoView.setVideoURI(mPresenter.getVideoUri());

            //设置播放速度
            binding.videoView.setPlaybackSpeed(mDataManager.getVideoSpeed(UserInfoManager.getInstance().getUserId()));
        } else {
            //这里暂时去掉广告显示
//            if (mVideoAdStateWatcher != null) {
//                mVideoAdStateWatcher.getVideoAd(mPresenter.getVideoUri());
//            } else {
            recoverPlay(mPresenter.getVideoUri());
//            }
            //设置播放速度
            binding.videoView.setPlaybackSpeed(1.0F);
        }
    }

    //这里增加一个方法，主要是广告的空指针问题
    private void recoverPlay(Uri waitPlayUrl) {
        binding.textAdHint.setVisibility(View.GONE);
        //增加广告跳转
        binding.textAdHint.setOnClickListener(v -> {
            if (!UserInfoManager.getInstance().isLogin()) {
                NewLoginUtil.startToLogin(this);
                return;
            }

            VerifyUtil.startToVip(this, NewVipCenterActivity.BENYINGYONG);
        });
        binding.surfaceViewAd.setVisibility(View.GONE);
        binding.surfaceContainer.setVisibility(View.INVISIBLE);
        binding.surfaceContainer.setOnClickListener(null);
        mVideoControl.setOnTouchListener(listener);
        binding.videoView.setVisibility(View.VISIBLE);
        binding.videoView.setVideoURI(waitPlayUrl);
    }

    MyOnTouchListener.SingleTapListener mSingleTapListener = new MyOnTouchListener.SingleTapListener() {
        @Override
        public void onSingleTap() {
            if (mVideoControl != null) {
                if (mVideoControl.getControlVisibility() == View.GONE) {
                    mVideoControl.show();
                    if (binding.videoView.isPlaying()) {
                        mVideoControl.hideDelayed(VideoControls.DEFAULT_CONTROL_HIDE_DELAY);
                    }
                } else {
                    mVideoControl.hideDelayed(0);
                }
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            View decor = getWindow().getDecorView();
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            binding.fragmentContainer.setVisibility(View.GONE);
            //关闭广告
            stopAdTimer();
            AdBannerShowManager.getInstance().stopBannerAd();
            binding.adLayout.getRoot().setVisibility(View.GONE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            binding.fragmentContainer.setVisibility(View.VISIBLE);
            //判断是否开启广告
            if (isHideAdShow){
                stopAdTimer();
                AdBannerShowManager.getInstance().stopBannerAd();
                binding.adLayout.getRoot().setVisibility(View.GONE);
            }else {
                binding.adLayout.getRoot().setVisibility(View.VISIBLE);
                refreshAd();
            }
        }
        setVideoViewParams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        binding.otherLayout.refreshOrig.setClickable(true);
//            if(mCurPosition != 0) {
//                mVideoView.seekTo((int) mCurPosition);
//            }
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        pauseVideoPlayer("0");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //关闭广告
        stopAdTimer();
        AdBannerShowManager.getInstance().stopBannerAd();

        mDownloadDialog.dismiss();
        mPresenter.detachView();
        if (mDownloadPresenter != null) {
            mDownloadPresenter.cancelDownload();
            mDownloadPresenter.detachView();
        }
        EventBus.getDefault().unregister(this);

        //如果正在下载中，则删除文件
        if (isDownloading) {
            File audioFile = new File(getVoaAudioPath());
            if (audioFile.exists()) {
                audioFile.delete();
            }
            File videoFile = new File(getVoaVideoPath());
            if (videoFile.exists()) {
                videoFile.delete();
            }
        }

        if (mAdPlayer != null) {
            mAdPlayer.stopAndRelease();
        }
        if (mVideoAdStateWatcher != null) {
            mVideoAdStateWatcher.destroy();
        }
        binding.videoView.release();
        super.onDestroy();
    }

    private void setVideoViewParams() {
        ViewGroup.LayoutParams lp = binding.videoView.getLayoutParams();
        int[] screenSize = ScreenUtils.getScreenSize(this);
        lp.width = screenSize[0];
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.height = screenSize[1]; // 16 : 9
        } else {
            lp.height = (int) (lp.width * 0.5625);
        }
        binding.videoView.setLayoutParams(lp);
    }

    private void initFragment() {
        mFragmentList.add(IntroductionFragment.newInstance(mVoa.descCn()));
//        mFragmentList.add(RankingFragment.newInstance(mVoa));
        mFragmentList.add(RankingFragment.newInstance(mVoa, isNoLimit()));
        String[] titles = getResources().getStringArray(R.array.detail_page_title_default);
        if (mVoa.series() != 0) {
            mFragmentList.add(RecommendFragment.newSeriesInstance(mVoa.voaId(), mVoa.series()));
            titles = getResources().getStringArray(R.array.detail_page_title);
        }
        if (!BuildConfig.APPLICATION_ID.contains("xiaoxue") && !BuildConfig.APPLICATION_ID.contains("childenglish")
                && !BuildConfig.APPLICATION_ID.contains("primaryenglish") && !BuildConfig.APPLICATION_ID.contains("talkshow")) {
            mFragmentList.add(RecommendFragment.newInstance(mVoa.voaId(), mVoa.category()));
        }
        FragmentAdapter2 mFragmentAdapter = new FragmentAdapter2(
                this.getSupportFragmentManager(), mFragmentList, titles);
        binding.viewpager.setOffscreenPageLimit(2);
        binding.viewpager.setAdapter(mFragmentAdapter);
        binding.viewpager.setCurrentItem(0);
        binding.detailTabs.setupWithViewPager(binding.viewpager);
    }

    private void initView() {
        Drawable drawable = binding.otherLayout.difficultyRb.getProgressDrawable();
        int drawableSize = (int) getResources().getDimension(R.dimen.difficulty_image_size);
        drawable.setBounds(0, 0, drawableSize, drawableSize);
        binding.otherLayout.difficultyRb.setMax(Constant.Voa.MAX_DIFFICULTY);
        binding.otherLayout.difficultyRb.setProgress(mVoa.hotFlag());
        mPresenter.checkCollected(mVoa.voaId());
    }

    public void stopPlaying() {
        if (binding.videoView.isPlaying()) {
            mCurPosition = binding.videoView.getCurrentPosition();
//            pauseVideoPlayer("0");
        }
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setIsCollected(boolean isCollected) {
        collectFlag = isCollected;
        int resId = isCollected ? R.drawable.is_collect : R.drawable.not_collect;
        binding.otherLayout.collect.setImageDrawable(getResources().getDrawable(resId));
    }

//    @Override
//    public void setCollectTvText(int resId) {
//        mCollectTv.setText(getString(resId));
//    }

    @Override
    public void showVoaTextLit(List<VoaText> voaTextList) {
//        mDownloadPresenter.setWordCount(voaTextList, studyRecordUpdateUtil.getStudyRecord());
    }

    public void showPDFDialong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] choices = {"英文", "中英双语"};
        builder.setTitle("请选择导出文件的语言")
                .setItems(choices, (dialog, which) -> {
                    dialog.dismiss();
                    switch (which) {
                        case 0:
                            if (mDownloadPresenter.checkIsFree()) {
                                mPresenter.getPdf(mVoa.voaId(), 1);
                            } else if (UserInfoManager.getInstance().isLogin()) {
                                mDownloadPresenter.deductIntegral(DetailDownloadPresenter.PDF_ENG);
                            } else {
                                showToast("请登录后在进行操作");
                                startToLogin();
                            }
                            break;
                        case 1:
                            if (mDownloadPresenter.checkIsFree()) {
                                mPresenter.getPdf(mVoa.voaId(), 0);
                            } else if (UserInfoManager.getInstance().isLogin()) {
                                mDownloadPresenter.deductIntegral(DetailDownloadPresenter.PDF_BOTH);
                            } else {
                                showToast("请登录后在进行操作");
                                startToLogin();
                            }
                            break;

                        default:
                            break;
                    }
                });
        builder.show();
    }

    @Override
    public void onDeductIntegralSuccess(int type) {
        if (type == DetailDownloadPresenter.TYPE_DOWNLOAD) {
            startDownload();
        } else if (type == DetailDownloadPresenter.PDF_ENG) {
            showLoading("正在生成PDF文件");
            mPresenter.getPdf(mVoa.voaId(), 1);
        } else {
            showLoading("正在生成PDF文件");
            mPresenter.getPdf(mVoa.voaId(), 0);
        }
    }

    @Override
    public void showPdfFinishDialog(String url) {
        final String downloadPath = "http://apps." + Constant.Web.WEB_SUFFIX + "iyuba" + url;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        final androidx.appcompat.app.AlertDialog dialog = builder.setTitle("PDF已生成 请妥善保存。")
                .setMessage("下载链接：" + downloadPath + "\n[已复制到剪贴板]\n")
                .setNegativeButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(Intent.createChooser(intent, "请选择浏览器"));
                        } else {
                            ToastUtil.showToast(mContext, "未查找到浏览器");
                        }
                    }
                })
                .setPositiveButton("关闭", null)
//                .setNeutralButton("发送", null)
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        try {
            View v = dialog.getWindow().getDecorView().findViewById(android.R.id.message);
            if (v != null) {
                v.setOnClickListener(v1 -> {
                    Util.copy2ClipBoard(mContext, downloadPath);
                    ToastUtil.showToast(this, "PDF下载链接已复制到剪贴板");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button positive = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
        positive.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mVoa.titleCn())) {
                mDownloadPresenter.onDownloadPdf(mContext, "" + mVoa.voaId(), downloadPath);
            } else {
                mDownloadPresenter.onDownloadPdf(mContext, mVoa.titleCn().trim(), downloadPath);
            }
        });
        Button neutral = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL);
        neutral.setOnClickListener(v -> {
            String title = mVoa.title() + " PDF";
            if (App.APP_SHARE_HIDE > 0) {
                ToastUtil.showToast(this, "对不起，发送暂时不支持");
            } else {
                Share.shareMessage(mContext, App.Url.APP_ICON_URL, "", downloadPath, title, null);
            }
        });
        if (App.APP_SHARE_HIDE > 0) {
            neutral.setVisibility(View.GONE);
        }
        Util.copy2ClipBoard(mContext, downloadPath);
        ToastUtil.showToast(this, "PDF下载链接已复制到剪贴板");
    }

    //    @OnClick(R.id.collect)
    public void onCollectClick() {
        if (!UserInfoManager.getInstance().isLogin()) {
            startToLogin();
            return;
        }
        if (!collectFlag) {
            mPresenter.saveCollect(mVoa.voaId());
        } else {
            mPresenter.deleteCollect(mVoa.voaId());
        }
    }

    public void clickMore() {
        //如果正在下载，则提示
        if (isSaveVideoToAlbum) {
            ToastUtil.showToast(this, "正在保存到相册中，请稍后操作");
            return;
        }

        if (isDownloading) {
            ToastUtil.showToast(this, "正在下载音视频资源，请稍后操作");
            return;
        }

        //暂停视频播放
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
        }

        showPopUp(binding.otherLayout.icMore);
    }

    public void onDubbingClick() {
        if (mDownloadPresenter.checkFileExist()) {
            showPermissionDialog();
        } else {
            if (!NetStateUtil.isConnected(mContext)) {
                showToast(R.string.main_need_network);
            } else {
                showPermissionDialog();
            }
        }
    }

    //增加功能询问的理由
    private void showPermissionDialog() {
        List<Pair<String, Pair<String, String>>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(Manifest.permission.RECORD_AUDIO, new Pair<>("麦克风权限", "录制配音时朗读的音频，用于评测打分使用")));
        pairList.add(new Pair<>(Manifest.permission.WRITE_EXTERNAL_STORAGE, new Pair<>("存储权限", "保存配音的音频文件，用于评测打分使用")));
        LibPermissionDialogUtil.getInstance().showMsgDialog(this, pairList, new LibPermissionDialogUtil.OnPermissionResultListener() {
            @Override
            public void onGranted(boolean isSuccess) {
                if (isSuccess) {
                    requestAudioSuccess();
                }
            }
        });

        /*if (!PermissionX.isGranted(this, Manifest.permission.RECORD_AUDIO)
                || !PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("注意")
                    .setMessage("配音功能需要录音权限和读写存储权限，是否现在授权？")
                    .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askForPermisions();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else {
            requestAudioSuccess();
        }*/
    }

    /*private void askForPermisions() {
        PermissionX.init(this).permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request((granted, strings, strings2) -> {
                    if (granted) requestAudioSuccess();
                    else requestAudioFailed();
                });
    }*/

    public void requestAudioSuccess() {
        stopPlaying();
        mIsPause = true;
        long timestamp = TimeUtil.getTimeStamp();
        if (UserInfoManager.getInstance().isLogin()) {
            //根据要求，第一个可以进入，其余的不允许进入
            if (!isNoLimit()) {
                //这里暂停播放
                if (binding.videoView.isPlaying()) {
                    binding.videoView.pause();
                }

                showDubbingDialog();
            } else {
//                Intent intent = DubbingActivity.buildIntent(this, mVoa, timestamp);
//                startActivity(intent);

                //新的界面
                DubbingNewActivity.start(this, mVoa, mVoa.sound(), mVoa.video());
            }
        } else {
            startToLogin();
        }
    }


    //    @OnClick(R.id.share)
    public void onShareClick() {
        binding.otherLayout.share.setClickable(false);
        if (App.APP_SHARE_HIDE > 0) {
            ToastUtil.showToast(this, "对不起，分享暂时不支持");
        } else {
            Share.prepareVideoMessage(this, mVoa, mPresenter.getInregralService(), UserInfoManager.getInstance().getUserId());
        }
    }

    public void download() {
        //显示权限弹窗内容
        List<Pair<String, Pair<String, String>>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(Manifest.permission.WRITE_EXTERNAL_STORAGE, new Pair<>("存储权限", "用于下载并保存音频、视频文件")));
        LibPermissionDialogUtil.getInstance().showMsgDialog(this, pairList, new LibPermissionDialogUtil.OnPermissionResultListener() {
            @Override
            public void onGranted(boolean isSuccess) {
                if (isSuccess) {
                    if (mDownloadPresenter.checkIsFree()) {
                        startDownload();
                    } else {
                        showIntegralDialog();
                    }
                }
            }
        });
    }

    public void refresh() {
        mPresenter.syncVoaTexts(mVoa.voaId());
        mPresenter.loadVoas();
    }

    private void chooseDevice(String url, String title, List<String> voaUrls, List<String> voaTitles) {
        Intent intent = new Intent(this, DevControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("urls", (Serializable) voaUrls);
        intent.putExtra("titles", (Serializable) voaTitles);
        intent.putExtra("title", title);
        startActivity(intent);
        s.unsubscribe();
    }

    private void startDownload() {
        /*if (mDownloadPresenter.checkFileExist()) {
            binding.otherLayout.ivDownload.setVisibility(View.GONE);
        } else {
            if (!NetStateUtil.isConnected(mContext)) {
                showToast("网络异常");
                return;
            }

            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            isDownloading = true;

            //下载音频视频
            startLoading(getString(R.string.downloading));
            mDownloadPresenter.download();
        }*/

        if (mDownloadPresenter.checkFileExist(mVoa.voaId(), getVoaAudioPath(), getVoaVideoPath())) {
            binding.otherLayout.ivDownload.setVisibility(View.GONE);
        } else {
            if (!NetStateUtil.isConnected(mContext)) {
                showToast("网络异常");
                return;
            }

            if (!UserInfoManager.getInstance().isLogin()) {
                startToLogin();
                return;
            }

            downloadToLocal();
        }
    }

    private void showIntegralDialog() {
        new AlertDialog.Builder(mContext).setTitle("提示")
                .setMessage("非会员用户下载需消耗20积分，请确认是否下载")
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("下载", (dialog, which) -> {
                    if (UserInfoManager.getInstance().isLogin()) {
                        mDownloadPresenter.deductIntegral(DetailDownloadPresenter.TYPE_DOWNLOAD);
                    } else {
                        startToLogin();
                    }
                })
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadFinish(DownloadEvent downloadEvent) {
        switch (downloadEvent.status) {
            case DownloadEvent.Status.FINISH:
                stopLoading();
                binding.otherLayout.ivDownload.setVisibility(View.GONE);
//                setVideoAndAudio();
                //关闭这个
//                mDownloadPresenter.addFreeDownloadNumber();

                if (downloadEvent.downloadId == 1002) {
                    ToastUtil.showToast(mContext, "保存相册完成");
                    isSaveVideoToAlbum = false;
                } else {
                    ToastUtil.showToast(mContext, "下载完成");
                    isDownloading = false;
                }
                break;
            case DownloadEvent.Status.ERROR:
                stopLoading();
                binding.otherLayout.ivDownload.setVisibility(View.GONE);
                if (downloadEvent.downloadId == 1002) {
                    ToastUtil.showToast(mContext, "保存相册出错，请重试");
                    isSaveVideoToAlbum = false;
                } else {
                    ToastUtil.showToast(mContext, "下载出错，请重试");
                    isDownloading = false;
                }
                break;
            case DownloadEvent.Status.DOWNLOADING:
                startLoading(downloadEvent.msg);
                break;
            default:
                break;
        }
    }

    /*public void setVideoAndAudio() {
        try {
            if (binding.videoView.isPlaying()) {
                binding.videoView.pause();
            }
            int pos = (int) binding.videoView.getCurrentPosition();
            binding.videoView.setVideoURI(Uri.fromFile(StorageUtil.getVideoFile(this, mVoa.voaId())));
            binding.videoView.seekTo(pos - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
//

    private void pauseVideoPlayer(String flag) {
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
        }
        //关闭这里的听力操作
//        studyRecordUpdateUtil.stopStudyRecord(getApplicationContext(), mPresenter.checkLogin(), flag, mDataManager.getUploadStudyRecordService());
    }

    @Override
    public void onBackPressed() {
        if (isDownloading || isSaveVideoToAlbum) {
//            mDownloadDialog.show();
            mDownloadPresenter.cancelDownload();
            stopLoading();
            showToastShort("下载已经取消。");

            isSaveVideoToAlbum = false;
            isDownloading = false;
        } else {
            if (mVideoControl.isFullScreen()) {
                mVideoControl.exitFullScreen();
            } else {
                finish();
            }
        }
    }

    private void showPopUp(View v) {
        BubblePopupWindow popup = new BubblePopupWindow(DetailActivity.this);
        View bubbleView = LayoutInflater.from(this).inflate(R.layout.layout_popup_menu, null, false);
        LinearLayout share = bubbleView.findViewById(R.id.share);
        if (App.APP_SHARE_HIDE > 0) {
            share.setVisibility(View.GONE);
        } else {
            share.setVisibility(View.VISIBLE);
        }
        LinearLayout download = bubbleView.findViewById(R.id.download);
        LinearLayout pdf = bubbleView.findViewById(R.id.pdf);
        LinearLayout refresh = bubbleView.findViewById(R.id.refresh);
//        if (mDownloadPresenter.checkFileExist()) {
//            download.setVisibility(View.GONE);
//        }
        if (mDownloadPresenter.checkFileExist(mVoa.voaId(), getVoaAudioPath(), getVoaVideoPath())) {
            download.setVisibility(View.GONE);
        }
        LinearLayout saveVideo = bubbleView.findViewById(R.id.download_video);
        saveVideo.setVisibility(View.VISIBLE);
        //根据权限判断
        if (PermissionX.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (mDownloadPresenter.checkAlbumVideoExist(getAlbumVideoName(mVoa))) {
                saveVideo.setVisibility(View.GONE);
            }
        }

        popup.setBubbleView(bubbleView); // 设置气泡内容
        popup.show(v, Gravity.BOTTOM, 1000, true); // 显示弹窗
        View.OnClickListener listener = v1 -> {
            if (v1.getId() == R.id.share) {
                if (ChildLockManager.getInstance().isChildLock(this, true)) {
                    return;
                }

                onShareClick();
                popup.dismiss();
            } else if (v1.getId() == R.id.download) {
                if (ChildLockManager.getInstance().isChildLock(this, true)) {
                    return;
                }

                if (UserInfoManager.getInstance().isLogin()) {
                    download();
                } else {
                    startToLogin();
                }
                popup.dismiss();
            } else if (v1.getId() == R.id.pdf) {
                if (ChildLockManager.getInstance().isChildLock(this, true)) {
                    return;
                }

                if (!UserInfoManager.getInstance().isLogin()) {
                    startToLogin();
                    return;
                }

                if (UserInfoManager.getInstance().isVip()) {
                    showPDFDialong();
                } else {
                    showCreditDialong();
                }
                popup.dismiss();
            } else if (v1.getId() == R.id.refresh) {
                refresh();
                popup.dismiss();
            }
            //增加下载视频到相册
            else if (v1.getId() == R.id.download_video) {
                if (ChildLockManager.getInstance().isChildLock(this, true)) {
                    return;
                }

                if (binding.videoView != null && binding.videoView.isPlaying()) {
                    binding.videoView.pause();
                }

                //显示权限弹窗内容
                List<Pair<String, Pair<String, String>>> pairList = new ArrayList<>();
                pairList.add(new Pair<>(Manifest.permission.WRITE_EXTERNAL_STORAGE, new Pair<>("存储权限", "用于下载并保存视频文件到相册")));
                LibPermissionDialogUtil.getInstance().showMsgDialog(this, pairList, new LibPermissionDialogUtil.OnPermissionResultListener() {
                    @Override
                    public void onGranted(boolean isSuccess) {
                        if (isSuccess) {
                            checkDownloadCondition();
                        }
                    }
                });

                popup.dismiss();
            }
        };
        share.setOnClickListener(listener);
        download.setOnClickListener(listener);
        pdf.setOnClickListener(listener);
        refresh.setOnClickListener(listener);
        saveVideo.setOnClickListener(listener);
    }

    private void showCreditDialong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("非VIP用户每篇PDF需扣除20积分");
        builder.setPositiveButton("我知道了", (dialog, which) -> {
            showPDFDialong();
            dialog.dismiss();
        });
        builder.show();

    }

    private void initAdViews() {
        mVideoAdStateWatcher = new VideoAdStateWatcher();
        mAdPlayer = new Player();
        mAdPlayer.setOnStateChangeListener(mVideoAdStateWatcher);
        SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
            private boolean adSurfaceReady = false;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "ad surface view created");
                if (holder == binding.surfaceViewAd.getHolder()) {
                    adSurfaceReady = true;
                    mAdPlayer.setSurface(binding.surfaceViewAd.getHolder().getSurface());
                    if (mAdPlayer.isPausing()) {
                        mAdPlayer.restart();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "ad surface view destroyed");
                if (holder == binding.surfaceViewAd.getHolder()) {
                    adSurfaceReady = false;
                    if (mAdPlayer.isInPlayingBackState()) {
                        mAdPlayer.pause();
                    }
                }
            }
        };
        binding.surfaceViewAd.getHolder().addCallback(callback);
    }

    private class VideoAdStateWatcher implements OnStateChangeListener, YouDaoNative.YouDaoNativeNetworkListener {
        private boolean videoAdOn;
        private Uri waitPlayUrl;
        private YouDaoNative youDaoNative;
        private int countDown;

        public VideoAdStateWatcher() {
            videoAdOn = false;
            try {
                youDaoNative = new YouDaoNative(mContext, Constant.YOUDAO_VIDEO_ID, this);
            } catch (Exception var2) {
            }
        }

        public void getVideoAd(Uri waitUrl) {
            waitPlayUrl = waitUrl;
            binding.videoView.setVisibility(View.INVISIBLE);
            binding.surfaceViewAd.setVisibility(View.VISIBLE);
            if (youDaoNative != null) {
                youDaoNative.makeRequest(new RequestParameters.RequestParametersBuilder().build());
            } else {
                recoverPlay();
            }
        }

        public void destroy() {
            mDownCounter.removeCallbacksAndMessages(null);
            if (youDaoNative != null) {
                youDaoNative.destroy();
            }
        }

        @Override
        public void onStateChange(int newState) {
            switch (newState) {
                case State.PREPARED:
                    binding.surfaceViewAd.setVideoSize(mAdPlayer.getMediaPlayer().getVideoWidth(),
                            mAdPlayer.getMediaPlayer().getVideoHeight());
                    countDown = 9;
                    break;
                case State.PLAYING:
                    binding.textAdHint.setVisibility(View.VISIBLE);
                    binding.textAdHint.setText(countDown + " | 开通vip可关闭广告");
                    mDownCounter.sendEmptyMessageDelayed(0, 1000);
                    break;
                case State.PAUSED:
                    mDownCounter.removeCallbacksAndMessages(null);
                    break;
                case State.INTERRUPTED:
                case State.COMPLETED:
                case State.ERROR:
                    recoverPlay();
                    break;
            }
        }

        @Override
        public void onNativeLoad(NativeResponse nativeResponse) {
            if ((nativeResponse != null) && (nativeResponse.getVideoAd() != null) && !TextUtils.isEmpty(nativeResponse.getVideoAd().getVideoUrl())) {
                Log.e(TAG, "youdao video NativeResponse: " + nativeResponse);
                binding.surfaceContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nativeResponse.handleClick(binding.surfaceContainer);
                    }
                });
                nativeResponse.recordImpression(binding.surfaceContainer);
                videoAdOn = true;
                String url = nativeResponse.getVideoAd().getVideoUrl();
                Log.e(TAG, "youdao video url " + url);
                try {
                    mAdPlayer.startToPlay(url);
                } catch (Exception var) {
                    if (var != null) {
                        Log.e(TAG, "youdao startToPlay Exception " + var.getMessage());
                    }
                    onNativeFail(NativeErrorCode.EMPTY_AD_RESPONSE);
                }
            } else {
                Log.e(TAG, "youdao video NativeResponse is null?");
                onNativeFail(NativeErrorCode.EMPTY_AD_RESPONSE);
            }
        }

        @Override
        public void onNativeFail(NativeErrorCode errorCode) {
            if (errorCode != null) {
                Log.e(TAG, "youdao video onNativeFail! errorCode : " + errorCode.getCode());
                Log.e(TAG, "youdao video onNativeFail! error : " + errorCode.toString());
            }
            recoverPlay();
        }

        private void recoverPlay() {
            Log.e(TAG, "recoverPlay waitPlayUrl " + waitPlayUrl);
            videoAdOn = false;
            mDownCounter.removeCallbacksAndMessages(null);
            binding.textAdHint.setVisibility(View.GONE);
            binding.surfaceViewAd.setVisibility(View.GONE);
            binding.surfaceContainer.setVisibility(View.INVISIBLE);
            binding.surfaceContainer.setOnClickListener(null);
            mVideoControl.setOnTouchListener(listener);
            binding.videoView.setVisibility(View.VISIBLE);
            binding.videoView.setVideoURI(waitPlayUrl);
        }

        private final Handler mDownCounter = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                countDown -= 1;
                if (countDown > 0) {
                    binding.textAdHint.setText(countDown + " | 开通vip可关闭广告");
                    mDownCounter.sendEmptyMessageDelayed(0, 1000);
                } else {
                    if (mAdPlayer.isInPlayingBackState()) {
                        mAdPlayer.stopPlay();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void goResultActivity(LoginResult data) {
        /*if (data == null) {
            Log.e(TAG, "goResultActivity LoginResult is null. ");
            Toast.makeText(mContext, "请使用用户名密码登录。", Toast.LENGTH_SHORT).show();
            LoginUtil.toLogin(this);
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
    public void showLoading(String text) {
        startLoading(text);
    }

    @Override
    public void hideLoading() {
        stopLoading();
    }

    //判断是否处理非会员操作
    private boolean isNoLimit() {
        if (!isDealByPackageName()) {
            return true;
        }

        //不受限制：
        //1。特殊的界面跳转
        //2。vip会员
        //受到限制
        //不是vip会员且不是第一集
        if (UserInfoManager.getInstance().isVip()) {
            return true;
        }

        if (getIntent().getIntExtra(TAG_DATA, -1) == TAG_NO_LIMIT) {
            return true;
        }

        if (selectPosition == 0) {
            return true;
        }

        return false;
    }

    //展示配音弹窗
    private AlertDialog dubbingDialog;

    private void showDubbingDialog() {
        if (dubbingDialog == null) {
            dubbingDialog = new AlertDialog.Builder(mContext)
                    .setTitle("提醒")
                    .setMessage("非会员每一季仅能免费配音第一篇，会员无限制。是否开通会员?")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext, NewVipCenterActivity.class));
                        }
                    }).create();
        }
        if (!dubbingDialog.isShowing()) {
            dubbingDialog.show();
        }
    }

    //展示会员信息弹窗
    private AlertDialog timeDialog;

    private void showTimeDialog() {
        if (timeDialog == null) {
            timeDialog = new AlertDialog.Builder(mContext)
                    .setTitle("提醒")
                    .setMessage("非会员每一季仅能免费观看第一篇，其余只能观看30秒。是否开通会员?")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext, NewVipCenterActivity.class));
                        }
                    }).create();
        }
        if (!timeDialog.isShowing()) {
            timeDialog.show();
        }
    }

    //回调seekbar的数据，目前限制观看30s
    int limitTime = 30;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SeekbarEvent event) {
        if (isNoLimit()) {
            return;
        }

        int curTime = (int) (((double) event.getProgress() / event.getDuration()) * binding.videoView.getDuration() / 1000);
        //限制时间的进度
        long limitTimeProgress = limitTime * 1000L;
        //限制seekbar的进度
        long limitSeekbarProgress = (long) (((double) limitTimeProgress / binding.videoView.getDuration()) * event.getDuration());
        if (curTime >= limitTime) {
            if (binding.videoView.isPlaying()) {
                binding.videoView.pause();
            }
            binding.videoView.seekTo(limitTimeProgress);
            mVideoControl.setSeekbarProgress(limitSeekbarProgress);

            showTimeDialog();
        }
    }

    //自动播放下一篇
    private void playNext() {
        selectPosition++;
        if (selectPosition <= playVoaList.size() - 1) {
            mVoa = playVoaList.get(selectPosition);

            //下边刷新界面数据
            mPresenter.setVoa(mVoa);
            mDownloadPresenter.init(mVoa);
            //控制
            mVideoControl = new NormalVideoControl(this);
            mVideoControl.setPlayPauseDrawables(getResources().getDrawable(R.drawable.play), getResources().getDrawable(R.drawable.pause));
            mVideoControl.setMode(BaseVideoControl.Mode.SHOW_AUTO);
            mVideoControl.setBackCallback(() -> {
                if (!isDownloading) {
                    finish();
                } else {
                    mDownloadDialog.show();
                }
            });
            //操作倍速
            mVideoControl.setToSpeedCallback(() -> {
                showSpeedDialog();
            });
            mVideoControl.setToTvCallBack(() -> s = mDataManager.getSeriesList(mVoa.voaId(), mVoa.series(), 1, 10)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap((Func1<List<Voa>, Observable<Pair<List<String>, List<String>>>>) voas -> {
                        List<String> stringUrl = new ArrayList<>();
                        List<String> stringTitle = new ArrayList<>();
                        for (int i = 0; i < voas.size(); i++) {
                            //替换vip的video链接
//                            stringUrl.add(VoaMediaUtil.getVideoVipUrl(voas.get(i).voaId()));
                            stringUrl.add(VoaMediaUtil.getVideoVipUrl(voas.get(i).video()));
                            stringTitle.add(voas.get(i).titleCn() + "");
                        }
                        Pair<List<String>, List<String>> pair = new Pair<>(stringUrl, stringTitle);
                        return Observable.just(pair);
                    })
                    .subscribe(listListPair -> chooseDevice(
                            //替换vip的video链接
                            VoaMediaUtil.getVideoVipUrl(mVoa.video()),
                            mVoa.titleCn(), listListPair.first, listListPair.second)));
            binding.videoView.setControls(mVideoControl);
            //这里在处理完成后，需要判断是否需要显示投屏
            if (!isNoLimit()) {
                mVideoControl.showTVButton(false);
            } else {
                mVideoControl.showTVButton(true);
            }
            if (UserInfoManager.getInstance().isVip()) {
                mVideoControl.showSpeedButton(true);
            } else {
                mVideoControl.showSpeedButton(false);
            }

            //设置视频数据
            if (UserInfoManager.getInstance().isVip()) {
                binding.videoView.setVideoURI(mPresenter.getVideoUri());
            } else {
                if (mVideoAdStateWatcher != null) {
                    mVideoAdStateWatcher.getVideoAd(mPresenter.getVideoUri());
                } else {
                    recoverPlay(mPresenter.getVideoUri());
                }
            }
            binding.otherLayout.difficultyRb.setProgress(mVoa.hotFlag());
            mPresenter.checkCollected(mVoa.voaId());
            //fragment
            mFragmentList.clear();

            initFragment();
            //其他
            studyRecordUpdateUtil = new UploadStudyRecordUtil(UserInfoManager.getInstance().isLogin(),
                    mContext, UserInfoManager.getInstance().getUserId(), mVoa.voaId(), "0", "1");//这里的mode由原来的2改为1
            if (mDownloadPresenter.checkFileExist()) {
                binding.otherLayout.ivDownload.setVisibility(View.GONE);
                mDownloadPresenter.getVoaTexts(mVoa.voaId());
            } else {
                mDownloadPresenter.syncVoaTexts(mVoa.voaId());
            }
        }
    }

    //判断数据并解析
    private void fixData() {
        int tag = getIntent().getIntExtra(TAG_DATA, TAG_NO_LIMIT);
        if (tag == TAG_AUTO) {
            //限制但是需要自动播放
            playVoaList = getIntent().getParcelableArrayListExtra(VOA_LIST);
        }
        mVoa = getIntent().getParcelableExtra(VOA);
        selectPosition = getSelectPosition(mVoa) - 1;
    }

    //获取当前数据的选中位置(非当前集数)
    //这里需要注意下，当前根据字符匹配，如果后期变动请修改
    private int getSelectPosition(Voa curVoa) {
        try {
            String title = curVoa.descCn();
            if (TextUtils.isEmpty(title)) {
                return 0;
            }

            String positionStr = title.substring(title.indexOf("季") + 1, title.indexOf("集"));
            if (TextUtils.isEmpty(positionStr)) {
                return 0;
            }

            return Integer.parseInt(positionStr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //这里需要根据包名处理
    //只有小猪英语进行处理，其他的不进行处理
    private boolean isDealByPackageName() {
        if (getPackageName().equals("com.iyuba.talkshow.pappa")
                || getPackageName().equals("com.iyuba.talkshow.pig")) {
            return true;
        }

        return false;
    }

    //检查下载前置
    private void checkDownloadCondition() {
        if (!UserInfoManager.getInstance().isLogin()) {
            startToLogin();
            return;
        }

        if (!UserInfoManager.getInstance().isVip()) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("保存视频到相册需要VIP会员权限，是否确认开通VIP会员？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(mContext, NewVipCenterActivity.class);
                            intent.putExtra(NewVipCenterActivity.HUI_YUAN, NewVipCenterActivity.BENYINGYONG);
                            startActivity(intent);
                        }
                    }).show();
            return;
        }

        downloadVideoToAlbum();
    }

    //下载文件
    private void downloadVideoToAlbum() {
        if (!NetStateUtil.isConnected(mContext)) {
            showToast("网络异常");
            return;
        }

        isSaveVideoToAlbum = true;

        //下载视频
        startLoading("正在保存到相册");
        String videoUrl = mPresenter.getDownVideoUri().toString();
//        String videoUrl = "https://stream7.iqilu.com/10339/upload_transcode/202002/18/202002181038474liyNnnSzz.mp4";
        String fileName = getAlbumVideoName(mVoa);
        String localPath = getExternalFilesDir(null).getPath() + File.separator + fileName;
        mDownloadPresenter.downVideoAndImportAlbum(videoUrl, localPath);
        //这里以1002作为数据显示，出现1002则表示为保存视频到相册
    }

    //获取当前下载视频的名称
    private String getAlbumVideoName(Voa curVoa) {
        return curVoa.category() + "_" + curVoa.voaId() + Constant.Voa.MP4_SUFFIX;
    }

    //倍速数据
    private String[] speedArray = new String[]{"0.5x", "0.75x", "1.0x", "1.25x", "1.5x", "1.75x", "2.0x"};

    //显示倍速弹窗
    private void showSpeedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("当前倍速：" + mDataManager.getVideoSpeed(UserInfoManager.getInstance().getUserId()) + "x")
                .setItems(speedArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String speedStr = speedArray[which];
                        float speed = Float.parseFloat(speedStr.replace("x", ""));

                        binding.videoView.setPlaybackSpeed(speed);
                        mDataManager.setVideoSpeed(UserInfoManager.getInstance().getUserId(), speed);

                        dialog.dismiss();
                    }
                }).show();
    }

    private void startToLogin() {
        NewLoginUtil.startToLogin(this);
    }

    //加载弹窗
    private LoadingDialog loadingDialog;

    private void startLoading(String showMsg) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.create();
        }
        loadingDialog.setMsg(showMsg);
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    private void stopLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /***************************新的操作*****************************/
    //音频下载链接
    private String getAudioUrl() {
        String downloadUrl = null;
        String audioUrl = mVoa.sound();

        if (UserInfoManager.getInstance().isVip()) {
            String vipPrefix = "http://staticvip." + NetHostManager.getInstance().getDomainShort() + "/sounds/voa";
            if (audioUrl.startsWith("/")) {
                downloadUrl = vipPrefix + audioUrl;
            } else {
                downloadUrl = vipPrefix + "/" + audioUrl;
            }
        } else {
            String prefix = "http://staticvip." + NetHostManager.getInstance().getDomainShort() + "/sounds/voa";
            if (audioUrl.startsWith("/")) {
                downloadUrl = prefix + audioUrl;
            } else {
                downloadUrl = prefix + "/" + audioUrl;
            }
        }

        return downloadUrl;
    }

    //视频下载的链接
    private String getVideoUrl() {
        String videoUrl = mVoa.video();

        if (UserInfoManager.getInstance().isVip()) {
            return "http://staticvip." + NetHostManager.getInstance().getDomainShort() + videoUrl;
        } else {
            return "http://static0." + NetHostManager.getInstance().getDomainShort() + videoUrl;
        }
    }

    //下载的音频地址
    private String getVoaAudioPath() {
        String dirPath = StorageUtil.getMediaDir(TalkShowApplication.getContext(), mVoa.voaId()).getAbsolutePath();
        //获取路径显示
        String extendPath = "/dubbing/";
        String audioUrl = getAudioUrl();
        int index = audioUrl.lastIndexOf(".");
        if (index > 0) {
            String suffix = audioUrl.substring(index);
            extendPath = extendPath + mVoa.voaId() + suffix;
        } else {
            extendPath = extendPath + mVoa.voaId() + ".mp3";
        }

        return dirPath + extendPath;
    }

    //下载的视频地址
    private String getVoaVideoPath() {
        String dirPath = StorageUtil.getMediaDir(TalkShowApplication.getContext(), mVoa.voaId()).getAbsolutePath();
        //获取路径显示
        String extendPath = "/dubbing/";
        String audioUrl = getVideoUrl();
        int index = audioUrl.lastIndexOf(".");
        if (index > 0) {
            String suffix = audioUrl.substring(index);
            extendPath = extendPath + mVoa.voaId() + suffix;
        } else {
            extendPath = extendPath + mVoa.voaId() + ".mp4";
        }

        return dirPath + extendPath;
    }

    //下载音视频内容
    private void downloadToLocal() {
        //获取当前的音视频数据
        String localAudioPath = getVoaAudioPath();
        String localVideoPath = getVoaVideoPath();

        File audioFile = new File(localAudioPath);
        File videoFile = new File(localVideoPath);
        if (audioFile.exists() && audioFile.length() > 0
                && videoFile.exists() && videoFile.length() > 0) {
            //显示数据
            initVideo();
        } else {
            //下载音视频
            startLoading("正在下载音视频内容");
            //先删除现在的文件
            if (audioFile.exists()) {
                audioFile.delete();
            }
            if (videoFile.exists()) {
                videoFile.delete();
            }
            //下载文件
            List<Pair<String, Pair<String, String>>> downloadList = new ArrayList<>();
            downloadList.add(new Pair<>(DubbingNewPresenter.TAG_audio, new Pair<>(getAudioUrl(), localAudioPath)));
            downloadList.add(new Pair<>(DubbingNewPresenter.TAG_video, new Pair<>(getVideoUrl(), localVideoPath)));
            mDownloadPresenter.downloadFile(downloadList);
        }
    }

    //下载回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadEvent(DownloadFileEvent event) {
        switch (event.getDownloadStatus()) {
            case DownloadFileEvent.STATUS_downloading:
                //下载中
                isDownloading = true;
                startLoading(event.getShowMsg());
                break;
            case DownloadFileEvent.STATUS_error:
                //下载失败
                stopLoading();
                isDownloading = false;
                new AlertDialog.Builder(this)
                        .setTitle("下载失败")
                        .setMessage(event.getShowMsg())
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloadToLocal();
                            }
                        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setCancelable(false).create().show();
                break;
            case DownloadFileEvent.STATUS_finish:
                //下载完成
                stopLoading();
                isDownloading = false;
                initVideo();

                //下载完成后，将数据保存在本地中用于使用和显示
                mDownloadPresenter.saveDownloadDataToDB(mVoa.voaId(), getVoaAudioPath(), getVoaVideoPath());
                break;
        }
    }

    /**************************广告计时器**************************/
    //是否关闭广告了
    private boolean isHideAdShow = false;
    //广告定时器
    private static final String timer_ad = "timer_ad";
    //广告间隔时间
    private static final long adScaleTime = 20 * 1000L;

    //开始计时
    private void startAdTimer() {
        stopAdTimer();
        LibRxTimer.getInstance().multiTimerInMain(timer_ad, 0, adScaleTime, new LibRxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {
                showBannerAd();
            }
        });
    }

    //停止计时
    private void stopAdTimer() {
        LibRxTimer.getInstance().cancelTimer(timer_ad);
    }

    /*******************************新的banner广告显示**********************/
    //是否已经获取了奖励
    private boolean isGetRewardByClickAd = false;
    //显示的界面模型
    private AdBannerViewBean bannerViewBean = null;

    //显示banner广告
    private void showBannerAd() {
        //请求广告
        if (bannerViewBean == null) {
            bannerViewBean = new AdBannerViewBean(binding.adLayout.iyubaSdkAdLayout, binding.adLayout.webAdLayout, binding.adLayout.webAdImage, binding.adLayout.webAdClose, binding.adLayout.webAdTips, new AdBannerShowManager.OnAdBannerShowListener() {
                @Override
                public void onLoadFinishAd() {

                }

                @Override
                public void onAdShow(String adType) {
                    binding.adLayout.getRoot().setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdClick(String adType, boolean isJumpByUserClick, String jumpUrl) {
                    pauseVideoPlayer("0");

                    if (isJumpByUserClick) {
                        if (TextUtils.isEmpty(jumpUrl)) {
                            ToastUtil.showToast(DetailActivity.this, "暂无内容");
                            return;
                        }

                        Intent intent = new Intent();
                        intent.setClass(DetailActivity.this, WebActivity.class);
                        intent.putExtra("url", jumpUrl);
                        startActivity(intent);
                    }

                    //点击广告获取奖励
                    if (!isGetRewardByClickAd) {
                        isGetRewardByClickAd = true;

                        //获取奖励
                        String fixShowType = AdShowUtil.NetParam.AdShowPosition.show_banner;
                        String fixAdType = adType;
                        AdUploadManager.getInstance().clickAdForReward(fixShowType, fixAdType, new AdUploadManager.OnAdClickCallBackListener() {
                            @Override
                            public void showClickAdResult(boolean isSuccess, String showMsg) {
                                //直接显示信息即可
                                com.iyuba.talkshow.util.ToastUtil.showToast(TalkShowApplication.getContext(), showMsg);

                                if (isSuccess) {
                                    EventBus.getDefault().post(new RefreshDataEvent(TypeLibrary.RefreshDataType.userInfo));
                                }
                            }
                        });
                        //点击广告提交数据
                        /*List<AdLocalMarkBean> localAdList = new ArrayList<>();
                        localAdList.add(new AdLocalMarkBean(
                                fixAdType,
                                fixShowType,
                                AdShowUtil.NetParam.AdOperation.operation_click,
                                System.currentTimeMillis()/1000L
                        ));
                        AdUploadManager.getInstance().submitAdMsgData(getActivity().getPackageName(), localAdList, new AdUploadManager.OnAdSubmitCallbackListener() {
                            @Override
                            public void showSubmitAdResult(boolean isSuccess, String showMsg) {
                                //不进行处理
                            }
                        });*/
                    }
                }

                @Override
                public void onAdClose(String adType) {
                    //设置关闭
                    isHideAdShow = true;
                    //关闭界面
                    binding.adLayout.getRoot().setVisibility(View.GONE);
                    //关闭计时器
                    stopAdTimer();
                    //关闭广告
                    AdBannerShowManager.getInstance().stopBannerAd();
                }

                @Override
                public void onAdError(String adType) {

                }
            });
            AdBannerShowManager.getInstance().setShowData(this, bannerViewBean);
        }
        AdBannerShowManager.getInstance().showBannerAd();
        //重置数据
        isGetRewardByClickAd = false;
    }

    //配置广告显示
    private void refreshAd() {
        //针对oppo平台打包，暂时关闭oppo上的banner广告显示
        String channel = ChannelReaderUtil.getChannel(this);
//        String brand = Build.BRAND;
        // && (brand.toLowerCase().equals("oppo")||brand.toLowerCase().equals("oneplus")||brand.toLowerCase().equals("realme"))
        if (channel.equals("oppo")){
            return;
        }

        if (!UserInfoManager.getInstance().isVip() && !AdBlocker.getInstance().shouldBlockAd()) {
            startAdTimer();
        } else {
            binding.adLayout.getRoot().setVisibility(View.GONE);
            stopAdTimer();
            AdBannerShowManager.getInstance().stopBannerAd();
        }
    }
}
