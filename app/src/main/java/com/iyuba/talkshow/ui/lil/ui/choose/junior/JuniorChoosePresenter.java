package com.iyuba.talkshow.ui.lil.ui.choose.junior;

import android.util.Pair;

import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.model.local.entity.BookEntity_junior;
import com.iyuba.lib_common.model.local.manager.JuniorEnLocalManager;
import com.iyuba.lib_common.model.remote.bean.App_check;
import com.iyuba.lib_common.model.remote.bean.Junior_book;
import com.iyuba.lib_common.model.remote.bean.Junior_type;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data_junior;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data_primary;
import com.iyuba.lib_common.model.remote.manager.JuniorEnRemoteManager;
import com.iyuba.lib_common.model.remote.manager.VerifyRemoteManager;
import com.iyuba.lib_common.model.remote.util.RemoteTransUtil;
import com.iyuba.lib_common.ui.mvp.BasePresenter;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.iyuba.talkshow.constant.ConfigData;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @title:
 * @date: 2023/5/22 09:31
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class JuniorChoosePresenter extends BasePresenter<JuniorChooseView> {

    //出版社数据
    private Disposable getPublishTypeDis;
    //书籍数据
    private Disposable getBookDataDis;

    //人教版审核接口数据
    private Disposable getRenVerifyDis;

    @Override
    public void detachView() {
        super.detachView();

        LibRxUtil.unDisposable(getPublishTypeDis);
        LibRxUtil.unDisposable(getBookDataDis);
        LibRxUtil.unDisposable(getRenVerifyDis);
    }

    //获取出版社数据
    public void getTypeData(String types){
        if (types.equals(TypeLibrary.BookType.junior_primary)){
            //小学
            getPrimaryTypeData();
        }else if (types.equals(TypeLibrary.BookType.junior_middle)){
            //初中
            getMiddleTypeData();
        }
    }

    //获取书籍数据
    public void getBookData(String category){
        List<BookEntity_junior> list = JuniorEnLocalManager.getBookFromDB(category);
        if (list!=null&&list.size()>0){
            getMvpView().showBookData(list);
        }else {
            getMvpView().refreshBookData();
        }
    }

    /*************远程接口************/
    //获取人教版审核接口数据
    public void getRenVerifyData(){
        checkViewAttach();
        LibRxUtil.unDisposable(getRenVerifyDis);

        int verifyId = ConfigData.getRenLimitChannelId(ChannelReaderUtil.getChannel(LibResUtil.getInstance().getContext()));
        VerifyRemoteManager.getVerifyData(verifyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<App_check>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getRenVerifyDis = d;
                    }

                    @Override
                    public void onNext(App_check response) {
                        if (getMvpView()!=null){
                            if (response.getResult().equals("0")){
                                AbilityControlManager.getInstance().setLimitPep(false);
                            }else {
                                AbilityControlManager.getInstance().setLimitPep(true);
                            }

                            getMvpView().showPepVerifyData(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            AbilityControlManager.getInstance().setLimitPep(false);
                            getMvpView().showPepVerifyData(false);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取小学的出版社数据
    private void getPrimaryTypeData(){
        checkViewAttach();
        LibRxUtil.unDisposable(getPublishTypeDis);
        JuniorEnRemoteManager.getPrimaryType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_data<BaseBean_data_primary<List<Junior_type>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getPublishTypeDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_data<BaseBean_data_primary<List<Junior_type>>> bean) {
                        if (getMvpView()!=null){
                            if (bean!=null
                                    &&bean.getResult()!=null
                                    &&bean.getResult().equals("200")){
                                if (bean.getData().getPrimary()!=null){
                                    //转化数据
                                    List<Pair<String,List<Pair<String,String>>>> list = RemoteTransUtil.transJuniorTypeData(bean.getData().getPrimary());
                                    //展示数据
                                    getMvpView().showTypeData(list);
                                }else {
                                    getMvpView().showTypeData(null);
                                }
                            }else {
                                getMvpView().showTypeData(null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showTypeData(null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取初中的出版社数据
    private void getMiddleTypeData(){
        checkViewAttach();
        LibRxUtil.unDisposable(getPublishTypeDis);
        JuniorEnRemoteManager.getMiddleType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_data<BaseBean_data_junior<List<Junior_type>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getPublishTypeDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_data<BaseBean_data_junior<List<Junior_type>>> bean) {
                        if (getMvpView()!=null){
                            if (bean!=null&&bean.getResult().equals("200")){
                                if (bean.getData().getJunior()!=null){
                                    //转化数据
                                    List<Pair<String,List<Pair<String,String>>>> list = RemoteTransUtil.transJuniorTypeData(bean.getData().getJunior());
                                    //展示数据
                                    getMvpView().showTypeData(list);
                                }else {
                                    getMvpView().showTypeData(null);
                                }
                            }else {
                                getMvpView().showTypeData(null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showTypeData(null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取中小学的书籍数据
    public void getJuniorNetBookData(String types,String category){
        checkViewAttach();
        LibRxUtil.unDisposable(getBookDataDis);
        JuniorEnRemoteManager.getJuniorBook(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_data<List<Junior_book>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getBookDataDis = d;
                    }

                    @Override
                    public void onNext(BaseBean_data<List<Junior_book>> bean) {
                        if (getMvpView()!=null){
                            if (bean.getResult().equals("1")){
                                //保存在数据库
                                JuniorEnLocalManager.saveBookToDB(RemoteTransUtil.transJuniorBookData(types,bean.getData()));
                                //从数据库取出
                                List<BookEntity_junior> list = JuniorEnLocalManager.getBookFromDB(category);
                                //展示数据
                                getMvpView().showBookData(list);
                            }else {
                                getMvpView().showBookData(null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView()!=null){
                            getMvpView().showBookData(null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
