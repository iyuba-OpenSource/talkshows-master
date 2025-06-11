package com.iyuba.iyubamovies.network.api;

import com.iyuba.iyubamovies.network.result.PointsForIyubaResult;
import com.iyuba.module.commonvar.CommonVars;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iyuba on 2018/1/25.
 */

public interface GetPointsForIyubaApi {
    String SRID = "40";
    String MOBILE = "1";
    String NEWJIFEN_URL = "http://api."+ CommonVars.domain +"/credits/" ;
    @GET("updateScore.jsp")
    Call<PointsForIyubaResult> PointsForIyuba(@Query("srid")String srid,
                                              @Query("mobile")String mobile,
                                              @Query("flag")String flag,
                                              @Query("uid")String uid,
                                              @Query("appid")String appid,
                                              @Query("idindex")String idindex);
}
