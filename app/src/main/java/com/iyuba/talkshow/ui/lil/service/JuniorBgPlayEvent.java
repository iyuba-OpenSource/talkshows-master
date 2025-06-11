package com.iyuba.talkshow.ui.lil.service;

/**
 * @title: 中小学-后台播放事件
 * @date: 2023/10/27 13:35
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class JuniorBgPlayEvent {

    /******************类型******************/
    public static final String event_audio_prepareFinish = "junior_audio_prepareFinish";//音频-加载完成
    public static final String event_audio_completeFinish = "junior_audio_completeFinish";//音频-播放完成
    public static final String event_audio_play = "junior_audio_play";//音频-播放
    public static final String event_audio_pause = "junior_audio_pause";//音频-暂停
    public static final String event_audio_stop = "junior_audio_stop";//音频-停止
    public static final String event_audio_switch = "junior_switch";//音频-切换
    public static final String event_control_play = "junior_control_play";//控制栏-播放
    public static final String event_control_pause = "junior_control_pause";//控制栏-暂停
    public static final String event_control_hide = "junior_control_hide";//控制栏-隐藏
    public static final String event_data_refresh = "junior_data_refresh";//数据-刷新

    /*******************事件*****************/
    private String showType;
    private int dataIndex;

    public JuniorBgPlayEvent(String showType) {
        this.showType = showType;
    }

    public JuniorBgPlayEvent(String showType, int dataIndex) {
        this.showType = showType;
        this.dataIndex = dataIndex;
    }

    public String getShowType() {
        return showType;
    }

    public int getDataIndex() {
        return dataIndex;
    }
}
