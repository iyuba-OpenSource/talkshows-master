//package com.iyuba.talkshow.ui.dubbing;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.TranslateAnimation;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.core.content.res.ResourcesCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.devbrackets.android.exomedia.listener.OnPreparedListener;
//import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
//import com.devbrackets.android.exomedia.ui.widget.VideoControls;
//import com.devbrackets.android.exomedia.ui.widget.VideoControlsCore;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.iyuba.lib_common.data.Constant;
//import com.iyuba.lib_common.model.local.entity.DubbingHelpEntity;
//import com.iyuba.lib_common.model.local.manager.CommonDataManager;
//import com.iyuba.lib_user.manager.UserInfoManager;
//import com.iyuba.module.toolbox.GsonUtils;
//import com.iyuba.play.ExtendedPlayer;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.data.DataManager;
//import com.iyuba.talkshow.data.local.PreferencesHelper;
//import com.iyuba.talkshow.data.model.Record;
//import com.iyuba.talkshow.data.model.Voa;
//import com.iyuba.talkshow.data.model.VoaSoundNew;
//import com.iyuba.talkshow.data.model.VoaText;
//import com.iyuba.talkshow.data.model.WavListItem;
//import com.iyuba.talkshow.data.model.WordResponse;
//import com.iyuba.talkshow.data.model.result.SendEvaluateResponse;
//import com.iyuba.talkshow.databinding.ActivityDubbingBinding;
//import com.iyuba.talkshow.event.DownloadEvent;
//import com.iyuba.talkshow.http.RetrofitUtils;
//import com.iyuba.talkshow.newview.WordApi;
//import com.iyuba.talkshow.ui.base.BaseActivity;
//import com.iyuba.talkshow.ui.detail.MyOnTouchListener;
//import com.iyuba.talkshow.ui.lil.dialog.LoadingDialog;
//import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
//import com.iyuba.talkshow.ui.lil.util.BigDecimalUtil;
//import com.iyuba.talkshow.ui.preview.PreviewActivity;
//import com.iyuba.talkshow.ui.preview.PreviewInfoBean;
//import com.iyuba.talkshow.util.NetStateUtil;
//import com.iyuba.talkshow.util.ScreenUtils;
//import com.iyuba.talkshow.util.StorageUtil;
//import com.iyuba.talkshow.util.TimeUtil;
//import com.iyuba.talkshow.util.ToastUtil;
//import com.iyuba.talkshow.util.UploadStudyRecordUtil;
//import com.iyuba.talkshow.util.videoView.BaseVideoControl;
//import com.iyuba.wordtest.db.WordOp;
//import com.iyuba.wordtest.entity.WordEntity;
//import com.jaeger.library.StatusBarUtil;
//import com.umeng.analytics.MobclickAgent;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.inject.Inject;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 配音界面
// */
//public class DubbingActivity extends BaseActivity implements DubbingMvpView {
//    private String playurl = "http://dict.youdao.com/dictvoice?audio=";
//    private static final String VOA = "voa";
//    private static final String TIMESTAMP = "timestamp";
//    private TranslateAnimation animation;
//    private Voa mVoa;
//    private List<VoaText> mVoaTextList;
//    public static boolean isSend = false;
//    private MediaRecordHelper mediaRecordHelper = new MediaRecordHelper();
//    //    private AudioEncoder mAudioEncoder;
//    private long mTimeStamp;
//    private NewScoreCallback mNewScoreCallback;
//    private Map<Integer, WavListItem> map = new HashMap<>();
//    ActivityDubbingBinding binding;
//
//    @Inject
//    public DubbingPresenter mPresenter;
//    @Inject
//    public DubbingAdapter mAdapter;
//    @Inject
//    public PreferencesHelper mHelper;
//    @Inject
//    public DataManager mManager;
//
//    private String saveFile;
//    private MediaPlayer mAccAudioPlayer;
//    private ExtendedPlayer mRecordPlayer;
//    private MediaPlayer wordPlayer;
//
//    private DubbingVideoControl mVideoControl;
//
//    private PreviewInfoBean previewInfoBean;
//
//    private boolean mIsFirstIn = true;
//
//    private String wordString;
//
//    UploadStudyRecordUtil studyRecordUpdateUtil;
//    public long mDuration;
//    private WordApi wordApi;
//    private WordEntity wordEntity = null;
//    private WordOp wordOp;
//
//    private void buildMap(int index, WavListItem item) {
//        map.put(index, item);
//    }
//
//    DubbingAdapter.RecordingCallback mRecordingCallback = new DubbingAdapter.RecordingCallback() {
//        @Override
//        public void init(String path) {
//            initRecord(path);
//        }
//
//        @Override
//        public void start(final VoaText voaText) {
//            startRecording(voaText);
//        }
//
//        @Override
//        public boolean isRecording() {
//            return mediaRecordHelper.isRecording;
////            return isOnRecording();
//        }
//
//        @Override
//        public void lookWord(String word) {
//            getNetworkInterpretation(word);
//        }
//
//        @Override
//        public void setRecordingState(boolean state) {
////            mAudioEncoder.setState(state);
//        }
//
//        @Override
//        public void stop() {
//            stopRecording();
//        }
//
//        @Override
//        public void save(VoaText list) {
//            saveFile = mediaRecordHelper.getFilePath();
//            if (TextUtils.isEmpty(saveFile)) {
//                Log.e("DubbingAdapter", "save saveFile is null? ");
//                return;
//            }
//            VoaSoundNew voaSound = VoaSoundNew.builder()
//                    .setItemid(Integer.parseInt(list.paraId() + "" + list.idIndex() + "" + mVoa.voaId()))
//                    .setUid(UserInfoManager.getInstance().getUserId())
//                    .setVoa_id(mVoa.voaId())
//                    .setTotalscore(0)
//                    .setWordscore("")
//                    .setFilepath(saveFile)
//                    .setTime("" + mTimeStamp)
//                    .setSound_url("")
//                    .build();
//            mPresenter.saveVoaSound(voaSound);
//        }
//
//        @Override
//        public void convert(int paraId, List<VoaText> list) {
//        }
//
//        @Override
//        public void upload(VoaText list, String path, int flag) {
//            //提交评测
////            String saveFile = mAudioEncoder.getmSavePath();
//            if (flag == 0) {
//                saveFile = mediaRecordHelper.getFilePath();
//            } else {
//                saveFile = path;
//            }
//            if (TextUtils.isEmpty(saveFile)) {
//                Log.e("DubbingAdapter", "upload saveFile is null? ");
//                return;
//            }
//            int paraId = list.paraId();
//            File flacFile = new File(saveFile);
//            mPresenter.uploadSentence(list.sentence(), list.idIndex(), list.getVoaId(), paraId, Constant.EVAL_TYPE, String.valueOf(UserInfoManager.getInstance().getUserId()), flacFile).
//                    observeOn(AndroidSchedulers.mainThread()).
//                    subscribeOn(Schedulers.io()).
//                    subscribe(new Subscriber<SendEvaluateResponse>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            mNewScoreCallback.onError(paraId, "评测服务暂时有问题，请稍后再试！");
//                            if (e != null) {
//                                Log.e("DubbingAdapter", "onError  " + e.getMessage());
//                            }
//                        }
//
//                        @Override
//                        public void onNext(SendEvaluateResponse s) {
//                            if (isDestroyed()) {
//                                return;
//                            }
//                            int totalScore = (int) (Math.sqrt(Float.parseFloat(s.getData().getTotal_score()) * 2000));
//                            mNewScoreCallback.onResult(paraId, totalScore, s.getData());
//                            WavListItem item = new WavListItem();
//                            item.setUrl(s.getData().getURL());
//                            item.setBeginTime(mVoaTextList.get(paraId - 1).timing());
//                            if (paraId < mVoaTextList.size()) {
//                                item.setEndTime(mVoaTextList.get(paraId).timing());
//                            } else {
//                                item.setEndTime(mVoaTextList.get(paraId - 1).endTiming());
//                            }
//                            float duration = getAudioFileVoiceTime(flacFile.getAbsolutePath()) / 1000.0f;
//                            String temp = String.format("%.1f", duration);
//                            item.setDuration(Float.parseFloat(temp));
//                            item.setIndex(paraId);
//                            buildMap(paraId, item);
//                            // save to database
//                            String wordScore = "";
//                            SendEvaluateResponse.DataBean dataBean = s.getData();
//                            if (dataBean != null && dataBean.getWords() != null) {
//                                List<SendEvaluateResponse.DataBean.WordsBean> words = dataBean.getWords();
//                                for (int i = 0; i < words.size(); i++) {
//                                    SendEvaluateResponse.DataBean.WordsBean word = words.get(i);
//                                    wordScore = wordScore + word.getScore() + ",";
//                                }
//                            }
//
//                            long itemId = Long.parseLong(paraId + "" + list.idIndex() + "" + mVoa.voaId());
//                            VoaSoundNew voaSound = VoaSoundNew.builder()
//                                    .setItemid(itemId)
//                                    .setUid(UserInfoManager.getInstance().getUserId())
//                                    .setVoa_id(mVoa.voaId())
//                                    .setTotalscore(totalScore)
//                                    .setWordscore(wordScore)
//                                    .setFilepath(saveFile)
//                                    .setTime("" + mTimeStamp)
//                                    .setWords(GsonUtils.toJson(dataBean, SendEvaluateResponse.DataBean.class))
//                                    .setSound_url(s.getData().getURL())
//                                    .build();
//                            mPresenter.saveVoaSound(voaSound);
//
//                            //刷新纠音显示
////                            mAdapter.refreshCheckEvalShow();
//
//                            //保存在辅助表中
//                            double score = BigDecimalUtil.trans2Double(s.getData().getTotal_score());
//                            DubbingHelpEntity helpEntity = new DubbingHelpEntity(
//                                    itemId,
//                                    UserInfoManager.getInstance().getUserId(),
//                                    mAdapter.getRecordTime(),
//                                    s.getData().getSentence(),
//                                    score*20.0f,
//                                    score,
//                                    saveFile,
//                                    s.getData().getURL(),
//                                    GsonUtils.toJson(dataBean, SendEvaluateResponse.DataBean.class)
//                            );
//                            CommonDataManager.saveDataToDubbingHelp(helpEntity);
//                        }
//                    });
//        }
//    };
//
//    public long getAudioFileVoiceTime(String filePath) {
//        long mediaPlayerDuration = 0L;
//        if (filePath == null || filePath.isEmpty()) {
//            return 0;
//        }
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(filePath);
//            mediaPlayer.prepare();
//            mediaPlayerDuration = mediaPlayer.getDuration();
//        } catch (IOException ioException) {
//            Log.i("tag", ioException.getMessage());
//        }
//        mediaPlayer.stop();
//        mediaPlayer.reset();
//        mediaPlayer.release();
//        return mediaPlayerDuration;
//    }
//
//    DubbingAdapter.PlayVideoCallback mPlayVideoCallback = new DubbingAdapter.PlayVideoCallback() {
//        @Override
//        public void start(final VoaText voaText) {
//            startPlayVideo(voaText);
//        }
//
//        @Override
//        public boolean isPlaying() {
//            return isPlayVideo();
//        }
//
//        @Override
//        public int getCurPosition() {
//            return (int) binding.videoView.getCurrentPosition();
//        }
//
//        @Override
//        public void stop() {
//            pause();
//        }
//
//        @Override
//        public int totalTimes() {
//            return (int) binding.videoView.getDuration();
//        }
//    };
//
//    DubbingAdapter.PlayRecordCallback mPlayRecordCallback = new DubbingAdapter.PlayRecordCallback() {
//        @Override
//        public void start(final VoaText voaText) {
//            startPlayRecord(voaText);
//        }
//
//        @Override
//        public void stop() {
//            pause();
////            mAdapter.mOperateHolder.iPlay.setVisibility(View.VISIBLE);
////            mAdapter.mOperateHolder.iPause.setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        public int getLength() {
//            return (int) mRecordPlayer.getDuration();
//        }
//    };
//
//    OnPreparedListener mOnPreparedListener = new OnPreparedListener() {
//        @Override
//        public void onPrepared() {
//            if (mIsFirstIn) {
//                mIsFirstIn = false;
//                mAdapter.repeatPlayVoaText(mVoaTextList.get(0));
//            }
//        }
//    };
//
//    public static Intent buildIntent(Context context, Voa voa, long timeStamp) {
//        Intent intent = new Intent();
//        intent.setClass(context, DubbingActivity.class);
//        intent.putExtra(VOA, voa);
//        intent.putExtra(TIMESTAMP, timeStamp);
//        return intent;
//    }
//
//    @Override
//    public boolean isSwipeBackEnable() {
//        return false;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityDubbingBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        StatusBarUtil.setColor(this, ResourcesCompat.getColor(getResources(), R.color.status_bar_video, getTheme()));
//        activityComponent().inject(this);
//        mPresenter.attachView(this);
//        EventBus.getDefault().register(this);
//        mVoa = getIntent().getParcelableExtra(VOA);
//        mTimeStamp = getIntent().getLongExtra(TIMESTAMP, 0);
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        previewInfoBean = new PreviewInfoBean();
////        List<VoaSoundNew> localArrayList = mPresenter.getVoaSoundVoaId(mVoa.voaId());
////        if (localArrayList != null && localArrayList.size() > 0) {
////            mTimeStamp = Long.parseLong(localArrayList.get(0).time());
////            Log.e("DubbingAdapter", "mTimeStamp = " + mTimeStamp);
////            for (VoaSoundNew voaSound: localArrayList) {
////                if (voaSound != null) {
////                    previewInfoBean.setSentenceScore(voaSound.itemid(), voaSound.totalscore());
////                    previewInfoBean.setSentenceFluent(voaSound.itemid(), voaSound.totalscore());
////                    previewInfoBean.setSentenceUrl(voaSound.itemid(), voaSound.sound_url());
////                }
////            }
////        }
//        mPresenter.init(mVoa);
//        setVideoViewParams();
//        startStudyRecord();
//        initMedia();
//        initAnimation();
//        initRecyclerView();
//        initClick();
//        mPresenter.getVoaTexts(mVoa.voaId());
//    }
//
//    private void initClick() {
//        binding.previewDubbing.setOnClickListener(v -> onPreviewClick());
////        binding.dialogBtnAddword.setOnClickListener(v -> onAddClicked());
//        binding.ivAudio.setOnClickListener(v -> onAudioClicked());
//        binding.close.setOnClickListener(v -> onCloseClicked());
//        binding.collect.setOnClickListener(v -> onAddClicked());
//        wordApi = RetrofitUtils.getInstance().getApiService(Constant.Web.WordBASEURL, "xml", WordApi.class);
//        wordOp = new WordOp(mContext);
//    }
//
//    private void saveDraft() {
//        Record record = getDraftRecord();
////        L.e("save draft ::  " + new Gson().toJson(record));
//        mPresenter.saveRecord(record);
//    }
//
//    private Record getDraftRecord() {
//        int totalNum;
//        if (mVoaTextList != null) {
//            totalNum = mVoaTextList.size();
//        } else {
//            totalNum = 0;
//        }
//
//        return Record.builder()
//                .setTimestamp(mTimeStamp)
//                .setVoaId(mVoa.voaId())
//                .setTitle(mVoa.title())
//                .setTitleCn(mVoa.titleCn())
//                .setImg(mVoa.pic())
//                .setDate(TimeUtil.getCurDate())
//                .setTotalNum(totalNum)
//                .setFinishNum(mPresenter.getFinishNum(mVoa.voaId(), mTimeStamp))
//                .setScore(previewInfoBean.getAllScore())
//                .setAudio(previewInfoBean.getAllAudioUrl())
//                .build();
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void initMedia() {
////        mAudioEncoder = new AudioEncoder();
//        mAccAudioPlayer = new MediaPlayer();
//        wordPlayer = new MediaPlayer();
//        wordPlayer.setOnPreparedListener(mp -> wordPlayer.start());
//        mRecordPlayer = new ExtendedPlayer(getApplicationContext());
//        MyOnTouchListener listener = new MyOnTouchListener(this);
//        listener.setSingleTapListener(mSingleTapListener);
//        mVideoControl = new DubbingVideoControl(this);
//        mVideoControl.setMode(BaseVideoControl.Mode.SHOW_MANUAL);
//        mVideoControl.setFullScreenBtnVisible(false);
//
//        //调速
//        if (UserInfoManager.getInstance().isVip()) {
//            mVideoControl.showSpeedButton(true);
//            mVideoControl.setToSpeedCallback(new BaseVideoControl.ToSpeedCallBack() {
//                @Override
//                public void onToSpeed() {
//                    showSpeedDialog();
//                }
//            });
//        } else {
//            mVideoControl.showSpeedButton(false);
//        }
//
//        mVideoControl.setButtonListener(new VideoControlsButtonListener() {
//            @Override
//            public boolean onPlayPauseClicked() {
//                if (binding.videoView.isPlaying()) {
//                    pause();
//                    //停止学习报告记录
//                    stopStudyRecord("1");
//                } else {
//                    mAdapter.repeatPlayVoaText(mAdapter.getOperateVoaText());
//                    //开始学习报告记录
//                    startStudyRecord();
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onPreviousClicked() {
//                return false;
//            }
//
//            @Override
//            public boolean onNextClicked() {
//                return false;
//            }
//
//            @Override
//            public boolean onRewindClicked() {
//                return false;
//            }
//
//            @Override
//            public boolean onFastForwardClicked() {
//                return false;
//            }
//        });
//        mVideoControl.setBackCallback(() -> {
//            if (mPresenter.checkFileExist()) {
//                if (!isSend) {
//                    int finishNum = mPresenter.getFinishNum(mVoa.voaId(), mTimeStamp);
//                    if (finishNum > 0) {
//                        saveDraft();
//                    }
//                }
//            }
//            finish();
//        });
//        binding.videoView.setControls((VideoControlsCore) mVideoControl);
//        binding.videoView.setOnCompletionListener(() -> {
//            try {
//                binding.videoView.setVideoURI(Uri.fromFile(StorageUtil.getVideoFile(DubbingActivity.this, mVoa.voaId())));
//                mAccAudioPlayer.reset();
//                mAccAudioPlayer.setDataSource(StorageUtil.getAudioFile(DubbingActivity.this, mVoa.voaId()).getAbsolutePath());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        mVideoControl.setOnTouchListener(listener);
//        checkVideoAndMedia();
//    }
//
//    MyOnTouchListener.SingleTapListener mSingleTapListener = new MyOnTouchListener.SingleTapListener() {
//        @Override
//        public void onSingleTap() {
//            if (mVideoControl != null) {
//                if (mVideoControl.getControlVisibility() == View.GONE) {
//                    mVideoControl.show();
//                    if (binding.videoView.isPlaying()) {
//                        mVideoControl.hideDelayed(VideoControls.DEFAULT_CONTROL_HIDE_DELAY);
//                    }
//                } else {
//                    mVideoControl.hideDelayed(0);
//                }
//            }
//        }
//    };
//
//    private void initRecyclerView() {
//        mAdapter.setPlayVideoCallback(mPlayVideoCallback);
//        mAdapter.setPlayRecordCallback(mPlayRecordCallback);
//        mAdapter.setRecordingCallback(mRecordingCallback);
//        mAdapter.setScoreCallback((pos, score, fluec, url) -> {
//            previewInfoBean.setSentenceScore(pos, score);
//            previewInfoBean.setSentenceFluent(pos, fluec);
//            previewInfoBean.setSentenceUrl(pos, url);
//        });
//        mAdapter.setTimeStamp(mTimeStamp);
//        mAdapter.SetVoa(mVoa);
//        mAdapter.SetActivity(mContext);
//
//        binding.recyclerView.setAdapter(mAdapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.recyclerView.setLayoutManager(layoutManager);
//    }
//
//    public void checkVideoAndMedia() {
//        if (mPresenter.checkFileExist()) {
//            stopLoading();
//            setVideoAndAudio();
//        } else {
//            //下载音频视频
//            startLoading(getString(R.string.downloading));
//            mPresenter.download();
//        }
//    }
//
//    public void setVideoAndAudio() {
//        try {
//            binding.videoView.setVideoURI(Uri.fromFile(StorageUtil.getVideoFile(this, mVoa.voaId())));
//            binding.videoView.setPlaybackSpeed(mManager.getVideoSpeed(UserInfoManager.getInstance().getUserId()));
//            mAccAudioPlayer.setDataSource(
//                    StorageUtil.getAudioFile(this, mVoa.voaId()).getAbsolutePath());
//            binding.videoView.setOnPreparedListener(() -> mDuration = binding.videoView.getDuration());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//        pause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//        VoaText operateVoaText = mAdapter.getOperateVoaText();
//        if (operateVoaText != null) {
//            binding.videoView.setVideoURI(Uri.fromFile(StorageUtil.getVideoFile(this, mVoa.voaId())));
//            binding.videoView.seekTo(TimeUtil.secToMilliSec(operateVoaText.timing()));
//            mAccAudioPlayer.seekTo(TimeUtil.secToMilliSec(operateVoaText.timing()));
//            pauseVideoView();
//            mAccAudioPlayer.pause();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mPresenter != null) {
////            mPresenter.cancelDownload();
//            mPresenter.detachView();
//        }
////        mDownloadDialog.dismiss();
//
//        EventBus.getDefault().unregister(this);
//        DubbingActivity.isSend = false;
//
//        //关闭学习报告
//        stopStudyRecord("1");
//    }
//
//    //    @OnClick(R.id.preview_dubbing)
//    public void onPreviewClick() {
//        List<VoaSoundNew> localArrayList = mPresenter.getVoaSoundVoaId(mVoa.voaId());
//        if (localArrayList != null && localArrayList.size() > 0) {
//            mPresenter.merge(mVoa.voaId(), mTimeStamp, mVoaTextList, (int) binding.videoView.getDuration());
//        } else {
//            Toast.makeText(getApplicationContext(), R.string.not_dubbing, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void showMergeDialog() {
//        startLoading(getString(R.string.merging));
//    }
//
//    @Override
//    public void dismissMergeDialog() {
//        stopLoading();
//    }
//
//    private void setPreviewInfo() {
//        List<VoaSoundNew> localArrayList = mPresenter.getVoaSoundVoaId(mVoa.voaId());
//        if ((localArrayList != null) && (localArrayList.size() > 0)) {
//            mTimeStamp = Long.parseLong(localArrayList.get(0).time());
//            mAdapter.setTimeStamp(mTimeStamp);
//            Log.e("DubbingAdapter", "setPreviewInfo mTimeStamp = " + mTimeStamp);
//            for (VoaSoundNew voaSound : localArrayList) {
//                if (voaSound == null) {
//                    continue;
//                }
//                String voaid = "" + voaSound.voa_id();
//                String itemid = "" + voaSound.itemid();
//                int paraId = Integer.parseInt(itemid.replace(voaid, ""));
//                int idIndex = paraId % 10;
//                paraId = paraId / 10;
//                int position = paraId;
//                for (int i = 0; i < mVoaTextList.size(); i++) {
//                    if ((mVoaTextList.get(i) != null) && (mVoaTextList.get(i).paraId() == paraId) && (mVoaTextList.get(i).idIndex() == idIndex)) {
//                        position = i + 1;
//                        previewInfoBean.setSentenceScore(position, voaSound.totalscore());
//                        previewInfoBean.setSentenceFluent(position, voaSound.totalscore());
//                        previewInfoBean.setSentenceUrl(position, voaSound.sound_url());
//                        break;
//                    }
//                }
//            }
//        }
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void showVoaTexts(List<VoaText> voaTextList) {
//        mVoaTextList = voaTextList;
//        binding.videoView.setOnPreparedListener(mOnPreparedListener);
//        mAdapter.setList(voaTextList);
//        //设置缓存
//        binding.recyclerView.setItemViewCacheSize(voaTextList.size());
//        mAdapter.notifyDataSetChanged();
//        studyRecordUpdateUtil.getStudyRecord().setWordCount(voaTextList);
//        previewInfoBean.setVoaTexts(voaTextList);
//        if ((mVoaTextList != null) && (mVoaTextList.size() > 0)) {
//            setPreviewInfo();
//        } else {
//            Log.e("DubbingAdapter", "showVoaTexts mVoaTextList is null? ");
//        }
//        mPresenter.checkDraftExist(mTimeStamp);
//    }
//
//    @Override
//    public void showEmptyTexts() {
//        mAdapter.setList(Collections.emptyList());
//        //设置缓存
//        binding.recyclerView.setItemViewCacheSize(Collections.emptyList().size());
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void dismissDubbingDialog() {
////        mDubbingDialog.dismiss();
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void startPreviewActivity() {
////        stopStudyRecord("1");//关闭学习报告
//        previewInfoBean.initIndexList();
//        Record record = getDraftRecord();
//        Intent intent = PreviewActivity.buildIntent(mVoaTextList, this, mVoa, previewInfoBean, record, mTimeStamp, false);
//        startActivity(intent);
//    }
//
//    @Override
//    public void showToast(int resId) {
//        ToastUtil.show(getApplicationContext(), getResources().getString(resId));
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onDownloadFinish(DownloadEvent downloadEvent) {
//        switch (downloadEvent.status) {
//            case DownloadEvent.Status.FINISH:
//                stopLoading();
//                setVideoAndAudio();
//                break;
//            case DownloadEvent.Status.DOWNLOADING:
//                startLoading(downloadEvent.msg);
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void initRecord(String path) {
////        mAudioEncoder.setSavePath(path);
//        mediaRecordHelper.setFilePath(path);
//    }
//
//    public void startRecording(VoaText voaText) {
//        mediaRecordHelper.recorder_Media();
//        try {
//            //视频播放
//            binding.videoView.setVolume(0);
//            binding.videoView.setPlaybackSpeed(1.0f);
//            binding.videoView.seekTo(TimeUtil.secToMilliSec(voaText.timing()));
//            binding.videoView.start();
//            //音频播放
//            mAccAudioPlayer.seekTo(TimeUtil.secToMilliSec(voaText.timing()));
//            binding.videoView.setOnSeekCompletionListener(() -> {
////                try {
////                    mAudioEncoder.prepare();
////                    mAudioEncoder.start();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//            });
//
//            if (!mAccAudioPlayer.isPlaying()) {
//                mAccAudioPlayer.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            showToast(R.string.record_error);
//        }
//    }
//
//
////    public boolean isOnRecording() {
////        return mAudioEncoder.isRecording();
////    }
//
//    public void stopRecording() {
//        mediaRecordHelper.stop_record();
//        binding.videoView.setPlaybackSpeed(mManager.getVideoSpeed(UserInfoManager.getInstance().getUserId()));
//        binding.videoView.setOnSeekCompletionListener(null);
//        mAccAudioPlayer.pause();
//        pauseVideoView();
////        if (mAudioEncoder.isRecording()) {
////            mAudioEncoder.stop();
////            binding.videoView.setOnSeekCompletionListener(null);
////            mAccAudioPlayer.pause();
////            pauseVideoView();
////        }
//    }
//
//    public void startPlayRecord(VoaText voaText) {
//        try {
////            mRecordPlayer.reset();
////            mRecordPlayer.setDataSource(voaText.pathLocal);
////                    StorageUtil.getParaRecordAacFile(getApplicationContext(),
////                            voaText.getVoaId(), voaText.paraId(), mTimeStamp).getAbsolutePath());
////            mRecordPlayer.prepareAsync();
//            mRecordPlayer.initialize(voaText.pathLocal);
//            mRecordPlayer.setOnCompletionListener(mp -> {
//                binding.videoView.pause();
//                mPlayRecordCallback.stop();
//                mAdapter.stopRecordPlayView();
//            });
//            mRecordPlayer.prepareAndPlay();
//
//            //这里增加倍速
//            float speed = mManager.getVideoSpeed(UserInfoManager.getInstance().getUserId());
//            mRecordPlayer.setPlaySpeed(speed);
//
//            binding.videoView.setVolume(0);
////            mRecordPlayer.setVolume(1.0f, 1.0f);
//            seekTo(TimeUtil.secToMilliSec(voaText.timing()));
//            start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void startPlayVideo(VoaText voaText) {
//        pause();
//        binding.videoView.setVolume(1);
//        if (voaText != null) {
//            binding.videoView.seekTo(TimeUtil.secToMilliSec(voaText.timing()));
//        }
//        binding.videoView.start();
//
//        //开启学习记录
//        startStudyRecord();
//    }
//
//    public boolean isPlayVideo() {
//        return binding.videoView.isPlaying();
//    }
//
//    public void start() {
//        if (!binding.videoView.isPlaying()) {
//            binding.videoView.start();
//        }
//        if (!mAccAudioPlayer.isPlaying()) {
//            mAccAudioPlayer.start();
//        }
//        if (!mRecordPlayer.isPlaying()) {
//            mRecordPlayer.start();
//        }
//    }
//
//    public void seekTo(int millSec) {
//        binding.videoView.seekTo(millSec);
//        mAccAudioPlayer.seekTo(millSec);
//    }
//
//    @Override
//    public void pause() {
//        if (binding.videoView.isPlaying()) {
//            pauseVideoView();
//        }
//        if (mAccAudioPlayer.isPlaying()) {
//            mAccAudioPlayer.pause();
//        }
//        if (mRecordPlayer.isPlaying()) {
//            mRecordPlayer.pause();
//        }
//
//    }
//
//    @Override
//    public void onDraftRecordExist(Record record) {
//        buildScoreMap(record);
//        previewInfoBean.initIndexList(record, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                previewInfoBean.setVoaTextScore(mVoaTextList, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
//    }
//
//    public void buildScoreMap(Record record) {
//        String score = record.score();
//        String audio = record.audio();
//        if (TextUtils.isEmpty(score)) {
//            return;
//        }
//        java.lang.reflect.Type type = new TypeToken<HashMap<Integer, Integer>>() {
//        }.getType();
//        java.lang.reflect.Type type2 = new TypeToken<HashMap<Integer, String>>() {
//        }.getType();
//        Map<Integer, Integer> map = new Gson().fromJson(score, type);
//        Map<Integer, String> map2 = new Gson().fromJson(audio, type2);
//        for (Map.Entry<Integer, String> i : map2.entrySet()) {
//            for (Map.Entry<Integer, Integer> j : map.entrySet()) {
//                if (i.getKey().equals(j.getKey())) {
//                    WavListItem item = new WavListItem();
//                    item.setUrl(i.getValue());
//                    item.setBeginTime(mVoaTextList.get(i.getKey()).timing());
//                    if (i.getKey() < mVoaTextList.size()) {
//                        item.setEndTime(mVoaTextList.get(i.getKey() + 1).timing());
//                    } else {
//                        item.setEndTime(mVoaTextList.get(i.getKey()).endTiming());
//                    }
//                    File file = StorageUtil.getParaRecordAacFile(mContext, mVoa.voaId(), i.getKey() + 1, mTimeStamp);
//                    float duration = getAudioFileVoiceTime(file.getAbsolutePath()) / 1000.0f;
//                    @SuppressLint("DefaultLocale")
//                    String temp = String.format("%.1f", duration);
//                    item.setDuration(Float.parseFloat(temp));
//                    item.setIndex(i.getKey() + 1);
//                    buildMap(i.getKey(), item);
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void showWord(WordResponse bean) {
//        showWordView(bean);
//    }
//
//    private void pauseVideoView() {
//        runOnUiThread(() -> binding.videoView.pause());
////        stopStudyRecord("0");//关闭学习报告
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (loadingDialog != null && loadingDialog.isShowing()) {
//            stopLoading();
//            mPresenter.cancelDownload();
//            showToastShort("下载已经取消。");
//            return;
//        } else if (!mPresenter.checkFileExist()) {
//            finish();
//        } else {
//            if (!isSend) {
//                if (mPresenter.getFinishNum(mVoa.voaId(), mTimeStamp) == 0) {
//                    finish();
//                    return;
//                }
//
//                saveDraft();
//            } else {
//                finish();
//            }
//        }
//    }
//
//    public void startStudyRecord() {
//        studyRecordUpdateUtil = new UploadStudyRecordUtil(UserInfoManager.getInstance().isLogin(),
//                mContext, UserInfoManager.getInstance().getUserId(), mVoa.voaId(), "1", "1");//这里的mode由原来的2改为1
//    }
//
//    public void stopStudyRecord(String flag) {
//        if (studyRecordUpdateUtil != null) {
//            studyRecordUpdateUtil.stopStudyRecord(getApplicationContext(), UserInfoManager.getInstance().isLogin(), flag, mPresenter.getUploadStudyRecordService());
//        }
//    }
//
//    public void onCloseClicked() {
//        binding.jiexiRoot.setVisibility(View.GONE);
//    }
//
//    public void onAddClicked() {
//        if (!UserInfoManager.getInstance().isLogin()) {
//            ToastUtil.show(this, "请先登录");
//            NewLoginUtil.startToLogin(this);
//            return;
//        }
////        List<String> words = Collections.singletonList(wordString);
////        mPresenter.insertWords(mAccountManager.getUid(), words);
//        //屏蔽掉这这个数据，处理单词即可，不需要增加什么东西
////        if (wordEntity.key.contains(mVoa.voaId() + "")) {
////            wordEntity.key = wordEntity.key.replace("" + mVoa.voaId(), "");
////        }
//        if (wordOp.isFavorWord(wordEntity.key, mVoa.voaId(), UserInfoManager.getInstance().getUserId())) {
//            wordApi.updateWord(String.valueOf(UserInfoManager.getInstance().getUserId()), "delete", "Iyuba", wordEntity.key).enqueue(new Callback<WordEntity>() {
//                @Override
//                public void onResponse(Call<WordEntity> call, Response<WordEntity> response) {
//                    //这里不需要增加什么数据，直接删除
////                    if (wordEntity.key.contains(mVoa.voaId() + "")) {
////                        wordOp.deleteWord(wordEntity.key, mAccountManager.getUid());
////                    } else {
////                        wordOp.deleteWord(wordEntity.key + mVoa.voaId(), mAccountManager.getUid());
////                    }
//                    wordOp.deleteWord(wordEntity.key, UserInfoManager.getInstance().getUserId());
//                    ToastUtil.showToast(mContext, "单词取消收藏成功！");
////                    binding.dialogBtnAddword.setText("添加收藏");
//                    binding.collect.setImageResource(R.drawable.ic_word_nocollect);
//                }
//
//                @Override
//                public void onFailure(Call<WordEntity> call, Throwable t) {
//                    if (t != null) {
//                        Log.e("DubbingAdapter", "updateWord onFailure =  " + t.getMessage());
//                    }
//                }
//            });
//        } else {
//            wordApi.updateWord(String.valueOf(UserInfoManager.getInstance().getUserId()), "insert", "Iyuba", wordEntity.key).enqueue(new Callback<WordEntity>() {
//                @Override
//                public void onResponse(Call<WordEntity> call, Response<WordEntity> response) {
//                    //这里为什么要将单词处理成单词+voaid的样式，没搞懂，先屏蔽掉
////                    if (!wordEntity.key.contains(mVoa.voaId() + "")) {
////                        wordEntity.key = wordEntity.key + mVoa.voaId();
////                    }
//                    wordEntity.voa = mVoa.voaId();
//                    wordEntity.book = mVoa.series();
//                    if (!wordOp.isExsitsWord(wordEntity.key, UserInfoManager.getInstance().getUserId())) {
//                        long result = wordOp.insertWord(wordEntity, UserInfoManager.getInstance().getUserId());
//                        Log.e("DubbingAdapter", "updateWord insertWord result " + result);
//                    }
//                    ToastUtil.showToast(mContext, "单词收藏成功！");
////                    binding.dialogBtnAddword.setText("取消收藏");
//                    binding.collect.setImageResource(R.drawable.ic_word_collected);
//                }
//
//                @Override
//                public void onFailure(Call<WordEntity> call, Throwable t) {
//                    if (t != null) {
//                        Log.e("DubbingAdapter", "updateWord onFailure =  " + t.getMessage());
//                    }
//                    ToastUtil.showToast(mContext, "单词添加失败，请重试！");
//                }
//            });
//        }
//    }
//
//    private void getNetworkInterpretation(String word) {
//        Log.e("DubbingAdapter", "getNetworkInterpretation word =  " + word);
//        wordString = word;
//        wordEntity = wordOp.findWordEntity(wordString, UserInfoManager.getInstance().getUserId());
//        if ((wordEntity != null) && !TextUtils.isEmpty(wordEntity.key)) {
//            wordEntity.voa = mVoa.voaId();
//            wordEntity.book = mVoa.series();
//            wordEntity.unit = 0;
//            showWordEntity(wordEntity);
//        } else if (!NetStateUtil.isConnected(mContext)) {
//            showToastShort("查询单词需要开启数据网络");
//        } else {
//            wordApi.getWordApi(wordString).enqueue(new Callback<WordEntity>() {
//                @Override
//                public void onResponse(Call<WordEntity> call, Response<WordEntity> response) {
//                    if ((response != null) && (response.body() != null) && !TextUtils.isEmpty(response.body().key)) {
//                        wordEntity = response.body();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showWordEntity(wordEntity);
//                            }
//                        });
//
//                        //查出来数据不要直接插入数据库，因为根本没有收藏，收藏之后在保存
//                        Log.e("DubbingAdapter", "getWordApi onResponse wordEntity.key = " + wordEntity.key);
////                        wordEntity.voa = mVoa.voaId();
////                        wordEntity.book = mVoa.series();
////                        wordEntity.unit = 0;
////                        if (!wordOp.isExsitsWord(wordEntity.key, mAccountManager.getUid())) {
////                            long result = wordOp.insertWord(wordEntity, mAccountManager.getUid());
////                            Log.e("DubbingAdapter", "getWordApi onResponse insertWord result " + result);
////                        }
//                        return;
//                    } else {
//                        Log.e("DubbingAdapter", "getWordApi onResponse is null? ");
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showToastShort("暂时没有查到这个单词的解释");
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure(Call<WordEntity> call, Throwable t) {
//                    if (t != null) {
//                        Log.e("DubbingAdapter", "getWordApi onFailure =  " + t.getMessage());
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showToastShort("暂时没有查到这个单词的解释");
//                        }
//                    });
//                }
//            });
//        }
//    }
//
//    public void onAudioClicked() {
//        try {
//            wordPlayer.reset();
//            wordPlayer.setDataSource(playurl + wordString);
//            wordPlayer.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    interface NewScoreCallback {
//        void onResult(int pos, int score, SendEvaluateResponse.DataBean beans);
//
//        void onError(int pos, String errorMessage);
//    }
//
//    void setNewScoreCallback(NewScoreCallback mNewScoreCallback) {
//        this.mNewScoreCallback = mNewScoreCallback;
//    }
//
//
//    public void initAnimation() {
//        animation = new TranslateAnimation(-300, 0, 0, 0);
//        animation.setDuration(500);
//    }
//
//    private void showWordEntity(WordEntity bean) {
//        pauseVideoView();
//        wordString = bean.key;
//        binding.jiexiRoot.startAnimation(animation);
//        binding.jiexiRoot.setVisibility(View.VISIBLE);
//        binding.word.setText(bean.key);
//        binding.def.setText(bean.def);
//        if (TextUtils.isEmpty(bean.pron)) {
//            binding.pron.setText("");
//        } else {
//            binding.pron.setText(String.format("[%s]", bean.pron));
//        }
//        if (TextUtils.isEmpty(bean.audio)) {
//            binding.ivAudio.setVisibility(View.INVISIBLE);
//        } else {
//            binding.ivAudio.setVisibility(View.VISIBLE);
//        }
//        if (UserInfoManager.getInstance().isLogin()) {
//            if (wordOp.isFavorWord(wordString, mVoa.voaId(), UserInfoManager.getInstance().getUserId())) {
//                binding.collect.setImageResource(R.drawable.ic_word_collected);
//            } else {
//                binding.collect.setImageResource(R.drawable.ic_word_nocollect);
//            }
//        } else {
//            binding.collect.setImageResource(R.drawable.ic_word_nocollect);
//        }
//    }
//
//    private void showWordView(WordResponse bean) {
//        pauseVideoView();
//        wordString = bean.getKey();
//        binding.jiexiRoot.startAnimation(animation);
//        binding.jiexiRoot.setVisibility(View.VISIBLE);
//        binding.word.setText(bean.getKey());
//        binding.def.setText(bean.getDef());
//        binding.pron.setText(String.format("[%s]", bean.getPron()));
//    }
//
//    private void setVideoViewParams() {
//        ViewGroup.LayoutParams lp = binding.videoView.getLayoutParams();
//        int[] screenSize = ScreenUtils.getScreenSize(this);
//        lp.width = screenSize[0];
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            lp.height = screenSize[1]; // 16 : 9
//        } else {
//            lp.height = (int) (lp.width * 0.5625);
//        }
//        binding.videoView.setLayoutParams(lp);
//    }
//
//    //倍速数据
//    private String[] speedArray = new String[]{"0.5x", "0.75x", "1.0x", "1.25x", "1.5x", "1.75x", "2.0x"};
//
//    //显示倍速弹窗
//    private void showSpeedDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("当前倍速：" + mManager.getVideoSpeed(UserInfoManager.getInstance().getUserId()) + "x")
//                .setItems(speedArray, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String speedStr = speedArray[which];
//                        float speed = Float.parseFloat(speedStr.replace("x", ""));
//
//                        //设置视频
//                        binding.videoView.setPlaybackSpeed(speed);
//                        //音频需要加载在完成后进行倍速设置
//
//                        mManager.setVideoSpeed(UserInfoManager.getInstance().getUserId(), speed);
//
//                        dialog.dismiss();
//                    }
//                }).show();
//    }
//
//    //加载弹窗
//    private LoadingDialog loadingDialog;
//
//    private void startLoading(String showMsg) {
//        if (loadingDialog == null) {
//            loadingDialog = new LoadingDialog(this);
//            loadingDialog.create();
//        }
//        loadingDialog.setMsg(showMsg);
//        loadingDialog.show();
//    }
//
//    private void stopLoading() {
//        if (loadingDialog != null && loadingDialog.isShowing()) {
//            loadingDialog.dismiss();
//        }
//    }
//}
