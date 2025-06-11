package com.iyuba.talkshow.ui.lil.bean;

/**
 * @title: 学习报告
 * @date: 2023/10/23 14:09
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class Study_listen_report {

    /**
     * result : 1
     * jifen : 10
     * message : success!!
     * reward : 10
     * rewardMessage : 该内容奖励领取成功, 继续学习解锁更多奖励吧！
     */

    private String result;
    private String jifen;
    private String message;
    private String reward;
    private String rewardMessage;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRewardMessage() {
        return rewardMessage;
    }

    public void setRewardMessage(String rewardMessage) {
        this.rewardMessage = rewardMessage;
    }
}
