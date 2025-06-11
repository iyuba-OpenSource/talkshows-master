//package com.iyuba.talkshow.ui.dubbing;
//
//import android.Manifest;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.MediaPlayer;
//import android.os.Handler;
//import android.os.Message;
//import android.text.SpannableString;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.style.ForegroundColorSpan;
//import android.util.Log;
//import android.util.Pair;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.iyuba.lib_common.model.local.entity.DubbingHelpEntity;
//import com.iyuba.lib_common.model.local.manager.CommonDataManager;
//import com.iyuba.lib_common.util.LibBigDecimalUtil;
//import com.iyuba.lib_user.manager.UserInfoManager;
//import com.iyuba.module.toolbox.GsonUtils;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.data.DataManager;
//import com.iyuba.talkshow.data.local.PreferencesHelper;
//import com.iyuba.talkshow.data.manager.ConfigManager;
//import com.iyuba.talkshow.data.model.EvaluateBean;
//import com.iyuba.talkshow.data.model.Voa;
//import com.iyuba.talkshow.data.model.VoaSoundNew;
//import com.iyuba.talkshow.data.model.VoaText;
//import com.iyuba.talkshow.data.model.result.SendEvaluateResponse;
//import com.iyuba.talkshow.databinding.ItemRecordBinding;
//import com.iyuba.talkshow.newview.EvalCorrectPage;
//import com.iyuba.talkshow.ui.lil.util.BigDecimalUtil;
//import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
//import com.iyuba.talkshow.ui.widget.DubbingProgressBar;
//import com.iyuba.talkshow.util.DensityUtil;
//import com.iyuba.talkshow.util.NumberUtil;
//import com.iyuba.talkshow.util.StorageUtil;
//import com.iyuba.talkshow.util.TimeUtil;
//import com.iyuba.talkshow.util.ToastUtil;
//import com.iyuba.talkshow.util.iseutil.ResultParse;
//import com.iyuba.textpage.TextPage;
//import com.iyuba.wordtest.lil.fix.util.LibPermissionDialogUtil;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//public class DubbingAdapter extends RecyclerView.Adapter<DubbingAdapter.RecordHolder> {
//
//    private static final int POSITION = 65;
//    private RecordingCallback mRecordingCallback;
//    private PlayRecordCallback mPlayRecordCallback;
//    private PlayVideoCallback mPlayVideoCallback;
//    private ScoreCallback mScoreCallback;
//
//    private List<VoaText> mList;
//    private long mTimeStamp;
//    private VoaText mOperateVoaText;
//    public RecordHolder mOperateHolder;
//
//    private int mActivitePosition;
//    int fluent = 0;
//
//
//    @Inject
//    public DubbingPresenter mPresenter;
//
//    Context mContext;
//
//    @Inject
//    public PreferencesHelper mHelper;
//
//    private static final int FULL_PERCENT = 100;
//    private EvalCorrectPage fragment;
//
//    @Inject
//    ConfigManager configManager;
//    @Inject
//    DataManager dataManager;
//    private boolean isRecording = false;
//
//    //录音时间
//    private long recordTime = 0;
//    //录音位置
//    private int recordIndex = 0;
//
//    //录音开始时间
//    private long recordStartTime = 0;
//    //录音结束时间
//    private long recordEndTime = 0;
//
//    @Inject
//    public DubbingAdapter() {
//        mList = new ArrayList<>();
//    }
//
//    @NotNull
//    @Override
//    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        mContext = parent.getContext();
//        ItemRecordBinding binder = ItemRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        return new RecordHolder(binder);
//    }
//
//    @Override
//    public void onBindViewHolder(RecordHolder holder, int position) {
//        holder.setItem(holder, mList.get(position), mList, position);
//        holder.setClick(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }
//
//    public void setList(List<VoaText> mList) {
//        this.mList = mList;
//    }
//
//    void setRecordingCallback(RecordingCallback mRecordingCallback) {
//        this.mRecordingCallback = mRecordingCallback;
//    }
//
//    void setPlayRecordCallback(PlayRecordCallback mPlayRecordCallback) {
//        this.mPlayRecordCallback = mPlayRecordCallback;
//    }
//
//    void setPlayVideoCallback(PlayVideoCallback mPlayVideoCallback) {
//        this.mPlayVideoCallback = mPlayVideoCallback;
//    }
//
//    void setScoreCallback(ScoreCallback mScoreCallback) {
//        this.mScoreCallback = mScoreCallback;
//    }
//
//    int getProgress(int total, int curPosition, float beginTime, float endTiming) {
//        float curSec = TimeUtil.milliSecToSec(curPosition);
//        return (int) (total * (curSec - beginTime) / (endTiming - beginTime));
//    }
//
//
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NotNull Message msg) {
//            int position = mPlayVideoCallback.getCurPosition();
//            float recordEndTiming;
//            float perfectTiming;
//            int sidePos = mList.size() - 1;
//            int firstProgress;
//            if (mActivitePosition == sidePos) {
//                recordEndTiming = mList.get(mActivitePosition).endTiming() + 0.1f;
//                perfectTiming = mList.get(mActivitePosition).endTiming();
//            } else {
//                recordEndTiming = mList.get(mActivitePosition + 1).timing();
//                perfectTiming = mList.get(mActivitePosition + 1).endTiming();
//            }
//
//            switch (msg.what) {
//                //  读配音进度条的更改
//                case 1:
//                    if (TimeUtil.milliSecToSec(position) >= recordEndTiming) {
//                        mPlayRecordCallback.stop();
////                        mOperateHolder.progressBar.setProgress(POSITION);
//                        mOperateHolder.ivPlay.setVisibility(View.VISIBLE);
//                        mOperateHolder.ivPause.setVisibility(View.INVISIBLE);
////                        mOperateHolder.progressBar.setSecondaryProgress(mOperateHolder.secondPosition);
//                    } else {
//                        handler.sendEmptyMessageDelayed(1, 25);
//                    }
//                    break;
//                // 录音计时
//                case 2:
//                    position = recordIndex;
//                    if (position<0||mActivitePosition<0){
//                        return;
//                    }
//
//                    //这里计时显示可以这样处理：
//                    //1.计算进度时间和增加时间，然后根据当前时间处理的情况，显示进度百分比和增加百分比
//                    //2.根据时间处理显示
//                    long progressTime = (long) (LibBigDecimalUtil.trans2Double(mList.get(position).endTiming()-mList.get(position).timing())*1000L);
//                    float endTag = 0;
//                    if (position<mList.size()-1){
//                        endTag = mList.get(position+1).timing();
//                    }else {
//                        endTag = mList.get(position).endTiming()+0.1f;
//                    }
//                    long addTime = (long) (BigDecimalUtil.trans2Double(endTag-mList.get(position).endTiming())*1000L);
//                    if (addTime<1.0f){
//                        addTime = 1000L;
//                    }
//                    //计时录音时长并累加
//                    recordTime+=30;
//                    //音频时长
//                    long playTime = recordTime;
//                    //总时长
//                    long totalTime = progressTime+addTime;
//
//                    //计算显示
//                    if (playTime>=progressTime){
//                        mOperateHolder.progress.setProgress(POSITION);
//                        if (playTime>=totalTime){
//                            mOperateHolder.progress.setSecondaryProgress(100);
//                            endRecording();
//                        }else {
//                            long lastTime = playTime-progressTime;
//                            int showAddProgress = (int) ((lastTime*1.0f/addTime)*(100-POSITION))+POSITION;
//                            mOperateHolder.progress.setSecondaryProgress(showAddProgress);
//                            handler.sendEmptyMessageDelayed(2, 25);
//                        }
//                    }else {
//                        int showProgress = (int) ((playTime*1.0f/progressTime)*POSITION);
//                        mOperateHolder.progress.setProgress(showProgress);
//                        mOperateHolder.progress.setSecondaryProgress(showProgress);
//                        handler.sendEmptyMessageDelayed(2, 25);
//                    }
//
//                    /*firstProgress = getProgress(POSITION, position, mList.get(mActivitePosition).timing(), mList.get(mActivitePosition).endTiming());
//                    Log.d("录音显示时间111111", firstProgress+"");
//                    if (TimeUtil.milliSecToSec(position) >= recordEndTiming) {
//                        if (configManager.isAutoDubbing()) {
//                            endRecording();
//                        } else {
//                            saveRecording();
//                        }
//
//                        int secondProgress = getProgress(FULL_PERCENT, position, mList.get(mActivitePosition).timing(), recordEndTiming);
//                        mOperateHolder.progress.setSecondaryProgress(secondProgress);
//                        mOperateHolder.progress.setProgress(POSITION);
//
//                        Log.d("录音时间显示", firstProgress+"----"+secondProgress);
//                    } else if (TimeUtil.milliSecToSec(position) >= perfectTiming) {
//                        int secondProgress = 0;
//                        if (mActivitePosition < sidePos) {
//                            secondProgress = getProgress(FULL_PERCENT, position, mList.get(mActivitePosition).timing(), mList.get(mActivitePosition + 1).timing());
//                        } else {
//                            secondProgress = getProgress(FULL_PERCENT, position, mList.get(mActivitePosition).timing(), recordEndTiming);
//                        }
//                        mOperateHolder.progress.setSecondaryProgress(secondProgress);
//
//                        Log.d("录音时间显示2222", firstProgress+"----"+secondProgress);
//                        mOperateHolder.progress.setProgress(POSITION);
//                        handler.sendEmptyMessageDelayed(2, 25);
//                    } else {
//                        if (firstProgress>=POSITION){
//                            //分进度显示
//                            mOperateHolder.progress.setProgress(POSITION);
//                            mOperateHolder.progress.setSecondaryProgress(firstProgress);
//                        }else {
//                            mOperateHolder.progress.setProgress(firstProgress);
//                            mOperateHolder.progress.setSecondaryProgress(firstProgress);
//                        }
//
//                        Log.d("录音显示时间333333", firstProgress+"");
//                        handler.sendEmptyMessageDelayed(2, 25);
//                    }
//                    EventBus.getDefault().post(new DubbingEvent());*/
//                    break;
//                default:
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//
//    public void setTimeStamp(long timeStamp) {
//        this.mTimeStamp = timeStamp;
//    }
//
//    class RecordHolder extends RecyclerView.ViewHolder implements TextPage.OnSelectListener {
//
//        private float rate;
//
//        int position1 = 0;
//        int secondPosition = 100;
//        VoaText mVoaText;
//        AnimatorSet animatorSet = new AnimatorSet();
//        private TextPage tvContentEn;
//        private TextView tvContentCn;
//        private TextView tvTime;
//        private DubbingProgressBar progress;
//        private ViewGroup recordLayout;
//        private TextView voiceScore;
//        private ImageView ivPlay;
//        private ImageView ivPause;
//        private TextView tVIndex;
//        private ImageView ivRecord;
//        private LinearLayout wordCorrect;
//        private TextView tvChoose;
//        private Button btCommit;
//
//        RecordHolder(ItemRecordBinding itemView) {
//            super(itemView.getRoot());
//            tvContentEn = itemView.tvContentEn;
//            tvContentCn = itemView.tvContentCh;
//            progress = itemView.progress;
//            tvTime = itemView.tvTime;
//            wordCorrect = itemView.wordCorrect;
//            tvChoose = itemView.wordChoose;
//            btCommit = itemView.wordCommit;
//            recordLayout = itemView.recordLayout;
//            voiceScore = itemView.voiceScore;
//            ivPlay = itemView.ivPlay;
//            ivPause = itemView.ivPause;
//            tVIndex = itemView.tVIndex;
//            ivRecord = itemView.ivRecord;
//        }
//
//        @SuppressLint("CheckResult")
//        public void setItem(RecordHolder holder, VoaText voaText, List<VoaText> voaTextList, int pos) {
//            tvContentEn.setOnSelectListener(this);
//            this.mVoaText = voaText;
//            position1 = voaText.paraId();
//            if (pos < voaTextList.size() - 1) {
//                float addTime = (voaTextList.get(pos + 1).timing() - voaText.endTiming());
//                if (addTime < 1.0) {
//                    Log.e("DubbingAdapter", "pos = " + pos + ", addTime = " + addTime);
//                    addTime = 1;
//                }
//                progress.setAddingTime(MessageFormat.format(itemView.getResources().getString(R.string.record_time),
//                        NumberUtil.keepOneDecimal(addTime)));
//                rate = (voaText.endTiming() - voaText.timing()) * 100 / (voaTextList.get(pos + 1).timing() - voaText.timing());
//            } else {
//                float addTime = (voaText.endTiming() - voaText.timing()) / 4;
//                if (addTime < 1.0) {
//                    Log.e("DubbingAdapter", "pos = " + pos + ", addTime = " + addTime);
//                    addTime = 1;
//                }
//                progress.setAddingTime(MessageFormat.format(itemView.getResources().getString(R.string.record_time),
//                        NumberUtil.keepOneDecimal(addTime)));
//                rate = 70;
//            }
//
//            //样式中显示的进度
//            progress.setPerfectTime(MessageFormat.format(itemView.getResources().getString(R.string.record_time), NumberUtil.keepOneDecimal(voaText.endTiming() - voaText.timing())));
//            progress.setPosition(POSITION);
//            tvTime.setText(MessageFormat.format(itemView.getResources().getString(R.string.record_time), NumberUtil.keepOneDecimal(voaText.endTiming() - voaText.timing())));
//            holder.wordCorrect.setVisibility(View.INVISIBLE);
//            // get record
//
//            long itemId = Long.parseLong(voaText.paraId() + "" + voaText.idIndex()  + "" +  voaText.getVoaId());
//            List<VoaSoundNew> voaRecord = mPresenter.getVoaSoundItemid(itemId, UserInfoManager.getInstance().getUserId());
//            if ((voaRecord != null) && (voaRecord.size()>0)) {
//                VoaSoundNew voaSound = voaRecord.get(0);
//
//                if ((voaSound != null) && (!TextUtils.isEmpty(voaSound.sound_url()))) {
//                    String[] floats = voaSound.wordscore().split(",");
//                    mVoaText.readScore = voaSound.totalscore();
//                    mVoaText.readResult = ResultParse.getSenResultLocal(floats, voaText.sentence());
//                    mVoaText.isRead = true;
//                    mVoaText.evaluateBean = (new EvaluateBean(voaSound.sound_url()));
//                    mVoaText.evaluateBean.setURL(voaSound.sound_url());
//                    mVoaText.pathLocal = voaSound.filepath();
//
//                    int flag = 0;
//                    StringBuilder fWord = new StringBuilder(16);
//                    if (!TextUtils.isEmpty(voaSound.words())) {
//                        try {
//                            EvaluateBean evalBean = GsonUtils.toObject(voaSound.words(), EvaluateBean.class);
//                            if (evalBean != null) {
//                                mVoaText.evaluateBean = evalBean;
//                                for (EvaluateBean.WordsBean word: evalBean.getWords()) {
//                                    if ((word != null) && !TextUtils.isEmpty(word.getContent()) && !("-".equals(word.getContent())) && (word.getScore() < 2.5)) {
//                                        ++flag;
//                                        if (flag < 2) {
//                                            if ("Mr.".equals(word.getContent()) || "Mrs.".equals(word.getContent()) || "Ms.".equals(word.getContent())) {
//                                                fWord.append(word.getContent()).append(" ");
//                                            } else {
//                                                fWord.append(word.getContent().replaceAll("[?:!.,;\"]*([a-zA-Z]+)[?:!.,;\"]*", "$1")).append(" ");
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (Exception var) { }
//                    }
//                    Log.e("DubbingAdapter", "onBindViewHolder flag ----- " + flag);
//                    if (((position1-1) == pos) && (flag > 0)) {
//                        holder.wordCorrect.setVisibility(View.VISIBLE);
//                        if (flag == 1) {
//                            holder.tvChoose.setText(fWord + "单词发音有误");
//                            ViewGroup.LayoutParams params = holder.wordCorrect.getLayoutParams();
//                            params.height = DensityUtil.dp2px(mActivity, 42);
//                            if (fWord.length() < 4) {
//                                params.width = DensityUtil.dp2px(mActivity, 180);
//                                holder.wordCorrect.setLayoutParams(params);
//                            } else if (fWord.length() < 8) {
//                                params.width = DensityUtil.dp2px(mActivity, 200);
//                                holder.wordCorrect.setLayoutParams(params);
//                            } else if (fWord.length() < 12) {
//                                params.width = DensityUtil.dp2px(mActivity, 220);
//                                holder.wordCorrect.setLayoutParams(params);
//                            } else {
//                                params.width = DensityUtil.dp2px(mActivity, 240);
//                                holder.wordCorrect.setLayoutParams(params);
//                            }
//                        } else {
//                            holder.tvChoose.setText(fWord + "等单词发音有误");
//                            ViewGroup.LayoutParams params = holder.wordCorrect.getLayoutParams();
//                            params.height = DensityUtil.dp2px(mActivity, 42);
//                            if (fWord.length() < 4) {
//                                params.width = DensityUtil.dp2px(mActivity, 190);
//                                holder.wordCorrect.setLayoutParams(params);
//                            } else if (fWord.length() < 8) {
//                                params.width = DensityUtil.dp2px(mActivity, 210);
//                                holder.wordCorrect.setLayoutParams(params);
//                            } else if (fWord.length() < 12) {
//                                params.width = DensityUtil.dp2px(mActivity, 230);
//                                holder.wordCorrect.setLayoutParams(params);
//                            } else {
//                                params.width = DensityUtil.dp2px(mActivity, 250);
//                                holder.wordCorrect.setLayoutParams(params);
//                            }
//                        }
//                        holder.btCommit.setOnClickListener(v -> {
//                            Log.e("DubbingAdapter", "EvalCorrectPage clicked.");
//                            // stop the play
//                            mPlayVideoCallback.stop();
//
//
//                            if (isRecording) {
////                                mRecordingCallback.stop();
////                                isRecording = false;
//                                ToastUtil.showToast(mContext,"正在录音中...");
//                                return;
//                            }
//
//                            fragment = new EvalCorrectPage();
//                            fragment.setUid(UserInfoManager.getInstance().getUserId());
//                            fragment.setDataBean(mVoaText.evaluateBean);
//                            fragment.setVoaText(mVoaText);
//                            fragment.show(mActivity.getFragmentManager(), "EvalCorrectPage");
//                        });
//                    } else {
//                        // no data to process
//                    }
//                } else if (!configManager.isAutoDubbing() && (voaSound != null) && !TextUtils.isEmpty(voaSound.filepath())) {
//                    String[] floats = new String[1];
//                    mVoaText.readScore = voaSound.totalscore();
//                    mVoaText.readResult = ResultParse.getSenResultLocal(floats, mVoaText.sentence());
//                    mVoaText.isRead = false;
//                    mVoaText.evaluateBean = (new EvaluateBean(""));
//                    mVoaText.evaluateBean.setURL("");
//                    mVoaText.pathLocal = voaSound.filepath();
//                    Log.e("DubbingAdapter", "onBindViewHolder no auto pathLocal " + mVoaText.pathLocal);
//                }
//            }
//            tVIndex.setText((pos + 1) + "/" + mList.size());
//            //音频
//            String playUrl = null;
//            if (voaRecord!=null&&voaRecord.size()>0){
//                playUrl = voaRecord.get(0).filepath();
//            }
//            setDefaultView(itemId,holder,pos,playUrl);
//            if (mVoaText.isRead) {
//                Log.d("评测显示", mVoaText.sentence());
//                tvContentEn.setText(mVoaText.readResult);
//                voiceScore.setVisibility(View.VISIBLE);
//                holder.mVoaText.setIscore(true);
//                holder.mVoaText.setIsshowbq(true);
//                setScoreColor(holder, mVoaText.readScore);
//            } else {
//                tvContentEn.setText(mVoaText.sentence());
//                showScore();
//            }
//            tvContentEn.setLongClickable(false);
//            showBiaoqian();
//        }
//
//        @SuppressLint("SetTextI18n")
//        private void showScore() {
//            if (mVoaText.isIscore()) {
//                voiceScore.setVisibility(View.GONE);
//                voiceScore.setText(mVoaText.getScore() + "");
//                Log.d("分数显示", mVoaText.getScore()+"--"+mVoaText.sentence());
//                if (mVoaText.getScore() >= 80) {
//                    voiceScore.setBackgroundResource(R.drawable.blue_circle);
//                } else if (mVoaText.getScore() >= 45 && mVoaText.getScore() < 80) {
//                    voiceScore.setBackgroundResource(R.drawable.red_circle);
//                } else {
//                    voiceScore.setBackgroundResource(R.drawable.scroe_low);
//                    voiceScore.setText("");
//                }
//            } else {
//                Log.e("DubbingAdapter", "showScore mVoaText.pathLocal " + mVoaText.pathLocal);
//                if (!configManager.isAutoDubbing() && !TextUtils.isEmpty(mVoaText.pathLocal)) {
//                    voiceScore.setVisibility(View.VISIBLE);
//                    voiceScore.setBackgroundResource(R.drawable.blue_circle);
//                    voiceScore.setText("提交");
//                }
//            }
//        }
//
//        private void showBiaoqian() {
//            if (mVoaText.isIsshowbq()) {
//                ivPlay.setVisibility(View.VISIBLE);
//                tVIndex.setBackgroundResource(R.drawable.index_green);
//            }
//        }
//
//        @SuppressLint("SetTextI18n")
//        private void setDefaultView(long itemId,RecordHolder holder,int position,String playUrl) {
//            ivPlay.setVisibility(View.INVISIBLE);
//            ivPause.setVisibility(View.INVISIBLE);
//            voiceScore.setVisibility(View.GONE);
//            tvContentEn.setText(mVoaText.sentence());
//            tvContentCn.setText(mVoaText.sentenceCn());
//            if ((mVoaText.isRead || !configManager.isAutoDubbing()) && checkExist(mVoaText.getVoaId(), mTimeStamp, getAdapterPosition()+1)) {
//                tVIndex.setBackgroundResource(R.drawable.index_green);
//                /*progress.setProgress(POSITION);
//                progress.setSecondaryProgress(secondPosition);*/
//                showCurEvalProgress(itemId,holder,position,playUrl);
//                ivPlay.setVisibility(View.VISIBLE);
//                ivPause.setVisibility(View.INVISIBLE);
//            } else {
//                tVIndex.setBackgroundResource(R.drawable.index_gray);
//                progress.setProgress(0);
//                progress.setSecondaryProgress(0);
//                ivPlay.setVisibility(View.INVISIBLE);
//                ivPause.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        private boolean checkExist(int voaId, long timestamp, int paraId) {
//            File file = StorageUtil.getAccRecordFile(tVIndex.getContext(), voaId, timestamp, paraId);
//            return file.exists();
//        }
//
//        public void setClick(int position) {
//            ivPlay.setOnClickListener(v -> onPlayClick());
//            ivPause.setOnClickListener(v -> onPauseClick());
//            ivRecord.setOnClickListener(v -> {
//                List<Pair<String, Pair<String,String>>> pairList = new ArrayList<>();
//                pairList.add(new Pair<>(Manifest.permission.RECORD_AUDIO,new Pair<>("麦克风权限","录制评测时朗读的音频，用于评测打分使用")));
//                pairList.add(new Pair<>(Manifest.permission.WRITE_EXTERNAL_STORAGE,new Pair<>("存储权限","保存评测的音频文件，用于评测打分使用")));
//
//                LibPermissionDialogUtil.getInstance().showMsgDialog(mContext, pairList, new LibPermissionDialogUtil.OnPermissionResultListener() {
//                    @Override
//                    public void onGranted(boolean isSuccess) {
//                        if (isSuccess){
//                            onRecordClick(position);
//                        }
//                    }
//                });
//            });
//            recordLayout.setOnClickListener(v ->
//                    onLayoutClick()
//            );
//            if (!configManager.isAutoDubbing()) {
//                voiceScore.setOnClickListener(v -> onUploadClick());
//            }
//        }
//
//        void onUploadClick() {
//            if (mVoaText.isIscore()) {
//                Log.e("DubbingAdapter", "onUploadClick is already scored ");
//                return;
//            }
//            if (TextUtils.isEmpty(mVoaText.pathLocal)) {
//                ToastUtil.show(mContext, "您还没有录制呢");
//                return;
//            }
//            Log.e("DubbingAdapter", "onUploadClick mVoaText.pathLocal " + mVoaText.pathLocal);
//            File file = new File(mVoaText.pathLocal);
//            if (file.exists() && file.isFile()) {
//                mOperateHolder = this;
//                int temp = (int) (mOperateHolder.rate * 100 / mOperateHolder.progress.getSecondaryProgress());
//                fluent = Math.min(temp, 100);
//                setCallBack(this);
//                beginUITransform();
//                mRecordingCallback.upload(mVoaText, mVoaText.pathLocal, 1);
//            }
//        }
//
//        void onPlayClick() {
//            mOperateHolder = this;
//            isRecording = false;
//            if (ivPlay.getVisibility() == View.VISIBLE) {
//                if (isRecording) {
//                    mRecordingCallback.stop();
//                }
//                mActivitePosition = getAdapterPosition();
//                if (mOperateHolder != null) {
//                    tVIndex.setBackgroundResource(R.drawable.index_green);
//                    ivPause.setVisibility(View.INVISIBLE);
//                    ivPlay.setVisibility(View.VISIBLE);
//                }
//                mOperateVoaText = mVoaText;
//                ivPlay.setVisibility(View.INVISIBLE);
//                ivPause.setVisibility(View.VISIBLE);
//                playRecordTask(mOperateVoaText);
//            }
//        }
//
//        public void playRecordTask(final VoaText voaText) {
//            if (isRecording) {
//                mRecordingCallback.stop();
//                mRecordingCallback.upload(voaText, "", 0);
//                mRecordingCallback.convert(voaText.paraId(), mList);
//                isRecording = false;
//            }
//            mPlayVideoCallback.stop();
//            mPlayRecordCallback.start(voaText);
//            handler.sendEmptyMessage(1);
//        }
//
//
//        void onPauseClick() {
//            if (ivPause.getVisibility() == View.VISIBLE) {
//                ivPause.setVisibility(View.INVISIBLE);
//                ivPlay.setVisibility(View.VISIBLE);
//                mPlayRecordCallback.stop();
//                mPlayVideoCallback.stop();
//                if (isRecording) {
//                    isRecording = false;
//                    mRecordingCallback.stop();
//                    mRecordingCallback.upload(mVoaText, "", 0);
//                    mRecordingCallback.convert(mVoaText.paraId(), mList);
//                }
//            }
//            handler.removeCallbacksAndMessages(null);
//        }
//
//        void onRecordClick(int position) {
//            //先把权限给开启了
//            /*if (!PermissionX.isGranted(mContext, Manifest.permission.RECORD_AUDIO)
//                    ||!PermissionX.isGranted(mContext,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                PermissionX.init((FragmentActivity) mContext).permissions(Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .request(new RequestCallback() {
//                            @Override
//                            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
//                                if (!allGranted){
//                                    Toast.makeText(mContext, "请授予必要的权限", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//                return;
//            }*/
//
//            //如果不是会员并且评测超过三句了，则提示开通会员
//            int itemId = Integer.parseInt(mVoaText.paraId() + "" + mVoaText.idIndex()  + "" +  mVoaText.getVoaId());
//            if (mPresenter.isLimitEval(mVoa.voaId(),itemId)){
//                new AlertDialog.Builder(mContext)
//                        .setTitle("开通会员")
//                        .setMessage("非会员可评测3句，会员尊享无限制评测，是否开通会员继续评测学习？")
//                        .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent();
//                                intent.setClass(mContext,NewVipCenterActivity.class);
//                                intent.putExtra(NewVipCenterActivity.HUI_YUAN,NewVipCenterActivity.BENYINGYONG);
//                                mContext.startActivity(intent);
//                            }
//                        }).setNegativeButton("暂不使用",null)
//                        .show();
//                return;
//            }
//
//            if (isRecording) {
//                endRecording();
//                return;
//            }
//
//            //关闭上一个的动画
//            if (mOperateHolder!=null){
//                mOperateHolder.ivPause.setVisibility(View.GONE);
//                mOperateHolder.ivPlay.setVisibility(View.VISIBLE);
//            }
//
//            //录音操作
//            recordTime = 0;
//            recordIndex = position;
//
//            mOperateHolder = this;
//            //先隐藏纠音
//            mOperateHolder.wordCorrect.setVisibility(View.INVISIBLE);
//            //设置其他操作
//            mActivitePosition = getAdapterPosition();
//            voiceScore.setText("");
//            voiceScore.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
//            if (mOperateVoaText == mVoaText && isRecording) {
//                mRecordingCallback.stop();
//                mRecordingCallback.upload(mVoaText, "", 0);
//                mRecordingCallback.convert(mVoaText.paraId(), mList);
//                tVIndex.setBackgroundResource(R.drawable.index_green);
//                ivPlay.setVisibility(View.VISIBLE);
//            } else {
//                isRecording = true;
////                wordCorrect.setVisibility(View.VISIBLE);
//                if (mOperateHolder != null) {
//                    tVIndex.setBackgroundResource(R.drawable.index_green);
//                    ivPlay.setVisibility(View.VISIBLE);
//                    ivPause.setVisibility(View.INVISIBLE);
//                }
//                mOperateVoaText = mVoaText;
//                //是否需要重新初始化
//                File saveFile = StorageUtil.getAccRecordFile(itemView.getContext(), mVoaText.getVoaId(), mTimeStamp, getAdapterPosition()+1);
//                mVoaText.isRead = false;
//                mVoaText.setIscore(false);
//                mOperateVoaText.pathLocal = saveFile.getAbsolutePath();
//                Log.e("DubbingAdapter", "onRecordClick saveFile.getAbsolutePath() " + saveFile.getAbsolutePath());
//                mRecordingCallback.init(saveFile.getAbsolutePath());
//                mPlayRecordCallback.stop();
//                ivPlay.setVisibility(View.INVISIBLE);
//                ivPause.setVisibility(View.INVISIBLE);
//                progress.setProgress(0);
//                progress.setSecondaryProgress(0);
//                handler.sendEmptyMessage(2);
//                setCallBack(this);
//                mRecordingCallback.start(mVoaText);
//                //保存录音开始时间
//                recordStartTime = System.currentTimeMillis();
//            }
//        }
//
//        void onLayoutClick() {
//            if (isRecording && (mActivitePosition != getAdapterPosition())) {
//                if (!configManager.isAutoDubbing()) {
//                    Log.e("DubbingAdapter", "onLayoutClick not auto mActivitePosition " + mActivitePosition);
//                    saveRecording();
//                }
//            }
//            if (isRecording)
//                return;
//            mActivitePosition = getAdapterPosition();
//            repeatPlayVoaText(mVoaText);
//        }
//
//        private void setCallBack(RecordHolder recordHolder) {
//            ((DubbingActivity) mContext).setNewScoreCallback(new DubbingActivity.NewScoreCallback() {
//                @Override
//                public void onResult(int pos, int score, SendEvaluateResponse.DataBean beans) {
//                    if (pos == recordHolder.position1) {
//                        if (null != recordHolder.animatorSet) {
//                            recordHolder.animatorSet.cancel();
//                        }
//                        resetText(recordHolder);
//                        mScoreCallback.onResult(recordHolder.getAdapterPosition(), score, fluent, beans.getURL());
//                        setTextColorSpan(recordHolder, beans.getWords());
//                        setScoreColor(recordHolder, score);
//                    }
//
//
//                    //判断下纠音是否显示
//                    boolean showCheck = false;
//                    check:for (int i = 0; i < beans.getWords().size(); i++) {
//                        double showScore = beans.getWords().get(i).getScore();
//                        if (showScore<2.5f){
//                            showCheck = true;
//                            break check;
//                        }
//                    }
//
//                    if (showCheck){
//                        mOperateHolder.wordCorrect.setVisibility(View.VISIBLE);
//                    }
//                }
//
//                @Override
//                public void onError(int pos, String errorMessage) {
////                    ToastUtil.show(mContext, errorMessage);
//                    if (pos == recordHolder.position1) {
//                        if (null != recordHolder.animatorSet) {
//                            recordHolder.animatorSet.cancel();
//                        }
//                        resetText(recordHolder);
//                    }
//                }
//
//            });
//        }
//
//        private void setTextColorSpan(RecordHolder recordHolder, List<SendEvaluateResponse.DataBean.WordsBean> beans) {
//            String string = tvContentEn.getText().toString();
//            SpannableString spannableString = new SpannableString(string);
//            int curentIndex = 0;
//            for (int i = 0; i < beans.size(); i++) {
//                if (beans.get(i).getScore() < 2.5) {
//                    spannableString.setSpan(new ForegroundColorSpan(Color.RED), curentIndex, curentIndex + beans.get(i).getContent().length()
//                            , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                } else if (beans.get(i).getScore() > 3.75) {
//                    spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.GREEN)), curentIndex, curentIndex + beans.get(i).getContent().length()
//                            , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                }
//                curentIndex += beans.get(i).getContent().length() + 1;
//            }
//            tvContentEn.setText(spannableString);
//            recordHolder.mVoaText.setParseData(new SpannableStringBuilder(spannableString));
//        }
//
//        @SuppressLint("SetTextI18n")
//        private void setScoreColor(RecordHolder holder, int score) {
//            if (score >= 80) {
//                voiceScore.setBackgroundResource(R.drawable.blue_circle);
//                voiceScore.setText(score + "");
//                Log.d("分数显示", "第2个--"+mVoaText.getScore()+"--"+mVoaText.paraId());
//                holder.mVoaText.setScore(score);
//            } else if (score >= 50 && score < 80) {
//                voiceScore.setBackgroundResource(R.drawable.red_circle);
//                voiceScore.setText(score + "");
//                Log.d("分数显示", "第3个--"+mVoaText.getScore()+"--"+mVoaText.paraId());
//                holder.mVoaText.setScore(score);
//            } else {
//                holder.mVoaText.setScore(score);
//                voiceScore.setBackgroundResource(R.drawable.scroe_low);
//                voiceScore.setText("");
//            }
//            if (holder.mVoaText.isIsshowbq()) {
//                ivPlay.setVisibility(View.VISIBLE);
//                tVIndex.setBackgroundResource(R.drawable.index_green);
//            }
//        }
//
//        private void resetText(RecordHolder holder) {
//            holder.mVoaText.setIsshowbq(true);
//            holder.mVoaText.setIscore(true);
//            voiceScore.setRotation(0);
//            voiceScore.setAlpha(1);
//            voiceScore.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onSelect(String text) {
//            if (text != null && text.length() != 0) {
//                if (mActivity != null && mRecordingCallback != null) {
//                    mRecordingCallback.lookWord(text);
//                } else {
//                    mPresenter.getNetworkInterpretation(text);
//                }
//            } else {
//                ToastUtil.show(mContext, "请取英文单词");
//            }
//        }
//    }
//
//
//    public void repeatPlayVoaText(final VoaText voaText) {
//        mOperateVoaText = voaText;
//        mPlayVideoCallback.start(voaText);
//    }
//
//    public VoaText getOperateVoaText() {
//        return mOperateVoaText;
//    }
//
//    private void beginUITransform() {
//        mOperateHolder.voiceScore.setVisibility(View.VISIBLE);
//        mOperateHolder.voiceScore.setBackgroundResource(R.drawable.ic_wait_64px);
//        ValueAnimator rotateAnim = ObjectAnimator.ofFloat(mOperateHolder.voiceScore, "rotation", 0f, 45f);
//        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(mOperateHolder.voiceScore, "alpha", 0f, 1f, 0f);
//        rotateAnim.setRepeatCount(3000);
//        fadeAnim.setRepeatCount(300);
//        mOperateHolder.animatorSet.playTogether(rotateAnim, fadeAnim);
//        mOperateHolder.animatorSet.setDuration(1050);
//        mOperateHolder.animatorSet.start();
//    }
//
//    private void saveRecording() {
//        handler.removeCallbacksAndMessages(null);
//        mOperateHolder.voiceScore.setVisibility(View.VISIBLE);
//        mOperateHolder.voiceScore.setBackgroundResource(R.drawable.blue_circle);
//        mOperateHolder.voiceScore.setText("提交");
//        Log.e("DubbingAdapter", "saveRecording : no upload ");
//        mRecordingCallback.stop();
//        mRecordingCallback.save(mOperateVoaText);
//        mPlayVideoCallback.stop();
//        isRecording = false;
//        mOperateHolder.ivPlay.setVisibility(View.VISIBLE);
//        mOperateHolder.tVIndex.setBackgroundResource(R.drawable.index_green);
//    }
//
//    private void endRecording() {
//        handler.removeCallbacksAndMessages(null);
//        int temp = (int) (mOperateHolder.rate * 100 / mOperateHolder.progress.getSecondaryProgress());
//        fluent = Math.min(temp, 100);
//        beginUITransform();
//        Log.e("DubbingAdapter", "endRecording : >>> cancel ");
//        stopRecordTask(mOperateVoaText);
//        isRecording = false;
//        mOperateHolder.ivPlay.setVisibility(View.VISIBLE);
//        mOperateHolder.tVIndex.setBackgroundResource(R.drawable.index_green);
//        //保存录音结束时间
//        recordEndTime = System.currentTimeMillis();
//    }
//
//    public void stopRecordTask(final VoaText voaText) {
//        if (isRecording) {
//            mRecordingCallback.stop();
//            mRecordingCallback.upload(voaText, "", 0);
//            mRecordingCallback.convert(voaText.paraId(), mList);
//        }
//        mPlayVideoCallback.stop();
//    }
//
//    public void stopRecordPlayView() {
//        if (mOperateHolder != null) {
//            mOperateHolder.ivPlay.setVisibility(View.VISIBLE);
//            mOperateHolder.ivPause.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    interface RecordingCallback {
//        void init(String path);
//
//        void start(VoaText voaText);
//
//        boolean isRecording();
//        void lookWord(String word);
//        void setRecordingState(boolean state);
//
//        void stop();
//        void save(VoaText list);
//
//        // 現在不需要转化成mp3了
//        void convert(int paraId, List<VoaText> list);
//
//        void upload(VoaText list, String path, int flag);
//    }
//
//    interface PlayRecordCallback {
//        void start(VoaText voaText);
//
//        void stop();
//
//        int getLength();
//    }
//
//    interface PlayVideoCallback {
//        void start(VoaText voaText);
//
//        boolean isPlaying();
//
//        int getCurPosition();
//
//        void stop();
//
//        int totalTimes();
//    }
//
//    interface ScoreCallback {
//        void onResult(int pos, int score, int fluence, String url);
//    }
//
//    private Voa mVoa;
//    public void SetVoa(Voa voa) {
//        mVoa = voa;
//    }
//    Activity mActivity;
//    public void SetActivity(Activity activ) {
//        mActivity = activ;
//    }
//
//
//    /*********************************进度计算****************************/
//    //获取当前配音的总时间
//    private long getShowAudioTotalTime(){
//        return 0;
//    }
//
//    //获取当前配音的实际时长
//    private long getShowAudioRealTime(double startTime,double endTime){
//        double realTime = LibBigDecimalUtil.trans2Double(endTime-startTime);
//        return (long) (realTime*1000L);
//    }
//
//    //获取当前配音的多余时长
//    private long getShowAudioMoreTime(double curEndTime,double nextStartTime){
//        //这里涉及到最后的数据，需要处理下
//        double addTime = LibBigDecimalUtil.trans2Double(nextStartTime-curEndTime);
//        return (long) (addTime*1000L);
//    }
//
//    //获取当前评测音频的时长
//    private long getEvalAudioTime(String evalUrlOrPath){
//        if (TextUtils.isEmpty(evalUrlOrPath)){
//            return 0;
//        }
//
//        try {
//            MediaPlayer player = new MediaPlayer();
//            player.setDataSource(evalUrlOrPath);
//            player.prepare();
//            return player.getDuration();
//        }catch (Exception e){
//            return 0;
//        }
//    }
//
//    //计算并显示当前样式的时长进度
//    private void showCurEvalProgress(long itemId,RecordHolder holder,int position,String playUrlOrPath){
//        long progressTime = (long) (LibBigDecimalUtil.trans2Double(mList.get(position).endTiming()-mList.get(position).timing())*1000L);
//        float endTag = 0;
//        if (position<mList.size()-1){
//            endTag = mList.get(position+1).timing();
//        }else {
//            endTag = mList.get(position).endTiming()+0.1f;
//        }
//        long addTime = (long) (BigDecimalUtil.trans2Double(endTag-mList.get(position).endTiming())*1000L);
//        if (addTime<1.0f){
//            addTime = 1000L;
//        }
//        //音频时长
//        long playTime = getEvalAudioTime(playUrlOrPath);
//        DubbingHelpEntity helpEntity = CommonDataManager.getDubbingHelpData(itemId,UserInfoManager.getInstance().getUserId());
//        if (helpEntity!=null){
//            playTime = helpEntity.recordTime;
//        }
//        //总时长
//        long totalTime = progressTime+addTime;
//
//        //进度显示
//        if (playTime>=totalTime){
//            holder.progress.setProgress(POSITION);
//            holder.progress.setSecondaryProgress(FULL_PERCENT);
//        }else {
//            if (playTime>progressTime){
//                holder.progress.setProgress(POSITION);
//                int addProgress = (int) ((playTime*100.0f/totalTime)*FULL_PERCENT/100.0f);
//                holder.progress.setSecondaryProgress(POSITION+addProgress);
//            }else if (playTime==progressTime){
//                holder.progress.setProgress(POSITION);
//                holder.progress.setSecondaryProgress(POSITION);
//            }else {
//                int showProgress = (int) ((playTime*100.0f/progressTime)*POSITION/100.0f);
//                holder.progress.setProgress(showProgress);
//                holder.progress.setSecondaryProgress(showProgress);
//            }
//        }
//    }
//
//    //获取当前录音的时间
//    public long getRecordTime(){
//        long curRecordTime = recordEndTime-recordStartTime;
//        if (curRecordTime>0){
//            return curRecordTime;
//        }
//        return 0;
//    }
//}