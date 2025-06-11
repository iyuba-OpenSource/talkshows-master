package com.iyuba.wordtest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.util.HashMap;

public class MediaUtils {
    public static Bitmap getNetVideoBitmap(String videoUrl) throws IOException {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            return (cm != null) && (cm.getActiveNetworkInfo() != null)
                    && cm.getActiveNetworkInfo().isAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
