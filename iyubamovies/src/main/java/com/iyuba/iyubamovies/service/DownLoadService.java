package com.iyuba.iyubamovies.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.dlex.bizs.DLTaskInfo;
import com.iyuba.dlex.interfaces.IDListener;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.iyubamovies.model.OneSerisesData;

import java.io.File;
import java.util.List;

/**
 * Created by iyuba on 2018/1/24.
 */

public class DownLoadService extends Service {

    private DLManager dlManager;
    private ImoviesDatabaseManager dbManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dlManager = IMoviesApp.getDlManager();
        dbManager = ImoviesDatabaseManager.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("运行了","onStartCommand");
        String cmd = "";
        if(intent!=null){
            cmd = intent.getStringExtra("cmd");
        }
        if("start".equals(cmd)) {
            List<OneSerisesData> datas = dbManager.getAllSerisesByDownLoadState();
            for (final OneSerisesData data : datas) {
                DLTaskInfo info = dlManager.getDLTaskInfo("imovies" + data.getId() + data.getSerisesid() + data.getType());
                if (info != null) {
                    switch (info.state) {
                        case DLTaskInfo.TaskState.INIT:
                            break;
                        case DLTaskInfo.TaskState.WAITING:
                            break;
                        case DLTaskInfo.TaskState.PREPARING:
                        case DLTaskInfo.TaskState.DOWNLOADING:
                            break;
                        case DLTaskInfo.TaskState.ERROR:
                            dlManager.resumeTask(info);
                            info.setDListener(new DLListener(data));
                            break;
                        case DLTaskInfo.TaskState.PAUSING:
                            break;
                        case DLTaskInfo.TaskState.COMPLETED:
                            if (info.isFileExist()) {
                                if (!"1".equals(data.getIsFinishDownLoad())) {
                                    data.setIsDownload("1");
                                    data.setIsFinishDownLoad("1");
                                    dbManager.saveSerise(data);
                                }
                            } else {
                                dlManager.cancelTask(info);
                                data.setIsFinishDownLoad("0");
                                data.setIsDownload("0");
                                dbManager.saveSerise(data);
                            }
                            break;
                        default:
                            break;

                    }
                }else {
                    data.setIsFinishDownLoad("0");
                    data.setIsDownload("0");
                    dbManager.saveSerise(data);
                }
            }
        }else if("pause".equals(cmd)){
            dlManager.stopAllTasks();
        }

        return super.onStartCommand(intent, flags, startId);
    }
    class DLListener implements IDListener{
        private OneSerisesData data;
        public DLListener(OneSerisesData data){
            this.data = data;
        }
        @Override
        public void onPrepare() {

        }

        @Override
        public void onStart(String fileName, String realUrl, int fileLength) {

        }

        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onStop(int progress) {

        }

        @Override
        public void onFinish(File file) {
            data.setIsFinishDownLoad("1");
            data.setIsDownload("1");
            dbManager.saveSerise(data);
        }

        @Override
        public void onError(int status, String error) {

        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.e("运行了","onDestroy");
    }
}
