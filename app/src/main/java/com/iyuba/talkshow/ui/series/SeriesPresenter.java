package com.iyuba.talkshow.ui.series;

import android.text.TextUtils;
import android.util.Log;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class SeriesPresenter extends BasePresenter<SeriesMVPView> {

    private final DataManager mDataManager;
    private Subscription mSeriesSub;
    private Subscription mSeriesSync;
    private Subscription mSeriesList;

    @Inject
    public SeriesPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mSeriesSub);
        RxUtil.unsubscribe(mSeriesSync);
        RxUtil.unsubscribe(mSeriesList);
    }

    public void getSeries(String key, String category) {
        Log.e("SeriesActivity", "getSeries seriesId " + key);
        getMvpView().showLoading();
        RxUtil.unsubscribe(mSeriesSub);
        mSeriesSub = mDataManager.getSeriesLocal(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Voa>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                            syncSeries(key, category);
                        } else {
                            getMvpView().dismissLoading();
                            if (e != null) {
                                e.printStackTrace();
                            }
                            getMvpView().setVoas(new ArrayList<>(), key);
//                            getMvpView().showToastShort("请求失败");
                        }
                    }

                    @Override
                    public void onNext(List<Voa> voaList) {
                        if ((voaList == null) || voaList.isEmpty()) {
                            Log.e("SeriesActivity", "getSeries onNext is null. ");
                            if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                                syncSeries(key, category);
                                return;
                            }
                        }
                        getMvpView().setVoas(voaList , key);
                        getMvpView().dismissLoading();
                    }
                });

    }
    protected void syncSeries(String key, String ids) {
        checkViewAttached();
        RxUtil.unsubscribe(mSeriesSync);
        mSeriesSync = mDataManager.getVoa4Category(ids, UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
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
                        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                            getMvpView().showToastShort(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_more_data);
                        }
                        getMvpView().dismissLoading();
                        getMvpView().setVoas(new ArrayList<>(), key);
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        List<Voa> voaList = new ArrayList<>();
                        if ((voas != null) && voas.size() > 0) {
                            Log.e("SeriesActivity", "getVoa4Category voa.size " + voas.size());
                            for (Voa va: voas) {
                                if ((va != null) && key.equals(va.series() + "")) {
                                    voaList.add(va);
                                }
                            }
                        }
                        Collections.sort(voaList, new Comparator<Voa>() {
                            @Override
                            public int compare(Voa o1, Voa o2) {
                                return o2.voaId() - o1.voaId();
                            }
                        });
                        Log.e("SeriesActivity", "getVoa4Category voaList.size " + voaList.size());
                        getMvpView().setVoas(voaList , key);
                        getMvpView().dismissLoading();
                    }
                });
    }

    public void getSeriesList(String cat) {
        checkViewAttached();
        RxUtil.unsubscribe(mSeriesList);
        mSeriesList = mDataManager.getSeriesList(cat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SeriesData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dismissLoading();
                        e.printStackTrace();
                        getMvpView().showToastShort("请求失败");
                    }

                    @Override
                    public void onNext(List<SeriesData> list) {
                        if (list == null) {
                            Log.e("SeriesActivity", "getSeriesList is null? ");
                            list = new ArrayList<>();
                        } else {
                            if ((App.APP_ID == 280) && App.DEFAULT_SERIESID.equals(cat)) {
                                Filter4Pig(list);
                            }
                        }
                        getMvpView().setChoise(list);
                        getMvpView().dismissLoading();
                    }
                });

    }

    public static void Filter4Pig(List<SeriesData> dataList) {
        if ((dataList == null) || dataList.isEmpty()) {
            return;
        }
        Iterator<SeriesData> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            SeriesData series = iterator.next();
            if ((series == null) || TextUtils.isEmpty(series.getId())) {
                iterator.remove();
                continue;
            }
            int id = Integer.parseInt(series.getId());
            if ((201 == id) || (id >= 459)) {
                continue;
            } else {
                iterator.remove();
            }
        }
    }

}
