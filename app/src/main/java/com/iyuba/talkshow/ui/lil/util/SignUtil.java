package com.iyuba.talkshow.ui.lil.util;

/**
 * @title:
 * @date: 2023/10/23 11:41
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class SignUtil {

    //奖励的历史记录
    public static String getRewardHistorySign(int uid){
        String data = uid+"iyuba"+DateUtil.toDateStr(System.currentTimeMillis(),DateUtil.YMD);
        return EncodeUtil.md5(data);
    }

    //听力学习报告
    public static String getListenStudyReportSign(int uid,String startTime){
        String data = uid+startTime+DateUtil.toDateStr(System.currentTimeMillis(),DateUtil.YMD);
        return EncodeUtil.md5(data);
    }
}
