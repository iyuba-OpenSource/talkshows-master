package com.iyuba.iyubamovies.network.api;

import com.iyuba.iyubamovies.network.result.ImoviesDetailResult;
import com.iyuba.module.commonvar.CommonVars;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者：renzhy on 17/2/21 15:16
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface GetImoviesDetailApi {
    String URL = "http://cms."+ CommonVars.domain;
    @GET("dataapi/jsp/getText.jsp")
    Observable<ImoviesDetailResult> getImoviesDetail(@Query("uid") String uid,
                                                       @Query("appid") String appid,
                                                       @Query("type") String type,
                                                       @Query("id") String id,
                                                       @Query("format") String format,
                                                       @Query("sign") String sign);
}
