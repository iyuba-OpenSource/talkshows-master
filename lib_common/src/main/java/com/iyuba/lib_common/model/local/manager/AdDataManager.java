//package com.iyuba.lib_common.model.local.manager;
//
//import com.iyuba.lib_common.model.local.RoomDB;
//import com.iyuba.lib_common.model.local.entity.AdOperationHistoryEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 广告的本地操作类
// */
//public class AdDataManager {
//
//    /****************************广告历史记录****************************/
//    //保存单个数据
//    public static void saveSingleAdHistoryData(AdOperationHistoryEntity entity) {
//        RoomDB.getInstance().getAdOperationHistoryDao().saveSingleData(entity);
//    }
//
//    //获取特定日期和未上传的广告记录(日期模糊查询)
//    public static List<AdOperationHistoryEntity> getAdHistoryDataByTime(String showTime,String upState) {
//        return RoomDB.getInstance().getAdOperationHistoryDao().getDataByTimeAndUploadStatus(showTime, upState);
//    }
//
//    //获取特定日期和特定状态的广告记录，并设置限定条数(日期模糊查询)
//    public static List<AdOperationHistoryEntity> getAdHistoryDataByTime(String showTime,String upState,int limitCount) {
//        return RoomDB.getInstance().getAdOperationHistoryDao().getDataByTimeAndUploadStatus(showTime, upState,limitCount);
//    }
//
//    //更新当前日期未上传的广告记录为已上传
//    public static void updateAdHistoryUpState(List<AdOperationHistoryEntity> list,String upState) {
//        if (list==null||list.size()==0){
//            return;
//        }
//
//        List<AdOperationHistoryEntity> updateList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            AdOperationHistoryEntity entity = list.get(i);
//            entity.uploadState = upState;
//            updateList.add(entity);
//        }
//        RoomDB.getInstance().getAdOperationHistoryDao().saveMultiData(updateList);
//    }
//}
