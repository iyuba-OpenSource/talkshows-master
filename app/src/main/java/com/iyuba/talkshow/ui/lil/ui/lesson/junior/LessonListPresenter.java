package com.iyuba.talkshow.ui.lil.ui.lesson.junior;

import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.model.local.entity.ChapterEntity_junior;
import com.iyuba.lib_common.model.local.manager.JuniorEnLocalManager;
import com.iyuba.lib_common.model.local.util.DBTransUtil;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.model.remote.manager.JuniorEnRemoteManager;
import com.iyuba.lib_common.model.remote.util.RemoteTransUtil;
import com.iyuba.lib_common.ui.mvp.BasePresenter;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_user.manager.UserInfoManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @title:
 * @date: 2024/1/2 14:25
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LessonListPresenter extends BasePresenter<LessonListView> {

    //获取课程数据
    private Disposable getLessonDis;

    @Override
    public void detachView() {
        super.detachView();

        LibRxUtil.unDisposable(getLessonDis);
    }

    //获取本地课程数据
    public void getLessonData(String types,String bookLevel,String bookId){
        if (getMvpView()==null){
            return;
        }

        switch (types){
            /********小说**********/
            /*case TypeLibrary.BookType.newCamstory://剑桥小说馆
            case TypeLibrary.BookType.newCamstoryColor://剑桥小说馆彩绘
            case TypeLibrary.BookType.bookworm://书虫英语
                List<ChapterEntity_novel> novelList = NovelDataManager.searchMultiChapterFromDB(types, bookLevel, bookId);
                Log.d("预存数据获取", "小说--"+novelList.size());
                if (novelList!=null&&novelList.size()>0){
                    getMvpView().showData(DBTransUtil.novelToChapterData(novelList));
                }else {
                    getMvpView().loadNetData();
                }
                break;*/
            /********中小学**********/
            case TypeLibrary.BookType.junior_primary://小学
            case TypeLibrary.BookType.junior_middle://初中
                List<ChapterEntity_junior> juniorList = JuniorEnLocalManager.getMultiChapterFromDB(bookId);
                if (juniorList!=null&&juniorList.size()>0){
                    getMvpView().showData(DBTransUtil.transJuniorChapterData(types,juniorList, UserInfoManager.getInstance().isVip()));
                }else {
                    getMvpView().loadNetData();
                }
                break;
        }
    }

    //获取网络课程数据
    public void loadNetChapterData(String types,String bookLevel,String bookId){
        if (getMvpView()==null){
            return;
        }

        switch (types){
            /********小说**********/
            /*case TypeLibrary.BookType.newCamstory://剑桥小说馆
            case TypeLibrary.BookType.newCamstoryColor://剑桥小说馆彩绘
            case TypeLibrary.BookType.bookworm://书虫英语
                loadNovelChapterData(bookLevel,bookId,types);
                break;*/
            /********中小学**********/
            case TypeLibrary.BookType.junior_primary://小学
                getJuniorPrimaryChapterData(types, bookId);
                break;
            case TypeLibrary.BookType.junior_middle://初中
                getJuniorMiddleChapterData(types, bookId);
                break;
        }
    }

    /********************************接口数据*****************************/
    //中小学-获取小学的章节数据(远程)
    private void getJuniorPrimaryChapterData(String types,String bookId){
        checkViewAttach();
        LibRxUtil.unDisposable(getLessonDis);
        JuniorEnRemoteManager.getPrimaryChapter(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_data<List<Junior_chapter>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getLessonDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_data<List<Junior_chapter>> bean) {
                        if (getMvpView()!=null){
                            if (bean!=null&&bean.getResult().equals("1")&&bean.getData()!=null){
                                //插入数据库
                                JuniorEnLocalManager.saveChapterToDB(RemoteTransUtil.transJuniorChapterData(types,bean.getData()));
                                //从数据库取出
                                List<ChapterEntity_junior> list = JuniorEnLocalManager.getMultiChapterFromDB(bookId);
                                //展示数据
                                getMvpView().showData(DBTransUtil.transJuniorChapterData(types,list,UserInfoManager.getInstance().isVip()));
                            }else {
                                getMvpView().showData(null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showData(null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //中小学-获取初中的章节数据(远程)
    private void getJuniorMiddleChapterData(String types,String bookId){
        checkViewAttach();
        LibRxUtil.unDisposable(getLessonDis);
        JuniorEnRemoteManager.getMiddleChapter(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_data<List<Junior_chapter>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getLessonDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_data<List<Junior_chapter>> bean) {
                        if (getMvpView()!=null){
                            if (bean!=null&&bean.getResult().equals("1")&&bean.getData()!=null){
                                //插入数据库
                                JuniorEnLocalManager.saveChapterToDB(RemoteTransUtil.transJuniorChapterData(types,bean.getData()));
                                //从数据库取出
                                List<ChapterEntity_junior> list = JuniorEnLocalManager.getMultiChapterFromDB(bookId);
                                //展示数据
                                getMvpView().showData(DBTransUtil.transJuniorChapterData(types,list,UserInfoManager.getInstance().isVip()));
                            }else {
                                getMvpView().showData(null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showData(null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
