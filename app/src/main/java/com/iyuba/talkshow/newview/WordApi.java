package com.iyuba.talkshow.newview;

import com.iyuba.wordtest.entity.WordAiEntity;
import com.iyuba.wordtest.entity.WordEntity;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WordApi {

    /*setAbsoluteURI("http://word.iyuba.cn/words/updateWord.jsp?userId="
                           + this.userId + "&mod=" + update_mode + "&groupName="
                           + groupname + "&word=" + this.word);*/
    @GET("words/updateWord.jsp?")
    Observable<String>operateWord(
            @Query("word") String word,
            @Query("mod") String mod,
            @Query("groupName") String groupname,
            @Query("userId") String userId
    );

    @GET("apiWord.jsp")
    Call<WordEntity> getWordApi(@Query("q") String word);

    @GET("updateWord.jsp")
    Call<WordEntity> updateWord(@Query("userId") String userId,
                                @Query("mod") String mod,
                                @Query("groupName") String groupName,
                                @Query("word") String word);

    @GET("apiWordAi.jsp")
    Call<WordAiEntity> getWordAi(@Query("q") String word, @Query("user_pron") String user_pron, @Query("ori_pron") String ori_pron,
                                 @Query("appid") int appid, @Query("uid") int uid);

}
