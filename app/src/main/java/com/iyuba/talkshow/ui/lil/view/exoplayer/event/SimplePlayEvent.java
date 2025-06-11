package com.iyuba.talkshow.ui.lil.view.exoplayer.event;

public class SimplePlayEvent {

    //类型
    public static final String type_back = "back";//返回
    public static final String type_play = "play";//播放
    public static final String type_pause = "pause";//暂停

    private String type;

    public SimplePlayEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
