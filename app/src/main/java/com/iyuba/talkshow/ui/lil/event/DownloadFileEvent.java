package com.iyuba.talkshow.ui.lil.event;

/**
 * 文件下载事件
 */
public class DownloadFileEvent {

    public static final String STATUS_downloading = "downloading";
    public static final String STATUS_finish = "finish";
    public static final String STATUS_error = "error";

    private String downloadStatus;//下载状态
    private String showMsg;//显示信息

    public DownloadFileEvent(String downloadStatus, String showMsg) {
        this.downloadStatus = downloadStatus;
        this.showMsg = showMsg;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public String getShowMsg() {
        return showMsg;
    }
}
