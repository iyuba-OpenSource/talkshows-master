package com.iyuba.talkshow.data.remote;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.data.HttpUtil;
import com.iyuba.talkshow.data.model.SeriesResponse;
import com.iyuba.talkshow.util.MyGsonTypeAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface MovieService{
    String ENDPOINT = "http://cms."+Constant.Web.WEB_SUFFIX;


    @Headers({DOMAIN_NAME_HEADER + Constant.DOMAIN_CMS})
    @GET("dataapi/jsp/getTitle.jsp")
    Observable<SeriesResponse> getSeries(@Query("id") String id ,
                                         @Query("type") String type ,
                                         @Query("total") String total ,
                                         @Query("sign") String sign ,
                                         @Query("format") String format  );
//    private static Converter.Factory xmlConverterFactory = SimpleXmlConverterFactory.create();

    class Creator {

        public static MovieService newMovieService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieService.ENDPOINT)
                    .client(HttpUtil.getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            return retrofit.create(MovieService.class);
        }
    }
}
