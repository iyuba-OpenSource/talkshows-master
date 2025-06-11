package com.iyuba.lib_common.model.remote.manager;

import com.iyuba.lib_common.manager.AppInfoManager;
import com.iyuba.lib_common.model.remote.RemoteHelper;
import com.iyuba.lib_common.model.remote.bean.Collect_chapter;
import com.iyuba.lib_common.model.remote.bean.Junior_book;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter_collect;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter_detail;
import com.iyuba.lib_common.model.remote.bean.Junior_type;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data_junior;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data_primary;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_textDetail;
import com.iyuba.lib_common.model.remote.service.JuniorEnService;
import com.iyuba.lib_common.model.remote.util.SignUtil;
import com.iyuba.lib_common.util.LibTopicUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * @title:  青少年英语-远程接口管理
 * @date: 2023/12/28 17:02
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description: 小学英语和初中英语内容
 */
public class JuniorEnRemoteManager {

    /***************************************出版社********************************/
    //小学-出版社类型数据
    public static Observable<BaseBean_data<BaseBean_data_primary<List<Junior_type>>>> getPrimaryType(){
        String uid = "0";
        String type = "primary";
        int version = 3;
        int appId = AppInfoManager.getInstance().getAppId();

        JuniorEnService juniorService = RemoteHelper.getInstance().createJson(JuniorEnService.class);
        return juniorService.getPrimaryTypeData(appId,uid,type,version);
    }

    //初中-出版社类型数据
    public static Observable<BaseBean_data<BaseBean_data_junior<List<Junior_type>>>> getMiddleType(){
        String uid = "0";
        String type = "junior";
        int version = 3;
        int appId = AppInfoManager.getInstance().getAppId();

        JuniorEnService juniorService = RemoteHelper.getInstance().createJson(JuniorEnService.class);
        return juniorService.getMiddleTypeData(appId,uid,type,version);
    }

    /******************************************书籍*********************************/
    //中小学-书籍数据
    public static Observable<BaseBean_data<List<Junior_book>>> getJuniorBook(String typeId){
        String type = "category";
        String uid = "0";
        String format = "json";
        String sign = SignUtil.getJuniorBookSign();
        int appId = AppInfoManager.getInstance().getAppId();

        JuniorEnService juniorEnService = RemoteHelper.getInstance().createJson(JuniorEnService.class);
        return juniorEnService.getJuniorBookData(type,typeId,uid,appId,sign,format);
    }

    /*******************************************章节************************************/
    //小学-章节数据
    public static Observable<BaseBean_data<List<Junior_chapter>>> getPrimaryChapter(String bookId){
        int version = 1;
        String type = "title";
        String format = "json";
        String sign = SignUtil.getJuniorChapterSign();
        String uid = "0";
        int appId = AppInfoManager.getInstance().getAppId();

        JuniorEnService juniorService = RemoteHelper.getInstance().createJson(JuniorEnService.class);
        return juniorService.getPrimaryChapterData(type,bookId,uid,appId,sign,format,version);
    }

    //初中-章节数据
    public static Observable<BaseBean_data<List<Junior_chapter>>> getMiddleChapter(String bookId){
        int version = 1;
        String type = "title";
        String format = "json";
        String sign = SignUtil.getJuniorChapterSign();
        String uid = "0";
        int appId = AppInfoManager.getInstance().getAppId();

        JuniorEnService juniorService = RemoteHelper.getInstance().createJson(JuniorEnService.class);
        return juniorService.getMiddleChapterData(type,bookId,uid,appId,sign,format,version);
    }

    /********************************章节详情*********************************/
    //中小学-章节详情数据
    public static Observable<BaseBean_textDetail<List<Junior_chapter_detail>>> getJuniorChapterDetail(String voaId){
        String format = "json";

        JuniorEnService juniorService = RemoteHelper.getInstance().createJson(JuniorEnService.class);
        return juniorService.getJuniorChapterDetailData(voaId,format);
    }

    /**********************************收藏/取消收藏*******************************/
    //接口-收藏/取消收藏文章
    public static Observable<Collect_chapter> collectArticle(String types, String userId, String voaId, boolean isCollect){
        String groupName = "Iyuba";
        String sentenceFlag = "0";

        int appId = LibTopicUtil.getAppId(types);
        String topic = LibTopicUtil.getLessonCollectTopic(types);
        String sentenceId = "0";
        String type = "del";
        if (isCollect){
            type = "insert";
        }

        JuniorEnService commonService = RemoteHelper.getInstance().createXml(JuniorEnService.class);
        return commonService.collectArticle(groupName,sentenceFlag,appId,userId,topic,voaId,sentenceId,type);
    }

    //接口-获取收藏的文章数据
    public static Observable<BaseBean_data<List<Junior_chapter_collect>>> getArticleCollect(String types, int userId){
        int appId = LibTopicUtil.getAppId(types);
        String topic = LibTopicUtil.getLessonCollectTopic(types);
        int flag = 0;
        String sign = SignUtil.getJuniorArticleCollectSign(topic,userId,appId);

        JuniorEnService commonService = RemoteHelper.getInstance().createJson(JuniorEnService.class);
        return commonService.getArticleCollect(userId,sign,topic,appId,flag);
    }
}
