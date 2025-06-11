package com.iyuba.talkshow.data.remote;


import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.data.HttpUtil;
import com.iyuba.talkshow.data.model.ExamWordResponse;
import com.iyuba.talkshow.data.model.RankingListBean;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;


public interface RankingService {
    /**
     * http://daxue."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"ecollege/getTopicRanking.jsp?shuoshuotype=4&topic=voa&topicid=0
     * &uid=5219278&type=D&start=0&total=15&sign=85713c696f57275cef2e93bd03a1ec38
     * 功能：指定周期内的用户做题排行榜（目前以做题总数排序）
     * 参数：
     * uid：用户id 可以为0
     * topic：voa,bbc,concept…;
     * type: "D" 日榜,"W" 周榜,"M" 月榜;
     * shuoshuotype: 默认值："0" 全部  "2" 句子排行 "3" 口语秀排行 "4": 合成播音排行;
     * topicid：文章ID;
     * start：开始的排名 从0开台
     * total：取得的件数
     * sign: md5("uid+topic+topicid+start+total+YYYY-MM-DD")
     * <p>
     * 返回参数：
     * result: 取不到数据为0， 取到数据是数据的件数
     * message: 返回的错误信息。
     * myid: 我的uid
     * myname:我的用户名
     * imgSrc:用户头像地址
     * myscores:我的总得分
     * mycount:我的句子数
     * myranking: 我的排名
     * data: json的列表内容如下
     * sort：在这批排行榜数据中的排序
     * uid: 排行榜的用户id
     * name:用户名
     * imgSrc:用户头像地址
     * counts:句子总数
     * scores:总分数
     * ranking: 排名
     */

    @Headers({DOMAIN_NAME_HEADER + Constant.DOMAIN_DAXUE})
    @GET("ecollege/getTopicRanking.jsp?shuoshuotype=3&topic=talkshow&topicid=0")
    Observable<RankingListBean> getRankinglList(
            @Query("uid") int uid,
//            @Query("topic") String topic,
            @Query("type") String type,
//            @Query("shuoshuotype") String type,
//            @Query("topicid") String appName,
            @Query("start") int start,
            @Query("total") int total,
            @Query("sign") String sign
            );


    class Creator {
        public static RankingService newRankingService() {
            String baseUrl = "http://daxue."+ Constant.Web.WEB_SUFFIX+"";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(HttpUtil.getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RankingService.class);
        }
    }

    @Headers({DOMAIN_NAME_HEADER + Constant.DOMAIN_DAXUE})
    @GET("ecollege/getExamDetailNew.jsp")
    Observable<ExamWordResponse> getExamWordDetail(@Query("uid") int uid,
                                                   @Query("appId") String appId,
                                                   @Query("lesson") String lesson,
                                                   @Query("TestMode") String TestMode,
                                                   @Query("mode") int mode,
                                                   @Query("sign") String sign);

}
