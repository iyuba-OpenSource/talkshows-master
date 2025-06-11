package com.iyuba.lib_common.model.remote.manager;

import com.iyuba.lib_common.manager.AppInfoManager;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.remote.RemoteHelper;
import com.iyuba.lib_common.model.remote.bean.Login_account;
import com.iyuba.lib_common.model.remote.bean.Mob_verify;
import com.iyuba.lib_common.model.remote.bean.User_info;
import com.iyuba.lib_common.model.remote.service.UserInfoService;
import com.iyuba.lib_common.util.LibEncodeUtil;

import io.reactivex.Observable;

/**
 * @title: 用户信息-远程接口管理
 * @date: 2023/12/28 18:00
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class UserRemoteManager {

    //获取用户信息-20001
    public static Observable<User_info> getUserInfo(long userId){
        String url = "http://api."+ NetHostManager.getInstance().getDomainLong() +"/v2/api.iyuba";

        int protocol = 20001;
        int appId = AppInfoManager.getInstance().getAppId();
        String format = "json";
        String platform = "android";

        String sign = LibEncodeUtil.md5(protocol+""+userId+"iyubaV2");

        UserInfoService infoService = RemoteHelper.getInstance().createJson(UserInfoService.class);
        return infoService.getUserInfo(url,protocol,appId,userId,userId,format,sign,platform);
    }

    //账号登录-11001
    public static Observable<Login_account> loginByAccount(String userName, String password){
        //http://api.iyuba.com.cn/v2/api.iyuba
        String url = "http://api."+NetHostManager.getInstance().getDomainLong()+"/v2/api.iyuba";

        int protocol = 11001;
        String longitude = "";
        String latitude = "";
        int appId = AppInfoManager.getInstance().getAppId();
        String format = "json";
        String sign = LibEncodeUtil.md5(protocol+userName+ LibEncodeUtil.md5(password)+"iyubaV2");

        userName = LibEncodeUtil.encode(userName);
        password = LibEncodeUtil.md5(password);

        UserInfoService infoService = RemoteHelper.getInstance().createJson(UserInfoService.class);
        return infoService.loginByAccount(url,protocol,appId,longitude,latitude,format,userName,password,sign);
    }

    //秒验查询用户信息-10010
    public static Observable<Mob_verify> mobVerifyFromServer(String token, String opToken, String operator){
        String url = "http://api."+NetHostManager.getInstance().getDomainLong()+"/v2/api.iyuba";

        int protocol = 10010;
        int appId = AppInfoManager.getInstance().getAppId();
        String mobKey = AppInfoManager.getInstance().getMobKey();
        token = LibEncodeUtil.encode(token);

        UserInfoService infoService = RemoteHelper.getInstance().createJson(UserInfoService.class);
        return infoService.loginByMob(url,protocol,token,opToken,operator,appId,mobKey);
    }
}
