package com.iyuba.talkshow.ui.courses.coursedetail;

import android.util.Log;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.TitleSeries;
import com.iyuba.talkshow.data.model.TitleSeriesResponse;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class CourseDetailPresenter extends BasePresenter<CourseDetailMVPView> {

    private Subscription mSeriesSub;
    private final DataManager dataManager;

    @Inject
    CourseDetailPresenter(DataManager dataManager ){
        this.dataManager = dataManager ;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mSeriesSub);
    }

    public void getVoaSeries(int series) {
        checkViewAttached();
        RxUtil.unsubscribe(mSeriesSub);
        mSeriesSub = dataManager.getTitleSeriesList(series)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TitleSeriesResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("MainFragPresenter", "getVoaSeries onError  " + e.getMessage());
                        }
                        getVoas(series);
                    }

                    @Override
                    public void onNext(TitleSeriesResponse response) {
                        if (response == null || response.getData() == null) {
                            getVoas(series);
                            return;
                        }

                        //处理数据
                        List<TitleSeries> seriesData = response.getData();
                        List<Voa> voaData = new ArrayList<>();
                        for (TitleSeries series: seriesData) {
                            try {
                                voaData.add(Series2Voa(series));
                            } catch (Exception var2) {
                                var2.printStackTrace();
                            }
                        }

                        //展示数据
                        getMvpView().showCourses(voaData);

                        //插入数据
                        TalkShowApplication.getSubHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                for (Voa series: voaData) {
                                    long result = dataManager.insertVoaDB(series);
                                }
                            }
                        });
                    }
                });
    }

    public static Voa Series2Voa(TitleSeries series) {
        if (series == null) {
            return null;
        }
        return Voa.builder().setUrl(series.Sound).setPic(series.Pic).setTitle(series.Title).setTitleCn(series.Title_cn)
                .setVoaId(series.Id).setCategory(series.Category).setDescCn(series.DescCn).setSeries(series.series)
                .setCreateTime(series.CreatTime).setPublishTime(series.PublishTime).setHotFlag(series.HotFlg).setReadCount(series.ReadCount)
                .setSound(series.Sound.replace("http://staticvip."+ Constant.Web.WEB_SUFFIX.replace("/","") +"/sounds/voa", ""))
                .setIntroDesc(series.IntroDesc).setPageTitle(series.Title).setKeyword(series.Keyword)
                //增加video
                .setVideo(series.video)
                .build();
    }

    public void  getVoas(int cat){
        checkViewAttached();
        RxUtil.unsubscribe(mSeriesSub);
        mSeriesSub = dataManager.getCoursesVoas(cat).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<List<Voa>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                        getMvpView().showToastShort(R.string.database_error);
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        getMvpView().showCourses(voas);
                    }

                });
    }
}
