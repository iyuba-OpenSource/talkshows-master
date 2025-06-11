package com.iyuba.wordtest.utils;

import android.content.Context;

import com.iyuba.wordtest.R;


import java.io.File;
import java.text.MessageFormat;

import cn.aigestudio.downloader.interfaces.IDListener;

public class DLManager {

    private  cn.aigestudio.downloader.bizs.DLManager mDLManager;

    private static String mVideoUrl;
    private String mDir;
    private Context context ;
    private String mMsg;
    private int voaId ;


    private IDListener mVideoListener = new IDListener() {
        private int mFileLength = 0;

        @Override
        public void onPrepare() {

        }

        @Override
        public void onStart(String fileName, String realUrl, int fileLength) {
            this.mFileLength = fileLength;
        }

        @Override
        public void onProgress(int progress) {
            if (mFileLength != 0) {
                int percent = progress * 100 / mFileLength;
                if (context != null) {
                    mMsg = MessageFormat.format(
                            context.getString(R.string.video_loading_tip), percent);
                }
            }
        }

        @Override
        public void onStop(int progress) {

        }

        @Override
        public void onFinish(File file) {
            StorageUtil.renameVideoFile(mDir, voaId);
        }

        @Override
        public void onError(int status, String error) {

        }
    };
}
