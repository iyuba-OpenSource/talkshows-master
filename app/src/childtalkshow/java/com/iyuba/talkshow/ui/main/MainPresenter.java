package com.iyuba.talkshow.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.AdManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.manager.VersionManager;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.EnterGroup;
import com.iyuba.talkshow.data.model.Header;
import com.iyuba.talkshow.data.model.Level;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.SeriesResponse;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.remote.LoopService;
import com.iyuba.talkshow.data.remote.VoaService;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.BrandUtil;
import com.iyuba.talkshow.util.GetLocation;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.aigestudio.downloader.bizs.DLManager;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private final AdManager mAdManager;
    private final DLManager mDLManager;
    private final VersionManager mVersionManager;
    private final GetLocation mGetLocation;
    private final ConfigManager mConfigManager;

    private Subscription mLoadNewSub;
    private Subscription mLoadSub;
    private Subscription mLoadLoopSub;
    private Subscription mGetWelcomeSub;
    private Subscription mLoginSub;
    private Subscription mGetVoaByIdSub;
    private Subscription mGetMoreVoaSub;
    private Subscription mGetVoa4Ids;

    //获取大批量数据（口语秀、配音秀和少儿英语）[原有接口对小猪英语和小猪佩奇可用，暂时不改]
    private Subscription mLoadNewSub2;

    private static DateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
    private Subscription mLoadSeriesSub;

    @Inject
    public MainPresenter(DataManager dataManager, DLManager dlManager,ConfigManager configManager,
                         AdManager adManager,
                         GetLocation getLocation, VersionManager versionManager) {
        mDataManager = dataManager;
        mDLManager = dlManager;
        mConfigManager = configManager;
        mAdManager = adManager;
        mGetLocation = getLocation;
        mVersionManager = versionManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mLoginSub);
        RxUtil.unsubscribe(mLoadNewSub);
        RxUtil.unsubscribe(mLoadSub);
        RxUtil.unsubscribe(mLoadLoopSub);
        RxUtil.unsubscribe(mGetWelcomeSub);
        RxUtil.unsubscribe(mGetVoaByIdSub);
        RxUtil.unsubscribe(mGetMoreVoaSub);
        RxUtil.unsubscribe(mLoadSeriesSub);
        RxUtil.unsubscribe(mGetVoa4Ids);
    }
    public void getVoas() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSub);
        mLoadSub = mDataManager.getXiaoxue()
                .subscribeOn(Schedulers.io())
                .flatMap(voas -> mDataManager.getChildHomeVoas())
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
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_more_data);
                            loadVoas();
                        }
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        if ((voas == null) || (voas.size() < 4)) {
                            if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                                getMvpView().showReloadView();
                            } else {
                                loadVoas();
                            }
                        } else {
                            getMvpView().showVoas(voas);
                        }
                    }
                });
    }

    /*private void loadNewVoas(int maxId) {
        Log.e("loadNewVoas", "执行了！");
        checkViewAttached();
        RxUtil.unsubscribe(mLoadNewSub);
        mLoadNewSub = mDataManager.syncVoas(maxId, mAccountManager.getUid())
                .subscribeOn(Schedulers.io())
                .flatMap(voas -> mDataManager.getChildHomeVoas())
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
                        getMvpView().dismissRefreshingView();
                        getSyncVoas();
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        getMvpView().dismissReloadView();
                        getMvpView().dismissRefreshingView();
                        getSyncVoas();
                    }
                });
    }*/

    //增加新的(当前界面使用)
    private void loadNewVoas2(int maxId,int pageSize) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadNewSub2);
        mLoadNewSub2 = mDataManager.syncVoas(VoaService.GetVoa.Param.Value.PAGE_NUM,pageSize,maxId, UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .flatMap(voas -> mDataManager.getChildHomeVoas())
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
                        getMvpView().dismissRefreshingView();
                        getSyncVoas();
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        getMvpView().dismissReloadView();
                        getMvpView().dismissRefreshingView();
                        getSyncVoas();
                    }
                });
    }

    public void getSyncVoas() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSub);
        mLoadSub = mDataManager.getXiaoxue()
                .subscribeOn(Schedulers.io())
                .flatMap(voas -> mDataManager.getChildHomeVoas())
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
                            getMvpView().showToast(R.string.please_check_network);
                        }
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        if ((voas == null) || (voas.size() < 4)) {
                            if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                                getMvpView().showToast(R.string.please_check_network);
                            }
                        } else {
                            getMvpView().showVoas(voas);
                        }
                    }
                });
    }

    public int flag = 1;
    public void loadMoreVoas(final CategoryFooter category, int limit, String ids) {
        checkViewAttached();
        RxUtil.unsubscribe(mGetMoreVoaSub);
        final List voas = new ArrayList();
        mGetMoreVoaSub = mDataManager.getVoasByCategoryNotWith(category.getCategory(), limit, ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Voa>() {
                    @Override
                    public void onCompleted() {
                        if (voas.isEmpty()) {
                            getMvpView().showToast(R.string.no_data);
                        } else {
                            if (checkVoaSeries(voas) || (flag > 3)) {
                                getMvpView().showVoasByCategory(voas, category);
                            } else {
                                flag++;
                                Log.e("loadMoreVoas", "loadMoreVoas need run flag = " + flag);
                                loadMoreVoas(category, limit, ids);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().showToast(R.string.database_error);
                    }

                    @Override
                    public void onNext(Voa voa) {
                        voas.add(voa);
                    }
                });
    }

    private boolean checkVoaSeries(List<Voa> voas) {
        if ((voas == null) || voas.isEmpty()) {
            return true;
        }
        int diYIJi = 0;
        int jiaFei = 0;
        for (Voa vo: voas) {
            if ((vo != null) && (vo.series() == 201)) {
                ++diYIJi;
            }
            if (diYIJi > 1) {
                return false;
            }
            if ((vo != null) && (vo.series() == 202)) {
                ++jiaFei;
            }
            if (jiaFei > 1) {
                return false;
            }
        }
        return true;
    }

    public void loadVoas() {
        Log.e("loadVoas", "执行了");
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSub);
        mLoadSub = mDataManager.getMaxVoaId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_more_data);
                            //本来是要更换参数获取大量数据，可是太卡了，获取数据需要50s，改为原来的接口
                            //修改参数接口
//                            loadNewVoas(321001);
                            loadNewVoas2(0,3500);
                        }
                    }

                    @Override
                    public void onNext(Integer id) {
                        if (id != null) {
                            //本来是要更换参数获取大量数据，可是太卡了，获取数据需要50s，改为原来的接口
                            //修改参数接口
//                            loadNewVoas(id);
                            loadNewVoas2(0,3500);
                        }
                    }
                });
    }

    public void syncVoa4Category() {
        Log.e("syncVoa4Category", "syncVoa4Category run.");
        TalkShowApplication.getSubHandler().post(new Runnable() {
            @Override
            public void run() {
                List<Voa> getVoa1 = mDataManager.getVoa4Category("309");
                if ((getVoa1 != null) && (getVoa1.size() > 1)) {
                    mDataManager.deleteVoa4Category("309");
                }
                getVoa4Category("309");
            }
        });
    }
    protected void getVoa4Category(String ids) {
        Log.e("com.iyuba.talkshow", "getVoa4Category " + Header.getAllHeaders().get(1).getValue().toString());
        checkViewAttached();
        RxUtil.unsubscribe(mGetVoa4Ids);
        mGetVoa4Ids = mDataManager.getVoa4Category(Header.getAllHeaders().get(1).getValue().toString(), UserInfoManager.getInstance().getUserId())
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
//                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_more_data);
                        }
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        if (voas != null && voas.size() > 0) {
                            Log.e("com.iyuba.talkshow", "getVoa4Category voa.size " + voas.size());
//                            getSyncVoas();
                        } else {
                            Log.e("getVoa4Category", "onNext is null. ");
                        }
                    }
                });
    }

    public void getSeriesses() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSeriesSub);
        Log.e("getSeriesses", " getSeries4Ids called. ");
        mLoadSeriesSub = mDataManager.getSeries4Ids()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SeriesData>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                        if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                            Log.e("getSeriesses", " onError need syncSeries. ");
                            syncSeries();
                        }
                    }

                    @Override
                    public void onNext(List<SeriesData> response) {
                        if ((response == null) || (response.size() < 32)) {
                            if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                                Log.e("getSeriesses", " onNext  need syncSeries. ");
                                syncSeries();
                            }
                        } else {
                            Log.e("getSeriesses", " onNext  response.size() " + response.size());
                        }
                    }
                });
    }

    public void syncSeries() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSeriesSub);
        mLoadSeriesSub = mDataManager.syncSeriesList(UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SeriesResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
//                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onNext(SeriesResponse response) {
                        if ((response == null) || (response.getData() == null) || response.getData().isEmpty()) {
                            Log.e("syncSeries", " onNext  response is null. ");
                            return;
                        }

                        Log.e("syncSeries", " onNext  response.size() " + response.getData().size());
                        TalkShowApplication.getSubHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                List<SeriesData> getVoa3 = mDataManager.getSeries4Category("309");
                                if ((getVoa3 != null) && (getVoa3.size() > 1)) {
                                    mDataManager.deleteSeries4Category("309");
                                }
                                for (SeriesData bean : response.getData()) {
                                    mDataManager.insertSeriesDB(bean);
                                }
                            }
                        });
                    }
                });
    }

    public void loadLoop() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadLoopSub);
        String type = App.Apk.isChild() ? LoopService.GetLoopInfo.Param.Value.CHILD_LOOP_TYPE
                : LoopService.GetLoopInfo.Param.Value.LOOP_TYPE;
        mLoadLoopSub = mDataManager.getLunboItems(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LoopItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onNext(List<LoopItem> loopItems) {
                        getMvpView().setBanner(loopItems);
                    }
                });
    }

    /*public void getWelcomePic() {
        checkViewAttached();
        RxUtil.unsubscribe(mGetWelcomeSub);
        mGetWelcomeSub = mDataManager.getAd1()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GetAdResponse1>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (!NetStateUtil.isConnected((Context) getMvpView())) {
                            getMvpView().showToast(R.string.please_check_network);
                        }
                    }

                    @Override
                    public void onNext(List<GetAdResponse1> responseList) {
                        Date curDate = new Date();
                        GetAdResponse1 response = responseList.get(0);
                        if (response.result().equals(AdService.GetAd.Result.Code.SUCCESS)) {
                            try {
                                GetAdData1 data = response.data();
                                if (data != null) {
                                    if (!"web".equals(data.type())) {
                                        return;
                                    }

                                    Date startDate = mSdf.parse(data.startDate());
                                    if (startDate.getTime() <= curDate.getTime()) {
                                        GetAdData1 lastAd = mAdManager.getAdData1();
                                        String filename = AdService.GetAd1.getAdFilename(data.picUrl());
                                        if (lastAd != null) {
                                            String lastFilename = AdService.GetAd1.getAdFilename(lastAd.picUrl());
                                            if (TextUtils.equals(filename, lastFilename)
                                                    && !TextUtils.equals(lastAd.startDate(), data.startDate())) {
                                                StorageUtil.deleteAdFile(((Context) getMvpView()), filename);
                                            }
                                        }

                                        // 将改标识存下来，用与下次启动时显示
                                        mAdManager.saveAdData1(data);
                                        // 将图片的adStartTime存起来，如果图片表示一样，但adStartTime不一样，说明图片更换了
                                        String picUrl = Constant.Url.getAdPicUrl(data.picUrl());
                                        mDLManager.dlStart(picUrl, StorageUtil.getAdDir((Context) getMvpView()), filename, null);
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void login(double latitude, double longitude) {
        User user = mAccountManager.getSaveUser();

        if (user != null) {
            if (!TextUtils.isEmpty(user.getUsername())
                    && !TextUtils.isEmpty(user.getPassword())) {
                checkViewAttached();
                RxUtil.unsubscribe(mLoginSub);
                mLoginSub = mAccountManager.login(user.getUsername(),
                        user.getPassword(), latitude, longitude, null);
            }
        }
    }

    public void login() {
        Location location = mGetLocation.getLocation();
        login(location.getLatitude(), location.getLongitude());
    }

    public void loginWithoutLocation() {
        login(0, 0);
    }*/

    public void getVoaById(int voaId) {
        checkViewAttached();
        RxUtil.unsubscribe(mGetVoaByIdSub);
        mGetVoaByIdSub = mDataManager.getVoaById(voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Voa>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (getMvpView() != null) {
                            getMvpView().showToast(R.string.database_error);
                        }
                    }

                    @Override
                    public void onNext(Voa voa) {
                        if (getMvpView() != null) {
                            getMvpView().startDetailActivity(voa);
                        }
                    }
                });
    }

    public void getVoa4Id(int voaId) {
        checkViewAttached();
        RxUtil.unsubscribe(mGetVoaByIdSub);
        Log.e("MainPresenter", "getVoa4Id is called voaId " + voaId);
        mGetVoaByIdSub = mDataManager.getVoa4Id(voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Voa>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("MainPresenter", "getVoa4Id onError " + e.getMessage());
                        }
                        getVoaById(voaId);
                    }

                    @Override
                    public void onNext(List<Voa> voa) {
                        if ((voa != null) && (voa.size() > 0) && getMvpView() != null) {
                            getMvpView().startDetailActivity(voa.get(0));
                        } else {
                            getVoaById(voaId);
                            Log.e("MainPresenter", "getVoa4Id onNext is null.");
                        }
                    }
                });
    }

    public void checkVersion() {
//        mVersionManager.checkVersion(callBack);
        //在这里进行qq客服和qq群组的请求
        BrandUtil.requestQQNumber(mDataManager.getPreferencesHelper());
        BrandUtil.requestQQGroupNumber(mDataManager.getPreferencesHelper(), UserInfoManager.getInstance().getUserId());
    }

    public void enterGroup() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoginSub);
        mLoginSub = mDataManager.enterGroup(UserInfoManager.getInstance().getUserId(), Constant.EVAL_TYPE)
                .compose(RxUtil.io2main())
                .subscribe(new Subscriber<EnterGroup>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("MeFragPresenter", "enterGroup onError  " + e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(EnterGroup items) {
                        if ((items != null) && (items.groupinfo != null)) {
                            int groupId = 0;
                            try {
                                groupId = Integer.parseInt(items.groupinfo.get(0).groupid);
                                mConfigManager.setQQOfficial(groupId);
                            } catch (Exception var) { }
                            Log.e("MeFragPresenter", "enterGroup groupId  " + groupId);
                        } else {
                            Log.e("MeFragPresenter", "enterGroup onNext null. ");
                        }
                    }
                });
    }

    VersionManager.AppUpdateCallBack callBack = new VersionManager.AppUpdateCallBack() {
        @Override
        public void appUpdateSave(final String versionCode, final String appUrl) {
            String updateAlert = ((BaseActivity) getMvpView()).getString(R.string.about_update_alert);
            getMvpView().showAlertDialog(
                    MessageFormat.format(updateAlert, versionCode),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getMvpView().startAboutActivity(versionCode, appUrl);
                        }
                    });
        }

        @Override
        public void appUpdateFailed() {

        }
    };

    public void loadMoreChildNews(int pageNum) {
        if (null == getMvpView()){
            return;
        }

        //这里进行数据限制
        int categoryId = 309;
        if (ConfigData.isOppoCopyrightLimit(LibResUtil.getInstance().getContext())){
            categoryId = 301;
        }

        mDataManager.getVoa(categoryId, Level.Value.ALL, pageNum, 15)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Voa>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView() == null){
                            Log.e("MainPresenter", "loadMoreChildNews onError ");
                            return;
                        }
                        getMvpView().dismissRefreshingView();
                        if (!NetStateUtil.isConnected((Context) getMvpView())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
                            getMvpView().showToast(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        if ((getMvpView() == null) || (voas == null)){
                            Log.e("MainPresenter", "loadMoreChildNews onNext null ");
                            return;
                        }
                        Log.e("MainPresenter", "loadMoreChildNews voas.size " + voas.size());
                        getMvpView().showMoreVoas(voas);
                        getMvpView().dismissRefreshingView();
                    }
                });
    }

    /*public void registerToken(final String token, final String opTopken, String operator) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoginSub);
        mLoginSub = mDataManager.registerByMob(token, opTopken, operator)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterMobResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("MainPresenter", "registerToken onError  " + e.getMessage());
                        }
                        getMvpView().goResultActivity(null);
                    }

                    @Override
                    public void onNext(RegisterMobResponse response) {
                        if (response == null) {
                            Log.e("MainPresenter", "registerToken onNext response is null.");
                            getMvpView().goResultActivity(null);
                            return;
                        }
                        Log.e("MainPresenter", "registerToken onNext isLogin " + response.isLogin);
                        if (1 == response.isLogin) {
                            getMvpView().goResultActivity(new LoginResult());
                            getMvpView().showToast("您已经登录成功，可以进行学习了。");
                            // already login, need update user info
                            if (response.userinfo != null) {
                                mAccountManager.setUser(response.userinfo, "");
                                mAccountManager.saveUser();
                            } else {
                                Log.e("MainPresenter", "registerToken onNext response.userinfo is null.");
                            }
                            EventBus.getDefault().post(new LoginEvent());
                        } else {
                            if (response.res != null) {
                                // register by this phone
                                RegisterMobResponse.MobBean mobBean = response.res;
                                LoginResult loginResult = new LoginResult();
                                loginResult.setPhone(mobBean.phone);
                                getMvpView().goResultActivity(loginResult);
                            } else {
                                Log.e("MainPresenter", "registerToken onNext response.res is null.");
                                getMvpView().goResultActivity(null);
                            }
                        }
                    }
                });
    }*/

    /*public void convertToAac() {
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File file) {
                getMvpView().showToast("成功");
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                getMvpView().showToast("失败");
            }
        };
        File file = BaseStorageUtil.getExternalFile(((Context) getMvpView()), "silent.wav");
        Timber.e("*******" + file.getAbsolutePath());
        ConvertAudio.wavToMp3(((Context) getMvpView()), file.getAbsolutePath(), callback);
    }*/


}
