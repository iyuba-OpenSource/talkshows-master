package com.iyuba.talkshow.data.remote;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.talkshow.data.HttpUtil;
import com.iyuba.talkshow.data.model.NewSeriesDataResponse;
import com.iyuba.talkshow.data.model.TitleSeriesResponse;
import com.iyuba.talkshow.ui.lil.bean.Study_listen_report;
import com.iyuba.talkshow.ui.lil.bean.Reward_history;
import com.iyuba.talkshow.util.MyGsonTypeAdapterFactory;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface FixService {

    String ENDPOINT = "http://apps."+Constant.Web.WEB_SUFFIX;

    //根据出版社获取系列课本
    @Headers({DOMAIN_NAME_HEADER + Constant.DOMAIN_CMS})
    @GET("iyuba/getTitleBySeries.jsp")
    Observable<TitleSeriesResponse> getTitleSeries(@Query("type") String type,
                                                   @Query("seriesid") int series,
                                                   @Query("sign") String sign,
                                                   @Query("format") String format);

    //这里增加一个接口，获取系列数据的
    @GET("iyuba/getTitleBySeries.jsp")
    Observable<NewSeriesDataResponse> getTitleSeries2(@Query("type") String type,
                                                      @Query("category") String series,
                                                      @Query("sign") String sign);

    //增加钱包的历史记录
    //http://api.iyuba.cn/credits/getuseractionrecord.jsp?uid=6307010&pages=1&pageCount=20&sign=0fd32b5d167482f0cc3561b2abc70738
    @GET
    Observable<BaseBean_data<List<Reward_history>>> getWalletHistory(@Url String url,
                                                                     @Query("uid") int uid,
                                                                     @Query("pages") int pages,
                                                                     @Query("pageCount") int pageCount,
                                                                     @Query("sign") String sign);

    //听力学习报告
    //http://daxue.iyuba.cn/ecollege/updateStudyRecordNew.jsp?EndTime=2023-10-23%2014%3A08%3A14&UserAnswer=25&DeviceId=b32668f9-d351-41a0-9f1a-52f92c028c4d&LessonId=15909&rewardVersion=1&sign=8971ebb5e78698192e9e4506a21dbf2b&BeginTime=2023-10-23%2014%3A08%3A07&TestMode=25&TestNumber=25&Lesson=csvoa&uid=12071118&Score=0&appId=262&EndFlg=1&TestWords=383
    @GET
    Observable<Study_listen_report> submitListenStudyReport(@Url String url,
                                                            @Query("format") String format,
                                                            @Query("platform") String platform,
                                                            @Query("appName") String appName,
                                                            @Query("Lesson") String appNameEncode,
                                                            @Query("appId") int appId,
                                                            @Query("BeginTime") String BeginTimeEncode,
                                                            @Query("EndTime") String EndTimeEncode,
                                                            @Query("EndFlg") int EndFlg,
                                                            @Query("LessonId") String lessonId,
                                                            @Query("TestNumber") String TestNumber,
                                                            @Query("TestWords") String TestWords,
                                                            @Query("TestMode") String TestMode,
                                                            @Query("UserAnswer") String UserAnswer,
                                                            @Query("Score") String score,
                                                            @Query("DeviceId") String deviceId,
                                                            @Query("uid") int uid,
                                                            @Query("sign") String sign,
                                                            @Query("rewardVersion") int rewardVersion);

    class Creator {
        public static FixService newCmsService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(HttpUtil.getOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(FixService.ENDPOINT)
                    .build();
            return retrofit.create(FixService.class);
        }
    }
}
