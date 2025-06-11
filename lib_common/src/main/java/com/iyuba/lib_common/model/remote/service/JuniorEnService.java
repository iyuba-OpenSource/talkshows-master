package com.iyuba.lib_common.model.remote.service;

import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.data.UrlLibrary;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.remote.bean.Collect_chapter;
import com.iyuba.lib_common.model.remote.bean.Eval_result;
import com.iyuba.lib_common.model.remote.bean.Junior_book;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter_collect;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter_detail;
import com.iyuba.lib_common.model.remote.bean.Junior_type;
import com.iyuba.lib_common.model.remote.bean.Junior_word;
import com.iyuba.lib_common.model.remote.bean.Marge_eval;
import com.iyuba.lib_common.model.remote.bean.Publish_eval;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data_junior;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data_primary;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_textDetail;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @title: 青少年英语服务
 * @date: 2023/12/28 16:59
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description: 小学英语和初中英语内容
 */
public interface JuniorEnService {

    /************************************************出版社***********************************************/
    //小学-出版社数据
    //http://apps.iyuba.cn/iyuba/chooseLessonNew.jsp?&appid=260&uid=0&type=primary&version=3
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_APPS, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @GET(UrlLibrary.junior_type)
    Observable<BaseBean_data<BaseBean_data_primary<List<Junior_type>>>> getPrimaryTypeData(@Query(StrLibrary.appid) int appId,
                                                                                           @Query(StrLibrary.uid) String uid,
                                                                                           @Query(StrLibrary.type) String type,
                                                                                           @Query(StrLibrary.version) int version);

    //初中-出版社数据
    //http://apps.iyuba.cn/iyuba/chooseLessonNew.jsp?&appid=259&uid=12071118&type=junior&version=3
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_APPS, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @GET(UrlLibrary.junior_type)
    Observable<BaseBean_data<BaseBean_data_junior<List<Junior_type>>>> getMiddleTypeData(@Query(StrLibrary.appid) int appId,
                                                                                         @Query(StrLibrary.uid) String uid,
                                                                                         @Query(StrLibrary.type) String type,
                                                                                         @Query(StrLibrary.version) int version);

    /*************************************************书籍**********************************************/
    //中小学-书籍数据
    //http://apps.iyuba.cn/iyuba/getTitleBySeries.jsp?type=category&category=316&uid=12071118&appid=259&sign=b1930851a7b7531e87d75828b6c0844a&format=json
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_APPS, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @GET(UrlLibrary.junior_book)
    Observable<BaseBean_data<List<Junior_book>>> getJuniorBookData(@Query(StrLibrary.type) String type,
                                                                   @Query(StrLibrary.category) String category,
                                                                   @Query(StrLibrary.uid) String uid,
                                                                   @Query(StrLibrary.appid) int appId,
                                                                   @Query(StrLibrary.sign) String sign,
                                                                   @Query(StrLibrary.format) String format);

    /**********************************************章节****************************************/
    //小学-章节数据
    //http://apps.iyuba.cn/iyuba/getTitleBySeries.jsp?type=title&seriesid=205&uid=0&appid=260&sign=b1930851a7b7531e87d75828b6c0844a&format=json&version=1
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_APPS, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @GET(UrlLibrary.junior_chapter)
    Observable<BaseBean_data<List<Junior_chapter>>> getPrimaryChapterData(@Query(StrLibrary.type) String type,
                                                                          @Query(StrLibrary.seriesid) String seriesId,
                                                                          @Query(StrLibrary.uid) String uid,
                                                                          @Query(StrLibrary.appid) int appId,
                                                                          @Query(StrLibrary.sign) String sign,
                                                                          @Query(StrLibrary.format) String format,
                                                                          @Query(StrLibrary.version) int version);

    //初中-章节数据
    //http://apps.iyuba.cn/iyuba/getTitleBySeries.jsp?type=title&seriesid=453&uid=12071118&appid=259&sign=b1930851a7b7531e87d75828b6c0844a&format=json
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_APPS, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @GET(UrlLibrary.junior_chapter)
    Observable<BaseBean_data<List<Junior_chapter>>> getMiddleChapterData(@Query(StrLibrary.type) String type,
                                                                         @Query(StrLibrary.seriesid) String seriesId,
                                                                         @Query(StrLibrary.uid) String uid,
                                                                         @Query(StrLibrary.appid) int appId,
                                                                         @Query(StrLibrary.sign) String sign,
                                                                         @Query(StrLibrary.format) String format,
                                                                         @Query(StrLibrary.version) int version);

    /**************************************************章节详情************************************/
    //中小学-章节详情数据
    //http://apps.iyuba.cn/iyuba/textExamApi.jsp?format=json&voaid=3371001
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_APPS, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @GET(UrlLibrary.junior_chapter_detail)
    Observable<BaseBean_textDetail<List<Junior_chapter_detail>>> getJuniorChapterDetailData(@Query(StrLibrary.voaid) String voaId,
                                                                                            @Query(StrLibrary.format) String format);

    /****************************************************单词************************************/
    //中小学-单词数据
    //http://apps.iyuba.cn/iyuba/getWordByUnit.jsp?bookid=205
    /*@Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_APPS, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @GET(UrlLibrary.junior_word)
    Observable<BaseBean_data<List<Junior_word>>> getJuniorWordData(@Query(StrLibrary.bookid) String bookId);*/

    /****************************************************评测*****************************************/
    /**
     * 中小学-课程单句评测
     * http://iuserspeech.iyuba.cn:9001/test/ai/
     * sentence			I%20have%20a%20book.
     * flg			0
     * paraId			3
     * newsId			313002
     * protocol			60003
     * IdIndex			1
     * wordId			0
     * appId			260
     * type			primaryenglish
     * userId			12071118
     * platform			android
     * file	application/octet-stream	/storage/emulated/0/Android/data/com.iyuba.talkshow.childenglish/files/313002/1687830155526/3.aac	8.09 KB (8,280 bytes)
     */
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_IUSERSPEECH, StrLibrary.urlHost + ":" + NetHostManager.domain_short, StrLibrary.urlSuffix + ":" + UrlLibrary.SUFFIX_9001})
    @POST(UrlLibrary.junior_word_eval)
    Observable<BaseBean_data<Eval_result>> submitLessonSingleEval(@Body RequestBody body);

    /**
     * 中小学-发布单个评测数据
     * http://voa.iyuba.cn/voa/UnicomApi
     * topic	primaryenglish
     * topicid	313027
     * paraid	4
     * idIndex	1
     * platform	android
     * format	json
     * protocol	60003
     * userid	12071118
     * username	aiyuba_lil
     * voaid	313027
     * score	74
     * shuoshuotype	2
     * content	wav8/202306/primaryenglish/20230607/16861261070600446.mp3
     */
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_VOA, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @FormUrlEncoded
    @POST(UrlLibrary.EVAL_PUBLISH)
    Observable<Publish_eval> publishSingleEval(@Field(StrLibrary.topic) String topic,
                                               @Field(StrLibrary.topicid) String topicid,
                                               @Field(StrLibrary.paraid) String paraId,
                                               @Field(StrLibrary.idIndex) String idIndex,
                                               @Field(StrLibrary.platform) String platform,
                                               @Field(StrLibrary.format) String format,
                                               @Field(StrLibrary.protocol) int protocol,
                                               @Field(StrLibrary.userid) long uId,
                                               @Field(StrLibrary.username) String userName,
                                               @Field(StrLibrary.voaid) String voaId,
                                               @Field(StrLibrary.score) int score,
                                               @Field(StrLibrary.shuoshuotype) int shuoshuotype,
                                               @Field(StrLibrary.content) String content);


    //中小学-合成配音的数据
    //http://iuserspeech.iyuba.cn:9001/test/merge/
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_IUSERSPEECH, StrLibrary.urlHost + ":" + NetHostManager.domain_short, StrLibrary.urlSuffix + ":" + UrlLibrary.SUFFIX_9001})
    @POST(UrlLibrary.EVAL_MARGE)
    Observable<Marge_eval> margeAudioEval(@Body RequestBody body);

    /**
     * 发布合成的评测到排行榜
     * http://voa.iyuba.cn/voa/UnicomApi
     * topic	concept
     * platform	android
     * format	json
     * protocol	60003
     * userid	13883503
     * username
     * voaid	3002
     * score	1
     * shuoshuotype	4
     * content	wav6/202303/concept/20230303/16778226389594980.mp3
     */
    @Headers({StrLibrary.urlPrefix + ":" + UrlLibrary.HTTP_VOA, StrLibrary.urlHost + ":" + NetHostManager.domain_short})
    @FormUrlEncoded
    @POST(UrlLibrary.EVAL_PUBLISH)
    Observable<Publish_eval> publishMargeAudio(@Field(StrLibrary.topic) String topic,
                                               @Field(StrLibrary.platform) String platform,
                                               @Field(StrLibrary.format) String format,
                                               @Field(StrLibrary.protocol) int protocol,
                                               @Field(StrLibrary.userid) long uid,
                                               @Field(StrLibrary.username) String userName,
                                               @Field(StrLibrary.voaid) String voaId,
                                               @Field(StrLibrary.score) int score,
                                               @Field(StrLibrary.shuoshuotype) int shuoshuoType,
                                               @Field(StrLibrary.content) String content,
                                               @Field(StrLibrary.appid) int appId,
                                               @Field(StrLibrary.rewardVersion) int rewardVersion);

    /****************************************收藏*****************************/
    //收藏/取消收藏文章
    //http://apps.iyuba.cn/iyuba/updateCollect.jsp?groupName=Iyuba&sentenceFlg=0&appId=260&userId=12071118&topic=primary&voaId=313026&sentenceId=0&type=insert
    @Headers({StrLibrary.urlPrefix+":"+UrlLibrary.HTTP_APPS,StrLibrary.urlHost+":"+NetHostManager.domain_short})
    @GET(UrlLibrary.COLLECT_ARTICLE)
    Observable<Collect_chapter> collectArticle(@Query(StrLibrary.groupName) String groupName,
                                               @Query(StrLibrary.sentenceFlg) String sentenceFlg,
                                               @Query(StrLibrary.appId) int appId,
                                               @Query(StrLibrary.userId) String userId,
                                               @Query(StrLibrary.topic) String topic,
                                               @Query(StrLibrary.voaId) String voaId,
                                               @Query(StrLibrary.sentenceId) String sentenceId,
                                               @Query(StrLibrary.type) String type);

    //获取收藏的文章数据
    //http://cms.iyuba.cn/dataapi/jsp/getCollect.jsp?userId=12071118&sign=a9f0a998cf149fd187145a3abb176a30&topic=primary&appid=260&sentenceFlg=0
    @Headers({StrLibrary.urlPrefix+":"+UrlLibrary.HTTP_CMS,StrLibrary.urlHost+":"+NetHostManager.domain_short})
    @GET(UrlLibrary.COURSE_COLLECT)
    Observable<BaseBean_data<List<Junior_chapter_collect>>> getArticleCollect(@Query(StrLibrary.userId) int userId,
                                                                              @Query(StrLibrary.sign) String sign,
                                                                              @Query(StrLibrary.topic) String topic,
                                                                              @Query(StrLibrary.appid) int appId,
                                                                              @Query(StrLibrary.sentenceFlg) int flag);
}
