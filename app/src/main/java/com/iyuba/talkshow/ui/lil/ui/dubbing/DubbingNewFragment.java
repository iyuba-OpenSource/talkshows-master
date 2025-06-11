package com.iyuba.talkshow.ui.lil.ui.dubbing;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.local.entity.DubbingEntity;
import com.iyuba.lib_common.model.remote.manager.DubbingManager;
import com.iyuba.lib_common.util.LibRxTimer;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.databinding.FragmentDubbingNewBinding;
import com.iyuba.talkshow.ui.base.BaseFragment;
import com.iyuba.talkshow.ui.lil.dialog.LoadingDialog;
import com.iyuba.talkshow.ui.lil.event.DownloadFileEvent;
import com.iyuba.talkshow.ui.lil.ui.dubbing.preview.PreviewNewActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.util.StorageUtil;
import com.iyuba.wordtest.lil.fix.util.LibPermissionDialogUtil;
import com.iyuba.wordtest.utils.RecordManager;
import com.iyuba.wordtest.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 新的配音界面
 */
public class DubbingNewFragment extends BaseFragment implements DubbingNewView{

    //参数
    private Voa mVoa;
    private String audioUrl;
    private String videoUrl;

    //录音器
    private RecordManager recordManager;
    //录音的开始时间
    private long recordStartTime = 0;
    //是否正在录音
    private boolean isRecording = false;

    //音频播放器
    private ExoPlayer audioPlayer;

    //视频播放器
    private ExoPlayer videoPlayer;
    //是否正常加载视频
    private boolean isPrepareVideo = false;

    //数据
    @Inject
    public DubbingNewPresenter presenter;
    //适配器
    private DubbingNewAdapter adapter;
    //布局
    private FragmentDubbingNewBinding binding;
    //是否正在下载中
    private boolean isDownloading = false;

    public static DubbingNewFragment getInstance(Voa voa, String audioUrl, String videoUrl){
        DubbingNewFragment fragment = new DubbingNewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(StrLibrary.data,voa);
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
        binding = FragmentDubbingNewBinding.inflate(LayoutInflater.from(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //绑定
        fragmentComponent().inject(this);
        presenter.attachView(this);

        initList();
        initExoplayer();
        initClick();

        //下载音视频(先判断是否存在然后再下载)
        downloadFile();

        //同步加载本地文本数据
        presenter.getLocalText(mVoa.voaId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        //停止弹窗
        stopLoading();
        //停止视频
        pauseVideo();
        //停止音频
        pauseAudio();
        //停止录音
        stopRecord();
        //停止操作
        if (presenter!=null){
            presenter.detachView();
        }
    }

    /***************************初始化****************************/
    private void initList(){
        adapter = new DubbingNewAdapter(getActivity(),new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnDubbingListener(new DubbingNewAdapter.OnDubbingListener() {
            @Override
            public void onPlayVideo(int position, long startTime, long endTime) {
                //播放视频

                //判断录音
                if (isRecording){
                    ToastUtil.showToast(getActivity(),"正在录音中～");
                    return;
                }

                //停止音频
                pauseAudio();

                //判断是否可以播放
                if (!isPrepareVideo){
                    ToastUtil.showToast(getActivity(),"视频播放器初始化失败");
                    return;
                }

                //开始播放和刷新显示
                playVideo(startTime, endTime);
            }

            @Override
            public void onPlayEval(String evalUrl, int voaId, int paraId, int indexId) {
                //播放音频

                //判断录音
                if (isRecording){
                    ToastUtil.showToast(getActivity(),"正在录音中～");
                    return;
                }

                //停止视频
                pauseVideo();

                //获取播放的链接
                String playUrl = getRecordEvalPath(voaId, paraId, indexId);
                File playFile = new File(playUrl);
                if (!playFile.exists()){
                    //http://iuserspeech.iyuba.cn:9001/voa
                    playUrl = "http://iuserspeech."+NetHostManager.getInstance().getDomainShort()+":9001/voa";
                }

                int selectPosition = adapter.getSelectIndex();
                int tempPosition = adapter.getTempIndex();
                if (selectPosition<0){
                    playAudio(playUrl);
                    return;
                }

                if (selectPosition>=0&&selectPosition!=tempPosition){
                    pauseAudio();
                    playAudio(playUrl);
                    return;
                }

                if (audioPlayer.isPlaying()){
                    pauseAudio();
                }else {
                    playAudio(playUrl);
                }
            }

            @Override
            public void onRecordAudio(String sentence, long startTime,long endTime, int voaId, int paraId, int indexId) {
                //录音

                //停止音频
                pauseAudio();
                //停止视频
                pauseVideo();

                //如果不是会员并且评测超过了三句，则显示需要开通会员
                if (!UserInfoManager.getInstance().isLogin()){
                    NewLoginUtil.startToLogin(getActivity());
                    return;
                }

                boolean isVip = UserInfoManager.getInstance().isVip();
                boolean curDubbing = DubbingManager.getSingleDubbingData(UserInfoManager.getInstance().getUserId(), voaId,paraId,indexId)!=null;
                List<DubbingEntity> dubbingList = DubbingManager.getMultiDubbingData(UserInfoManager.getInstance().getUserId(), voaId);
                boolean isThan3 = (dubbingList!=null&&dubbingList.size()>=3);
                if (!isVip&&!curDubbing&&isThan3){
                    new AlertDialog.Builder(mContext)
                            .setTitle("开通会员")
                            .setMessage("非会员可评测3句，会员尊享无限制评测，是否开通会员继续评测学习？")
                            .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, NewVipCenterActivity.class);
                                    intent.putExtra(NewVipCenterActivity.HUI_YUAN,NewVipCenterActivity.BENYINGYONG);
                                    mContext.startActivity(intent);
                                }
                            }).setNegativeButton("暂不使用",null)
                            .show();
                    return;
                }

                List<Pair<String, Pair<String, String>>> pairList = new ArrayList<>();
                pairList.add(new Pair<>(Manifest.permission.RECORD_AUDIO, new Pair<>("麦克风权限", "录制配音时朗读的音频，用于评测打分使用")));
                pairList.add(new Pair<>(Manifest.permission.WRITE_EXTERNAL_STORAGE, new Pair<>("存储权限", "保存配音的音频文件，用于评测打分使用")));
                LibPermissionDialogUtil.getInstance().showMsgDialog(getActivity(), pairList, new LibPermissionDialogUtil.OnPermissionResultListener() {
                    @Override
                    public void onGranted(boolean isSuccess) {
                        if (isSuccess){

                            if (isRecording){
                                stopRecord();
                                //提交评测
                                submitEval(sentence, voaId, paraId, indexId);
                            }else {
                                startRecord(sentence, startTime,endTime, voaId, paraId, indexId);
                            }
                        }
                    }
                });
            }
        });
    }

    private void initExoplayer(){
        //视频播放器
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
                            //刷新适配器的显示
                            adapter.refreshVideoDuration(videoPlayer.getDuration());
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
                ToastUtil.showToast(getActivity(),"视频播放器初始化失败");
            }
        });
        binding.videoPlayer.setPlayer(videoPlayer);

        //音频播放器
        audioPlayer = new ExoPlayer.Builder(getActivity()).build();
        audioPlayer.setPlayWhenReady(false);
        audioPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState){
                    case Player.STATE_READY:
                        //加载完成
                        playAudio(null);
                        break;
                    case Player.STATE_ENDED:
                        //播放完成
                        //刷新适配器中的显示
                        adapter.refreshAudio(false);
                        break;
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                ToastUtil.showToast(getActivity(),"音频播放器加载失败");
            }
        });
    }

    private void initClick(){
        binding.previewBtn.setOnClickListener(v->{
            //切换到预览界面
            List<DubbingEntity> dubbingList = DubbingManager.getMultiDubbingData(UserInfoManager.getInstance().getUserId(), mVoa.voaId());
            if (dubbingList==null||dubbingList.size()<1){
                ToastUtil.showToast(getActivity(),"还没有配音哦，点击话筒试试吧");
                return;
            }

            if (isRecording){
                ToastUtil.showToast(getActivity(),"正在录音中～");
                return;
            }

            pauseVideo();
            pauseAudio();

            //显示在预览界面
            PreviewNewActivity.start(getActivity(),mVoa,audioUrl,videoUrl);
        });
    }

    /***************************视频******************************/
    //视频计时器
    private static final String timer_video = "timer_video";
    //加载视频
    private void initVideo(){
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
    }

    //播放视频
    private void playVideo(long startTime,long endTime){
        pauseVideo();

        float volume = videoPlayer.getVolume();
        videoPlayer.setVolume(volume);
        videoPlayer.seekTo(startTime);
        videoPlayer.play();

        LibRxTimer.getInstance().multiTimerInMain(timer_video, 0, 200L, new LibRxTimer.RxActionListener() {
            @Override
            public void onAction(long number) {
                long curVideoTime = videoPlayer.getCurrentPosition();
                Log.d("视频播放时间", curVideoTime+"--"+endTime);
                if (curVideoTime>=endTime){
                    pauseVideo();
                }
            }
        });
    }

    //暂停视频
    private void pauseVideo(){
        if (videoPlayer!=null&&videoPlayer.isPlaying()){
            videoPlayer.pause();
        }
        LibRxTimer.getInstance().cancelTimer(timer_video);
    }

    //播放视频(静音)
    private void playVideoMute(long startTime){
        videoPlayer.setVolume(0f);
        videoPlayer.seekTo(startTime);
        videoPlayer.play();
    }

    /****************************音频****************************/
    //播放音频
    private void playAudio(String urlOrPath){
        if (!TextUtils.isEmpty(urlOrPath)){
            MediaItem mediaItem = null;
            if (urlOrPath.startsWith("https;//")||urlOrPath.startsWith("http://")){
                mediaItem = MediaItem.fromUri(urlOrPath);
            }else {
                Uri uri = null;
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                    uri = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName()+".fileprovider",new File(urlOrPath));
                }else {
                    uri = Uri.fromFile(new File(urlOrPath));
                }
                mediaItem = MediaItem.fromUri(uri);
            }
            audioPlayer.setMediaItem(mediaItem);
            audioPlayer.prepare();
        }else {
            audioPlayer.play();
            //刷新样式显示
            adapter.refreshAudio(true);
        }
    }

    //暂停音频
    private void pauseAudio(){
        if (audioPlayer!=null&&audioPlayer.isPlaying()){
            audioPlayer.pause();
        }

        //同步刷新适配器样式
        adapter.refreshAudio(false);
    }

    /*****************************录音***************************/
    //录音计时器
    private static final String timer_record = "timer_record";
    //开始录音
    private void startRecord(String sentence,long startTime,long endTime,int voaId,int paraId,int indexId){
        try {
            String recordPath = getRecordEvalPath(voaId, paraId, indexId);
            //设置文件
            File recordFile = new File(recordPath);
            if (recordFile.exists()){
                recordFile.delete();
            }else {
                if (!recordFile.getParentFile().exists()){
                    recordFile.getParentFile().mkdirs();
                }
            }
            boolean isCreateFile = recordFile.createNewFile();
            if (!isCreateFile){
                ToastUtil.showToast(getActivity(),"创建文件失败，请重试～");
                return;
            }

            isRecording = true;
            recordManager = new RecordManager(recordFile);
            recordManager.startRecord();
            //记录下录音的开始时间
            recordStartTime = System.currentTimeMillis();
            //计时器开始
            long recordTime = endTime-startTime;
            LibRxTimer.getInstance().multiTimerInMain(timer_record, 0, 200L, new LibRxTimer.RxActionListener() {
                @Override
                public void onAction(long number) {
                    long progressTime = number*200L;
                    adapter.refreshRecord(progressTime,recordTime,true);

                    if (progressTime>=recordTime){
                        stopRecord();
                        //提交评测
                        submitEval(sentence, voaId, paraId, indexId);
                    }
                }
            });

            //同步开始视频显示
            playVideoMute(startTime);
        }catch (Exception e){
            ToastUtil.showToast(getActivity(),"录音异常，请重试～");
        }
    }

    //停止录音
    private void stopRecord(){
        if (recordManager!=null){
            recordManager.stopRecord();
        }
        isRecording = false;
        //停止计时器
        LibRxTimer.getInstance().cancelTimer(timer_record);
        //刷新显示
        adapter.refreshRecord(0,0,false);
        //同步停止视频
        pauseVideo();
    }

    /****************************评测***************************/
    private void submitEval(String sentence,int voaId,int paraId,int indexId){
        startLoading("正在提交评测～",false);

        int userId = UserInfoManager.getInstance().getUserId();
        String evalPath = getRecordEvalPath(voaId, paraId, indexId);
        long recordTime = System.currentTimeMillis()-recordStartTime;

        presenter.submitEval(sentence,voaId,paraId,indexId,userId,evalPath,recordTime);
    }

    /****************************下载音视频************************/
    private void downloadFile(){
        //获取当前的音视频数据
        String localAudioPath = getVoaAudioPath();
        String localVideoPath = getVoaVideoPath();

        File audioFile = new File(localAudioPath);
        File videoFile = new File(localVideoPath);
        if (presenter.checkFileExist(mVoa.voaId(),localAudioPath,localVideoPath)){
            //显示数据
            initVideo();
        }else {
            //下载音视频
            startLoading("正在下载音视频内容",true);
            //先删除现在的文件
            if (audioFile.exists()){
                audioFile.delete();
            }
            if (videoFile.exists()){
                videoFile.delete();
            }
            //下载文件
            List<Pair<String,Pair<String,String>>> downloadList = new ArrayList<>();
            downloadList.add(new Pair<>(DubbingNewPresenter.TAG_audio,new Pair<>(getAudioUrl(),localAudioPath)));
            downloadList.add(new Pair<>(DubbingNewPresenter.TAG_video,new Pair<>(getVideoUrl(),localVideoPath)));
            presenter.downloadFile(downloadList);
        }
    }

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

    /****************************其他功能***************************/
    //加载弹窗
    private LoadingDialog loadingDialog;

    private void startLoading(String showMsg,boolean isDownload){
        if (loadingDialog==null){
            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.create();
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (isDownload){
                        if (isDownloading){
                            stopLoading();

                            File audioFile = new File(getVoaAudioPath());
                            audioFile.delete();
                            File videoFile = new File(getVoaVideoPath());
                            videoFile.delete();
                        }

                        getActivity().finish();
                    }
                }
            });
        }
        loadingDialog.setMsg(showMsg);
        if (loadingDialog!=null&&!loadingDialog.isShowing()){
            loadingDialog.show();
        }
    }

    private void stopLoading(){
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    /************************回调**********************************/
    @Override
    public void showVoaText(List<VoaText> list) {
        //判断是否显示
        if (list!=null&&list.size()>0){
            adapter.refreshData(list);
        }
    }

    @Override
    public void showEval(boolean isSuccess, String showMsg) {
        stopLoading();

        if (!isSuccess){
            ToastUtil.showToast(getActivity(),showMsg);
            return;
        }

        //刷新界面显示
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadEvent(DownloadFileEvent event){
        switch (event.getDownloadStatus()){
            case DownloadFileEvent.STATUS_downloading:
                //下载中
                isDownloading = true;
                startLoading(event.getShowMsg(),true);
                break;
            case DownloadFileEvent.STATUS_error:
                //下载失败
                stopLoading();
                isDownloading = false;
                new AlertDialog.Builder(getActivity())
                        .setTitle("下载失败")
                        .setMessage(event.getShowMsg())
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloadFile();
                            }
                        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                }).setCancelable(false).create().show();
                break;
            case DownloadFileEvent.STATUS_finish:
                //下载完成
                stopLoading();
                isDownloading = false;
                initVideo();

                //下载完成后，将数据保存在本地中用于使用和显示
                presenter.saveDownloadDataToDB(mVoa.voaId(), getVoaAudioPath(),getVoaVideoPath());
                break;
        }
    }
}
