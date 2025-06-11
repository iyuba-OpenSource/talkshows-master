package com.iyuba.lib_common.model.remote.service;

import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.data.UrlLibrary;
import com.iyuba.lib_common.model.remote.bean.App_check;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @title:  审核的服务
 * @date: 2024/1/5 16:52
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface VerifyService {

    //审核接口处理(微课、视频、人教版等)
    //http://api.qomolama.cn/getRegisterAll.jsp
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_API, StrLibrary.urlHost + ":" + UrlLibrary.QOMLAMA_URL})
    @GET(UrlLibrary.verify_ability)
    Observable<App_check> verify(@Query(StrLibrary.appId) int appId,
                                 @Query(StrLibrary.appVersion) String version);
}
