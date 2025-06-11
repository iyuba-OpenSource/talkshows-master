package com.iyuba.iyubamovies.network.api;

import com.iyuba.iyubamovies.network.result.ImoviesAgreeCountResult;
import com.iyuba.module.commonvar.CommonVars;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by iyuba on 2017/8/31.
 */

public interface GetSendCommentApi {
    String URL = "http://daxue."+ CommonVars.domain +"/appApi/";
    String PLATFORM = "android";
    String FORMAT = "json";
    String STAR = "0";
    String SHOW = "yes";
    String PROTOCAL = "60002";
    String APPNAME = "voa";
    String ZAN_PROTOCAL = "61001";
    @FormUrlEncoded
    @POST("UnicomApi")
    Observable<ResponseBody> writeComment(@Field("protocol") String protocol,
                                          @Field("platform") String platform,
                                          @Field("format") String format,
                                          @Field("shuoshuotype") String shuoshuotype,
                                          @Field("appName") String appName,
                                          @Field("star") String star,
                                          @Field("userid") String userId,
                                          @Field("voaid") String voaid,
                                          @Field(encoded = true, value = "content") String content,
                                          @Field("show") String show);
    @FormUrlEncoded
    @POST("UnicomApi")
    Observable<ResponseBody> writetoSBComment(@Field("protocol") String protocol,
                                              @Field("platform") String platform,
                                              @Field("format") String format,
                                              @Field("shuoshuotype") String shuoshuotype,
                                              @Field("appName") String appName,
                                              @Field("star") String star,
                                              @Field("userid") String userId,
                                              @Field("voaid") String voaid,
                                              @Field(encoded = true, value = "content") String content,
                                              @Field("backId") String backId,
                                              @Field("show") String show);

    @GET("UnicomApi")
    Call<ImoviesAgreeCountResult> clickAgreeCount(@Query("protocol") String protocol,
                                                  @Query("id") String id);
}
