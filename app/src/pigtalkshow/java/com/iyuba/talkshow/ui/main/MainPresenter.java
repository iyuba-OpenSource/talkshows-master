package com.iyuba.talkshow.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.iyuba.talkshow.Constant;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.AccountManager;
import com.iyuba.talkshow.data.manager.AdManager;
import com.iyuba.talkshow.data.manager.VersionManager;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.Header;
import com.iyuba.talkshow.data.model.Level;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.RegisterMobResponse;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.SeriesResponse;
import com.iyuba.talkshow.data.model.User;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.result.GetAdData1;
import com.iyuba.talkshow.data.model.result.GetAdResponse1;
import com.iyuba.talkshow.data.remote.AdService;
import com.iyuba.talkshow.data.remote.LoopService;
import com.iyuba.talkshow.event.LoginResult;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.util.GetLocation;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.StorageUtil;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import cn.aigestudio.downloader.bizs.DLManager;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.iyuba.talkshow.util.BrandUtil;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private final AdManager mAdManager;
    private final DLManager mDLManager;
    private final VersionManager mVersionManager;
    private final AccountManager mAccountManager;
    private final GetLocation mGetLocation;

    private Subscription mLoadNewSub;
    private Subscription mLoadSub;
    private Subscription mLoadLoopSub;
    private Subscription mGetWelcomeSub;
    private Subscription mLoginSub;
    private Subscription mGetVoaByIdSub;
    private Subscription mGetMoreVoaSub;
    private Subscription mGetVoa4Ids;

    private static DateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
    private Subscription mLoadSeriesSub;

    @Inject
    public MainPresenter(DataManager dataManager, DLManager dlManager,
                         AccountManager accountManager, AdManager adManager,
                         GetLocation getLocation, VersionManager versionManager) {
        mDataManager = dataManager;
        mDLManager = dlManager;
        mAccountManager = accountManager;
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

    private void loadNewVoas(int maxId) {
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
                            loadNewVoas(321001);
                        }
                    }

                    @Override
                    public void onNext(Integer id) {
                        if (id != null) {
                            loadNewVoas(id);
                        }
                    }
                });
    }

    protected void getVoa4Category() {
        Log.e("syncVoa4Category", "syncVoa4Category run.");
        TalkShowApplication.getSubHandler().post(new Runnable() {
            @Override
            public void run() {
                List<Voa> getVoa1 = mDataManager.getVoa4Category(App.DEFAULT_SERIESID);
                if ((getVoa1 == null) || (getVoa1.size() < 100)) {
                    syncVoa4Category(App.DEFAULT_SERIESID);
                }
            }
        });
    }
    protected void syncVoa4Category(String ids) {
        Log.e("com.iyuba.talkshow", "getVoa4Category ids " + ids);
        checkViewAttached();
        RxUtil.unsubscribe(mGetVoa4Ids);
        mGetVoa4Ids = mDataManager.getVoa4Category(ids, mAccountManager.getUid())
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

    public void getSeriesList(String cat) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSeriesSub);
        Log.e("getSeriesses", " getSeries4Ids called. ");
        mLoadSeriesSub = mDataManager.getSeriesList(cat)
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
                            syncSeries(cat);
                        } else {
                            getMvpView().dismissRefreshingView();
                            getMvpView().dismissReloadView();
                            getMvpView().setChoise(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onNext(List<SeriesData> response) {
                        if ((response == null) || (response.size() < 4)) {
                            if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                                Log.e("getSeriesses", " onNext  need syncSeries. ");
                                syncSeries(cat);
                            }
                        } else {
                            try {
                                Filter4Pig(response);
                            } catch (Exception var3) { }
                            getMvpView().dismissRefreshingView();
                            getMvpView().dismissReloadView();
                            getMvpView().setChoise(response);
                            Log.e("getSeriesses", " onNext  response.size() " + response.size());
                        }
                    }
                });
    }

    public static void Filter4Pig(List<SeriesData> dataList) {
        if ((dataList == null) || dataList.isEmpty()) {
            return;
        }
        Iterator<SeriesData> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            SeriesData series = iterator.next();
            if ((series == null) || TextUtils.isEmpty(series.getId())) {
                iterator.remove();
                continue;
            }
            int id = Integer.parseInt(series.getId());
            if ((201 == id) || (id >= 459)) {
                continue;
            } else {
                iterator.remove();
            }
        }
    }

    public void syncSeries(String cat) {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadSeriesSub);
        mLoadSeriesSub = mDataManager.syncSeries4Category(cat, mAccountManager.getUid())
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
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_fail);
                        }
                        getMvpView().dismissRefreshingView();
                        getMvpView().dismissReloadView();
//                        getMvpView().setChoise(new ArrayList<>());
                    }

                    @Override
                    public void onNext(SeriesResponse response) {
                        if ((response == null) || (response.getData() == null) || response.getData().isEmpty()) {
                            Log.e("syncSeries", " onNext  response is null. ");
                            getMvpView().dismissRefreshingView();
                            getMvpView().dismissReloadView();
//                            getMvpView().setChoise(new ArrayList<>());
                            return;
                        }
                        getMvpView().dismissRefreshingView();
                        getMvpView().dismissReloadView();
                        List<SeriesData> seriesData = response.getData();
                        try {
                            Filter4Pig(seriesData);
                        } catch (Exception var3) { }
                        Collections.sort(seriesData, new Comparator<SeriesData>() {
                            @Override
                            public int compare(SeriesData o1, SeriesData o2) {
                                return o1.getId().compareTo(o2.getId());
                            }
                        });
                        getMvpView().setChoise(response.getData());
                        Log.e("syncSeries", " onNext  response.size() " + response.getData().size());
                        TalkShowApplication.getSubHandler().post(new Runnable() {
                            @Override
                            public void run() {
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
        mLoadLoopSub = mDataManager.getLunboItems(App.LOOP_TYPE)
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

    public void getWelcomePic() {
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
    }

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
        BrandUtil.requestQQGroupNumber(mDataManager.getPreferencesHelper(), mAccountManager.getUid());
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

    public boolean isVip() {
        return mAccountManager.isVip();
    }

    public void loadMoreChildNews(int pageNum) {
        if (null == getMvpView()){
            return;
        }
        mDataManager.getVoa(309, Level.Value.ALL, pageNum, 15)
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

    public void registerToken(final String token, final String opTopken, String operator) {
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
    }

//    public void convertToAac() {
//        IConvertCallback callback = new IConvertCallback() {
//            @Override
//            public void onSuccess(File file) {
//                getMvpView().showToast("成功");
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                e.printStackTrace();
////                getMvpView().showToast("失败");
//            }
//        };
//        File file = BaseStorageUtil.getExternalFile(((Context) getMvpView()), "silent.wav");
//        Timber.e("*******" + file.getAbsolutePath());
//        ConvertAudio.wavToMp3(((Context) getMvpView()), file.getAbsolutePath(), callback);
//    }
}
