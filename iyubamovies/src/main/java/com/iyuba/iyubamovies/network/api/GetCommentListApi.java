package com.iyuba.iyubamovies.network.api;

import com.iyuba.module.commonvar.CommonVars;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by iyuba on 2017/8/29.
 */

public interface GetCommentListApi {
    String PROTOCOL = "60001";
    String PLATFORM = "android";
    String FORMAT = "json";
    String WRITE_COMMENT_PROTOCOL = "60002";
    String URL = "http://daxue."+ CommonVars.domain +"/appApi/";
    String APPNAME = "voa";
    @GET("UnicomApi")
    Observable<ResponseBody> getCommentList(@Query("protocol") String protocol, @Query("platform")
            String platform, @Query("format") String format, @Query("appName") String appName, @Query("voaid") String voaid, @Query("pageNumber")
                                                    String pageNumber, @Query("pageCounts") String pageCounts,
                                            @Query("appid") String appid);




}