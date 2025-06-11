package com.iyuba.iyubamovies.network.api;

import com.iyuba.iyubamovies.network.result.ImoviesUploadRecordResult;
import com.iyuba.module.commonvar.CommonVars;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 作者：renzhy on 17/3/10 14:56
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface GetUploadStudyRecordApi {
    String UPLOADURL = "http://daxue."+ CommonVars.domain;
    @FormUrlEncoded
    @POST("ecollege/updateStudyRecordNew.jsp")
    Observable<ImoviesUploadRecordResult> uploadStudyRecord(
            @Field("format") String format,
            @Field("appId") String appId,
            @Field("appName") String appName,
            @Field(encoded = true, value = "Lesson") String Lesson,
            @Field("LessonId") String LessonId,
            @Field("uid") String uid,
            @Field(encoded = true, value = "Device") String Device,
            @Field("DeviceId") String DeviceId,
            @Field(encoded = true, value = "BeginTime") String BeginTime,
            @Field(encoded = true, value = "EndTime") String EndTime,
            @Field("EndFlg") String EndFlg,
            @Field("TestWords") int TestWords,
            @Field("TestMode") String TestMode,
            @Field("platform") String platform,
            @Field("sign") String sign
    );
}
