package com.iyuba.lib_common.model.remote.service;

import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.model.remote.bean.Login_account;
import com.iyuba.lib_common.model.remote.bean.Mob_verify;
import com.iyuba.lib_common.model.remote.bean.User_info;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @title: 用户接口
 * @date: 2023/12/28 18:00
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface UserInfoService {

    //获取用户信息
    //http://api.iyuba.com.cn/v2/api.iyuba?protocol=20001&myid=13865961&appid=260&format=json&sign=4603fde5e47c4c44628e30912817d8e5&id=13865961&platform=android
    @GET()
    Observable<User_info> getUserInfo(@Url String url,
                                      @Query(StrLibrary.protocol) int protocol,
                                      @Query(StrLibrary.appid) int appId,
                                      @Query(StrLibrary.myid) long uid,
                                      @Query(StrLibrary.id) long searchUserId,
                                      @Query(StrLibrary.format) String format,
                                      @Query(StrLibrary.sign) String sign,
                                      @Query(StrLibrary.platform) String platform);

    //账号登录
    //http://api.iyuba.com.cn/v2/api.iyuba?protocol=11001&password=d993e6acf1d43e02f4dc71818dbf9adc&appid=260&x=0.0&sign=b57be113fac7e1535e33c156308b240d&format=json&y=0.0&username=aiyuba_lil
    @GET()
    Observable<Login_account> loginByAccount(@Url String url,
                                             @Query(StrLibrary.protocol) int protocol,
                                             @Query(StrLibrary.appid) int appId,
                                             @Query(StrLibrary.x) String longitude,
                                             @Query(StrLibrary.y) String latitude,
                                             @Query(StrLibrary.format) String format,
                                             @Query(StrLibrary.username) String userName,
                                             @Query(StrLibrary.password) String password,
                                             @Query(StrLibrary.sign) String sign);

    //mob登录
    @GET
    Observable<Mob_verify> loginByMob(@Url String url,
                                      @Query(StrLibrary.protocol) int protocol,
                                      @Query(StrLibrary.token) String token,
                                      @Query(StrLibrary.opToken) String opToken,
                                      @Query(StrLibrary.operator) String operator,
                                      @Query(StrLibrary.appId) int appId,
                                      @Query(StrLibrary.appkey) String mobKey);
}
