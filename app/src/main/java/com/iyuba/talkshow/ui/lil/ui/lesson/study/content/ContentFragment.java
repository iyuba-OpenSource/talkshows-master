package com.iyuba.talkshow.ui.lil.ui.lesson.study.content;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.bean.ChapterDetailBean;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.ui.BaseViewBindingFragment;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.lib_common.util.LibRxTimer;
import com.iyuba.lib_common.util.LibToastUtil;
import com.iyuba.lib_user.event.UserRefreshEvent;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.sdk.other.NetworkUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.databinding.FragmentContentBinding;
import com.iyuba.talkshow.ui.lil.data.AdDataUtil;
import com.iyuba.talkshow.ui.lil.dialog.searchWord.SearchWordDialog;
import com.iyuba.talkshow.ui.lil.manager.StudySettingManager;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlayEvent;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlayManager;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlaySession;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.lil.view.CenterLinearLayoutManager;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.util.NetStateUtil;
import com.yd.saas.base.interfaces.AdViewBannerListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdBanner;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: 原文界面
 * @date: 2024/1/3 15:03
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description: 功能设置请参考新概念的代码，功能使用新概念中的中小学部分逻辑
 */
public class ContentFragment extends BaseViewBindingFragment<FragmentContentBinding> implements ContentView {

    //数值
    private String bookType;
    private String voaId;
    private int position = -1;
    private String bookId;
    private String unitId;

    //适配器
    private ContentAdapter contentAdapter;
    //操作
    private ContentPresenter presenter;

    //章节数据
    private BookChapterBean chapterBean;
    //详情数据
    private List<ChapterDetailBean> list;
    //播放器
    private ExoPlayer exoPlayer;
    //音频播放地址
    private String playAudioUrl = null;
    //是否可以播放
    private boolean isCanPlay = true;

    //单词查询弹窗
    private SearchWordDialog searchWordDialog;

    //ab点次数
    private long abState = 0;
    //ab开始点
    private long abStartPosition = 0;
    //ab结束点
    private long abEndPosition = 0;

    //是否最终学习报告提交了
    private boolean isSubmitReport = false;

    public static ContentFragment getInstance(String types, String voaId, int position, String bookId) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StrLibrary.types, types);
        bundle.putString(StrLibrary.voaId, voaId);
        bundle.putInt(StrLibrary.position, position);
        bundle.putString(StrLibrary.bookId, bookId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        bookType = getArguments().getString(StrLibrary.types);
        voaId = getArguments().getString(StrLibrary.voaId);
        position = getArguments().getInt(StrLibrary.position, -1);
        bookId = getArguments().getString(StrLibrary.bookId);
        unitId = getArguments().getString(StrLibrary.id);

        presenter = new ContentPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initList();
        initData();
        initClick();
        initPlayer();

        //刷新数据
        refreshData();

        //广告显示
        binding.adLayout.setVisibility(View.GONE);
        startAdTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAdTimer();

        EventBus.getDefault().unregister(this);
        closeSearchWordDialog();
    }

    /**************************************初始化***************************************/
    private void initList() {
        contentAdapter = new ContentAdapter(getActivity(), new ArrayList<>());
        CenterLinearLayoutManager manager = new CenterLinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(contentAdapter);
        contentAdapter.setOnWordSearchListener(new ContentAdapter.onWordSearchListener() {
            @Override
            public void onWordSearch(String selectText) {
                pauseAudio(false);
                if (!NetworkUtil.isConnected(getActivity())) {
                    LibToastUtil.showToast(getActivity(), "请链接网络后重试～");
                    return;
                }

                if (!TextUtils.isEmpty(selectText)) {
                    //先处理下数据
                    selectText = filterWord(selectText);

                    if (selectText.matches("^[a-zA-Z]*")) {
                        showSearchWordDialog(selectText);
                    } else {
                        LibToastUtil.showToast(getActivity(), "请取英文单词");
                    }
                } else {
                    LibToastUtil.showToast(getActivity(), "请取英文单词");
                }
            }
        });
    }

    private void initData() {
        binding.bottomLayout.setVisibility(View.GONE);
        binding.settingLayout.setVisibility(View.GONE);

        //字体设置
        showTextLanguage();

        //文本滚动设置
        showTextSync();

        //播放模式设置
        showPlayMode();

        //播放倍速设置
        float playSpeed = StudySettingManager.getInstance().getContentPlaySpeed(UserInfoManager.getInstance().getUserId());
        binding.playSpeedText.setText("倍速："+playSpeed + "x");
    }

    private void initClick() {
        binding.textLanguage.setOnClickListener(v -> {
            //切换语言
            String curType = StudySettingManager.getInstance().getContentLanguage();
            if (curType.equals(TypeLibrary.TextShowType.ALL)) {
                StudySettingManager.getInstance().setContentLanguage(TypeLibrary.TextShowType.EN);
                LibToastUtil.showToast(getActivity(),"文本切换为英语模式");
            } else if (curType.equals(TypeLibrary.TextShowType.EN)) {
                StudySettingManager.getInstance().setContentLanguage(TypeLibrary.TextShowType.ALL);
                LibToastUtil.showToast(getActivity(),"文本切换为双语模式");
            }

            showTextLanguage();
        });
        binding.preSentence.setOnClickListener(v -> {
            //上一句
            int selectIndex = contentAdapter.getSelectIndex();
            if (selectIndex==0){
                LibToastUtil.showToast(getActivity(),"当前已经是第一个了");
            }else {
                int preIndex = selectIndex-1;
                long preProgress = (long) (list.get(preIndex).getTiming()*1000L);
                exoPlayer.seekTo(preProgress);

                isSubmitReport = false;
            }
        });
        binding.playAudio.setOnClickListener(v -> {
            //播放
            if (!NetworkUtil.isConnected(getActivity())){
                LibToastUtil.showToast(getActivity(),"请链接网络后播放音频～");
                return;
            }

            if (!isCanPlay) {
                LibToastUtil.showToast(getActivity(), "正在加载音频文件～");
                return;
            }

            if (exoPlayer != null) {
                if (exoPlayer.isPlaying()) {
                    pauseAudio(false);
                } else {
                    playAudio(null);
                }
            }
        });
        binding.nextSentence.setOnClickListener(v -> {
            //下一句
            int selectIndex = contentAdapter.getSelectIndex();
            if (selectIndex==list.size()-1){
                LibToastUtil.showToast(getActivity(),"当前已经是最后一个了");
            }else {
                int nextIndex = selectIndex+1;
                long nextProgress = (long) (list.get(nextIndex).getTiming()*1000L);
                exoPlayer.seekTo(nextProgress);

                isSubmitReport = false;
            }
        });
        binding.setting.setOnClickListener(v -> {
            //设置
            if (binding.settingLayout.getVisibility() == View.VISIBLE){
                binding.settingLayout.setVisibility(View.GONE);
            }else {
                binding.settingLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.textSyncLayout.setOnClickListener(v -> {
            //文本滚动
            boolean isSync = StudySettingManager.getInstance().getContentTextSync();
            StudySettingManager.getInstance().setContentTextSync(!isSync);
            if (isSync){
                LibToastUtil.showToast(getActivity(),"文本滚动功能已关闭");
            }else {
                LibToastUtil.showToast(getActivity(),"文本滚动功能已开启");
            }

            showTextSync();
        });
        binding.abPlayLayout.setOnClickListener(v -> {
            //ab点播放
            if (exoPlayer == null||!isCanPlay) {
                LibToastUtil.showToast(getActivity(),"播放器未初始化～");
                return;
            }

            abState++;
            if (abState%3==1){
                //a点记录
                abStartPosition = exoPlayer.getCurrentPosition();
                LibToastUtil.showToast(getActivity(), "开始记录A-，再次点击即可区间播放");
            }else if (abState%3==2){
                //b点播放
                abEndPosition = exoPlayer.getCurrentPosition();
                Log.d("ab点时间记录", abStartPosition+"---"+abEndPosition);

                if (abEndPosition<=abStartPosition){
                    LibToastUtil.showToast(getActivity(),"结束时间需要大于开始时间，请重新记录");
                    abEndPosition = 0;
                    abState--;
                }else {
                    LibToastUtil.showToast(getActivity(), "开始播放A-B");
                    exoPlayer.seekTo(abStartPosition);
                    if (!exoPlayer.isPlaying()){
                        playAudio(null);
                    }
                }
            }else if (abState%3==0){
                //停止循环
                abStartPosition = 0;
                abEndPosition = 0;
                LibToastUtil.showToast(getActivity(), "区间播放已取消");
            }

            showAbPlay();
        });
        binding.playSpeedLayout.setOnClickListener(v -> {
            //播放倍速
            if (!UserInfoManager.getInstance().isLogin()){
                showAbilityDialog(true,"调速功能");
                return;
            }

            if (!UserInfoManager.getInstance().isLogin()){
                pauseAudio(false);
                NewLoginUtil.startToLogin(getActivity());
                return;
            }

            if (!UserInfoManager.getInstance().isVip()) {
                pauseAudio(false);
                showAbilityDialog(false,"调速功能");
                return;
            }

            //弹出倍速
            showPlaySpeedDialog();
        });
        binding.playModeLayout.setOnClickListener(v -> {
            //播放模式
            String playMode = StudySettingManager.getInstance().getContentPlayMode();
            if (playMode.equals(TypeLibrary.PlayModeType.ORDER_PLAY)) {
                StudySettingManager.getInstance().setContentPlayMode(TypeLibrary.PlayModeType.RANDOM_PLAY);
                LibToastUtil.showToast(getActivity(),"已切换为随机播放模式");
            } else if (playMode.equals(TypeLibrary.PlayModeType.RANDOM_PLAY)) {
                StudySettingManager.getInstance().setContentPlayMode(TypeLibrary.PlayModeType.SINGLE_SYNC);
                LibToastUtil.showToast(getActivity(),"已切换为单曲循环模式");
            }else if (playMode.equals(TypeLibrary.PlayModeType.SINGLE_SYNC)){
                StudySettingManager.getInstance().setContentPlayMode(TypeLibrary.PlayModeType.ORDER_PLAY);
                LibToastUtil.showToast(getActivity(),"已切换为顺序播放模式");
            }

            showPlayMode();
        });
        binding.playProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    exoPlayer.seekTo(progress);
                    isSubmitReport = false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playAudio(null);
            }
        });
    }

    /**************************************刷新数据***********************************/
    private void refreshData() {
        chapterBean = presenter.getChapterData(bookType, voaId);
        list = presenter.getChapterDetail(bookType, voaId);
        contentAdapter.refreshData(list);

        //显示底部
        binding.bottomLayout.setVisibility(View.VISIBLE);
    }

    /**************************************数据回调***********************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoEvent(UserRefreshEvent refreshEvent){
        //刷新数据显示
        if (UserInfoManager.getInstance().isVip()){
            //设置广告
            destoryAd();
            binding.adLayout.setVisibility(View.GONE);
            //设置倍速
            if (exoPlayer!=null){
                float playSpeed = StudySettingManager.getInstance().getContentPlaySpeed(UserInfoManager.getInstance().getUserId());
                exoPlayer.setPlaybackSpeed(playSpeed);
                binding.playSpeedText.setText("倍速："+playSpeed + "x");
            }
        }
    }

    /****************************************音频相关**********************************/
    //是否是中小学（不是中小学就是小说）
    private boolean isJunior() {
        if (bookType.equals(TypeLibrary.BookType.junior_primary)
                || bookType.equals(TypeLibrary.BookType.junior_middle)) {
            return true;
        }
        return false;
    }

    //初始化音频
    private void initPlayer() {
        chapterBean = presenter.getChapterData(bookType, voaId);
        if (chapterBean != null) {
            playAudioUrl = chapterBean.getAudioUrl();
        }

        //设置图标样式
        binding.playProgress.setProgress(0);
        binding.playTime.setText(showTime(0));
        binding.playAudio.setImageResource(R.drawable.ic_study_play);

        //是否和外面的播放数据一致
        boolean isSameInOut = false;
        if (isJunior()) {
            exoPlayer = JuniorBgPlayManager.getInstance().getPlayService().getPlayer();

            if (JuniorBgPlaySession.getInstance().getCurData() != null
                    && JuniorBgPlaySession.getInstance().getCurData().getVoaId().equals(voaId)) {
                isSameInOut = true;
                JuniorBgPlayManager.getInstance().getPlayService().setPrepare(false);
            } else {
                isSameInOut = false;
                JuniorBgPlayManager.getInstance().getPlayService().setPrepare(true);
            }
            JuniorBgPlaySession.getInstance().setPlayPosition(position);
        } else {
            /*exoPlayer = NovelBgPlayManager.getInstance().getPlayService().getPlayer();

            if (NovelBgPlaySession.getInstance().getCurData() != null
                    && NovelBgPlaySession.getInstance().getCurData().getVoaId().equals(voaId)) {
                isSameInOut = true;
                NovelBgPlayManager.getInstance().getPlayService().setPrepare(false);
            } else {
                isSameInOut = false;
                NovelBgPlayManager.getInstance().getPlayService().setPrepare(true);
            }
            NovelBgPlaySession.getInstance().setPlayPosition(position);*/
        }

        //倍速设置
        float playSpeed = StudySettingManager.getInstance().getContentPlaySpeed(UserInfoManager.getInstance().getUserId());
        exoPlayer.setPlaybackSpeed(playSpeed);

        if (isSameInOut) {
            if (exoPlayer != null) {
                if (exoPlayer.isPlaying()) {
                    //直接播放即可
                    playAudio(null);
                } else {
                    //显示进度数据并且暂停
                    binding.playTime.setText(showTime(exoPlayer.getCurrentPosition()));
                    binding.totalTime.setText(showTime(exoPlayer.getDuration()));
                    binding.playProgress.setMax((int) exoPlayer.getDuration());
                    binding.playProgress.setProgress((int) exoPlayer.getCurrentPosition());
                    int curScrolledIndex = getCurShowTextIndex();
                    contentAdapter.refreshIndex(curScrolledIndex);
                    if (StudySettingManager.getInstance().getContentTextSync()) {
                        ((CenterLinearLayoutManager) binding.recyclerView.getLayoutManager()).smoothScrollToPosition(binding.recyclerView, new RecyclerView.State(), curScrolledIndex);
                    }
                    pauseAudio(false);
                }
            } else {
                LibToastUtil.showToast(getActivity(), "播放器未初始化");
            }
        } else {
            //不一致的话则直接播放
            playAudio(playAudioUrl);
        }
    }

    //播放音频
    private void playAudio(String urlOrPath) {
        if (!TextUtils.isEmpty(urlOrPath)) {
            MediaItem mediaItem = null;
            if (urlOrPath.startsWith("http")) {
                mediaItem = MediaItem.fromUri(urlOrPath);
            } else {
                //本地加载
                Uri uri = Uri.fromFile(new File(urlOrPath));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(getActivity(), getResources().getString(R.string.file_provider_name_personal), new File(urlOrPath));
                }
                mediaItem = MediaItem.fromUri(uri);
            }
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
        } else {
            boolean isPrepare = true;
            if (isJunior()) {
                isPrepare = JuniorBgPlayManager.getInstance().getPlayService().isPrepare();
            } else {
//                isPrepare = NovelBgPlayManager.getInstance().getPlayService().isPrepare();
            }

            if (isPrepare) {
                LibToastUtil.showToast(getActivity(), "正在加载音频内容，请稍后～");
                return;
            }

            if (exoPlayer != null && !exoPlayer.isPlaying()) {
                exoPlayer.play();
            }
            //图标文本设置
            if (binding != null) {
                binding.playAudio.setImageResource(R.drawable.ic_study_pause);
            }
            //倒计时
            startTimer();
            //外部控制
            boolean isTempData = false;
            if (isJunior()) {
                isTempData = JuniorBgPlaySession.getInstance().isTempData();
            } else {
//                isTempData = NovelBgPlaySession.getInstance().isTempData();
            }
            if (!isTempData) {
                if (isJunior()) {
                    EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_play));
                } else {
//                    EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_control_play));
                }
            }
            //通知栏控制
            if (isJunior()) {
                JuniorBgPlayManager.getInstance().getPlayService().showNotification(false, true, chapterBean.getTitleEn());
            } else {
//                NovelBgPlayManager.getInstance().getPlayService().showNotification(false, true, chapterBean.getTitleEn());
            }

            if (!isCanPlay) {
                pauseAudio(false);
            }
        }
    }

    //暂停音频
    private void pauseAudio(boolean isEnd) {
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            exoPlayer.pause();
        }
        //图标
        if (binding != null) {
            binding.playAudio.setImageResource(R.drawable.ic_study_play);
        }
        //倒计时
        stopTimer();
        //外部控制
        boolean isTempData = false;
        if (isJunior()) {
            isTempData = JuniorBgPlaySession.getInstance().isTempData();
        } else {
//            isTempData = NovelBgPlaySession.getInstance().isTempData();
        }
        //通知栏控制
        if (isJunior()) {
            JuniorBgPlayManager.getInstance().getPlayService().showNotification(false, false, chapterBean.getTitleEn());
        } else {
//            NovelBgPlayManager.getInstance().getPlayService().showNotification(false, false, chapterBean.getTitleEn());
        }
        if (!isTempData) {
            if (isJunior()) {
                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_pause));
            } else {
//                EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_control_pause));
            }
        }


        //临时数据处理
        if (isTempData) {
            //直接暂停
            if (isEnd) {
                exoPlayer.seekTo(0);
                if (isJunior()) {
                    JuniorBgPlayManager.getInstance().getPlayService().setPrepare(false);
                } else {
//                    NovelBgPlayManager.getInstance().getPlayService().setPrepare(false);
                }
            }
            return;
        }

        //非临时数据
        if (UserInfoManager.getInstance().isLogin()) {
            if (isEnd) {
                //是否展示学习报告
                boolean isShowReport = StudySettingManager.getInstance().showContentStudyReport();
                submitListenReport(true, isShowReport);
                Log.d("学习报告显示", "pauseAudio: ");
            } else {
                //不用展示学习报告，直接提交就行
                submitListenReport(false, false);
            }
        } else {
            if (isEnd) {
                if (isJunior()) {
                    EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_switch));
                } else {
//                    EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_audio_switch));
                }
                //注销数据
                destroyReadFragment();
            }
        }
    }

    //接收音频操作
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJuniorPlayEvent(JuniorBgPlayEvent event) {
        //播放完成
        if (event.getShowType().equals(JuniorBgPlayEvent.event_audio_completeFinish)) {
            //暂停
            if (!isSubmitReport) {
                isSubmitReport = true;
                pauseAudio(true);
                Log.d("学习报告显示", "onJuniorPlayEvent: --播放完成");

                //判断下ab点播放是否重置
                if (!StudySettingManager.getInstance().getContentPlayMode().equals(TypeLibrary.PlayModeType.SINGLE_SYNC)){
                    abState = 0;
                    abStartPosition = 0;
                    abEndPosition = 0;
                    binding.abPlayImg.setImageResource(R.drawable.ic_study_ab_normal);
                }
            }
        }

        //播放
        if (event.getShowType().equals(JuniorBgPlayEvent.event_audio_play)) {
            //停止播放
//            EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_control_pause));

            boolean isPrepare = JuniorBgPlayManager.getInstance().getPlayService().isPrepare();
            if (exoPlayer != null && !isPrepare) {
                if (binding != null) {
                    binding.totalTime.setText(showTime(exoPlayer.getDuration()));
                    binding.playProgress.setMax((int) exoPlayer.getDuration());
                }
            }

            //这里是自动播放按钮的处理
//            if (!ConfigManager.Instance().loadAutoPlay()||!isCanPlay){
            if (!isCanPlay) {
                pauseAudio(false);
                return;
            }

            playAudio(null);
        }

        //暂停
        if (event.getShowType().equals(JuniorBgPlayEvent.event_audio_pause)) {
            pauseAudio(false);
            Log.d("学习报告显示", "onJuniorPlayEvent: --暂停");
        }
    }

    //播放计时器
    private static final String playTagTimer = "playTagTimer";

    private void startTimer() {
        if (binding != null) {
            if (exoPlayer != null) {
                //设置时间
                binding.playTime.setText(showTime(exoPlayer.getCurrentPosition()));
                binding.totalTime.setText(showTime(exoPlayer.getDuration()));
                //设置进度
                binding.playProgress.setMax((int) exoPlayer.getDuration());
                binding.playProgress.setProgress((int) exoPlayer.getCurrentPosition());
            }
        }

        LibRxTimer.getInstance().multiTimerInMain(playTagTimer, 0, 500L, new LibRxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {

                if (binding != null) {
                    //设置时间
                    binding.playTime.setText(showTime(exoPlayer.getCurrentPosition()));
                    binding.totalTime.setText(showTime(exoPlayer.getDuration()));
                    //设置进度
                    binding.playProgress.setMax((int) exoPlayer.getDuration());
                    binding.playProgress.setProgress((int) exoPlayer.getCurrentPosition());
                    //设置文本
                    int curShowIndex = getCurShowTextIndex();
                    contentAdapter.refreshIndex(curShowIndex);
                    if (StudySettingManager.getInstance().getContentTextSync()) {
                        ((CenterLinearLayoutManager) binding.recyclerView.getLayoutManager()).smoothScrollToPosition(binding.recyclerView, new RecyclerView.State(), curShowIndex);
                    }
                    //处理a、b点播放
                    if (abStartPosition != 0 && abEndPosition != 0) {
                        if (exoPlayer.getCurrentPosition() > abEndPosition) {
                            exoPlayer.seekTo(abStartPosition);
                        }
                    }
                }
            }
        });
    }

    private void stopTimer() {
        LibRxTimer.getInstance().cancelTimer(playTagTimer);
    }

    /*****************************************学习报告*********************************/
    //显示学习报告
    /*private Map<String,VoaWord2> collectWordMap = new HashMap<>();
    private NewListenStudyReportDialog reportDialog = null;
    private String reportTime = "reportTime";
    private void showContentReportDialog(String reward){
        //当前单词
        List<VoaWord2> wordList = new ArrayList<>();
        for (String key:collectWordMap.keySet()){
            wordList.add(collectWordMap.get(key));
        }

        //这里获取当前的单词数据
        List<VoaWord2> showWordList = new ArrayList<>();
        if (isJunior()
                &&!TextUtils.isEmpty(bookId)
                &&!TextUtils.isEmpty(unitId)){
            List<WordEntity_junior> juniorList = JuniorDataManager.searchWordByUnitIdFromDB(bookId,unitId);
            if (juniorList!=null&&juniorList.size()>0){
                for (int i = 0; i < juniorList.size(); i++) {
                    WordEntity_junior curWord = juniorList.get(i);
                    VoaWord2 word2 = new VoaWord2();
                    word2.word = curWord.word;
                    word2.def = curWord.def;
                    word2.pron = curWord.pron;
                    word2.audio = curWord.audio;

                    showWordList.add(word2);
                }
            }
        }

        Log.d("学习报告显示", "showReadReportDialog: ");
        reportDialog = new NewListenStudyReportDialog(mContext);
        reportDialog.setData(reward, showWordList, wordList, new DialogCallBack() {
            @Override
            public void callback() {
                boolean isTempData = false;
                if (isJunior()){
                    isTempData = JuniorBgPlaySession.getInstance().isTempData();
                }else {
                    isTempData = NovelBgPlaySession.getInstance().isTempData();
                }

                if (isTempData){
                    if (isJunior()){
                        JuniorBgPlayManager.getInstance().getPlayService().setPrepare(false);
                    }else {
                        NovelBgPlayManager.getInstance().getPlayService().setPrepare(false);
                    }
                    exoPlayer.seekTo(0);
                }else {
                    if (isJunior()){
                        EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_switch));
                    }else {
                        EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_audio_switch));
                    }
                }

                //注销数据
                destroyReadFragment();
            }
        });
        reportDialog.setOnDialogTouchListener(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                RxTimer.getInstance().cancelTimer(reportTime);
            }
        });
        reportDialog.create();
        reportDialog.show();
        RxTimer.getInstance().timerInMain(reportTime, 5000L, new RxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {
                if (reportDialog!=null){
                    reportDialog.dismiss();
                }
                RxTimer.getInstance().cancelTimer(reportTime);
            }
        });

        *//*reportDialog = ListenStudyReportDialog.getInstance()
                .init(mContext)
                .setData(reward, showWordList, wordList, new DialogCallBack() {
                    @Override
                    public void callback() {
                        boolean isTempData = false;
                        if (isJunior()){
                            isTempData = JuniorBgPlaySession.getInstance().isTempData();
                        }else {
                            isTempData = NovelBgPlaySession.getInstance().isTempData();
                        }

                        if (isTempData){
                            if (isJunior()){
                                JuniorBgPlayManager.getInstance().getPlayService().setPrepare(false);
                            }else {
                                NovelBgPlayManager.getInstance().getPlayService().setPrepare(false);
                            }
                            exoPlayer.seekTo(0);
                        }else {
                            if (isJunior()){
                                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_switch));
                            }else {
                                EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_audio_switch));
                            }
                        }

                        //注销数据
                        destroyReadFragment();
                    }
                })
                .setOnDialogTouchListener(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        RxTimer.getInstance().cancelTimer(reportTime);
                    }
                })
                .prepare()
                .show();
        RxTimer.getInstance().timerInMain(reportTime, 5000L, new RxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {
                reportDialog.closeSelf();
                RxTimer.getInstance().cancelTimer(reportTime);
            }
        });*//*
    }*/

    //提交学习报告
    private void submitListenReport(boolean isEnd,boolean isShowReport){
        /*if (isEnd&&isShowReport){
            startLoading("正在提交学习报告...");
        }

        StudyReportManager.getInstance().submitListenReportData(bookType, System.currentTimeMillis(), isEnd, voaId, new StudyReportManager.OnListenReportCallBack() {
            @Override
            public void onShowReward(String price) {
                stopLoading();

                if (isEnd){
                    if (isShowReport){
                        Log.d("学习报告显示", "onShowReward: ");

                        showReadReportDialog(price);
                    }else {
                        if (isJunior()){
                            EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_switch));
                        }else {
                            EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_audio_switch));
                        }
                        //注销数据
                        destroyReadFragment();
                    }
                }

                if (!TextUtils.isEmpty(price)){
                    double readPrice = Double.parseDouble(price);
                    if (readPrice>0){
                        EventBus.getDefault().post(new RefreshDataEvent(TypeLibrary.RefreshDataType.userInfo));
                    }
                }
            }
        });*/

        if (isEnd){
            if (isJunior()){
                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_switch));
            }else {
//                EventBus.getDefault().post(new NovelBgPlayEvent(NovelBgPlayEvent.event_audio_switch));
            }
            //注销数据
            destroyReadFragment();
        }
    }

    /*****************************************广告相关*********************************/
    //加载广告
    private void loadBannerAd() {
        if (getActivity()==null||getActivity().isFinishing()||getActivity().isDestroyed()){
            stopAdTimer();
            return;
        }

        if (AdBlocker.getInstance().shouldBlockAd()){
            binding.adLayout.setVisibility(View.GONE);
            stopAdTimer();
            return;
        }

        if (UserInfoManager.getInstance().isVip()){
            binding.adLayout.setVisibility(View.GONE);
            stopAdTimer();
            return;
        }

        if (!NetStateUtil.isConnected(getActivity())){
            binding.adLayout.setVisibility(View.GONE);
            stopAdTimer();
            return;
        }

        presenter.getBannerAd();
    }

    //广告定时器
    private static final String tag_adTimer = "adTimer";
    private static final long time_adScale = 20 * 1000L;

    private void startAdTimer() {
        LibRxTimer.getInstance().multiTimerInMain(tag_adTimer, 0,time_adScale, new LibRxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {
                Log.d("广告显示", "时间间隔显示广告");
                loadBannerAd();
            }
        });
    }

    private void stopAdTimer() {
        LibRxTimer.getInstance().cancelTimer(tag_adTimer);
    }

    //显示有道类型的广告
    private YouDaoNative youDaoNative;
    @Override
    public void showYoudaoSplashAD(String picUrl, String linkUrl) {
        Log.d("广告显示", "banner--爱语吧sdk广告--"+picUrl+"--"+linkUrl);

        try {
            if (youDaoNative==null){
                youDaoNative = new YouDaoNative(getActivity(), ConfigData.YOUDAO_AD_BANNER_CODE, new YouDaoNative.YouDaoNativeNetworkListener() {
                    @Override
                    public void onNativeLoad(NativeResponse response) {
                        if (getActivity()==null||getActivity().isFinishing()||getActivity().isDestroyed()){
                            destoryAd();
                            return;
                        }

                        if (response==null){
                            showWebSplashAD(picUrl,linkUrl);
                            return;
                        }

                        DisplayMetrics dm = getResources().getDisplayMetrics();
                        int width = dm.widthPixels;
                        int height = dm.widthPixels*7/40;
                        //设置宽高
                        ViewGroup.LayoutParams params = binding.adLayout.getLayoutParams();
                        params.width = width;
                        params.height = height;
                        binding.adLayout.setLayoutParams(params);

                        binding.adLayout.setVisibility(View.VISIBLE);
                        binding.adClose.setVisibility(View.VISIBLE);
                        binding.adTips.setVisibility(View.VISIBLE);

                        /*Glide.clear(binding.adImage);
                        Glide.with(getActivity())
                                .load(response.getMainImageUrl())
                                .asBitmap()
                                .into(binding.adImage);*/
                        LibGlide3Util.loadImg(getActivity(),response.getMainImageUrl(),0,binding.adImage);
                        response.recordImpression(binding.adImage);

                        binding.adImage.setOnClickListener(v->{
                            pauseAudio(false);
                            response.handleClick(binding.adImage);
                        });
                        binding.adClose.setOnClickListener(v->{
                            destoryAd();
                            binding.adLayout.setVisibility(View.GONE);
                        });
                    }

                    @Override
                    public void onNativeFail(NativeErrorCode nativeErrorCode) {
                        Log.d("广告显示", "开屏--有道广告--"+nativeErrorCode.getCode());
                        showWebSplashAD(picUrl,linkUrl);
                    }
                });
            }

            RequestParameters requestParameters = new RequestParameters.RequestParametersBuilder().build();
            youDaoNative.makeRequest(requestParameters);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //显示爱语吧广告sdk类型的广告
    private YdBanner ydBanner;
    @Override
    public void showIyubaSdkAD(String picUrl, String linkUrl) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.widthPixels*7/40;
        //设置宽高
        ViewGroup.LayoutParams params = binding.adLayout.getLayoutParams();
        params.width = width;
        params.height = height;
        binding.adLayout.setLayoutParams(params);

        ydBanner = new YdBanner.Builder(getActivity())
                .setWidth(width)
                .setHeight(height)
                .setMaxTimeoutSeconds(5)
                .setKey(ConfigData.IYUBA_AD_BANNER_CODE)
                .setBannerListener(new AdViewBannerListener() {
                    @Override
                    public void onReceived(View view) {
                        if (getActivity()==null||getActivity().isFinishing()||getActivity().isDestroyed()){
                            destoryAd();
                            return;
                        }

                        binding.adLayout.addView(view);
                        binding.adLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdExposure() {

                    }

                    @Override
                    public void onAdClick(String s) {
                        pauseAudio(false);
                    }

                    @Override
                    public void onClosed() {
                        destoryAd();
                        binding.adLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdFailed(YdError ydError) {
                        Log.d("广告显示", "banner--爱语吧sdk广告--"+ydError.getCode()+"--"+ydError.getMsg()+"--"+ydError.getErrorType());
                        showWebSplashAD(picUrl, linkUrl);
                    }
                }).build();
        ydBanner.requestBanner();
    }

    //显示网页类型的广告
    @Override
    public void showWebSplashAD(String picUrl, String linkUrl) {
        Log.d("广告显示", "banner--web广告--"+picUrl+"--"+linkUrl);

        if (!TextUtils.isEmpty(picUrl)){
            picUrl = AdDataUtil.fixPicUrl(picUrl);
            linkUrl = AdDataUtil.fixJumpUrl(linkUrl);
        }else {
            picUrl = AdDataUtil.localBannerADPicUrl();
            linkUrl = AdDataUtil.localBannerADJumpUrl();
        }

        String showLinkUrl = linkUrl;

        if (getActivity()==null||getActivity().isFinishing()||getActivity().isDestroyed()){
            destoryAd();
            return;
        }

        binding.adLayout.setVisibility(View.VISIBLE);
        binding.adClose.setVisibility(View.VISIBLE);
        binding.adTips.setVisibility(View.VISIBLE);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.widthPixels*7/40;
        //设置宽高
        ViewGroup.LayoutParams params = binding.adLayout.getLayoutParams();
        params.width = width;
        params.height = height;
        binding.adLayout.setLayoutParams(params);

        /*Glide.clear(binding.adImage);
        Glide.with(getActivity())
                .load(picUrl)
                .error(AdDataUtil.localBannerADPic)
                .into(binding.adImage);*/
        LibGlide3Util.loadImg(getActivity(),picUrl,AdDataUtil.localBannerADPic,binding.adImage);
        binding.adClose.setOnClickListener(v->{
            destoryAd();
            binding.adLayout.setVisibility(View.GONE);
        });
        binding.adImage.setOnClickListener(v->{
            if (TextUtils.isEmpty(showLinkUrl)){
                LibToastUtil.showToast(getActivity(),"暂无内容");
            }else {
                pauseAudio(false);
                Intent intent = WebActivity.buildIntent(getActivity(), showLinkUrl, "精彩内容");
                getActivity().startActivity(intent);
            }
        });
    }

    //销毁广告
    private void destoryAd(){
        if (ydBanner!=null){
            ydBanner.destroy();
        }
        if (youDaoNative!=null){
            youDaoNative.destroy();
        }
        stopAdTimer();
    }

    /*********************单词查询****************/
    //显示查询弹窗
    private void showSearchWordDialog(String word) {
        searchWordDialog = new SearchWordDialog(getActivity(), word);
        searchWordDialog.create();
        searchWordDialog.show();
    }

    //关闭查询弹窗
    private void closeSearchWordDialog() {
        if (searchWordDialog != null && searchWordDialog.isShowing()) {
            searchWordDialog.dismiss();
        }
    }

    /*****************************************样式显示********************************/
    //显示文本同步
    private void showTextSync() {
        boolean textSync = StudySettingManager.getInstance().getContentTextSync();
        binding.textSyncImg.setImageResource(textSync?R.drawable.ic_study_sync_selected:R.drawable.ic_study_sync_normal);
    }

    //显示播放模式
    private void showPlayMode() {
        String playMode = StudySettingManager.getInstance().getContentPlayMode();
        if (playMode.equals(TypeLibrary.PlayModeType.SINGLE_SYNC)) {
            binding.playModeImg.setImageResource(R.drawable.ic_study_mode_single);
        } else if (playMode.equals(TypeLibrary.PlayModeType.ORDER_PLAY)) {
            binding.playModeImg.setImageResource(R.drawable.ic_study_mode_order);
        } else if (playMode.equals(TypeLibrary.PlayModeType.RANDOM_PLAY)) {
            binding.playModeImg.setImageResource(R.drawable.ic_study_mode_random);
        }
    }

    //显示文本类型
    private void showTextLanguage() {
        String languageType = StudySettingManager.getInstance().getContentLanguage();
        contentAdapter.refreshShowTextType(languageType);

        if (languageType.equals(TypeLibrary.TextShowType.ALL)){
            binding.textLanguage.setImageResource(R.drawable.ic_study_language_cn);
        }else if (languageType.equals(TypeLibrary.TextShowType.EN)){
            binding.textLanguage.setImageResource(R.drawable.ic_study_language_en);
        }
    }

    //显示倍速播放弹窗
    private void showPlaySpeedDialog() {
        String[] items = new String[]{"0.5x","0.75x","1.0x","1.5x","2.0x"};
        new AlertDialog.Builder(getActivity())
                .setItems(items, (dialog, which) -> {
                    dialog.dismiss();

                    String speed = items[which];
                    float playSpeed = Float.parseFloat(speed.replace("x",""));
                    binding.playSpeedText.setText("倍速："+playSpeed + "x");
                    if (exoPlayer!=null){
                        exoPlayer.setPlaybackSpeed(playSpeed);
                    }
                    StudySettingManager.getInstance().setContentPlaySpeed(UserInfoManager.getInstance().getUserId(), playSpeed);
                })
                .create()
                .show();
    }

    //弹窗功能展示
    public void showAbilityDialog(boolean isLogin,String abName){
        String msg = null;
        if (isLogin){
            msg = "该功能需要登录后才可以使用，是否立即登录？";
        }else {
            msg = abName+"需要VIP权限，是否立即开通解锁？";
        }

        new AlertDialog.Builder(getActivity())
                .setTitle(abName)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pauseAudio(false);

                        if (isLogin){
                            NewLoginUtil.startToLogin(getActivity());
                        }else {
                            NewVipCenterActivity.start(getActivity(),NewVipCenterActivity.BENYINGYONG);
                        }
                    }
                }).setNegativeButton("取消",null)
                .show();
    }

    //显示ab点播放
    private void showAbPlay(){
        /*if (abState%3==1){
            //显示a点
            binding.abPlayImg.setImageResource(R.drawable.ic_study_ab_a);
        }else if (abState%3==2){
            //显示b点
            binding.abPlayImg.setImageResource(R.drawable.ic_study_ab_b);
        }else {
            //结束显示
            binding.abPlayImg.setImageResource(R.drawable.ic_study_ab_normal);
        }*/
    }

    /*****************************************其他功能*******************************/
    //将当前播放暂停
    public void setPlayStatus(boolean isPlay) {
        this.isCanPlay = isPlay;
        //暂停音频播放
        if (!isPlay) {
            pauseAudio(false);
        }
    }

    //时间显示
    private String showTime(long time) {
        if (time == 0) {
            return "00:00";
        }

        long totalTime = time / 1000;

        long minTime = totalTime / 60;
        long secTime = totalTime % 60;

        String showMin = "";
        String showSec = "";
        if (minTime >= 10) {
            showMin = String.valueOf(minTime);
        } else {
            showMin = "0" + String.valueOf(minTime);
        }
        if (secTime >= 10) {
            showSec = String.valueOf(secTime);
        } else {
            showSec = "0" + String.valueOf(secTime);
        }

        //这里判断下时间显示，要是太长，就直接设置为0
        if (minTime>=120){
            return "00:00";
        }

        return showMin + ":" + showSec;
    }

    //文章滚动操作
    private int getCurShowTextIndex(){
        if (exoPlayer==null){
            return 0;
        }

        long curProgress = exoPlayer.getCurrentPosition();

        if (list!=null&&list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                ChapterDetailBean detail = list.get(i);

                if (i == list.size()-1){
                    return i;
                }

                if (curProgress <= detail.getEndTiming()*1000L){
                    return i;
                }
            }
        }
        return 0;
    }

    //处理单词数据
    public String filterWord(String selectText){
        selectText = selectText.replace(".","");
        selectText = selectText.replace(",","");
        selectText = selectText.replace("!","");
        selectText = selectText.replace("?","");
        selectText = selectText.replace("'","");

        return selectText;
    }

    //处理句子数据
    public String filterSentence(String selectText){
        selectText = selectText.replace("."," ");
        selectText = selectText.replace(","," ");
        selectText = selectText.replace("!"," ");
        selectText = selectText.replace("?"," ");
        selectText = selectText.replace("'"," ");

        return selectText;
    }

    //外部处理
    public void destroyReadFragment(){
//        LibRxTimer.getInstance().cancelTimer(reportTime);
        stopTimer();
        destoryAd();
        presenter.detachView();
    }
}
