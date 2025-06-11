//package com.iyuba.lib_common.model.local.dao;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import com.iyuba.lib_common.model.local.entity.AdOperationHistoryEntity;
//
//import java.util.List;
//
//@Dao
//public interface AdOperationHistoryDao {
//
//    //保存单个数据
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void saveSingleData(AdOperationHistoryEntity entity);
//
//    //保存多个数据
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void saveMultiData(List<AdOperationHistoryEntity> list);
//
//    //获取某个时间的全部数据
//    @Query("select * from AdOperationHistoryEntity where operationTime like '%' || :time || '%' order by id desc")
//    List<AdOperationHistoryEntity> getDataByTime(String time);
//
//    //获取某个时间的特定上传状态数据
//    @Query("select * from AdOperationHistoryEntity where operationTime like '%' || :time || '%' and uploadState=:uploadStatus order by id desc")
//    List<AdOperationHistoryEntity> getDataByTimeAndUploadStatus(String time, String uploadStatus);
//
//    //获取某个时间的特定上传状态数据的前10条
//    @Query("select * from AdOperationHistoryEntity where operationTime like '%' || :time || '%' and uploadState=:uploadStatus order by id desc limit :limitCount")
//    List<AdOperationHistoryEntity> getDataByTimeAndUploadStatus(String time, String uploadStatus,int limitCount);
//
//    //修改某个数据的上传状态
//    @Query("update AdOperationHistoryEntity set uploadState=:uploadStatus where id=:id")
//    void updateUploadStatus(String uploadStatus, int id);
//
//    //删除全部数据
//    @Query("delete from AdOperationHistoryEntity")
//    void deleteAllData();
//}
