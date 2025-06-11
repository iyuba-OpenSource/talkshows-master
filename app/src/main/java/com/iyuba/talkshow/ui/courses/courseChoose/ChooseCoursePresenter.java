package com.iyuba.talkshow.ui.courses.courseChoose;


import androidx.fragment.app.Fragment;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.NewSeriesDataResponse;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.SeriesResponse;
import com.iyuba.talkshow.injection.PerFragment;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.ui.lil.local.manager.TempDbManager;
import com.iyuba.talkshow.util.FileUtils;
import com.iyuba.talkshow.util.StorageUtil;
import com.iyuba.wordtest.db.WordDataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@PerFragment
public class ChooseCoursePresenter extends BasePresenter<ChooseCourseMVPView> {

    private final DataManager mDataManager;
    private final ConfigManager mConfigManager;

    private Subscription mSeriesSub;

    @Inject
    public ChooseCoursePresenter(ConfigManager configManager , DataManager mDataManager) {
        this.mConfigManager = configManager ;
        this.mDataManager = mDataManager;
    }

    //获取非初中的本地数据处理
    public void chooseCourse(String catId) {
        mSeriesSub = mDataManager.getSeriesList(catId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SeriesData>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<SeriesData> list) {
                        //本来想自行处理来着，但是返回的数据没有isVideo参数，李涛那边从接口处理了，这里不进行处理了
                        List<SeriesData> showList = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            SeriesData showData = list.get(i);

                            //不行，这里还需要判断下：因为课程和单词的选书是两个接口，单词的数据可能会被同步上来
                            if (showData.getIsVideo()!=null && showData.getIsVideo().equals("0")){
                                continue;
                            }

                            showList.add(showData);
                        }

                        //合并筛选的数据
                        getMvpView().setCoures(showList);
                    }
                });
    }

    //获取初中系列的数据(正式)
    public void chooseJuniorCourse(String catId) {
        //先从本地获取数据
        List<SeriesData> dbList = TempDbManager.getInstance().getMultiData(catId);
        if (dbList!=null && dbList.size()>0){
            List<SeriesData> showList = filterData(dbList);
            getMvpView().setCoures(showList);
        }else {
            chooseDataByRemote(true,catId);
        }
    }

    //获取非初中系列的数据(正式)
    public void choosePrimaryCourse(String catId){
        //先从本地获取数据，没有数据就从服务器获取数据
        List<SeriesData> dbList = TempDbManager.getInstance().getMultiData(catId);
        if (dbList!=null && dbList.size()>0){
            List<SeriesData> showList = filterData(dbList);
            getMvpView().setCoures(showList);
        }else {
            chooseDataByRemote(false,catId);
        }
    }

    //获取数据-远程
    public void chooseDataByRemote(boolean isJunior,String catId){
        if (isJunior){
            mSeriesSub = mDataManager.getSeriesList2(catId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NewSeriesDataResponse>(){
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(NewSeriesDataResponse tempBean) {
                            //转换数据
                            List<SeriesData> seriesDataList = new ArrayList<>();
                            if (tempBean.getData()!=null){
                                for (int i = 0; i < tempBean.getData().size(); i++) {
                                    NewSeriesDataResponse.DataBean titleSeries = tempBean.getData().get(i);

                                    //转换数据
                                    SeriesData seriesData = new SeriesData();
                                    seriesData.setDescCn(titleSeries.getDescCn());
                                    seriesData.setCategory(String.valueOf(titleSeries.getCategory()));
                                    seriesData.setSeriesName(titleSeries.getSeriesName());
                                    seriesData.setCreateTime(titleSeries.getCreateTime());
                                    seriesData.setUpdateTime(titleSeries.getUpdateTime());
                                    seriesData.setHotFlg(String.valueOf(titleSeries.getHotFlg()));
                                    seriesData.setId(String.valueOf(titleSeries.getId()));
                                    seriesData.setPic(titleSeries.getPic());
                                    seriesData.setKeyWords(titleSeries.getKeyWords());
                                    seriesData.setIsVideo(titleSeries.getIsVideo());

                                    //合并数据
                                    seriesDataList.add(seriesData);
                                }
                            }

                            //保存数据到本地
                            TempDbManager.getInstance().saveData(seriesDataList);
                            //筛选数据后显示
                            List<SeriesData> showList = filterData(seriesDataList);
                            getMvpView().setCoures(showList);
                        }
                    });
        }else {
            mSeriesSub = mDataManager.getCategorySeriesList(UserInfoManager.getInstance().getUserId(), catId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SeriesResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(SeriesResponse response) {
                            //转换成需要的数据
                            List<SeriesData> showList = new ArrayList<>();

                            if (response==null || response.getData()==null ||response.getData().size()==0){
                                getMvpView().setCoures(showList);
                                return;
                            }

                            //判断数据处理下
                            // TODO: 2024/10/29 当参数isVideo为0时，不显示视频内容
                            showList = filterData(response.getData());

                            //显示数据
                            getMvpView().setCoures(showList);
                            //将数据保存在本地
                            TempDbManager.getInstance().saveData(response.getData());
                        }
                    });
        }
    }

    //将isVideo的数据为0的数据关闭
    private List<SeriesData> filterData(List<SeriesData> oldList){
        List<SeriesData> showList = new ArrayList<>();

        if (oldList==null || oldList.size()==0){
            return showList;
        }

        for (int i = 0; i < oldList.size(); i++) {
            SeriesData seriesData = oldList.get(i);

            // TODO: 2024/10/28 这里判断数据：根据和李涛商议，使用isVideo=0判断是否显示书籍
            if (seriesData.getIsVideo()!=null && seriesData.getIsVideo().equals("0")){
                continue;
            }

            showList.add(seriesData);
        }

        return  showList;
    }

    public void putCourseId(int parseInt,String courseTitle) {
        mConfigManager.putCourseId(parseInt);
        mConfigManager.putCourseTitle(courseTitle);
    }


    public void refreshWords(int bookId, int version) {
        //这里不刷新单词，分开
//        EventBus.getDefault().post(new RefreshBookEvent(bookId,version));
    }

    public void deletCourses(int bookId) {
        List<Integer> mDatas  = mDataManager.getXiaoxueVoaIdsByBookId(bookId);
        List<Integer> mNotDeleteDatas =  WordDataBase.getInstance(((Fragment) getMvpView()).getActivity()).getTalkShowWordsDao().getVoasNotIn(bookId);
        Iterator<Integer> iterator = mDatas.iterator();
        while (iterator.hasNext()){
            int i = iterator.next() ;
            if (mNotDeleteDatas.contains(i)){
                iterator.remove();
            }
        }
        for (Integer voaid: mDatas) {
            File file = StorageUtil.getMediaDir(((Fragment) getMvpView()).getActivity(),voaid);
            FileUtils.deleteFile(file);
        }
        File sentenceAudioFolder = StorageUtil.getBookFolder(((Fragment) getMvpView()).getActivity(),bookId);

        if (sentenceAudioFolder.exists()){
            FileUtils.deleteFile(sentenceAudioFolder);
        }
        WordDataBase.getInstance(((Fragment) getMvpView()).getActivity()).getBookLevelDao().updateBookDownload(bookId,0);
        getMvpView().showToastLong("删除成功！");
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
