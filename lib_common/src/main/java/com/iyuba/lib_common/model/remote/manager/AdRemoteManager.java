package com.iyuba.lib_common.model.remote.manager;

import com.iyuba.lib_common.manager.AppInfoManager;
import com.iyuba.lib_common.model.remote.RemoteHelper;
import com.iyuba.lib_common.model.remote.bean.Ad_click_result;
import com.iyuba.lib_common.model.remote.bean.Ad_clock_submit;
import com.iyuba.lib_common.model.remote.bean.Ad_result;
import com.iyuba.lib_common.model.remote.bean.Ad_reward_vip;
import com.iyuba.lib_common.model.remote.bean.Ad_stream_result;
import com.iyuba.lib_common.model.remote.service.AdService;
import com.iyuba.lib_common.model.remote.util.SignUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * @title: 广告接口管理
 * @date: 2024/1/4 10:27
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class AdRemoteManager {

    //接口-获取广告信息(开屏、插屏、banner)
    //开屏：0，banner：4
    public static Observable<List<Ad_result>> getAd(int uid,int adFlag,int appId){
        AdService commonService = RemoteHelper.getInstance().createJson(AdService.class);
        return commonService.getAd(appId,uid,adFlag);
    }

    //接口-获取信息流广告数据
    public static Observable<List<Ad_stream_result>> getTemplateAd(int userId, int flag, int appId){
        AdService adService = RemoteHelper.getInstance().createJson(AdService.class);
        return adService.getStreamAd(appId,userId,flag);
    }

    //接口-获取广告数据
    /*public static Observable<List<Ad_result>> getAd(int uid, int flag){
        int appId = AppInfoManager.getInstance().getAppId();

        AdService commonService = RemoteHelper.getInstance().createJson(AdService.class);
        return commonService.getAd(appId,uid,flag);
    }*/

    //接口-点击广告获取奖励
    public static Observable<Ad_click_result> getAdClickReward(int uid, int platform, int adSpace){
        int appId = AppInfoManager.getInstance().getAppId();
        long timestamp = System.currentTimeMillis()/1000L;
        String sign = SignUtil.getAdClickSign(uid,appId,timestamp);

        AdService commonService = RemoteHelper.getInstance().createJson(AdService.class);
        return commonService.getAdClick(uid,appId,platform,adSpace,timestamp,sign);
    }

    //接口-激励广告获取vip
    public static Observable<Ad_reward_vip> getAdRewardVip(int uid){
        int appId = AppInfoManager.getInstance().getAppId();
        long timestamp = System.currentTimeMillis()/1000L;
        String sign = SignUtil.getRewardAdVipSign(timestamp,uid,appId);

        AdService commonService = RemoteHelper.getInstance().createJson(AdService.class);
        return commonService.getAdRewardVip(uid,appId,timestamp,sign);
    }

    //接口-提交广告数据
    public static Observable<Ad_clock_submit> submitAdData(int userId,String device,String deviceId,String packageName,String ads){
        int appId = AppInfoManager.getInstance().getAppId();
        long timestamp = System.currentTimeMillis()/1000L;
        int os = 2;

        AdService commonService = RemoteHelper.getInstance().createJson(AdService.class);
        return commonService.submitAdData(String.valueOf(timestamp),appId,device,deviceId,userId,packageName,os,ads);
    }
}
