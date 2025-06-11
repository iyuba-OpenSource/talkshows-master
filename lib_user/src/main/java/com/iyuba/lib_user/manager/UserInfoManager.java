package com.iyuba.lib_user.manager;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.lib_common.manager.AppInfoManager;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.remote.bean.Login_account;
import com.iyuba.lib_common.model.remote.bean.User_info;
import com.iyuba.lib_common.model.remote.manager.UserRemoteManager;
import com.iyuba.lib_common.util.LibBigDecimalUtil;
import com.iyuba.lib_common.util.LibGsonUtil;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_common.util.LibSPUtil;
import com.iyuba.lib_user.bean.LocalUserBean;
import com.iyuba.lib_user.event.UserRefreshEvent;
import com.iyuba.lib_user.listener.UserinfoCallbackListener;
import com.iyuba.lib_user.util.UserInfoErrorMsgUtil;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @title: 用户信息管理
 * @date: 2023/11/3 09:01
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class UserInfoManager {
    private static final String TAG = "UserInfoManager";
    private static UserInfoManager instance;

    public static UserInfoManager getInstance(){
        if (instance==null){
            synchronized (UserInfoManager.class){
                if (instance==null){
                    instance = new UserInfoManager();
                }
            }
        }
        return instance;
    }

    //获取缓存信息
    private SharedPreferences preferences;

    private SharedPreferences getPreferences(){
        if (preferences==null){
            preferences = LibSPUtil.getPreferences(LibResUtil.getInstance().getContext(), TAG);
        }
        return preferences;
    }

    /***************************缓存信息*******************************/
    //用户信息的key(20001接口的)
    private String sp_userInfo = "userInfo";
    //用户登录的账号
    private String sp_account = "account";
    //用户登录的密码
    private String sp_password = "password";
    //是否使用过了之前的userId数据
    private String sp_used = "sp_used";

    //保存用户信息
    private void saveUserInfo(int userId, User_info info){
        //转换成字符串类型
        String infoData = LibGsonUtil.toJson(info);
        //将数据转换成相应的类型
        LocalUserBean userBean = new LocalUserBean(
                userId,
                info.getUsername(),
                info.getNickname(),
                info.getMiddle_url(),
                info.getBio(),
                info.getVipStatus(),
                info.getExpireTime(),
                TextUtils.isEmpty(info.getCredits())?0:Integer.parseInt(info.getCredits()),
                LibBigDecimalUtil.trans2Double(info.getMoney()/100.0f),
                info.getAmount(),
                infoData
                );
        //转换成字符
        String localUserInfoStr = LibGsonUtil.toJson(userBean);
        //将数据保存
        getPreferences().edit().putString(sp_userInfo,localUserInfoStr).apply();
    }

    //删除用户信息
    private void deleteUserinfo(){
        getPreferences().edit().putString(sp_userInfo,null).apply();

        //将原来的数据处理掉
        SharedPreferences tempPreference = LibSPUtil.getPreferences(LibResUtil.getInstance().getContext(), "config");
        tempPreference.edit().clear().apply();
    }

    //获取用户信息
    private LocalUserBean getUserinfo(){
        //从缓存中获取数据
        String userinfoStr = getPreferences().getString(sp_userInfo,null);
        if (TextUtils.isEmpty(userinfoStr)){
            return new LocalUserBean();
        }
        //转换
        try {
            return new Gson().fromJson(userinfoStr,LocalUserBean.class);
        }catch (Exception e){
            return new LocalUserBean();
        }
    }

    /**************************实际操作信息***************************/
    //获取用户id
    public int getUserId(){
        return getUserinfo().getUserId();
    }

    //获取用户名称
    public String getUserName(){
        return getUserinfo().getUserName();
    }

    //获取用户昵称
    public String getNickName(){
        return getUserinfo().getNickName();
    }

    //获取用户头像
    public String getUserPic(){
        if (getUserId() == 0){
            return "";
        }
        return "http://api."+ NetHostManager.getInstance().getDomainLong() +"/v2/api.iyuba?protocol=10005&uid=" + getUserId() + "&size=big";
    }

    //获取用户爱语币
    public int getIyuIcon(){
        return getUserinfo().getIyuIcon();
    }

    //获取用户积分
    public int getJiFen(){
        return getUserinfo().getJiFen();
    }

    //获取用户奖励
    public double getMoney(){
        return getUserinfo().getMoney();
    }

    //获取会员状态
    public String getVipStatus(){
        return getUserinfo().getVipStatus();
    }

    //获取用户会员时间(直接转换使用即可)
    public long getVipTime(){
        return getUserinfo().getVipTime()*1000L;
    }

    //获取其他数据显示
    public User_info getOtherData(){
        //从缓存取出
        String allData = getUserinfo().getUserInfo();
        try {
            return new Gson().fromJson(allData,User_info.class);
        }catch (Exception e){
            return new User_info();
        }
    }

    //判断用户登录
    public boolean isLogin(){
        if (getUserId()==0){
            return false;
        }
        return true;
    }

    //判断用户会员状态
    public boolean isVip(){
        if (!isLogin()
                ||getVipTime()==0
                ||TextUtils.isEmpty(getVipStatus())
                ||getVipStatus().equals("0")
                ||getVipTime()<System.currentTimeMillis()){
            return false;
        }

        return true;
    }

    //刷新用户信息
    private void refreshUserInfo(int userId,User_info info){
        //保存在本地
        saveUserInfo(userId, info);

        //设置到个人中心
        User user = new User();
        user.uid = userId;
        user.name = info.getUsername();
        user.nickname = info.getNickname();
        user.imgUrl = info.getMiddle_url();
        user.vipStatus = info.getVipStatus();
        user.vipExpireTime = info.getExpireTime();
        user.iyubiAmount = info.getAmount();
        IyuUserManager.getInstance().setCurrentUser(user);

        //设置视频的一些信息
        IHeadline.resetMseUrl();
        String extraMergeUrl = "http://" + NetHostManager.getInstance().getDomainShort() + ":9001/test/merge/";
        IHeadline.setExtraMergeAudioUrl(extraMergeUrl);
        String extraUrl = "http://iuserspeech." + NetHostManager.getInstance().getDomainShort() + ":9001/test/ai/";
        IHeadline.setExtraMseUrl(extraUrl);
        IHeadlineManager.appId = String.valueOf(AppInfoManager.getInstance().getAppId());
        IHeadlineManager.appName = AppInfoManager.getInstance().getAppType();

        EventBus.getDefault().post(new UserRefreshEvent());
    }

    //删除用户信息
    public void clearUserInfo(){
        //删除本地
        deleteUserinfo();

        //删除个人中心
        IyuUserManager.getInstance().logout();
    }

    //初始化用户信息
    public void initUserInfo(){
        //设置视频的一些信息
        IHeadline.resetMseUrl();
        String extraMergeUrl = "http://" + NetHostManager.getInstance().getDomainShort() + ":9001/test/merge/";
        IHeadline.setExtraMergeAudioUrl(extraMergeUrl);
        String extraUrl = "http://iuserspeech." + NetHostManager.getInstance().getDomainShort() + ":9001/test/ai/";
        IHeadline.setExtraMseUrl(extraUrl);
        IHeadlineManager.appId = String.valueOf(AppInfoManager.getInstance().getAppId());
        IHeadlineManager.appName = AppInfoManager.getInstance().getAppType();

        if (!UserInfoManager.getInstance().isLogin()){
            clearUserInfo();
            return;
        }

        //设置到个人中心
        User user = new User();
        user.uid = getUserId();
        user.name = getUserName();
        user.nickname = getNickName();
        user.imgUrl = getUserPic();
        user.vipStatus = getVipStatus();
        user.vipExpireTime = getVipTime();
        //这里增加下会员判断，如果当前时间>会员时间，则说明会员过期
        if (user.vipExpireTime < System.currentTimeMillis()){
            user.vipStatus = "0";
            user.vipExpireTime = 0;
        }
        user.iyubiAmount = getIyuIcon();
        IyuUserManager.getInstance().setCurrentUser(user);
    }

    //保存用户的账号和密码(仅用于账号登录、注册等)
    public void saveAccountAndPwd(String userName,String password){
        getPreferences().edit().putString(sp_account,userName).apply();
        getPreferences().edit().putString(sp_password,password).apply();
    }

    //获取用户的账号和密码(仅用于账号登录、注册等)
    public String getLoginAccount(){
        String account = getPreferences().getString(sp_account,"");
        return account;
    }

    public String getLoginPassword(){
        String password = getPreferences().getString(sp_password,"");
        return password;
    }

    //获取之前的userId数据的使用状态
    private boolean isUsed(){
        return getPreferences().getBoolean(sp_used,false);
    }

    private void setUsed(boolean isUsed){
        getPreferences().edit().putBoolean(sp_used,isUsed).apply();
    }

    /******************************用户信息*****************************/
    //用户信息
    private Disposable userInfoDis;
    //获取用户信息-20001
    public void getRemoteUserInfo(int userId, UserinfoCallbackListener listener){
        destroyUserInfo();
        UserRemoteManager.getUserInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User_info>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        userInfoDis = d;
                    }

                    @Override
                    public void onNext(User_info info) {
                        if (info!=null){
                            //保存在本地缓存中
                            refreshUserInfo(userId,info);
                            //发送刷新
                            if (listener!=null){
                                listener.onSuccess();
                            }
                        }else {
                            if (listener!=null){
                                listener.onFail("用户信息获取失败("+info.getResult()+")");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener!=null){
                            listener.onFail("用户信息获取异常");
                        }
                    }

                    @Override
                    public void onComplete() {
                        destroyUserInfo();
                    }
                });
    }

    //取消用户信息
    public void destroyUserInfo(){
        LibRxUtil.unDisposable(userInfoDis);
    }

    /*******************************账号登录******************************/
    //账号登录
    private Disposable accountLoginDis;
    //账号登录接口
    public void postRemoteAccountLogin(String account, String password, UserinfoCallbackListener listener){
        destroyAccountLogin();
        UserRemoteManager.loginByAccount(account, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login_account>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        accountLoginDis = d;
                    }

                    @Override
                    public void onNext(Login_account bean) {
                        if (bean!=null&&bean.getUid()!=0){
                            //保存在本地
                            saveAccountAndPwd(account,password);
                            //获取20001的数据信息
                            getRemoteUserInfo(bean.getUid(),listener);
                        }else {
                            if (listener!=null){
                                listener.onFail(UserInfoErrorMsgUtil.showLoginMsg(bean.getResult()));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener!=null){
                            listener.onFail("账号登录异常，请重试");
                        }
                    }

                    @Override
                    public void onComplete() {
                        destroyAccountLogin();
                    }
                });
    }
    //取消账号登录
    public void destroyAccountLogin(){
        LibRxUtil.unDisposable(accountLoginDis);
    }
}
