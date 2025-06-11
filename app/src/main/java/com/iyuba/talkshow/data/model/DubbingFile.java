package com.iyuba.talkshow.data.model;

import java.io.Serializable;

/**
 * @desction: 配音文件下载记录
 * @date: 2023/1/29 11:19
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class DubbingFile implements Serializable {

    private String id;//主键(uid_voaId)

    private int voaId;//voaId
    private int uid;//用于id（用于判断是谁下载的，不同用户之前不能单独查看）
    private String audioPath;//音频路径
    private String videoPath;//视频路径
    private String downloadTime;//下载时间

    //格外保存voa数据用于展示
    private Voa voa;

    public DubbingFile(){

    }

    public DubbingFile(int voaId, int uid, String audioPath, String videoPath, String downloadTime, Voa voa) {
        this.id = transId(uid,voaId);

        this.voaId = voaId;
        this.uid = uid;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
        this.downloadTime = downloadTime;
        this.voa = voa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVoaId() {
        return voaId;
    }

    public void setVoaId(int voaId) {
        this.voaId = voaId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public Voa getVoa() {
        return voa;
    }

    public void setVoa(Voa voa) {
        this.voa = voa;
    }

    //获取id
    public static String transId(int uid,int voaId){
        return uid+"_"+voaId;
    }
}
