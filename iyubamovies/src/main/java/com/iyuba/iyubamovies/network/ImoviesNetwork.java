package com.iyuba.iyubamovies.network;

import com.iyuba.iyubamovies.network.api.GetCommentListApi;
import com.iyuba.iyubamovies.network.api.GetImoviesByOneApi;
import com.iyuba.iyubamovies.network.api.GetImoviesDetailApi;
import com.iyuba.iyubamovies.network.api.GetImoviesListApi;
import com.iyuba.iyubamovies.network.api.GetPointsForIyubaApi;
import com.iyuba.iyubamovies.network.api.GetSendCommentApi;
import com.iyuba.iyubamovies.network.api.GetUploadStudyRecordApi;
import com.iyuba.module.commonvar.CommonVars;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by iyuba on 2017/12/23.
 */

public class ImoviesNetwork {

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static GetImoviesByOneApi getImoviesByOneApi;
    private static GetImoviesListApi getImoviesListApi;
    private static GetImoviesDetailApi detailApi;
    private static GetCommentListApi commentListApi;
    private static GetSendCommentApi sendCommentApi;
    private static GetPointsForIyubaApi pointsForIyubaApi;
    private static GetUploadStudyRecordApi uploadStudyRecordApi;
    private static Converter.Factory xmlConverterFactory = SimpleXmlConverterFactory.create();
    private ImoviesNetwork(){

    }
    public static void initOkHttpClient(){
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }
    public static void setOkHttpClient(OkHttpClient client){
        okHttpClient = client;
    }

    public static GetImoviesListApi getImoviesListApi(){
        if(okHttpClient==null)
            initOkHttpClient();
        if(getImoviesListApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://cms." + CommonVars.domain + "/dataapi/jsp/")
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            getImoviesListApi = retrofit.create(GetImoviesListApi.class);
        }
        return getImoviesListApi;

    }

    public static GetImoviesByOneApi getImoviesByOneApi(){
        if(okHttpClient==null)
            initOkHttpClient();
        if(getImoviesByOneApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://cms." + CommonVars.domain + "/dataapi/jsp/")
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            getImoviesByOneApi = retrofit.create(GetImoviesByOneApi.class);
        }
        return getImoviesByOneApi;
    }
    public static GetImoviesDetailApi getImvDetailApi(){
        if(okHttpClient==null)
            initOkHttpClient();
        if(detailApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://cms." + CommonVars.domain + "/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            detailApi = retrofit.create(GetImoviesDetailApi.class);
        }
        return detailApi;
    }
    public static GetCommentListApi getCommentListApi(){
        if(okHttpClient==null)
            initOkHttpClient();
        if(commentListApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://daxue." + CommonVars.domain + "/appApi/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            commentListApi = retrofit.create(GetCommentListApi.class);
        }
        return commentListApi;
    }
    public static GetSendCommentApi getSendCommentApi(){
        if(okHttpClient==null)
            initOkHttpClient();
        if(sendCommentApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://daxue." + CommonVars.domain + "/appApi/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sendCommentApi = retrofit.create(GetSendCommentApi.class);
        }
        return sendCommentApi;
    }

    public static GetPointsForIyubaApi getPointsForIyubaApi() {
        if(okHttpClient==null){
            initOkHttpClient();
        }
        if(pointsForIyubaApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://api." + CommonVars.domain + "/credits/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            pointsForIyubaApi = retrofit.create(GetPointsForIyubaApi.class);
        }
        return pointsForIyubaApi;
    }
    public static GetUploadStudyRecordApi getUploadStudyRecordApi(){
        if(okHttpClient==null){
            initOkHttpClient();
        }
        if(uploadStudyRecordApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://daxue." + CommonVars.domain + "/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            uploadStudyRecordApi = retrofit.create(GetUploadStudyRecordApi.class);
        }
        return uploadStudyRecordApi;
    }
}
