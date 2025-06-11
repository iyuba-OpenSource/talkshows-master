package com.iyuba.talkshow.data.remote;

import com.iyuba.talkshow.data.HttpUtil;
import com.iyuba.talkshow.data.model.TemporaryUserJson;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iyuba on 2018/1/8.
 */

public interface TemporaryAccountApi {
    public static String URL = "http://api.iyuba.com.cn/";
    public static int PROTOCOL = 11003;
    public static int PROTOCOL2 = 20001;
    public static String PLATFORM = "Android";
    public static int APPID = 249;
    public static String FORMAT = "json";
    @GET("v2/api.iyuba")
    public Call<TemporaryUserJson> getTemporaryAccount(@Query("protocol") int protocol,
                                                       @Query("deviceId") String deviceId,
                                                       @Query("platform") String platform,
                                                       @Query("appid") int appid,
                                                       @Query("format")String format,
                                                       @Query("sign") String sign);

    //sgin:MD5.getMD5ofStr(newPhoneDiviceId()+ TemporaryAccountApi.APPID+TemporaryAccountApi.PLATFORM+
    //getTimeDate()+"iyubaV2");

    class Creator {
        public static TemporaryAccountApi newTemporaryAccountApi() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(HttpUtil.getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(TemporaryAccountApi.class);
        }
    }
}
