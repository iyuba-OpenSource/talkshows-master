package com.iyuba.lib_common.manager;

/**
 * @title: app的一些信息
 * @date: 2023/12/29 10:03
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description: appId、app名称等
 */
public class AppInfoManager {
    private static AppInfoManager instance;

    public static AppInfoManager getInstance(){
        if (instance==null){
            synchronized (AppInfoManager.class){
                if (instance==null){
                    instance = new AppInfoManager();
                }
            }
        }
        return instance;
    }

    //app的id
    private int appId;
    //app的类型
    private String appType;
    //app的图标
    private int appIcon;
    //app的名称
    private String appName;

    //app的隐私政策
    private String privacyPolicy;
    //app的用户协议
    private String userAgreement;
    //儿童隐私政策
    private String childPrivacyPolicy;

    //mob的key
    private String mobKey;
    //mob的secret
    private String mobSecret;

    public void initAppInfo(int appId,String appType,int appIcon,String appName,String privacyPolicy,String userAgreement,String childPrivacyPolicy,String mobKey,String mobSecret){
        this.appId = appId;
        this.appType = appType;

        this.appIcon = appIcon;
        this.appName = appName;

        this.privacyPolicy = privacyPolicy;
        this.userAgreement = userAgreement;
        this.childPrivacyPolicy = childPrivacyPolicy;

        this.mobKey = mobKey;
        this.mobSecret = mobSecret;
    }

    public int getAppId() {
        return appId;
    }

    public String getAppType() {
        return appType;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public String getUserAgreement() {
        return userAgreement;
    }

    public String getChildPrivacyPolicy() {
        return childPrivacyPolicy;
    }

    public String getMobKey() {
        return mobKey;
    }

    public String getMobSecret() {
        return mobSecret;
    }
}
