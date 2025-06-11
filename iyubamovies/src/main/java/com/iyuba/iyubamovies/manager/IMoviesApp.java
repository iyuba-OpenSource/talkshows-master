package com.iyuba.iyubamovies.manager;

import android.content.Context;
import android.content.Intent;

import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.service.DownLoadService;

/**
 * Created by iyuba on 2017/12/22.
 */

public class IMoviesApp {
    private static IMoviesApp app;
    private static Context appliacation;
    private static DLManager dlManager;
    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
//            }
//        });
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
//            }
//        });
    }
    private IMoviesApp(){

    }
    public static synchronized IMoviesApp getInstance(){
        if(app==null)
        {
            app = new IMoviesApp();
        }
        return app;
    }
    public static void init(Context appcontext){
        appliacation = appcontext;
        ImoviesDatabaseManager.init(appcontext);
        IMoviesConstant.FILE_PATH = appcontext.getExternalFilesDir(null).getAbsolutePath() + "/imoviesdl";
        dlManager = DLManager.getInstance();
        appliacation.startService(new Intent(appliacation,DownLoadService.class));
    }
    public static void setUser(String userid,String vipstauts){
        IMoviesConstant.UserId = userid;
        IMoviesConstant.Vipstatus = vipstauts;
    }
    public static void setAppID(String appID){
        IMoviesConstant.App_id = appID;
    }
    public static void setUserID(String userID){
        IMoviesConstant.UserId = userID;
    }
    public static void setVipStauts(String vipStauts){
        IMoviesConstant.Vipstatus = vipStauts;
    }
    public static void setAppName(String appName){
        IMoviesConstant.App_Name = appName;
    }
    public static void setVIP_CENTER_ACTION(String vip_center_action){
        IMoviesConstant.VIPCENTER_ACTION = vip_center_action;
    }
    public static void clearUser(){
        IMoviesConstant.UserId = "0";
        IMoviesConstant.Vipstatus = "0";
    }
    public static void stopService(){
        appliacation.stopService(new Intent(appliacation,DownLoadService.class));
    }
    public static DLManager getDlManager(){
        return dlManager;
    }
    public static Context getAppliacationContext(){
        return appliacation;
    }
}
