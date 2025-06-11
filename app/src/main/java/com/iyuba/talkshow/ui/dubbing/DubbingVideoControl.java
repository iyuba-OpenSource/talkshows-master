//package com.iyuba.talkshow.ui.dubbing;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.ui.detail.NormalVideoControl;
//
///**
// * Created by Administrator on 2017/1/17/017.
// */
//
//public class DubbingVideoControl extends NormalVideoControl {
//    public DubbingVideoControl(Context context) {
//        super(context);
//    }
//
//    public DubbingVideoControl(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public DubbingVideoControl(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.dubbing_video_control;
//    }
//
//    //增加倍速调整
//    public void showSpeedButton(boolean isShow){
//        if (isShow){
//            toSpeedBtn.setVisibility(VISIBLE);
//        }else {
//            toSpeedBtn.setVisibility(GONE);
//        }
//    }
//
//    //隐藏播放按钮和下方按钮
//    public void showPlayAndBottom(boolean isShow){
//        if (isShow){
//            playView.setVisibility(VISIBLE);
//            bottomLayout.setVisibility(VISIBLE);
//        }else {
//            playView.setVisibility(GONE);
//            bottomLayout.setVisibility(GONE);
//        }
//    }
//}
