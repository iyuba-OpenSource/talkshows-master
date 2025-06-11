package com.iyuba.lib_common.model.remote.service;

import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.data.UrlLibrary;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_marge;
import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_release;
import com.iyuba.lib_common.model.remote.bean.Eval_result;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 配音功能的服务
 */
public interface DubbingService {

    //下载文件
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    //评测配音
    //http://iuserspeech.iyuba.cn:9001/test/ai/
    //sentence	text/plain; charset=utf-8		It is raining today.
    //flg	text/plain; charset=utf-8		0
    //paraId	text/plain; charset=utf-8		2
    //newsId	text/plain; charset=utf-8		309680
    //IdIndex	text/plain; charset=utf-8		1
    //wordId	text/plain; charset=utf-8		0
    //appId	text/plain; charset=utf-8		280
    //type	text/plain; charset=utf-8		talkshow
    //userId	text/plain; charset=utf-8		12071118
    //file	multipart/form-data	2.aac	5.40 KB (5,528 bytes)
    @Headers({StrLibrary.urlPrefix+":"+ UrlLibrary.HTTP_IUSERSPEECH,StrLibrary.urlHost+":"+ NetHostManager.domain_short,StrLibrary.urlSuffix+":"+UrlLibrary.SUFFIX_9001})
    @POST("/test/ai/")
    Observable<BaseBean_data<Eval_result>> submitEval(@Body RequestBody body);

    //提交评测
    //http://voa.iyuba.cn/voa/UnicomApi2?&protocol=60002&userid=15217918&content=3
    @Headers({StrLibrary.urlPrefix+":"+ UrlLibrary.HTTP_VOA,StrLibrary.urlHost+":"+ NetHostManager.domain_short})
    @POST("/voa/UnicomApi2")
    Observable<Dubbing_publish_marge> publishMarge(@Query(StrLibrary.protocol) int protocol,
                                                   @Query(StrLibrary.userid) long uerId,
                                                   @Query(StrLibrary.content) int content,
                                                   @Body RequestBody body);

    //获取正式发布的配音数据
    //http://voa.iyuba.cn/voa/getTalkShowOtherWorks.jsp?uid=15300794&appid=280&appname=talkshow
    @Headers({StrLibrary.urlPrefix+":"+ UrlLibrary.HTTP_VOA,StrLibrary.urlHost+":"+ NetHostManager.domain_short})
    @GET("/voa/getTalkShowOtherWorks.jsp")
    Observable<Dubbing_publish_release> getReleasePublishData(@Query(StrLibrary.uid) long userId,
                                                              @Query(StrLibrary.appid) int appId,
                                                              @Query(StrLibrary.appname) String appName);
}
