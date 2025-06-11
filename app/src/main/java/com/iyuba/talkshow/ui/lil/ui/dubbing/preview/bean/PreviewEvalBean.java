package com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean;

/**
 * 评测数据
 */
public class PreviewEvalBean {

    private long startTime;
    private long playTime;
    private long endTime;

    private String playPath;//播放地址-本地

    private int voaId;
    private int paraId;
    private int idIndex;

    public PreviewEvalBean(long startTime, long playTime, long endTime, String playPath, int voaId, int paraId, int idIndex) {
        this.startTime = startTime;
        this.playTime = playTime;
        this.endTime = endTime;
        this.playPath = playPath;
        this.voaId = voaId;
        this.paraId = paraId;
        this.idIndex = idIndex;
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

    public String getPlayPath() {
        return playPath;
    }

    public int getVoaId() {
        return voaId;
    }

    public int getParaId() {
        return paraId;
    }

    public int getIdIndex() {
        return idIndex;
    }
}
