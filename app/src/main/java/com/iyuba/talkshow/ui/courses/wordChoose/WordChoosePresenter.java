package com.iyuba.talkshow.ui.courses.wordChoose;


import android.util.Log;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.LessonNewResponse;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.SeriesResponse;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.injection.PerFragment;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.BaseLessonResponse;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.JuniorResponse;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.PrimaryResponse;
import com.iyuba.talkshow.util.FileUtils;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.StorageUtil;
import com.iyuba.wordtest.db.WordDataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@PerFragment
public class WordChoosePresenter extends BasePresenter<WordChooseMVPView> {

    private final DataManager mDataManager;
    private final ConfigManager mConfigManager;

    private Subscription mLessonSub;
    private Subscription mSeriesSub;
    private Subscription mLoadSeriesSub;

    @Inject
    public WordChoosePresenter(ConfigManager configManager , DataManager mDataManager) {
        this.mConfigManager = configManager ;
        this.mDataManager = mDataManager;
    }

    public List<Voa> loadVoasByBookId(int bookId) {
        checkViewAttached();
        Log.e("ChooseCoursePresenter", "loadVoasByBookId " + bookId);
        return mDataManager.getVoaXiaoxueByBookId(bookId);
    }
    public List<SeriesData> getAllSeries(int cat) {
        return mDataManager.getSeriesCategory(cat);
    }

    public void chooseLessonNew(String lessonType) {
        Log.e("ChooseCoursePresenter", "chooseLessonNew app Id = " + App.APP_ID);
        checkViewAttached();
        RxUtil.unsubscribe(mLessonSub);
        mLessonSub = mDataManager.chooseLessonNew(App.APP_ID, UserInfoManager.getInstance().getUserId(), lessonType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LessonNewResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("ChooseCoursePresenter", "chooseLessonNew onError " + e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(LessonNewResponse response) {
                        if (response == null || response.data == null) {
                            Log.e("ChooseCoursePresenter", "chooseLessonNew onNext response is null. ");
                            return;
                        }
                        if (response.result != 200) {
                            Log.e("ChooseCoursePresenter", "chooseLessonNew onNext response.result " + response.result);
                            return;
                        }
                        if (getMvpView() != null) {
                            getMvpView().setLesson(response.data.primary);
                        }
                    }
                });
    }

    //小学数据
    public void choosePrimaryLessonNew(String lessonType) {
        checkViewAttached();
        RxUtil.unsubscribe(mLessonSub);
        mLessonSub = mDataManager.choosePrimaryLessonNew(App.APP_ID, UserInfoManager.getInstance().getUserId(), lessonType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseLessonResponse<PrimaryResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().setLessonFail("获取类型数据失败");
                    }

                    @Override
                    public void onNext(BaseLessonResponse<PrimaryResponse> response) {
                        if (response == null || response.data == null) {
                            getMvpView().setLessonFail("获取类型数据失败");
                            return;
                        }
                        if (response.result != 200) {
                            getMvpView().setLessonFail("获取类型数据失败");
                            return;
                        }
                        if (getMvpView() != null) {
                            getMvpView().setLesson(response.data.primary);
                        }
                    }
                });
    }
    //初中数据
    public void chooseJuniorLessonNew(String lessonType) {
        checkViewAttached();
        RxUtil.unsubscribe(mLessonSub);
        mLessonSub = mDataManager.chooseJuniorLessonNew(App.APP_ID, UserInfoManager.getInstance().getUserId(), lessonType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseLessonResponse<JuniorResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().setLessonFail("获取类型数据失败");
                    }

                    @Override
                    public void onNext(BaseLessonResponse<JuniorResponse> response) {
                        if (response == null || response.data == null) {
                            getMvpView().setLessonFail("获取类型数据失败");
                            return;
                        }
                        if (response.result != 200) {
                            getMvpView().setLessonFail("获取类型数据失败");
                            return;
                        }
                        if (getMvpView() != null) {
                            getMvpView().setLesson(response.data.junior);
                        }
                    }
                });
    }

    //获取书籍数据-网络
    public void getBookDataByRemote(String catId) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSeriesSub);
        mLoadSeriesSub = mDataManager.getCategorySeriesList(UserInfoManager.getInstance().getUserId(), catId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SeriesResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getBookDataByDb(catId);
                    }

                    @Override
                    public void onNext(SeriesResponse response) {
                        if (response == null || response.getData() == null) {
                            getBookDataByDb(catId);
                            return;
                        }

                        List<SeriesData> result = new ArrayList<>();
                        for (SeriesData series: response.getData()) {
                            if (series.getCategory().equalsIgnoreCase("" + catId)) {
                                result.add(series);
                            }
                        }
                        if (getMvpView() != null) {
                            getMvpView().setCoures(result);
                        }
                        TalkShowApplication.getSubHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                for (SeriesData bean : response.getData()) {
                                    mDataManager.insertSeriesDB(bean);
                                }
                            }
                        });
                    }
                });
    }
    //获取书籍数据-本地
    public void getBookDataByDb(String catId) {
        checkViewAttached();
        RxUtil.unsubscribe(mSeriesSub);
        mSeriesSub = mDataManager.getSeriesList(catId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SeriesData>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("ChooseCoursePresenter", "chooseCourse onError " + e.getMessage());
                        }
//                        getSeriesList(catId);
                    }

                    @Override
                    public void onNext(List<SeriesData> list) {
                        if ((list == null) || (list.size() < 1)) {
                            Log.e("ChooseCoursePresenter", "chooseCourse onNext is null? ");
//                            getSeriesList(catId);
                            return;
                        }
                        if (getMvpView() != null) {
                            getMvpView().setMoreCourse(list);
                        }
                    }
                });
    }

    public String getLessonType(){
        return mConfigManager.getWordShowType();
    }
    public void putLessonType(String lessonType){
        mConfigManager.setWordShowType(lessonType);
    }

    public int getCourseCategory() {
        return mConfigManager.getCourseCategory();
    }
    public void putCourseCategory(int type) {
        mConfigManager.putCourseCategory(type);
    }
    public int getCourseType() {
        return mConfigManager.getCourseType();
    }
    public void putCourseType(int type) {
        mConfigManager.putCourseType(type);
    }
    public int getCourseClass() {
        return mConfigManager.getCourseClass();
    }
    public void putCourseClass(int type) {
        mConfigManager.putCourseClass(type);
    }
    public String getCourseTitle() {
        return mConfigManager.getCourseTitle();
    }
    public int getCourseId() {
        return mConfigManager.getCourseId();
    }
    public void putCourseId(int parseInt,String courseTitle) {
        mConfigManager.putCourseId(parseInt);
        mConfigManager.putCourseTitle(courseTitle);
    }

    public void putWordCategory(int type) {
        mConfigManager.putWordCategory(type);
    }
    public int getWordType() {
        return mConfigManager.getWordType();
    }
    public void putWordType(int type) {
        mConfigManager.putWordType(type);
    }
    public int getWordClass() {
        return mConfigManager.getWordClass();
    }
    public void putWordClass(int type) {
        mConfigManager.putWordClass(type);
    }
    public String getWordTitle() {
        return mConfigManager.getWordTitle();
    }
    public int getWordId() {
        return mConfigManager.getWordId();
    }
    public void putWordId(int parseInt,String courseTitle) {
        mConfigManager.putWordId(parseInt);
        mConfigManager.putWordTitle(courseTitle);
    }

    public void deletCourses(int bookId) {
        List<Integer> mDatas  = mDataManager.getXiaoxueVoaIdsByBookId(bookId);
        List<Integer> mNotDeleteDatas =  WordDataBase.getInstance(TalkShowApplication.getInstance()).getTalkShowWordsDao().getVoasNotIn(bookId);
        Iterator<Integer> iterator = mDatas.iterator();
        while (iterator.hasNext()){
            int i = iterator.next() ;
            if (mNotDeleteDatas.contains(i)){
                iterator.remove();
            }
        }
        for (Integer voaid: mDatas) {
            File file = StorageUtil.getMediaDir(TalkShowApplication.getInstance(),voaid);
            FileUtils.deleteFile(file);
        }
        File sentenceAudioFolder = StorageUtil.getBookFolder(TalkShowApplication.getInstance(),bookId);

        if (sentenceAudioFolder.exists()){
            FileUtils.deleteFile(sentenceAudioFolder);
        }
        WordDataBase.getInstance(TalkShowApplication.getInstance()).getBookLevelDao().updateBookDownload(bookId,0);
        getMvpView().showToastLong("删除成功！");
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mLessonSub);
        RxUtil.unsubscribe(mSeriesSub);
        RxUtil.unsubscribe(mLoadSeriesSub);
    }

    /***********************************单词相关处理***************************/
    //单词的类型id
    public String getWordShowTypeId(){
        return mConfigManager.getWordShowTypeId();
    }

    public void setWordShowTypeId(String wordShowTypeId){
        mConfigManager.setWordShowTypeId(wordShowTypeId);
    }

    //单词的书籍名称
    public String getWordShowBookName(){
        return mConfigManager.getWordShowBookName();
    }

    public void setWordShowBookName(String wordBookName){
        mConfigManager.setWordShowBookName(wordBookName);
    }

    //单词的书籍id
    public int getWordShowBookId(){
        return mConfigManager.getWordShowBookId();
    }

    public void setWordShowBookId(int wordBookId){
        mConfigManager.setWordShowBookId(wordBookId);
    }
}