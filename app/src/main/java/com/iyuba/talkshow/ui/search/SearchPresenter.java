package com.iyuba.talkshow.ui.search;

import android.text.TextUtils;

import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.DataManager;
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

/**
 * SearchPresenter
 *
 * @author wayne
 * @date 2018/2/10
 */
@ConfigPersistent
public class SearchPresenter extends BasePresenter<SearchMvpView> {
    private final DataManager mDataManager;
    private int total = 15;
    private int start = 1;
    String key;


    private Subscription mSearchSub;

    @Inject
    public SearchPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mSearchSub);
    }

    public void searchVoa(String key) {
        this.key = key;
        RxUtil.unsubscribe(mSearchSub);
        getMvpView().showLoading();
        mSearchSub = mDataManager.searchVoa(key, start, total, App.APP_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchListBean>() {
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
                    public void onNext(SearchListBean searchListBean) {
                        getMvpView().dismissLoading();
                        try {
                            int total = Integer.parseInt(searchListBean.getTotal());
                            if (total > 0) {
                                start += 1;

                                //这里根据要求处理下数据
                                List<Voa> showList = toVoa(searchListBean.getData());
                                if (ConfigData.isOppoCopyrightLimit(LibResUtil.getInstance().getContext())){
                                    List<Voa> tempList = new ArrayList<>();
                                    for (int i = 0; i < showList.size(); i++) {
                                        Voa temp = showList.get(i);
                                        if (temp.category() != 309){
                                            tempList.add(temp);
                                        }
                                    }
                                    if (tempList.size()>0){
                                        getMvpView().showVoas(tempList);
                                    }else {
                                        getMvpView().showEmptyVoas();
                                    }
                                }else {
                                    getMvpView().showVoas(showList);
                                }
                            } else {
                                getMvpView().showEmptyVoas();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            getMvpView().showEmptyVoas();
                        }
                    }
                });

    }

    private List<Voa> toVoa(List<SearchListBean.DataBean> data) {
        List<Voa> list = new ArrayList<>();

        if (data == null) {
            return list;
        }
        for (SearchListBean.DataBean bean : data) {
            try {

                list.add(Voa.builder()
                        .setVoaId(Integer.parseInt(bean.getVoaId()))
                        .setCategory(Integer.parseInt(bean.getCategory()))
                        .setCreateTime(bean.getCreatTime())
                        .setPublishTime(bean.getPublishTime())
                        .setDescCn(bean.getDescCn())
                        .setHotFlag(Integer.parseInt(bean.getHotFlg()))
                        .setPic(bean.getPic())
                        .setSound(bean.getSound())
                        .setTitleCn(bean.getTitle_cn())
                        .setTitle(bean.getTitle())
                        .setIntroDesc("")
                        .setPageTitle("")
                        .setSeries(0)
                        .setUrl("")
                        .setReadCount(Integer.parseInt(bean.getReadCount()))
                        //增加video
                        .setVideo(bean.getVideo())
                        .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void search(String key) {
        if (App.APP_ID == 280) {
            searchLocal(key);
            return;
        }
//        if (this.key != null && this.key.equals(key)) {
//            return;
//        }
        getMvpView().resetAd();
        start = 1;
        searchVoa(key);
    }

    public void searchLocal(String key) {
        this.key = key;
        RxUtil.unsubscribe(mSearchSub);
        getMvpView().showLoading();
        mSearchSub = mDataManager.searchLocal(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Voa>>() {
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
                    public void onNext(List<Voa> searchListBean) {
                        getMvpView().dismissLoading();
                        if ((searchListBean == null) || searchListBean.isEmpty()) {
                            getMvpView().showEmptyVoas();
                        } else {
                            getMvpView().showVoas(searchListBean);
                        }
                    }
                });
    }

    public void loadMore() {
        if (TextUtils.isEmpty(key)) {
            getMvpView().dismissLoading();
            return;
        }
        searchVoa(key);
    }
}
