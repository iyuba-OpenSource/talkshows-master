package com.iyuba.talkshow.ui.detail;

/*
 * Copyright (C) 2016 Brian Wernick
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.util.videoView.BaseVideoControl;

/**
 * Provides playback controls for the EMVideoView on Mobile
 * (Phone, Tablet, etc.) devices.
        */
@SuppressWarnings("unused")
public class NormalVideoControl extends BaseVideoControl {

    public NormalVideoControl(Context context) {
        super(context);
    }

    public NormalVideoControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalVideoControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NormalVideoControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.detail_video_control;
    }

    public void showPlayPauseButton(boolean isShow) {
        if(isShow) {
            playPauseButton.setVisibility(View.VISIBLE);
        } else {
            playPauseButton.setVisibility(View.GONE);
        }
    }

    //增加投屏控制
    public void showTVButton(boolean isShow){
        if (isShow){
            toTvBtn.setVisibility(VISIBLE);
        }else {
            toTvBtn.setVisibility(GONE);
        }
    }

    //增加倍速调整
    public void showSpeedButton(boolean isShow){
        if (isShow){
            toSpeedBtn.setVisibility(VISIBLE);
        }else {
            toSpeedBtn.setVisibility(GONE);
        }
    }

    //设置seekbar进度操作
    public void setSeekbarProgress(long position){
        setPosition(position);
    }

    //开启或关闭底部
    public void openOrCloseBottomLayout(boolean isVisible){
        if (isVisible){
            bottomLayout.setVisibility(View.VISIBLE);
        }else {
            bottomLayout.setVisibility(View.GONE);
        }
    }
}
