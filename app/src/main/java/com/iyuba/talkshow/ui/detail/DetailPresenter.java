package com.iyuba.talkshow.ui.detail;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.Collect;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.data.model.result.PdfResponse;
import com.iyuba.talkshow.data.remote.IntegralService;
import com.iyuba.talkshow.injection.ConfigPersistent;
import com.iyuba.talkshow.ui.base.BasePresenter;
import com.iyuba.talkshow.ui.lil.bean.Study_listen_report;
import com.iyuba.talkshow.ui.lil.event.RefreshEvent;
import com.iyuba.talkshow.ui.lil.manager.ListenStudyManager;
import com.iyuba.talkshow.ui.lil.util.BigDecimalUtil;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.StorageUtil;
import com.iyuba.talkshow.util.TimeUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.talkshow.util.VoaMediaUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager mDataManager;

    private Subscription mSaveCollectSub;
    private Subscription mCheckCollectedSub;
    private Subscription mDeleteCollectSub;

    //增加保存voa
    private Subscription mSaveCollectVoaSub;

    private Voa mVoa;
    private Subscription mLoadSub;
    private Subscription mLoadNewSub;
    private Subscription mGetPdfSub;
    private Subscription mSyncVoaSub;
    private Subscription mLoginSub;

    @Inject
    public DetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public Uri getVideoUri() {
        File videoFile = StorageUtil.getVideoFile(TalkShowApplication.getContext(), mVoa.voaId());
        if (videoFile.exists()) {
            return Uri.fromFile(videoFile);
        } else {
//            Log.e("showVip",
//                    VoaMediaUtil.getVideoUrl(mVoa.category(),mVoa.voaId()));
////            return mAccountManager.isVip() ?
////                    Uri.parse(VoaMediaUtil.getVideoVipUrl(mVoa.voaId())) :
//                  return   Uri.parse(VoaMediaUtil.getVideoUrl(mVoa.category() ,mVoa.voaId()));
            //替换video的链接
//            Log.e("showVip",
//                    VoaMediaUtil.getVideoVipUrl2(mVoa.video()));
//            return  Uri.parse(VoaMediaUtil.getVideoUrl2(mVoa.video()));

            //判断是否为vip
            if (UserInfoManager.getInstance().isVip()){
                return Uri.parse(VoaMediaUtil.getVideoVipUrl(mVoa.video()));
            }else {
                return Uri.parse(VoaMediaUtil.getVideoUrl(mVoa.video()));
            }
        }
    }

    //获取下载的视频路径
    public Uri getDownVideoUri(){
        //判断是否为vip
        if (UserInfoManager.getInstance().isVip()){
            return Uri.parse(VoaMediaUtil.getVideoVipUrl(mVoa.video()));
        }else {
            return Uri.parse(VoaMediaUtil.getVideoUrl(mVoa.video()));
        }
    }

    public void getPdf(int voaId , int type ){
        checkViewAttached();
        RxUtil.unsubscribe(mGetPdfSub);
        mGetPdfSub = mDataManager.getPdf(Constant.EVAL_TYPE , voaId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PdfResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().hideLoading();
                        getMvpView().showToast("生成pdf失败");
                    }

                    @Override
                    public void onNext(PdfResponse pdfResponse) {
                        getMvpView().hideLoading();
                        if (null != pdfResponse.exists){
                            getMvpView().showPdfFinishDialog(pdfResponse.path);
                        }
                    }
                });
    }

    public void loadVoas() {
        Log.e("loadVoas", "执行了");
        checkViewAttached();
        //RxUtil.unsubscribe(mLoadSub);
        mLoadSub = mDataManager.getMaxVoaId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (!NetStateUtil.isConnected((Context) getMvpView())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_fail);
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

    private void loadNewVoas(int maxId) {
        Log.e("loadNewVoas", "执行了！");
        checkViewAttached();
        RxUtil.unsubscribe(mLoadNewSub);
        mLoadNewSub = mDataManager.syncVoas(maxId, UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<Voa>, Observable<List<Voa>>>() {
                    @Override
                    public Observable<List<Voa>> call(List<Voa> voas) {
                        return mDataManager.getHomeVoas();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Voa>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        if (!NetStateUtil.isConnected((Context) getMvpView())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
//                            getMvpView().showToast(R.string.request_fail);
                        }
                        mDataManager.getHomeVoas().subscribe(new Subscriber<List<Voa>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(List<Voa> voas) {
                                if (voas == null || voas.isEmpty()) {
                                    getMvpView().showToast("更新失败");

                                } else {
                                    getMvpView().showToast("更新失败");
                                }
                            }
                        });
                    }

                    @Override
                    public void onNext(List<Voa> voas) {
                        if (voas == null || voas.isEmpty()) {
                            getMvpView().showToast("更新成功");

                        } else {
                            getMvpView().showToast("更新成功");
                        }
                    }
                });
    }

    public void setVoa(Voa voa) {
        this.mVoa = voa;
    }
    public void saveCollect(int voaId) {
        int uid = UserInfoManager.getInstance().getUserId();
        checkViewAttached();
        RxUtil.unsubscribe(mSaveCollectSub);
        mSaveCollectSub = mDataManager.saveCollect(
                Collect.builder()
                        .setUid(uid)
                        .setVoaId(voaId)
                        .setDate(TimeUtil.getCurDate())
                        .build())
                .compose(RxUtil.<Boolean>io2main())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showToast(R.string.database_error);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getMvpView().setIsCollected(true);
//                            getMvpView().setCollectTvText(R.string.have_collected);
                        } else {
                            getMvpView().setIsCollected(false);
//                            getMvpView().setCollectTvText(R.string.collect);
                        }
                    }
                });
    }

    public void deleteCollect(int voaId) {
        checkViewAttached();
        RxUtil.unsubscribe(mDeleteCollectSub);
        int uid = UserInfoManager.getInstance().getUserId();
        mDeleteCollectSub = mDataManager.deleteCollect(uid, voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showToast(R.string.database_error);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getMvpView().setIsCollected(false);
//                            getMvpView().setCollectTvText(R.string.collect);
                        } else {
                            getMvpView().setIsCollected(true);
//                            getMvpView().setCollectTvText(R.string.have_collected);
                        }
                    }
                });
    }

    public void checkCollected(int voaId) {
        checkViewAttached();
        RxUtil.unsubscribe(mCheckCollectedSub);
        mCheckCollectedSub = mDataManager.getCollectByVoaId(voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showToast(R.string.database_error);
                    }

                    @Override
                    public void onNext(Integer voaId) {
                        if (voaId == mVoa.voaId()) {
                            getMvpView().setIsCollected(true);
//                            getMvpView().setCollectTvText(R.string.have_collected);
                        } else {
                            getMvpView().setIsCollected(false);
//                            getMvpView().setCollectTvText(R.string.collect);
                        }

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

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mSaveCollectSub);
        RxUtil.unsubscribe(mCheckCollectedSub);
        RxUtil.unsubscribe(mDeleteCollectSub);
        RxUtil.unsubscribe(mSyncVoaSub);
        RxUtil.unsubscribe(mLoadSub);
        RxUtil.unsubscribe(mGetPdfSub);
        RxUtil.unsubscribe(mLoadNewSub);
        RxUtil.unsubscribe(mLoginSub);
    }

    public IntegralService getInregralService() {
        return mDataManager.getIntegralService();
    }

    public void syncVoaTexts(final int voaId) {
        checkViewAttached();
        RxUtil.unsubscribe(mSyncVoaSub);
        mSyncVoaSub = mDataManager.syncVoaTexts(voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<VoaText>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (!NetStateUtil.isConnected((Context) getMvpView())) {
                            getMvpView().showToast(R.string.please_check_network);
                        } else {
                            getMvpView().showToast(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onNext(List<VoaText> voaTextList) {
                        if (voaTextList.size() == 0) {
//                            getMvpView().showEmptyTexts();
                        } else {
//                            getMvpView().showVoaTexts(voaTextList);
                        }
                    }
                });
    }

    public void saveNoExistVoa(Voa mVoa){
        //这里做一个极端的操作，只要跳转到详情界面，我就将数据保存在本地，便于查阅
        //这里有一个奇怪的bug：调用这个方法后，本来跳转到详情页，现在同时跳转到了配音界面，不过现在已经解决了
        List<Voa> voaList = mDataManager.getVoaByVoaId(mVoa.voaId());
        if (voaList!=null&&voaList.size()>0){

        }else {
            mDataManager.insertVoaDB(mVoa);
        }
    }

    /************************************学习报告处理*****************************/
    //获取当前课程的文本数据(填充到学习报告管理器中使用)
    public void getLessonText(int voaId){
        mDataManager.getVoaTexts(voaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VoaText>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<VoaText> list) {
                        ListenStudyManager.getInstance().setVoaTextList(list);
                    }
                });
    }

    //提交听力学习报告
    private Subscription listenReportSub;
    public void submitListenReport(int voaId){
        //这里进行处理
        int curPlayIndex = 0;
        int curStudyWords = 0;
        //先获取当前播放的时间进度
//        long videoProgress = binding.videoView.getCurrentPosition();
        //获取当前的内容
        List<VoaText> textList = ListenStudyManager.getInstance().getVoaTextList();
        if (textList!=null&&textList.size()>0){
            //这里默认是播放完成才调用，后期如果修改，请循环判断当前位置
            curPlayIndex = textList.size()-1;
            //单词数量
            for (int i = 0; i < textList.size(); i++) {
                String sentence = textList.get(i).sentence();
                //先筛选
                sentence = sentence.replace(","," ");
                sentence = sentence.replace("."," ");
                sentence = sentence.replace("!"," ");
                sentence = sentence.replace("?"," ");

                //划分单词
                String[] wordArray = sentence.split(" ");
                curStudyWords+=wordArray.length;
            }
        }

        RxUtil.unsubscribe(listenReportSub);
        listenReportSub = mDataManager.submitListenStudyReport(UserInfoManager.getInstance().getUserId(),
                ListenStudyManager.getInstance().getStartTime(),
                ListenStudyManager.getInstance().getEndTime(),
                String.valueOf(voaId),
                true,
                curStudyWords,
                curPlayIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Study_listen_report>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Study_listen_report bean) {
                        if (bean!=null&&bean.getResult().equals("1")){
                            //获取价格
                            float price = TextUtils.isEmpty(bean.getReward())?0:Float.parseFloat(bean.getReward());
                            if (price>0){
                                price = (float) BigDecimalUtil.trans2Double(price*0.01f);

                                String formatStr = "本次学习获得%1$s元红包奖励,已自动存入您的钱包账户";
                                ToastUtil.showToast(TalkShowApplication.getContext(),String.format(formatStr,String.valueOf(price)));
                                //刷新用户信息
                                EventBus.getDefault().post(new RefreshEvent(RefreshEvent.USER_INFO));
                            }
                        }
                    }
                });
    }
}
