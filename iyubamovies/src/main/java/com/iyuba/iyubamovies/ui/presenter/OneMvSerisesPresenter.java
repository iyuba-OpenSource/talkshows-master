package com.iyuba.iyubamovies.ui.presenter;

import android.util.Log;

//import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorDBManager;
//import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorPart;
//import com.iyuba.basichdsfavorlibrary.model.UpdateCollectResultXML;
//import com.iyuba.basichdsfavorlibrary.network.FavorHeadlineNetwork;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.dlex.bizs.DLTaskInfo;
import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.iyubamovies.manager.IMoviesConstant;
import com.iyuba.iyubamovies.model.OneSerisesData;
import com.iyuba.iyubamovies.network.CommentReslutToItemsMapper;
import com.iyuba.iyubamovies.network.ImoviesDetailResultToDataMapper;
import com.iyuba.iyubamovies.network.ImoviesNetwork;
import com.iyuba.iyubamovies.network.api.GetCommentListApi;
import com.iyuba.iyubamovies.network.api.GetImoviesByOneApi;
import com.iyuba.iyubamovies.network.api.GetSendCommentApi;
import com.iyuba.iyubamovies.network.result.ImoviesByOneResult;
import com.iyuba.iyubamovies.network.result.ImoviesCommentData;
import com.iyuba.iyubamovies.network.result.ImoviesDetailData;
import com.iyuba.iyubamovies.ui.view.OneMv.OneMvSerisesView;
import com.iyuba.iyubamovies.ui.view.OneMv.OneMvSerisesViewInf;
import com.iyuba.iyubamovies.util.IMoviesMD5;
import com.iyuba.iyubamovies.util.IMoviesSignUtil;
import com.iyuba.module.commonvar.CommonVars;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
//
//import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.COLLECT_GroupName_FLAG;
//import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.GET_COLLECT_NOT_SENTENCE_FLG;
//import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.HDS_COLLECT_TYPE_DEL;
//import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.HDS_COLLECT_TYPE_INSERT;
//import static com.iyuba.iyubamovies.util.ImoviesConvertToCollectData.simplifySeriesData;

/**
 * Created by iyuba on 2018/1/15.
 */

public class OneMvSerisesPresenter {
    private OneMvSerisesViewInf oneMvSerisesViewInf;
    private Subscription subscription;
    private OneSerisesData data;
    private List<ImoviesDetailData> detailDatas;
    private List<ImoviesCommentData>commentDatas;
    private Subscription comment_subscription;
    private int pagecounts = 10;
    private int pagenumber = 1;
    private String voaid="";
    public static String BASICHDS_COLLECT_SEL = "1";
    public static String BASICHDS_COLLECT_NOR = "0";
    public static String BASICHDS_SYN_SEL = "1";
    public static String BASICHDS_SYN_NOR = "0";
    public static String BASICHDS_INSERTFROM="美剧";
    private DLManager dlManager;
    public OneMvSerisesPresenter(OneMvSerisesViewInf oneMvSerisesViewInf){
        this.oneMvSerisesViewInf = oneMvSerisesViewInf;
        detailDatas = new ArrayList<>();
        commentDatas = new ArrayList<>();
        dlManager = IMoviesApp.getDlManager();
    }
    public void getSerisesDataCounts(final String id){
        Log.e("getdata","http://cms."+ CommonVars.domain +"/dataapi/jsp/getTitle.jsp?type=series&id="+id+"&sign="
                +IMoviesSignUtil.getSignOfOneSerisesApi()+"&format=json&total=200");
        List<OneSerisesData>datas = ImoviesDatabaseManager.getInstance().getSerisesBySerisesId(id);
        if(datas!=null&&datas.size()>0&&oneMvSerisesViewInf!=null){
            oneMvSerisesViewInf.setSerisesList(datas);
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.setTotalSerises(datas.size()+"");
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.setClickSerisesViewData(datas.get(0));
        }
        ImoviesNetwork.getImoviesByOneApi().getImoviesbyoneData(GetImoviesByOneApi.type,id,
                IMoviesSignUtil.getSignOfOneSerisesApi(),GetImoviesByOneApi.format,GetImoviesByOneApi.total).
                enqueue(new Callback<ImoviesByOneResult>() {
                    @Override
                    public void onResponse(Call<ImoviesByOneResult> call, Response<ImoviesByOneResult> response) {
                        ImoviesByOneResult result = response.body();
                        if(result!=null&&result.getMessage().equals("success")&&result.getResult()==1&&result.getData()!=null){
                            Log.e("result",""+result.toString());
                            if(result.getTotal()>ImoviesDatabaseManager.getInstance().countofSeriseid(id)){
                                Log.e("result-size",""+result.getTotal()+"---"+ImoviesDatabaseManager.getInstance().countofSeriseid(id)+"---"+id);
                            List<OneSerisesData>datas = result.convertToDatabaseData(id);
                            ImoviesDatabaseManager.getInstance().saveSerises(datas);
                                List<OneSerisesData>datass = ImoviesDatabaseManager.getInstance().getSerisesBySerisesId(id);
                                if(datass!=null&&datass.size()>0&&oneMvSerisesViewInf!=null){
                                    oneMvSerisesViewInf.setSerisesList(datass);
                                    if(oneMvSerisesViewInf!=null)
                                    oneMvSerisesViewInf.setTotalSerises(datass.size()+"");
                                    if(oneMvSerisesViewInf!=null)
                                    oneMvSerisesViewInf.setClickSerisesViewData(datass.get(0));
                                }
                                Log.e("数据有变化","---"+datas.size());
                            }else {
                                Log.e("数据无变化","---");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ImoviesByOneResult> call, Throwable t) {

                        Log.e("onedata_Error",t.toString());
                    }
                });
    }
    public void CollectToPlay(final String seriesid,final String voaid){
        List<OneSerisesData>datas = ImoviesDatabaseManager.getInstance().getSerisesBySerisesId(seriesid);
        if(datas!=null&&datas.size()>0&&oneMvSerisesViewInf!=null){
            oneMvSerisesViewInf.setSerisesList(datas);
            oneMvSerisesViewInf.setTotalSerises(datas.size()+"");
            for(int i=0;i<datas.size();i++){
                if(datas.get(i).getId().equals(voaid)){
                    OneMvSerisesView.curplayposition = i;
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.setClickSerisesViewData(datas.get(i));
                    break;
                }
            }
        }
        else
        ImoviesNetwork.getImoviesByOneApi().getImoviesbyoneData(GetImoviesByOneApi.type,seriesid,
                IMoviesSignUtil.getSignOfOneSerisesApi(),GetImoviesByOneApi.format,GetImoviesByOneApi.total).
                enqueue(new Callback<ImoviesByOneResult>() {
                    @Override
                    public void onResponse(Call<ImoviesByOneResult> call, Response<ImoviesByOneResult> response) {
                        ImoviesByOneResult result = response.body();
                        if(result!=null&&result.getMessage().equals("success")&&result.getResult()==1&&result.getData()!=null){
                            if(result.getTotal()>ImoviesDatabaseManager.getInstance().countofSeriseid(seriesid)){
                                List<OneSerisesData>datas = result.convertToDatabaseData(seriesid);
                                ImoviesDatabaseManager.getInstance().saveSerises(datas);
                                List<OneSerisesData>datass = ImoviesDatabaseManager.getInstance().getSerisesBySerisesId(seriesid);
                                if(datass!=null&&datass.size()>0&&oneMvSerisesViewInf!=null){
                                    oneMvSerisesViewInf.setSerisesList(datass);
                                    if(oneMvSerisesViewInf!=null)
                                    oneMvSerisesViewInf.setTotalSerises(datass.size()+"");
                                    for(int i=0;i<datass.size();i++){
                                        if(datass.get(i).getId().equals(voaid)){
                                            OneMvSerisesView.curplayposition = i;
                                            if(oneMvSerisesViewInf!=null)
                                            oneMvSerisesViewInf.setClickSerisesViewData(datass.get(i));
                                            break;
                                        }
                                    }
                                }
                                Log.e("数据有变化","---"+datas.size());
                            }else {
                                Log.e("数据无变化","---");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ImoviesByOneResult> call, Throwable t) {

                        Log.e("onedata_Error",t.toString());
                    }
                });

    }
    public void getDetailsData(OneSerisesData data){
        if(data==null)
            return;
        this.data = data;
        detailDatas = ImoviesDatabaseManager.getInstance().getImoviesDetailsData(data.getId(),data.getSerisesid(),data.getType());
        if(detailDatas.size()>0){
//            if(ischinese){
//                getMixDetail(1);
//            }else {
//                getMixDetail(0);
//            }
            getMixDetail(0);
        }else {
        unsubscribe();
        Log.e("imvdetails","http://cms."+CommonVars.domain+"/dataapi/jsp/getText.jsp?uid=0&appid=666&type=series&id="+data.getId()+
                "&format=json&sign="+IMoviesMD5.getMD5ofStr("iyuba"+
                IMoviesSignUtil.getCurrentDate()+data.getId()+data.getType()));
        subscription = ImoviesNetwork.getImvDetailApi().getImoviesDetail(IMoviesConstant.UserId,
                IMoviesConstant.App_id,"series",data.getId(),"json", IMoviesMD5.getMD5ofStr("iyuba"+
                        IMoviesSignUtil.getCurrentDate()+data.getId()+data.getType()))
                .map(ImoviesDetailResultToDataMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        }
    }
    Observer<List<ImoviesDetailData>> observer = new Observer<List<ImoviesDetailData>>() {
        @Override
        public void onCompleted() {
            getMixDetail(0);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("detail-error",e.toString());
            getMixDetail(0);
        }

        @Override
        public void onNext(List<ImoviesDetailData> datas) {
            if(datas!=null&&data!=null) {
                ImoviesDatabaseManager.getInstance().insertImoviesDetails(datas,data.getId(),data.getSerisesid(),data.getType());
            }
        }
    };
    private void unsubscribe(){
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
    private void comment_unsubscribe(){
        if(comment_subscription!=null && !comment_subscription.isUnsubscribed()){
            comment_subscription.unsubscribe();
        }
    }
    public void getMixDetail(int mix){
        if(mix == 0) {
            String detail = "\t1:暂无简介";
            if (detailDatas.size() > 0) {
                StringBuilder builder = new StringBuilder();
                for(ImoviesDetailData data:detailDatas){
                    builder.append(data.getParaId()+":"+data.getSentence_cn()+"\n\n");
                }
                detail = builder.toString();
            }
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.setImoviesDetail(detail);
        }else {
            String detail = "1:No brief introduction\n\t暂无简介";
            if (detailDatas.size() > 0) {
                StringBuilder builder = new StringBuilder();
                for(ImoviesDetailData data:detailDatas){
                    builder.append(data.getParaId()+":"+data.getSentence()+"\n"+data.getSentence_cn()+"\n\n");
                }
                detail = builder.toString();
            }
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.setImoviesDetail(detail);
        }
    }
//    private BasicHDsFavorPart uploadBSFavorPart = new BasicHDsFavorPart();
    /*deleted by diao
    Observer<UpdateCollectResultXML> updateCollectObserver = new Observer<UpdateCollectResultXML>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(UpdateCollectResultXML updateCollectResult) {
            if(updateCollectResult.msg.equals("Success")){
                if(oneMvSerisesViewInf!=null)
                uploadBSFavorPart = BasicHDsFavorDBManager.getInstance(oneMvSerisesViewInf.getContext())
                        .queryBasicHDsFavorPart(voaid,IMoviesConstant.UserId,"series");
                uploadBSFavorPart.setSynflg("1");
                if(updateCollectResult.type.equals("insert")){
                    uploadBSFavorPart.setFlag("1");
                    if(oneMvSerisesViewInf!=null)
                    BasicHDsFavorDBManager.getInstance(oneMvSerisesViewInf.getContext())
                            .updateBasicHDsFavorPart(uploadBSFavorPart);
                }else if(updateCollectResult.type.equals("del")){
                    uploadBSFavorPart.setFlag("0");
                    if(oneMvSerisesViewInf!=null)
                    BasicHDsFavorDBManager.getInstance(oneMvSerisesViewInf.getContext()).deleteBasicHDsFavorPart(voaid,IMoviesConstant.UserId,
                            uploadBSFavorPart.getType());
                }
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.showToast("成功同步至服务器!");
            }
        }
    };
    public void collect(OneSerisesData data){
        BasicHDsFavorPart basicInHDsFavorPart = BasicHDsFavorDBManager.getInstance(oneMvSerisesViewInf.getContext())
                .queryBasicHDsFavorPart(voaid,IMoviesConstant.UserId,"series");
            if(basicInHDsFavorPart!=null&&basicInHDsFavorPart.getFlag().equals("1")){
                basicInHDsFavorPart.setFlag("0");
                basicInHDsFavorPart.setSynflg("0");
                if(oneMvSerisesViewInf!=null)
                BasicHDsFavorDBManager.getInstance(oneMvSerisesViewInf.getContext())
                        .updateBasicHDsFavorPart(basicInHDsFavorPart);
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.changeCollectSrc(R.drawable.imovies_collect);
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.showToast("已取消收藏！");
                subscription = FavorHeadlineNetwork.getUpdateCollectApi().updateCollectState(
                        COLLECT_GroupName_FLAG,
                        GET_COLLECT_NOT_SENTENCE_FLG,
                        IMoviesConstant.App_id, IMoviesConstant.UserId,
                        HDS_COLLECT_TYPE_DEL,
                        voaid,
                        GET_COLLECT_NOT_SENTENCE_FLG,
                        "series")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updateCollectObserver);
            }else{//未收藏时，则添加收藏
                if(oneMvSerisesViewInf!=null){
                BasicHDsFavorDBManager.getInstance(oneMvSerisesViewInf.getContext())
                        .insertOrReplaceFavorPart(
                                simplifySeriesData(
                                        data,
                                        IMoviesConstant.UserId,
                                        BASICHDS_SYN_NOR,
                                        BASICHDS_INSERTFROM,
                                        BASICHDS_COLLECT_SEL,
                                        IMoviesSignUtil.getFormatDate()));
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.changeCollectSrc(R.drawable.imovies_collect_select);
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.showToast("已加入收藏！");
                subscription = FavorHeadlineNetwork.getUpdateCollectApi().updateCollectState(
                        COLLECT_GroupName_FLAG,
                        GET_COLLECT_NOT_SENTENCE_FLG,
                        IMoviesConstant.App_id, IMoviesConstant.UserId,
                        HDS_COLLECT_TYPE_INSERT,
                        voaid,
                        GET_COLLECT_NOT_SENTENCE_FLG,
                        "series")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updateCollectObserver);
                }
            }


    }*/
    public void refreshSerisesCount(final String id){
        if(ImoviesNetwork.getImoviesByOneApi().getImoviesbyoneData(GetImoviesByOneApi.type,id,
                IMoviesSignUtil.getSignOfOneSerisesApi(),GetImoviesByOneApi.format,GetImoviesByOneApi.total).isExecuted()){
            ImoviesNetwork.getImoviesByOneApi().getImoviesbyoneData(GetImoviesByOneApi.type,id,
                    IMoviesSignUtil.getSignOfOneSerisesApi(),GetImoviesByOneApi.format,GetImoviesByOneApi.total).cancel();

        }
        ImoviesNetwork.getImoviesByOneApi().getImoviesbyoneData(GetImoviesByOneApi.type,id,
                IMoviesSignUtil.getSignOfOneSerisesApi(),GetImoviesByOneApi.format,GetImoviesByOneApi.total).
                enqueue(new Callback<ImoviesByOneResult>() {
                    @Override
                    public void onResponse(Call<ImoviesByOneResult> call, Response<ImoviesByOneResult> response) {
                        ImoviesByOneResult result = response.body();
                        if(result!=null&&result.getMessage().equals("success")&&result.getResult()==1&&result.getData()!=null){
                            if(result.getTotal()>ImoviesDatabaseManager.getInstance().countofSeriseid(id)){
                                List<OneSerisesData>datas = result.convertToDatabaseData(id);
                                ImoviesDatabaseManager.getInstance().saveSerises(datas);
                                Log.e("数据有变化","---"+datas.size());
                            }else {
                                Log.e("数据无变化","---");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ImoviesByOneResult> call, Throwable t) {

                        Log.e("onedata_Error",t.toString());
                    }
                });
    }
    public void onDestroy(){
        unsubscribe();
        comment_unsubscribe();

        comment_subscription=null;
        oneMvSerisesViewInf=null;
        data=null;
        if(detailDatas!=null)
            detailDatas.clear();
        detailDatas = null;
        if(commentDatas!=null)
            commentDatas.clear();
        commentDatas=null;

        subscription=null;
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    public void getCommentsData(String voaid,String pagenumber,boolean isnew){
        Log.e("getcomments","http://daxue."+CommonVars.domain+"/appApi/UnicomApi?protocol=60001&platform=android&format=json&appName=voa&voaid="
                +voaid+"&pageNumber="+pagenumber+"&pageCounts=20&appid=666");
        comment_unsubscribe();
        if(isnew)
            this.pagenumber = 1;
        this.voaid = voaid;
        comment_subscription = ImoviesNetwork.getCommentListApi().getCommentList(GetCommentListApi.PROTOCOL,GetCommentListApi.PLATFORM,
                GetCommentListApi.FORMAT,GetCommentListApi.APPNAME,voaid,pagenumber+"",
                pagecounts+"",IMoviesConstant.App_id).map(CommentReslutToItemsMapper.getIntence(IMoviesConstant.UserId)).
                subscribeOn(Schedulers.io()).observeOn
                (AndroidSchedulers.mainThread())
                .subscribe(commentResultObserver);
    }
    public void loadMoreData(){
        if(oneMvSerisesViewInf==null)
            return;
        pagenumber++;
        if(pagenumber>CommentReslutToItemsMapper.TotalPage){
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.onrefreshloadmorefinish();
        }else {
            getCommentsData(voaid,pagenumber+"",false);
        }
    }
    public void refreshData(){
        pagenumber=1;
        getCommentsData(voaid,pagenumber+"",false);
    }

    private Observer<List<ImoviesCommentData>> commentResultObserver = new Observer<List<ImoviesCommentData>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            //swipeRefreshLayout.setRefreshing(false);
            if(pagenumber>1)
            {
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.onrefreshloadmorefinish();
                pagenumber--;
            }
            else{
                pagenumber = 1;
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.onrefreshfinish();
            }
            //Toast.makeText(mContext, R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ImoviesCommentData> comments) {
            if( pagenumber==1){
                if(comments!=null){
//                    adapter.setCommentItems(comments);
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.setCommentslistData(comments);
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.onrefreshfinish();
                    if(comments.size()>0){
                        if(oneMvSerisesViewInf!=null)
                        oneMvSerisesViewInf.setCommentSize(comments.get(0).getCommentcounts()+"");
                    }
                    commentDatas.clear();
                    commentDatas.addAll(comments);
                    Log.e("TAG--refresh",comments.size()+"");
                }else {
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.onrefreshfinish();
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.setCommentslistData(new ArrayList<ImoviesCommentData>());
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.showToast("暂无评论");
                    //Toast.makeText(mContext, "暂无评论", Toast.LENGTH_SHORT).show();
                }
                // swipeRefreshLayout.setRefreshing(false);
            }else {
                //adapter.addCommentItems(comments);
                if(comments!=null){
                    if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.addCommentslistData(comments);
                //mcomments.addAll(comments);
                    commentDatas.addAll(comments);
                    if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.onrefreshloadmorefinish();
                }else {
                    if(oneMvSerisesViewInf!=null)
                    oneMvSerisesViewInf.setCommentslistData(new ArrayList<ImoviesCommentData>());
                }
            }

        }
    };
    public void writeComment(String content,int position){
        if(position==-1)
        writeComment(null,content,voaid);
        else {
            if(commentDatas!=null&&position<commentDatas.size()){
                writeComment(commentDatas.get(position),content,voaid);
            }
        }
        if(oneMvSerisesViewInf!=null)
        oneMvSerisesViewInf.showDialog("评论发送中...");
    }

    private void writeComment(ImoviesCommentData comment,String commentcontent,String voaid){

        String content="";
        try {
            content = URLEncoder.encode(URLEncoder.encode(commentcontent, "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            content = commentcontent;
        }
        unsubscribe();

        if(comment==null)
            comment_subscription = ImoviesNetwork.getSendCommentApi().writeComment(GetSendCommentApi.PROTOCAL,
                    GetSendCommentApi.PLATFORM,GetSendCommentApi.FORMAT,"0",GetSendCommentApi.APPNAME,GetSendCommentApi.STAR,
                    IMoviesConstant.UserId,voaid,content,GetSendCommentApi.SHOW).
                    map(CommentReslutToItemsMapper.getIntence(IMoviesConstant.UserId)).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread())
                    .subscribe(writecommentResultObserver);
        else
            comment_subscription = ImoviesNetwork.getSendCommentApi().writetoSBComment(GetSendCommentApi.PROTOCAL,
                    GetSendCommentApi.PLATFORM,GetSendCommentApi.FORMAT,comment.ShuoShuoType,GetSendCommentApi.APPNAME,GetSendCommentApi.STAR,
                    IMoviesConstant.UserId,voaid,content,comment.id,GetSendCommentApi.SHOW).
                    map(CommentReslutToItemsMapper.getIntence(IMoviesConstant.UserId))
                    .subscribeOn(Schedulers.io()).observeOn
                            (AndroidSchedulers.mainThread())
                    .subscribe(writecommentResultObserver);
    }
    private Observer<List<ImoviesCommentData>> writecommentResultObserver = new Observer<List<ImoviesCommentData>>() {
        @Override
        public void onCompleted() {
            //waitingDialog.dismiss();
        }

        @Override
        public void onError(Throwable e) {
//            if(pagenumber>1)
//                Toast.makeText(mContext, R.string.send_comment_failed, Toast.LENGTH_SHORT).show();
//            waitingDialog.dismiss();
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.showToast("发送失败，请稍后重试。");
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.sendUnSuccess();
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.dismissDialog();
        }

        @Override
        public void onNext(List<ImoviesCommentData> comments) {
            if(comments!=null) {
                commentDatas.clear();
                commentDatas.addAll(comments);
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.setCommentslistData(comments);
//                mcomments.clear();
//                mcomments.addAll(comments);
//                adapter.setCommentItems(mcomments);
//                Log.e("write_refresh", comments.size() + "");
               pagenumber = 1;
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.sendSuccess();
//                commentcontent = "";
            }else {
                if(oneMvSerisesViewInf!=null)
                oneMvSerisesViewInf.sendUnSuccess();
            }
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.dismissDialog();
        }
    };

    public void DlSeriseData(OneSerisesData data){
        if(data==null)
            return;
        if(dlManager==null)
            dlManager = IMoviesApp.getDlManager();
        if(dlManager==null)
            return;
        if(!data.getIsDownload().equals("1")){
            data.setIsDownload("1");
            ImoviesDatabaseManager.getInstance().saveSerise(data);
            DLTaskInfo info = new DLTaskInfo();
            info.tag = "imovies"+data.getId()+data.getSerisesid()+data.getType();
            info.filePath=IMoviesConstant.FILE_PATH;
            info.fileName=data.getSerisesid()+"_"+data.getId()+"_"+data.getType()+".mp4";
            info.initalizeUrl(IMoviesConstant.BuildVideoPlayOrDownLoadURL(data.getSerisesid(),data.getId()));
            dlManager.addDownloadTask(info);
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.showToast("已加入下载队列！");
        }else {
            if(oneMvSerisesViewInf!=null)
            oneMvSerisesViewInf.showToast("当前剧集已经加入下载队列");
        }
    }


}
