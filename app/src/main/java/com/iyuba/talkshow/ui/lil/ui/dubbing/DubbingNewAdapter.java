package com.iyuba.talkshow.ui.lil.ui.dubbing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.lib_common.model.local.entity.DubbingEntity;
import com.iyuba.lib_common.model.remote.bean.Eval_result;
import com.iyuba.lib_common.model.remote.manager.DubbingManager;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.toolbox.GsonUtils;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.databinding.ItemDubbingNewBinding;
import com.iyuba.talkshow.databinding.ItemRecordBinding;
import com.iyuba.talkshow.ui.lil.util.BigDecimalUtil;
import com.iyuba.talkshow.ui.lil.util.ResultParse;
import com.iyuba.talkshow.ui.widget.DubbingProgressBar;
import com.iyuba.talkshow.util.NumberUtil;
import com.iyuba.textpage.TextPage;
import com.iyuba.wordtest.utils.ToastUtil;

import java.text.MessageFormat;
import java.util.List;

public class DubbingNewAdapter extends RecyclerView.Adapter<DubbingNewAdapter.DubbingNewHolder> {

    private Context context;
    private List<VoaText> list;

    //进度条分界点
    private static final int Tag_progress = 65;
    private static final int Tag_all = 100;

    //视频总时长(计算最后一个加时使用)
    private long videoDurationTime = 0;

    //是否正在录音
    private boolean isRecording = false;

    /****************正式的数据****************/
    //选中的位置
    private int selectIndex = -1;
    //选中的样式
    private DubbingNewHolder selectHolder;
    //选中的数据
    private VoaText selectData;
    //选中的评测数据
    private DubbingEntity selectEval;

    /******************临时数据******************/
    private int tempIndex = -1;
    private DubbingNewHolder tempHolder;
    private VoaText tempData;
    private DubbingEntity tempEval;

    public DubbingNewAdapter(Context context, List<VoaText> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DubbingNewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDubbingNewBinding binding = ItemDubbingNewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new DubbingNewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DubbingNewHolder holder, @SuppressLint("RecyclerView") int position) {
        VoaText voaText = list.get(position);
        holder.index.setText(String.valueOf(position + 1));
        holder.sentenceEn.setText(voaText.sentence());
        holder.sentenceCn.setText(voaText.sentenceCn());
        //设置进度的显示
        holder.progressBar.setPosition(Tag_progress);
        holder.progressBar.setPerfectTime(MessageFormat.format(context.getString(R.string.record_time), NumberUtil.keepOneDecimal(getTextShowTime(position) / 1000.0f)));
        holder.progressBar.setAddingTime(MessageFormat.format(context.getString(R.string.record_time), NumberUtil.keepOneDecimal(getTextAddTime(position) / 1000.0f)));

        //判断评测数据
        DubbingEntity entity = DubbingManager.getSingleDubbingData(UserInfoManager.getInstance().getUserId(), voaText.getVoaId(), voaText.paraId(), voaText.idIndex());
        if (entity != null) {
            holder.play.setVisibility(View.VISIBLE);
            holder.score.setVisibility(View.VISIBLE);
            holder.index.setBackgroundResource(R.drawable.index_green);

            if (entity.showScore >= 80) {
                holder.score.setText(String.valueOf((int) entity.showScore));
                holder.score.setBackgroundResource(R.drawable.blue_circle);
            } else if (entity.showScore >= 45 && entity.showScore < 80) {
                holder.score.setText(String.valueOf((int) entity.showScore));
                holder.score.setBackgroundResource(R.drawable.red_circle);
            } else {
                holder.score.setText("");
                holder.score.setBackgroundResource(R.drawable.scroe_low);
            }

            //转换单词显示
            List<Eval_result.WordsBean> wordsBeanList = GsonUtils.toObjectList(entity.wordData, Eval_result.WordsBean.class);
            holder.sentenceEn.setText(ResultParse.getSenResultLocal(getWordScore(wordsBeanList), entity.sentence));

            //显示进度样式
            long totalTime = getTextShowTime(position) + getTextAddTime(position);
            refreshProgress(holder, entity.recordTime, totalTime);
            Log.d("数据显示", "0000-----"+position);
        } else {
            holder.play.setVisibility(View.INVISIBLE);
            holder.score.setVisibility(View.INVISIBLE);
            holder.index.setBackgroundResource(R.drawable.index_gray);

            holder.progressBar.setProgress(0);
            holder.progressBar.setSecondaryProgress(0);
        }

        //显示操作
        holder.itemView.setOnClickListener(v -> {
            long startTime = (long) (voaText.timing() * 1000L);
            long endTime = (long) (voaText.endTiming() * 1000L);

            //增加补时
            long addTime = getTextAddTime(position);
            endTime = endTime + addTime;

            if (onDubbingListener != null) {
                onDubbingListener.onPlayVideo(position, startTime, endTime);
            }
        });
        holder.record.setOnClickListener(v -> {
            if (selectIndex!=position){
                if (selectIndex>=0&&isRecording){
                    ToastUtil.showToast(context, "正在录音中～");
                    return;
                }
            }

            //记录下数据
            this.selectIndex = position;
            this.selectHolder = holder;
            this.selectData = voaText;
            this.selectEval = entity;

            long startTime = (long) (voaText.timing() * 1000L);
            long endTime = (long) (voaText.endTiming() * 1000L);

            //增加补时
            long addTime = getTextAddTime(position);
            endTime = endTime + addTime;

            if (onDubbingListener != null) {
                onDubbingListener.onRecordAudio(voaText.sentence(), startTime,endTime, voaText.getVoaId(), voaText.paraId(), voaText.idIndex());
            }
        });
        holder.play.setOnClickListener(v -> {
            //记录下临时数据
            tempIndex = position;
            tempHolder = holder;
            tempData = voaText;
            tempEval = entity;

            if (onDubbingListener != null) {
                onDubbingListener.onPlayEval(entity.audioUrl, voaText.getVoaId(), voaText.paraId(), voaText.idIndex());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class DubbingNewHolder extends RecyclerView.ViewHolder {

        private TextView index;
        private TextView score;
        private TextView sentenceEn;
        private TextView sentenceCn;

        private ConstraintLayout bottomLayout;
        private DubbingProgressBar progressBar;
        private ImageView play;
        private ImageView record;

        public DubbingNewHolder(ItemDubbingNewBinding binding) {
            super(binding.getRoot());

            index = binding.index;
            score = binding.score;
            sentenceEn = binding.sentenceEn;
            sentenceCn = binding.sentenceCn;

            bottomLayout = binding.bottomLayout;
            progressBar = binding.progressBar;
            play = binding.play;
            record = binding.record;
        }
    }

    /****************************刷新数据**********************/
    //刷新数据
    public void refreshData(List<VoaText> refreshList) {
        this.list = refreshList;
        notifyDataSetChanged();

        Log.d("数据显示000", "数据刷新");
    }

    //刷新视频总时长
    public void refreshVideoDuration(long totalTime) {
        this.videoDurationTime = totalTime;
        notifyDataSetChanged();

        Log.d("数据显示000", "视频刷新");
    }

    //刷新进度显示
    private void refreshProgress(DubbingNewHolder holder, long progress, long total) {
        //当前进度
        int showProgress = (int) (progress * 100 / total);
        if (showProgress>Tag_all && isRecording){
            Log.d("数据显示", "抛弃");
            return;
        }

        if (showProgress >= Tag_all) {
            holder.progressBar.setProgress(Tag_progress);
            holder.progressBar.setSecondaryProgress(Tag_all);
        } else {
            if (showProgress >= Tag_progress) {
                holder.progressBar.setProgress(Tag_progress);
            } else {
                holder.progressBar.setProgress(showProgress);
            }
            holder.progressBar.setSecondaryProgress(showProgress);
        }

        Log.d("数据显示", showProgress+"---"+progress+"--"+total+"---"+isRecording);
    }

    //重置当前的进度显示
    public void resetRecord(){
        if (selectHolder!=null){
            selectHolder.progressBar.setProgress(0);
            selectHolder.progressBar.setSecondaryProgress(0);
        }
    }

    //刷新录音的样式和进度条样式
    public void refreshRecord(long progress, long total, boolean isRecord) {
        this.isRecording = isRecord;
        Log.d("数据显示", "录音状态--"+isRecord);

        if (isRecord) {
            refreshProgress(selectHolder, progress, total);
        } else {
            notifyDataSetChanged();
        }
    }

    //刷新播放音频
    public void refreshAudio(boolean isPlay) {
        if (selectHolder==null&&tempHolder==null){
            return;
        }

        if (selectIndex >= 0 && selectIndex != tempIndex && selectHolder!=null) {
            selectHolder.play.setImageResource(R.drawable.play);
        }

        if (tempIndex >= 0) {
            selectIndex = tempIndex;
            selectHolder = tempHolder;
            selectData = tempData;
            selectEval = tempEval;

            //取消数据
            tempIndex = -1;
        }

        if (isPlay) {
            selectHolder.play.setImageResource(R.drawable.pause);
        } else {
            selectHolder.play.setImageResource(R.drawable.play);
        }
    }

    /*****************************进度样式************************/
    //获取当前的文本时间
    private long getTextShowTime(int position) {
        long startTime = (long) (list.get(position).timing() * 1000L);
        long endTime = (long) (list.get(position).endTiming() * 1000L);
        return endTime - startTime;
    }

    //获取当前的文本增加时间
    private long getTextAddTime(int position) {
        //正文结束时间
        long addStartTime = (long) (list.get(position).endTiming() * 1000L);
        long extendTime = 0;

        if (position == list.size() - 1) {
            if (videoDurationTime > addStartTime) {
                extendTime = videoDurationTime - addStartTime;
            } else {
                extendTime = 0;
            }
        } else {
            extendTime = (long) (list.get(position + 1).timing() * 1000L) - addStartTime;
        }

        if (extendTime > 3000L) {
            extendTime = 3000L;
        }
        return extendTime;
    }

    //获取当前的评测音频时间

    /*******************************回调****************************/
    private OnDubbingListener onDubbingListener;

    public interface OnDubbingListener {
        //播放视频
        void onPlayVideo(int position, long startTime, long endTime);

        //播放评测音频
        void onPlayEval(String evalUrl, int voaId, int paraId, int indexId);

        //录音
        void onRecordAudio(String sentence, long startTime,long endTime, int voaId, int paraId, int indexId);
    }

    public void setOnDubbingListener(OnDubbingListener onDubbingListener) {
        this.onDubbingListener = onDubbingListener;
    }

    /************************************其他操作************************/
    //获取成绩的集合
    private String[] getWordScore(List<Eval_result.WordsBean> list) {
        String[] scoreArray = null;
        if (list != null && list.size() > 0) {
            scoreArray = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                Eval_result.WordsBean wordsBean = list.get(i);
                scoreArray[i] = wordsBean.getScore();
            }
        }
        return scoreArray;
    }

    //之前选中的数据
    public int getSelectIndex() {
        return selectIndex;
    }

    //当前选中的数据
    public int getTempIndex() {
        return tempIndex;
    }
}
