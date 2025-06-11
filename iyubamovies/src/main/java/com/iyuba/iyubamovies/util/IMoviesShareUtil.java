package com.iyuba.iyubamovies.util;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by iyuba on 2018/1/25.
 */

public class IMoviesShareUtil {
    public static interface OnShareStateListener{
        void onComplete(Platform platform, int i, HashMap<String, Object> hashMap);
        void onError(Platform platform, int i, Throwable throwable);
        void onCancel(Platform platform, int i);
    }
    private static OnShareStateListener onShareStateListener;

    public static void setOnShareStateListener(OnShareStateListener onShareStateListener) {
        IMoviesShareUtil.onShareStateListener = onShareStateListener;
    }

    public static void showShare(Context context, String imgUrl,String title,String title_en,String shareurl) {
//,String title_cn
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title_en+title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(shareurl);
        // text是分享文本，所有平台都需要这个字段
        String show = "我正在看:"
                + title+ " "
                + "[ "+title_en
                + "]";
        oks.setText(show);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(imgUrl);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareurl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("很不错的美剧！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(shareurl);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareurl);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(onShareStateListener!=null){
                    onShareStateListener.onComplete(platform,i,hashMap);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("okCallbackonError", "onError");
                if(onShareStateListener!=null){
                    onShareStateListener.onError(platform,i,throwable);
                }
                Log.e("--分享失败===", throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if(onShareStateListener!=null){
                    onShareStateListener.onCancel(platform,i);
                }
            }
        });
        // 启动分享GUI
        oks.show(context);
    }
}
