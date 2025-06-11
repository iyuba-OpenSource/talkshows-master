package com.iyuba.iyubamovies.util;

import android.content.Context;

import com.iyuba.iyubamovies.manager.IMoviesConstant;
import com.iyuba.iyubamovies.model.ImoviesStudyRecord;
import com.iyuba.iyubamovies.network.ImoviesNetwork;
import com.iyuba.iyubamovies.network.result.ImoviesUploadRecordResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by iyuba on 2017/11/30.
 */

public class ImoviesStudyRecordUtil {

        private static final String TAG = "HeadlineStudyRecordUtil";
        private static ImoviesStudyRecordUtil sInstance;
        private static Context mContext;
        private ImoviesGetDeviceInfo getDeviceInfo;
        private int readSpeed;

        private static final SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        private static final SimpleDateFormat ssdf = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.CHINA);

//    private Context mContext = HeadlinesRuntimeManager.getContext();

        protected Subscription subscription;
        private static String recordId;
        private static String recordStart;

    public static synchronized ImoviesStudyRecordUtil  getInstance(Context context){
        mContext = context;
        if(sInstance == null){
            sInstance = new ImoviesStudyRecordUtil ();
        }
        return sInstance;
    }

    Observer<ImoviesUploadRecordResult> observer = new Observer<ImoviesUploadRecordResult>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
//            Toast.makeText(mContext, R.string.headline_upload_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(ImoviesUploadRecordResult uploadRecordResult) {
            if (uploadRecordResult.getResult().equals("1")) {
//                Toast.makeText(mContext, R.string.upload_success, Toast.LENGTH_SHORT).show();
                if(uploadRecordResult.getResult() != null && Integer.parseInt(uploadRecordResult.getJifen()) > 0){

                }

            }
        }
    };

    public void recordStart(String voaid) {
        recordId = voaid;
        recordStart = sdf.format(new Date());
    }

    public boolean recordStop(String lesson, String flag, String testMode, int wordCount) {
        getDeviceInfo = new ImoviesGetDeviceInfo(mContext);
        ImoviesStudyRecord studyRecord = new ImoviesStudyRecord();
        studyRecord.endtime = sdf.format(new Date());
        studyRecord.flag = flag;
        String userid = IMoviesConstant.UserId;
        studyRecord.lesson = ImoviesTextAttr.encode(ImoviesTextAttr.encode(lesson));
        studyRecord.voaid = String.valueOf(recordId);
        studyRecord.testmode = testMode;
        studyRecord.starttime = recordStart;
        try {
            if(sdf != null && studyRecord.starttime != null
                    && studyRecord.endtime != null
                    && !studyRecord.endtime.equals(studyRecord.starttime)){
                readSpeed = wordCount / (int)((sdf.parse(studyRecord.endtime).getTime()
                        - sdf.parse(studyRecord.starttime).getTime()) / 1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if( lesson.equals("news") && readSpeed <= 5){
            sendToNet(studyRecord, userid, testMode,wordCount);
        }else if (!lesson.equals("news")){
            sendToNet(studyRecord, userid, testMode,wordCount);
        }else {
//            Toast.makeText(mContext,"您的阅读速度为:"+readSpeed+",过快，请重新阅读！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendToNet(final ImoviesStudyRecord studyRecord, String userid, String testMode, int wordCount) {
        if (IMoviesNetWorkCheck.isConnectInternet(mContext)) {
            subscription = ImoviesNetwork.getUploadStudyRecordApi()
                    .uploadStudyRecord(
                            "json",
                            IMoviesConstant.App_id,
                            IMoviesConstant.App_Name,
                            studyRecord.getLesson(),
                            studyRecord.getVoaid(),
                            userid,
                            getDeviceInfo.getLocalDeviceType(),
                            getDeviceInfo.getLocalMACAddress(),
                            studyRecord.getStarttime(),
                            studyRecord.getEndtime(),
                            studyRecord.getFlag(),
                            wordCount,
                            testMode,
                            "android",
                            IMoviesMD5.getMD5ofStr(userid+studyRecord.getStarttime()+ssdf.format(new Date()))
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

}

