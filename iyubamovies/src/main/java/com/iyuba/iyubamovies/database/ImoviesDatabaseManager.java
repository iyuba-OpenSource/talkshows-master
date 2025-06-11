package com.iyuba.iyubamovies.database;

import android.content.Context;

import com.iyuba.iyubamovies.database.opinf.ImoviesAgreeCountInf;
import com.iyuba.iyubamovies.database.opinf.ImoviesDetailInf;
import com.iyuba.iyubamovies.database.opinf.ImoviesListInf;
import com.iyuba.iyubamovies.database.opinf.ImoviesOneDataInf;
import com.iyuba.iyubamovies.model.ImoviesList;
import com.iyuba.iyubamovies.model.OneSerisesData;
import com.iyuba.iyubamovies.network.result.ImoviesDetailData;

import java.util.List;

/**
 * Created by iyuba on 2017/12/23.
 */

public class ImoviesDatabaseManager implements ImoviesListInf,
        ImoviesOneDataInf,ImoviesDetailInf,ImoviesAgreeCountInf{

    private static ImoviesDatabaseManager instance;
    private static ImoviesDbhelper dbhelper;
    private static Context context;
    private static ImoviesListOp listOp;
    private static ImoviesOneSerisesOp serisesOp;
    private static ImoviesDetailOp imoviesDetailOp;
    private static ImoviesAgreeCountsOp imoviesAgreeCountsOp;
    private ImoviesDatabaseManager(){

    }
    public static void init(Context mcontext){
        context = mcontext;
        dbhelper = new ImoviesDbhelper(context);
        listOp = new ImoviesListOp(dbhelper);
        serisesOp = new ImoviesOneSerisesOp(dbhelper);
        imoviesDetailOp = new ImoviesDetailOp(dbhelper);
        imoviesAgreeCountsOp = new ImoviesAgreeCountsOp(dbhelper);
    }

    public static synchronized ImoviesDatabaseManager getInstance() {
        if(instance==null)
            instance = new ImoviesDatabaseManager();
        return instance;
    }

    @Override
    public synchronized void SavaImoviesList(ImoviesList data) {
        listOp.SavaImoviesList(data);
    }

    @Override
    public synchronized void SavaImoviesLists(List<ImoviesList> datas) {
        listOp.SavaImoviesLists(datas);
    }

    @Override
    public synchronized List<ImoviesList> SelectAllImoviesLists() {
        return listOp.SelectAllImoviesLists();
    }

    @Override
    public synchronized ImoviesList SelectImoviesList(String seriesid) {
        return listOp.SelectImoviesList(seriesid);
    }

    @Override
    public synchronized void saveSerises(List<OneSerisesData> datas) {
        serisesOp.saveSerises(datas);
    }

    @Override
    public synchronized List<OneSerisesData> getSerisesBySerisesId(String serisesid) {
        return serisesOp.getSerisesBySerisesId(serisesid);
    }

    @Override
    public synchronized void saveSerise(OneSerisesData data) {
        serisesOp.saveSerise(data);
    }

    @Override
    public synchronized int countofSeriseid(String serisesid) {
        return serisesOp.countofSeriseid(serisesid);
    }

    @Override
    public synchronized void updateSerisesDownloadState(String download,String id,String isfinishdl,String seriseid) {
        serisesOp.updateSerisesDownloadState(download,id,isfinishdl,seriseid);
    }

    @Override
    public synchronized List<OneSerisesData> getAllSerisesByDownLoadState() {
        return serisesOp.getAllSerisesByDownLoadState();
    }

    @Override
    public synchronized void insertImoviesDetails(List<ImoviesDetailData> datas, String id, String seriseid, String type) {
        imoviesDetailOp.insertImoviesDetails(datas,id,seriseid,type);
    }

    @Override
    public synchronized List<ImoviesDetailData> getImoviesDetailsData(String id, String seriseid, String type){
        return imoviesDetailOp.getImoviesDetailsData(id,seriseid,type);
    }

    @Override
    public synchronized boolean isClickZAN(String uid, String id) {
        return imoviesAgreeCountsOp.isClickZAN(uid,id);
    }

    @Override
    public synchronized void savaclickZan(String uid, String id) {
        imoviesAgreeCountsOp.savaclickZan(uid,id);
    }
}
