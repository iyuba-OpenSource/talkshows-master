//package com.iyuba.talkshow.util.ise;
//
//import android.support.annotation.Nullable;
//import android.text.SpannableStringBuilder;
//import android.util.Pair;
//
//import com.iflytek.ise.result.Result;
//import com.iyuba.play.Playable;
//import com.iyuba.voaseries.Constant;
//import com.iyuba.voaseries.data.model.ReadVoiceComment;
//import com.iyuba.voaseries.data.model.Shareable;
//import com.iyuba.voaseries.util.FileUploader;
//
//import java.io.File;
//
//public class ReadEvaluateItem implements Playable, FileUploader.Uploadable {
//
//    public static final long TO_THE_END = -233;
//
//    public int order;
//    public int voaid;
//    public String sentence;
//    public String sentenceCN;
//    public long startTime;
//    public long endTime;
//    public String paraID;
//    public String indexID;
//
//    public int readScore = -1;
//    public SpannableStringBuilder readResult;
//    public String lowScoreWords;
//
//    private boolean mSendable = false;
//
//    private ReadVoiceComment mReadVoiceComment;
//
//    public long getDuration() {
//        if (startTime > 0 && endTime > startTime) {
//            return endTime - startTime;
//        }
//        return 0;
//    }
//
//    public boolean isPlayToTheEnd() {
//        return endTime == TO_THE_END;
//    }
//
//    public boolean isReaded() {
//        return readScore > 0 && readResult != null;
//    }
//
//    public String getRecordPCMPath() {
//        return Constant.getRecordAddr() + order + Constant.AudioSuffix.PCM;
//    }
//
//    public String getRecordWavPath() {
//        return Constant.getRecordAddr() + order + Constant.AudioSuffix.WAV;
//    }
//
//    public String getRecordMp3Path() {
//        return Constant.getRecordAddr() + order + Constant.AudioSuffix.MP3;
//    }
//
//    public void clearOldFiles() {
//        File pcm = new File(getRecordPCMPath());
//        File wav = new File(getRecordWavPath());
//        File mp3 = new File(getRecordMp3Path());
//        if (pcm.exists()) pcm.delete();
//        if (wav.exists()) wav.delete();
//        if (mp3.exists()) mp3.delete();
//    }
//
//    public String getRecordFilePath() {
////        return Constant.getRecordAddr() + order + Constant.getRecordTag();
//        return getRecordMp3Path();
//    }
//
//    public void parseResult(Result result) {
//        readScore = (int) (result.total_score * 20);
//        Pair<SpannableStringBuilder, String> pr = ResultParser.getSenResult(result, sentence);
//        readResult = pr.first;
//        lowScoreWords = pr.second;
//    }
//
//    public void setReadVoiceComment(@Nullable ReadVoiceComment comment) {
//        mReadVoiceComment = comment;
//        comment.setReadEvaluateItemReference(this);
//    }
//
//    public boolean hasReadVoiceComment() {
//        return mReadVoiceComment != null;
//    }
//
//    public void setSendable(boolean value) {
//        mSendable = value;
//    }
//
//    public boolean isSendable() {
//        return mSendable;
//    }
//
//    public File getRecordFile() {
//        return new File(getRecordFilePath());
//    }
//
//    public boolean isShareable() {
//        return mReadVoiceComment != null;
//    }
//
//    public Shareable getShareableStuff() {
//        return mReadVoiceComment;
//    }
//
//    @Override
//    public File getUploadedFile() {
//        return getRecordFile();
//    }
//
//    @Override
//    public String getPlayableUrl() {
//        return getRecordFilePath();
//    }
//
//    @Override
//    public void refreshPlayableUrl() {
//        // do nothing here
//    }
//    // TODO put more domain-related methods into this Model
//
//}
