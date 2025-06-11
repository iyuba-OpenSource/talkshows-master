package com.iyuba.lib_common.model.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

/**
 * 评测结果数据表
 */
@Entity(primaryKeys = {"userId","voaId","paraId","indexId"})
public class DubbingEntity {

    @NonNull
    public int userId;//用户id
    @NonNull
    public int voaId;//课程id
    @NonNull
    public int paraId;
    @NonNull
    public int indexId;

    //录音的时间
    public long recordTime;//录音时长

    public String sentence;
    public double score;
    public double showScore;
    public String localPath;
    public String audioUrl;
    public String wordData;

    public DubbingEntity() {
    }

    @Ignore
    public DubbingEntity(int userId, int voaId, int paraId, int indexId, long recordTime, String sentence, double score, double showScore, String localPath, String audioUrl, String wordData) {
        this.userId = userId;
        this.voaId = voaId;
        this.paraId = paraId;
        this.indexId = indexId;
        this.recordTime = recordTime;
        this.sentence = sentence;
        this.score = score;
        this.showScore = showScore;
        this.localPath = localPath;
        this.audioUrl = audioUrl;
        this.wordData = wordData;
    }
}
