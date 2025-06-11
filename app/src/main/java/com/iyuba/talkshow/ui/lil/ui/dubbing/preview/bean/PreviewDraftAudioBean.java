package com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean;

/**
 * 预览界面-草稿箱-音频数据
 */
public class PreviewDraftAudioBean {

    private String index;
    private String audioUrl;

    public PreviewDraftAudioBean(String index, String audioUrl) {
        this.index = index;
        this.audioUrl = audioUrl;
    }

    public String getIndex() {
        return index;
    }

    public String getAudioUrl() {
        return audioUrl;
    }
}
