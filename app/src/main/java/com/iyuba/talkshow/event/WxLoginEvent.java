package com.iyuba.talkshow.event;

/**
 * @desction:
 * @date: 2023/3/13 19:08
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class WxLoginEvent {

    private int errCode;

    public WxLoginEvent(int errCode) {
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
