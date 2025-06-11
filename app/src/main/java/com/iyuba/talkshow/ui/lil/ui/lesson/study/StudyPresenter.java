package com.iyuba.talkshow.ui.lil.ui.lesson.study;

import android.text.TextUtils;

import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.event.RefreshDataEvent;
import com.iyuba.lib_common.model.local.entity.ChapterDetailEntity_junior;
import com.iyuba.lib_common.model.local.entity.ChapterEntity_junior;
import com.iyuba.lib_common.model.local.manager.JuniorEnLocalManager;
import com.iyuba.lib_common.model.local.util.DBTransUtil;
import com.iyuba.lib_common.model.remote.bean.Collect_chapter;
import com.iyuba.lib_common.model.remote.bean.Junior_chapter_detail;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_textDetail;
import com.iyuba.lib_common.model.remote.manager.JuniorEnRemoteManager;
import com.iyuba.lib_common.model.remote.util.RemoteTransUtil;
import com.iyuba.lib_common.ui.mvp.BasePresenter;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_common.util.LibToastUtil;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.sdk.other.NetworkUtil;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.TalkShowWords;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @title:
 * @date: 2024/1/3 15:12
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class StudyPresenter extends BasePresenter<StudyView> {

    //获取详情数据
    private Disposable getDetailDataDis;
    //收藏/取消收藏操作
    private Disposable collectChapterDis;

    @Override
    public void detachView() {
        super.detachView();

        LibRxUtil.unDisposable(getDetailDataDis);
        LibRxUtil.unDisposable(collectChapterDis);
    }

    //获取当前章节是否存在单词
    public boolean isExistWord(String types,String bookId,String voaId){
        if (TextUtils.isEmpty(types)){
            return false;
        }

        switch (types) {
            case TypeLibrary.BookType.junior_primary:
            case TypeLibrary.BookType.junior_middle:
                //中小学
                List<TalkShowWords> juniorList = WordDataBase.getInstance(LibResUtil.getInstance().getContext()).getTalkShowWordsDao().getUnitByVoa(Integer.parseInt(bookId),Integer.parseInt(voaId));
                if (juniorList!=null&&juniorList.size()>0){
                    return true;
                }
                break;
        }
        return false;
    }

    //获取当前章节的数据
    public BookChapterBean getChapterData(String types, String voaId){
        if (TextUtils.isEmpty(types)){
            return null;
        }

        switch (types){
            case TypeLibrary.BookType.junior_primary://小学
            case TypeLibrary.BookType.junior_middle://初中
                //中小学
                ChapterEntity_junior junior = JuniorEnLocalManager.getSingleChapterFromDB(voaId);
                return DBTransUtil.transJuniorSingleChapterData(types,junior, UserInfoManager.getInstance().isVip());
            /*case TypeLibrary.BookType.bookworm://书虫
            case TypeLibrary.BookType.newCamstory://剑桥小说馆
            case TypeLibrary.BookType.newCamstoryColor://剑桥小说馆彩绘
                //小说
                ChapterEntity_novel novel = NovelDataManager.searchSingleChapterFromDB(types, voaId);
                return DBTransUtil.novelToSingleChapterData(novel);*/
        }
        return null;
    }

    //获取当前章节的详情数据
    public void getChapterDetail(String types,String bookId,String voaId){
        switch (types){
            case TypeLibrary.BookType.junior_primary://小学
            case TypeLibrary.BookType.junior_middle://初中
                //中小学
                List<ChapterDetailEntity_junior> juniorList = JuniorEnLocalManager.getMultiChapterDetailFromDB(voaId);
                if (juniorList!=null&&juniorList.size()>0){
                    getMvpView().showData(DBTransUtil.transJuniorChapterDetailData(juniorList));
                }else {
                    if (!NetworkUtil.isConnected(LibResUtil.getInstance().getContext())){
                        LibToastUtil.showToast(LibResUtil.getInstance().getContext(), "请链接网络后重试~");
                        return;
                    }

                    getMvpView().showLoading("正在加载详情内容~");
                    loadJuniorChapterDetail(types, bookId, voaId);
                }
                break;
            /*case TypeLibrary.BookType.bookworm://书虫
            case TypeLibrary.BookType.newCamstory://剑桥小说馆
            case TypeLibrary.BookType.newCamstoryColor://剑桥小说馆彩绘
                //小说
                List<ChapterDetailEntity_novel> novelList = NovelDataManager.searchMultiChapterDetailFromDB(types,voaId);
                if (novelList!=null&&novelList.size()>0){
                    getMvpView().showData(DBTransUtil.novelToChapterDetailData(novelList));
                }else {
                    if (!NetworkUtil.isConnected(ResUtil.getInstance().getContext())){
                        ToastUtil.showToast(ResUtil.getInstance().getContext(), "请链接网络后重试~");
                        return;
                    }

                    getMvpView().showLoading("正在加载详情内容~");
                    loadNovelChapterDetailData(types, voaId);
                }
                break;*/
        }
    }

    /*****************************************详情数据*****************************/
    //获取中小学的章节详情数据
    private void loadJuniorChapterDetail(String types,String bookId,String voaId){
        checkViewAttach();
        LibRxUtil.unDisposable(getDetailDataDis);
        JuniorEnRemoteManager.getJuniorChapterDetail(voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_textDetail<List<Junior_chapter_detail>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getDetailDataDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_textDetail<List<Junior_chapter_detail>> bean) {
                        if (getMvpView()!=null){
                            if (bean!=null&&bean.getVoatext()!=null){
                                //保存在数据库
                                JuniorEnLocalManager.saveChapterDetailToDB(RemoteTransUtil.transJuniorChapterDetailData(types,bookId,voaId,bean.getVoatext()));
                                //从数据库取出
                                List<ChapterDetailEntity_junior> list = JuniorEnLocalManager.getMultiChapterDetailFromDB(voaId);
                                //展示数据
                                getMvpView().showData(DBTransUtil.transJuniorChapterDetailData(list));
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

    //获取小说的章节详情数据
    /*private void loadNovelChapterDetailData(String types,String voaId){
        checkViewAttach();
        RxUtil.unDisposable(novelChapterDetailDis);
        NovelDataManager.getChapterDetailData(types, voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_bookInfo_texts<Novel_book, List<Novel_chapter_detail>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        novelChapterDetailDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_bookInfo_texts<Novel_book, List<Novel_chapter_detail>> bean) {
                        if (getMvpView()!=null) {
                            if (bean!=null&&bean.getResult() == 200) {
                                //保存在本地
                                NovelDataManager.saveChapterDetailToDB(RemoteTransUtil.transNovelChapterDetailToDB(types,bean.getTexts()));
                                //从本地获取数据
                                List<ChapterDetailEntity_novel> list = NovelDataManager.searchMultiChapterDetailFromDB(types, voaId);
                                //显示在界面上
                                getMvpView().showData(DBTransUtil.novelToChapterDetailData(list));
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
    }*/

    /****************************************收藏/取消收藏****************************/
    //收藏/取消收藏文章
    public void collectArticle(String types,String voaId,String userId,boolean isCollect){
        if (TextUtils.isEmpty(types)){
            LibToastUtil.showToast(LibResUtil.getInstance().getContext(), "暂无该类型数据");
            return;
        }

        switch (types){
            case TypeLibrary.BookType.junior_primary:
            case TypeLibrary.BookType.junior_middle:
                //中小学
                collectJuniorArticle(types, voaId, userId, isCollect);
                break;
            /*case TypeLibrary.BookType.bookworm:
            case TypeLibrary.BookType.newCamstory:
            case TypeLibrary.BookType.newCamstoryColor:
                //小说
                collectNovelArticle(types, voaId, userId, isCollect);
                break;*/
        }
    }

    //中小学-收藏/取消收藏文章
    public void collectJuniorArticle(String types,String voaId,String userId,boolean isCollect){
        checkViewAttach();
        LibRxUtil.unDisposable(collectChapterDis);
        JuniorEnRemoteManager.collectArticle(types,userId,voaId,isCollect)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Collect_chapter>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        collectChapterDis = d;
                    }

                    @Override
                    public void onNext(Collect_chapter bean) {
                        if (getMvpView()!=null){
                            if (bean!=null&&bean.msg.equals("Success")){
                                getMvpView().showCollectArticle(true,isCollect);
                                //刷新收藏界面回调
                                EventBus.getDefault().post(new RefreshDataEvent(TypeLibrary.RefreshDataType.junior_lesson_collect));
                            }else {
                                getMvpView().showCollectArticle(false,isCollect);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showCollectArticle(false,false);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //小说-收藏/取消收藏文章
    /*public void collectNovelArticle(String types,String voaId,String userId,boolean isCollect){
        checkViewAttach();
        LibRxUtil.unDisposable(collectChapterDis);
        NovelDataManager.collectArticle(types, voaId, userId, isCollect)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Collect_chapter>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        collectChapterDis = d;
                    }

                    @Override
                    public void onNext(Collect_chapter bean) {
                        if (getMvpView()!=null){
                            if (bean!=null&&bean.msg.equals("Success")){
                                getMvpView().showCollectArticle(true,isCollect);
                                //刷新收藏界面回调
                                EventBus.getDefault().post(new RefreshDataEvent(TypeLibrary.RefreshDataType.novel_lesson_collect));
                            }else {
                                getMvpView().showCollectArticle(false,isCollect);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showCollectArticle(false,false);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }*/
}
