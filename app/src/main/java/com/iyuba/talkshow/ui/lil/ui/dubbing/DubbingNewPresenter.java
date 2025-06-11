package com.iyuba.talkshow.ui.lil.ui.dubbing;

import android.util.Log;
import android.util.Pair;

import com.iyuba.lib_common.model.local.entity.DubbingEntity;
import com.iyuba.lib_common.model.remote.bean.Eval_result;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.model.remote.manager.DubbingManager;
import com.iyuba.lib_common.ui.mvp.BasePresenter;
import com.iyuba.lib_common.util.LibDateUtil;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.toolbox.GsonUtils;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.Download;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.ui.lil.event.DownloadFileEvent;
import com.iyuba.talkshow.ui.lil.util.BigDecimalUtil;
import com.iyuba.talkshow.util.RxUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DubbingNewPresenter extends BasePresenter<DubbingNewView> {
    private static final String TAG = "DubbingNewPresenter";

    //类型
    public static final String TAG_audio = "audio";
    public static final String TAG_video = "video";

    //获取文本数据
    private Subscription getTextDis;
    //下载音视频
    private Disposable downloadFileDis;
    //提交评测
    private Disposable submitEvalDis;

    //数据
    private final DataManager dataManager;

    @Inject
    public DubbingNewPresenter(DataManager mDataManager){
        this.dataManager = mDataManager;
    }

    @Override
    public void detachView() {
        super.detachView();

        RxUtil.unsubscribe(getTextDis);
        LibRxUtil.unDisposable(downloadFileDis);
        LibRxUtil.unDisposable(submitEvalDis);
    }

    //获取本地的文本数据
    public void getLocalText(int voaId){
        checkViewAttach();
        RxUtil.unsubscribe(getTextDis);
        getTextDis = dataManager.getVoaTexts(voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VoaText>>() {
                    @Override
                    public void onCompleted() {
                        RxUtil.unsubscribe(getTextDis);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showVoaText(null);
                    }

                    @Override
                    public void onNext(List<VoaText> voaTexts) {
                        if (voaTexts!=null&&voaTexts.size()>0){
                            getMvpView().showVoaText(voaTexts);
                        }else {
                            getMvpView().showVoaText(new ArrayList<>());
                        }
                    }
                });
    }

    //下载音视频数据
    private int curIndex = 0;
    private List<Pair<String,Pair<String,String>>> pairList = null;
    private Pair<String,Pair<String,String>> pair = null;

    public void downloadFile(List<Pair<String,Pair<String,String>>> list){
        if (list!=null&&list.size()>0){
            this.pairList = list;
        }

        if (curIndex >= pairList.size()){
            //完成
            EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_finish,null));
            return;
        }

        if (pairList!=null&&pairList.size()>curIndex){
            pair = pairList.get(curIndex);
        }

        if (pair!=null){
            checkViewAttach();
            String downloadUrl = pair.second.first;
            DubbingManager.downloadFile(downloadUrl)
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(io.reactivex.schedulers.Schedulers.io())
                    .subscribe(new io.reactivex.Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            downloadFileDis = d;
                        }

                        @Override
                        public void onNext(ResponseBody response) {
                            InputStream is = response.byteStream();
                            saveFile(pair.first,pair.second.second,is, response.contentLength());
                        }

                        @Override
                        public void onError(Throwable e) {
                            String fileType = "";
                            if (pair.first.equals(TAG_audio)){
                                fileType = "音频";
                            }else if (pair.first.equals(TAG_video)){
                                fileType = "视频";
                            }

                            EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_error,fileType+"文件下载异常，请重试~"));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

            /*DubbingManager.downloadFile(downloadUrl)
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .subscribe(new io.reactivex.Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            downloadFileDis = d;
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            InputStream is = responseBody.byteStream();
                            saveFile(pair.first,pair.second.second,is, responseBody.contentLength());
                        }

                        @Override
                        public void onError(Throwable e) {
                            String fileType = "";
                            if (pair.first.equals(TAG_audio)){
                                fileType = "音频";
                            }else if (pair.first.equals(TAG_video)){
                                fileType = "视频";
                            }

                            EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_error,fileType+"文件下载异常，请重试~"));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });*/

            /*OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10,TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .get()
                    .url(downloadUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String fileType = "";
                    if (pair.first.equals(TAG_audio)){
                        fileType = "音频";
                    }else if (pair.first.equals(TAG_video)){
                        fileType = "视频";
                    }

                    EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_error,fileType+"文件下载异常，请重试~"));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    saveFile(pair.first,pair.second.second,is, response.body().contentLength());
                }
            });*/
        }
    }

    private void saveFile(String fileType, String savePath, InputStream inputStream,long fileLength){
        //文件类型
        String showFileType = "";
        if (fileType.equals(TAG_audio)){
            showFileType = "音频";
        }else if (fileType.equals(TAG_video)){
            showFileType = "视频";
        }
        //创建文件
        File saveFile = new File(savePath);
        try {
            if (saveFile.exists()){
                saveFile.delete();
            }else {
                if (!saveFile.getParentFile().exists()){
                    saveFile.getParentFile().mkdirs();
                }
            }

            boolean isCreateFile = saveFile.createNewFile();
            if (isCreateFile){
                //累加长度
                long progressLength = 0;

                OutputStream os = new FileOutputStream(savePath);
                byte[] bytes = new byte[2048];
                int length = 0;

                while ((length = inputStream.read(bytes))!=-1){
                    os.write(bytes,0,length);

                    //刷新进度显示
                    progressLength+=length;
                    int progress = (int) (progressLength*100/fileLength);
                    String showMsg = showFileType+"文件下载进度("+progress+"%)";
                    EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_downloading,showMsg));
                    Log.d("下载进度", showFileType+showMsg);
                }

                os.flush();
                os.close();

                //下一个文件
                curIndex++;
                downloadFile(null);
                if (fileType.equals(TAG_audio)){
                    EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_downloading,"准备下载视频文件"));
                }
            }else {
                EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_error,"创建文件异常"));
            }
        }catch (Exception e){
            EventBus.getDefault().post(new DownloadFileEvent(DownloadFileEvent.STATUS_error,"创建文件异常"));
        }
    }

    //提交评测数据
    public void submitEval(String sentence,int voaId,int paraId,int indexId,int userId,String evalPath,long recordTime){
        checkViewAttach();
        LibRxUtil.unDisposable(submitEvalDis);
        DubbingManager.submitEval(sentence, voaId, paraId, indexId, userId, evalPath)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<BaseBean_data<Eval_result>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        submitEvalDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_data<Eval_result> bean) {
                        if (bean!=null&&bean.getResult().equals("1")){
                            //评测的数据
                            Eval_result result = bean.getData();
                            String wordData = GsonUtils.toJson(result.getWords());
                            //保存在本地
                            DubbingEntity entity = new DubbingEntity(
                                    userId,
                                    voaId,
                                    paraId,
                                    indexId,
                                    recordTime,
                                    sentence,
                                    result.getTotal_score(),
                                    result.getScores(),
                                    evalPath,
                                    result.getUrl(),
                                    wordData
                            );
                            DubbingManager.saveSingleDubbingData(entity);
                            //刷新适配器显示
                            getMvpView().showEval(true,null);
                        }else {
                            getMvpView().showEval(false,"提交评测失败("+bean.getResult()+")，请重试～");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showEval(false,"提交评测异常，请重试～");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //保存音视频数据到本地数据库
    public void saveDownloadDataToDB(int voaId,String audioPath,String videoPath){
        String downloadId = UserInfoManager.getInstance().getUserId()+"_"+voaId;
        String downloadDate = LibDateUtil.toDateStr(System.currentTimeMillis(),LibDateUtil.YMDHMS);

        //处理下载数据
        Download download = Download.builder()
                .setAudioPath(audioPath)
                .setVideoPath(videoPath)
                .setId(downloadId)
                .setVoaId(voaId)
                .setDate(downloadDate)
                .setUid(UserInfoManager.getInstance().getUserId())
                .build();

        //保存到数据库
        dataManager.saveDownload(download)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "保存下载数据到数据库异常");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.d(TAG, "保存下载数据到数据库成功");
                    }
                });
    }

    //判断文件是否存在
    //
    public boolean checkFileExist(int voaId,String audioPath,String videoPath) {
        //文件存在
        File audioFile = new File(audioPath);
        File videoFile = new File(videoPath);
        boolean isFileExist = audioFile.exists()&&videoFile.exists();
        //数据库内容存在
        boolean isDbExist = dataManager.getSingleDownload(UserInfoManager.getInstance().getUserId(), voaId)!=null;

        return isFileExist&&isDbExist;
    }
}
