package com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean;

/**
 * 展示数据
 */
public class PreviewShowBean {

    public static final int type_audio = 0;
    public static final int type_eval = 1;


    private long startTime;
    private long playTime;
    private long endTime;


    private int audioType;//类型-音频、评测
    private String playPath;//播放地址-本地

    public PreviewShowBean(long startTime, long playTime, long endTime, int audioType, String playPath) {
        this.startTime = startTime;
        this.playTime = playTime;
        this.endTime = endTime;
        this.audioType = audioType;
        this.playPath = playPath;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getPlayTime() {
        return playTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getAudioType() {
        return audioType;
    }

    public String getPlayPath() {
        return playPath;
    }
}
