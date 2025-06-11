//package com.iyuba.talkshow.ui.lil.ui.newChoose;
//
//import com.iyuba.lib_common.model.remote.bean.Junior_book;
//import com.iyuba.lib_common.model.remote.bean.Junior_type;
//import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
//import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data_primary;
//import com.iyuba.lib_common.model.remote.manager.JuniorEnRemoteManager;
//import com.iyuba.lib_common.ui.mvp.BasePresenter;
//import com.iyuba.lib_common.util.LibRxUtil;
//import com.iyuba.talkshow.data.model.SeriesData;
//import com.iyuba.talkshow.ui.lil.local.manager.TempDbManager;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.BookShowBean;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.TypeShowBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.Observable;
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//public class BookChoosePresenter extends BasePresenter<BookChooseView> {
//
//    //类型操作
//    private Disposable typeDis;
//    //书籍操作
//    private Disposable bookDis;
//
//    @Override
//    public void detachView() {
//        super.detachView();
//
//        LibRxUtil.unDisposable(typeDis);
//        LibRxUtil.unDisposable(bookDis);
//    }
//
//    //加载类型数据-课程
//    public void loadLessonTypeData(){
//        List<TypeShowBean> typeList = new ArrayList<>();
//        //人教版
//        List<TypeShowBean.SmallTypeBean> renSmallTypeList = new ArrayList<>();
//        renSmallTypeList.add(new TypeShowBean.SmallTypeBean("313","新起点"));
//        renSmallTypeList.add(new TypeShowBean.SmallTypeBean("314","PEP"));
//        renSmallTypeList.add(new TypeShowBean.SmallTypeBean("315","精通"));
//        typeList.add(new TypeShowBean("人教版", renSmallTypeList));
//
//        //北师版
//        List<TypeShowBean.SmallTypeBean> beiSmallTypeList = new ArrayList<>();
//        beiSmallTypeList.add(new TypeShowBean.SmallTypeBean("320","一起点"));
//        beiSmallTypeList.add(new TypeShowBean.SmallTypeBean("319","三起点"));
//        typeList.add(new TypeShowBean("北师版", beiSmallTypeList));
//
//        //初中
//        List<TypeShowBean.SmallTypeBean> chuSmallTypeList = new ArrayList<>();
//        chuSmallTypeList.add(new TypeShowBean.SmallTypeBean("316","人教版"));
//        typeList.add(new TypeShowBean("初中", chuSmallTypeList));
//
//        getMvpView().showTypeData(typeList);
//    }
//
//    //加载类型数据-单词(小学)
//    public void loadWordTypeData(){
//        JuniorEnRemoteManager.getPrimaryType()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseBean_data<BaseBean_data_primary<List<Junior_type>>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        typeDis = d;
//                    }
//
//                    @Override
//                    public void onNext(BaseBean_data<BaseBean_data_primary<List<Junior_type>>> data) {
//                        if (data!=null && data.getResult().equals("200")){
//                            //转换数据显示
//                            List<TypeShowBean> showList = transTypeList(data.getData().getPrimary());
//                            //回调数据
//                            getMvpView().showTypeData(showList);
//                        }else {
//                            getMvpView().showFail("暂无类型数据");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        getMvpView().showError("类型数据获取异常");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//    //加载书籍数据-远程
//    public void loadBookDataByRemote(String typeId){
//        JuniorEnRemoteManager.getJuniorBook(typeId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseBean_data<List<Junior_book>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        bookDis = d;
//                    }
//
//                    @Override
//                    public void onNext(BaseBean_data<List<Junior_book>> data) {
//                        if (data!=null && data.getResult().equals("1")){
//                            //保存在数据库
//                            TempDbManager.getInstance().saveDataNew(data.getData());
//                            //转换数据显示
//                            List<BookShowBean> showList = transBookList(data.getData());
//                            //回调数据
//                            getMvpView().showCourseData(showList);
//                        }else {
//                            getMvpView().showFail("未查询到书籍数据");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        getMvpView().showError("查询书籍数据异常");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//    //加载数据数据
//    public void loadBookData(String typeId){
//        //先从数据库中获取数据
//        List<BookShowBean> dbList = TempDbManager.getInstance().getMultiDataNew(typeId);
//        if (dbList!=null && dbList.size()>0){
//            getMvpView().showCourseData(dbList);
//        }else {
//            loadBookDataByRemote(typeId);
//        }
//    }
//
//    //转换类型显示
//    private List<TypeShowBean> transTypeList(List<Junior_type> list){
//        List<TypeShowBean> showList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            Junior_type typeData = list.get(i);
//
//            List<TypeShowBean.SmallTypeBean> smallList = new ArrayList<>();
//            for (int j = 0; j < typeData.getSeriesData().size(); j++) {
//                Junior_type.SeriesDataBean smallData = typeData.getSeriesData().get(j);
//                smallList.add(new TypeShowBean.SmallTypeBean(
//                        smallData.getCategory(),
//                        smallData.getSeriesName()
//                ));
//            }
//            showList.add(new TypeShowBean(
//                    typeData.getSourceType(),
//                    smallList
//            ));
//        }
//        return showList;
//    }
//
//    //转换书籍显示
//    private List<BookShowBean> transBookList(List<Junior_book> list){
//        List<BookShowBean> showList = new ArrayList<>();
//
//        for (int i = 0; i < list.size(); i++) {
//            Junior_book bookData = list.get(i);
//
//            showList.add(new BookShowBean(
//                    bookData.getId(),
//                    bookData.getSeriesName(),
//                    bookData.getPic(),
//                    bookData.getIsVideo()
//            ));
//        }
//
//        return showList;
//    }
//}
