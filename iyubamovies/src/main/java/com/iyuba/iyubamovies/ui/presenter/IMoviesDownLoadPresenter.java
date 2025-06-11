package com.iyuba.iyubamovies.ui.presenter;

import android.util.Log;

import com.iyuba.dlex.bizs.DLTaskInfo;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.iyubamovies.model.OneSerisesData;
import com.iyuba.iyubamovies.ui.view.download.DownLoadViewInf;
import com.iyuba.iyubamovies.util.ImoviesNetworkDataConvert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyuba on 2018/1/24.
 */

public class IMoviesDownLoadPresenter {
    private DownLoadViewInf downLoadViewInf;
    private List<Object>objects;
    private List<Integer> checkpos;
    private List<Object> delets;
    private ImoviesDatabaseManager databaseManager;
    public IMoviesDownLoadPresenter(DownLoadViewInf downLoadViewInf){
        this.downLoadViewInf = downLoadViewInf;
        objects = new ArrayList<>();
        checkpos = new ArrayList<>();
        delets = new ArrayList<>();
        databaseManager = ImoviesDatabaseManager.getInstance();
    }
    public void getDownLoadData(){
        objects = ImoviesNetworkDataConvert.SerisesconvertObjs(databaseManager.getAllSerisesByDownLoadState());
        downLoadViewInf.setDownLoadListItems(objects);
    }
    public void addCheckLisener(Integer postion){
        if(!checkpos.contains(postion))
        checkpos.add(postion);
    }
    public void removeCheck(Integer position){
        if(checkpos.contains(position))
            checkpos.remove(position);
    }
    public void clearChecks(){
        checkpos.clear();
    }
    public void onDownLoadedItemClick(int postion){
        if(postion<objects.size()){
            OneSerisesData data = (OneSerisesData) objects.get(postion);
            if(data!=null)
                downLoadViewInf.onDownLoadedItem(data);
        }
    }
    public void deleteDatas(){
        showChecks();
        for(Integer pos:checkpos){
            if(pos<objects.size()){
               OneSerisesData data = (OneSerisesData) objects.get(pos);
                delets.add(data);
                delete(data);
            }
        }
        downLoadViewInf.showDialog("文件正在删除中...");
        for (Object obj:delets){
            objects.remove(obj);
        }
        delets.clear();
        checkpos.clear();
        downLoadViewInf.dismissdiloag();
        downLoadViewInf.setDownLoadListItems(objects);
    }
    public void showChecks(){
        for (Integer integer:checkpos){
            Log.e("checks",integer+"");
        }
    }
    private void delete(final OneSerisesData data){
        if(data!=null){
            DLTaskInfo taskInfo = IMoviesApp.getDlManager().getDLTaskInfo("imovies" + data.getId() + data.getSerisesid() + data.getType());
            if (taskInfo!=null){
                data.setIsDownload("0");
                data.setIsFinishDownLoad("0");
                ImoviesDatabaseManager.getInstance().saveSerise(data);
                Log.e("数据库","更新信息了！");
                new Thread(new DelteFlieRunnable(taskInfo.getDownloadFile())).start();
                IMoviesApp.getDlManager().cancelTask(taskInfo,true);
            }
        }
    }
    class DelteFlieRunnable implements Runnable{

        private File file;
        public DelteFlieRunnable(File file){
            this.file = file;
        }
        @Override
        public void run() {
            if(file!=null){
                try {
                    file.delete();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}
