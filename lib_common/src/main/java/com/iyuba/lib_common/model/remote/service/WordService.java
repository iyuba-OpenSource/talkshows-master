package com.iyuba.lib_common.model.remote.service;

import com.iyuba.lib_common.bean.Word_collect;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.data.UrlLibrary;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.remote.bean.Word_detail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @title:  单词功能服务
 * @date: 2024/1/4 15:21
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public interface WordService {

    /**************************单词查询**************************/
    //查询单词
    @Headers({StrLibrary.urlPrefix+":"+ UrlLibrary.HTTP_WORD,StrLibrary.urlHost+":"+ NetHostManager.domain_short})
    @GET(UrlLibrary.word_search)
    Observable<Word_detail> searchWord(@Query(StrLibrary.q) String word);

    //收藏/取消收藏单词
    @Headers({StrLibrary.urlPrefix+":"+UrlLibrary.HTTP_WORD,StrLibrary.urlHost+":"+NetHostManager.domain_short})
    @GET(UrlLibrary.word_insert)
    Observable<Word_collect> collectWord(@Query(StrLibrary.userId) int userId,
                                         @Query(StrLibrary.mod) String mode,
                                         @Query(StrLibrary.groupName) String groupName,
                                         @Query(StrLibrary.word) String wordsStr);
}
