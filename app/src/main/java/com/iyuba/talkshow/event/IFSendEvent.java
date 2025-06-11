package com.iyuba.talkshow.event;

/**
 * Created by iyuba on 2018/1/11.
 */

public class IFSendEvent {
    public boolean  Send = false;
    public IFSendEvent(){
        Send = false;
    }
    public IFSendEvent(boolean  Send){
         Send = false;
    }

    public void setSend(boolean send) {
        Send = send;
    }
    public boolean getSend(){
        return Send;
    }
}
