package com.iyuba.talkshow.ui.lil.ui.lesson.study.content;

import android.text.TextUtils;
import android.util.Log;

import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.bean.ChapterDetailBean;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.model.local.entity.ChapterDetailEntity_junior;
import com.iyuba.lib_common.model.local.entity.ChapterEntity_junior;
import com.iyuba.lib_common.model.local.manager.JuniorEnLocalManager;
import com.iyuba.lib_common.model.local.util.DBTransUtil;
import com.iyuba.lib_common.model.remote.bean.Ad_result;
import com.iyuba.lib_common.model.remote.manager.AdRemoteManager;
import com.iyuba.lib_common.ui.mvp.BasePresenter;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.ui.lil.data.AdDataUtil;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.AdShowUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @title:
 * @date: 2024/1/4 10:03
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class ContentPresenter extends BasePresenter<ContentView> {

    //加载广告数据
    private Disposable getAdDataDis;

    @Override
    public void detachView() {
        super.detachView();

        LibRxUtil.unDisposable(getAdDataDis);
    }

    //加载章节数据
    public BookChapterBean getChapterData(String types, String voaId){
        if (TextUtils.isEmpty(types)){
            return null;
        }

        switch (types){
            case TypeLibrary.BookType.junior_primary://小学
            case TypeLibrary.BookType.junior_middle://初中
                //中小学
                ChapterEntity_junior junior = JuniorEnLocalManager.getSingleChapterFromDB(voaId);
                return DBTransUtil.transJuniorSingleChapterData(types,junior,UserInfoManager.getInstance().isVip());
            /*case TypeLibrary.BookType.bookworm://书虫
            case TypeLibrary.BookType.newCamstory://剑桥小说馆
            case TypeLibrary.BookType.newCamstoryColor://剑桥小说馆彩绘
                ChapterEntity_novel novel = NovelDataManager.searchSingleChapterFromDB(types, voaId);
                return DBTransUtil.novelToSingleChapterData(novel);*/
        }
        return null;
    }

    //加载章节详情数据
    public List<ChapterDetailBean> getChapterDetail(String types, String voaId){
        List<ChapterDetailBean> detailList = new ArrayList<>();
        if (TextUtils.isEmpty(types)){
            return detailList;
        }

        switch (types){
            case TypeLibrary.BookType.junior_primary://小学
            case TypeLibrary.BookType.junior_middle://初中
                //中小学
                List<ChapterDetailEntity_junior> juniorList = JuniorEnLocalManager.getMultiChapterDetailFromDB(voaId);
                if (juniorList!=null&&juniorList.size()>0){
                    detailList = DBTransUtil.transJuniorChapterDetailData(juniorList);
                }
            /*case TypeLibrary.BookType.bookworm://书虫
            case TypeLibrary.BookType.newCamstory://剑桥小说馆
            case TypeLibrary.BookType.newCamstoryColor://剑桥小说馆彩绘
                //小说
                List<ChapterDetailEntity_novel> novelList = NovelDataManager.searchMultiChapterDetailFromDB(types, voaId);
                if (novelList!=null&&novelList.size()>0){
                    detailList = DBTransUtil.novelToChapterDetailData(novelList);
                }*/
        }
        return detailList;
    }

    //获取广告数据
    public void getBannerAd(){
        checkViewAttach();
        LibRxUtil.unDisposable(getAdDataDis);
        AdRemoteManager.getAd(UserInfoManager.getInstance().getUserId(), 4, AdShowUtil.NetParam.AppId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Ad_result>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getAdDataDis = d;
                    }

                    @Override
                    public void onNext(List<Ad_result> list) {
                        if (getMvpView()!=null){
                            if (list==null||list.size()==0){
                                getMvpView().showWebSplashAD(null,null);
                                return;
                            }

                            Ad_result result = list.get(0);
                            if (!result.getResult().equals("1")){
                                getMvpView().showWebSplashAD(null,null);
                                return;
                            }

                            Ad_result.DataBean data = result.getData();
                            if (data==null){
                                getMvpView().showWebSplashAD(null,null);
                                return;
                            }

                            Log.d("广告显示", "类型--"+data.getType());

                            // TODO: 2023/9/14 展姐在[中小学英语书虫讨论组]中明确说明ads2使用共通广告模块显示
                            switch (data.getType()){
                                case AdDataUtil.AD_Youdao:
                                    getMvpView().showYoudaoSplashAD(data.getStartuppic(),data.getStartuppic_Url());
                                    break;
                                case AdDataUtil.AD_Web:
                                    getMvpView().showWebSplashAD(data.getStartuppic(),data.getStartuppic_Url());
                                    break;
                                case AdDataUtil.AD_Ads1:
                                case AdDataUtil.AD_Ads2:
                                case AdDataUtil.AD_Ads3:
                                case AdDataUtil.AD_Ads4:
                                case AdDataUtil.AD_Ads5:
                                    getMvpView().showIyubaSdkAD(data.getStartuppic(),data.getStartuppic_Url());
                                    break;
                                default:
                                    getMvpView().showWebSplashAD(data.getStartuppic(),data.getStartuppic_Url());
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showWebSplashAD(null,null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
