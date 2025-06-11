package com.iyuba.iyubamovies.network.api;

import com.iyuba.iyubamovies.network.result.ImoviesByOneResult;
import com.iyuba.module.commonvar.CommonVars;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iyuba on 2017/12/23.
 */

public interface GetImoviesByOneApi {
    String url = "http://cms."+ CommonVars.domain +"/dataapi/jsp/";
    String format = "json";
    String type = "series";
    String total = "200";
    @GET("getTitle.jsp")
    Call<ImoviesByOneResult> getImoviesbyoneData(@Query("type")String type,
                                                 @Query("id")String id,
                                                 @Query("sign")String sign,
                                                 @Query("format")String format,
                                                 @Query("total")String total);
}
