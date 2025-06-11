package com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean;

/**
 * 预览界面-草稿箱-成绩数据
 */
public class PreviewDraftScoreBean {

    private String index;
    private int score;

    public PreviewDraftScoreBean(String index, int score) {
        this.index = index;
        this.score = score;
    }

    public String getIndex() {
        return index;
    }

    public int getScore() {
        return score;
    }
}
