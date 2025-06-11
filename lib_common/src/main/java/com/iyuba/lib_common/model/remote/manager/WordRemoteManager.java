package com.iyuba.lib_common.model.remote.manager;

import com.iyuba.lib_common.bean.Word_collect;
import com.iyuba.lib_common.model.remote.RemoteHelper;
import com.iyuba.lib_common.model.remote.bean.Word_detail;
import com.iyuba.lib_common.model.remote.service.WordService;

import io.reactivex.Observable;

/**
 * @title:
 * @date: 2024/1/4 15:33
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class WordRemoteManager {

    //接口-查询单词
    public static Observable<Word_detail> searchWord(String word){
        WordService commonService = RemoteHelper.getInstance().createXml(WordService.class);
        return commonService.searchWord(word);
    }

    //接口-收藏/取消收藏单词
    public static Observable<Word_collect> insertOrDeleteWord(String word, int userId, boolean isInsert){
        String mode = "delete";
        if (isInsert){
            mode = "insert";
        }
        String groupName = "Iyuba";

        WordService commonService = RemoteHelper.getInstance().createXml(WordService.class);
        return commonService.collectWord(userId,mode,groupName,word);
    }
}
