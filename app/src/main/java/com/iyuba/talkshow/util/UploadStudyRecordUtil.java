package com.iyuba.talkshow.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.model.StudyRecord;
import com.iyuba.talkshow.data.model.UploadRecordResult;
import com.iyuba.talkshow.data.remote.UploadStudyRecordService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * UploadStudyRecordUtil
 *
 * @author wayne
 * @date 2018/2/8
 */
public class UploadStudyRecordUtil {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    SimpleDateFormat sdf222 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private StudyRecord studyRecord;
    private int userId;
    private String getLocalDeviceType;
    private String getLocalMACAddress;
    boolean isFirst = true;

    public UploadStudyRecordUtil(boolean login, Context context, int uid, int lessonId, String testNum, String mode) {
        GetDeviceInfo getDeviceInfo = new GetDeviceInfo(context);
        getLocalDeviceType = getDeviceInfo.getLocalDeviceType();
        getLocalMACAddress = getDeviceInfo.getLocalMACAddress();
        this.userId = uid;
        studyRecord = new StudyRecord();
        studyRecord.setLesson(App.APP_NAME_EN);
        studyRecord.setLessonid(lessonId + "");
        studyRecord.setTestNumber(testNum);
        studyRecord.setTestmode(mode);
    }

    public UploadStudyRecordUtil(boolean login, Context context, int uid, int lessonId, String testNum) {
        GetDeviceInfo getDeviceInfo = new GetDeviceInfo(context);
        getLocalDeviceType = getDeviceInfo.getLocalDeviceType();
        getLocalMACAddress = getDeviceInfo.getLocalMACAddress();
        this.userId = uid;
        studyRecord = new StudyRecord();
        studyRecord.setLesson(App.APP_NAME_EN);
        studyRecord.setLessonid(lessonId + "");
        studyRecord.setTestNumber(testNum);
        Timber.e("学习记录start");
    }

    public void stopStudyRecord(final Context context, boolean login, String flag, UploadStudyRecordService service) {

        Observable<UploadRecordResult> observable = stopAndUpload(login, flag, service);
        if (observable == null) {
            Log.e("UploadStudyRecordUtil", "UploadStudyRecordUtil observable is null");
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadRecordResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("UploadStudyRecordUtil", "UploadStudyRecordUtil onError " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(UploadRecordResult uploadRecordResult) {
                        if (uploadRecordResult == null) {
                            Log.e("UploadStudyRecordUtil", "uploadRecordResult.onNext() is null.");
                            return;
                        }
                        Log.e("UploadStudyRecordUtil", "uploadRecordResult.getResult() " + uploadRecordResult.getResult());
                        if ("1".equals(uploadRecordResult.getResult())) {
                            if (!TextUtils.isEmpty(uploadRecordResult.getJifen()) && uploadRecordResult.getMessage().contains("success")) {
                                Log.e("UploadStudyRecordUtil", "uploadRecordResult.getJifen() " + uploadRecordResult.getJifen());
                                try {
                                    if (Integer.parseInt(uploadRecordResult.getJifen()) > 0) {
                                        Toast.makeText(context, "学习记录上传成功,  积分加" + uploadRecordResult.getJifen(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    public StudyRecord getStudyRecord() {
        return studyRecord;
    }

    public Observable<UploadRecordResult> stopAndUpload(boolean isLogin, String flag, UploadStudyRecordService service) {
        if (!isLogin) {
            return null;
        }
        if (flag.equals("0") && isFirst) {
            isFirst = false;
//        } else {
//            return null;
        }
        studyRecord.setEndtime(System.currentTimeMillis());
        studyRecord.setFlag(flag);

        if (studyRecord.getEndtime() - studyRecord.getStarttime() < 3 * 1000) {
            return null;
        }

        Log.d("学习报告", "stopAndUpload: --"+getFormatTime(sdf222,studyRecord.getStarttime())+"--"+getFormatTime(sdf222,studyRecord.getEndtime()));

        return uploadStudyRecord(userId + "", service, studyRecord);
    }

    public String getFormatTime(SimpleDateFormat format, long currentTime) {
        return format.format(new Date(currentTime));
    }

    public Observable<UploadRecordResult> uploadStudyRecord(String userId,
                                                            UploadStudyRecordService service,
                                                            StudyRecord studyRecord) {

        return service.uploadStudyRecord(
                "json",
                App.APP_ID + "",
                App.APP_NAME_CH,
                studyRecord.getLesson(),
                studyRecord.getLessonid(),
                userId,
                getLocalDeviceType,
                getLocalMACAddress,
                getFormatTime(sdf222, studyRecord.getStarttime()),
                getFormatTime(sdf222, studyRecord.getEndtime()),
                studyRecord.getFlag(),
                studyRecord.getWordCount(),
                studyRecord.getTestmode(),
                "android",
                studyRecord.getTestNumber(),
                MD5.getMD5ofStr(userId + getFormatTime(sdf222, studyRecord.getStarttime()) + sdf.format(new Date()))
        );
    }
}
