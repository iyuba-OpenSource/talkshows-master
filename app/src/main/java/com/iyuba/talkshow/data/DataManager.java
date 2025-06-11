package com.iyuba.talkshow.data;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.module.toolbox.SingleParser;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.local.DatabaseHelper;
import com.iyuba.talkshow.data.local.PreferencesHelper;
import com.iyuba.talkshow.data.model.AllWordsRespons;
import com.iyuba.talkshow.data.model.AppCheckResponse;
import com.iyuba.talkshow.data.model.ChangeNameResponse;
import com.iyuba.talkshow.data.model.Collect;
import com.iyuba.talkshow.data.model.Comment;
import com.iyuba.talkshow.data.model.Download;
import com.iyuba.talkshow.data.model.EnterGroup;
import com.iyuba.talkshow.data.model.ExamWordResponse;
import com.iyuba.talkshow.data.model.Header;
import com.iyuba.talkshow.data.model.IntegralBean;
import com.iyuba.talkshow.data.model.LessonNewResponse;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.NewSeriesDataResponse;
import com.iyuba.talkshow.data.model.PostItem;
import com.iyuba.talkshow.data.model.RankingListBean;
import com.iyuba.talkshow.data.model.Record;
import com.iyuba.talkshow.data.model.RegisterMobResponse;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.SeriesResponse;
import com.iyuba.talkshow.data.model.Thumb;
import com.iyuba.talkshow.data.model.University;
import com.iyuba.talkshow.data.model.UpdateWordResponse;
import com.iyuba.talkshow.data.model.UserData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.VoaSoundNew;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.data.model.WavListItem;
import com.iyuba.talkshow.data.model.WordResponse;
import com.iyuba.talkshow.data.model.result.AddReadCountResponse;
import com.iyuba.talkshow.data.model.result.AppUpdateResponse;
import com.iyuba.talkshow.data.model.result.CheckAmountResponse;
import com.iyuba.talkshow.data.model.result.EditUserBasicInfoResponse;
import com.iyuba.talkshow.data.model.result.FeedbackResponse;
import com.iyuba.talkshow.data.model.result.GetAdResponse;
import com.iyuba.talkshow.data.model.result.GetAdResponse1;
import com.iyuba.talkshow.data.model.result.GetAliPayInfoResponse;
import com.iyuba.talkshow.data.model.result.GetAliPayResponse;
import com.iyuba.talkshow.data.model.result.GetCommentResponse;
import com.iyuba.talkshow.data.model.result.GetLoopResponse;
import com.iyuba.talkshow.data.model.result.GetMyDubbingResponse;
import com.iyuba.talkshow.data.model.result.GetRankingResponse;
import com.iyuba.talkshow.data.model.result.GetUserBasicInfoResponse;
import com.iyuba.talkshow.data.model.result.GetUserResponse;
import com.iyuba.talkshow.data.model.result.GetVerifyCodeResponse;
import com.iyuba.talkshow.data.model.result.GetVoaResponse;
import com.iyuba.talkshow.data.model.result.GetWXPayInfoResponse;
import com.iyuba.talkshow.data.model.result.LoginResponse;
import com.iyuba.talkshow.data.model.result.NotifyAliPayResponse;
import com.iyuba.talkshow.data.model.result.PdfResponse;
import com.iyuba.talkshow.data.model.result.RegisterResponse;
import com.iyuba.talkshow.data.model.result.SendCommentResponse;
import com.iyuba.talkshow.data.model.result.SendDubbingResponse;
import com.iyuba.talkshow.data.model.result.SendEvaluateResponse;
import com.iyuba.talkshow.data.model.result.ShareInfoResponse;
import com.iyuba.talkshow.data.model.result.ThumbsResponse;
import com.iyuba.talkshow.data.model.result.UploadImageResponse;
import com.iyuba.talkshow.data.model.result.VoaTextResponse;
import com.iyuba.talkshow.data.model.result.location.GetLocationResponse;
import com.iyuba.talkshow.data.remote.AdService;
import com.iyuba.talkshow.data.remote.ClearUserResponse;
import com.iyuba.talkshow.data.remote.CmsService;
import com.iyuba.talkshow.data.remote.CommentService;
import com.iyuba.talkshow.data.remote.EvalServiece;
import com.iyuba.talkshow.data.remote.FeedbackService;
import com.iyuba.talkshow.data.remote.FixService;
import com.iyuba.talkshow.data.remote.IntegralService;
import com.iyuba.talkshow.data.remote.LocationService;
import com.iyuba.talkshow.data.remote.LoopService;
import com.iyuba.talkshow.data.remote.MovieService;
import com.iyuba.talkshow.data.remote.OtherService;
import com.iyuba.talkshow.data.remote.PayService;
import com.iyuba.talkshow.data.remote.PdfService;
import com.iyuba.talkshow.data.remote.RankingService;
import com.iyuba.talkshow.data.remote.ThumbsService;
import com.iyuba.talkshow.data.remote.UploadStudyRecordService;
import com.iyuba.talkshow.data.remote.UserService;
import com.iyuba.talkshow.data.remote.VerifyCodeService;
import com.iyuba.talkshow.data.remote.VersionService;
import com.iyuba.talkshow.data.remote.VipService;
import com.iyuba.talkshow.data.remote.VoaService;
import com.iyuba.talkshow.data.remote.WordCollectService;
import com.iyuba.talkshow.data.remote.WordService;
import com.iyuba.talkshow.data.remote.WordTestService;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.BaseLessonResponse;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.JuniorResponse;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.PrimaryResponse;
import com.iyuba.talkshow.ui.lil.bean.Reward_history;
import com.iyuba.talkshow.ui.lil.bean.Study_listen_report;
import com.iyuba.talkshow.ui.lil.util.DateUtil;
import com.iyuba.talkshow.ui.lil.util.EncodeUtil;
import com.iyuba.talkshow.ui.lil.util.SignUtil;
import com.iyuba.talkshow.ui.search.SearchListBean;
import com.iyuba.talkshow.ui.user.edit.GetIpResponse;
import com.iyuba.talkshow.ui.user.edit.ImproveUserResponse;
import com.iyuba.talkshow.ui.user.login.TokenBean;
import com.iyuba.talkshow.ui.user.login.UidBean;
import com.iyuba.talkshow.ui.words.Word;
import com.iyuba.talkshow.ui.words.WordCollectResponse;
import com.iyuba.talkshow.util.MD5;
import com.iyuba.talkshow.util.OtherUtil;
import com.iyuba.talkshow.util.ParameterUrl;
import com.iyuba.talkshow.util.TextAttr;
import com.iyuba.talkshow.util.Util;
import com.iyuba.talkshow.util.request.ProgressListener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

@Singleton
public class DataManager {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private final VoaService mVoaService;
    private final LoopService mLoopService;   //
    private final ThumbsService mThumbsService;
    private final FeedbackService mFeedbackService;
    private final CommentService mCommentService;
    private final PdfService mPdfService;
    private final WordService mWordService;
    private final WordCollectService mWordCollectService;
    private final MovieService mMovieService;
    private final UserService mUserService;
    private final PayService mPayService;
    private final VerifyCodeService mVerifyCodeService;
    private final VersionService mVersionService;
    private final VipService mVipService;
    private final LocationService mLocationService;
    private final AdService mAdService;
    private final CmsService mCmsService;
    private final OtherService mOtherService;
    private final UploadStudyRecordService mUploadStudyRecordService;
    private final RankingService mRankingService;
    private final IntegralService mIntegralService;
    private final EvalServiece mEvalServiece;
    private final WordTestService mWordTestServiece;

    //补充的service
    private final FixService mFixService;

    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(VoaService voaService, LoopService loopService,
                       ThumbsService thumbsService, FeedbackService feedbackService,
                       CommentService commentService, UserService userService,
                       PayService payService, VerifyCodeService verifyCodeService,
                       VersionService versionService, VipService vipService,
                       LocationService locationService, AdService adService,
                       OtherService otherService,
                       UploadStudyRecordService uploadStudyRecordService,
                       RankingService rankingService,
                       IntegralService integralService,
                       EvalServiece evalServiece,
                       PdfService pdfService,
                       PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper,
                       WordService wordService,
                       MovieService movieService,
                       CmsService cmsService,
                       WordTestService wordTestService,
                       WordCollectService collectService,
                       FixService fixService//增加fixservice
    ) {
        mVoaService = voaService;
        mLoopService = loopService;
        mThumbsService = thumbsService;
        mFeedbackService = feedbackService;
        mCommentService = commentService;
        mUserService = userService;
        mWordService = wordService;
        mMovieService = movieService;
        mPayService = payService;
        mVerifyCodeService = verifyCodeService;
        mVersionService = versionService;
        mVipService = vipService;
        mLocationService = locationService;
        mAdService = adService;
        mOtherService = otherService;
        mUploadStudyRecordService = uploadStudyRecordService;
        mRankingService = rankingService;
        mIntegralService = integralService;
        mEvalServiece = evalServiece;
        mCmsService = cmsService;
        mWordCollectService = collectService;
        mWordTestServiece = wordTestService;
        mPdfService = pdfService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;

        mFixService = fixService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<List<Voa>> getVoa4Id(int mId) {
        return mVoaService.getVoaById("andriod", "json", mId)
                .concatMap(new Func1<GetVoaResponse, Observable<List<Voa>>>() {
                    @Override
                    public Observable<List<Voa>> call(GetVoaResponse getVoaResponse) {
                        return mDatabaseHelper.setVoas(getVoaResponse.data()).toList();
                    }
                });
    }

    //设置视频播放调速
    public void setVideoSpeed(int uid,float speed){
        mPreferencesHelper.putFloat(String.valueOf(uid),speed);
    }

    public float getVideoSpeed(int uid){
        return mPreferencesHelper.loadFloat(String.valueOf(uid),1.0F);
    }

    public Observable<List<Voa>> syncVoas(int pageNum, int pageSize, int maxId, int uid) {
        Map<String, String> map = new HashMap<>();
        map.put(VoaService.GetVoa.Param.Key.TYPE, VoaService.GetVoa.Param.Value.TYPE);
        map.put(VoaService.GetVoa.Param.Key.FORMAT, VoaService.GetVoa.Param.Value.FORMAT);
        map.put("appid", App.APP_ID + "");
        map.put("uid", uid + "");
        map.put(VoaService.GetVoa.Param.Key.PAGES, String.valueOf(pageNum));
        map.put(VoaService.GetVoa.Param.Key.PAGE_NUM, String.valueOf(pageSize));
        map.put(VoaService.GetVoa.Param.Key.MAX_ID, String.valueOf(maxId));
        Timber.e("getVoas*****");
        return mVoaService.getVoas(map)
                .concatMap(new Func1<GetVoaResponse, Observable<List<Voa>>>() {
                    @Override
                    public Observable<List<Voa>> call(GetVoaResponse getVoaResponse) {
                        Timber.e("getVoas*****");
                        return mDatabaseHelper.setVoas(getVoaResponse.data()).toList();
                    }
                });
    }

    public Observable<List<Voa>> getVoa4Category(String ids, int uid) {
        Timber.e("getVoa4Category*****");
        return mVoaService.getVoa4Category(VoaService.GetVoa.Param.Value.TYPE, VoaService.GetVoa.Param.Value.FORMAT, 0,
                            App.APP_ID, uid,
                            VoaService.GetVoa.Param.Value.PAGE_NUM, VoaService.GetVoa.Param.Value.PAGE_SIZE, ids)
                .concatMap(new Func1<GetVoaResponse, Observable<List<Voa>>>() {
                    @Override
                    public Observable<List<Voa>> call(GetVoaResponse getVoaResponse) {
                        Timber.e("getVoa4Category*****");
                        return mDatabaseHelper.setVoas(getVoaResponse.data()).toList();
                    }
                });
    }

    public Observable<SendEvaluateResponse> uploadSentence(String sentence, int idIndex, int newsid, int paraid, String type,
                                                           String uid, File file) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put(EvalServiece.GetVoa.Param.Key.SENTENCE, fromString(sentence));
        map.put(EvalServiece.GetVoa.Param.Key.IDINDEX, fromString(idIndex + ""));
        map.put(EvalServiece.GetVoa.Param.Key.NEWSID, fromString(newsid + ""));
        map.put(EvalServiece.GetVoa.Param.Key.PARAID, fromString(paraid + ""));
        map.put(EvalServiece.GetVoa.Param.Key.TYPE, fromString(type + ""));
        map.put(EvalServiece.GetVoa.Param.Key.USERID, fromString(uid));
        if (Constant.EvaluateCorrect) {
            map.put("flg", fromString("0"));
            map.put("wordId", fromString("0"));
            map.put("appId", fromString(App.APP_ID + ""));
            return mEvalServiece.sendVoiceEval(map, fromFile(file));
        } else {
            return mEvalServiece.sendVoiceComment(map, fromFile(file));
        }
    }

    public Observable<EnterGroup> enterGroup(int uid, String type) {
        Log.e("DataManager", "enterGroup **** for type " + type);
        return mEvalServiece.enterGroup(uid, type);
    }

    public Observable<List<Voa>> syncVoas(int maxId, int uid) {
        Timber.e("syncVoas ****");
        return syncVoas(VoaService.GetVoa.Param.Value.PAGE_NUM, VoaService.GetVoa.Param.Value.PAGE_SIZE, maxId, uid);
    }

    public Observable<List<Voa>> syncVoas() {
        return syncVoas(VoaService.GetVoa.Param.Value.PAGE_NUM, VoaService.GetVoa.Param.Value.PAGE_SIZE, VoaService.GetVoa.Param.Value.RECENT_VOA_ID, 0);
    }

    public Observable<List<Voa>> getVoas() {
        return mDatabaseHelper.getVoas().distinct();
    }

    public Observable<List<Voa>> getHomeVoas() {
        if (ConfigData.isBlockJuniorEnglish(LibResUtil.getInstance().getContext())) {
            return mDatabaseHelper.getVivoVoas(1, 4,
                    Header.getAllHeaders().get(0).getValue().toString(),
                    Header.getAllHeaders().get(1).getValue().toString(),
                    Header.getAllHeaders().get(2).getValue().toString(),
                    Header.getAllHeaders().get(3).getValue().toString(),
                    Header.getAllHeaders().get(4).getValue().toString());
        }
        return mDatabaseHelper.getVoas(1, 4,
                Header.getAllHeaders().get(0).getValue().toString(),
                Header.getAllHeaders().get(1).getValue().toString(),
                Header.getAllHeaders().get(2).getValue().toString(),
                Header.getAllHeaders().get(3).getValue().toString(),
                Header.getAllHeaders().get(4).getValue().toString(),
                Header.getAllHeaders().get(5).getValue().toString(),
                Header.getAllHeaders().get(6).getValue().toString()
        );
    }

    public Observable<List<Voa>> getChildHomeVoas() {
        return mDatabaseHelper.getChildHomeVoas(Header.getAllHeaders().get(1).getValue().toString());
    }

    public Observable<List<Voa>> getXiaoxue() {
        return mDatabaseHelper.getXiaoxueHomeVoas(Header.getAllHeaders().get(5).getValue().toString());
    }

    public Observable<List<Voa>> getXiaoxueByBookId(int bookId) {
        return mDatabaseHelper.getXiaoxueByBookId(bookId);
    }

    public List<Voa> getVoa4Category(String seriesId) {
        return mDatabaseHelper.getVoa4Category(seriesId);
    }

    public List<Voa> getVoa4Series(String seriesId) {
        return mDatabaseHelper.getVoa4Series(seriesId);
    }

    public List<SeriesData> getSeries4Category(String seriesId) {
        return mDatabaseHelper.getSeries4Category(seriesId);
    }

    public List<Integer> getXiaoxueVoaIdsByBookId(int bookId) {
        return mDatabaseHelper.getXiaoxueVoaIdsByBookId(bookId);
    }

    public List<Voa> getVoaByVoaId(int voaId) {
        return mDatabaseHelper.getVoaByVoaId(voaId);
    }

    public Observable<List<Voa>> getHomeNewList(int pageNum) {
        return mDatabaseHelper.getVoaByLevelOne(pageNum, 4).toList();
    }

    public Observable<List<Voa>> getCoursesVoas(int series) {
        return mDatabaseHelper.getVoasBySeries(series).toList();
    }

    public Observable<UpdateWordResponse> updateWordByBookId(int bookId, int version) {
        return mVoaService.updateWords(bookId, version);
    }

    public Observable<UpdateWordResponse> getWordByBookId(int bookId) {
        return mVoaService.getWords(bookId);
    }

//    public Observable<OfficialResponse> getOfficialAccount(int pageNum, int pageSize) {
//        return mVoaService.getOfficialAccount(pageNum, pageSize, Constant.LESSON_TYPE);
//    }
//
//    public Observable<LessonNewResponse> chooseLessonNew(int appid, int uid) {
//        return mVoaService.chooseLessonNew(appid, uid, Constant.LESSON_TYPE, 3);
//    }

    public Observable<ExamWordResponse> getExamWordDetail(int userId, String appid, String lesson, String TestMode, int mode) {
        String sign = buildWordSign(userId, appid, lesson, TestMode, mode);
        Log.e("DataManager", "DataManager buildWordSign sign = " + sign);
        return mRankingService.getExamWordDetail(userId, appid, lesson, TestMode, mode, sign);
    }

    private String buildWordSign(int userId, String appid, String lesson, String TestMode, int mode) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String signText = userId + lesson + mode + TestMode + appid + format.format(System.currentTimeMillis());
        Log.e("DataManager", "DataManager buildWordSign text = " + signText);
        return MD5.getMD5ofStr(signText);
    }

    public Observable<Voa> getVoasByCategoryNotWith(String category, int limit, String ids) {
        return mDatabaseHelper.getVoasByCategoryNotWith(true, category, limit, ids);
    }

    // 是否是试听的课程（非会员也可以进入的）
    public boolean isTrial(Voa voa) {
        if (voa == null) return true ;
        return mDatabaseHelper.isTrial(voa);
    }

    public Observable<Integer> getMinVoaId() {
        return mDatabaseHelper.getMinVoaId();
    }

    public Observable<Integer> getMaxVoaId() {
        return mDatabaseHelper.getMaxVoaId();
    }

    public Observable<List<Voa>> getVoa(int category, String level, int pageNum, int pageSize) {
        return mDatabaseHelper.getVoa(category, level, pageNum, pageSize);
    }

    public Observable<List<Voa>> getVoa(String category, String level, int pageNum, int pageSize) {
        return mDatabaseHelper.getVoa(category, level, pageNum, pageSize);
    }

    public Observable<Voa> getVoaById(int voaId) {
        return mDatabaseHelper.getVoaById(voaId);
    }

    public Observable<AddReadCountResponse> addReadCount(int voaId) {
        return mOtherService.addReadCount(OtherService.AddReadCount.Param.Value.PROTOCOL,
                OtherService.AddReadCount.Param.Value.COUNT, OtherService.AddReadCount.Param.Value.FORMAT, voaId);
    }

    public Observable<List<Voa>> getRecommendList(final int voaId, int category, int pageNum, int pageSize) {
        return mDatabaseHelper.getRecommendList(category, pageNum, pageSize)
                .map(new Func1<List<Voa>, List<Voa>>() {
                    @Override
                    public List<Voa> call(List<Voa> voas) {
                        Iterator<Voa> it = voas.iterator();
                        while (it.hasNext()) {
                            Voa voa = it.next();
                            if (voa.voaId() == voaId) {
                                it.remove();
                            }
                        }
                        return voas;
                    }
                });
    }

    public Observable<List<Voa>> getSeriesList(final int voaId, int series, int pageNum, int pageSize) {
        return mDatabaseHelper.getSeriesList(series, pageNum, pageSize)
                .map(new Func1<List<Voa>, List<Voa>>() {
                    @Override
                    public List<Voa> call(List<Voa> voas) {
                        Iterator<Voa> it = voas.iterator();
//                        while (it.hasNext()) {
//                            Voa voa = it.next();
//                            if (voa.voaId() == voaId) {
//                                it.remove();
//                            }
//                        }
                        return voas;
                    }
                });
    }

    public Observable<List<LoopItem>> getLunboItems(String type) {
        return mLoopService.getLoopInfo(type)
                .map(new Func1<GetLoopResponse, List<LoopItem>>() {
                    @Override
                    public List<LoopItem> call(GetLoopResponse getLoopResponse) {
                        return getLoopResponse.data();
                    }
                });
    }


    public Observable<GetRankingResponse> getRanking(int voaId, int sort, int pageNum, int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put(ThumbsService.GetRanking.Param.Key.PLATFORM, ThumbsService.GetRanking.Param.Value.PLATFORM);
        map.put(ThumbsService.GetRanking.Param.Key.FORMAT, ThumbsService.GetRanking.Param.Value.FORMAT);
        map.put(ThumbsService.GetRanking.Param.Key.PROTOCOL, String.valueOf(ThumbsService.GetRanking.Param.Value.PROTOCOL));
        map.put(ThumbsService.GetRanking.Param.Key.VOA_ID, String.valueOf(voaId));
        map.put(ThumbsService.GetRanking.Param.Key.PAGE_NUM, String.valueOf(pageNum));
        map.put(ThumbsService.GetRanking.Param.Key.PAGE_COUNT, String.valueOf(pageSize));
        map.put(ThumbsService.GetRanking.Param.Key.SORT, String.valueOf(sort));
        map.put(ThumbsService.GetRanking.Param.Key.TOPIC, ThumbsService.GetRanking.Param.Value.TOPIC);
        map.put(ThumbsService.GetRanking.Param.Key.SELECT_TYPE, ThumbsService.GetRanking.Param.Value.SELECT_TYPE);
        return mThumbsService.getThumbRanking(map);
    }

    public Observable<GetRankingResponse> getThumbsRanking(int voaId, int pageNum, int pageSize) {
        return getRanking(voaId, ThumbsService.GetRanking.Param.Value.SORT_BY_SCORE, pageNum, pageSize);
    }

    public Observable<ThumbsResponse> doAgree(int uid, int id) {
        return mThumbsService.doThumbs(ThumbsService.DoThumbs.Param.Value.AGREE_PROTOCOL, uid, id);
    }

    public Observable<ThumbsResponse> doAgainst(int uid, int id) {
        return mThumbsService.doThumbs(ThumbsService.DoThumbs.Param.Value.AGAINST_PROTOCOL, uid, id);
    }

    public Observable<Pair<Integer, List<Comment>>> getComments(int voaId, int commentId, int pageNum, int pageSize) {
        return getComments(voaId, commentId, CommentService.SendComment.Param.Value.SORT_BY_DATE, pageNum, pageSize);
    }

    public Observable<Pair<Integer, List<Comment>>> getComments(int voaId, int commentId, int sort, int pageNum, int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put(CommentService.GetComment.Param.Key.PLATFORM, CommentService.GetComment.Param.Value.PLATFORM);
        map.put(CommentService.GetComment.Param.Key.FORMAT, CommentService.GetComment.Param.Value.FORMAT);
        map.put(CommentService.GetComment.Param.Key.PROTOCOL, String.valueOf(CommentService.GetComment.Param.Value.PROTOCOL));
        map.put(CommentService.GetComment.Param.Key.VOA_ID, String.valueOf(voaId));
        map.put(CommentService.GetComment.Param.Key.PAGE_NUM, String.valueOf(pageNum));
        map.put(CommentService.GetComment.Param.Key.PAGE_COUNT, String.valueOf(pageSize));
        map.put(CommentService.GetComment.Param.Key.BACK_ID, String.valueOf(commentId));
        map.put(CommentService.GetComment.Param.Key.SORT, String.valueOf(sort));
        map.put(CommentService.GetComment.Param.Key.TOPIC, CommentService.GetComment.Param.Value.TOPIC);

        return mCommentService.getComments(map).map(new Func1<GetCommentResponse, Pair<Integer, List<Comment>>>() {
            @Override
            public Pair<Integer, List<Comment>> call(GetCommentResponse response) {
                if (response.data() == null) {
                    return new Pair<>(0, null);
                }
                return new Pair<>(response.counts(), response.data());
            }
        });
    }

    // type  1: 英文 2 ： 中文  0 ：双语
    public Observable<PdfResponse> getPdf(String type, int voaId, int languane) {
        return mPdfService.getPdf(type, voaId, languane);
    }

    public Observable<GetIpResponse> getIpRequest(String userId) {
        return mPdfService.getIpRequest(userId, "" + App.APP_ID);
    }

    public Observable<GetCommentResponse> sendTextComment(int uid, String username, int voaId, int rankingId, String content) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put(CommentService.SendComment.Param.Key.BACK_ID, fromString(String.valueOf(rankingId)));
        map.put(CommentService.SendComment.Param.Key.TOPIC, fromString(CommentService.SendComment.Param.Value.TOPIC));
        map.put(CommentService.SendComment.Param.Key.PLATFORM, fromString(CommentService.SendComment.Param.Value.PLATFORM));
        map.put(CommentService.SendComment.Param.Key.FORMAT, fromString(CommentService.SendComment.Param.Value.FORMAT));
        map.put(CommentService.SendComment.Param.Key.PROTOCOL, fromString(String.valueOf(CommentService.SendComment.Param.Value.PROTOCOL)));
        map.put(CommentService.SendComment.Param.Key.USER_ID, fromString(String.valueOf(uid)));
        map.put(CommentService.SendComment.Param.Key.VOA_ID, fromString(String.valueOf(voaId)));
        map.put(CommentService.SendComment.Param.Key.USER_NAME, fromString(String.valueOf(username)));
        map.put(CommentService.SendComment.Param.Key.CONTENT, fromString(content));
        map.put(CommentService.SendComment.Param.Key.SHUOSHUO_TYPE, fromString(String.valueOf(CommentService.SendComment.Param.Value.SHUOSHUO_TYPE_WORDS)));
        map.put(CommentService.SendComment.Param.Key.SHOW, fromString(CommentService.SendComment.Param.Value.SHOW));
        return mCommentService.sendTextComment(map);
    }

    public Observable<SendCommentResponse> sendVoiceComment(int uid, String username, int voaId, int rankingId, File file) {
        Map<String, RequestBody> optionMap = new HashMap<>();
        optionMap.put(CommentService.SendComment.Param.Key.BACK_ID, fromString(String.valueOf(rankingId)));
        optionMap.put(CommentService.SendComment.Param.Key.TOPIC, fromString(CommentService.SendComment.Param.Value.TOPIC));
        optionMap.put(CommentService.SendComment.Param.Key.PLATFORM, fromString(CommentService.SendComment.Param.Value.PLATFORM));
        optionMap.put(CommentService.SendComment.Param.Key.FORMAT, fromString(CommentService.SendComment.Param.Value.FORMAT));
        optionMap.put(CommentService.SendComment.Param.Key.PROTOCOL, fromString(String.valueOf(CommentService.SendComment.Param.Value.PROTOCOL)));
        optionMap.put(CommentService.SendComment.Param.Key.USER_ID, fromString(String.valueOf(uid)));
        optionMap.put(CommentService.SendComment.Param.Key.USER_NAME, fromString(username));
        optionMap.put(CommentService.SendComment.Param.Key.VOA_ID, fromString(String.valueOf(voaId)));
        optionMap.put(CommentService.SendComment.Param.Key.SHUOSHUO_TYPE, fromString(String.valueOf(CommentService.SendComment.Param.Value.SHUOSHUO_TYPE_VOICE)));
        return mCommentService.sendVoiceComment(optionMap, fromFile(file));
    }

    public Observable<SendDubbingResponse> sendDubbingComment(Map<Integer, WavListItem> recordItems
            , int uid, String username, int voaId, String sound, int score, int cat) {
        Map<String, String> map = new HashMap<>();
        map.put(CommentService.SendComment.Param.Key.PROTOCOL, String.valueOf(CommentService.SendComment.Param.Value.PROTOCOL));
        map.put(CommentService.SendComment.Param.Key.CONTENT, "3");
        map.put(CommentService.SendComment.Param.Key.USER_ID, String.valueOf(uid));
        PostItem item = new PostItem();
        item.setAppName(App.APP_NAME_EN);
        item.setFlag(CommentService.SendComment.Param.Value.FLAG);
        item.setFormat(CommentService.SendComment.Param.Value.FORMAT);
        item.setParaId(0);
        item.setIdIndex(0);
        item.setPlatform(CommentService.SendComment.Param.Value.PLATFORM);
        item.setScore(score);
        item.setShuoshuotype(3);
        item.setSound(sound);
        item.setTopic(Constant.EVAL_TYPE);
        item.setUsername(username);
        item.setVoaid(voaId);
        item.setCategory(cat);
        item.setWavListItems(buildList(recordItems));
        Timber.e("@@@SendDubbingResponse ~~");
        return mCommentService.sendDubbingComment(map, getBody(item));
    }

    private RequestBody getBody(PostItem item) {
        Gson gson = new Gson();
        String json = gson.toJson(item);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }


    private List<WavListItem> buildList(Map<Integer, WavListItem> recordItems) {
        List<WavListItem> requests = new ArrayList<>();
        for (Integer i : recordItems.keySet()) {
            requests.add(recordItems.get(i));
        }
        Collections.sort(requests);
        return requests;
    }


    private MultipartBody.Part fromFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    private RequestBody fromString(String text) {
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }

    public Observable<UploadImageResponse> uploadImage(int uid, File file, ProgressListener listener) {
//        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, listener);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return mUserService.uploadImage(uid, requestFile);
    }

    public Observable<List<VoaText>> syncVoaTexts(final int voaId) {
        return mVoaService.getVoaTexts(VoaService.GetVoaText.Param.Value.FORMAT, voaId)
                .concatMap(new Func1<VoaTextResponse, Observable<List<VoaText>>>() {
                    @Override
                    public Observable<List<VoaText>> call(VoaTextResponse voaTextResponse) {
                        return mDatabaseHelper.setVoaTexts(voaTextResponse.voaTexts(), voaId).toList();
                    }
                });
    }

    public Observable<List<VoaText>> getVoaTexts(final int voaId) {
        return mDatabaseHelper.getVoaTexts(voaId);
    }

    public Observable<SeriesData> getSeriesById(String seriesId) {
        return mDatabaseHelper.getSeriesById(seriesId);
    }

//    public com.iyuba.talkshow.data.model.SeriesResponse.DataBean getSeriesById(final int seriesId) {
//        return mDatabaseHelper.getSeriesById(seriesId);
//    }

    public Observable<List<Record>> getDraftRecord() {
        return mDatabaseHelper.getDraftRecord();
    }

    public Observable<List<Record>> getDraftRecord(long mTimeStamp) {
        return mDatabaseHelper.getDraftRecord(mTimeStamp);
    }

    public List<Record> getRecordByVoaId(int voaid) {
        return mDatabaseHelper.getRecordbyVoaId(voaid);
    }

    public List<VoaSoundNew> getVoaSoundVoaId(int voaid) {
        return mDatabaseHelper.getVoaSoundVoaId(voaid);
    }

    public List<VoaSoundNew> getVoaSoundVoaTime(int voaid, long timestamp) {
        return mDatabaseHelper.getVoaSoundVoaTime(voaid, timestamp);
    }

    public List<VoaSoundNew> getVoaSoundItemUid(int uid, long itemid) {
        return mDatabaseHelper.getVoaSoundItemUid(uid, itemid);
    }

    public List<VoaSoundNew> getVoaSoundItemid(int itemid) {
        return mDatabaseHelper.getVoaSoundItemid(itemid);
    }

    public List<VoaSoundNew> getVoaSoundItemid(long itemid,int uid) {
        return mDatabaseHelper.getVoaSoundItemid(itemid,uid);
    }

    public List<VoaSoundNew> getVoaSoundVoaId(int voaId,int uid) {
        return mDatabaseHelper.getVoaSoundVoaId(voaId,uid);
    }

    public int checkDbUpgrade() {
        Log.e("DataManager", "checkDbUpgrade db_upgrade = " + mPreferencesHelper.loadInt("db_upgrade", 0));
        if (mPreferencesHelper.loadInt("db_upgrade", 0) == 0) {
            int result = mDatabaseHelper.checkDbUpgrade();
            mPreferencesHelper.putInt("db_upgrade", result);
            Log.e("DataManager", "mDatabaseHelper.checkDbUpgrade = " + result);
            return result;
        }
        return 0;
    }

    public Observable<Boolean> saveVoaSound(VoaSoundNew record) {
        return mDatabaseHelper.saveVoaSound(record);
    }
    public Observable<GetMyDubbingResponse> getMyDubbing(int uid) {
        return mThumbsService.getMyDubbing(uid, App.APP_ID, Constant.EVAL_TYPE);
    }

    public Observable<List<Record>> getUnreleasedData() {
        return mDatabaseHelper.getFinishedRecord();
    }

    public Observable<Boolean> deleteRecord(List<String> timestamps) {
        return mDatabaseHelper.deleteRecord(timestamps);
    }

    public Observable<Boolean> deleteRecord(long timestamp) {
        return mDatabaseHelper.deleteRecord(timestamp);
    }

    public Observable<Boolean> saveRecord(Record record) {
        return mDatabaseHelper.saveRecord(record);
    }

    public Observable<Boolean> saveCollect(Collect collect) {
        return mDatabaseHelper.saveCollect(collect);
    }

    public Observable<Boolean> deleteCollect(int uid, List<String> list) {
        return mDatabaseHelper.deleteCollect(uid, list);
    }

    public Observable<Boolean> deleteCollect(int uid, int voaId) {
        return mDatabaseHelper.deleteCollect(uid, voaId);
    }

    public Observable<List<Collect>> getCollect(int uid) {
        return mDatabaseHelper.getCollect(uid);
    }

    public Observable<Integer> getCollectByVoaId(int voaId) {
        return mDatabaseHelper.getCollectByVoaId(voaId);
    }

    //保存配音的文件下载信息
    public Observable<Boolean> saveDownload(Download download) {
        return mDatabaseHelper.saveDownload(download);
    }

    //删除配音的文件下载信息
    public Observable<Boolean> deleteDownload(List<String> list) {
        return mDatabaseHelper.deleteDownload(list);
    }

    //获取所有的配音下载信息
    public Observable<List<Download>> getDownload(int uid) {
        return mDatabaseHelper.getDownload(uid);
    }

    //获取单个配音文件下载信息
    public Download getSingleDownload(int uid,int voaId){
        return mDatabaseHelper.getSingleDownload(uid, voaId);
    }

    public Observable<LoginResponse> login(String username, String password,
                                           double latitude, double longitude) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.Login.Param.Key.PROTOCOL, String.valueOf(UserService.Login.Param.Value.PROTOCOL));
        map.put(UserService.Login.Param.Key.USERNAME, URLEncoder.encode(username));
        map.put(UserService.Login.Param.Key.PASSWORD, MD5.getMD5ofStr(password));
        map.put(UserService.Login.Param.Key.LONGITUDE, String.valueOf(longitude));
        map.put(UserService.Login.Param.Key.LATITUDE, String.valueOf(latitude));
        map.put(UserService.Login.Param.Key.APP_ID, String.valueOf(App.APP_ID));
        map.put(UserService.Login.Param.Key.SIGN, Constant.User.getLoginSign(username, password));
        map.put(UserService.Login.Param.Key.FORMAT, UserService.Login.Param.Value.FORMAT);
        return mUserService.login(map);
    }

    public Observable<GetUserResponse> getUser(String username) {
        return mUserService.getUser(UserService.GetUser.Param.Value.PROTOCOL, username, App.APP_ID);
    }

    public Observable<RegisterResponse> registerByEmail(String username, String password, String email) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.Register.Param.Key.APP, App.APP_NAME_EN);
        map.put(UserService.Register.Param.Key.PROTOCOL, String.valueOf(UserService.Register.Param.Value.PROTOCOL));
        map.put(UserService.Register.Param.Key.EMAIL, email);
        map.put(UserService.Register.Param.Key.USERNAME, URLEncoder.encode(username));
        map.put(UserService.Register.Param.Key.PASSWORD, MD5.getMD5ofStr(password));
        map.put(UserService.Register.Param.Key.PLATFORM, App.PLATFORM);
        map.put(UserService.Register.Param.Key.FORMAT, UserService.Register.Param.Value.FORMAT);
        map.put(UserService.Register.Param.Key.SIGN, Constant.User.getRegisterByEmailSign(username, password, email));
        return mUserService.registerByEmail(map);
    }

    public Observable<RegisterResponse> registerByPhone(String username, String password, String mobile) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.Register.Param.Key.APP, App.APP_NAME_EN);
        map.put(UserService.Register.Param.Key.PLATFORM, App.PLATFORM);
        map.put(UserService.Register.Param.Key.PROTOCOL, String.valueOf(UserService.Register.Param.Value.PROTOCOL));
        map.put(UserService.Register.Param.Key.FORMAT, UserService.Register.Param.Value.FORMAT);
        map.put(UserService.Register.Param.Key.USERNAME, URLEncoder.encode(username));
        map.put(UserService.Register.Param.Key.PASSWORD, MD5.getMD5ofStr(password));
        map.put(UserService.Register.Param.Key.SIGN, Constant.User.getRegisterByPhoneSign(username, password));
        map.put(UserService.Register.Param.Key.MOBILE, mobile);
        return mUserService.registerByPhone(map);
    }

    public Observable<RegisterMobResponse> registerByMob(String token, String opToken, String operator) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.Register.Param.Key.PROTOCOL, "10010");
        try {
            map.put("token", URLEncoder.encode(token, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("token", token);
        }
        map.put("opToken", opToken);
        map.put("operator", operator);
        map.put(UserService.Login.Param.Key.APP_ID, String.valueOf(App.APP_ID));
        map.put("appkey", ConfigData.mob_key);
        return mUserService.registerByMob(map);
    }
    public Observable<ChangeNameResponse> ChangeUserName(String uid, String username, String oldUsername) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.Register.Param.Key.PROTOCOL, "10012");
        map.put("uid", uid);
        map.put("username", username);
        map.put("oldUsername", oldUsername);
        try {
            map.put("username", URLEncoder.encode(username, "UTF-8"));
            map.put("oldUsername", URLEncoder.encode(oldUsername, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put(UserService.Register.Param.Key.SIGN, MD5.getMD5ofStr("10012" + uid  + "iyubaV2"));
        return mUserService.ChangeUserName(map);
    }

    public Observable<CheckAmountResponse> checkAmount(int uid) {
        return mPayService.checkAmount(uid);
    }

    public Observable<ShareInfoResponse> getShareInfo(int uid, int appid, int pageNum, int pageSize) {
        return mPayService.getShareInfo(uid, appid, pageNum, pageSize);
    }
    public Observable<ShareInfoResponse> getCalendar(int uid, int appid, String time) {
        return mPayService.getCalendar(uid, appid, time);
    }

    public Observable<GetVerifyCodeResponse> getVerifyCode(String phone) {
        return mVerifyCodeService.getCode(phone, VerifyCodeService.GetCode.Param.Value.FORMAT);
    }

    public Observable<AppUpdateResponse> checkVersion(int version) {
        return mVersionService.checkVersion(version, VersionService.CheckVersion.Param.Value.FORMAT);
    }

    public Observable<FeedbackResponse> submitFeedback(int uid, String email, String content) {
        Map<String, String> map = new HashMap<>();
        map.put(FeedbackService.DoFeedback.Param.Key.UID, String.valueOf(uid));
        map.put(FeedbackService.DoFeedback.Param.Key.EMAIL, email);
        map.put(UserService.Register.Param.Key.PROTOCOL, "91001");
        map.put(UserService.Register.Param.Key.APP, App.APP_NAME_EN);
        map.put(UserService.Register.Param.Key.PLATFORM, App.PLATFORM);
        try {
            map.put(FeedbackService.DoFeedback.Param.Key.CONTENT, URLEncoder.encode(content, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            map.put(FeedbackService.DoFeedback.Param.Key.CONTENT, content);
            e.printStackTrace();
        }
        map.put(FeedbackService.DoFeedback.Param.Key.FORMAT, FeedbackService.DoFeedback.Param.Value.FORMAT);
        return mFeedbackService.doFeedback(map);
    }

    public Observable<GetAliPayInfoResponse> getAliPayInfo(int uid, int amount, int productId,
                                                           String outTradeNo, String subject,
                                                           String fee, String body) {
        Map<String, String> map = new HashMap<>();
        map.put(VipService.GetAliPayInfo.Param.Key.WID_DEFAULT_BANK, VipService.GetAliPayInfo.Param.Value.WID_DEFAULT_BANK);
        map.put(VipService.GetAliPayInfo.Param.Key.WID_SHOW_URL, VipService.GetAliPayInfo.Param.Value.WID_SHOW_URL);
        map.put(VipService.GetAliPayInfo.Param.Key.WID_SELLER_EMAIL, VipService.GetAliPayInfo.Param.Value.WID_SELLER_EMAIL);
        map.put(VipService.GetAliPayInfo.Param.Key.WID_OUT_TRADE_NO, outTradeNo);
        map.put(VipService.GetAliPayInfo.Param.Key.WID_SUBJECT, subject);
        map.put(VipService.GetAliPayInfo.Param.Key.WID_TOTAL_FEE, fee);
        map.put(VipService.GetAliPayInfo.Param.Key.WID_BODY, body);
        map.put(VipService.GetAliPayInfo.Param.Key.APP_ID, String.valueOf(App.APP_ID));
        map.put(VipService.GetAliPayInfo.Param.Key.USER_ID, String.valueOf(uid));
        map.put(VipService.GetAliPayInfo.Param.Key.AMOUNT, String.valueOf(amount));
        map.put(VipService.GetAliPayInfo.Param.Key.PRODUCT_ID, String.valueOf(productId));
        map.put(VipService.GetAliPayInfo.Param.Key.CODE, VipService.GetAliPayInfo.getCode(uid));
        return mVipService.getAliPayInfo(map, null);
    }

    public Observable<GetAliPayResponse> getAliPay(int uid, int amount, int productId,
                                                   String subject, String fee, String body,long deducation) {
        Map<String, String> map = new HashMap<>();
        map.put(VipService.GetAliPayInfo.Param.Key.APP_ID, String.valueOf(App.APP_ID));
        map.put(VipService.GetAliPayInfo.Param.Key.USER_ID, String.valueOf(uid));
        map.put(VipService.GetAliPayInfo.Param.Key.CODE, VipService.GetAliPayInfo.getCode(uid));
        map.put(VipService.GetAliPayInfo.Param.Key.WID_TOTAL_FEE, fee);
        map.put(VipService.GetAliPayInfo.Param.Key.AMOUNT, String.valueOf(amount));
        map.put(VipService.GetAliPayInfo.Param.Key.PRODUCT_ID, String.valueOf(productId));
        map.put(VipService.GetAliPayInfo.Param.Key.WID_BODY, body);

        //增加抵扣信息
        map.put(VipService.GetAliPayInfo.Param.Key.DEDUCATION,String.valueOf(deducation));
        try {
            map.put(VipService.GetAliPayInfo.Param.Key.WID_BODY, URLEncoder.encode(body, "UTF-8"));
            map.put(VipService.GetAliPayInfo.Param.Key.WID_SUBJECT, URLEncoder.encode(subject, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mVipService.getAliPay(map);
    }

    public Observable<NotifyAliPayResponse> NotifyAliPay(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("data", body);
        return mVipService.NotifyAliPay(map);
    }

    //原来的微信接口
    /*public Observable<GetWXPayInfoResponse> getWechatPayInfo(int uid, int amount, int productId,
                                                             String outTradeNo, String subject,
                                                             String fee, String body) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(uid));
        map.put("weixinApp", ConfigData.wx_key);
        map.put("appid", String.valueOf(App.APP_ID));
        map.put("money", fee);
        map.put("amount", String.valueOf(amount));
        map.put("productid", String.valueOf(productId));
//        map.put("weixinApp", String.valueOf("wxd698c6b5372fd6d0"));
        map.put("sign", generateSign(String.valueOf(App.APP_ID), String.valueOf(uid), fee, String.valueOf(amount)));
        map.put("format", "json");
        //增加抵扣信息
        map.put("deduction",String.valueOf(deduction));

        return mVipService.getWechatPayInfo(map, null);
    }*/

    //从小学英语上复制过来的接口
    public Observable<GetWXPayInfoResponse> getWechatPayInfo(int uid, int amount, int productId,
                                                             String outTradeNo, String subject,
                                                             String fee, String body,long deduction) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(uid));
//        map.put("weixinApp", App.WX_KEY);
        map.put("weixinApp", ConfigData.wx_key);
        map.put("appid", String.valueOf(App.APP_ID));
        map.put("money", fee);
        map.put("amount", String.valueOf(amount));
        map.put("productid", String.valueOf(productId));
        map.put("sign", generateSign(String.valueOf(App.APP_ID), String.valueOf(uid), fee, String.valueOf(amount)));
        map.put("format", "json");
        //增加抵扣信息
        map.put("deduction",String.valueOf(deduction));
        try {
            map.put("body",URLEncoder.encode(body, "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }

        return mVipService.getWechatPayInfo(map, null);
    }

    public Observable<AllWordsRespons> getAllWords() {
        return mWordTestServiece.getAllWords();
    }

    public Observable<AllWordsRespons> getAllWordsByType(String appName) {
        return mWordTestServiece.getAllWordsByType(appName);
    }

    public Observable<AllWordsRespons> getWordsByBookId(String bookId) {
        return mWordTestServiece.getWordsByBookId(bookId);
    }

    public Observable<AllWordsRespons> getWordsByBookUnit(String bookId, String unit) {
        return mWordTestServiece.getWordsByBookUnit(bookId, unit);
    }

    public Observable<UserData> getUserInfo(int uid, int myid) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.GetUserBasicInfo.Param.Key.PROTOCOL, "20001");
        map.put(UserService.GetUserBasicInfo.Param.Key.PLATFORM, App.PLATFORM);
        map.put(UserService.GetUserBasicInfo.Param.Key.FORMAT, UserService.GetUserBasicInfo.Param.Value.FORMAT);
        map.put(UserService.GetUserBasicInfo.Param.Key.ID, String.valueOf(uid));
        map.put("myid", String.valueOf(myid));
        map.put("appid", App.APP_ID + "");
        map.put(UserService.GetUserBasicInfo.Param.Key.SIGN, Constant.User.getUserInfoSign(uid));
        return mUserService.userInfoApi(map);
    }

    public Observable<GetUserBasicInfoResponse> getUserBasicInfo(int uid) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.GetUserBasicInfo.Param.Key.PROTOCOL, UserService.GetUserBasicInfo.Param.Value.PROTOCOL);
        map.put(UserService.GetUserBasicInfo.Param.Key.PLATFORM, App.PLATFORM);
        map.put(UserService.GetUserBasicInfo.Param.Key.FORMAT, UserService.GetUserBasicInfo.Param.Value.FORMAT);
        map.put(UserService.GetUserBasicInfo.Param.Key.ID, String.valueOf(uid));
        map.put(UserService.GetUserBasicInfo.Param.Key.SIGN, Constant.User.getUserBasicInfoSign(uid));
        return mUserService.getUserBasicInfo(map);
    }

    public Observable<EditUserBasicInfoResponse> editUserBasicInfo(int uid, String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.EditUserBasicInfo.Param.Key.PALTFORM, App.PLATFORM);
        map.put(UserService.EditUserBasicInfo.Param.Key.FORMAT, UserService.EditUserBasicInfo.Param.Value.FORMAT);
        map.put(UserService.EditUserBasicInfo.Param.Key.PROTOCOL, UserService.EditUserBasicInfo.Param.Value.PROTOCOL);
        map.put(UserService.EditUserBasicInfo.Param.Key.ID, String.valueOf(uid));
        map.put(UserService.EditUserBasicInfo.Param.Key.SIGN, Constant.User.editUserBasicInfoSign(uid));
        map.put(UserService.EditUserBasicInfo.Param.Key.KEY, key);
        map.put(UserService.EditUserBasicInfo.Param.Key.VALUE, TextAttr.encode(value));
        return mUserService.editUserBasicInfo(map);
    }

    public Observable<ImproveUserResponse> improveUserInfo(int uid, String gender, String age, String province, String city, String title) {
        Map<String, String> map = new HashMap<>();
        map.put(UserService.EditUserBasicInfo.Param.Key.PALTFORM, App.PLATFORM);
        map.put(UserService.EditUserBasicInfo.Param.Key.FORMAT, UserService.EditUserBasicInfo.Param.Value.FORMAT);
        map.put(UserService.EditUserBasicInfo.Param.Key.SIGN, MD5.getMD5ofStr("iyubaV2" + uid));
        map.put(UserService.EditUserBasicInfo.Param.Key.PROTOCOL, "99010");
        map.put("userid", String.valueOf(uid));
        if ("男生".equalsIgnoreCase(gender)) {
            map.put("gender", "1");
        } else {
            map.put("gender", "0");
        }
        map.put("age", age);
        map.put("appid", App.APP_ID + "");
        map.put("resideprovince", ParameterUrl.encode(ParameterUrl.encode(province)));
        map.put("residecity", ParameterUrl.encode(ParameterUrl.encode(city)));
        map.put("occupation", ParameterUrl.encode(ParameterUrl.encode(title)));
        return mUserService.improveUserInfo(map);
    }

    public Observable<GetLocationResponse> getLocation(double latitude, double longitude) {
        return mLocationService.getLocation(
                LocationService.GetLocation.Param.Value.getLatLng(
                        String.valueOf(latitude), String.valueOf(longitude)),
                LocationService.GetLocation.Param.Value.SENSOR,
                LocationService.GetLocation.Param.Value.LANGUAGE);
    }

    public Observable<List<University>> searchSchool(String keyword, int size) {
        return mDatabaseHelper.getUniversity(keyword, size);
    }

    public Observable<List<University>> getallSchools() {
        return mDatabaseHelper.getAllUniversity();
    }

    public Observable<GetAdResponse> getAd() {
        return mAdService.getAd(App.APP_ID, AdService.GetAd.Param.Value.FLAG);
    }

    public List<Thumb> getCommentThumb(int uid, int commentId) {
        return mDatabaseHelper.getCommentThumb(uid, commentId);
    }

    public Observable<Thumb> getThumb(int uid, int commentId) {
        return mDatabaseHelper.getThumb(uid, commentId);
    }

    public Observable<Boolean> insertThumb(Thumb thumb) {
        return mDatabaseHelper.insertThumb(thumb);
    }

    public Observable<Boolean> updateThumb(Thumb thumb) {
        return mDatabaseHelper.updateThumb(thumb);
    }

    public Observable<Boolean> deleteSub(Thumb thumb) {
        return mDatabaseHelper.deleteThumb(thumb.uid(), thumb.commentId());
    }

    public Observable<RankingListBean> getRankingList(
            int uid,
//            String topic,
            String type,
//         String type,
//            String appName,
            int start,
            int total
    ) {
        //  uid+topic+topicid+start+total+YYYY-MM-DD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = sdf.format(new Date());
        String sign = MD5.getMD5ofStr(uid + "voa" + "0" + start + total + date);
        return mRankingService.getRankinglList(uid, type, start, total, sign);
    }

    public UploadStudyRecordService getUploadStudyRecordService() {
        return mUploadStudyRecordService;
    }

    public int getVoaDownloadNumber() {
        int number = getPreferencesHelper().loadInt("voa_free_download_number", 0);
        Timber.e("free download " + number);
        return number;
    }

    public void addVoaDownloadNumber() {
        int number = getVoaDownloadNumber();
        Timber.e("free download +1 :  " + (number + 1));
        getPreferencesHelper().putInt("voa_free_download_number", number + 1);
    }

    public Observable<IntegralBean> deductIntegral(String flag, int uid, int appid, int idindex) {
        //更换扣除积分的接口
        String baseUrl = "http://api."+Constant.Web.WEB_SUFFIX+"credits/updateScore.jsp?srid=40&mobile=1";
        return mIntegralService.deductIntegral(baseUrl,flag, uid, appid, idindex);
    }

    public Observable<Object> deleteReleaseRecord(Integer s) {
        return mOtherService.deleteReleaseRecord(s);
    }

    public Observable<SearchListBean> searchVoa(String key, int start, int total, int appId) {
        return mVoaService.searchVoa(key, start, total, appId);
    }

    public Observable<List<Voa>> searchLocal(String key) {
        return mDatabaseHelper.searchLocalVoa(key);
    }

    public IntegralService getIntegralService() {
        return mIntegralService;
    }

    public Observable<WordResponse> getWordOnNet(String key) {

        return mWordService.getNetWord(key);

    }

    public Observable<SeriesResponse> getSeries(String key) {
        String type = "series";
        String sign = buildSign(type);
        return mMovieService.getSeries(key, type, "200", sign, "json");
    }

    public Observable<List<Voa>> getSeriesLocal(String key) {
        return mDatabaseHelper.getVoasBySeriesId(key);
    }

    public Observable<com.iyuba.talkshow.data.model.SeriesResponse> syncSeriesList(int uid) {
//        String type = "309,313,314,315,316,317,318,319,320";
        String sign = buildSign(Constant.PRIMARY_TYPE);

        Timber.e("@@@SendDubbingResponse ~~");
        return mCmsService.getDramas(Constant.PRIMARY_TYPE, App.APP_ID, uid, sign, "json");
    }

    public Observable<SeriesResponse> syncSeries4Category(String type, int uid) {
        String sign = buildSign(type);
        Timber.e("@@@syncVoa4Series ~~");
        return mCmsService.getDramas(type, App.APP_ID, uid, sign, "json");
    }

    public Observable<List<SeriesData>>  getSeries4Ids() {
        Timber.e("@@@getSeries4Ids ~~");
        return mDatabaseHelper.getSeriesList4Ids(Constant.PRIMARY_TYPE);
    }

    public Observable<com.iyuba.talkshow.data.model.TitleSeriesResponse> getTitleSeriesList(int series) {
        String sign = buildSign("series");
//        return mCmsService.getTitleSeries("title", series, sign, "json");
        return mFixService.getTitleSeries("title", series, sign, "json");
    }

//    public Observable<com.iyuba.talkshow.data.model.TitleSeriesResponse> SyncTitleSeries(String series, int uid, int appid) {
//        String sign = buildSign("series");
//        return mVoaService.getTitleSeries("title", series, uid, appid, sign, "json");
//    }

    public long insertVoaDB(Voa bean) {
        return mDatabaseHelper.insertToVoa(bean);
    }

    private String buildSign(String type) {
        return com.iyuba.module.toolbox.MD5.getMD5ofStr("iyuba" + buildTimeStamp() + type);
    }

    private String buildTimeStamp() {
        long timeStamp = new Date().getTime() / 1000 + 3600 * 8; //东八区;
        long days = timeStamp / 86400;
        return Long.toString(days);
    }

    public void deleteVoa4Category(String bean) {
        mDatabaseHelper.deleteVoa4Category(bean);
    }

    public void deleteVoa4Series(String bean) {
        mDatabaseHelper.deleteVoa4Series(bean);
    }

    public void deleteSeries4Category(String bean) {
        mDatabaseHelper.deleteSeries4Category(bean);
    }

    public void insertSeriesDB(SeriesData bean) {
        mDatabaseHelper.insertToSeries(bean);
    }

    //这个是从数据库中获取数据的，但是数据库中不一定存在数据，因此需要使用接口处理
    //下边是接口
    public Observable<List<SeriesData>> getSeriesList(String cat) {
        return mDatabaseHelper.getSeriesList(cat);
    }

    //这个是上边的接口
    public Observable<NewSeriesDataResponse> getSeriesList2(String cat) {
        String sign = buildSign("series");
        return mFixService.getTitleSeries2("category", cat, sign);
    }

    public Single<Boolean> deleteWords(int userId, List<String> words) {
        String wordsStr = buildUpdateWords(words);
        return mWordCollectService.updateWords(userId, "delete", "Iyuba", wordsStr)
                .compose(this.<WordCollectResponse.Update, Boolean>applyParser());
    }

    public Single<Boolean> insertWords(int userId, List<String> words) {
        String wordsStr = buildUpdateWords(words);
        return mWordCollectService.updateWords(userId, "insert", "Iyuba", wordsStr)
                .compose(this.<WordCollectResponse.Update, Boolean>applyParser());
    }

    public Single<Pair<List<Word>, Integer>> getNoteWords(int useId, final int pageNumber, int pageCounts) {
        return mWordCollectService.getNoteWords(useId, pageNumber, pageCounts)
                .flatMap(new Function<WordCollectResponse.GetNoteWords, SingleSource<? extends Pair<List<Word>, Integer>>>() {
                    @Override
                    public SingleSource<? extends Pair<List<Word>, Integer>> apply(WordCollectResponse.GetNoteWords response) throws Exception {
                        if (pageNumber <= response.lastPage && response.tempWords.size() > 0) {
                            List<Word> words = new ArrayList<>(response.tempWords.size());
                            for (WordCollectResponse.GetNoteWords.TempWord tempWord : response.tempWords) {
                                words.add(new Word(tempWord.word, tempWord.audioUrl,
                                        tempWord.pronunciation, tempWord.definition));
                            }
                            return Single.just(new Pair<>(words, response.counts));
                        } else {
                            List<Word> words = new ArrayList<>();
                            return Single.just(new Pair<>(words, 0));
                        }
                    }
                });
    }

    private String buildUpdateWords(List<String> words) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.size(); i += 1) {
            if (i == 0) {
                sb.append(words.get(i));
            } else {
                sb.append(",").append(words.get(i));
            }
        }
        return sb.toString();
    }

    private static String generateSign(String appid, String uid, String money, String amount) {
        StringBuilder sb = new StringBuilder();
        sb.append(appid).append(uid).append(money).append(amount);
        sb.append(sdf.format(System.currentTimeMillis()));
        return MD5.getMD5ofStr(sb.toString());
    }

    private <T, R> SingleTransformer<T, R> applyParser() {
        return (SingleTransformer<T, R>) SingleParser.parseTransformer;
    }

    /*
    * 注销用户的功能
     */
    public Observable<ClearUserResponse> clearUser(String username, String password) {
        int protocol = 11005;
        String format = "json";
        String passwordMD5 = MD5.getMD5ofStr(password);
        String sign = Constant.User.getClearUserSign(protocol,username, password);
        return  mUserService.clearUser(protocol, username, passwordMD5, sign, format);
    }

    public Observable<List<SeriesData>> getSeriesListByIds(List<Integer> idList) {
        return mDatabaseHelper.getSeriesListFromIds(idList);
    }

//    //保存收藏时网络上的数据
//    public void saveNewVoa(Voa mVoa){
//        mDatabaseHelper.saveNewtVoa(mVoa);
//    }

    //判断video数据是否和全部数据一致
    public long getVoaVideoNullCount(){
        return mDatabaseHelper.getVoaVideoNullCount();
    }

    //下载文件
    public Observable<ResponseBody> downloadFile(String url){
        return mOtherService.downloadFile(url);
    }

    //获取app的审核状态
    public Observable<AppCheckResponse> getAppCheckStatus(int verifyCode) {
        String url = "http://api.qomolama.cn/getRegisterAll.jsp";

        //版本号
        String versionName = Util.getAppVersion(TalkShowApplication.getContext(),TalkShowApplication.getContext().getPackageName());
        //对于oppo旗下的手机单独处理，版本号为：oppo_version，这样进行提交
        if (OtherUtil.isBelongToOppoPhone()){
            versionName = "oppo_"+versionName;
        }

        return mAdService.getAppCheckStatus(url,verifyCode, versionName);
    }

    //获取小程序登陆-token
    public Observable<TokenBean> getToken(){
        String platform = App.PLATFORM;
        String format = "json";
        int protocol = 10011;
        int appId = App.APP_ID;
        String sign = MD5.getMD5ofStr(protocol+""+appId+"iyubaV2");

        return mUserService.getToken(platform,format,protocol,appId,sign);
    }

    //获取小程序登陆-uid
    public Observable<UidBean> getUid(String token){
        String platform = App.PLATFORM;
        String format = "json";
        int protocol = 10016;
        int appId = App.APP_ID;
        String sign = MD5.getMD5ofStr(protocol+""+appId+token+"iyubaV2");

        return mUserService.getUid(platform,format,protocol,token,appId,sign);
    }

    public List<Voa> getVoaXiaoxueByBookId(int bookId) {
        return mDatabaseHelper.getVoaXiaoxueByBookId(bookId);
    }

    public List<SeriesData> getSeriesCategory(int cat) {
        return mDatabaseHelper.getSeriesCategory(cat);
    }

    public Observable<LessonNewResponse> chooseLessonNew(int appid, int uid,String lessonType) {
        return mVoaService.chooseLessonNew(appid, uid, lessonType, 3);
    }

    //小学
    public Observable<BaseLessonResponse<PrimaryResponse>> choosePrimaryLessonNew(int appid, int uid, String lessonType) {
        return mVoaService.choosePrimaryLessonNew(appid, uid, lessonType, 3);
    }

    //初中
    public Observable<BaseLessonResponse<JuniorResponse>> chooseJuniorLessonNew(int appid, int uid, String lessonType) {
        return mVoaService.chooseJuniorLessonNew(appid, uid, lessonType, 3);
    }

    public Observable<com.iyuba.talkshow.data.model.SeriesResponse> getCategorySeriesList(int uid, String series) {
        String sign = buildSign("series");
        return mVoaService.getCategorySeries("category", series, uid, App.APP_ID, sign, "json");
    }

    //开屏广告
    public Observable<List<GetAdResponse1>> getAd1() {
        return mAdService.getAd1(App.APP_ID, AdService.GetAd1.Param.Value.FLAG);
    }

    //banner广告
    public Observable<List<GetAdResponse1>> getAd4Uid(int uid) {
        return mAdService.getAdByUid(uid, App.APP_ID, 4);
    }

    //获取钱包的历史记录
    public Observable<BaseBean_data<List<Reward_history>>> getWalletHistory(int pageIndex, int pageCount, int userId){
        String url = "http://api."+Constant.Web.WEB_SUFFIX+"credits/getuseractionrecord.jsp";
        String sign = SignUtil.getRewardHistorySign(userId);
        return mFixService.getWalletHistory(url,userId,pageIndex,pageCount,sign);
    }

    //提交听力学习报告
    public Observable<Study_listen_report> submitListenStudyReport(int userId,long startTime,long endTime,String lessonId,boolean isEnd,int studyWords,int studySentenceIndex){
        String url = "http://daxue."+Constant.Web.WEB_SUFFIX+"ecollege/updateStudyRecordNew.jsp";

        int endFlag = 0;
        if (isEnd){
            endFlag = 1;
        }


        String format = "json";
        String platform = "android";
        int appId = App.APP_ID;
        String appName = App.APP_NAME_EN;
        String appNameEncode = EncodeUtil.encode(appName);
        String userAnswer = "";
        String score = "0";
        String testMode = "1";
        String sign = SignUtil.getListenStudyReportSign(userId,DateUtil.toDateStr(startTime,DateUtil.YMD));
        //奖励标志
        int rewardVersion = 1;

        //转换时间
        String startTimeStr = EncodeUtil.encode(DateUtil.toDateStr(startTime,DateUtil.YMDHMS));
        String endTimeStr = EncodeUtil.encode(DateUtil.toDateStr(endTime,DateUtil.YMDHMS));

        return mFixService.submitListenStudyReport(
                url,
                format,
                platform,
                appName,
                appNameEncode,
                appId,
                startTimeStr,
                endTimeStr,
                endFlag,
                lessonId,
                String.valueOf(studySentenceIndex),
                String.valueOf(studyWords),
                testMode,
                userAnswer,
                score,
                "",
                userId,
                sign,
                rewardVersion);
    }
}
