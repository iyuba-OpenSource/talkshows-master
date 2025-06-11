package com.iyuba.talkshow.ui.lil.ui.dubbing.preview;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.iyuba.lib_common.model.local.entity.DubbingEntity;
import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_marge;
import com.iyuba.lib_common.model.remote.bean.Publish_eval;
import com.iyuba.lib_common.model.remote.bean.Publish_marge;
import com.iyuba.lib_common.model.remote.manager.DubbingManager;
import com.iyuba.lib_common.ui.mvp.BasePresenter;
import com.iyuba.lib_common.util.LibDateUtil;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.toolbox.GsonUtils;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.Record;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.data.remote.IntegralService;
import com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean.PreviewDraftAudioBean;
import com.iyuba.talkshow.ui.lil.ui.dubbing.preview.bean.PreviewDraftScoreBean;
import com.iyuba.talkshow.ui.lil.util.BigDecimalUtil;
import com.iyuba.talkshow.util.RxUtil;
import com.qq.e.comm.pi.NEADI;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PreviewNewPresenter extends BasePresenter<PreviewNewView> {
    private static final String TAG = "PreviewNewPresenter";

    //类型
    public static final String TAG_audio = "audio";
    public static final String TAG_video = "video";

    //获取文本数据
    private Subscription getTextDis;
    //发布合成数据
    private Disposable publishEvalDis;

    //数据
    private final DataManager dataManager;

    @Inject
    public PreviewNewPresenter(DataManager mDataManager){
        this.dataManager = mDataManager;
    }

    @Override
    public void detachView() {
        super.detachView();

        RxUtil.unsubscribe(getTextDis);
        LibRxUtil.unDisposable(publishEvalDis);
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

    //发布评测数据
    public void submitPublishDis(int voaId,String category,List<VoaText> voaTexts,int score,String audioUrl){
        checkViewAttach();
        LibRxUtil.unDisposable(publishEvalDis);

        int flag = 1;
        String format = "json";
        int idIndex = 0;
        int paraId = 0;
        String platform = "android";
        int shuoshuotype = 3;

        //合成集合数据
        List<Publish_marge.WavListDTO> wavList = new ArrayList<>();
        for (int i = 0; i < voaTexts.size(); i++) {
            VoaText tempData = voaTexts.get(i);
            DubbingEntity entity = DubbingManager.getSingleDubbingData(UserInfoManager.getInstance().getUserId(), tempData.getVoaId(), tempData.paraId(), tempData.idIndex());
            if (entity!=null){
                double recordTime = BigDecimalUtil.trans2Double(entity.recordTime/1000.0f);

                wavList.add(new Publish_marge.WavListDTO(
                        entity.audioUrl,
                        BigDecimalUtil.trans2Double(tempData.timing()),
                        recordTime,
                        BigDecimalUtil.trans2Double(tempData.timing())+recordTime,
                        tempData.idIndex()
                ));
            }
        }

        //合成总数据
        Publish_marge margeBean = new Publish_marge(
                App.APP_NAME_EN,
                Integer.parseInt(category),
                flag,
                format,
                idIndex,
                paraId,
                platform,
                score,
                shuoshuotype,
                audioUrl,
                "talkshow",
                UserInfoManager.getInstance().getUserName(),
                voaId,
                wavList
        );

        //转换成json
        String jsonData = GsonUtils.toJson(margeBean);

        DubbingManager.publishMarge(UserInfoManager.getInstance().getUserId(),jsonData)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Dubbing_publish_marge>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        publishEvalDis = d;
                    }

                    @Override
                    public void onNext(Dubbing_publish_marge bean) {
                        if (bean!=null &&bean.getResultCode()==200&&bean.getShuoShuoId()>0){
                            getMvpView().showPublishStatus(true,bean.getShuoShuoId());
                        }else {
                            getMvpView().showPublishStatus(false,0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showPublishStatus(false,0);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //取消发布数据
    public void cancelUpload(){
        LibRxUtil.unDisposable(publishEvalDis);
    }


    public CharSequence formatTitle(String string) {
        SpannableString spannableString = new SpannableString(string);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#F85778"));
        spannableString.setSpan(colorSpan, string.length() - 5, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public CharSequence formatMessage(int wordCount, int averageScore, String time) {
        SpannableStringBuilder builder = new SpannableStringBuilder(
                "您一共配音了" + wordCount + "个单词，\n读了" + time + "s秒的时间，\n");

        SpannableString spannableString = new SpannableString("正确率" + averageScore + "%");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#F85778"));
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        builder.append(spannableString);

        if (averageScore >= 80) {
            builder.append("，读的真棒~");
        } else if (averageScore >= 60) {
            builder.append("，\n读的一般般，还需要努力呀～");
        } else {
            builder.append("，\n分数好低，还需努力啊~");
        }
        return builder;
    }

    public IntegralService getIntegralService(){
        return dataManager.getIntegralService();
    }

    //将数据保存到草稿箱中
    public void saveDataToDraft(Voa voa,int voaTextSize){
        //获取当前的评测数据
        List<DubbingEntity> list = DubbingManager.getMultiDubbingData(UserInfoManager.getInstance().getUserId(), voa.voaId());
        //音频数据
        List<PreviewDraftAudioBean> audioBeanList = new ArrayList<>();
        //成绩数据
        List<PreviewDraftScoreBean> scoreBeanList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            audioBeanList.add(new PreviewDraftAudioBean(String.valueOf(list.get(i).paraId-1),list.get(i).audioUrl));
            scoreBeanList.add(new PreviewDraftScoreBean(String.valueOf(list.get(i).paraId-1), (int) list.get(i).showScore));
        }

        String audioData = GsonUtils.toJson(audioBeanList);
        String scoreData = GsonUtils.toJson(scoreBeanList);

        //将评测数据合并成草稿箱数据
        Record record = Record.builder()
                .setAudio(audioData)
                .setScore(scoreData)
                .setDate(LibDateUtil.toDateStr(System.currentTimeMillis(),LibDateUtil.YMDHMS))
                .setVoaId(voa.voaId())
                .setTitle(voa.title())
                .setTitleCn(voa.titleCn())
                .setTimestamp(System.currentTimeMillis())
                .setTotalNum(voaTextSize)
                .setFinishNum(list.size())
                .setImg(voa.pic())
                .build();

        //将数据保存在数据库中
        dataManager.saveRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showSaveDraftStatus(false);
                        Log.d(TAG, "保存草稿数据到数据库异常");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getMvpView().showSaveDraftStatus(aBoolean);
                        Log.d(TAG, "保存草稿数据到数据库成功");
                    }
                });
    }
}
