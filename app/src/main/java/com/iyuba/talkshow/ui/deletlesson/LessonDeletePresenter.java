package com.iyuba.talkshow.ui.deletlesson;

import android.content.Context;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.FileUtils;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.StorageUtil;
import com.iyuba.wordtest.db.WordDataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class LessonDeletePresenter extends BasePresenter<LessonDeleteMVPView> {

    private final DataManager mDataManager;

    @Inject
    public LessonDeletePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    public void deleteLessons(int bookId) {
        List<Integer> mDatas = mDataManager.getXiaoxueVoaIdsByBookId(bookId);
        List<Integer> mNotDeleteDatas = WordDataBase.getInstance((Context) getMvpView()).getTalkShowWordsDao().getVoasNotIn(bookId);
        Iterator<Integer> iterator = mDatas.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            if (mNotDeleteDatas.contains(i)) {
                iterator.remove();
            }
        }
        for (Integer voaid : mDatas) {
            File file = StorageUtil.getMediaDir((Context) getMvpView(), voaid);
            FileUtils.deleteFile(file);
        }
        File sentenceAudioFolder = StorageUtil.getBookFolder((Context) getMvpView(), bookId);

        if (sentenceAudioFolder.exists()) {
            FileUtils.deleteFile(sentenceAudioFolder);
        }
        WordDataBase.getInstance((Context) getMvpView()).getBookLevelDao().updateBookDownload(bookId, 0);
        getMvpView().showDeleteMessage("删除成功！");
    }

    public void getDownloadedClass() {
        List<Integer> lessonIds = WordDataBase.getInstance((Context) getMvpView()).getBookLevelDao().getDownloaded();
        if (lessonIds.size() == 0) {
            getMvpView().showBookList(null);
        }else {
            mDataManager.getSeriesListByIds(lessonIds)
                    .compose(RxUtil.io2main())
                    .subscribe(new Subscriber<List<SeriesData>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<SeriesData> seriesData) {
                            if (seriesData != null && seriesData.size() > 0) {
                                getMvpView().showBookList(seriesData);
                            } else {
                                getMvpView().showBookList(null);
                            }
                        }
                    });
        }
    }
}
