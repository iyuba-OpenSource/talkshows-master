package com.iyuba.iyubamovies.network.api;

import com.iyuba.iyubamovies.network.result.ImoviesListResult;
import com.iyuba.module.commonvar.CommonVars;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iyuba on 2017/12/23.
 */

public interface GetImoviesListApi {
    String url = "http://cms."+ CommonVars.domain +"/dataapi/jsp/";
    String format = "json";

    @GET("getSeries.jsp")
    Call<ImoviesListResult> getImoviesListData(@Query("type")String type, @Query("sign")String sign,
                                               @Query("format")String format);
}
