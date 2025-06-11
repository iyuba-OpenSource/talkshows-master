package com.iyuba.talkshow.event;

/**
 * Created by Administrator on 2017/1/9/009.
 */

public class InputMethodEvent {
    public int status;

    public InputMethodEvent() {
    }

    public InputMethodEvent(int status) {
        this.status = status;
    }

    public interface STATUS {
        int HIDE = 0;
        int SHOW = 1;
    }
}
