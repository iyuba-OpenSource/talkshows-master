package com.iyuba.talkshow.data.remote;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.data.HttpUtil;
import com.iyuba.talkshow.data.model.SeriesResponse;
import com.iyuba.talkshow.data.model.TitleSeriesResponse;
import com.iyuba.talkshow.util.MyGsonTypeAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface CmsService {
    String ENDPOINT = "http://cms."+Constant.Web.WEB_SUFFIX;

    @Headers({DOMAIN_NAME_HEADER + Constant.DOMAIN_CMS})
    @GET("dataapi/jsp/getSeries.jsp")
    Observable<SeriesResponse> getDramas(@Query("type") String type,
                                         @Query("appid") int appid, @Query("uid") int uid,
                                         @Query("sign") String sign,
                                         @Query("format") String format);


    @Headers({DOMAIN_NAME_HEADER + Constant.DOMAIN_CMS})
    @GET("dataapi/jsp/getTitleBySeries.jsp")
    Observable<TitleSeriesResponse> getTitleSeries(@Query("type") String type,
                                                   @Query("seriesid") int series,
                                                   @Query("sign") String sign,
                                                   @Query("format") String format);

    class Creator {
        public static CmsService newCmsService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(HttpUtil.getOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(CmsService.ENDPOINT)
                    .build();
            return retrofit.create(CmsService.class);
        }
    }
}
