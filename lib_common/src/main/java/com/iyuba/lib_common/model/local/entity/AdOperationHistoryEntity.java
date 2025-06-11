//package com.iyuba.lib_common.model.local.entity;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//
//@Entity
//public class AdOperationHistoryEntity {
//
//    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    public int id;
//
//    public String showType;//显示的类型
//
//    public String adType;//广告的类型
//    public String adKey;//广告的key
//
//    public String operationType;//操作类型
//    public String operationTime;//操作时间（yMd Hmss）
//
//    public String uploadState;//上传状态
//
//    public AdOperationHistoryEntity() {
//    }
//
//    @Ignore
//    public AdOperationHistoryEntity(String showType, String adType, String adKey, String operationType, String operationTime, String uploadState) {
//        this.showType = showType;
//        this.adType = adType;
//        this.adKey = adKey;
//        this.operationType = operationType;
//        this.operationTime = operationTime;
//        this.uploadState = uploadState;
//    }
//}
