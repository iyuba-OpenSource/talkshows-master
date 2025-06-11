package com.iyuba.talkshow.ui.user.download;

import android.content.Context;
import android.util.Log;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.Download;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.StorageUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/27/027.
 */

@ConfigPersistent
public class DownloadPresenter extends BasePresenter<DownloadMvpView> {

    private final DataManager mDataManager;

    private Subscription mGetDownloadSub;
    private Subscription mDeleteDownloadSub;

    private Context mContext;

    @Inject
    public DownloadPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mGetDownloadSub);
        RxUtil.unsubscribe(mDeleteDownloadSub);
    }

    public void getDownload() {
        checkViewAttached();
        RxUtil.unsubscribe(mGetDownloadSub);
        mGetDownloadSub = mDataManager.getDownload(UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Download>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().showToast(R.string.database_error);
                    }

                    @Override
                    public void onNext(List<Download> downloads) {
                        getMvpView().dismissLoadingLayout();
                        if (downloads.isEmpty()) {
                            getMvpView().setAdapterEmpty();
                        } else {
//                            List<Download> list = filterData(downloads);
                            List<Download> list = downloads;
                            if (list.isEmpty()) {
                                getMvpView().setAdapterEmpty();
                            } else {
                                getMvpView().setAdapterData(list);
                            }
                        }
                    }
                });
    }

    private List<Download> filterData(List<Download> downloads) {
        List<Download> list = new ArrayList<>();
        List<String> delete = new ArrayList<>();
        for (Download download : downloads) {
            try {
                String mDir = StorageUtil
                        .getMediaDir(getMvpView().getContextt(), download.voaId())
                        .getAbsolutePath();
                if (StorageUtil.checkFileExist(mDir, download.voaId())) {
                    list.add(download);
                } else {
                    delete.add(String.valueOf(download.id()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!delete.isEmpty()) {
            mDataManager.deleteDownload(delete)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            Log.e("delete", "download voa id ");
                        }
                    });
        }
        return list;
    }

    public void deleteDownload(final List<String> list) {
        checkViewAttached();
        RxUtil.unsubscribe(mDeleteDownloadSub);
        mDeleteDownloadSub = mDataManager.deleteDownload(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().showToast(R.string.database_error);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getDownload();
                    }
                });
    }

    private void deleteDownloadFile(List<String> list) {
        for (String voaId : list) {
            StorageUtil.deleteAudioFile(mContext, Integer.valueOf(voaId));
            StorageUtil.deleteVideoFile(mContext, Integer.valueOf(voaId));
        }
    }
}
