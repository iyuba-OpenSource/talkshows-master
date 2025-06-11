package com.iyuba.talkshow.ui.lil.ui.dubbing.preview;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.devbrackets.android.exomedia.ui.widget.VideoControlsCore;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.local.entity.DubbingEntity;
import com.iyuba.lib_common.model.remote.bean.Eval_result;
import com.iyuba.lib_common.model.remote.manager.DubbingManager;
import com.iyuba.lib_common.util.LibRxTimer;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.toolbox.GsonUtils;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.databinding.FragmentPreviewNewBinding;
import com.iyuba.talkshow.ui.base.BaseFragment;
import com.iyuba.talkshow.ui.detail.MyOnTouchListener;
import com.iyuba.talkshow.ui.detail.NormalVideoControl;
import com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean.PreviewEvalBean;
import com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean.PreviewShowBean;
import com.iyuba.talkshow.ui.lil.view.exoplayer.event.SimplePlayEvent;
import com.iyuba.talkshow.ui.main.drawer.Share;
import com.iyuba.talkshow.ui.widget.LoadingAdDialog;
import com.iyuba.talkshow.util.StorageUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.talkshow.util.videoView.BaseVideoControl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 预览界面
 */
public class PreviewNewFragment extends BaseFragment implements PreviewNewView{

    //参数
    private Voa mVoa;
    private String audioUrl;
    private String videoUrl;

    //一些必要的数据
    //分享的id
    private int shuoshuoId;
    //评测单词数量
    private int evalWordCount = 0;
    //平均分
    private int averageScore = 0;
    //录音时长
    private long recordTotalTime = 0;
    //文本数据
    private List<VoaText> voaTextList;

    //布局
    private FragmentPreviewNewBinding binding;
    //数据
    @Inject
    public PreviewNewPresenter presenter;

    //视频播放器
    private ExoPlayer videoPlayer;
    //是否正常加载视频
    private boolean isPrepareVideo = false;

    //音频播放器
    private ExoPlayer audioPlayer;
    //是否正常加载音频
    private boolean isPrepareAudio = false;

    //评测播放器
    private ExoPlayer evalPlayer;
    //是否正在播放评测
    private boolean isPlayEval = false;

    //新的视频播放器
    private NormalVideoControl mVideoControl;

    //刚进入当前界面的时间
    private long enterTime = System.currentTimeMillis();

    public static PreviewNewFragment getInstance(Voa mVoa, String audioUrl, String videoUrl){
        PreviewNewFragment fragment = new PreviewNewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(StrLibrary.data,mVoa);
        bundle.putString(StrLibrary.audio,audioUrl);
        bundle.putString(StrLibrary.video,videoUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        mVoa = getArguments().getParcelable(StrLibrary.data);
        audioUrl = getArguments().getString(StrLibrary.audio);
        videoUrl = getArguments().getString(StrLibrary.video);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPreviewNewBinding.inflate(LayoutInflater.from(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //绑定
        fragmentComponent().inject(this);
        presenter.attachView(this);

        initClick();
        initPlayer();

        //显示数据
        showData();

        //刷新数据
        presenter.getLocalText(mVoa.voaId());
    }

    @Override
    public void onPause() {
        super.onPause();

        stopLoading();
        pauseVideo();
        pauseAudio();
        stopAudioTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        presenter.detachView();
    }

    /*********************************初始化***********************/
    private void initClick(){
        binding.fixBtn.setOnClickListener(v->{
            //返回
            getActivity().finish();
        });
        binding.textPublish.setOnClickListener(v->{
            //发布
            String showText = binding.textPublish.getText().toString();
            if (showText.equals("发布并分享")){
                //暂停音频
                isPlayBack = false;
                pausePlay();

                presenter.submitPublishDis(mVoa.voaId(),String.valueOf(mVoa.category()),voaTextList,averageScore,audioUrl);
                showLoading();
            }else if (showText.equals("分享给好友")){
                //暂停音频
                isPlayBack = false;
                pausePlay();

                if (shuoshuoId>0){
                    shareMarge();
                }else {
                    presenter.submitPublishDis(mVoa.voaId(),String.valueOf(mVoa.category()),voaTextList,averageScore,audioUrl);
                    showLoading();
                }
            }
        });
        binding.saveDraft.setOnClickListener(v->{
            //保存到草稿箱
            presenter.saveDataToDraft(mVoa,voaTextList.size());
        });
        binding.backVideo.setVisibility(View.INVISIBLE);
    }

    private void initPlayer(){
        //初始化视频
        videoPlayer = new ExoPlayer.Builder(getActivity()).build();
        videoPlayer.setPlayWhenReady(false);
        videoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState){
                    case Player.STATE_READY:
                        if (!isPrepareVideo){
                            //加载完成
                            isPrepareVideo = true;

                            //合成数据
                            margeEvalAudioSort();

                            //判断播放
                            if (isPrepareAudio){
                                Log.d("当前播放进度", "显示1");
                                startPlay();
                            }
                        }
                        break;
                    case Player.STATE_ENDED:
                        //播放完成
                        break;
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                isPrepareVideo = false;
                com.iyuba.wordtest.utils.ToastUtil.showToast(getActivity(),"视频播放器初始化失败");
            }
        });
        binding.videoPlayer.setPlayer(videoPlayer);
        videoPlayer.setVolume(0f);
        binding.videoPlayer.setControllerAutoShow(false);

        //加载视频
        String localVideoPath = getVoaVideoPath();
        Uri videoUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            videoUri = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName()+".fileprovider",new File(localVideoPath));
        }else {
            videoUri = Uri.fromFile(new File(localVideoPath));
        }
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        videoPlayer.setMediaItem(mediaItem);
        videoPlayer.prepare();

        //第二种方法处理
        initMedia();

        //初始化音频
        audioPlayer = new ExoPlayer.Builder(getActivity()).build();
        audioPlayer.setPlayWhenReady(false);
        audioPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState){
                    case Player.STATE_READY:
                        //加载完成
                        if (!isPrepareAudio){
                            isPrepareAudio = true;

                            if (isPrepareVideo){
                                Log.d("当前播放进度", "显示2");
                                startPlay();
                            }
                        }
                        break;
                    case Player.STATE_ENDED:
                        //播放完成
                        break;
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                com.iyuba.wordtest.utils.ToastUtil.showToast(getActivity(),"音频播放器加载失败");
            }
        });
        //加载音频
        MediaItem audioItem = null;
        String urlOrPath = getVoaAudioPath();
        if (urlOrPath.startsWith("https://")||urlOrPath.startsWith("http://")){
            audioItem = MediaItem.fromUri(urlOrPath);
        }else {
            Uri uri = null;
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                uri = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName()+".fileprovider",new File(urlOrPath));
            }else {
                uri = Uri.fromFile(new File(urlOrPath));
            }
            audioItem = MediaItem.fromUri(uri);
        }
        audioPlayer.setMediaItem(audioItem);
        audioPlayer.prepare();

        //初始化评测播放器
        evalPlayer = new ExoPlayer.Builder(getActivity()).build();
        evalPlayer.setPlayWhenReady(false);
        evalPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState){
                    case Player.STATE_READY:
                        //加载完成
                        if (!isPlayEval){
                            isPlayEval = true;

                            evalPlayer.seekTo(evalTime);
                            evalPlayer.play();
                            Log.d("当前进度", "正在播放评测音频");
                        }
                        break;
                    case Player.STATE_ENDED:
                        //播放完成
                        if (isPlayEval){
                            isPlayEval = false;
                        }
                        break;
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                com.iyuba.wordtest.utils.ToastUtil.showToast(getActivity(),"音频播放器加载失败");
            }
        });
    }

    public void initMedia() {
        /*MyOnTouchListener listener = new MyOnTouchListener(getActivity());
        listener.setSingleTapListener(new MyOnTouchListener.SingleTapListener() {
            @Override
            public void onSingleTap() {
                if (mVideoControl != null) {
                    if (mVideoControl.getControlVisibility() == View.GONE) {
                        mVideoControl.show();
                        if (binding.videoPlayer.isPlaying()) {
                            mVideoControl.hideDelayed(VideoControls.DEFAULT_CONTROL_HIDE_DELAY);
                        }
                    } else {
                        mVideoControl.hideDelayed(0);
                    }
                }
            }
        });

        mVideoControl = new NormalVideoControl(getActivity());
        mVideoControl.setPlayPauseDrawables(getResources().getDrawable(R.drawable.play), getResources().getDrawable(R.drawable.pause));
        mVideoControl.setMode(BaseVideoControl.Mode.SHOW_MANUAL);
        mVideoControl.openOrCloseBottomLayout(false);
        mVideoControl.setButtonListener(new VideoControlsButtonListener() {
            @Override
            public boolean onPlayPauseClicked() {
                return false;
            }

            @Override
            public boolean onPreviousClicked() {
                return false;
            }

            @Override
            public boolean onNextClicked() {
                return false;
            }

            @Override
            public boolean onRewindClicked() {
                return false;
            }

            @Override
            public boolean onFastForwardClicked() {
                return false;
            }
        });
        mVideoControl.setBackCallback(() -> getActivity().finish());
        mVideoControl.setIsShowPlayPause(false);
        binding.videoPlayer.setControls((VideoControlsCore) mVideoControl);
        mVideoControl.setOnTouchListener(listener);
        binding.videoPlayer.setVolume(0);
        binding.videoPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                if (!isPrepareVideo){
                    isPrepareVideo = true;
                    //合成数据
                    margeEvalAudioSort();
                    //判断播放
                    if (isPrepareAudio){
                        startPlay();
                    }
                }
            }
        });
        binding.videoPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {

            }
        });
        //加载视频
        String localVideoPath = getVoaVideoPath();
        Uri videoUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            videoUri = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName()+".fileprovider",new File(localVideoPath));
        }else {
            videoUri = Uri.fromFile(new File(localVideoPath));
        }
        binding.videoPlayer.setVideoURI(videoUri);*/
    }

    /**********************************播放视频***********************/
    //静音播放
    private void startVideo(long startTime){
//        binding.videoPlayer.seekTo(startTime);
//        binding.videoPlayer.start();
        videoPlayer.seekTo(startTime);
        videoPlayer.play();
    }

    private void pauseVideo(){
//        if (binding.videoPlayer!=null&&binding.videoPlayer.isPlaying()){
//            binding.videoPlayer.pause();
//        }
        if (videoPlayer!=null&&videoPlayer.isPlaying()){
            videoPlayer.pause();
        }
    }

    /***********************************播放音频*************************/
    private void startPlay(){
        startVideo(pauseTime);

        PreviewShowBean showBean = playList.get(playPosition);
        if (showBean.getAudioType() == PreviewShowBean.type_audio){
            startAudio(pauseTime,false);
            Log.d("当前播放进度", "背景音--"+pauseTime);
        }else {
            startAudio(pauseTime,true);
            evalTime = pauseTime-showBean.getStartTime();
            startEval(showBean.getPlayPath());
            Log.d("当前播放进度", "评测音--"+evalTime);
        }

        startAudioTimer();
    }

    private void pausePlay(){
        stopAudioTimer();
        pauseVideo();
        pauseAudio();
    }

    private void startAudio(long startTime,boolean isMute){
        audioPlayer.seekTo(startTime);
        audioPlayer.setVolume(isMute?0f:1f);
        audioPlayer.play();
    }

    private void startEval(String urlOrPath){
        Uri uri = null;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName()+".fileprovider",new File(urlOrPath));
        }else {
            uri = Uri.fromFile(new File(urlOrPath));
        }
        MediaItem mediaItem = MediaItem.fromUri(uri);
        evalPlayer.setMediaItem(mediaItem);
        evalPlayer.prepare();
    }

    private void pauseAudio(){
        if (audioPlayer!=null&&audioPlayer.isPlaying()){
            audioPlayer.pause();
        }

        if (evalPlayer!=null&&evalPlayer.isPlaying()){
            evalPlayer.pause();
            isPlayEval = false;
        }
    }

    //音频的计时器
    private static final String timer_audio = "timer_audio";
    private void startAudioTimer(){
        LibRxTimer.getInstance().multiTimerInMain(timer_audio, 0, 20L, new LibRxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {
                //当前进度
                pauseTime = audioPlayer.getCurrentPosition();
                //当前的操作
                PreviewShowBean curBean = playList.get(playPosition);
                if (curBean.getEndTime()-pauseTime<30L){

                    //下一个操作
                    playPosition = playPosition+1;
                    PreviewShowBean nextBean = playList.get(playPosition);
                    if (nextBean.getAudioType() == PreviewShowBean.type_audio){
                        pauseAudio();
                        startAudio(nextBean.getStartTime(),false);
                        Log.d("当前进度", "播放背景音");
                    }else {
                        audioPlayer.setVolume(0f);
                        startEval(nextBean.getPlayPath());
                        Log.d("当前进度", "播放评测音频--"+nextBean.getStartTime()+"--"+nextBean.getEndTime());
                    }
                }
            }
        });
    }

    private void stopAudioTimer(){
        LibRxTimer.getInstance().cancelTimer(timer_audio);
    }

    /************************************合成操作顺序*********************/
    //这里只需要开启音频的定时器就可以了，评测的音频在结束后自动处理

    //评测的时间
    private long evalTime = 0;

    //当前的播放位置
    private int playPosition = 0;
    //当前的播放/暂停时间
    private long pauseTime = 0;

    //总数据
    private List<PreviewShowBean> playList = new ArrayList<>();

    //获取评测音频的顺序
    private void margeEvalAudioSort(){
        //先合并文本和评测数据
        List<PreviewShowBean> evalList = new ArrayList<>();

        for (int i = 0; i < voaTextList.size(); i++) {
            VoaText temp = voaTextList.get(i);
            DubbingEntity entity = DubbingManager.getSingleDubbingData(UserInfoManager.getInstance().getUserId(), mVoa.voaId(), temp.paraId(),temp.idIndex());
            if (entity!=null){
                long startTime = (long) (temp.timing()*1000L);

                evalList.add(new PreviewShowBean(
                        startTime,
                        entity.recordTime,
                        startTime+entity.recordTime,
                        PreviewShowBean.type_eval,
                        getRecordEvalPath(mVoa.voaId(), temp.paraId(), temp.idIndex())
                ));
            }
        }

        //再添加音频数据
        for (int i = 0; i < evalList.size(); i++) {
            PreviewShowBean showBean = evalList.get(i);

            if (i==0){
                if (showBean.getStartTime()>0){
                    //添加音频显示
                    playList.add(new PreviewShowBean(
                            0,
                            showBean.getStartTime(),
                            showBean.getStartTime(),
                            PreviewShowBean.type_audio,
                            getVoaAudioPath()
                    ));
                }
                playList.add(showBean);

                if (evalList.size()==1){
//                    long videoDuration = binding.videoPlayer.getDuration();
                    long videoDuration = videoPlayer.getDuration();

                    if (videoDuration-showBean.getEndTime()>200L){
                        playList.add(new PreviewShowBean(
                                showBean.getEndTime(),
                                videoDuration-showBean.getEndTime(),
                                videoDuration,
                                PreviewShowBean.type_audio,
                                getVoaAudioPath()
                        ));
                    }
                }
            }else if (i==evalList.size()-1){
                //上一个
                PreviewShowBean preBean = evalList.get(i-1);
                long preScaleTime = showBean.getStartTime()-preBean.getEndTime();
                if (preScaleTime>0){
                    playList.add(new PreviewShowBean(
                            preBean.getEndTime(),
                            preScaleTime,
                            showBean.getStartTime(),
                            PreviewShowBean.type_audio,
                            getVoaAudioPath()
                    ));
                }

                //增加最后一个
                playList.add(showBean);

//                long videoDuration = videoPlayer.getDuration();
                long videoDuration = videoPlayer.getDuration();

                long lastScaleTime = videoDuration-showBean.getEndTime();
                if (lastScaleTime>200L){
                    playList.add(new PreviewShowBean(
                            showBean.getEndTime(),
                            lastScaleTime,
                            videoDuration,
                            PreviewShowBean.type_audio,
                            getVoaAudioPath()
                    ));
                }
            }else {
                PreviewShowBean preBean = evalList.get(i-1);
                if (preBean.getEndTime()<showBean.getStartTime()){
                    playList.add(new PreviewShowBean(
                            preBean.getEndTime(),
                            showBean.getStartTime()-preBean.getEndTime(),
                            showBean.getStartTime(),
                            PreviewShowBean.type_audio,
                            getVoaAudioPath()
                    ));
                }
                playList.add(showBean);
            }
        }
    }

    /***********************************其他操作***********************/
    //显示数据
    private void showData(){
        List<DubbingEntity> dubbingList = DubbingManager.getMultiDubbingData(UserInfoManager.getInstance().getUserId(), mVoa.voaId());

        long wordScore = 0;

        long sentenceScore = 0;
        long sentenceCount = dubbingList.size();

        for (int i = 0; i < dubbingList.size(); i++) {
            DubbingEntity entity = dubbingList.get(i);
            recordTotalTime+=entity.recordTime;
            sentenceScore+=entity.showScore;
            List<Eval_result.WordsBean> wordList = GsonUtils.toObjectList(entity.wordData,Eval_result.WordsBean.class);
            for (int j = 0; j < wordList.size(); j++) {
                int score = (int) (Double.parseDouble(wordList.get(j).getScore())*20);
                wordScore+=score;
                evalWordCount++;
            }
        }

        //计算流畅度：总单词成绩/单词总数
        int showFlow = (int) (wordScore/evalWordCount);
        binding.progressFlow.setMax(100);
        binding.progressFlow.setProgress(showFlow);
        binding.textFlow.setText(String.valueOf(showFlow));

        //计算准确度：总句子成绩/句子总数
        averageScore = (int) (sentenceScore/sentenceCount);
        binding.progressRight.setMax(100);
        binding.progressRight.setProgress(averageScore);
        binding.textRight.setText(String.valueOf(averageScore));

        //判断是否显示
        String videoPath = getVoaVideoPath();
        String audioPath = getVoaAudioPath();
        File videoFile = new File(videoPath);
        File audioFile = new File(audioPath);
        if (!videoFile.exists()||!audioFile.exists()){
            ToastUtil.showToast(getActivity(),"暂无音视频数据，请返回重试~");
            binding.textPublish.setVisibility(View.INVISIBLE);
            binding.saveDraft.setVisibility(View.INVISIBLE);
        }
    }

    //分享内容
    private void shareMarge(){
        Share.prepareDubbingMessage(getActivity() ,  mVoa, shuoshuoId, UserInfoManager.getInstance().getUserName(), presenter.getIntegralService(), UserInfoManager.getInstance().getUserId());
    }

    /***********************************地址*****************************/
    //音频下载链接
    private String getAudioUrl(){
        String downloadUrl = null;

        if (UserInfoManager.getInstance().isVip()){
            String vipPrefix = "http://staticvip." + NetHostManager.getInstance().getDomainShort() + "/sounds/voa";
            if (audioUrl.startsWith("/")){
                downloadUrl = vipPrefix+audioUrl;
            }else {
                downloadUrl = vipPrefix+"/"+audioUrl;
            }
        }else {
            String prefix = "http://staticvip." + NetHostManager.getInstance().getDomainShort() + "/sounds/voa";
            if (audioUrl.startsWith("/")){
                downloadUrl = prefix+audioUrl;
            }else {
                downloadUrl = prefix+"/"+audioUrl;
            }
        }

        return downloadUrl;
    }

    //视频下载的链接
    private String getVideoUrl(){
        if (UserInfoManager.getInstance().isVip()){
            return "http://staticvip."+NetHostManager.getInstance().getDomainShort()+videoUrl;
        }else {
            return "http://static0."+NetHostManager.getInstance().getDomainShort()+videoUrl;
        }
    }

    //下载的音频地址
    private String getVoaAudioPath(){
        String dirPath = StorageUtil.getMediaDir(TalkShowApplication.getContext(), mVoa.voaId()).getAbsolutePath();
        //获取路径显示
        String extendPath = "/dubbing/";
        String audioUrl = getAudioUrl();
        int index = audioUrl.lastIndexOf(".");
        if (index>0){
            String suffix = audioUrl.substring(index);
            extendPath = extendPath+mVoa.voaId()+suffix;
        }else {
            extendPath = extendPath+mVoa.voaId()+".mp3";
        }

        return dirPath+extendPath;
    }

    //下载的视频地址
    private String getVoaVideoPath(){
        String dirPath = StorageUtil.getMediaDir(TalkShowApplication.getContext(), mVoa.voaId()).getAbsolutePath();
        //获取路径显示
        String extendPath = "/dubbing/";
        String audioUrl = getVideoUrl();
        int index = audioUrl.lastIndexOf(".");
        if (index>0){
            String suffix = audioUrl.substring(index);
            extendPath = extendPath+mVoa.voaId()+suffix;
        }else {
            extendPath = extendPath+mVoa.voaId()+".mp4";
        }

        return dirPath+extendPath;
    }

    //评测的录音地址
    private String getRecordEvalPath(int voaId,int paraId,int indexId){
        String fileName = voaId+"_"+paraId+"_"+indexId+"_"+UserInfoManager.getInstance().getUserId()+".mp3";
        String dirPath = StorageUtil.getMediaDir(TalkShowApplication.getContext(), voaId).getAbsolutePath();
        String extendPath = "/dubbing/eval/";
        return dirPath+extendPath+fileName;
    }

    /***********************************加载显示*******************************/
    private LoadingAdDialog loadingAdDialog;

    private void showLoading(){
        if (loadingAdDialog==null){
            loadingAdDialog = new LoadingAdDialog(getActivity());
            loadingAdDialog.setTitleText(presenter.formatTitle(getString(R.string.record_publishing)));
            loadingAdDialog.setMessageText(
                    presenter.formatMessage(
                            evalWordCount,
                            averageScore,
                            String.valueOf(recordTotalTime/1000.0f)
                    )
            );
            loadingAdDialog.setOnDismissListener(dialogInterface -> presenter.cancelUpload());
            loadingAdDialog.setRetryOnClick(view -> {
                loadingAdDialog.retry();

                presenter.submitPublishDis(mVoa.voaId(),String.valueOf(mVoa.category()),voaTextList,averageScore,audioUrl);
            });
        }
        loadingAdDialog.show(UserInfoManager.getInstance().isVip());
    }

    private void stopLoading(){
        if (loadingAdDialog!=null&&loadingAdDialog.isShowing()){
            loadingAdDialog.dismiss();
        }
    }

    private void failLoading(){
        if (loadingAdDialog!=null){
            loadingAdDialog.setCanceledOnTouchOutside(true);
            loadingAdDialog.showFailure("发布失败");
            loadingAdDialog.showRetryButton();
        }
    }

    private void successLoading(){
        if (loadingAdDialog!=null){
            loadingAdDialog.setCanceledOnTouchOutside(true);
            loadingAdDialog.showSuccess("发布成功～");
        }
    }

    /*******************************回调**************************/
    @Override
    public void showVoaText(List<VoaText> list) {
        this.voaTextList = list;

        if (list!=null&&list.size()>0){
            //计算完整度
            int evalCount = DubbingManager.getMultiDubbingData(UserInfoManager.getInstance().getUserId(), mVoa.voaId()).size();
            int showComplete = (int) (evalCount*100.0f/list.size());
            binding.progressAll.setMax(100);
            binding.progressAll.setProgress(showComplete);
            binding.textAll.setText(String.valueOf(showComplete));
        }
    }

    @Override
    public void showPublishStatus(boolean isSuccess,int shuoshuoId) {
        if (isSuccess){
            stopLoading();
            //显示分享操作
            this.shuoshuoId = shuoshuoId;
            shareMarge();
            binding.textPublish.setText("分享给好友");
        }else {
            failLoading();
        }
    }

    @Override
    public void showSaveDraftStatus(boolean isSuccess) {
        if (isSuccess){
            ToastUtil.showToast(getActivity(),"保存到草稿箱成功");
        }else {
            ToastUtil.showToast(getActivity(),"保存到草稿箱失败");
        }
    }

    //是否已经回调播放
    private boolean isPlayBack = true;
    //视频控制器的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimplePlayEvent event){
        if (event.getType().equals(SimplePlayEvent.type_back)){
            //返回
            getActivity().finish();
        }

        if (event.getType().equals(SimplePlayEvent.type_play)){
            //播放
            if (!isPlayBack){
                isPlayBack = true;
                startPlay();

                //显示数据处理
                Log.d("数据显示和处理--播放", "背景音--"+pauseTime+"--评测音--"+evalTime+"--位置--"+playPosition);
            }
        }

        if (event.getType().equals(SimplePlayEvent.type_pause)){
            //暂停
            if (isPlayBack){
                //显示数据处理
                long audioPlayTime = audioPlayer.isPlaying()?audioPlayer.getCurrentPosition():0;
                long evalPlayTime = evalPlayer.isPlaying()?evalPlayer.getCurrentPosition():0;
                Log.d("数据显示和处理--暂停", "背景音--("+audioPlayTime+"/"+pauseTime+")--评测音--"+evalPlayTime+"--位置--"+playPosition);

                isPlayBack = false;
                pausePlay();
            }
        }
    }
}
