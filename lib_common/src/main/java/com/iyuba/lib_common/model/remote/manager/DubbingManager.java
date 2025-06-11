package com.iyuba.lib_common.model.remote.manager;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.manager.AppInfoManager;
import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.local.RoomDB;
import com.iyuba.lib_common.model.local.entity.DubbingEntity;
import com.iyuba.lib_common.model.remote.RemoteHelper;
import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_marge;
import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_release;
import com.iyuba.lib_common.model.remote.bean.Eval_result;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.model.remote.service.DubbingService;
import com.iyuba.lib_common.util.LibEncodeUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DubbingManager {

    //下载文件
    public static Observable<ResponseBody> downloadFile(String url){
//        DubbingService dubbingService = RemoteHelper.getInstance().create(DubbingService.class);
//        return dubbingService.downloadFile(url);

        //okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();

        String baseUrl = "";
        String baseFix1 = "http://staticvip."+ NetHostManager.getInstance().getDomainShort();
        String baseFix2 = "http://static0."+NetHostManager.getInstance().getDomainShort();
        if (url.startsWith(baseFix1)){
            baseUrl = baseFix1;
            url = url.replace(baseFix1,"");
        }else if (url.startsWith(baseFix2)){
            baseUrl = baseFix2;
            url = url.replace(baseFix2,"");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        DubbingService dubbingService = retrofit.create(DubbingService.class);
        return dubbingService.downloadFile(url);
    }

    //提交评测
    public static Observable<BaseBean_data<Eval_result>> submitEval(String sentence, int voaId, int paraId, int indexId, int userId, String filePath){
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

        int flag = 0;
        String type = Constant.EVAL_TYPE;
        int appId = AppInfoManager.getInstance().getAppId();
        int wordId = 0;
        sentence = LibEncodeUtil.encode(sentence).replaceAll("\\+","%20");

        File file = new File(filePath);
        RequestBody fileBody = MultipartBody.create(MediaType.parse("application/octet-stream"),file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(StrLibrary.sentence, sentence)
                .addFormDataPart(StrLibrary.flg,String.valueOf(flag))
                .addFormDataPart(StrLibrary.paraId,String.valueOf(paraId))
                .addFormDataPart(StrLibrary.newsId,String.valueOf(voaId))
                .addFormDataPart(StrLibrary.IdIndex,String.valueOf(indexId))
                .addFormDataPart(StrLibrary.wordId,String.valueOf(wordId))
                .addFormDataPart(StrLibrary.appId,String.valueOf(appId))
                .addFormDataPart(StrLibrary.type,type)
                .addFormDataPart(StrLibrary.userId,String.valueOf(userId))
                .addFormDataPart(StrLibrary.file,file.getName(),fileBody)
                .build();

        DubbingService dubbingService = RemoteHelper.getInstance().createJson(DubbingService.class);
        return dubbingService.submitEval(multipartBody);
    }

    //发布合成
    public static Observable<Dubbing_publish_marge> publishMarge(int userId, String jsonData){
        int protocol = 60002;
        int content = 3;

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonData);
        DubbingService dubbingService = RemoteHelper.getInstance().createJson(DubbingService.class);
        return dubbingService.publishMarge(protocol,userId,content,body);
    }

    //获取已经发布的评测数据
    public static Observable<Dubbing_publish_release> getPublishData(int userId){
        int appId = AppInfoManager.getInstance().getAppId();
        String appName = "talkshow";

        DubbingService dubbingService = RemoteHelper.getInstance().createJson(DubbingService.class);
        return dubbingService.getReleasePublishData(userId,appId,appName);
    }

    /************************************数据库*************************/
    //保存单个评测数据
    public static void saveSingleDubbingData(DubbingEntity entity){
        RoomDB.getInstance().getDubbingDao().saveSingleData(entity);
    }

    //获取单个评测数据
    public static DubbingEntity getSingleDubbingData(int userId,int voaId,int paraId,int indexId){
        return RoomDB.getInstance().getDubbingDao().getSingleData(userId, voaId, paraId, indexId);
    }

    //获取多个评测数据
    public static List<DubbingEntity> getMultiDubbingData(int userId, int voaId){
        return RoomDB.getInstance().getDubbingDao().getMultiData(userId, voaId);
    }

}
