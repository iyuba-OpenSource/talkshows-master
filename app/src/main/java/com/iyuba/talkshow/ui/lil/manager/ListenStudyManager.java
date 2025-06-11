package com.iyuba.talkshow.ui.lil.manager;

import com.iyuba.talkshow.data.model.VoaText;

import java.util.List;

/**
 * @title: 听力学习报告管理类
 * @date: 2023/10/23 13:56
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class ListenStudyManager {
    private static ListenStudyManager instance;

    public static ListenStudyManager getInstance(){
        if (instance==null){
            synchronized (ListenStudyManager.class){
                if (instance==null){
                    instance = new ListenStudyManager();
                }
            }
        }
        return instance;
    }

    //开始时间
    private long startTime;
    //结束时间
    private long endTime;
    //文本内容
    private List<VoaText> voaTextList;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<VoaText> getVoaTextList() {
        return voaTextList;
    }

    public void setVoaTextList(List<VoaText> voaTextList) {
        this.voaTextList = voaTextList;
    }
}
