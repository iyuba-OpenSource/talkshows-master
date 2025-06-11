package com.iyuba.lib_common.model.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

/**
 * 配音操作辅助功能
 */
@Entity(primaryKeys = {"itemId","userId"})
public class DubbingHelpEntity {

    @NonNull
    public long itemId;//id(voaId+""+paraId+""+idIndex转换成long)
    @NonNull
    public int userId;


    @NonNull
    public long recordTime;//录音时间
    public String sentence;
    @NonNull
    public double scores;
    @NonNull
    public double total_score;
    public String filepath;
    public String url;
    public String wordList;

    public DubbingHelpEntity() {
    }

    @Ignore
    public DubbingHelpEntity(long itemId, int userId, long recordTime, String sentence, double scores, double total_score, String filepath, String url, String wordList) {
        this.itemId = itemId;
        this.userId = userId;
        this.recordTime = recordTime;
        this.sentence = sentence;
        this.scores = scores;
        this.total_score = total_score;
        this.filepath = filepath;
        this.url = url;
        this.wordList = wordList;
    }
}
